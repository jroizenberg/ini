package com.institucion.model;

import java.io.Serializable;
import java.util.ArrayList;

public class PulsoClinicaDetalle  implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/* Primer columna, esto esta supuestamente en el HEADER_AUX*/
	private String mes;
	private int mesId;
	private ArrayList<PulsoClinicaDetalleParte2> list;

	public PulsoClinicaDetalle() {}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public int getMesId() {
		return mesId;
	}

	public void setMesId(int mesId) {
		this.mesId = mesId;
	}

	public ArrayList<PulsoClinicaDetalleParte2> getList() {
		return list;
	}

	public void setList(ArrayList<PulsoClinicaDetalleParte2> list) {
		this.list = list;
	}	
}