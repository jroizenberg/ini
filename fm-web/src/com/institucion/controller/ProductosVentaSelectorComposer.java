package com.institucion.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Listitem;

import com.institucion.bz.CajaEJB;
import com.institucion.bz.ClienteEJB;
import com.institucion.bz.ProductoEJB;
import com.institucion.desktop.delegated.ClienteDelegate;
import com.institucion.fm.conf.Session;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.service.SelectorFEComposer;
import com.institucion.fm.desktop.view.MessageBox;
import com.institucion.fm.desktop.view.PanelCrud;
import com.institucion.fm.desktop.view.PanelList;
import com.institucion.fm.security.service.PermissionTxManager;
import com.institucion.model.CajaMovimiento;
import com.institucion.model.FormasDePagoSubscripcionEnum;
import com.institucion.model.PagosPorSubscripcion;
import com.institucion.model.Producto;
import com.institucion.model.ProductoList;
import com.institucion.model.ProductoPrecioCrud;
import com.institucion.model.ProductoVentaDelDiaList2;
import com.institucion.model.ProductoVentaListPrueba;
import com.institucion.model.SucursalEnum;
import com.institucion.model.TipoMovimientoCajaEnum;
import com.institucion.model.VentaProducto;
import com.institucion.model.VentaProductoPorProducto;

public class ProductosVentaSelectorComposer extends SelectorFEComposer implements ClienteDelegate{
	DecimalFormat formateador = new DecimalFormat("###,###");

	private ProductoVentaDelDiaList2 ventasDelDiaListGrid;
	private List<Producto> pharmacyList;

	private ProductoEJB productoEJB;
	private CajaEJB cajaEJB;
	private PanelList curso2;
	private ClienteEJB clienteEJB;

	private PanelCrud panelPrecio; 
	
	public ProductosVentaSelectorComposer(){
		super();
		productoEJB = BeanFactory.<ProductoEJB>getObject("fmEjbProducto");
		cajaEJB = BeanFactory.<CajaEJB>getObject("fmEjbCaja");
		clienteEJB = BeanFactory.<ClienteEJB>getObject("fmEjbCliente");	

	}

	
	public void clear(){
//		getFilter().clear();
//		getFilter().clear();

	}
	public void onCreate() {
		((ProductoPrecioCrud)panelPrecio.getGridCrud()).setDelegate(this);
		getList().setActionComposerDelegate(this);
		if(Session.getAttribute("sucursalPrincipalSeleccionada") == null){
			MessageBox.validation("¡Debe seleccionar con que sucursal desea operar!", I18N.getLabel("selector.actionwithoutitem.title"));
			Executions.getCurrent().sendRedirect(null);
		}

		setCallFromMenu(Session.getDesktopPanel().getMessage().equals("menu")); // enlazado con DesktopMenu
		setCallFromMenu(false);

		// busca las ventas del dia
		buscarVentasDelDia();
		clear();
		onFind();		
		refreshEvents();
	}
	
	private void refreshEvents(){
	}
	
	private ProductoVentaListPrueba  getList(){
		return (ProductoVentaListPrueba)curso2.getGridList();
	}
	
	public void buscarVentasDelDia(){
		
		List <VentaProducto> ventaList=cajaEJB.findVentasDelDia(new Date(), new Date());
		if(ventaList != null){
			
			ventasDelDiaListGrid.setList(ventaList);
		}
	}

	public void onCambiarProductoSubs(Event event) throws Exception{
		List<VentaProducto> list= new ArrayList();
		for (Object iterable_element : ventasDelDiaListGrid.getSelectedItems()) {
			VentaProducto curso=(VentaProducto)((Listitem)iterable_element).getValue();
			list.add(curso);
	
		}
		if(list != null && list.size() > 1){
			MessageBox.info("El cambio de productos Fallados se puede realizar de a una venta por vez.", I18N.getLabel("selector.actionwithoutitem.title"));
			return;
		}
		
		if (list != null && list.size() ==1){
			
			VentaProducto venta=list.get(0);
			
			venta=clienteEJB.loadLazy(venta, true, true);

			if(venta.getAnulaVenta() != null && venta.getAnulaVenta()){
				MessageBox.info("NO se puede realizar cambio de productos ya que la venta FUE ANULADA con anterioridad.", I18N.getLabel("selector.actionwithoutitem.title"));
				return;
			}
			
			if (MessageBox.question("Si no tiene stock del mismo producto debe Cancelar esta pantalla y ANULAR la venta. Si tiene stock del producto y " +
					" se reemplaza el producto fallado por uno nuevo Continue. ", "Aviso")){
				
				// Vuelvo a devolver el stock
				String reemplazo= null;

				if(venta.getProductosList() != null){
					for (VentaProductoPorProducto object : venta.getProductosList()) {
						Producto prod=object.getProducto();
						String agregado="";
						if(object.getCantidadProd() > 1){
							agregado=" .LUEGO DEBE SELECCIONAR LA CANTIDAD DE PRODUCTOS A REEMPLAZAR. ";
						}else{
							agregado="  ";
						}
						
						if (MessageBox.question("Se desea reemplazar el producto: " +prod.getCodigo().toUpperCase() + " "
									+prod.getDescripcion().toUpperCase() + " ?  "+ agregado, "Consulta Producto a Reemplazar")){
							
							if(object.getCantidadProd() > 1){
								for(int i=1; i <= object.getCantidadProd(); i++){
									if (MessageBox.question("Se desea reemplazar una cantidad de "+i +" articulos del Producto: " +prod.getCodigo().toUpperCase()+ " "+prod.getDescripcion().toUpperCase() + " ?", "Consulta Cantidades a Reemplazar")){
										
										if(prod.getStock() <= 0 || prod.getStock() < i){
											MessageBox.info("Error, no alcanza el Stock para devolver el articulo. DEBE ANULAR LA VENTA, Devolver el dinero y/o Luego Crear nuevamente otra venta.", 
													"Error Acción Cancelada");
											return ;
										}
										
										if(reemplazo == null){
											reemplazo= prod.getCodigo().toUpperCase()+ " Cant: "+i ;
										}else{
											reemplazo= reemplazo +"- "+ prod.getCodigo().toUpperCase() + " Cant: "+i ;
										}
										prod.setStock(prod.getStock() - i);
										productoEJB.save(object.getProducto());
										
										break;
									}	
								}
							}else{
								
								if(prod.getStock() <= 0){
									MessageBox.info("Error, no alcanza el Stock para devolver el articulo. DEBE ANULAR LA VENTA, Devolver el dinero y/o Luego Crear nuevamente otra venta.", 
											"Error Acción Cancelada");
									return ;
								}
						
								if(reemplazo == null){
									reemplazo= prod.getCodigo().toUpperCase()+ " Cant: "+1 ;
								}else{
									reemplazo= reemplazo +"- "+ prod.getCodigo().toUpperCase()+ " Cant: "+1 ;
								}

								prod.setStock(prod.getStock() -1);
								productoEJB.save(object.getProducto());
								
							}
						}
					}
					
					if(reemplazo != null){
						if(venta.getVentaComentario() != null)
							venta.setVentaComentario(venta.getVentaComentario() +" .Se reemplazó por fallo: "+ reemplazo);
						else
							venta.setVentaComentario("Se reemplazó por fallo: "+reemplazo );

						cajaEJB.save(venta);
						
						// Guardo el movimiento en la caja ahora
						CajaMovimiento caja= new CajaMovimiento();
						caja.setConcepto("Se reemplazó por fallo: "+ reemplazo);
						caja.setFecha(new Date());
						caja.setIdUsuarioGeneroMovimiento(Session.getUsernameID().intValue());
						caja.setTipoMovimiento(TipoMovimientoCajaEnum.EGRESO);
						caja.setValor(Double.valueOf(0));
						caja.setSucursal(SucursalEnum.CENTRO);
						caja.setIdVentaProducto(venta.getId().intValue());

						cajaEJB.save(caja);
						
					}
					
				}
				
				if(reemplazo != null)
					MessageBox.info("Se registro el cambio de el/los productos fallados. Se actualizó el Stock.","Cambio Realizado");
				else
					MessageBox.info("No se realizo ninguna modificación en la Venta.","No se realizo ningun cambio");
				
				// busca las ventas del dia
				buscarVentasDelDia();
				
				onFind();		
			}
		}
	}
	
	
	public void onAnularSubs(Event event) throws Exception{
		List<VentaProducto> list= new ArrayList();
		for (Object iterable_element : ventasDelDiaListGrid.getSelectedItems()) {
			VentaProducto curso=(VentaProducto)((Listitem)iterable_element).getValue();
			list.add(curso);
		}
		if(list != null && list.size() > 1){
			MessageBox.info("Las ventas de Productos se pueden anular de a una por vez.", I18N.getLabel("selector.actionwithoutitem.title"));
			return;
		}
		
		if (list != null && list.size() ==1){
			
			VentaProducto venta=list.get(0);
			
			venta=clienteEJB.loadLazy(venta, true, true);
			if(venta.getAnulaVenta() != null && venta.getAnulaVenta()){
				MessageBox.info("La venta ya fue anulada con anterioridad.", I18N.getLabel("selector.actionwithoutitem.title"));
				return;
			}
			
			if (MessageBox.question(I18N.getLabel("venta.productos.anular"), I18N.getLabel("venta.productos.anular.title"))){
				
				int cantTotalDinero = obtenerPagos(venta);
				String pagosComentario=obtenerFormasDePagoPagos(venta);
				Object[] params3 =  {cantTotalDinero, pagosComentario};	

				if (MessageBox.question(I18N.getLabel("venta.productos.anular.concretar",params3),
						I18N.getLabel("venta.productos.anular.concretar.title"))){
										
					venta.setAnulaValorDevuelto(cantTotalDinero);
					venta.setAnulaVenta(true);
					venta.setFechaYHoraAnulacion(new Date());
					venta.setIdUsuarioAnuloVenta(Session.getUsernameID().intValue());

					cajaEJB.save(venta);
					
					// Guardo el movimiento en la caja ahora
					CajaMovimiento caja= new CajaMovimiento();
					caja.setConcepto("Anulacion de Venta Productos: "+venta.getVentaComentario());
					caja.setFecha(new Date());
					caja.setIdUsuarioGeneroMovimiento(Session.getUsernameID().intValue());
					caja.setTipoMovimiento(TipoMovimientoCajaEnum.EGRESO);
					caja.setValor(Double.valueOf(cantTotalDinero));
					caja.setSucursal(SucursalEnum.CENTRO);
					caja.setIdVentaProducto(venta.getId().intValue());

					cajaEJB.save(caja);

					// Vuelvo a devolver el stock
					if(venta.getProductosList() != null){
						for (VentaProductoPorProducto object : venta.getProductosList()) {
							Producto prod=object.getProducto();
							prod.setStock(prod.getStock() + object.getCantidadProd());
							productoEJB.save(object.getProducto());	
						}
					}
					
					MessageBox.info(I18N.getLabel("venta.productos.anular.concretar.title.confir"), 
							I18N.getLabel("selector.actionwithoutitem.title.realizada"));
					
					// busca las ventas del dia
					buscarVentasDelDia();
					
					onFind();		
				}
			}	
		}
	}

//	
	public void onChange2ClickEvt(Event event) throws Exception{
		
		Checkbox lh = (Checkbox) event.getData();
//		if(lh != null && lh.getId() != null)
//			cursopanelListGrid.actualizarListaDeCantVentaCambiada(lh.getId());
	
	}
	
	
	public String obtenerFormasDePagoPagos(VentaProducto producto){
		
		String comentarioFormasDePago="";

			int conTarjeta=0;
			int conTarjetaAdicionales= 0;
			int cuotasTarjeta=0;
			
			int conDebito=0;
			int conEfectivo=0;
			
			if(producto.getPagosPorSubscripcionList() != null){
				
				for (PagosPorSubscripcion pagoPorSubscripcion: 	producto.getPagosPorSubscripcionList()) {
					
					if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_15.toInt()){
						conTarjeta= conTarjeta+ pagoPorSubscripcion.getCantidadDinero();
						conTarjetaAdicionales= conTarjetaAdicionales+ pagoPorSubscripcion.getAdicional();
						
						if(pagoPorSubscripcion.getCantCuotas() >  cuotasTarjeta)
							cuotasTarjeta= pagoPorSubscripcion.getCantCuotas();
						
					}else if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_20.toInt()){
						conTarjeta= conTarjeta+ pagoPorSubscripcion.getCantidadDinero();
						conTarjetaAdicionales= conTarjetaAdicionales+ pagoPorSubscripcion.getAdicional();
						
						if(pagoPorSubscripcion.getCantCuotas() >  cuotasTarjeta)
							cuotasTarjeta= pagoPorSubscripcion.getCantCuotas();
						
					}else if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.DEBITO.toInt()){
						conDebito= conDebito+ pagoPorSubscripcion.getCantidadDinero();
						
					}else if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.EFECTIVO.toInt()){
						conEfectivo= conEfectivo+ pagoPorSubscripcion.getCantidadDinero();
						
					}				
				}					
			}
			if(conEfectivo > 0){
				comentarioFormasDePago="Se abono en Efectivo: $"+ formateador.format(conEfectivo ) +"- ";
				
			}
			if(conDebito > 0){
				comentarioFormasDePago="Debito: $"+ formateador.format(conDebito ) +"- ";
				
			}	
			if(conTarjeta > 0){
				comentarioFormasDePago="Tarjeta: $"+ formateador.format(conTarjeta ) + ", Adicional Tarjeta: $"+formateador.format(conTarjetaAdicionales  )+ ", Cuotas: "+ cuotasTarjeta ;
				
			}
		return comentarioFormasDePago;

	} 
	
	public int obtenerPagos(VentaProducto  producto){
		int pagosRealizados=0;
		int adicionales= 0;

		if(producto.getPagosPorSubscripcionList() != null){
			
			for (PagosPorSubscripcion pagoPorSubscripcion: 	producto.getPagosPorSubscripcionList()) {
				if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_15.toInt()){
					pagosRealizados= pagosRealizados+ pagoPorSubscripcion.getCantidadDinero();
					adicionales= adicionales+ pagoPorSubscripcion.getAdicional();
				}else if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_20.toInt()){
					pagosRealizados= pagosRealizados+ pagoPorSubscripcion.getCantidadDinero();
					adicionales= adicionales+ pagoPorSubscripcion.getAdicional();
				}else{
					pagosRealizados= pagosRealizados+ pagoPorSubscripcion.getCantidadDinero();
				}		
			}					
		}
		return adicionales+ pagosRealizados;
	} 
	
	public void onUpdate(Event event) throws Exception{
				
	}

	public void onBack(Event event) {
	     if (Sessions.getCurrent().getAttribute("idFromIgresoInscripcionSelectorVenta") != null){
	 		 com.institucion.fm.conf.Session.setAttribute("idFromIgresoInscripcionSelectorVenta", null);
	    	 super.gotoPage("/institucion/ingresoInscripcion-selector.zul");
	     }else
	    	 super.gotoPage(null);
	}
	
	
	private String obtenerProductosAvender(){
		List <Producto> prodList=(List)com.institucion.fm.conf.Session.getAttribute("idProductosVenta");
		String prod="";	
		for (Producto producto : prodList) {
			prod= prod + "Cod: "+producto.getCodigo() +" Cant:"+ producto.getCantidad() +"-";
			
		}
		return prod;
	}

	
	public void onVender(Event event) throws Exception {
		List<Producto> list =getList().getListProductsSelectedConCantidadActualizada();
		if (list != null && list.size() >0){
			
			List listaProductos=tieneStock(list);
			
			if(listaProductos != null && listaProductos.size() > 0){
				
				// obtener productos que tengan seteado una cantidad a comprar mayor a 0
				List productosAComprar=obtenerProductosAVEnderConCantMayorACero(listaProductos);
				if(productosAComprar != null && productosAComprar.size() > 0){
		
					if(list.size() != listaProductos.size())
						MessageBox.info("No se pudieron agregar a la Venta todos los Productos ya que algunos superaban la cantidad de Stock y/o No se ingreso una cantidad al producto",	"Aviso");
					
					com.institucion.fm.conf.Session.setAttribute("idProductosVenta", listaProductos);

					try{
						if(((ProductoPrecioCrud)panelPrecio.getGridCrud()).getEfectivo().isChecked()
								|| ((ProductoPrecioCrud)panelPrecio.getGridCrud()).getDebito().isChecked()){
							
							FormasDePagoSubscripcionEnum formaPago= null;
							if(((ProductoPrecioCrud)panelPrecio.getGridCrud()).getEfectivo().isChecked()){
								formaPago=FormasDePagoSubscripcionEnum.EFECTIVO;
							}else{
								formaPago=FormasDePagoSubscripcionEnum.DEBITO;
							}
							VentaProducto vp= new VentaProducto();
							List <Producto> prodList=(List)com.institucion.fm.conf.Session.getAttribute("idProductosVenta");
							String productosss=obtenerProductosAvender();
	
							List <PagosPorSubscripcion> pagosDelProd=fromViewToModel(vp, formaPago);
	
							if(pagosDelProd != null){
								for (PagosPorSubscripcion subscripcionDeClases : pagosDelProd) {
									subscripcionDeClases.setSucursal(SucursalEnum.CENTRO);	
								}
							}
	
							vp.setPagosPorSubscripcionList(new HashSet(pagosDelProd));
							vp.setFechayhoracompra(new Date());
							vp.setVentaComentario("Venta de Productos: "+productosss);
							vp.setIdUsuarioCreoVenta(Session.getUsernameID().intValue());
							List<VentaProductoPorProducto>  ventasProdss=obtenerVentaPorProductoProductos(prodList, vp);
							vp.setProductosList(new HashSet(ventasProdss));
							
							Long ventaId=cajaEJB.createVenta(vp);
	
							// Guardo el movimiento en la caja ahora
							CajaMovimiento caja= new CajaMovimiento();
							
							caja.setConcepto("Venta de Productos: "+productosss);
							caja.setFecha(new Date());
							caja.setIdUsuarioGeneroMovimiento(Session.getUsernameID().intValue());
							caja.setTipoMovimiento(TipoMovimientoCajaEnum.INGRESO);
							caja.setValor(Double.valueOf(obtenerCantidadDinero(formaPago)));
							caja.setSucursal(SucursalEnum.CENTRO);
	
							caja.setIdVentaProducto(ventaId.intValue());
							
							cajaEJB.save(caja);
							
							// Descuento Stock de las cosas que se estan vendiendo
							for (Producto producto : prodList) {
								producto.setStock(producto.getStock() - producto.getCantidad());
								productoEJB.save(producto);	
							}
							buscarVentasDelDia();

							MessageBox.info("Se realizo la venta Correctamente",	"Aviso");
						
							
						}else if(((ProductoPrecioCrud)panelPrecio.getGridCrud()).getTarjeta_15().isChecked()){
							
							FormasDePagoSubscripcionEnum formaPago= FormasDePagoSubscripcionEnum.TARJETA_15;

							VentaProducto vp= new VentaProducto();
							List <Producto> prodList=(List)com.institucion.fm.conf.Session.getAttribute("idProductosVenta");
							String productosss=obtenerProductosAvender();
	
							List <PagosPorSubscripcion> pagosDelProd=fromViewToModel(vp, formaPago);
	
							if(pagosDelProd != null){
								for (PagosPorSubscripcion subscripcionDeClases : pagosDelProd) {
									subscripcionDeClases.setSucursal(SucursalEnum.CENTRO);	
								}
							}
	
							vp.setPagosPorSubscripcionList(new HashSet(pagosDelProd));
							vp.setFechayhoracompra(new Date());
							vp.setVentaComentario("Venta de Productos: "+productosss);
							vp.setIdUsuarioCreoVenta(Session.getUsernameID().intValue());
							List<VentaProductoPorProducto>  ventasProdss=obtenerVentaPorProductoProductos(prodList, vp);
							vp.setProductosList(new HashSet(ventasProdss));
							
							Long ventaId=cajaEJB.createVenta(vp);
	
							// Guardo el movimiento en la caja ahora
							CajaMovimiento caja= new CajaMovimiento();
							
							caja.setConcepto("Venta de Productos: "+productosss);
							caja.setFecha(new Date());
							caja.setIdUsuarioGeneroMovimiento(Session.getUsernameID().intValue());
							caja.setTipoMovimiento(TipoMovimientoCajaEnum.INGRESO);
							caja.setValor(Double.valueOf(obtenerCantidadDinero(formaPago)));
							caja.setSucursal(SucursalEnum.CENTRO);
	
							caja.setIdVentaProducto(ventaId.intValue());
							
							cajaEJB.save(caja);
							
							// Descuento Stock de las cosas que se estan vendiendo
							for (Producto producto : prodList) {
								producto.setStock(producto.getStock() - producto.getCantidad());
								productoEJB.save(producto);	
							}
							buscarVentasDelDia();

							MessageBox.info("Se realizo la venta Correctamente",	"Aviso");
							
						}else if(((ProductoPrecioCrud)panelPrecio.getGridCrud()).getTarjeta_20().isChecked()){
							
							FormasDePagoSubscripcionEnum formaPago= FormasDePagoSubscripcionEnum.TARJETA_20;

							VentaProducto vp= new VentaProducto();
							List <Producto> prodList=(List)com.institucion.fm.conf.Session.getAttribute("idProductosVenta");
							String productosss=obtenerProductosAvender();
	
							List <PagosPorSubscripcion> pagosDelProd=fromViewToModel(vp, formaPago);
	
							if(pagosDelProd != null){
								for (PagosPorSubscripcion subscripcionDeClases : pagosDelProd) {
									subscripcionDeClases.setSucursal(SucursalEnum.CENTRO);	
								}
							}
	
							vp.setPagosPorSubscripcionList(new HashSet(pagosDelProd));
							vp.setFechayhoracompra(new Date());
							vp.setVentaComentario("Venta de Productos: "+productosss);
							vp.setIdUsuarioCreoVenta(Session.getUsernameID().intValue());
							List<VentaProductoPorProducto>  ventasProdss=obtenerVentaPorProductoProductos(prodList, vp);
							vp.setProductosList(new HashSet(ventasProdss));
							
							Long ventaId=cajaEJB.createVenta(vp);
	
							// Guardo el movimiento en la caja ahora
							CajaMovimiento caja= new CajaMovimiento();
							
							caja.setConcepto("Venta de Productos: "+productosss);
							caja.setFecha(new Date());
							caja.setIdUsuarioGeneroMovimiento(Session.getUsernameID().intValue());
							caja.setTipoMovimiento(TipoMovimientoCajaEnum.INGRESO);
							caja.setValor(Double.valueOf(obtenerCantidadDinero(formaPago)));
							caja.setSucursal(SucursalEnum.CENTRO);
	
							caja.setIdVentaProducto(ventaId.intValue());
							
							cajaEJB.save(caja);
							
							// Descuento Stock de las cosas que se estan vendiendo
							for (Producto producto : prodList) {
								producto.setStock(producto.getStock() - producto.getCantidad());
								productoEJB.save(producto);	
							}
							buscarVentasDelDia();

							MessageBox.info("Se realizo la venta Correctamente",	"Aviso");
							
						}else{
							
							MessageBox.info("Debe seleccionar una forma de pago", I18N.getLabel("selector.actionwithoutitem.title.realizada"));
							return ;
							// si no se selecciono nada me voy a la otra pantalla.
//							super.gotoPage("/institucion/vender-producto-crud.zul");
							
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

					if(((ProductoPrecioCrud)panelPrecio.getGridCrud()).getDebito().isChecked() 
							|| ((ProductoPrecioCrud)panelPrecio.getGridCrud()).getEfectivo().isChecked()
							|| ((ProductoPrecioCrud)panelPrecio.getGridCrud()).getTarjeta_15().isChecked()
							|| ((ProductoPrecioCrud)panelPrecio.getGridCrud()).getTarjeta_20().isChecked()){
					onFind();
					getList().actualizarIntbosaNegros();
					((ProductoPrecioCrud)panelPrecio.getGridCrud()).getPrecioCb().setValue("$0");
					com.institucion.fm.conf.Session.setAttribute("idProductosVenta", null);
					
					((ProductoPrecioCrud)panelPrecio.getGridCrud()).getTarjeta_15().setChecked(false);
					((ProductoPrecioCrud)panelPrecio.getGridCrud()).getTarjeta_20().setChecked(false);
					((ProductoPrecioCrud)panelPrecio.getGridCrud()).getEfectivo().setChecked(false);
					((ProductoPrecioCrud)panelPrecio.getGridCrud()).getDebito().setChecked(false);

					((ProductoPrecioCrud)panelPrecio.getGridCrud()).getPrecioCbLabel().setValue("Precio TOTAL ACUMULADO: ");
					
				     if (Sessions.getCurrent().getAttribute("idFromIgresoInscripcionSelectorVenta") != null){
				 		 com.institucion.fm.conf.Session.setAttribute("idFromIgresoInscripcionSelectorVenta", null);
				    	 super.gotoPage("/institucion/ingresoInscripcion-selector.zul");
				     }else
				    	 super.gotoPage("/institucion/productos-venta-selector.zul");
					}
				}else{
					MessageBox.info(I18N.getLabel("curso.productos.sin.cantidad"),	I18N.getLabel("selector.actionwithoutitem.title"));
					return ;
				}
				
			}else{
				MessageBox.info(I18N.getLabel("curso.productos.sin.stock"),	I18N.getLabel("selector.actionwithoutitem.title"));
				return ;
				
			}
		}
	}
	
	
	private int obtenerCantidadDinero(FormasDePagoSubscripcionEnum formaPago){
		int cantidad=0;
		List<Producto> list =getList().getListProductsSelectedConCantidadActualizada();

		if(list != null && list.size() >0) {
			for (Producto producto : list) {
				
				if(producto.getCantidad() > 0){
					int nuevoVal=producto.getCantidad() * producto.getPrecio();
					cantidad=cantidad + nuevoVal;
				}
				
			}	
		}
		
		if(formaPago != null && formaPago.equals(FormasDePagoSubscripcionEnum.TARJETA_15)){
			cantidad=((cantidad * 15 )/100) + cantidad;
		}else 		if(formaPago != null && formaPago.equals(FormasDePagoSubscripcionEnum.TARJETA_20)){
			cantidad=((cantidad * 20 )/100) + cantidad;
		}

		return cantidad;
	}
	
	private List <PagosPorSubscripcion> fromViewToModel(VentaProducto prod,  FormasDePagoSubscripcionEnum formaPago) {
		
		List lista= new ArrayList();
		
		PagosPorSubscripcion pagoNuevo= new PagosPorSubscripcion();
		pagoNuevo.setEsCopago(false);
		pagoNuevo.setCantidadDinero(obtenerCantidadDinero(formaPago));
		pagoNuevo.setSucursal(SucursalEnum.CENTRO);

		pagoNuevo.setIdTipoDePago(formaPago);
		pagoNuevo.setIdUsuarioGeneroMovimientoCaja(Session.getUsernameID().intValue());
		pagoNuevo.setTipoMovimiento(TipoMovimientoCajaEnum.INGRESO);
		pagoNuevo.setAdicional(0);
		
		if(formaPago.equals(FormasDePagoSubscripcionEnum.TARJETA_15)){
			pagoNuevo.setCantCuotas(1);
			int importeConAdicional=obtenerCantidadDinero(formaPago);
			int importeSinAdicional=obtenerCantidadDinero(null);		
			int diferenciaAdicional= importeConAdicional- importeSinAdicional;
			pagoNuevo.setAdicional(diferenciaAdicional);
			pagoNuevo.setPorcInteres(15);
		}else if(formaPago.equals(FormasDePagoSubscripcionEnum.TARJETA_20)){
			pagoNuevo.setCantCuotas(1);
			int importeConAdicional=obtenerCantidadDinero(formaPago);
			int importeSinAdicional=obtenerCantidadDinero(null);		
			int diferenciaAdicional= importeConAdicional- importeSinAdicional;
			pagoNuevo.setAdicional(diferenciaAdicional);
			pagoNuevo.setPorcInteres(20);
		}else{
			pagoNuevo.setAdicional(0);
			pagoNuevo.setCantCuotas(0);
			pagoNuevo.setPorcInteres(0);
		}
		pagoNuevo.setVentaProducto(prod);
		
		lista.add(pagoNuevo);
		
		return lista;
	}	
	
	public List<VentaProductoPorProducto> obtenerVentaPorProductoProductos(List <Producto> prodList, VentaProducto ventaProducto){
		List <VentaProductoPorProducto> ventasProdis= new ArrayList <VentaProductoPorProducto>();
		if(prodList != null){
			
			for (Producto producto : prodList) {
				VentaProductoPorProducto vp= new VentaProductoPorProducto();
				vp.setProducto(producto);
				vp.setVentaProducto(ventaProducto);	
				vp.setCantidadProd(producto.getCantidad());
				vp.setPreciocostoProd(producto.getPrecioCosto());
				ventasProdis.add(vp);
			}
		}
		return ventasProdis;
	} 
	private List obtenerProductosAVEnderConCantMayorACero(List lista){
		List listaConStock= new ArrayList();
		Iterator ite= lista.iterator();
		while(ite.hasNext()){
			Producto cli=(Producto)ite.next();
			if(cli.getCantidad() > 0){
				listaConStock.add(cli);
			}		
		}
		return listaConStock;
		
	}
	
	
	private List tieneStock(List lista){
		List listaConStock= new ArrayList();
		Iterator ite= lista.iterator();
		while(ite.hasNext()){
			Producto cli=(Producto)ite.next();
			if(cli.getStock() > 0 && cli.getCantidad() > 0 && cli.getCantidad() <= cli.getStock()){
				listaConStock.add(cli);
			}		
		}
		return listaConStock;
	}
	
	protected Producto hasSelectedOneItem(ProductoList gridlist) {
		int cantSected=0;
		Producto prodee= null;
		if(gridlist.getListProducts() != null){
			for (Producto prod : gridlist.getListProducts()) {
				if(prod.isChecked()){
					cantSected= cantSected+1;
					if(prodee== null)
						prodee=prod;
				}
			}
		}
		
		if(cantSected == 1){
			return prodee;
		}else{
			MessageBox.info(I18N.getLabel("selector.actionwithoutitem.information"), I18N.getLabel("selector.actionwithoutitem.title"));
			return null;
		}
	}	
	
	public void onInsertClass(Event event) throws Exception{
		super.gotoPage("/institucion/productos-selector.zul");				
	}
	
	
	public void onDelete(Event event) {

	}

	public void onInsert(Event event) throws Exception {
	
		com.institucion.fm.conf.Session.setAttribute("idProducto", null);
		Sessions.getCurrent().setAttribute("max", 0);
		super.saveHistory();
		super.gotoPage("/institucion/productos-crud.zul");
	}

	public void onDoubleClickEvt(Event event) throws Exception {
		// Pregunta si tiene permisos para la operacion
		if (this.getManager().havePermission(PermissionTxManager.TX_PHARMACY_MODIFY)) {
			this.onUpdate(null);
		}
	}


	public void onFind(Event evt) {
		Session.setAttribute("pag", false);
		this.onFind();
	}
	
	public void onFind() {
		Logger log=Logger.getLogger(this.getClass());
		log.info("Creando listado de farmacia en la version modificada");
		pharmacyList= new ArrayList<Producto>();

		StringBuilder actionConditions= new StringBuilder( "select * from producto producto   ");
		actionConditions.append(" where 1=1  ");
		actionConditions.append(" and producto.stock > 0");
		actionConditions.append(" order by producto.codigo ");			
		pharmacyList =productoEJB.findAllConJdbc(actionConditions.toString());
		
		 getList().setList(pharmacyList, false);

	}
	
	public void onClearFilter(Event evt){
		this.onFind();
	}

	@Override
	public void sortEvent(Event event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buscar(Event evt, boolean isFromCodigoBarras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void venderNuevaSubs(Event evt, int idCliente) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actualizarPaneldePrecioProducto() {
		// TODO Auto-generated method stub
		FormasDePagoSubscripcionEnum formaPago= null;
		if(((ProductoPrecioCrud)panelPrecio.getGridCrud()).getEfectivo().isChecked()){
			formaPago= FormasDePagoSubscripcionEnum.EFECTIVO;
			((ProductoPrecioCrud)panelPrecio.getGridCrud()).getPrecioCbLabel().setValue("Precio TOTAL ACUMULADO: ");
		} else if(((ProductoPrecioCrud)panelPrecio.getGridCrud()).getDebito().isChecked()){
			formaPago= FormasDePagoSubscripcionEnum.DEBITO;
			((ProductoPrecioCrud)panelPrecio.getGridCrud()).getPrecioCbLabel().setValue("Precio TOTAL ACUMULADO: ");
		} else if(((ProductoPrecioCrud)panelPrecio.getGridCrud()).getTarjeta_15().isChecked()){
			formaPago= FormasDePagoSubscripcionEnum.TARJETA_15;
			((ProductoPrecioCrud)panelPrecio.getGridCrud()).getPrecioCbLabel().setValue("Precio TOTAL ACUMULADO c/adicional: ");
		} else if(((ProductoPrecioCrud)panelPrecio.getGridCrud()).getTarjeta_20().isChecked()){
			formaPago= FormasDePagoSubscripcionEnum.TARJETA_20;
			((ProductoPrecioCrud)panelPrecio.getGridCrud()).getPrecioCbLabel().setValue("Precio TOTAL ACUMULADO c/adicional: ");
		}else{
			((ProductoPrecioCrud)panelPrecio.getGridCrud()).getPrecioCbLabel().setValue("Precio TOTAL ACUMULADO: ");
		}
		Integer aaa=obtenerCantidadDinero(formaPago);
				
		if(aaa != null){
			((ProductoPrecioCrud)panelPrecio.getGridCrud()).getPrecioCb().setValue("$"+aaa);	
		}		
	}
}