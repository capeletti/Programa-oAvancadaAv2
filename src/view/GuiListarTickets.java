package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import model.BD;
import model.Permissao;
import model.Ticket;
import model.TicketDAO;
import model.Usuario;

public class GuiListarTickets extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private DefaultTableModel modeloTabela;
	private BD bd;
	private Usuario usuarioLogado;
	private boolean filtroMeuSetor;

	public GuiListarTickets(BD bd, Usuario usuarioLogado) {
		this.bd = bd;
		this.usuarioLogado = usuarioLogado;
		this.filtroMeuSetor = false;
		inicializarComponentes();
	}

	private void inicializarComponentes() {
		setTitle("Lista de Tickets");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 900, 480);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 60, 844, 320);
		contentPane.add(scrollPane);

		modeloTabela = new DefaultTableModel(
			new Object[][] {},
			new String[] { "ID", "Titulo", "Status", "Prioridade", "Categoria", "Setor destino", "Aberto em" }
		) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		table = new JTable(modeloTabela);
		table.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				if (e.getClickCount() == 2) {
					abrirTicketSelecionado();
				}
			}
		});

		DefaultTableCellRenderer centralizado = new DefaultTableCellRenderer();
		centralizado.setHorizontalAlignment(SwingConstants.CENTER);

		table.getColumnModel().getColumn(0).setPreferredWidth(40);
		table.getColumnModel().getColumn(0).setCellRenderer(centralizado);
		table.getColumnModel().getColumn(1).setPreferredWidth(250);
		table.getColumnModel().getColumn(2).setPreferredWidth(110);
		table.getColumnModel().getColumn(2).setCellRenderer(centralizado);
		table.getColumnModel().getColumn(3).setPreferredWidth(90);
		table.getColumnModel().getColumn(3).setCellRenderer(centralizado);
		table.getColumnModel().getColumn(4).setPreferredWidth(110);
		table.getColumnModel().getColumn(4).setCellRenderer(centralizado);
		table.getColumnModel().getColumn(5).setPreferredWidth(110);
		table.getColumnModel().getColumn(5).setCellRenderer(centralizado);
		table.getColumnModel().getColumn(6).setPreferredWidth(90);
		table.getColumnModel().getColumn(6).setCellRenderer(centralizado);

		scrollPane.setViewportView(table);

		JButton btnVoltar = new JButton("Voltar");
		btnVoltar.setBounds(20, 15, 156, 30);
		btnVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				voltar();
			}
		});
		contentPane.add(btnVoltar);

		JButton btnNovo = new JButton("Novo Ticket");
		btnNovo.setBounds(192, 15, 156, 30);
		btnNovo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				novoTicket();
			}
		});
		contentPane.add(btnNovo);
		btnNovo.setVisible(temPermissao(Permissao.ABRIR_TICKET));

		JButton btnProximo = new JButton("Proximo da Fila");
		btnProximo.setBounds(364, 15, 156, 30);
		btnProximo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirProximoDaFila();
			}
		});
		contentPane.add(btnProximo);
		btnProximo.setVisible(temPermissao(Permissao.RESPONDER_TICKET));

		JButton btnFiltro = new JButton("Filtrar: todos");
		btnFiltro.setBounds(536, 15, 156, 30);
		btnFiltro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				filtroMeuSetor = !filtroMeuSetor;
				btnFiltro.setText(filtroMeuSetor ? "Filtrar: meu setor" : "Filtrar: todos");
				atualizarLista();
			}
		});
		contentPane.add(btnFiltro);

		JButton btnAtualizar = new JButton("Atualizar");
		btnAtualizar.setBounds(708, 15, 156, 30);
		btnAtualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				atualizarLista();
			}
		});
		contentPane.add(btnAtualizar);

		atualizarLista();
	}

	private boolean temPermissao(Permissao p) {
		if (usuarioLogado == null || usuarioLogado.getFuncao() == null) {
			return false;
		}
		return usuarioLogado.getFuncao().temPermissao(p);
	}

	private void voltar() {
		dispose();
		GuiMenuPrincipal tela = new GuiMenuPrincipal(bd, usuarioLogado);
		tela.setVisible(true);
	}

	private void novoTicket() {
		dispose();
		GuiCadastroTicket tela = new GuiCadastroTicket(bd, usuarioLogado);
		tela.setVisible(true);
	}

	private void abrirProximoDaFila() {
		if (!temPermissao(Permissao.RESPONDER_TICKET)) {
			JOptionPane.showMessageDialog(this, "Sem permissao para atender tickets.");
			return;
		}

		if (usuarioLogado == null || usuarioLogado.getSetor() == null) {
			JOptionPane.showMessageDialog(this, "Usuario sem setor definido.");
			return;
		}

		TicketDAO dao = new TicketDAO(new Ticket());
		Ticket proximo = dao.proximoDaFila(usuarioLogado.getSetor());

		if (proximo == null) {
			JOptionPane.showMessageDialog(this, "Nao ha tickets na fila do setor " + usuarioLogado.getSetor().getDescricao());
			return;
		}

		dispose();
		GuiCadastroTicket tela = new GuiCadastroTicket(bd, usuarioLogado, proximo);
		tela.setVisible(true);
	}

	private void atualizarLista() {
		try {
			TicketDAO dao = new TicketDAO(new Ticket());
			ArrayList<Ticket> lista;

			if (filtroMeuSetor && usuarioLogado != null && usuarioLogado.getSetor() != null) {
				lista = dao.listarPorSetor(usuarioLogado.getSetor());
			} else {
				lista = dao.listarTodos();
			}

			DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			modeloTabela.setRowCount(0);

			for (int i = 0; i < lista.size(); i++) {
				Ticket t = lista.get(i);
				modeloTabela.addRow(new Object[] {
					t.getId(),
					t.getTitulo(),
					t.getStatus() != null ? t.getStatus().name() : "",
					t.getPrioridade() != null ? t.getPrioridade().name() : "",
					t.getCategoria() != null ? t.getCategoria().name() : "",
					t.getSetorDestino() != null ? t.getSetorDestino().getDescricao() : "",
					t.getDataAbertura() != null ? t.getDataAbertura().format(formato) : ""
				});
			}
		} catch (Exception erro) {
			JOptionPane.showMessageDialog(null, "Erro ao atualizar a tabela: " + erro.getMessage());
		}
	}

	private void abrirTicketSelecionado() {
		int linha = table.getSelectedRow();
		if (linha < 0) {
			return;
		}

		int idTicket = (Integer) modeloTabela.getValueAt(linha, 0);
		Ticket t = new Ticket();
		t.setId(idTicket);
		TicketDAO dao = new TicketDAO(t);

		if (dao.localizar()) {
			dispose();
			GuiCadastroTicket tela = new GuiCadastroTicket(bd, usuarioLogado, t);
			tela.setVisible(true);
		} else {
			JOptionPane.showMessageDialog(this, "Nao foi possivel carregar o ticket.");
		}
	}
}
