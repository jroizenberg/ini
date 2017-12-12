package com.institucion.model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;

import com.institucion.bz.CursoEJB;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.view.PanelRoutingCrud;
import com.institucion.fm.desktop.view.RequiredLabel;

public class PulsoClinicaResumenDetalladoTAB2List extends PanelRoutingCrud<PulsoClinicaDetalle> {
	private static final long serialVersionUID = 1L;
	private CursoEJB cursoEJB;
	
	public int ACTIVIDAD_MOVIMIENTO_CAJA=98;
	public int ACTIVIDAD_VENTA_PRODUCTOS=99;
	
	public PulsoClinicaResumenDetalladoTAB2List() {
		super();
		this.setCheckbox();
		this.setUIEvent();
		this.setDisable(false);
	}
	
	@Override
	protected boolean setPanelTitle() {

		this.addAuxheader("  ", 2, 1, "30%");
		this.addColumn(" ", true, "25%");
		this.addColumn(" ", true, "20%");

		this.addAuxheader("JULIO", 2, 1, "15%");
		this.addColumn("Efect", true, "15%");
		this.addColumn("Obra S", true, "15%");
		
		this.addAuxheader("AGOSTO", 2, 1, "15%");
		this.addColumn("Efect", true, "15%");
		this.addColumn("Obra S", true, "15%");

		this.addAuxheader("SEPT", 2, 1, "15%");
		this.addColumn("Efect", true, "15%");
		this.addColumn("Obra S", true, "15%");

		this.addAuxheader("OCTUB", 2, 1, "15%");
		this.addColumn("Efect", true, "15%");
		this.addColumn("Obra S", true, "15%");

		this.addAuxheader("NOVIEM", 2, 1, "15%");
		this.addColumn("Efect", true, "15%");
		this.addColumn("Obra S", true, "15%");
		
		this.addAuxheader("DIC", 2, 1, "15%");
		this.addColumn("Efect", true, "15%");
		this.addColumn("Obra S", true, "15%");
				
		return false;//esto se usa para no dibujar el ruteo
	}
	
	@Override
	protected void doEvent(Event event) {

	}
	
	
	public boolean esPar(int x) {
		if ((x % 2) == 0) {
			return true;
		}
		return false;
	}

	private  String setearColor(int posicion){		
		if(esPar(posicion)){
			return "background-color: #E6E6E6;";  
		}else{
			return "background-color: #FAFAFA;";  
		}
	} 

	public void setField(List<PulsoClinicaDetalle> productosList, List<Integer> actvidadesTodasList) {
		cleanRows();
			
		int i=0;

		for (Integer integer : actvidadesTodasList) {
			
			Row row =this.getCurrentRow2(setearColor(i)); 
			row.setStyle(setearColor(i));
			seteoPrimeraFila(integer, productosList);
			
			Row row2 =this.getCurrentRow2(setearColor(i)); 
			row2.setStyle(setearColor(i));
			if(integer != null){
				if(integer.intValue() ==  ACTIVIDAD_VENTA_PRODUCTOS){
					seteoSegundaFila(integer, productosList, true, false);		
				}else if(integer.intValue() ==  ACTIVIDAD_MOVIMIENTO_CAJA){
					seteoSegundaFila(integer, productosList, false, true);
				}else
					seteoSegundaFila(integer, productosList, false, false);
			}else{
				seteoSegundaFila(integer, productosList, false, false);
			}		
			
			Row row3 =this.getCurrentRow2(setearColor(i)); 
			row3.setStyle(setearColor(i));
			seteoTerceraFila(integer, productosList);
			i++;

			
			Row row4 =this.getCurrentRow2(setearColor(i)); 
			row4.setStyle(setearColor(i));
			seteoCuartaFila(integer, productosList);
			i++;
		}	
	}
	
	private void seteoCuartaFila(Integer idActividad, List<PulsoClinicaDetalle> productosList){
		int posicion=1;
		this.addField(new Label(" "));	
		 DecimalFormat formateador = new DecimalFormat("###,###"); 

		 RequiredLabel total=new RequiredLabel("TOT consolidado");
		total.setStyle("font-weight:bold; text-align:right;");
		this.addField(total);
		
		// Busca el Objeto para el integer de IDActividad y el Mes en Cuistion
		if(productosList != null){
			for (PulsoClinicaDetalle prod : productosList) {
				
				// llego al mes en el que estoy parado
				if(prod.getMesId() == posicion ){
					if(prod.getList() != null  && posicion >= 7 ){
						
						for (PulsoClinicaDetalleParte2 pulsoClinicaDetalle : prod.getList()) {
							
							// si estoy parado ahora en la actividad Fila/Columna deseada
							if(pulsoClinicaDetalle.getIdActividad() == idActividad.intValue()){
								RequiredLabel lab= null;
								Double cantidad= (double)0;
								
								if(pulsoClinicaDetalle.getCantidadTotal() != null ){
									cantidad=cantidad + pulsoClinicaDetalle.getCantidadTotal();
								}
								if(pulsoClinicaDetalle.getCantidadIngresoObraSocial() != null  && 
										pulsoClinicaDetalle.getCantidadIngresoObraSocial() > 0){
									cantidad=cantidad + pulsoClinicaDetalle.getCantidadIngresoObraSocial();
								}
								lab= new RequiredLabel("$"+formateador.format(cantidad));
								lab.setStyle("font-weight:bold;");
								this.addField(lab);

								Label lab2= new Label("");
								lab2.setStyle("font-weight:bold;");
								this.addField(lab2);								
								break;
							}
							
						}								
					}
					posicion= posicion +1;

				}
			}
		}
		super.addRow(" border-top: 3px solid #131212;  ");		
	}
	private CursoEJB getServiceCurso(){
		if(cursoEJB == null)
			cursoEJB = BeanFactory.<CursoEJB>getObject("fmEjbCurso");
		return cursoEJB;
	}
	
	private void seteoPrimeraFila(Integer idActividad, List<PulsoClinicaDetalle> productosList){
		int posicion=1;
		 DecimalFormat formateador = new DecimalFormat("###,###"); 

		// Seteo Primero la Actividad
		if(idActividad != null){
			String actividad="";
			if(idActividad.intValue() ==  ACTIVIDAD_VENTA_PRODUCTOS){
				actividad= "Venta Productos";
				
			}else if(idActividad.intValue() ==  ACTIVIDAD_MOVIMIENTO_CAJA){
				actividad= "Mov en Caja";
				
			}else{
				Actividad act=getServiceCurso().findByIdActividad(idActividad.longValue());
				actividad= act.getNombre();		
			}
			
			Label mes =new Label(actividad);
			mes.setStyle("font-weight: bold;");
			this.addField(mes);
		}else
			this.addField(new Label(" "));	

		this.addField(new Label("Ingresos"));

		if(productosList != null){
			for (PulsoClinicaDetalle prod : productosList) {
				
				// llego al mes en el que estoy parado
				if(prod.getMesId() == posicion ){
					if(prod.getList() != null  && posicion >= 7){
						
						for (PulsoClinicaDetalleParte2 pulsoClinicaDetalle : prod.getList()) {
							
							// si estoy parado ahora en la actividad Fila/Columna deseada
							if(pulsoClinicaDetalle.getIdActividad() == idActividad.intValue()){
								Label lab;
								
								if(pulsoClinicaDetalle.getCantidadIngresoEfectivo() != null){
									lab= new Label("$"+formateador.format(pulsoClinicaDetalle.getCantidadIngresoEfectivo()));
								}else{
									lab= new Label("$"+formateador.format(0));
								}
								this.addField(lab);
								
			
								if(pulsoClinicaDetalle.getCantidadIngresoObraSocial() != null && 
										pulsoClinicaDetalle.getCantidadIngresoObraSocial() > 0){
									
									Label lab2= null;

									lab2= new Label("$"+formateador.format(pulsoClinicaDetalle.getCantidadIngresoObraSocial()));
									this.addField(lab2);
								}else
									this.addField(new Label(" "));
								
								break;
							}
							
						}								
					}
					posicion= posicion +1;

				}
			}
		}			
		super.addRow();		
	}

	private void seteoSegundaFila(Integer idActividad, List<PulsoClinicaDetalle> productosList, boolean esVentaProductos, boolean esActMov){
		 DecimalFormat formateador = new DecimalFormat("###,###"); 
		 int posicion=1;
		this.addField(new Label(" "));	

		if(esVentaProductos)
			this.addField(new Label("Costo Productos"));
		else if(esActMov)
			this.addField(new Label("Egreso Caja"));
		else
			this.addField(new Label("Egresos Sueldos"));
		
		// Busca el Objeto para el integer de IDActividad y el Mes en Cuistion
		if(productosList != null){
			for (PulsoClinicaDetalle prod : productosList) {
				
				// llego al mes en el que estoy parado
				if(prod.getMesId() == posicion ){
					if(prod.getList() != null  && posicion >= 7){
						
						for (PulsoClinicaDetalleParte2 pulsoClinicaDetalle : prod.getList()) {
							
							// si estoy parado ahora en la actividad Fila/Columna deseada
							if(pulsoClinicaDetalle.getIdActividad() == idActividad.intValue()){
									
									if(pulsoClinicaDetalle.getCantidadEgresoRRHH() != null && 
											pulsoClinicaDetalle.getCantidadEgresoRRHH().intValue() > 0)
										this.addField(new Label("$-"+formateador.format(pulsoClinicaDetalle.getCantidadEgresoRRHH())));
									else
										this.addField(new Label("$"+formateador.format(0)));
				
									this.addField(new Label(" "));
									
									break;
							}
							
						}								
					}
					posicion= posicion +1;

				}
			}
			
		}
		super.addRow();		
	}

	
	private void seteoTerceraFila(Integer idActividad, List<PulsoClinicaDetalle> productosList){
		 DecimalFormat formateador = new DecimalFormat("###,###"); 
		 int posicion=1;
		this.addField(new Label(" "));	

		Label total=new Label("Subtotal");
		total.setStyle("font-weight:bold; text-align:right;");
		this.addField(total);
	
		// Busca el Objeto para el integer de IDActividad y el Mes en Cuistion
		if(productosList != null){
			for (PulsoClinicaDetalle prod : productosList) {
				
				// llego al mes en el que estoy parado
				if(prod.getMesId() == posicion ){
					if(prod.getList() != null  && posicion >= 7){
						
						for (PulsoClinicaDetalleParte2 pulsoClinicaDetalle : prod.getList()) {
							
							// si estoy parado ahora en la actividad Fila/Columna deseada
							if(pulsoClinicaDetalle.getIdActividad() == idActividad.intValue()){
								Label lab= null;
								if(pulsoClinicaDetalle.getCantidadTotal() != null ){
									lab= new Label("$"+formateador.format(pulsoClinicaDetalle.getCantidadTotal()));
									lab.setStyle("font-weight:bold;");
								}else{
									lab= new Label("$"+formateador.format(0));
									lab.setStyle("font-weight:bold;");
								}
								this.addField(lab);

								Label lab2= null;

								if(pulsoClinicaDetalle.getCantidadIngresoObraSocial() != null  && 
										pulsoClinicaDetalle.getCantidadIngresoObraSocial() > 0){
									
									lab2= new Label("$"+formateador.format(pulsoClinicaDetalle.getCantidadIngresoObraSocial()));
									lab2.setStyle("font-weight:bold;");
								}else{
									lab2= new Label(" ");
									lab2.setStyle("font-weight:bold;");
								}
								this.addField(lab2);
								
								break;
							}
							
						}								
					}
					posicion= posicion +1;

				}
			}
		
		}
		super.addRow(" border-top: 3px solid #131212;  ");		
	}
	
	@Override
	protected void setUIEvent() {
		this.setUievent(true);

	}

	@Override
	protected void setCheckbox() {
		this.setCheckbox(false);
	}


	@SuppressWarnings("unchecked")
	public void deleteRow() {

	}
	
	@SuppressWarnings("unchecked")
	public void cleanRows() {
		Rows rows =super.getRows();
		for (Iterator it = new ArrayList(rows.getChildren()).iterator(); it.hasNext();) {
			rows.removeChild((Component)it.next());
		} 
		super.addRow();
	}
}

