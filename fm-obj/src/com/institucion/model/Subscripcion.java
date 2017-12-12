package com.institucion.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Transient;

public class Subscripcion implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Set<Concepto> conceptoList;
	private Cliente cliente;
	
	private Integer precioCursosMatricula;
	private Integer valorAdicionaltarjeta;
	private Integer precioTOTALadicionalTarjeta;
	
	private Set<SubscripcionDeClases> subscripcionDeClases;
	private Set<PagosPorSubscripcion> pagosPorSubscripcionList;
	private Set<CupoActividad> cupoActividadList;
	private Date fechaIngresoSubscripcion; 
	
	private Boolean anulaSubscripcion;
	private String anulaComentario;
	private Boolean anulaDevuelveDinero;
	private Integer anulaValorDevuelvo;
	private Date fechaYHoraCreacion;
	private Date fechaYHoraAnulacion;
	private Date fechaYHoraSaldaSubscripcion;
	private Integer idUsuarioAnuloSubscripcion;
	private Integer idUsuarioCreoSubscripcion;
	private Integer idUsuarioSaldaSubscripcion;

	private String detalleCumple;
	
	@Transient
	private CupoActividad cupoActividadParaCalcular;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Set<PagosPorSubscripcion> getPagosPorSubscripcionList() {
		return pagosPorSubscripcionList;
	}

	public void setPagosPorSubscripcionList(Set<PagosPorSubscripcion> pagosPorSubscripcionList) {
		this.pagosPorSubscripcionList = pagosPorSubscripcionList;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Integer getValorAdicionaltarjeta() {
		return valorAdicionaltarjeta;
	}

	public void setValorAdicionaltarjeta(Integer valorAdicionaltarjeta) {
		this.valorAdicionaltarjeta = valorAdicionaltarjeta;
	}

	public Integer getPrecioTOTALadicionalTarjeta() {
		return precioTOTALadicionalTarjeta;
	}

	public Set<Concepto> getConceptoList() {
		return conceptoList;
	}

	public void setConceptoList(Set<Concepto> conceptoList) {
		this.conceptoList = conceptoList;
	}

	public void setPrecioTOTALadicionalTarjeta(Integer precioTOTALadicionalTarjeta) {
		this.precioTOTALadicionalTarjeta = precioTOTALadicionalTarjeta;
	}

	public Integer getPrecioCursosMatricula() {
		return precioCursosMatricula;
	}

	public void setPrecioCursosMatricula(Integer precioCursosMatricula) {
		this.precioCursosMatricula = precioCursosMatricula;
	}

	public void setSubscripcionDeClases(Set<SubscripcionDeClases> subscripcionDeClases) {
		this.subscripcionDeClases = subscripcionDeClases;
	}

	public Boolean getAnulaSubscripcion() {
		return anulaSubscripcion;
	}

	public Boolean getAnulaDevuelveDinero() {
		return anulaDevuelveDinero;
	}

	public Set<SubscripcionDeClases> getSubscripcionDeClases() {
		return subscripcionDeClases;
	}

	public Set<CupoActividad> getCupoActividadList() {
		return cupoActividadList;
	}

	public void setCupoActividadList(Set<CupoActividad> cupoActividadList) {
		this.cupoActividadList = cupoActividadList;
	}

	public Date getFechaIngresoSubscripcion() {
		return fechaIngresoSubscripcion;
	}

	public void setFechaIngresoSubscripcion(Date fechaIngresoSubscripcion) {
		this.fechaIngresoSubscripcion = fechaIngresoSubscripcion;
	}

	public Boolean isAnulaSubscripcion() {
		return anulaSubscripcion;
	}

	public void setAnulaSubscripcion(Boolean anulaSubscripcion) {
		this.anulaSubscripcion = anulaSubscripcion;
	}

	public String getAnulaComentario() {
		return anulaComentario;
	}

	public void setAnulaComentario(String anulaComentario) {
		this.anulaComentario = anulaComentario;
	}

	public Boolean isAnulaDevuelveDinero() {
		return anulaDevuelveDinero;
	}

	public void setAnulaDevuelveDinero(Boolean anulaDevuelveDinero) {
		this.anulaDevuelveDinero = anulaDevuelveDinero;
	}

	public Integer getAnulaValorDevuelvo() {
		return anulaValorDevuelvo;
	}

	public void setAnulaValorDevuelvo(Integer anulaValorDevuelvo) {
		this.anulaValorDevuelvo = anulaValorDevuelvo;
	}

	public Date getFechaYHoraSaldaSubscripcion() {
		return fechaYHoraSaldaSubscripcion;
	}

	public void setFechaYHoraSaldaSubscripcion(Date fechaYHoraSaldaSubscripcion) {
		this.fechaYHoraSaldaSubscripcion = fechaYHoraSaldaSubscripcion;
	}

	public Integer getIdUsuarioSaldaSubscripcion() {
		return idUsuarioSaldaSubscripcion;
	}

	public void setIdUsuarioSaldaSubscripcion(Integer idUsuarioSaldaSubscripcion) {
		this.idUsuarioSaldaSubscripcion = idUsuarioSaldaSubscripcion;
	}

	public Date getFechaYHoraCreacion() {
		return fechaYHoraCreacion;
	}

	public void setFechaYHoraCreacion(Date fechaYHoraCreacion) {
		this.fechaYHoraCreacion = fechaYHoraCreacion;
	}

	public Date getFechaYHoraAnulacion() {
		return fechaYHoraAnulacion;
	}

	public void setFechaYHoraAnulacion(Date fechaYHoraAnulacion) {
		this.fechaYHoraAnulacion = fechaYHoraAnulacion;
	}

	public Integer getIdUsuarioAnuloSubscripcion() {
		return idUsuarioAnuloSubscripcion;
	}

	public void setIdUsuarioAnuloSubscripcion(Integer idUsuarioAnuloSubscripcion) {
		this.idUsuarioAnuloSubscripcion = idUsuarioAnuloSubscripcion;
	}

	public Integer getIdUsuarioCreoSubscripcion() {
		return idUsuarioCreoSubscripcion;
	}

	public void setIdUsuarioCreoSubscripcion(Integer idUsuarioCreoSubscripcion) {
		this.idUsuarioCreoSubscripcion = idUsuarioCreoSubscripcion;
	}


	public String getDetalleCumple() {
		return detalleCumple;
	}

	@Transient
	public CupoActividad getCupoActividadParaCalcular() {
		return cupoActividadParaCalcular;
	}

	@Transient
	public void setCupoActividadParaCalcular(CupoActividad cupoActividadParaCalcular) {
		this.cupoActividadParaCalcular = cupoActividadParaCalcular;
	}

	public void setDetalleCumple(String detalleCumple) {
		this.detalleCumple = detalleCumple;
	}
	
}