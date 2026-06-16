package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import model.ArquivoTicket;
import model.ArquivoTicketDAO;
import model.BD;
import model.Categoria;
import model.Mensagem;
import model.MensagemDAO;
import model.Permissao;
import model.Prioridade;
import model.Setor;
import model.Status;
import model.Ticket;
import model.TicketDAO;
import model.TipoOperacaoBD;
import model.Usuario;
import model.UsuarioDAO;

public class GuiCadastroTicket extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private BD bd;
	private Usuario usuarioLogado;
	private Ticket ticket;
	private boolean modoEdicao;

	private JTextField txtTitulo;
	private JTextArea txtDescricao;
	private JComboBox<Setor> cmbSetor;
	private JComboBox<Prioridade> cmbPrioridade;
	private JComboBox<Categoria> cmbCategoria;

	private JLabel lblStatus;
	private JLabel lblDataAbertura;
	private JLabel lblDataFechamento;
	private JLabel lblCriadoPor;
	private JLabel lblRespondidoPor;

	private JButton btnCancelar;
	private JButton btnSalvar;
	private JButton btnAssumir;
	private JButton btnConcluir;
	private JButton btnCancelarTicket;
	private JTextArea txtMensagens;
	private JTextField txtNovaMensagem;
	private JButton btnEnviarMensagem;
	private JScrollPane scrollMensagens;
	private JLabel lblMensagens;
	private JLabel lblNovaMensagem;

	private JList<String> listaAnexos;
	private JScrollPane scrollAnexos;
	private JLabel lblAnexos;
	private JButton btnAnexar;
	private JButton btnBaixarAnexo;
	private ArrayList<ArquivoTicket> arquivosCarregados;
	private ArrayList<ArquivoTicket> anexosPendentes = new ArrayList<>();




	public GuiCadastroTicket(BD bd, Usuario usuarioLogado) {
		this.bd = bd;
		this.usuarioLogado = usuarioLogado;
		this.ticket = null;
		this.modoEdicao = false;

		inicializarComponentes();
	}

	public GuiCadastroTicket(BD bd, Usuario usuarioLogado, Ticket ticket) {
		this.bd = bd;
		this.usuarioLogado = usuarioLogado;
		this.ticket = ticket;
		this.modoEdicao = true;

		inicializarComponentes();
		carregarDados();
	}

	private void inicializarComponentes() {
		setTitle(modoEdicao ? "Detalhes do Ticket #" + ticket.getId() : "Novo Ticket");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		if (modoEdicao) {
			setBounds(100, 100, 830, 700);
		} else {
			setBounds(100, 100, 830, 425);
		}
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblTitulo = new JLabel("Titulo");
		lblTitulo.setBounds(20, 135, 80, 18);
		contentPane.add(lblTitulo);

		txtTitulo = new JTextField();
		txtTitulo.setBounds(20, 155, 430, 28);
		contentPane.add(txtTitulo);

		JLabel lblDescricao = new JLabel("Descricao");
		lblDescricao.setBounds(20, 190, 80, 18);
		contentPane.add(lblDescricao);

		txtDescricao = new JTextArea();
		txtDescricao.setLineWrap(true);
		txtDescricao.setWrapStyleWord(true);
		JScrollPane scrollDescricao = new JScrollPane(txtDescricao);
		scrollDescricao.setBounds(20, 210, 430, 110);
		contentPane.add(scrollDescricao);

		JLabel lblSetor = new JLabel("Setor destino");
		lblSetor.setBounds(20, 70, 120, 18);
		contentPane.add(lblSetor);

		cmbSetor = new JComboBox<>();
		cmbSetor.setModel(new DefaultComboBoxModel<>(Setor.values()));
		cmbSetor.setBounds(20, 90, 175, 26);
		cmbSetor.setSelectedIndex(-1);
		contentPane.add(cmbSetor);

		JLabel lblPrioridade = new JLabel("Prioridade");
		lblPrioridade.setBounds(580, 15, 120, 18);
		contentPane.add(lblPrioridade);

		cmbPrioridade = new JComboBox<>();
		cmbPrioridade.setModel(new DefaultComboBoxModel<>(Prioridade.values()));
		cmbPrioridade.setBounds(580, 34, 175, 26);
		cmbPrioridade.setSelectedIndex(-1);
		contentPane.add(cmbPrioridade);

		JLabel lblCategoria = new JLabel("Categoria");
		lblCategoria.setBounds(580, 66, 120, 18);
		contentPane.add(lblCategoria);

		cmbCategoria = new JComboBox<>();
		cmbCategoria.setModel(new DefaultComboBoxModel<>(Categoria.values()));
		cmbCategoria.setBounds(580, 85, 175, 26);
		cmbCategoria.setSelectedIndex(-1);
		contentPane.add(cmbCategoria);

		JLabel lblStatusFixo = new JLabel("Status:");
		lblStatusFixo.setBounds(20, 43, 90, 18);
		contentPane.add(lblStatusFixo);
		lblStatus = new JLabel("Aberto");
		lblStatus.setBounds(115, 43, 165, 18);
		contentPane.add(lblStatus);

		JLabel lblDataAberturaFixo = new JLabel("Aberto em:");
		lblDataAberturaFixo.setBounds(300, 18, 90, 18);
		contentPane.add(lblDataAberturaFixo);
		lblDataAbertura = new JLabel("-");
		lblDataAbertura.setBounds(395, 18, 165, 18);
		contentPane.add(lblDataAbertura);

		JLabel lblDataFechamentoFixo = new JLabel("Fechado em:");
		lblDataFechamentoFixo.setBounds(300, 43, 100, 18);
		contentPane.add(lblDataFechamentoFixo);
		lblDataFechamento = new JLabel("-");
		lblDataFechamento.setBounds(405, 43, 155, 18);
		contentPane.add(lblDataFechamento);

		JLabel lblCriadoPorFixo = new JLabel("Criado por:");
		lblCriadoPorFixo.setBounds(20, 18, 90, 18);
		contentPane.add(lblCriadoPorFixo);
		lblCriadoPor = new JLabel("-");
		lblCriadoPor.setBounds(115, 18, 165, 18);
		contentPane.add(lblCriadoPor);

		JLabel lblRespondidoPorFixo = new JLabel("Respondido por:");
		lblRespondidoPorFixo.setBounds(300, 70, 110, 18);
		contentPane.add(lblRespondidoPorFixo);
		lblRespondidoPor = new JLabel("-");
		lblRespondidoPor.setBounds(415, 70, 145, 18);
		contentPane.add(lblRespondidoPor);

		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(20, 335, 90, 30);
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				voltar();
			}
		});
		contentPane.add(btnCancelar);

		btnSalvar = new JButton("Salvar");
		btnSalvar.setBounds(354, 335, 96, 30);
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				salvar();
			}
		});
		contentPane.add(btnSalvar);

		btnAssumir = new JButton("Assumir");
		btnAssumir.setBounds(204, 335, 100, 30);
		btnAssumir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				assumir();
			}
		});
		contentPane.add(btnAssumir);

		btnConcluir = new JButton("Concluir");
		btnConcluir.setBounds(204, 335, 100, 30);
		btnConcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				concluir();
			}
		});
		contentPane.add(btnConcluir);

		btnCancelarTicket = new JButton("Cancelar Ticket");
		btnCancelarTicket.setBounds(314, 335, 136, 30);
		btnCancelarTicket.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelarTicket();
			}
		});
		contentPane.add(btnCancelarTicket);

		lblMensagens = new JLabel("Mensagens");
		lblMensagens.setBounds(20, 378, 200, 18);
		contentPane.add(lblMensagens);

		txtMensagens = new JTextArea();
		txtMensagens.setLineWrap(true);
		txtMensagens.setWrapStyleWord(true);
		txtMensagens.setEditable(false);
		scrollMensagens = new JScrollPane(txtMensagens);
		scrollMensagens.setBounds(20, 398, 770, 150);
		contentPane.add(scrollMensagens);

		lblNovaMensagem = new JLabel("Nova mensagem");
		lblNovaMensagem.setBounds(20, 553, 150, 18);
		contentPane.add(lblNovaMensagem);


		txtNovaMensagem = new JTextField();
		txtNovaMensagem.setBounds(20, 573, 680, 28);
		contentPane.add(txtNovaMensagem);
		btnEnviarMensagem = new JButton("Enviar");
		btnEnviarMensagem.setBounds(710, 573, 80, 28);
		btnEnviarMensagem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				enviarMensagem();
			}
		});
		contentPane.add(btnEnviarMensagem);

		lblAnexos = new JLabel("Anexos");
		lblAnexos.setBounds(470, 190, 100, 18);
		contentPane.add(lblAnexos);

		listaAnexos = new JList<>(new DefaultListModel<String>());
		scrollAnexos = new JScrollPane(listaAnexos);
		scrollAnexos.setBounds(470, 210, 320, 110);
		contentPane.add(scrollAnexos);

		btnAnexar = new JButton("Anexar arquivo");
		btnAnexar.setBounds(470, 155, 155, 28);
		btnAnexar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				anexarArquivo();
			}
		});
		contentPane.add(btnAnexar);

		btnBaixarAnexo = new JButton("Baixar");
		btnBaixarAnexo.setBounds(635, 155, 155, 28);
		btnBaixarAnexo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				baixarAnexo();
			}
		});
		contentPane.add(btnBaixarAnexo);

		atualizarVisibilidadeBotoes();
		atualizarVisibilidadeCampos();
	}

	private void atualizarVisibilidadeBotoes() {
		if (!modoEdicao) {
			btnCancelar.setText("Cancelar");
			btnCancelar.setBounds(20, 335, 90, 30);
			btnSalvar.setVisible(true);
			btnAssumir.setVisible(false);
			btnConcluir.setVisible(false);
			btnCancelarTicket.setVisible(false);
			return;
		}

		btnCancelar.setText("Voltar");
		btnCancelar.setBounds(20, 615, 90, 30);
		btnSalvar.setVisible(false);

		Status status = ticket.getStatus();
		boolean encerrado = status == Status.FECHADO || status == Status.CANCELADO;
		boolean podeAssumir = status == Status.ABERTO && temPermissao(Permissao.RESPONDER_TICKET);
		boolean podeConcluir = status == Status.EM_ANDAMENTO && temPermissao(Permissao.FECHAR_TICKET);
		boolean podeCancelar = !encerrado && temPermissao(Permissao.FECHAR_TICKET);

		btnAssumir.setVisible(podeAssumir);
		btnConcluir.setVisible(podeConcluir);
		btnCancelarTicket.setVisible(podeCancelar);
	}

	private void atualizarVisibilidadeCampos() {
		boolean editavel = !modoEdicao;
		txtTitulo.setEditable(editavel);
		txtDescricao.setEditable(editavel);
		cmbSetor.setEnabled(editavel);
		cmbPrioridade.setEnabled(editavel);
		cmbCategoria.setEnabled(editavel);

		boolean fechado = modoEdicao && (ticket.getStatus() == Status.FECHADO || ticket.getStatus() == Status.CANCELADO);

		scrollMensagens.setVisible(modoEdicao);
		lblMensagens.setVisible(modoEdicao);
		boolean podeNovaMensagem = modoEdicao && !fechado;
		lblNovaMensagem.setVisible(podeNovaMensagem);
		txtNovaMensagem.setVisible(podeNovaMensagem);
		btnEnviarMensagem.setVisible(podeNovaMensagem);

		scrollAnexos.setVisible(true);
		lblAnexos.setVisible(true);
		btnAnexar.setVisible(!fechado);
		btnBaixarAnexo.setVisible(modoEdicao);
	}

	private boolean temPermissao(Permissao p) {
		if (usuarioLogado == null || usuarioLogado.getFuncao() == null) {
			return false;
		}
		return usuarioLogado.getFuncao().temPermissao(p);
	}

	private void carregarDados() {
		txtTitulo.setText(ticket.getTitulo());
		txtDescricao.setText(ticket.getDescricao());
		cmbSetor.setSelectedItem(ticket.getSetorDestino());
		cmbPrioridade.setSelectedItem(ticket.getPrioridade());
		cmbCategoria.setSelectedItem(ticket.getCategoria());

		lblStatus.setText(ticket.getStatus().getDescricao());

		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		if (ticket.getDataAbertura() != null) {
			lblDataAbertura.setText(ticket.getDataAbertura().format(formato));
		}
		if (ticket.getDataFechamento() != null) {
			lblDataFechamento.setText(ticket.getDataFechamento().format(formato));
		}

		if (ticket.getCriadoPor() != null) {
			lblCriadoPor.setText(buscarNomeUsuario(ticket.getCriadoPor().getId()));
		}
		if (ticket.getRespondidoPor() != null) {
			lblRespondidoPor.setText(buscarNomeUsuario(ticket.getRespondidoPor().getId()));
		}

		carregarMensagens();
		carregarAnexos();
	}

	private String buscarNomeUsuario(int id) {
		Usuario u = new Usuario();
		u.setId(id);
		UsuarioDAO dao = new UsuarioDAO(u);
		if (dao.localizar()) {
			return u.getNome();
		}
		return String.valueOf(id);
	}

	private void carregarMensagens() {
		if (ticket == null) {
			return;
		}
		
		

		MensagemDAO dao = new MensagemDAO(new Mensagem());
		ArrayList<Mensagem> mensagens = dao.listarPorTicket(ticket);

		StringBuilder sb = new StringBuilder();
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

		for (int i = 0; i < mensagens.size(); i++) {
			Mensagem m = mensagens.get(i);
			sb.append("[").append(m.getDataEnvio().format(formato)).append("] ");
			sb.append(m.getAutor().getNome()).append(":\n");
			sb.append(m.getTexto()).append("\n\n");
		}

		txtMensagens.setText(sb.toString());
	}

	private void enviarMensagem() {
		String texto = txtNovaMensagem.getText().trim();

		if (texto.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Digite uma mensagem.");
			return;
		}

		Mensagem msg = new Mensagem();
		msg.setTicket(ticket);
		msg.setAutor(usuarioLogado);
		msg.setTexto(texto);

		MensagemDAO dao = new MensagemDAO(msg);
		String resultado = dao.atualizar(TipoOperacaoBD.INCLUSAO);

		if (resultado.startsWith("Erro") || resultado.startsWith("Falha")) {
			JOptionPane.showMessageDialog(this, resultado);
			return;
		}

		txtNovaMensagem.setText("");
		carregarMensagens();
	}

	private void carregarAnexos() {
		if (ticket == null) {
			return;
		}

		ArquivoTicketDAO dao = new ArquivoTicketDAO(new ArquivoTicket());
		arquivosCarregados = dao.listarPorTicket(ticket);

		DefaultListModel<String> modelo = new DefaultListModel<>();
		for (int i = 0; i < arquivosCarregados.size(); i++) {
			modelo.addElement(arquivosCarregados.get(i).getNomeArquivo());
		}

		listaAnexos.setModel(modelo);
	}

	private void anexarArquivo() {
		JFileChooser seletor = new JFileChooser();
		if (seletor.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
			return;
		}

		File arquivo = seletor.getSelectedFile();

		if (arquivo.length() > 16 * 1024 * 1024) {
			JOptionPane.showMessageDialog(this, "Arquivo muito grande. O tamanho maximo permitido e 16 MB.");
			return;
		}

		try {
			byte[] conteudo = Files.readAllBytes(arquivo.toPath());

			ArquivoTicket anexo = new ArquivoTicket();
			anexo.setNomeArquivo(arquivo.getName());
			anexo.setTipoArquivo(extensao(arquivo.getName()));
			anexo.setArquivo(conteudo);
			anexo.setDataEnvio(LocalDateTime.now());
			anexo.setEnviadoPor(usuarioLogado);

			if (modoEdicao) {
				anexo.setTicket(ticket);
				ArquivoTicketDAO dao = new ArquivoTicketDAO(anexo);
				String resultado = dao.atualizar(TipoOperacaoBD.INCLUSAO);

				if (resultado.startsWith("Erro") || resultado.startsWith("Falha")) {
					JOptionPane.showMessageDialog(this, resultado);
					return;
				}

				carregarAnexos();
			} else {
				anexosPendentes.add(anexo);
				mostrarAnexosPendentes();
			}
		} catch (Exception erro) {
			JOptionPane.showMessageDialog(this, "Erro ao ler o arquivo: " + erro.getMessage());
		}
	}

	private void mostrarAnexosPendentes() {
		DefaultListModel<String> modelo = new DefaultListModel<>();
		for (int i = 0; i < anexosPendentes.size(); i++) {
			modelo.addElement(anexosPendentes.get(i).getNomeArquivo());
		}
		listaAnexos.setModel(modelo);
	}

	private void baixarAnexo() {
		int indice = listaAnexos.getSelectedIndex();
		if (indice < 0) {
			JOptionPane.showMessageDialog(this, "Selecione um anexo para baixar.");
			return;
		}

		ArquivoTicket anexo = arquivosCarregados.get(indice);

		JFileChooser seletor = new JFileChooser();
		seletor.setSelectedFile(new File(anexo.getNomeArquivo()));
		if (seletor.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
			return;
		}

		try {
			Files.write(seletor.getSelectedFile().toPath(), anexo.getArquivo());
			JOptionPane.showMessageDialog(this, "Arquivo salvo com sucesso.");
		} catch (Exception erro) {
			JOptionPane.showMessageDialog(this, "Erro ao salvar o arquivo: " + erro.getMessage());
		}
	}

	private String extensao(String nome) {
		int ponto = nome.lastIndexOf('.');
		if (ponto < 0) {
			return "";
		}
		return nome.substring(ponto + 1);
	}


	private void voltar() {
		dispose();
		GuiListarTickets tela = new GuiListarTickets(bd, usuarioLogado);
		tela.setVisible(true);
	}

	private void salvar() {
		if (txtTitulo.getText().trim().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Informe o titulo.");
			return;
		}

		if (txtTitulo.getText().trim().length() > 200) {
			JOptionPane.showMessageDialog(this, "O titulo deve ter no maximo 200 caracteres.");
			return;
		}

		if (txtDescricao.getText().trim().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Informe a descricao.");
			return;
		}

		if (cmbSetor.getSelectedItem() == null) {
			JOptionPane.showMessageDialog(this, "Selecione o setor destino.");
			return;
		}

		if (cmbPrioridade.getSelectedItem() == null) {
			JOptionPane.showMessageDialog(this, "Selecione a prioridade.");
			return;
		}

		if (cmbCategoria.getSelectedItem() == null) {
			JOptionPane.showMessageDialog(this, "Selecione a categoria.");
			return;
		}

		if (!temPermissao(Permissao.ABRIR_TICKET)) {
			JOptionPane.showMessageDialog(this, "Sem permissao para abrir tickets.");
			return;
		}

		Ticket novo = new Ticket();
		novo.setTitulo(txtTitulo.getText().trim());
		novo.setDescricao(txtDescricao.getText().trim());
		novo.setSetorDestino((Setor) cmbSetor.getSelectedItem());
		novo.setPrioridade((Prioridade) cmbPrioridade.getSelectedItem());
		novo.setCategoria((Categoria) cmbCategoria.getSelectedItem());
		novo.setStatus(Status.ABERTO);
		novo.setDataAbertura(LocalDateTime.now());
		novo.setDataFechamento(null);
		novo.setCriadoPor(usuarioLogado);
		novo.setRespondidoPor(null);

		TicketDAO dao = new TicketDAO(novo);
		String msg = dao.atualizar(TipoOperacaoBD.INCLUSAO);

		if (!msg.startsWith("Erro") && !msg.startsWith("Falha")) {
			for (int i = 0; i < anexosPendentes.size(); i++) {
				ArquivoTicket anexo = anexosPendentes.get(i);
				anexo.setTicket(novo);
				new ArquivoTicketDAO(anexo).atualizar(TipoOperacaoBD.INCLUSAO);
			}
			JOptionPane.showMessageDialog(this, msg);
			voltar();
		} else {
			JOptionPane.showMessageDialog(this, msg);
		}
	}

	private void assumir() {
		int resposta = JOptionPane.showConfirmDialog(
			this,
			"Assumir este ticket para atendimento?",
			"Confirmacao",
			JOptionPane.YES_NO_OPTION
		);
		if (resposta != JOptionPane.YES_OPTION) {
			return;
		}

		ticket.setStatus(Status.EM_ANDAMENTO);
		ticket.setRespondidoPor(usuarioLogado);

		TicketDAO dao = new TicketDAO(ticket);
		String msg = dao.atualizar(TipoOperacaoBD.ALTERACAO);
		JOptionPane.showMessageDialog(this, msg);

		if (!msg.startsWith("Erro") && !msg.startsWith("Falha")) {
			lblStatus.setText(ticket.getStatus().getDescricao());
			lblRespondidoPor.setText(usuarioLogado.getNome());
			atualizarVisibilidadeBotoes();
			atualizarVisibilidadeCampos();
		}
	}

	private void concluir() {
		int resposta = JOptionPane.showConfirmDialog(
			this,
			"Concluir este ticket?",
			"Confirmacao",
			JOptionPane.YES_NO_OPTION
		);
		if (resposta != JOptionPane.YES_OPTION) {
			return;
		}

		ticket.setStatus(Status.FECHADO);
		ticket.setDataFechamento(LocalDateTime.now());

		if (ticket.getRespondidoPor() == null) {
			ticket.setRespondidoPor(usuarioLogado);
		}

		TicketDAO dao = new TicketDAO(ticket);
		String msg = dao.atualizar(TipoOperacaoBD.ALTERACAO);
		JOptionPane.showMessageDialog(this, msg);

		if (!msg.startsWith("Erro") && !msg.startsWith("Falha")) {
			voltar();
		}
	}

	private void cancelarTicket() {
		int resposta = JOptionPane.showConfirmDialog(
			this,
			"Cancelar este ticket? Esta acao nao pode ser desfeita.",
			"Confirmacao",
			JOptionPane.YES_NO_OPTION
		);
		if (resposta != JOptionPane.YES_OPTION) {
			return;
		}

		ticket.setStatus(Status.CANCELADO);
		ticket.setDataFechamento(LocalDateTime.now());

		TicketDAO dao = new TicketDAO(ticket);
		String msg = dao.atualizar(TipoOperacaoBD.ALTERACAO);
		JOptionPane.showMessageDialog(this, msg);

		if (!msg.startsWith("Erro") && !msg.startsWith("Falha")) {
			voltar();
		}
	}
}


