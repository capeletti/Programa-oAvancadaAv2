package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.TextField;
import java.awt.Label;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;
import java.awt.Button;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GuiCadastroFuncao extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
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

	/**
	 * Create the frame.
	 */
	public GuiCadastroFuncao() {
		setTitle("Cadastro de Funções");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		TextField textFieldIdFuncao = new TextField();
		textFieldIdFuncao.setBounds(125, 15, 52, 20);
		contentPane.add(textFieldIdFuncao);
		
		Label lblNomeFuncao = new Label("Nome da Função:");
		lblNomeFuncao.setBounds(21, 44, 98, 22);
		contentPane.add(lblNomeFuncao);
		
		Label lblStatus = new Label("Status:");
		lblStatus.setBounds(21, 79, 48, 22);
		contentPane.add(lblStatus);
		
		JCheckBox chckbxAtivo = new JCheckBox("Ativo");
		chckbxAtivo.setBounds(122, 77, 68, 23);
		contentPane.add(chckbxAtivo);
		
		buttonSalvar.setBounds(58, 300, 82, 32);
		contentPane.add(buttonSalvar);
		
		Button buttonLimpar = new Button("LIMPAR");

		buttonLimpar.setBounds(249, 300, 76, 32);
		contentPane.add(buttonLimpar);
		
		Button buttonCancelar = new Button("CANCELAR");
		buttonCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonCancelar.setBounds(422, 300, 76, 32);
		contentPane.add(buttonCancelar);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Selecione as Permiss\u00F5es:", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 125, 564, 148);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JCheckBox chckbxCadastrarFuncao = new JCheckBox("Cadastrar Função");
		chckbxCadastrarFuncao.setBounds(410, 41, 136, 23);
		panel.add(chckbxCadastrarFuncao);
		
		JCheckBox chckbxFecharTicket = new JCheckBox("Fechar Ticket");
		chckbxFecharTicket.setBounds(207, 41, 111, 23);
		panel.add(chckbxFecharTicket);
		
		JCheckBox chckbxCadastrarUsuario = new JCheckBox("Cadastrar Usuário");
		chckbxCadastrarUsuario.setBounds(207, 67, 132, 23);
		panel.add(chckbxCadastrarUsuario);
		
		JCheckBox chckbxResponderTicket = new JCheckBox("Responder Ticket");
		chckbxResponderTicket.setBounds(6, 67, 131, 23);
		panel.add(chckbxResponderTicket);
		
		JCheckBox chckbxAbrirTicket = new JCheckBox("Abrir Ticket");
		chckbxAbrirTicket.setBounds(6, 41, 97, 23);
		panel.add(chckbxAbrirTicket);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(10, 11, 564, 337);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		TextField textFieldNomeFuncao = new TextField();
		textFieldNomeFuncao.setBounds(115, 32, 130, 20);
		panel_1.add(textFieldNomeFuncao);
		
		Label lblIdFuncao = new Label("ID da Função");
		lblIdFuncao.setBounds(11, 5, 98, 22);
		panel_1.add(lblIdFuncao);
		
		buttonSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					model.Funcao novaFuncao = new model.Funcao();
					
					novaFuncao.setNome(textFieldNomeFuncao.getText());
					novaFuncao.setAtivo(chckbxAtivo.isSelected());
					
					if (chckbxAbrirTicket.isSelected()) {
						novaFuncao.adicionarPermissao(model.Permissao.ABRIR_TICKET);
					}
					if (chckbxResponderTicket.isSelected()) {
						novaFuncao.adicionarPermissao(model.Permissao.RESPONDER_TICKET);
					}
					if (chckbxFecharTicket.isSelected()) {
						novaFuncao.adicionarPermissao(model.Permissao.FECHAR_TICKET);
					}
					if (chckbxCadastrarUsuario.isSelected()) {
						novaFuncao.adicionarPermissao(model.Permissao.CADASTRAR_USUARIO);
					}
					if (chckbxCadastrarFuncao.isSelected()) {
						novaFuncao.adicionarPermissao(model.Permissao.CADASTRAR_FUNCAO);
					}
					
					javax.swing.JOptionPane.showMessageDialog(null, "Função " + novaFuncao.getNome() + " salva com sucesso!");
					dispose();
					
				} catch (Exception erro) {

					javax.swing.JOptionPane.showMessageDialog(null, "Erro ao salvar a função: " + erro.getMessage());
				}
			}
		});
		
		buttonLimpar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textFieldIdFuncao.setText("");
				textFieldNomeFuncao.setText("");

				chckbxAtivo.setSelected(false);
				chckbxAbrirTicket.setSelected(false);
				chckbxResponderTicket.setSelected(false);
				chckbxFecharTicket.setSelected(false);
				chckbxCadastrarUsuario.setSelected(false);
				chckbxCadastrarFuncao.setSelected(false);
			}
		});
	}
}
