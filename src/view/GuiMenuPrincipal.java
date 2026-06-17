package view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import model.BD;
import model.Permissao;
import model.Usuario;

public class GuiMenuPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private BD bd;
	private Usuario usuarioLogado;

	public GuiMenuPrincipal(BD bd, Usuario usuarioLogado) {
		this.bd = bd;
		this.usuarioLogado = usuarioLogado;
		inicializarComponentes();
	}

	private void inicializarComponentes() {
		setTitle("Menu Principal - MyTicket");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 480, 460);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblTitulo = new JLabel("Sistema MyTicket");
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblTitulo.setBounds(20, 10, 420, 28);
		contentPane.add(lblTitulo);

		String nome = usuarioLogado != null && usuarioLogado.getNome() != null
			? usuarioLogado.getNome() : "Usuario";
		String setor = usuarioLogado != null && usuarioLogado.getSetor() != null
			? usuarioLogado.getSetor().getDescricao() : "-";
		String funcao = usuarioLogado != null && usuarioLogado.getFuncao() != null
			? usuarioLogado.getFuncao().getNome() : "-";

		JLabel lblBemVindo = new JLabel("Bem-vindo, " + nome);
		lblBemVindo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblBemVindo.setBounds(20, 45, 420, 22);
		contentPane.add(lblBemVindo);

		JLabel lblSetor = new JLabel("Setor: " + setor);
		lblSetor.setBounds(20, 72, 420, 18);
		contentPane.add(lblSetor);

		JLabel lblFuncao = new JLabel("Funcao: " + funcao);
		lblFuncao.setBounds(20, 92, 420, 18);
		contentPane.add(lblFuncao);

		JButton btnListarTickets = new JButton("Listar Tickets");
		btnListarTickets.setBounds(20, 130, 420, 36);
		btnListarTickets.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirListarTickets();
			}
		});
		contentPane.add(btnListarTickets);

		JButton btnNovoTicket = new JButton("Cadastrar Ticket");
		btnNovoTicket.setBounds(20, 175, 420, 36);
		btnNovoTicket.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirCadastroTicket();
			}
		});
		contentPane.add(btnNovoTicket);
		btnNovoTicket.setVisible(temPermissao(Permissao.ABRIR_TICKET));

		JButton btnListarUsuarios = new JButton("Listar Usuarios");
		btnListarUsuarios.setBounds(20, 220, 420, 36);
		btnListarUsuarios.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirListarUsuarios();
			}
		});
		contentPane.add(btnListarUsuarios);
		btnListarUsuarios.setVisible(temPermissao(Permissao.CADASTRAR_USUARIO));

		JButton btnListarFuncoes = new JButton("Listar Funcoes");
		btnListarFuncoes.setBounds(20, 265, 420, 36);
		btnListarFuncoes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirListarFuncoes();
			}
		});
		contentPane.add(btnListarFuncoes);
		btnListarFuncoes.setVisible(temPermissao(Permissao.CADASTRAR_FUNCAO));

		JButton btnSair = new JButton("Sair");
		btnSair.setBounds(20, 360, 420, 36);
		btnSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sair();
			}
		});
		contentPane.add(btnSair);
	}

	private boolean temPermissao(Permissao p) {
		if (usuarioLogado == null || usuarioLogado.getFuncao() == null) {
			return false;
		}
		return usuarioLogado.getFuncao().temPermissao(p);
	}

	private void abrirListarTickets() {
		dispose();
		GuiListarTickets tela = new GuiListarTickets(bd, usuarioLogado);
		tela.setVisible(true);
	}

	private void abrirCadastroTicket() {
		if (!temPermissao(Permissao.ABRIR_TICKET)) {
			JOptionPane.showMessageDialog(this, "Sem permissao para abrir tickets.");
			return;
		}
		dispose();
		GuiCadastroTicket tela = new GuiCadastroTicket(bd, usuarioLogado);
		tela.setVisible(true);
	}

	private void abrirListarUsuarios() {
		if (!temPermissao(Permissao.CADASTRAR_USUARIO)) {
			JOptionPane.showMessageDialog(this, "Sem permissao.");
			return;
		}
		dispose();
		GuiListarUsuario tela = new GuiListarUsuario(bd, usuarioLogado);
		tela.setVisible(true);
	}

	private void abrirListarFuncoes() {
		if (!temPermissao(Permissao.CADASTRAR_FUNCAO)) {
			JOptionPane.showMessageDialog(this, "Sem permissao.");
			return;
		}
		dispose();
		GuiListarFuncoes tela = new GuiListarFuncoes(bd, usuarioLogado);
		tela.setVisible(true);
	}

	private void sair() {
		dispose();
		GuiLogin tela = new GuiLogin(bd);
		tela.setVisible(true);
	}
}
