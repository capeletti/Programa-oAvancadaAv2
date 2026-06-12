package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import model.BD;
import model.Categoria;
import model.Permissao;
import model.Prioridade;
import model.Setor;
import model.Status;
import model.Ticket;
import model.TicketDAO;
import model.TipoOperacaoBD;
import model.Usuario;

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
	private JButton btnResponder;
	private JButton btnFinalizar;

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
		setBounds(100, 100, 600, 540);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblTitulo = new JLabel("Titulo");
		lblTitulo.setBounds(20, 15, 80, 18);
		contentPane.add(lblTitulo);

		txtTitulo = new JTextField();
		txtTitulo.setBounds(20, 34, 550, 28);
		contentPane.add(txtTitulo);

		JLabel lblDescricao = new JLabel("Descricao");
		lblDescricao.setBounds(20, 72, 80, 18);
		contentPane.add(lblDescricao);

		txtDescricao = new JTextArea();
		txtDescricao.setLineWrap(true);
		txtDescricao.setWrapStyleWord(true);
		JScrollPane scrollDescricao = new JScrollPane(txtDescricao);
		scrollDescricao.setBounds(20, 91, 550, 90);
		contentPane.add(scrollDescricao);

		JLabel lblSetor = new JLabel("Setor destino");
		lblSetor.setBounds(20, 192, 120, 18);
		contentPane.add(lblSetor);

		cmbSetor = new JComboBox<>();
		cmbSetor.setModel(new DefaultComboBoxModel<>(Setor.values()));
		cmbSetor.setBounds(20, 211, 175, 26);
		cmbSetor.setSelectedIndex(-1);
		contentPane.add(cmbSetor);

		JLabel lblPrioridade = new JLabel("Prioridade");
		lblPrioridade.setBounds(207, 192, 120, 18);
		contentPane.add(lblPrioridade);

		cmbPrioridade = new JComboBox<>();
		cmbPrioridade.setModel(new DefaultComboBoxModel<>(Prioridade.values()));
		cmbPrioridade.setBounds(207, 211, 175, 26);
		cmbPrioridade.setSelectedIndex(-1);
		contentPane.add(cmbPrioridade);

		JLabel lblCategoria = new JLabel("Categoria");
		lblCategoria.setBounds(395, 192, 120, 18);
		contentPane.add(lblCategoria);

		cmbCategoria = new JComboBox<>();
		cmbCategoria.setModel(new DefaultComboBoxModel<>(Categoria.values()));
		cmbCategoria.setBounds(395, 211, 175, 26);
		cmbCategoria.setSelectedIndex(-1);
		contentPane.add(cmbCategoria);

		JLabel lblStatusFixo = new JLabel("Status:");
		lblStatusFixo.setBounds(20, 260, 60, 18);
		contentPane.add(lblStatusFixo);
		lblStatus = new JLabel("ABERTO");
		lblStatus.setBounds(80, 260, 200, 18);
		contentPane.add(lblStatus);

		JLabel lblDataAberturaFixo = new JLabel("Aberto em:");
		lblDataAberturaFixo.setBounds(20, 285, 100, 18);
		contentPane.add(lblDataAberturaFixo);
		lblDataAbertura = new JLabel("-");
		lblDataAbertura.setBounds(110, 285, 200, 18);
		contentPane.add(lblDataAbertura);

		JLabel lblDataFechamentoFixo = new JLabel("Fechado em:");
		lblDataFechamentoFixo.setBounds(20, 310, 100, 18);
		contentPane.add(lblDataFechamentoFixo);
		lblDataFechamento = new JLabel("-");
		lblDataFechamento.setBounds(110, 310, 200, 18);
		contentPane.add(lblDataFechamento);

		JLabel lblCriadoPorFixo = new JLabel("Criado por (id):");
		lblCriadoPorFixo.setBounds(20, 335, 110, 18);
		contentPane.add(lblCriadoPorFixo);
		lblCriadoPor = new JLabel("-");
		lblCriadoPor.setBounds(135, 335, 200, 18);
		contentPane.add(lblCriadoPor);

		JLabel lblRespondidoPorFixo = new JLabel("Respondido por (id):");
		lblRespondidoPorFixo.setBounds(20, 360, 150, 18);
		contentPane.add(lblRespondidoPorFixo);
		lblRespondidoPor = new JLabel("-");
		lblRespondidoPor.setBounds(170, 360, 200, 18);
		contentPane.add(lblRespondidoPor);

		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(20, 450, 120, 32);
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				voltar();
			}
		});
		contentPane.add(btnCancelar);

		btnSalvar = new JButton("Salvar");
		btnSalvar.setBounds(450, 450, 120, 32);
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				salvar();
			}
		});
		contentPane.add(btnSalvar);

		btnResponder = new JButton("Responder");
		btnResponder.setBounds(160, 450, 130, 32);
		btnResponder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				responder();
			}
		});
		contentPane.add(btnResponder);

		btnFinalizar = new JButton("Finalizar");
		btnFinalizar.setBounds(305, 450, 130, 32);
		btnFinalizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				finalizar();
			}
		});
		contentPane.add(btnFinalizar);

		atualizarVisibilidadeBotoes();
		atualizarVisibilidadeCampos();
	}

	private void atualizarVisibilidadeBotoes() {
		if (!modoEdicao) {
			btnSalvar.setVisible(true);
			btnResponder.setVisible(false);
			btnFinalizar.setVisible(false);
			return;
		}

		btnSalvar.setVisible(false);

		Status status = ticket.getStatus();
		boolean podeResponder = status == Status.ABERTO && temPermissao(Permissao.RESPONDER_TICKET);
		boolean podeFinalizar = status != Status.FECHADO && temPermissao(Permissao.FECHAR_TICKET);

		btnResponder.setVisible(podeResponder);
		btnFinalizar.setVisible(podeFinalizar);
	}

	private void atualizarVisibilidadeCampos() {
		boolean editavel = !modoEdicao;
		txtTitulo.setEditable(editavel);
		txtDescricao.setEditable(editavel);
		cmbSetor.setEnabled(editavel);
		cmbPrioridade.setEnabled(editavel);
		cmbCategoria.setEnabled(editavel);
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

		lblStatus.setText(ticket.getStatus().name());

		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		if (ticket.getDataAbertura() != null) {
			lblDataAbertura.setText(formato.format(ticket.getDataAbertura()));
		}
		if (ticket.getDataFechamento() != null) {
			lblDataFechamento.setText(formato.format(ticket.getDataFechamento()));
		}

		if (ticket.getCriadoPor() != null) {
			lblCriadoPor.setText(String.valueOf(ticket.getCriadoPor().getId()));
		}
		if (ticket.getRespondidoPor() != null) {
			lblRespondidoPor.setText(String.valueOf(ticket.getRespondidoPor().getId()));
		}
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
		novo.setDataAbertura(new Date());
		novo.setDataFechamento(null);
		novo.setCriadoPor(usuarioLogado);
		novo.setRespondidoPor(null);

		TicketDAO dao = new TicketDAO(novo);
		String msg = dao.atualizar(TipoOperacaoBD.INCLUSAO);
		JOptionPane.showMessageDialog(this, msg);

		if (!msg.startsWith("Erro") && !msg.startsWith("Falha")) {
			voltar();
		}
	}

	private void responder() {
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
			voltar();
		}
	}

	private void finalizar() {
		int resposta = JOptionPane.showConfirmDialog(
			this,
			"Finalizar este ticket?",
			"Confirmacao",
			JOptionPane.YES_NO_OPTION
		);
		if (resposta != JOptionPane.YES_OPTION) {
			return;
		}

		ticket.setStatus(Status.FECHADO);
		ticket.setDataFechamento(new Date());

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
}
