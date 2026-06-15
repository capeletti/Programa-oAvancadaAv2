package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JComboBox;

import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;

import model.BD;
import model.Funcao;
import model.FuncaoDAO;
import model.Setor;
import model.TipoOperacaoBD;
import model.Usuario;
import model.UsuarioDAO;

import javax.swing.JButton;

public class GuiCadastroUsuario extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtNome;
	private JTextField txtEmail;
	private JPasswordField txtSenha;
	private BD bd;
	private Usuario usuario;
	private JButton btnSalvar;
	private JButton btnExcluir;
	private JButton btnCancelar;
	private JComboBox<Setor> cmbSetor;
	private JComboBox<Funcao> cmbFuncao;
	private boolean modoEdicao;
	private UsuarioDAO dao;
	private JLabel lblInfoSenha;
	private Usuario usuarioLogado;

	public GuiCadastroUsuario(BD bd, Usuario usuarioLogado) {

	    this.bd = bd;
	    this.usuario = null;
	    this.modoEdicao = false;
	    this.usuarioLogado = usuarioLogado;

	    inicializarComponentes();

	    btnExcluir.setVisible(modoEdicao);
	    lblInfoSenha.setVisible(modoEdicao);
	}

	public GuiCadastroUsuario(BD bd, Usuario usuarioLogado, Usuario usuario) {

	    this.bd = bd;
	    this.usuario = usuario;
	    this.modoEdicao = true;
	    this.usuarioLogado = usuarioLogado;

	    inicializarComponentes();

	    carregarDados();

	    btnExcluir.setVisible(modoEdicao);
	    lblInfoSenha.setVisible(modoEdicao);
	}

	private void inicializarComponentes() {
		setTitle(modoEdicao ? "Editar Usuário" : "Cadastrar Usuário");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100,100,450,265);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5,5,5,5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNome = new JLabel("Nome");
		lblNome.setBounds(10,25,44,12);
		contentPane.add(lblNome);

		txtNome = new JTextField();
		txtNome.setBounds(52,20,374,24);
		contentPane.add(txtNome);

		JLabel lblEmail = new JLabel("E-mail");
		lblEmail.setBounds(10,69,44,12);
		contentPane.add(lblEmail);

		txtEmail = new JTextField();
		txtEmail.setBounds(52,64,374,24);
		contentPane.add(txtEmail);

		JLabel lblSenha = new JLabel("Senha");
		lblSenha.setBounds(10,110,44,12);
		contentPane.add(lblSenha);

		txtSenha = new JPasswordField();
		txtSenha.setBounds(52,105,374,24);
		contentPane.add(txtSenha);

		lblInfoSenha = new JLabel("Deixe em branco para manter a senha atual");
		lblInfoSenha.setBounds(52,128,250,12);
		contentPane.add(lblInfoSenha);

		JLabel lblSetor = new JLabel("Setor");
		lblSetor.setBounds(10,153,44,12);
		contentPane.add(lblSetor);

		cmbSetor = new JComboBox<>();
		cmbSetor.setModel(new DefaultComboBoxModel<>(Setor.values()));
		cmbSetor.setBounds(52,147,157,24);
		contentPane.add(cmbSetor);
		cmbSetor.setSelectedIndex(-1);
		
		JLabel lblFuncao = new JLabel("Função");
		lblFuncao.setBounds(219,153,64,12);
		contentPane.add(lblFuncao);

		cmbFuncao = new JComboBox<>();
		cmbFuncao.setBounds(269,147,157,24);
		contentPane.add(cmbFuncao);

		FuncaoDAO funcaoDAO = new FuncaoDAO(new Funcao());
		ArrayList<Funcao> funcoes = funcaoDAO.listarTodas();

		for(Funcao funcao : funcoes) {
			cmbFuncao.addItem(funcao);
		}
		cmbFuncao.setSelectedIndex(-1);

		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(10,185,95,24);
		btnCancelar.addActionListener(e -> cancelar());
		contentPane.add(btnCancelar);

		btnExcluir = new JButton("Excluir");
		btnExcluir.setBounds(114,185,95,24);
		btnExcluir.addActionListener(e -> excluir());
		contentPane.add(btnExcluir);

		btnSalvar = new JButton("Salvar");
		btnSalvar.setBounds(331,185,95,24);
		btnSalvar.addActionListener(e -> salvar());
		contentPane.add(btnSalvar);

	}

	private void carregarDados() {
	    txtNome.setText(usuario.getNome());
	    txtEmail.setText(usuario.getEmail());
	    cmbSetor.setSelectedItem(usuario.getSetor());
	    cmbFuncao.setSelectedItem(usuario.getFuncao());
	    txtSenha.setText("");
	}

	private void cancelar() {
	    dispose();
	    GuiListarUsuario tela = new GuiListarUsuario(bd, usuarioLogado);
	    tela.setVisible(true);
	}

	private void excluir() {

	    int resposta = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir este usuário?", "Confirmação", JOptionPane.YES_NO_OPTION);

	    if(resposta != JOptionPane.YES_OPTION) {
	        return;
	    }

	    dao = new UsuarioDAO(usuario);

	    String mensagem = dao.atualizar(TipoOperacaoBD.EXCLUSAO);

	    JOptionPane.showMessageDialog(this, mensagem);

	    if(!mensagem.startsWith("Erro")) {
	        cancelar();
	    }
	}

	private void salvar() {
		
		if(txtNome.getText().trim().isEmpty()) {
		    JOptionPane.showMessageDialog(this, "Informe o nome.");
		    return;
		}

		if(txtEmail.getText().trim().isEmpty()) {
		    JOptionPane.showMessageDialog(this, "Informe o e-mail.");
		    return;
		}

		String email =txtEmail.getText().trim();

		if(!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
		    JOptionPane.showMessageDialog(this, "E-mail inválido.");
		    return;
		}

		if(!modoEdicao && txtSenha.getPassword().length == 0) {
		    JOptionPane.showMessageDialog(this, "Informe uma senha.");
		    return;
		}

		if(cmbSetor.getSelectedItem() == null) {
		    JOptionPane.showMessageDialog(this, "Selecione um setor.");
		    return;
		}

		if(cmbFuncao.getSelectedItem() == null) {
		    JOptionPane.showMessageDialog(this, "Selecione uma função.");
		    return;
		}

	    Usuario usuarioTela = montarUsuarioDaTela();
	    dao = new UsuarioDAO(usuarioTela);
	    String mensagem;

	    if(modoEdicao) {
	        mensagem = dao.atualizar( TipoOperacaoBD.ALTERACAO);
	    } else {
	        mensagem = dao.atualizar(TipoOperacaoBD.INCLUSAO);
	    }

	    JOptionPane.showMessageDialog(this, mensagem);

	    if(!mensagem.startsWith("Erro")) {
	        cancelar();
	    }

	}

	private Usuario montarUsuarioDaTela() {
	    if(usuario == null) {
	        usuario = new Usuario();
	        usuario.setDataCadastro(LocalDateTime.now());
	    }
	    
	    usuario.setNome(txtNome.getText().trim());
	    usuario.setEmail(txtEmail.getText().trim());
	    
	    String senha = new String(txtSenha.getPassword());
	    if(!senha.isBlank()) {
	        usuario.setSenha(senha);
	    }
	    
	    usuario.setSetor((Setor)cmbSetor.getSelectedItem());
	    usuario.setFuncao((Funcao)cmbFuncao.getSelectedItem());

	    return usuario;
	}
}