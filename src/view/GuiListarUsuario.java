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
import java.text.SimpleDateFormat;
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

	public GuiListarUsuario(BD bd) {
		this.bd = bd;
		inicializarComponentes();
	}
	
	private void inicializarComponentes() {
		setTitle("Usuario");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 810, 380);
		setLocationRelativeTo(null);
		
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
	
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(27, 50, 744, 280);
		contentPane.add(scrollPane);		

		modeloTabela = new DefaultTableModel(
			new Object[][] {},
			new String[] { "ID", "Nome", "E-mail", "Setor", "Fun\u00E7\u00E3o", "Data cria\u00E7\u00E3o" }
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
		btnAdicionar.setToolTipText("Adicionar");
		btnAdicionar.setBounds(664, 10, 107, 30);
		btnAdicionar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				adicionar();
			}
		});
		contentPane.add(btnAdicionar);
		
		btnVoltar = new JButton("Voltar");
		btnVoltar.setToolTipText("Voltar");
		btnVoltar.setBounds(27, 10, 107, 30);
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
        GuiCadastroUsuario tela = new GuiCadastroUsuario(this.bd);
        tela.setVisible(true);
	}
	
	private void voltar() {
	    dispose();
	    GuiLogin tela = new GuiLogin(bd);
	    tela.setVisible(true);
	}
	
	private void obterDados() {
		try {
			UsuarioDAO dao = new UsuarioDAO(this.bd);
			ArrayList<Usuario> lista = dao.listar();
			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

			modeloTabela.setRowCount(0);

			for (int i = 0; i < lista.size(); i++) {
				Usuario usuario = lista.get(i);

				modeloTabela.addRow(new Object[] {
					usuario.getId(),
					usuario.getNome(),
					usuario.getEmail(),
				    usuario.getSetor() != null ? usuario.getSetor().getDescricao() : "",
					usuario.getFuncao() != null ? usuario.getFuncao().getNome() : "",
					formato.format(usuario.getDataCadastro())
				});
			}
		} catch (Exception erro) {
			JOptionPane.showMessageDialog(null, "Erro ao atualizar a tabela: " + erro.getMessage());
		}
	}
	
	private void editarUsuarioSelecionado() {
		int linha = table.rowAtPoint(table.getMousePosition());

	    if (linha < 0) {
	        return;
	    }

	    int idUsuario = (Integer) modeloTabela.getValueAt(linha, 0);
	    UsuarioDAO dao = new UsuarioDAO(bd);
	    Usuario usuario = dao.localizar(idUsuario);

	    if (usuario != null) {
	        dispose();
	        GuiCadastroUsuario tela = new GuiCadastroUsuario(bd, usuario);
	        tela.setVisible(true);
	    }
	}
}
