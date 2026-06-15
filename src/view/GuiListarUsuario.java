package view;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import model.BD;
import model.Usuario;
import model.UsuarioDAO;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class GuiListarUsuario extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private DefaultTableModel modeloTabela;
	private JButton btnAdicionar;
	private JButton btnVoltar;
	private BD bd;
	private Usuario usuarioLogado;

	public GuiListarUsuario(BD bd, Usuario usuarioLogado) {
		this.bd = bd;
		this.usuarioLogado = usuarioLogado;
		inicializarComponentes();
	}

	private void inicializarComponentes() {
		setTitle("Usuario");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 810, 380);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5,5,5,5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(27,50,744,280);
		contentPane.add(scrollPane);

		modeloTabela = new DefaultTableModel(
			new Object[][] {},
			new String[] {
				"ID",
				"Nome",
				"E-mail",
				"Setor",
				"Função",
				"Data criação"
			}
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
				if(e.getClickCount() == 2) {
					editarUsuarioSelecionado();
				}
			}
		});

		DefaultTableCellRenderer centralizado = new DefaultTableCellRenderer();

		centralizado.setHorizontalAlignment(SwingConstants.CENTER);

		table.getColumnModel().getColumn(1).setPreferredWidth(300);
		table.getColumnModel().getColumn(1).setCellRenderer(centralizado);
		table.getColumnModel().getColumn(2).setPreferredWidth(200);
		table.getColumnModel().getColumn(3).setPreferredWidth(150);
		table.getColumnModel().getColumn(4).setPreferredWidth(150);
		table.getColumnModel().getColumn(5).setPreferredWidth(120);
		table.getColumnModel().getColumn(5).setCellRenderer(centralizado);
		scrollPane.setViewportView(table);


		btnAdicionar = new JButton("Adicionar");
		btnAdicionar.setBounds(664,10,107,30);
		btnAdicionar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				adicionar();
			}
		});
		contentPane.add(btnAdicionar);

		btnVoltar = new JButton("Voltar");
		btnVoltar.setBounds(27,10,107,30);
		btnVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				voltar();
			}
		});
		contentPane.add(btnVoltar);

		obterDados();
	}

	private void adicionar() {
		dispose();
		GuiCadastroUsuario tela = new GuiCadastroUsuario(this.bd, this.usuarioLogado);
		tela.setVisible(true);
	}

	private void voltar() {
		dispose();
		GuiMenuPrincipal tela = new GuiMenuPrincipal(this.bd, this.usuarioLogado);
		tela.setVisible(true);
	}

	private void obterDados() {
		try {

			Usuario usuarioBase = new Usuario();
			UsuarioDAO dao = new UsuarioDAO(usuarioBase);
			ArrayList<Usuario> lista = dao.listar();
			DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

			modeloTabela.setRowCount(0);

			for(Usuario usuario : lista) {

				modeloTabela.addRow(
					new Object[] {
						usuario.getId(),
						usuario.getNome(),
						usuario.getEmail(),
						usuario.getSetor() != null ? usuario.getSetor().getDescricao() : "",
						usuario.getFuncao() != null ? usuario.getFuncao().getNome() : "",
						usuario.getDataCadastro() != null ? usuario.getDataCadastro().format(formato) : ""
					}
				);
			}
		} catch(Exception erro) {
			JOptionPane.showMessageDialog(null, "Erro ao atualizar a tabela: " + erro.getMessage());
		}
	}

	private void editarUsuarioSelecionado() {

		int linha = table.rowAtPoint(table.getMousePosition());

		if(linha < 0) {
			return;
		}

		int idUsuario = (Integer) modeloTabela.getValueAt(linha,0);
		Usuario usuario = new Usuario();
		usuario.setId(idUsuario);

		UsuarioDAO dao = new UsuarioDAO(usuario);

		if(dao.localizar()) {
			dispose();
			GuiCadastroUsuario tela = new GuiCadastroUsuario(this.bd, this.usuarioLogado, usuario);
			tela.setVisible(true);
		}
	}
}