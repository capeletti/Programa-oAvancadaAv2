package model;

import java.time.LocalDateTime;

public class ArquivoTicket {

	private int id;
	private String nomeArquivo;
	private String tipoArquivo;
	private byte[] arquivo;
	private LocalDateTime dataEnvio;
	private Ticket ticket;
	private Usuario enviadoPor;


	public ArquivoTicket() {
		this.id = -1;
		this.nomeArquivo = null;
		this.tipoArquivo = null;
		this.arquivo = null;
		this.dataEnvio = null;
		this.ticket = null;
		this.enviadoPor = null;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}


	public String getTipoArquivo() {
		return tipoArquivo;
	}

	public void setTipoArquivo(String tipoArquivo) {
		this.tipoArquivo = tipoArquivo;
	}


	public byte[] getArquivo() {
		return arquivo;
	}

	public void setArquivo(byte[] arquivo) {
		this.arquivo = arquivo;
	}


	public LocalDateTime getDataEnvio() {
		return dataEnvio;
	}

	public void setDataEnvio(LocalDateTime dataEnvio) {
		this.dataEnvio = dataEnvio;
	}


	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}


	public Usuario getEnviadoPor() {
		return enviadoPor;
	}

	public void setEnviadoPor(Usuario enviadoPor) {
		this.enviadoPor = enviadoPor;
	}
}