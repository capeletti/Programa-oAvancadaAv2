package view;

import java.awt.Button;
import java.awt.EventQueue;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import model.Funcao;
import model.FuncaoDAO;
import model.Permissao;
import model.TipoOperacaoBD;

public class GuiCadastroFuncao extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private int idFuncao;
	private TextField textFieldNomeFuncao;
	private JCheckBox chckbxAtivo;
	private JCheckBox chckbxAbrirTicket;
	private JCheckBox chckbxResponderTicket;
	private JCheckBox chckbxFecharTicket;
	private JCheckBox chckbxCadastrarUsuario;
	private JCheckBox chckbxCadastrarFuncao;

	private final Button buttonSalvar = new Button("SALVAR");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GuiCadastroFuncao frame = new GuiCadastroFuncao();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// Construtor para criar a função
	public GuiCadastroFuncao() {
		idFuncao = 0;
		inicializarComponentes();
	}

	// Construtor para editar a função
	public GuiCadastroFuncao(Funcao funcao) {
		idFuncao = funcao.getId();
		inicializarComponentes();

		textFieldNomeFuncao.setText(funcao.getNome());
		chckbxAtivo.setSelected(funcao.isAtivo());

		chckbxAbrirTicket.setSelected(funcao.temPermissao(Permissao.ABRIR_TICKET));
		chckbxResponderTicket.setSelected(funcao.temPermissao(Permissao.RESPONDER_TICKET));
		chckbxFecharTicket.setSelected(funcao.temPermissao(Permissao.FECHAR_TICKET));
		chckbxCadastrarUsuario.setSelected(funcao.temPermissao(Permissao.CADASTRAR_USUARIO));
		chckbxCadastrarFuncao.setSelected(funcao.temPermissao(Permissao.CADASTRAR_FUNCAO));
	}


	private void inicializarComponentes() {
		setTitle("Cadastro de Funções");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(10, 11, 564, 70);
		contentPane.add(panel_1);
		panel_1.setLayout(null);

		Label lblNomeFuncao = new Label("Nome da Função:");
		lblNomeFuncao.setBounds(11, 10, 98, 22);
		panel_1.add(lblNomeFuncao);

		textFieldNomeFuncao = new TextField();
		textFieldNomeFuncao.setBounds(115, 8, 130, 20);
		panel_1.add(textFieldNomeFuncao);

		Label lblStatus = new Label("Status:");
		lblStatus.setBounds(11, 40, 48, 22);
		panel_1.add(lblStatus);

		chckbxAtivo = new JCheckBox("Ativo");
		chckbxAtivo.setBounds(112, 38, 68, 23);
		panel_1.add(chckbxAtivo);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Selecione as Permissões:", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 90, 564, 148);
		contentPane.add(panel);
		panel.setLayout(null);

		chckbxAbrirTicket = new JCheckBox("Abrir Ticket");
		chckbxAbrirTicket.setBounds(6, 41, 97, 23);
		panel.add(chckbxAbrirTicket);

		chckbxResponderTicket = new JCheckBox("Responder Ticket");
		chckbxResponderTicket.setBounds(6, 67, 131, 23);
		panel.add(chckbxResponderTicket);

		chckbxFecharTicket = new JCheckBox("Fechar Ticket");
		chckbxFecharTicket.setBounds(207, 41, 111, 23);
		panel.add(chckbxFecharTicket);

		chckbxCadastrarUsuario = new JCheckBox("Cadastrar Usuário");
		chckbxCadastrarUsuario.setBounds(207, 67, 132, 23);
		panel.add(chckbxCadastrarUsuario);

		chckbxCadastrarFuncao = new JCheckBox("Cadastrar Função");
		chckbxCadastrarFuncao.setBounds(410, 41, 136, 23);
		panel.add(chckbxCadastrarFuncao);

		buttonSalvar.setBounds(58, 300, 82, 32);
		contentPane.add(buttonSalvar);

		Button buttonLimpar = new Button("LIMPAR");
		buttonLimpar.setBounds(249, 300, 76, 32);
		contentPane.add(buttonLimpar);

		Button buttonCancelar = new Button("CANCELAR");
		buttonCancelar.setBounds(422, 300, 76, 32);
		contentPane.add(buttonCancelar);

		buttonSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				salvar();
			}
		});

		buttonLimpar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				limpar();
			}
		});

		buttonCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}

	public void salvar() {
		if (textFieldNomeFuncao.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Informe o nome da função.");
			return;
		}
		try {
			Funcao novaFuncao = new Funcao();

			novaFuncao.setId(idFuncao);
			novaFuncao.setNome(textFieldNomeFuncao.getText());
			novaFuncao.setAtivo(chckbxAtivo.isSelected());

			if (chckbxAbrirTicket.isSelected()) {
				novaFuncao.adicionarPermissao(Permissao.ABRIR_TICKET);
			}
			if (chckbxResponderTicket.isSelected()) {
				novaFuncao.adicionarPermissao(Permissao.RESPONDER_TICKET);
			}
			if (chckbxFecharTicket.isSelected()) {
				novaFuncao.adicionarPermissao(Permissao.FECHAR_TICKET);
			}
			if (chckbxCadastrarUsuario.isSelected()) {
				novaFuncao.adicionarPermissao(Permissao.CADASTRAR_USUARIO);
			}
			if (chckbxCadastrarFuncao.isSelected()) {
				novaFuncao.adicionarPermissao(Permissao.CADASTRAR_FUNCAO);
			}

			FuncaoDAO dao = new FuncaoDAO(novaFuncao);

			String resultado;
			if (idFuncao == 0) {
				resultado = dao.atualizar(TipoOperacaoBD.INCLUSAO);
			} else {
				resultado = dao.atualizar(TipoOperacaoBD.ALTERACAO);
			}

			JOptionPane.showMessageDialog(null, resultado);
			dispose();

		} catch (Exception erro) {
			JOptionPane.showMessageDialog(null, "Erro ao salvar a função: " + erro.getMessage());
		}
	}

	public void limpar() {
		textFieldNomeFuncao.setText("");
		chckbxAtivo.setSelected(false);
		chckbxAbrirTicket.setSelected(false);
		chckbxResponderTicket.setSelected(false);
		chckbxFecharTicket.setSelected(false);
		chckbxCadastrarUsuario.setSelected(false);
		chckbxCadastrarFuncao.setSelected(false);
	}
}