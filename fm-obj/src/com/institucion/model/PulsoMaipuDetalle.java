package com.institucion.model;

import java.util.ArrayList;

public class PulsoMaipuDetalle  {
	
	/* Primer columna, esto esta supuestamente en el HEADER_AUX*/
	private String quincena;
	private int quincenaId;
	private int posicionId;  // debe venir a partir del 0

	private ArrayList<PulsoMaipuDetalleParte2> list;

	public PulsoMaipuDetalle() {}

	public ArrayList<PulsoMaipuDetalleParte2> getList() {
		return list;
	}

	public void setList(ArrayList<PulsoMaipuDetalleParte2> list) {
		this.list = list;
	}

	public String getQuincena() {
		return quincena;
	}

	public void setQuincena(String quincena) {
		this.quincena = quincena;
	}

	public int getQuincenaId() {
		return quincenaId;
	}

	public void setQuincenaId(int quincenaId) {
		this.quincenaId = quincenaId;
	}

	public int getPosicionId() {
		return posicionId;
	}

	public void setPosicionId(int posicionId) {
		this.posicionId = posicionId;
	}	
}