package com.institucion.model;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import com.institucion.desktop.delegated.AnularSubscripcionDelegate;
import com.institucion.desktop.delegated.CursosDelegate;
import com.institucion.desktop.delegated.SubscripcionDelegate;
import com.institucion.desktop.helper.FormasDePagoViewHelper;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridCrud;
import com.institucion.fm.desktop.view.GridList;
import com.institucion.fm.desktop.view.PanelEntityCrudList;

public class FormasDePagoVentaProdCrud extends PanelEntityCrudList<PagosPorSubscripcion>{
	DecimalFormat formateador = new DecimalFormat("###,###");

	private static final long serialVersionUID = 1L;
	public static final String ID = "gridproduct";
	private CursosDelegate delegate;
	private SubscripcionDelegate subsdelegate;
	private AnularSubscripcionDelegate subsAnulardelegate;

	private GridCrud productCrudGrid;
	private GridList productListBox;
	
	private Combobox formaDePago;
	private Combobox pagoRapido;

	private Label valorSumadoPesos;
	private Label valorFaltantePesos;
	
	private Combobox cuotas;
	private Intbox dinero;

	public FormasDePagoVentaProdCrud(){
		this.setStyle("  width:70%;  align:center;  margin-left: 10%;");
		super.setStyle("  width:70%;   align:center;  margin-left: 10%;");
		
	}

	private String updateMethod="onDoubleClickEvt";

	/**
	 * @return the productAmount
	 */

	public CursosDelegate getDelegate() {
		return delegate;
	}

	public void setDelegate(CursosDelegate delegate) {
		this.delegate = delegate;
	}
	

	
	@Override
	public void cleanCrud() {
		Constraint c = formaDePago.getConstraint();
		formaDePago.setConstraint("");
		formaDePago.setText("");
		formaDePago.setConstraint(c);
		
		Constraint brandC = cuotas.getConstraint();
		cuotas.setConstraint("");
		cuotas.setText("");
		cuotas.setConstraint(brandC);
		
		Constraint presentationC = dinero.getConstraint();
		dinero.setConstraint("");
		dinero.setText("");
		dinero.setConstraint(presentationC);
		
		if(pagoRapido != null && pagoRapido.getSelectedItem() != null 
				&& ((PagoRapidoSubscripcionEnum)pagoRapido.getSelectedItem().getValue()).toInt() != PagoRapidoSubscripcionEnum.PAGO_COMBINADO.toInt() ){
			Constraint c3 = pagoRapido.getConstraint();
			pagoRapido.setConstraint("");
			pagoRapido.setText("");
			pagoRapido.setConstraint(c3);	
		}
	}

	private int obtenerTodosLosAdicionalesViejos(){
		int adic=0;
		List<?> rows = productListBox.getItems();

		for (int i = 0; i < rows.size(); i++) {
			Listitem rowProduct22 = (Listitem) rows.get(i);
			PagosPorSubscripcion dpp = (PagosPorSubscripcion)rowProduct22.getValue();
			if(dpp.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_15.toInt()){
   				if(dpp.getPorcInteres() != null){
   					adic=adic+(int)(dpp.getCantidadDinero() * dpp.getPorcInteres() )/100;
   				}else{
   					adic=adic+(int)(dpp.getCantidadDinero() * 15 )/100;
   				}
			}else if(dpp.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_20.toInt()){
   				if(dpp.getPorcInteres() != null){
   					adic=adic+(int)(dpp.getCantidadDinero() * dpp.getPorcInteres() )/100;
   				}else{
   					adic=adic+(int)(dpp.getCantidadDinero() * 20 )/100;
   				}
			}
		}
		return adic;
	}
	
	public Listitem createItemAnularSubs(PagosPorSubscripcion item) {
		Listitem rowProduct = new Listitem();
		
		int valorFinalDeTodosLosCursos=getSubsAnulardelegate().getValorCurso();
		int sumaTotalDeLoQueTenia=0;
		List<?> rows = productListBox.getItems();
		for (int i = 0; i < rows.size(); i++) {
			Listitem rowProduct22 = (Listitem) rows.get(i);
			PagosPorSubscripcion dpp = (PagosPorSubscripcion)rowProduct22.getValue();
			sumaTotalDeLoQueTenia= sumaTotalDeLoQueTenia +dpp.getCantidadDinero();
		}
		
		
		boolean superaValor=false;
		int faltante=0;
		int adicional=0;

		// si se supera del pago total, agrego solamente la parte que falta
		int valorQueSeEstaAgregando=item.getCantidadDinero();
		int sumaTotal= valorQueSeEstaAgregando + sumaTotalDeLoQueTenia;
		if(sumaTotal > valorFinalDeTodosLosCursos){
			superaValor=true;
			int aDescontarDeloqueseseteo=sumaTotal -valorFinalDeTodosLosCursos;
			faltante=valorQueSeEstaAgregando-aDescontarDeloqueseseteo;
			item.setEsCopago(false);
			item.setCantidadDinero(faltante);
			if(item.getAdicional() > 0){
				item.setCantidadDinero(faltante);	
   				if(item.getPorcInteres() != null){
   					adicional=(int)(item.getCantidadDinero() * item.getPorcInteres() )/100;
   				}else{
   					adicional=(int)(item.getCantidadDinero() * 15 )/100;
   				}
				item.setAdicional(adicional);
			}
		}
		adicional=adicional+obtenerTodosLosAdicionalesViejos();
				
		rowProduct.appendChild(new Listcell(String.valueOf(FormasDePagoSubscripcionEnum.fromIntToString(item.getIdTipoDePago().toInt()))));
		if(item.getIdTipoDePago().toInt() != FormasDePagoSubscripcionEnum.TARJETA_15.toInt() ){
			rowProduct.appendChild(new Listcell(String.valueOf("-")));
		}else if(item.getIdTipoDePago().toInt() != FormasDePagoSubscripcionEnum.TARJETA_20.toInt() ){
			rowProduct.appendChild(new Listcell(String.valueOf("-")));
		}else
			rowProduct.appendChild(new Listcell(String.valueOf(item.getCantCuotas())));
		
		rowProduct.appendChild(new Listcell(String.valueOf(item.getCantidadDinero())));
		
		Listcell cell= null; 
		if(item.getIdTipoDePago().toInt() != FormasDePagoSubscripcionEnum.TARJETA_15.toInt() 
				&& item.getIdTipoDePago().toInt() != FormasDePagoSubscripcionEnum.TARJETA_20.toInt()){
			if(item.getSaldadaDeuda() != null && item.getSaldadaDeuda()){
				if(item.getBancoPromocion() != null){
					cell =new Listcell(String.valueOf("Abonado como Deuda. Promoción: "+ item.getBancoPromocion().getNombrePromo()));	
				}else{
					cell =new Listcell(String.valueOf("Abonado como Deuda"));	
				}
			
			}else{
				if(item.getBancoPromocion() != null){
					cell =new Listcell(" Promoción: "+ item.getBancoPromocion().getNombrePromo());
				}else{
					cell =new Listcell(String.valueOf(" "));					
				}
			}
		}else{
			int adicional2=0;

			if(item.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_15.toInt()){
				if(item.getPorcInteres() != null){
					adicional2=(int)(item.getCantidadDinero() * item.getPorcInteres() )/100;
				}else{
					adicional2=(int)(item.getCantidadDinero() * 15 )/100;
				}
				
			} else 			if(item.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_20.toInt()){
				if(item.getPorcInteres() != null){
					adicional2=(int)(item.getCantidadDinero() * item.getPorcInteres() )/100;
				}else{
					adicional2=(int)(item.getCantidadDinero() * 20 )/100;
				}
			}
				

			int total=item.getCantidadDinero()+ adicional2;
			adicional=adicional+adicional2;
			if(item.getSaldadaDeuda() != null && item.getSaldadaDeuda()){
				
				if(item.getBancoPromocion() != null){
					if(item.getPorcInteres() != null && item.getPorcInteres() > 0){
						cell = new Listcell(String.valueOf("Abonado como Deuda. Promoción Tarjeta: "+ item.getBancoPromocion().getNombrePromo() + " Se abona un adicional de $" + formateador.format(adicional2)));	
					}else{
						cell = new Listcell(String.valueOf("Abonado como Deuda. Promoción Tarjeta: "+ item.getBancoPromocion().getNombrePromo()));
					}
						
				}else{
					if(item.getPorcInteres() != null && item.getPorcInteres() > 0){
						cell = new Listcell(String.valueOf("Abonado como Deuda. Se abona un adicional de $" + formateador.format(adicional2)));	
					}else{
						cell = new Listcell(String.valueOf("Abonado como Deuda. Se abona un adicional de $" + formateador.format(adicional2)));
					}
				}

			}else{
				if(item.getBancoPromocion() != null){
					
					if(item.getPorcInteres() != null && item.getPorcInteres() > 0){
						cell = new Listcell(String.valueOf("Promoción Tarjeta: "+ item.getBancoPromocion().getNombrePromo() + " Se abona un adicional de $" + formateador.format(adicional2)));	
					}else{
						cell = new Listcell(String.valueOf("Promoción Tarjeta: "+ item.getBancoPromocion().getNombrePromo()));
					}
						
				}else{
					if(item.getPorcInteres() != null && item.getPorcInteres() > 0){
						cell = new Listcell(String.valueOf("Se abona un adicional de $" + formateador.format(adicional2)));	
					}else{
						cell = new Listcell(String.valueOf("Se abona un adicional de $" + formateador.format(adicional2)));
					}
				}
			}
		}
		rowProduct.appendChild(cell);
		
		rowProduct.setValue(item);

		// actualiza los labels
		if(superaValor){
			valorSumadoPesos.setValue(String.valueOf(faltante + sumaTotalDeLoQueTenia +adicional));		
			float tot=valorFinalDeTodosLosCursos - faltante - sumaTotalDeLoQueTenia;
			valorFaltantePesos.setValue(String.valueOf(tot ) ) ;	
		}else{
			valorSumadoPesos.setValue(String.valueOf(valorQueSeEstaAgregando + sumaTotalDeLoQueTenia+ adicional));		
			float tot=valorFinalDeTodosLosCursos - valorQueSeEstaAgregando - sumaTotalDeLoQueTenia;
			valorFaltantePesos.setValue(String.valueOf(tot ) ) ;	
		}

		return rowProduct;
	}
	
	@Override
	public Listitem createItem(PagosPorSubscripcion item) {
		Listitem rowProduct = new Listitem();
		if(getSubsdelegate() == null){
			return createItemAnularSubs(item);
		}
		int valorFinalDeTodosLosCursos=getSubsdelegate().getValorCurso();
		int sumaTotalDeLoQueTenia=0;
		List<?> rows = productListBox.getItems();
		for (int i = 0; i < rows.size(); i++) {
			Listitem rowProduct22 = (Listitem) rows.get(i);
			PagosPorSubscripcion dpp = (PagosPorSubscripcion)rowProduct22.getValue();
			sumaTotalDeLoQueTenia= sumaTotalDeLoQueTenia +dpp.getCantidadDinero();
		}
		
		// si ya habia llegado al valor, no agrego nada
		if(sumaTotalDeLoQueTenia == valorFinalDeTodosLosCursos && sumaTotalDeLoQueTenia > 0){
			return null;
		}
		boolean superaValor=false;
		int faltante=0;
		int adicional=0;

		// si se supera del pago total, agrego solamente la parte que falta
		int valorQueSeEstaAgregando=item.getCantidadDinero();
		int sumaTotal= valorQueSeEstaAgregando + sumaTotalDeLoQueTenia;
		if(sumaTotal > valorFinalDeTodosLosCursos){
			superaValor=true;
			int aDescontarDeloqueseseteo=sumaTotal -valorFinalDeTodosLosCursos;
			faltante=valorQueSeEstaAgregando-aDescontarDeloqueseseteo;
			item.setEsCopago(false);
			item.setCantidadDinero(faltante);
			if(item.getAdicional() > 0){
				item.setCantidadDinero(faltante);	
				if(item.getPorcInteres() != null){
					adicional=(int)(item.getCantidadDinero() * item.getPorcInteres() )/100;
				}else{
					adicional=(int)(item.getCantidadDinero() * 15 )/100;
				}
				item.setAdicional(adicional);
			}
		}
		adicional=adicional+obtenerTodosLosAdicionalesViejos();

		rowProduct.appendChild(new Listcell(FormasDePagoSubscripcionEnum.fromIntToString(item.getIdTipoDePago().toInt())));
		
		if(item.getIdTipoDePago().toInt() != FormasDePagoSubscripcionEnum.TARJETA_15.toInt() ){
			rowProduct.appendChild(new Listcell(String.valueOf("-")));
		}else if(item.getIdTipoDePago().toInt() != FormasDePagoSubscripcionEnum.TARJETA_20.toInt() ){
			rowProduct.appendChild(new Listcell(String.valueOf("-")));
		}else
			rowProduct.appendChild(new Listcell(String.valueOf(item.getCantCuotas())));
		
		rowProduct.appendChild(new Listcell(String.valueOf(item.getCantidadDinero())));
		
		
		Listcell cell= null; 
		if(item.getIdTipoDePago().toInt() != FormasDePagoSubscripcionEnum.TARJETA_15.toInt() 
				&& item.getIdTipoDePago().toInt() != FormasDePagoSubscripcionEnum.TARJETA_20.toInt()){
			if(item.getSaldadaDeuda() != null && item.getSaldadaDeuda()){
				if(item.getBancoPromocion() != null){
					cell =new Listcell(String.valueOf("Abonado como Deuda. Promoción: "+ item.getBancoPromocion().getNombrePromo()));	
				}else{
					cell =new Listcell(String.valueOf("Abonado como Deuda"));	
				}
			
			}else{
				if(item.getBancoPromocion() != null){
					cell =new Listcell(" Promoción: "+ item.getBancoPromocion().getNombrePromo());
				}else{
					cell =new Listcell(String.valueOf(" "));					
				}
			}
		}else{
			int adicional2=0;
			
			if(item.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_15.toInt()){
				if(item.getPorcInteres() != null){
					adicional2=(int)(item.getCantidadDinero() * item.getPorcInteres() )/100;
				}else{
					adicional2=(int)(item.getCantidadDinero() * 15 )/100;
				}
				
			}else 			if(item.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_20.toInt()){
				if(item.getPorcInteres() != null){
					adicional2=(int)(item.getCantidadDinero() * item.getPorcInteres() )/100;
				}else{
					adicional2=(int)(item.getCantidadDinero() * 20 )/100;
				}
				
			} 

			int total=item.getCantidadDinero()+ adicional2;
			adicional=adicional+adicional2;
			
			if(item.getSaldadaDeuda() != null && item.getSaldadaDeuda()){
				
				if(item.getBancoPromocion() != null){
					if(item.getPorcInteres() != null && item.getPorcInteres() > 0){
						cell = new Listcell(String.valueOf("Abonado como Deuda. Promoción Tarjeta: "+ item.getBancoPromocion().getNombrePromo() + " Se abona un adicional de $" + formateador.format(adicional2)));	
					}else{
						cell = new Listcell(String.valueOf("Abonado como Deuda. Promoción Tarjeta: "+ item.getBancoPromocion().getNombrePromo()));
					}
						
				}else{
					if(item.getPorcInteres() != null && item.getPorcInteres() > 0){
						cell = new Listcell(String.valueOf("Abonado como Deuda. Se abona un adicional de $" + formateador.format(adicional2)));	
					}else{
						cell = new Listcell(String.valueOf("Abonado como Deuda. Se abona un adicional de $" + formateador.format(adicional2)));
					}
				}

			}else{
				if(item.getBancoPromocion() != null){
					
					if(item.getPorcInteres() != null && item.getPorcInteres() > 0){
						cell = new Listcell(String.valueOf("Promoción Tarjeta: "+ item.getBancoPromocion().getNombrePromo() + " Se abona un adicional de $" + formateador.format(adicional2)));	
					}else{
						cell = new Listcell(String.valueOf("Promoción Tarjeta: "+ item.getBancoPromocion().getNombrePromo()));
					}
						
				}else{
					if(item.getPorcInteres() != null && item.getPorcInteres() > 0){
						cell = new Listcell(String.valueOf("Se abona un adicional de $" + formateador.format(adicional2)));	
					}else{
						cell = new Listcell(String.valueOf("Se abona un adicional de $" + formateador.format(adicional2)));
					}
				}
			}
		}
		rowProduct.appendChild(cell);
		

		rowProduct.setValue(item);
		rowProduct.addForward(Events.ON_DOUBLE_CLICK,(Component) null,getUpdateMethod());
		addIDUEvent(rowProduct , subsdelegate.esModificacion());
		
		// Falta aca agregar la actualizacion de los valores
		getSubsdelegate().actualizarAdicionalyImporteTotal();
		
		// actualiza los labels
		if(superaValor){
			valorSumadoPesos.setValue(String.valueOf(faltante + sumaTotalDeLoQueTenia +adicional));		
			float tot=valorFinalDeTodosLosCursos - faltante - sumaTotalDeLoQueTenia;
			valorFaltantePesos.setValue(String.valueOf(tot ) ) ;	
		}else{
			valorSumadoPesos.setValue(String.valueOf(valorQueSeEstaAgregando + sumaTotalDeLoQueTenia+ adicional));		
			float tot=valorFinalDeTodosLosCursos - valorQueSeEstaAgregando - sumaTotalDeLoQueTenia;
			valorFaltantePesos.setValue(String.valueOf(tot ) ) ;	
		}

		return rowProduct;
	}
	
	@Override
	protected PagosPorSubscripcion createObject() {
		PagosPorSubscripcion dpp = new PagosPorSubscripcion();
		FormasDePagoSubscripcionEnum formaDePago = (FormasDePagoSubscripcionEnum) this.formaDePago.getSelectedItem().getValue();
		dpp.setIdTipoDePago(formaDePago);
		
		if(formaDePago.toInt() == FormasDePagoSubscripcionEnum.TARJETA_15.toInt()){
			Integer cantCuotas= (Integer) this.cuotas.getSelectedItem().getValue();
			dpp.setCantCuotas(cantCuotas);		
		}else if(formaDePago.toInt() == FormasDePagoSubscripcionEnum.TARJETA_20.toInt()){
			Integer cantCuotas= (Integer) this.cuotas.getSelectedItem().getValue();
			dpp.setCantCuotas(cantCuotas);		
		}
		Integer valor = (Integer) this.dinero.getValue();
		dpp.setEsCopago(false);
		dpp.setCantidadDinero(valor);

		if(formaDePago.toInt() == FormasDePagoSubscripcionEnum.TARJETA_15.toInt()){
			int adicional=0;
			if(dpp.getPorcInteres() != null){
				adicional=(int)(dpp.getCantidadDinero() * dpp.getPorcInteres() )/100;
			}else{
				adicional=(int)(dpp.getCantidadDinero() * 15 )/100;
			}
			dpp.setAdicional(adicional);
		}else if(formaDePago.toInt() == FormasDePagoSubscripcionEnum.TARJETA_20.toInt()){
			int adicional=0;
			if(dpp.getPorcInteres() != null){
				adicional=(int)(dpp.getCantidadDinero() * dpp.getPorcInteres() )/100;
			}else{
				adicional=(int)(dpp.getCantidadDinero() * 20 )/100;
			}
			dpp.setAdicional(adicional);
		}

		if (Sessions.getCurrent().getAttribute("idSubs") != null) {
			Subscripcion cur=(Subscripcion)Sessions.getCurrent().getAttribute("idSubs"); 
			dpp.setSubscripcion(cur);
		}
	
		return dpp;
	}
	
	@Override
	protected PagosPorSubscripcion fillObject(PagosPorSubscripcion item) {
		return item;
	}
	
	@Override
	public GridList getGridList() {
		return productListBox;
	}
	@Override
	protected String getPanelTitle() {
		return I18N.getLabel("formasDePago.subscripcion");
	}
	
	@Override
	protected boolean itemExists(Listitem item) {
		return false;
	}
	
	@Override
	protected boolean insert(){
		super.insert();
		//actualizarTipoCurso();
		if(getSubsdelegate() != null)
			getSubsdelegate().actualizarAdicionalyImporteTotal();

		return true;
	}

	
	@Override
	protected void update() {
		
		if (getGridList().getSelectedItem() == null)
			return;
	
		validateCrud();
			
		Listitem item=createItem(updateList());
		if (item != null){
			getGridList().addRow(item);
			getGridList().removeChild(getGridList().getSelectedItem());
			setInsertMode();
			cleanCrud();
		}
		
		
		// FALTA VALIDAR ACA QUE SE VALIDE Si SE PASA DEL LIMITE LE PONGA EL MAXIMO 
		getSubsdelegate().actualizarAdicionalyImporteTotal();
	}


	private void actualizarTipoCurso(){
//		getDelegate().actualizarTipoCurso();
	}
	
	@Override
	protected void delete(){
		super.delete();
		
		float valorFinalDeTodosLosCursos=getSubsdelegate().getValorCurso();
		float sumaTotalDeLoQueTenia=0;
		List<?> rows = productListBox.getItems();
		for (int i = 0; i < rows.size(); i++) {
			Listitem rowProduct22 = (Listitem) rows.get(i);
			PagosPorSubscripcion dpp = (PagosPorSubscripcion)rowProduct22.getValue();
			sumaTotalDeLoQueTenia= sumaTotalDeLoQueTenia+ dpp.getCantidadDinero();
		}
		
		// Falta aca agregar la actualizacion de los valores
		getSubsdelegate().actualizarAdicionalyImporteTotal();
		
		// actualiza los labels
		valorSumadoPesos.setValue(String.valueOf( sumaTotalDeLoQueTenia));		
		float tot=valorFinalDeTodosLosCursos -  sumaTotalDeLoQueTenia;
		valorFaltantePesos.setValue(String.valueOf(tot ) ) ;
		
	}

	public Set<PagosPorSubscripcion> getProducts() {
		PagosPorSubscripcion dpp = null;
		Set<PagosPorSubscripcion> dpps = new HashSet<PagosPorSubscripcion>();
		for (Object row : productListBox.getItems()) {
			dpp = (PagosPorSubscripcion)((Listitem)row).getValue();
			dpps.add(dpp);
		}
		return dpps;
	}
	
	@Override
	protected GridCrud makeFields() {
		productCrudGrid = new GridCrud();
		productCrudGrid.setFixedLayout(true);
		productCrudGrid.setWidth("auto");
			
		pagoRapido =FormasDePagoViewHelper.getPagoRapido2ComboBox();
		pagoRapido.setConstraint("strict");
		pagoRapido.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt) {
				if(pagoRapido.getSelectedItem() != null ){
					
	   			   if(((PagoRapidoSubscripcionEnum)pagoRapido.getSelectedItem().getValue()).toInt() ==PagoRapidoSubscripcionEnum.TODO_EN_EFECTIVO.toInt()){
		   				formaDePago.setDisabled(true);
						cuotas.setDisabled(true);
						dinero.setDisabled(true);
					
						getGridList().removeAll();
						// FALTA ELIMINAR ACA LOS ANTERIORES
		   				PagosPorSubscripcion dpp = new PagosPorSubscripcion();
		   				
		   				dpp.setIdTipoDePago(FormasDePagoSubscripcionEnum.EFECTIVO);
		   				dpp.setEsCopago(false);
		   				if(subsdelegate != null){
		   					dpp.setCantidadDinero(subsdelegate.getValorCurso());
		   				}else{
		   					dpp.setCantidadDinero(subsAnulardelegate.getValorCurso());
		   				}
		   				valorSumadoPesos.setValue(String.valueOf(dpp.getCantidadDinero()));
		   				valorFaltantePesos.setValue("0") ;	
		   				
		   				if (Sessions.getCurrent().getAttribute("idSubs") != null) {
		   					Subscripcion cur=(Subscripcion)Sessions.getCurrent().getAttribute("idSubs"); 
		   					dpp.setSubscripcion(cur);
		   				}
		   				Listitem item =createItem(dpp);
		   				getGridList().addRow(item);	
		   				if(getSubsdelegate() != null)
		   					getSubsdelegate().actualizarAdicionalyImporteTotal();
		   				
					}else if(((PagoRapidoSubscripcionEnum)pagoRapido.getSelectedItem().getValue()).toInt() ==PagoRapidoSubscripcionEnum.TODO_CON_DEBITO.toInt()){
						formaDePago.setDisabled(true);
						cuotas.setDisabled(true);
						dinero.setDisabled(true);
						
						getGridList().removeAll();
						// FALTA ELIMINAR ACA LOS ANTERIORES
						PagosPorSubscripcion dpp = new PagosPorSubscripcion();
		   				
		   				dpp.setIdTipoDePago(FormasDePagoSubscripcionEnum.DEBITO);
		   				dpp.setEsCopago(false);
		   				if(subsdelegate != null){
		   					dpp.setCantidadDinero(subsdelegate.getValorCurso());
		   				}else{
		   					dpp.setCantidadDinero(subsAnulardelegate.getValorCurso());
		   				}
		   				valorSumadoPesos.setValue(String.valueOf(dpp.getCantidadDinero()));
		   				valorFaltantePesos.setValue("0") ;	
		
		   				
		   				if (Sessions.getCurrent().getAttribute("idSubs") != null) {
		   					Subscripcion cur=(Subscripcion)Sessions.getCurrent().getAttribute("idSubs"); 
		   					dpp.setSubscripcion(cur);
		   				}
		   				Listitem item =createItem(dpp);
		   				getGridList().addRow(item);	
		   				if(getSubsdelegate() != null)
		   					getSubsdelegate().actualizarAdicionalyImporteTotal();
		   				
					}else if(((PagoRapidoSubscripcionEnum)pagoRapido.getSelectedItem().getValue()).toInt() ==PagoRapidoSubscripcionEnum.CUOTAS_1_CON_TARJETA.toInt()){
						formaDePago.setDisabled(true);
						cuotas.setDisabled(true);
						dinero.setDisabled(true);
						
						getGridList().removeAll();
						// FALTA ELIMINAR ACA LOS ANTERIORES
						PagosPorSubscripcion dpp = new PagosPorSubscripcion();
						dpp.setEsCopago(false);
		   				dpp.setIdTipoDePago(FormasDePagoSubscripcionEnum.TARJETA_15);
		   				if(subsdelegate != null){
		   					dpp.setCantidadDinero(subsdelegate.getValorCurso());
		   				}else{
		   					dpp.setCantidadDinero(subsAnulardelegate.getValorCurso());
		   				}

		   				int adicional=0;
		   				if(dpp.getPorcInteres() != null){
		   					adicional=(int)(dpp.getCantidadDinero() * dpp.getPorcInteres() )/100;
		   				}else{
		   					adicional=(int)(dpp.getCantidadDinero() * 15 )/100;
		   				}

		   				dpp.setAdicional(adicional);
		   				dpp.setCantCuotas(1);
		   				valorSumadoPesos.setValue(String.valueOf(dpp.getCantidadDinero() +adicional));
		   				valorFaltantePesos.setValue("0") ;	
		   				
		   				if (Sessions.getCurrent().getAttribute("idSubs") != null) {
		   					Subscripcion cur=(Subscripcion)Sessions.getCurrent().getAttribute("idSubs"); 
		   					dpp.setSubscripcion(cur);
		   				}
		   				Listitem item =createItem(dpp);
		   				getGridList().addRow(item);	
		   				if(getSubsdelegate() != null)
		   					getSubsdelegate().actualizarAdicionalyImporteTotal();
		   				
//					}else if(((PagoRapidoSubscripcionEnum)pagoRapido.getSelectedItem().getValue()).toInt() ==PagoRapidoSubscripcionEnum.CUOTAS_3_CON_TARJETA.toInt()){
//						formaDePago.setDisabled(true);
//						cuotas.setDisabled(true);
//						dinero.setDisabled(true);
//						
//						getGridList().removeAll();
//						// FALTA ELIMINAR ACA LOS ANTERIORES
//						PagosPorSubscripcion dpp = new PagosPorSubscripcion();
//		   				
//		   				dpp.setIdTipoDePago(FormasDePagoSubscripcionEnum.TARJETA_15);
//		   				if(subsdelegate != null){
//		   					dpp.setCantidadDinero(subsdelegate.getValorCurso());
//		   				}else{
//		   					dpp.setCantidadDinero(subsAnulardelegate.getValorCurso());
//		   				}
//		   				dpp.setCantCuotas(3);
//		   				int adicional=0;
//		   				if(dpp.getPorcInteres() != null){
//		   					adicional=(int)(dpp.getCantidadDinero() * dpp.getPorcInteres() )/100;
//		   				}else{
//		   					adicional=(int)(dpp.getCantidadDinero() * 15 )/100;
//		   				}
//
//		   				dpp.setAdicional(adicional);
//		   				valorSumadoPesos.setValue(String.valueOf(dpp.getCantidadDinero() +adicional));
//		   				valorFaltantePesos.setValue("0") ;			   				
//		   				
//		   				if (Sessions.getCurrent().getAttribute("idSubs") != null) {
//		   					Subscripcion cur=(Subscripcion)Sessions.getCurrent().getAttribute("idSubs"); 
//		   					dpp.setSubscripcion(cur);
//		   				}
//		   				Listitem item =createItem(dpp);
//		   				getGridList().addRow(item);	
//		   				if(getSubsdelegate() != null)
//		   					getSubsdelegate().actualizarAdicionalyImporteTotal();
//		   				
					}else if(((PagoRapidoSubscripcionEnum)pagoRapido.getSelectedItem().getValue()).toInt() ==PagoRapidoSubscripcionEnum.CUOTAS_6_CON_TARJETA.toInt()){
						formaDePago.setDisabled(true);
						cuotas.setDisabled(true);
						dinero.setDisabled(true);
						
						getGridList().removeAll();
						// FALTA ELIMINAR ACA LOS ANTERIORES
						PagosPorSubscripcion dpp = new PagosPorSubscripcion();
		   				
		   				dpp.setIdTipoDePago(FormasDePagoSubscripcionEnum.TARJETA_20);
		   				dpp.setEsCopago(false);
		   				if(subsdelegate != null){
		   					dpp.setCantidadDinero(subsdelegate.getValorCurso());
		   				}else{
		   					dpp.setCantidadDinero(subsAnulardelegate.getValorCurso());
		   				}
		   				dpp.setCantCuotas(3);
		   				int adicional=0;
		   				if(dpp.getPorcInteres() != null){
		   					adicional=(int)(dpp.getCantidadDinero() * dpp.getPorcInteres() )/100;
		   				}else{
		   					adicional=(int)(dpp.getCantidadDinero() * 20 )/100;
		   				}

		   				dpp.setAdicional(adicional);
		   				valorSumadoPesos.setValue(String.valueOf(dpp.getCantidadDinero() +adicional));
		   				valorFaltantePesos.setValue("0") ;			   				
		   				
		   				if (Sessions.getCurrent().getAttribute("idSubs") != null) {
		   					Subscripcion cur=(Subscripcion)Sessions.getCurrent().getAttribute("idSubs"); 
		   					dpp.setSubscripcion(cur);
		   				}
		   				Listitem item =createItem(dpp);
		   				getGridList().addRow(item);	
		   				if(getSubsdelegate() != null)
		   					getSubsdelegate().actualizarAdicionalyImporteTotal();
		   				
					}else if(((PagoRapidoSubscripcionEnum)pagoRapido.getSelectedItem().getValue()).toInt() ==PagoRapidoSubscripcionEnum.PAGO_COMBINADO.toInt()){
						formaDePago.setDisabled(false);
						//cuotas.setDisabled(false);
						dinero.setDisabled(false);
						getGridList().removeAll();

						valorSumadoPesos.setValue("0");
						int valor= 0;
						if(subsdelegate != null){
							valor= subsdelegate.getValorCurso();
		   				}else{
		   					valor= subsAnulardelegate.getValorCurso();
		   				}
		   				valorFaltantePesos.setValue(String.valueOf(valor));	
		   				pagoRapido.setSelectedItem(pagoRapido.getSelectedItem());
//						getGridList().removeAll();
									
					}
				}
			}
		});	
		productCrudGrid.addField(new Label(I18N.getLabel("PagoRapido")), pagoRapido);	
		
		productCrudGrid.addField(new Label(" "), new Label(" "));

		formaDePago =FormasDePagoViewHelper.getComboBox();
		formaDePago.setDisabled(true);
		formaDePago.setConstraint("strict");
		formaDePago.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt) {
				if(formaDePago.getSelectedItem() != null &&
					((FormasDePagoSubscripcionEnum)formaDePago.getSelectedItem().getValue()).toInt()  ==
					 	FormasDePagoSubscripcionEnum.TARJETA_15.toInt()){
	//					cuotas= FormasDePagoViewHelper.getCantCuotasComboBox();
						cuotas.setDisabled(false);
					}else 				if(formaDePago.getSelectedItem() != null &&
							((FormasDePagoSubscripcionEnum)formaDePago.getSelectedItem().getValue()).toInt()  ==
						 	FormasDePagoSubscripcionEnum.TARJETA_20.toInt()){
		//					cuotas= FormasDePagoViewHelper.getCantCuotasComboBox();
							cuotas.setDisabled(false);
						}
					else{
						cuotas.setDisabled(true);
				//		cuotas= FormasDePagoViewHelper.getCantCuotasComboBox();
					}
				}
		});	
		productCrudGrid.addField(new Label(I18N.getLabel("formaDePago")), formaDePago);
	
		
		valorSumadoPesos= new Label();
		valorSumadoPesos.setMaxlength(500);
		Label aa2=new Label(I18N.getLabel("valorSumadoPesos"));
		aa2.setStyle("color: #04B404;");
		valorSumadoPesos.setStyle("color: #04B404;");
		productCrudGrid.addField(aa2,valorSumadoPesos);
			
		cuotas = FormasDePagoViewHelper.getCantCuotasComboBox();
		cuotas.setConstraint("strict");
		cuotas.setDisabled(true);
		productCrudGrid.addField(new Label(I18N.getLabel("cuota")),cuotas);

		valorFaltantePesos= new Label();
		valorFaltantePesos.setMaxlength(500);
		valorFaltantePesos.setStyle("color: #DF0101;");
		Label aa=new Label(I18N.getLabel("valorFaltantePesos"));
		aa.setStyle("color: #DF0101;");
		productCrudGrid.addField(aa,valorFaltantePesos);
		
		dinero = new Intbox();
		dinero.setDisabled(true);
		productCrudGrid.addField(new Label(I18N.getLabel("valor")),dinero);

		return productCrudGrid;
	}

	public static Combobox getComboBoxActividad(List<Actividad> actividades, Combobox act) {
		Constraint brandC = act.getConstraint();
		act.setConstraint("");
		act.setText("");
		act.setConstraint(brandC);
		act.getItems().clear();
		
		for (Actividad actividad : actividades) {
			Comboitem item;
			item = new Comboitem(actividad.getNombre());
			item.setValue(actividad);
			// cb.appendChild(item);
			act.appendChild(item);		
		}

		return act;
	}
	

	@Override
	protected GridList makeList() {
		productListBox = new GridList();
		productListBox.setMultiple(false);
		productListBox.setPageSize(5);
		productListBox.addHeader((new Listheader(I18N.getLabel("formaDePago"))));
		productListBox.addHeader((new Listheader(I18N.getLabel("cuota"))));
		productListBox.addHeader((new Listheader(I18N.getLabel("valor"))));
		productListBox.addHeader((new Listheader(I18N.getLabel("comentario"))));
		productListBox.setHeight("150px"); 
		return productListBox;
	}
	
	public void setSelectedFormaDePago (FormasDePagoSubscripcionEnum selectedHPType){
		boolean found = false;
		int i = 0;
		while (!found && i<formaDePago.getItemCount()){
			if(selectedHPType.toInt() == (((FormasDePagoSubscripcionEnum) formaDePago.getItemAtIndex(i).getValue()).toInt())){
				found = true;
				formaDePago.setSelectedIndex(i);
			}
			i++;
		}
	}
	
	public void setSelectedCuotas (int selectedHPType){
		boolean found = false;
		int i = 0;
		while (!found && i<cuotas.getItemCount()){
			if(selectedHPType  == (((Integer) cuotas.getItemAtIndex(i).getValue()))){
				found = true;
				cuotas.setSelectedIndex(i);
			}
			i++;
		}
	}
	

	@Override
	public void updateFields() {
		if (productListBox.getSelectedItem() == null)
			return;
		PagosPorSubscripcion dpp =(PagosPorSubscripcion)productListBox.getSelectedItem().getValue();
		
		//setSelectedTipoActividad(TipoCursoEnum.fromInt(dpp.getActiv().getIdTipoCurso().toInt()));		
		setSelectedFormaDePago(dpp.getIdTipoDePago());

		if(dpp.getIdTipoDePago() != null  && 
				(dpp.getIdTipoDePago()).toInt() == FormasDePagoSubscripcionEnum.TARJETA_15.toInt())  
			setSelectedCuotas(dpp.getCantCuotas());
		else 		if(dpp.getIdTipoDePago() != null  && 
				(dpp.getIdTipoDePago()).toInt() == FormasDePagoSubscripcionEnum.TARJETA_20.toInt())  
			setSelectedCuotas(dpp.getCantCuotas());
		
		dinero.setValue((int) dpp.getCantidadDinero());
	}
	
	public void updateProductTotalDelivery() {
		Set<PagosPorSubscripcion> dppBackup = getProducts();
		this.getGridList().removeAll();
		getProducts().addAll(dppBackup);
		this.fillListBox(dppBackup);
	}

	@Override
	protected PagosPorSubscripcion updateList() {
		
		if (productListBox.getSelectedItem() == null)
			return null;
		
		if(formaDePago.getSelectedItem() == null || 
				(formaDePago.getSelectedItem() != null  && 
					((FormasDePagoSubscripcionEnum)formaDePago.getSelectedItem().getValue()).toInt() == FormasDePagoSubscripcionEnum.TARJETA_15.toInt()  
						&&  cuotas.getSelectedItem() == null))
					return null;
		else 		if(formaDePago.getSelectedItem() == null || 
				(formaDePago.getSelectedItem() != null  && 
				((FormasDePagoSubscripcionEnum)formaDePago.getSelectedItem().getValue()).toInt() == FormasDePagoSubscripcionEnum.TARJETA_20.toInt()  
					&&  cuotas.getSelectedItem() == null))
				return null;

		if(formaDePago.getSelectedItem() == null ||  dinero.getValue() == null)
			return null;
		
		
		PagosPorSubscripcion newActionProduct =(PagosPorSubscripcion)productListBox.getSelectedItem().getValue();

		int valorFinalDeTodosLosCursos=getSubsdelegate().getValorCurso();
		
		int sumaTotalDeLoQueTenia=0;
		List<?> rows = productListBox.getItems();
		for (int i = 0; i < rows.size(); i++) {
			Listitem rowProduct22 = (Listitem) rows.get(i);
			PagosPorSubscripcion dpp = (PagosPorSubscripcion)rowProduct22.getValue();
			if(dpp.getAdicional() == newActionProduct.getAdicional() && dpp.getCantidadDinero() == newActionProduct.getCantidadDinero()
					&& dpp.getCantCuotas() == newActionProduct.getCantCuotas() && 
					(dpp.getIdTipoDePago() != null && newActionProduct.getIdTipoDePago() != null &&  
						dpp.getIdTipoDePago() == newActionProduct.getIdTipoDePago())){
				
			}else{
				sumaTotalDeLoQueTenia= sumaTotalDeLoQueTenia +dpp.getCantidadDinero();	
			}
		}
				
		newActionProduct.setIdTipoDePago((FormasDePagoSubscripcionEnum)formaDePago.getSelectedItem().getValue());
		newActionProduct.setCantidadDinero(dinero.getValue());
		newActionProduct.setEsCopago(false);
		if(formaDePago.getSelectedItem() != null  && 
				((FormasDePagoSubscripcionEnum)formaDePago.getSelectedItem().getValue()).toInt() == FormasDePagoSubscripcionEnum.TARJETA_15.toInt())  
			newActionProduct.setCantCuotas((Integer)cuotas.getSelectedItem().getValue());
		
		else 		if(formaDePago.getSelectedItem() != null  && 
				((FormasDePagoSubscripcionEnum)formaDePago.getSelectedItem().getValue()).toInt() == FormasDePagoSubscripcionEnum.TARJETA_20.toInt())  
			newActionProduct.setCantCuotas((Integer)cuotas.getSelectedItem().getValue());

		if (Sessions.getCurrent().getAttribute("idSubs") != null) {
			Subscripcion cur=(Subscripcion)Sessions.getCurrent().getAttribute("idSubs"); 
			newActionProduct.setSubscripcion(cur);
		}
		
		
		sumaTotalDeLoQueTenia = sumaTotalDeLoQueTenia + newActionProduct.getCantidadDinero();
		
		
		// si ya habia llegado al valor, no agrego nada
		if(sumaTotalDeLoQueTenia == valorFinalDeTodosLosCursos && sumaTotalDeLoQueTenia > 0){
			return null;
		}
		
		// si se supera del pago total, agrego solamente la parte que falta
		int valorQueSeEstaAgregando=newActionProduct.getCantidadDinero();
		int sumaTotal= valorQueSeEstaAgregando + sumaTotalDeLoQueTenia;
		boolean superaValor= false;
		int faltante=0;
		if(sumaTotal > valorFinalDeTodosLosCursos){
			superaValor= true;
			int aDescontarDeloqueseseteo=sumaTotal -valorFinalDeTodosLosCursos;
			faltante=valorQueSeEstaAgregando-aDescontarDeloqueseseteo;
			newActionProduct.setEsCopago(false);
			newActionProduct.setCantidadDinero(faltante);
			if(newActionProduct.getAdicional() > 0){
				newActionProduct.setCantidadDinero(faltante);	
				int adicional=0;
				if(newActionProduct.getPorcInteres() != null){
					adicional=(int)(newActionProduct.getCantidadDinero() * newActionProduct.getPorcInteres() )/100;
				}else{
					adicional=(int)(newActionProduct.getCantidadDinero() * 15 )/100;
				}
				newActionProduct.setAdicional(adicional);
			}
		}
		
		if(superaValor){
			valorSumadoPesos.setValue(String.valueOf(faltante + sumaTotalDeLoQueTenia));		
			float tot=valorFinalDeTodosLosCursos - faltante - sumaTotalDeLoQueTenia;
			valorFaltantePesos.setValue(String.valueOf(tot ) ) ;	
		}else{
			valorSumadoPesos.setValue(String.valueOf(valorQueSeEstaAgregando + sumaTotalDeLoQueTenia));		
			float tot=valorFinalDeTodosLosCursos - valorQueSeEstaAgregando - sumaTotalDeLoQueTenia;
			valorFaltantePesos.setValue(String.valueOf(tot ) ) ;	
		}
//		actualizarTipoCurso();
		return newActionProduct;
	}
	
	
	@Override
	protected boolean validateCrud() {
			
		// si no se selecciono ningun curso arrojo error
		if(subsdelegate != null && !subsdelegate.seSeleccionoCurso()){
			throw new WrongValueException(getFormaDePago(), "Seleccione correctamente");
		}else if(subsAnulardelegate != null && !subsAnulardelegate.seSeleccionoElCurso()){
			throw new WrongValueException(getFormaDePago(), "Seleccione correctamente");
		}
		
		
		if (getFormaDePago().getSelectedItem()==null) {
				throw new WrongValueException(getFormaDePago(), I18N.getLabel("error.empty.field"));
		}
		
		if(getFormaDePago().getSelectedItem() != null  && 
				((FormasDePagoSubscripcionEnum)getFormaDePago().getSelectedItem().getValue()).toInt() == FormasDePagoSubscripcionEnum.TARJETA_15.toInt() 
					&& getCuotas().getSelectedItem() == null)  
				throw new WrongValueException(getCuotas(), I18N.getLabel("error.empty.field"));

		if(getFormaDePago().getSelectedItem() != null  && 
				((FormasDePagoSubscripcionEnum)getFormaDePago().getSelectedItem().getValue()).toInt() == FormasDePagoSubscripcionEnum.TARJETA_20.toInt() 
					&& getCuotas().getSelectedItem() == null)  
				throw new WrongValueException(getCuotas(), I18N.getLabel("error.empty.field"));

		if (getDinero().getValue()==null) {
			throw new WrongValueException(getDinero(), I18N.getLabel("error.empty.field"));
		}
		
//		if(subsAnulardelegate != null && !subsAnulardelegate.seSeleccionoElCurso()){
//			// valido que 
//			
//			
//		}

		return true;
	}
	
	
	public String getUpdateMethod() {
		return updateMethod;
	}
	public void setUpdateMethod(String updateMethod) {
		this.updateMethod = updateMethod;
	}

	public Combobox getFormaDePago() {
		return formaDePago;
	}

	public void setFormaDePago(Combobox formaDePago) {
		this.formaDePago = formaDePago;
	}

	public Combobox getCuotas() {
		return cuotas;
	}

	public void setCuotas(Combobox cuotas) {
		this.cuotas = cuotas;
	}

	public Intbox getDinero() {
		return dinero;
	}

	public SubscripcionDelegate getSubsdelegate() {
		return subsdelegate;
	}

	public void setSubsdelegate(SubscripcionDelegate subsdelegate) {
		this.subsdelegate = subsdelegate;
	}

	public void setDinero(Intbox dinero) {
		this.dinero = dinero;
	}

	public Label getValorSumadoPesos() {
		return valorSumadoPesos;
	}

	public void setValorSumadoPesos(Label valorSumadoPesos) {
		this.valorSumadoPesos = valorSumadoPesos;
	}

	public Label getValorFaltantePesos() {
		return valorFaltantePesos;
	}

	public void setValorFaltantePesos(Label valorFaltantePesos) {
		this.valorFaltantePesos = valorFaltantePesos;
	}

	public AnularSubscripcionDelegate getSubsAnulardelegate() {
		return subsAnulardelegate;
	}

	public void setSubsAnulardelegate(AnularSubscripcionDelegate subsAnulardelegate) {
		this.subsAnulardelegate = subsAnulardelegate;
	}

	public GridCrud getProductCrudGrid() {
		return productCrudGrid;
	}

	public void setProductCrudGrid(GridCrud productCrudGrid) {
		this.productCrudGrid = productCrudGrid;
	}

	public GridList getProductListBox() {
		return productListBox;
	}

	public void setProductListBox(GridList productListBox) {
		this.productListBox = productListBox;
	}

	public Combobox getPagoRapido() {
		return pagoRapido;
	}

	public void setPagoRapido(Combobox pagoRapido) {
		this.pagoRapido = pagoRapido;
	}

	@Override
	protected Listitem createItem2(ClienteNoEncontradoEnPiletaHistorico item) {
		// TODO Auto-generated method stub
		return null;
	}

}