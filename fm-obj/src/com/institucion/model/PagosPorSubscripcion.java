package com.institucion.model;

import java.io.Serializable;

public class PagosPorSubscripcion implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Subscripcion subscripcion;
	private FormasDePagoSubscripcionEnum idTipoDePago;
	private Integer cantidadDinero;
	private int adicional; // si es tarjeta se paga adicional
	private int cantCuotas; 
	private Boolean saldadaDeuda;
	private VentaProducto ventaProducto;
	private TipoMovimientoCajaEnum tipoMovimiento;
	private Integer idUsuarioGeneroMovimientoCaja;
	private Integer porcInteres;
	private BancoPromocion bancoPromocion;
	private Gastos gasto;
	private GastosMaipu gastoMaipu;
	private SucursalEnum sucursal;
	private Quincena quincena;
	private Boolean esCopago;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public Integer getCantidadDinero() {
		return cantidadDinero;
	}
	public void setCantidadDinero(Integer cantidadDinero) {
		this.cantidadDinero = cantidadDinero;
	}
	public int getAdicional() {
		return adicional;
	}
	public void setAdicional(int adicional) {
		this.adicional = adicional;
	}
	public int getCantCuotas() {
		return cantCuotas;
	}
	public void setCantCuotas(int cantCuotas) {
		this.cantCuotas = cantCuotas;
	}
	public FormasDePagoSubscripcionEnum getIdTipoDePago() {
		return idTipoDePago;
	}
	public void setIdTipoDePago(FormasDePagoSubscripcionEnum idTipoDePago) {
		this.idTipoDePago = idTipoDePago;
	}
	
	public Subscripcion getSubscripcion() {
		return subscripcion;
	}
	public void setSubscripcion(Subscripcion subscripcion) {
		this.subscripcion = subscripcion;
	}
	public Boolean getSaldadaDeuda() {
		return saldadaDeuda;
	}
	public void setSaldadaDeuda(Boolean saldadaDeuda) {
		this.saldadaDeuda = saldadaDeuda;
	}
	public VentaProducto getVentaProducto() {
		return ventaProducto;
	}
	public void setVentaProducto(VentaProducto ventaProducto) {
		this.ventaProducto = ventaProducto;
	}
	public TipoMovimientoCajaEnum getTipoMovimiento() {
		return tipoMovimiento;
	}
	public void setTipoMovimiento(TipoMovimientoCajaEnum tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}
	public Integer getIdUsuarioGeneroMovimientoCaja() {
		return idUsuarioGeneroMovimientoCaja;
	}
	public Integer getPorcInteres() {
		return porcInteres;
	}
	public void setPorcInteres(Integer porcInteres) {
		this.porcInteres = porcInteres;
	}
	public void setIdUsuarioGeneroMovimientoCaja(
			Integer idUsuarioGeneroMovimientoCaja) {
		this.idUsuarioGeneroMovimientoCaja = idUsuarioGeneroMovimientoCaja;
	}
	public BancoPromocion getBancoPromocion() {
		return bancoPromocion;
	}
	public void setBancoPromocion(BancoPromocion bancoPromocion) {
		this.bancoPromocion = bancoPromocion;
	}
	public Gastos getGasto() {
		return gasto;
	}
	public void setGasto(Gastos gasto) {
		this.gasto = gasto;
	}

	public SucursalEnum getSucursal() {
		return sucursal;
	}
	public void setSucursal(SucursalEnum sucursal) {
		this.sucursal = sucursal;
	}
	public Quincena getQuincena() {
		return quincena;
	}
	public Boolean getEsCopago() {
		return esCopago;
	}
	public void setEsCopago(Boolean esCopago) {
		this.esCopago = esCopago;
	}
	public void setQuincena(Quincena quincena) {
		this.quincena = quincena;
	}
	public GastosMaipu getGastoMaipu() {
		return gastoMaipu;
	}
	public void setGastoMaipu(GastosMaipu gastoMaipu) {
		this.gastoMaipu = gastoMaipu;
	}
	

}