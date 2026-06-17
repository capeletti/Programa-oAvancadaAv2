package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import model.Usuario;
import model.UsuarioDAO;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;

public class GuiLogin extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtEmail;
	private JPasswordField txtSenha;

	public GuiLogin() {
		inicializarComponentes();
	}

	private void inicializarComponentes() {

		setFont(new Font("Tahoma", Font.BOLD, 12));
		setTitle("Tela de Login");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel labelEmail = new JLabel("Email");
		labelEmail.setBounds(100, 42, 95, 13);
		contentPane.add(labelEmail);

		txtEmail = new JTextField();
		txtEmail.setBounds(100, 59, 217, 26);
		txtEmail.setToolTipText("Email");
		txtEmail.setColumns(10);
		contentPane.add(txtEmail);

		JLabel labelSenha = new JLabel("Senha");
		labelSenha.setBounds(100, 95, 95, 13);
		contentPane.add(labelSenha);

		txtSenha = new JPasswordField();
		txtSenha.setBounds(100, 112, 217, 26);
		txtSenha.setToolTipText("Senha");
		txtSenha.setColumns(10);
		contentPane.add(txtSenha);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(100, 148, 95, 21);
		btnCancelar.setToolTipText("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		contentPane.add(btnCancelar);

		JButton btnLogar = new JButton("Logar");
		btnLogar.setBounds(222, 148, 95, 21);
		btnLogar.setToolTipText("Logar");
		btnLogar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logar();
			}
		});
		contentPane.add(btnLogar);
	}
	public void logar() {

		String email = txtEmail.getText().trim();
		String senha = new String(txtSenha.getPassword());

		if(email.isEmpty() || senha.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Informe email e senha.");
			return;
		}

		try {

			Usuario usuarioBase = new Usuario();
			UsuarioDAO usuarioDAO = new UsuarioDAO(usuarioBase);
			Usuario usuario = usuarioDAO.validarLogin(email, senha);

			if(usuario != null) {
				JOptionPane.showMessageDialog(null, "Login realizado com sucesso!");
				dispose();
				GuiMenuPrincipal tela = new GuiMenuPrincipal(usuario);
				tela.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(null, "Email ou senha inválidos!");
			}
		} catch(Exception erro) {
			JOptionPane.showMessageDialog(null, "Erro ao tentar logar: " + erro.getMessage());
		}
	}
}