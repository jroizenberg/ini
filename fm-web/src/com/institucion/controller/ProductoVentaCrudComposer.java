package com.institucion.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJBException;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;

import com.institucion.bz.CajaEJB;
import com.institucion.bz.ProductoEJB;
import com.institucion.desktop.delegated.AnularSubscripcionDelegate;
import com.institucion.fm.conf.Session;
import com.institucion.fm.conf.exception.WebExceptionHandler;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.CrudComposer;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.MessageBox;
import com.institucion.fm.desktop.view.PanelCrud;
import com.institucion.fm.desktop.view.PanelList;
import com.institucion.model.CajaMovimiento;
import com.institucion.model.FormasDePagoSubscripcionEnum;
import com.institucion.model.FormasDePagoVentaProdCrud;
import com.institucion.model.PagosPorSubscripcion;
import com.institucion.model.Producto;
import com.institucion.model.ProductoVentaCrud;
import com.institucion.model.ProductoVentaListPrueba;
import com.institucion.model.SucursalEnum;
import com.institucion.model.TipoMovimientoCajaEnum;
import com.institucion.model.VentaProducto;
import com.institucion.model.VentaProductoPorProducto;
import com.institucion.model.VentaProductosPagarBotonesFilter;

public class ProductoVentaCrudComposer extends CrudComposer implements AnularSubscripcionDelegate{
	DecimalFormat formateador = new DecimalFormat("###,###");

	// crud formas de pago
	private FormasDePagoVentaProdCrud crudFormasDePago;
	private PanelCrud  pagar;
	private CajaEJB cajaEJB;
//	private ProductoVentaList cursopanelListGrid;
	private PanelCrud crud;
	private ProductoEJB productoEJB;
	private PanelList curso2;

	private VentaProductosPagarBotonesFilter getPagarCursosCrud() {
		return (VentaProductosPagarBotonesFilter) (pagar.getGridCrud());
	}
	

	private ProductoVentaListPrueba  getList(){
		return (ProductoVentaListPrueba)curso2.getGridList();
	}
	private ProductoVentaCrud getProductoVentaCrud() {
		return (ProductoVentaCrud)(crud.getGridCrud());
	}
	
	
	public ProductoVentaCrudComposer() {
		cajaEJB = BeanFactory.<CajaEJB>getObject("fmEjbCaja");
		productoEJB = BeanFactory.<ProductoEJB>getObject("fmEjbProducto");
	}
 	
	public void onCreate() {
		crudFormasDePago.setSubsAnulardelegate(this);
		getPagarCursosCrud().setSubsAnulardelegate(this);

		List <Producto> prodList=(List)com.institucion.fm.conf.Session.getAttribute("idProductosVenta");
		if(prodList != null){
			int cantFinal=obtenerDeuda(prodList);
			getProductoVentaCrud().getDeudaaSaldar().setValue(String.valueOf(cantFinal));
			 getList().setList(prodList, true);
		}
		fillListBoxProducts(null);

	}
	
	@Override
	public void pagarCurso() {
//		int debePagar =0; 
		int debePagar= Integer.parseInt(getProductoVentaCrud().getDeudaaSaldar().getValue());

		
		int pagosRealizados=0;
		int adicionales=0;
		if(crudFormasDePago.getProducts() != null && crudFormasDePago.getProducts().size() > 0){
			for (PagosPorSubscripcion pagoPorSubscripcion: crudFormasDePago.getProducts()) {
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
			
		if(pagosRealizados < debePagar ){
			// error el pago debe de ser exacto. 
			MessageBox.info(I18N.getLabel("curso.error.seleccion.select.suma.del.pago.del.producto"), I18N.getLabel("selector.actionwithoutitem.title"));
			return ;
			
		}else{
			int apagar=pagosRealizados + adicionales;
			Object[] params3 =  { "$"+formateador.format(apagar)};	

			if (MessageBox.question(I18N.getLabel("selector.seguro.de.abonar.producto",params3),
				I18N.getLabel("closeup.selector.abonar.y.guardar.title"))){
			
				try {
					onSave(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
				
	private int obtenerDeuda(List <Producto> prodList){
		int fantantePago=0;
		if(prodList != null){
			for (Producto producto : prodList) {
				int totPrecio=producto.getCantidad() * producto.getPrecio();
				fantantePago= fantantePago+ totPrecio;
				
			}
		}
		return fantantePago;
	}
	
	private void fillListBoxProducts(List <Producto> listProds) {
		Set<PagosPorSubscripcion> result = new HashSet<PagosPorSubscripcion>();
		if (listProds != null) {
			int importeFinal= Integer.parseInt(getProductoVentaCrud().getDeudaaSaldar().getValue());

//			for (PagosPorSubscripcion dpp : subscripcion.getPagosPorSubscripcionList()) {
//				result.add(dpp);
//			}
			
//			if(subscripcion.getPagosPorSubscripcionList() != null && subscripcion.getPagosPorSubscripcionList().size() ==0){
				// actualizo el rojo y el verde de la plata, no se hizo ningun pago aca.
				
				crudFormasDePago.getValorFaltantePesos().setValue(String.valueOf(importeFinal));
				crudFormasDePago.getValorSumadoPesos().setValue("0");
//			}
			crudFormasDePago.fillListBox(result);
			
			if(crudFormasDePago.getProducts() != null && crudFormasDePago.getProducts().size() > 0){
				int sumaTotPagos=0;
				int sumaAdicionalTarjeta=0;
				for (PagosPorSubscripcion pagoPorSubscripcion: crudFormasDePago.getProducts()) {
					if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_15.toInt()){
						
						int adicionalparte=0;
		   				if(pagoPorSubscripcion.getPorcInteres() != null){
		   					adicionalparte=(int)(pagoPorSubscripcion.getCantidadDinero() * pagoPorSubscripcion.getPorcInteres() )/100;
		   				}else{
		   					adicionalparte=(int)(pagoPorSubscripcion.getCantidadDinero() * 15 )/100;
		   				}
						pagoPorSubscripcion.setAdicional(adicionalparte);
						sumaTotPagos= sumaTotPagos+ pagoPorSubscripcion.getCantidadDinero()+ adicionalparte;
						sumaAdicionalTarjeta= sumaAdicionalTarjeta+ adicionalparte;
					}else if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_20.toInt()){
						
						int adicionalparte=0;
		   				if(pagoPorSubscripcion.getPorcInteres() != null){
		   					adicionalparte=(int)(pagoPorSubscripcion.getCantidadDinero() * pagoPorSubscripcion.getPorcInteres() )/100;
		   				}else{
		   					adicionalparte=(int)(pagoPorSubscripcion.getCantidadDinero() * 20 )/100;
		   				}
						pagoPorSubscripcion.setAdicional(adicionalparte);
						sumaTotPagos= sumaTotPagos+ pagoPorSubscripcion.getCantidadDinero()+ adicionalparte;
						sumaAdicionalTarjeta= sumaAdicionalTarjeta+ adicionalparte;
					}else{
						sumaTotPagos = sumaTotPagos +  pagoPorSubscripcion.getCantidadDinero();
						
					}		
				}	
				int faltante=importeFinal -sumaTotPagos+ sumaAdicionalTarjeta;

				crudFormasDePago.getValorFaltantePesos().setValue(String.valueOf(faltante));
				crudFormasDePago.getValorSumadoPesos().setValue(String.valueOf(sumaTotPagos));
				
			}
		}else{
			int importeFinal= Integer.parseInt(getProductoVentaCrud().getDeudaaSaldar().getValue());
			crudFormasDePago.getValorFaltantePesos().setValue(String.valueOf(importeFinal));
			crudFormasDePago.getValorSumadoPesos().setValue("0");
			
		}		
	}
	
	private String obtenerProductosAvender(){
		List <Producto> prodList=(List)com.institucion.fm.conf.Session.getAttribute("idProductosVenta");
		String prod="";	
		for (Producto producto : prodList) {
			prod= prod + "Cod: "+producto.getCodigo() +" Cant:"+ producto.getCantidad() +"-";
			
		}
		return prod;
	}
	
	
	public void onSave(Event event) throws Exception {
		try{
			
			if(!validateCrud()){
				MessageBox.info(I18N.getLabel("curso.error"), I18N.getLabel("selector.actionwithoutitem.title"));
				return;
			}
			
			String productosss=obtenerProductosAvender();
					
			List <Producto> prodList=(List)com.institucion.fm.conf.Session.getAttribute("idProductosVenta");
			
			VentaProducto vp= new VentaProducto();
			
			List <PagosPorSubscripcion> pagosDelProd=fromViewToModel(vp);


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
			caja.setValor(Double.valueOf(crudFormasDePago.getValorSumadoPesos().getValue()));
			caja.setSucursal(SucursalEnum.CENTRO);

			caja.setIdVentaProducto(ventaId.intValue());
			
			cajaEJB.save(caja);
			
			// Descuento Stock de las cosas que se estan vendiendo
			for (Producto producto : prodList) {
				producto.setStock(producto.getStock() - producto.getCantidad());
				productoEJB.save(producto);	
			}
			
		     if (Sessions.getCurrent().getAttribute("idFromIgresoInscripcionSelectorVenta") != null){
		 		 com.institucion.fm.conf.Session.setAttribute("idFromIgresoInscripcionSelectorVenta", null);
		    	 super.gotoPage("/institucion/ingresoInscripcion-selector.zul");
		     }else
		    	 super.gotoPage("/institucion/productos-venta-selector.zul");
			
		} catch (EJBException ex) {
			WebExceptionHandler.handleThis(ex);
		}
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
	
				
	public void onBack(Event event) {
	     if (Sessions.getCurrent().getAttribute("idFromIgresoInscripcionSelectorVenta") != null){
	 		 com.institucion.fm.conf.Session.setAttribute("idFromIgresoInscripcionSelectorVenta", null);
	    	 super.gotoPage("/institucion/ingresoInscripcion-selector.zul");
	     }else
	    	 super.gotoPage("/institucion/productos-venta-selector.zul");
	}

	private List <PagosPorSubscripcion> fromViewToModel(VentaProducto prod) {
		List <PagosPorSubscripcion> pagosNuevos = new ArrayList<PagosPorSubscripcion>();

		// tengo que saber primero cuanto debo 
		int deuda= Integer.parseInt(getProductoVentaCrud().getDeudaaSaldar().getValue());

		// obtengo la plata de la deuda, de todos los pagos que se hicieron
		if(crudFormasDePago.getProducts() != null && crudFormasDePago.getProducts().size() > 0){
			
			for (PagosPorSubscripcion pagoPorSubscripcion: crudFormasDePago.getProducts()) {
				
				if(pagoPorSubscripcion.getCantidadDinero() > deuda){
					// quiere decir que saco ya todo el dinero de este pago.
					PagosPorSubscripcion pagoNuevo= new PagosPorSubscripcion();
					pagoNuevo.setEsCopago(false);
					pagoNuevo.setCantidadDinero(deuda);
					pagoNuevo.setIdTipoDePago(pagoPorSubscripcion.getIdTipoDePago());
					pagoNuevo.setVentaProducto(prod);
					pagoNuevo.setPorcInteres(pagoPorSubscripcion.getPorcInteres());
					pagoNuevo.setSucursal(SucursalEnum.CENTRO);

					pagoNuevo.setSaldadaDeuda(true);

					if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_15.toInt()){
						
						int adicionalparte=0;
		   				if(pagoNuevo.getPorcInteres() != null){
		   					adicionalparte=(int)(pagoNuevo.getCantidadDinero() * pagoNuevo.getPorcInteres() )/100;
		   				}else{
		   					adicionalparte=(int)(pagoNuevo.getCantidadDinero() * 15 )/100;
		   				}

						pagoNuevo.setAdicional(adicionalparte);
						pagoNuevo.setCantCuotas(pagoPorSubscripcion.getCantCuotas());
						pagoPorSubscripcion.setEsCopago(false);
						pagoPorSubscripcion.setCantidadDinero(pagoPorSubscripcion.getCantidadDinero()- deuda);
						
						int adicional=0;
		   				if(pagoPorSubscripcion.getPorcInteres() != null){
		   					adicional=(int)(pagoPorSubscripcion.getCantidadDinero() * pagoPorSubscripcion.getPorcInteres() )/100;
		   				}else{
		   					adicional=(int)(pagoPorSubscripcion.getCantidadDinero() * 15 )/100;
		   				}
		   				pagoPorSubscripcion.setAdicional(adicional);
						
						pagoPorSubscripcion.setVentaProducto(prod);
					}else 			if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_20.toInt()){
						
						int adicionalparte=0;
		   				if(pagoNuevo.getPorcInteres() != null){
		   					adicionalparte=(int)(pagoNuevo.getCantidadDinero() * pagoNuevo.getPorcInteres() )/100;
		   				}else{
		   					adicionalparte=(int)(pagoNuevo.getCantidadDinero() * 20 )/100;
		   				}

						pagoNuevo.setAdicional(adicionalparte);
						pagoNuevo.setCantCuotas(pagoPorSubscripcion.getCantCuotas());
						pagoPorSubscripcion.setEsCopago(false);
						pagoPorSubscripcion.setCantidadDinero(pagoPorSubscripcion.getCantidadDinero()- deuda);
						
						int adicional=0;
		   				if(pagoPorSubscripcion.getPorcInteres() != null){
		   					adicional=(int)(pagoPorSubscripcion.getCantidadDinero() * pagoPorSubscripcion.getPorcInteres() )/100;
		   				}else{
		   					adicional=(int)(pagoPorSubscripcion.getCantidadDinero() * 20 )/100;
		   				}
		   				pagoPorSubscripcion.setAdicional(adicional);
						
						pagoPorSubscripcion.setVentaProducto(prod);
					}
					pagosNuevos.add(pagoNuevo);
					break;
				}else if(pagoPorSubscripcion.getCantidadDinero() == deuda){
//					pagoPorSubscripcion.setProducto(prod);
					pagoPorSubscripcion.setVentaProducto(prod);

					pagosNuevos.add(pagoPorSubscripcion);
					
					crudFormasDePago.getProducts().remove(pagoPorSubscripcion);
					break;
				}else{
					// quiere decir que saco el dinero de el pago anterior + un nuevo pago.
//					pagoPorSubscripcion.setProducto(prod);
					pagoPorSubscripcion.setVentaProducto(prod);

					pagosNuevos.add(pagoPorSubscripcion);
					deuda = deuda-pagoPorSubscripcion.getCantidadDinero(); // - pagoPorSubscripcion.getAdicional();
					crudFormasDePago.getProducts().remove(pagoPorSubscripcion);
				}
			}
			}
		return pagosNuevos;

		}	

	private boolean validateCrud() {
		
		// tiene que tener pagos en la lista de pagos
		if(crudFormasDePago.getProducts() == null 
				||  (crudFormasDePago.getProducts() != null && 
						crudFormasDePago.getProducts().size() <= 0)){
			throw new WrongValueException(crudFormasDePago, I18N.getLabel("error.empty.field"));
		}
		
		// la lista de pagos debe de ser igual a la deuda.
		if(crudFormasDePago.getProducts() != null && 
				crudFormasDePago.getProducts().size() > 0){
		
			int deudaAPagarSeleccionada= Integer.parseInt(getProductoVentaCrud().getDeudaaSaldar().getValue());

			int pagosRealizados=0;
			int adicionales=0;
			if(crudFormasDePago.getProducts() != null && crudFormasDePago.getProducts().size() > 0){
				for (PagosPorSubscripcion pagoPorSubscripcion: crudFormasDePago.getProducts()) {
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
			if(deudaAPagarSeleccionada != pagosRealizados)
				throw new WrongValueException(crudFormasDePago, I18N.getLabel("error.empty.field.pago.igual.venta"));
		}
		
		return true;
	}

	@Override
	public int getValorCurso() {
		return Integer.parseInt(getProductoVentaCrud().getDeudaaSaldar().getValue());
	}

	@Override
	public boolean seSeleccionoElCurso() {
		// TODO Auto-generated method stub
		return false;
	}
}
