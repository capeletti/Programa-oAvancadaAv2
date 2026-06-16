package model;

import java.time.LocalDateTime;

public class Ticket implements Comparable<Ticket>{

	
	private int id;
	private String titulo;
	private String descricao;
	private Setor setorDestino;
	private Status status;
	private Prioridade prioridade;
	private Categoria categoria;
	private LocalDateTime dataAbertura;
	private LocalDateTime dataFechamento;
    private Usuario criadoPor;
    private Usuario respondidoPor;
	
	public Ticket() {
		this.id = -1;
		this.titulo = null;
		this.descricao = null;
		this.status = Status.ABERTO;
		this.dataAbertura = null;
		this.dataFechamento = null;
		this.setorDestino = null;
		this.prioridade = null;
		this.categoria = null;
		this.criadoPor = null;
		this.respondidoPor = null;
	}

	public Usuario getCriadoPor() {
		return criadoPor;
	}

	public void setCriadoPor(Usuario criadoPor) {
		this.criadoPor = criadoPor;
	}

	public Usuario getRespondidoPor() {
		return respondidoPor;
	}

	public void setRespondidoPor(Usuario respondidoPor) {
		this.respondidoPor = respondidoPor;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Setor getSetorDestino() {
		return setorDestino;
	}

	public void setSetorDestino(Setor setorDestino) {
		this.setorDestino = setorDestino;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Prioridade getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(Prioridade prioridade) {
		this.prioridade = prioridade;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public LocalDateTime getDataAbertura() {
		return dataAbertura;
	}

	public void setDataAbertura(LocalDateTime dataAbertura) {
		this.dataAbertura = dataAbertura;
	}

	public LocalDateTime getDataFechamento() {
		return dataFechamento;
	}

	public void setDataFechamento(LocalDateTime dataFechamento) {
		this.dataFechamento = dataFechamento;
	}
	@Override
	public int compareTo(Ticket outro) {
		int comparaPrioridade = Integer.compare(outro.prioridade.ordinal(), this.prioridade.ordinal());
		if (comparaPrioridade != 0) {
			return comparaPrioridade;
		}
		return this.dataAbertura.compareTo(outro.dataAbertura);
	}
}
