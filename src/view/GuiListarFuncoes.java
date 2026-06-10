package view;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import model.FuncaoDAO;
import model.Funcao;
import model.Permissao;
import model.Usuario;

public class GuiListarFuncoes extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private DefaultTableModel modeloTabela;
	private Usuario usuarioLogado;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GuiListarFuncoes frame = new GuiListarFuncoes(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GuiListarFuncoes(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;

		setTitle("Lista de Funções Cadastradas");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 800, 450);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 20, 744, 280);
		contentPane.add(scrollPane);

		modeloTabela = new DefaultTableModel(
			new Object[][] {},
			new String[] { "ID", "Nome da Função", "Status", "Permissões" }
		) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		table = new JTable(modeloTabela);

		table.getColumnModel().getColumn(0).setPreferredWidth(40);
		table.getColumnModel().getColumn(1).setPreferredWidth(160);
		table.getColumnModel().getColumn(2).setPreferredWidth(60);
		table.getColumnModel().getColumn(3).setPreferredWidth(450);

		scrollPane.setViewportView(table);

		JButton btnNovo = new JButton("NOVA FUNÇÃO");
		btnNovo.setBounds(20, 330, 160, 35);
		contentPane.add(btnNovo);

		JButton btnEditar = new JButton("EDITAR");
		btnEditar.setBounds(214, 330, 160, 35);
		contentPane.add(btnEditar);

		JButton btnAtualizar = new JButton("ATUALIZAR LISTA");
		btnAtualizar.setBounds(408, 330, 160, 35);
		contentPane.add(btnAtualizar);

		JButton btnFechar = new JButton("FECHAR");
		btnFechar.setBounds(604, 330, 160, 35);
		contentPane.add(btnFechar);

		btnNovo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirCadastro();
			}
		});

		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editarFuncao();
			}
		});

		btnAtualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				atualizarLista();
			}
		});

		btnFechar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		atualizarLista();
	}

	public void atualizarLista() {
		try {
			Funcao funcao = new Funcao();
			FuncaoDAO dao = new FuncaoDAO(funcao);
			ArrayList<Funcao> lista = dao.listarTodas();

			modeloTabela.setRowCount(0);

			for (int i = 0; i < lista.size(); i++) {
				Funcao f = lista.get(i);

				String textoPermissoes = "";
				ArrayList<Permissao> perms = f.getPermissoes();
				for (int j = 0; j < perms.size(); j++) {
					textoPermissoes += perms.get(j).name();
					if (j < perms.size() - 1) {
						textoPermissoes += ", ";
					}
				}

				if (textoPermissoes.equals("")) {
					textoPermissoes = "Nenhuma";
				}

				modeloTabela.addRow(new Object[] {
					f.getId(),
					f.getNome(),
					f.isAtivo() ? "Ativo" : "Inativo",
					textoPermissoes
				});
			}

		} catch (Exception erro) {
			JOptionPane.showMessageDialog(null, "Erro ao atualizar a tabela: " + erro.getMessage());
		}
	}

	public void abrirCadastro() {
		GuiCadastroFuncao cadastro = new GuiCadastroFuncao();
		cadastro.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		cadastro.setVisible(true);
	}

	public void editarFuncao() {
		int[] linhas = table.getSelectedRows();

		if (linhas.length == 0) {
			JOptionPane.showMessageDialog(null, "Selecione uma função na tabela para editar.");
			return;
		}

		int linhaSelecionada = linhas[0];
		int idFuncao = (int) modeloTabela.getValueAt(linhaSelecionada, 0);

		Funcao funcao = new Funcao();
		funcao.setId(idFuncao);
		FuncaoDAO dao = new FuncaoDAO(funcao);

		if (dao.localizar(idFuncao)) {
			GuiCadastroFuncao cadastro = new GuiCadastroFuncao(funcao);
			cadastro.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			cadastro.setVisible(true);
		} else {
			JOptionPane.showMessageDialog(null, "Não foi possível carregar os dados da função.");
		}
	}
}