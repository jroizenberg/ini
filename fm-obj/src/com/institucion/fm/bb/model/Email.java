package com.institucion.fm.bb.model;

import java.io.Serializable;

public class Email implements Serializable
{
	private static final long serialVersionUID = 1L;

	private Long id;
	private String from;
	private String to;
	private String cc;
	private String bcc;
	private String subject;
	private String body;

	public Long getId() { return id; }
	@SuppressWarnings("unused")
	private void setId(Long id) { this.id = id; }

	public String getFrom() { return from; }
	public void setFrom(String from) { this.from = from; }

	public String getTo() { return to; }
	public void setTo(String to) { this.to = to; }

	public String getCc() { return cc; }
	public void setCc(String cc) { this.cc = cc; }

	public String getBcc() { return bcc; }
	public void setBcc(String bcc) { this.bcc = bcc; }

	public String getSubject() { return subject; }
	public void setSubject(String subject) { this.subject = subject; }

	public String getBody() { return body; }
	public void setBody(String body) { this.body = body; }
}