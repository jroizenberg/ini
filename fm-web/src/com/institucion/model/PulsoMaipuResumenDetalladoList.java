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

import com.institucion.bz.ClaseEJB;
import com.institucion.bz.CursoEJB;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.view.PanelRoutingCrud;
import com.institucion.fm.desktop.view.RequiredLabel;

public class PulsoMaipuResumenDetalladoList extends PanelRoutingCrud<PulsoClinicaDetalle> {
	private static final long serialVersionUID = 1L;
	private CursoEJB cursoEJB;
	private ClaseEJB claseEJB;

	public PulsoMaipuResumenDetalladoList() {
		super();
		this.setCheckbox();
		this.setUIEvent();
		this.setDisable(false);
		
	}
	
	@Override
	protected boolean setPanelTitle() {
			claseEJB = BeanFactory.<ClaseEJB>getObject("fmEjbClase");
			this.addAuxheader("  ", 2, 1, "30%");
			this.addColumn(" ", true, "25%");
			this.addColumn(" ", true, "20%");
			
			List<Quincena> quincenaList=claseEJB.findAllConActividadTomaListaDelDiaNombreCurso();

			this.addAuxheader(" ", quincenaList.size(), 1, "60%");

			for (Quincena quincena : quincenaList) {
				this.addColumn(quincena.getNombre(), true, "15%");
			}
			return false;//esto se usa para no dibujar el ruteo
	}
	
	@Override
	protected void doEvent(Event event) {

	}
	
	private boolean esCursoDePagoSueldos(List<PulsoMaipuDetalle> productosList, Integer idCurso){
		int posicion=1;
		// Busca el Objeto para el integer de IDActividad y el Mes en Cuistion
			if(productosList != null){
				for (PulsoMaipuDetalle prod : productosList) {
					
					// llego al mes en el que estoy parado
					if(prod.getPosicionId() == posicion ){
						
//						como saber aca que la quincena con la que estoy trabajando esta ordenada, o sea, estoy por mostrarla en la posicion correcta
						
						if(prod.getList() != null){
							
							for (PulsoMaipuDetalleParte2 pulsoClinicaDetalle : prod.getList()) {
								
								// si estoy parado ahora en la actividad Fila/Columna deseada
								if(pulsoClinicaDetalle.getIdCurso() == idCurso.intValue() 
										&&  pulsoClinicaDetalle.isEsCursoQueLLevaSueldoYnoEgresos()){
										return true;
								}
							}								
						}
						posicion= posicion +1;
					}
				}
			}
			return false;
		}

	
	private void actualizoGastosEncursos(List<PulsoMaipuDetalle> productosList, int quincenaID, Integer egresoGastos, GastosMaipuEnum gastoTipo){
		
		if(productosList != null && egresoGastos != null && egresoGastos.intValue() > 0){
			for (PulsoMaipuDetalle pulsoMaipuDetalle : productosList) {
				
				if(pulsoMaipuDetalle.getQuincenaId() ==quincenaID ){
					if(pulsoMaipuDetalle.getList() != null){
						for (PulsoMaipuDetalleParte2 detallado : pulsoMaipuDetalle.getList()) {
								Curso cc=getServiceCurso().findById(new Long(detallado.getIdCurso()));
								
								if(gastoTipo.toInt() == GastosMaipuEnum.CANTINA.toInt() && 
										cc != null && cc.getNombre().equalsIgnoreCase("INI VERANO-CANTINA")){
									
									detallado.setCantidadEgresoRRHH(detallado.getCantidadEgresoRRHH() + egresoGastos.intValue());
								}else if(gastoTipo.toInt() == GastosMaipuEnum.SUELDOS.toInt() && 
										cc != null && cc.getNombre().equalsIgnoreCase("INI VERANO")){
									detallado.setCantidadEgresoRRHH(detallado.getCantidadEgresoRRHH() +egresoGastos.intValue());
								}else if(gastoTipo.toInt() == GastosMaipuEnum.TRANSPORTE.toInt() && 
										cc != null && cc.getNombre().equalsIgnoreCase("INI VERANO-TRANSPORTE")){
									detallado.setCantidadEgresoRRHH(detallado.getCantidadEgresoRRHH() +egresoGastos.intValue());
								}
								
						}
					}
				}
			}
		}
	}		
	
	
	
	public void setField(List<PulsoMaipuDetalle> productosList, List<Integer> cursosTodasList) {
		cleanRows();
		int i=0;
//		List<PulsoMaipuDetalle> productosList2= new ArrayList();
		// recorro todos los pulsosMaipu, y cuando el id de curso sea igual a 99, recorro eso y agrego esas salidas a cada curso correspondiente
		for (PulsoMaipuDetalle pulso : productosList) {
			if(pulso.getList() != null){
				for (PulsoMaipuDetalleParte2 detallado : pulso.getList()) {
					if(detallado.getIdCurso() == 99 && detallado.getCantidadEgresoRRHH() != null && detallado.getCantidadEgresoRRHH().intValue() >0){
						actualizoGastosEncursos(productosList, pulso.getQuincenaId(), detallado.getCantidadEgresoRRHH(), detallado.getTipoGasto());		
					}
				}
			}
		}
		
		for (Integer integer : cursosTodasList) {
				
			Row row =this.getCurrentRow2(setearColor(i)); 
			row.setStyle(setearColor(i));

			if(integer != null && integer.intValue() != 98 && integer.intValue() != 99){  // que sea distinto a ingresos/egresos
				seteoPrimeraFila(integer, productosList);
				Row row2 =this.getCurrentRow2(setearColor(i)); 
				row2.setStyle(setearColor(i));
				i++;
				
				Row row3 =this.getCurrentRow2(setearColor(i)); 
				row3.setStyle(setearColor(i));
				boolean esCurso= esCursoDePagoSueldos(productosList, integer);
				seteoSegundaFila(integer, productosList,esCurso );
				i++;

				Row row4 =this.getCurrentRow2(setearColor(i)); 
				row4.setStyle(setearColor(i));
				seteoTercerFila(integer, productosList);
				i++;
			}
		}
		
		if(cursosTodasList != null && cursosTodasList.size() >0){
			
			// seteo ingresos
			Row row =this.getCurrentRow2(setearColor(i)); 
			row.setStyle(setearColor(i));
			seteoIngreosEgresos(TipoMovimientoCajaEnum.INGRESO, productosList);
			i++;
			
			Row row2 =this.getCurrentRow2(setearColor(i)); 
			row2.setStyle(setearColor(i));
			seteoIngreosEgresos(TipoMovimientoCajaEnum.EGRESO, productosList);
			i++;
	
			Row row5 =this.getCurrentRow2(setearColor(i)); 
			row5.setStyle(setearColor(i));
			seteoUnaVacia();
			
			Row row11 =this.getCurrentRow2(setearColor(i)); 
			row11.setStyle(setearColor(i));
			i++;
			seteoUnaVacia();
			Row row12 =this.getCurrentRow2(setearColor(i)); 
			row12.setStyle(setearColor(i));
			i++;
			seteoUltimaFila(productosList);
			
			Row row6 =this.getCurrentRow2(setearColor(i)); 
			row6.setStyle(setearColor(i));
			i++;
		}
	}
	
	private void seteoUnaVacia(){
		this.addField(new Label(" "));	
//		this.addField(new Label(" "));	
//		this.addField(new Label(" "));
//		this.addField(new Label(" "));
		super.addRow();		
	}
	
	private void seteoSegundaFila(Integer idCurso, List<PulsoMaipuDetalle> productosList, boolean esCursoDePagoSueldos){
		int posicion=1;
		this.addField(new Label(" "));	
		 DecimalFormat formateador = new DecimalFormat("###,###"); 

		 if(esCursoDePagoSueldos)
			 this.addField(new Label("Egresos Sueldos"));
		 else
			 this.addField(new Label("Gastos"));
		
		// Busca el Objeto para el integer de IDActividad y el Mes en Cuistion
			if(productosList != null){
				for (PulsoMaipuDetalle prod : productosList) {
					
					// llego al mes en el que estoy parado
					if(prod.getPosicionId() == posicion ){
						
//						como saber aca que la quincena con la que estoy trabajando esta ordenada, o sea, estoy por mostrarla en la posicion correcta
						
						if(prod.getList() != null){
							
							for (PulsoMaipuDetalleParte2 pulsoClinicaDetalle : prod.getList()) {
								
								// si estoy parado ahora en la actividad Fila/Columna deseada
								if(pulsoClinicaDetalle.getIdCurso() == idCurso.intValue()){
									Label lab= null;
									
									if(pulsoClinicaDetalle.getCantidadEgresoRRHH() != null && 
											pulsoClinicaDetalle.getCantidadEgresoRRHH().intValue() > 0){
										
										lab= new Label("$-"+formateador.format(pulsoClinicaDetalle.getCantidadEgresoRRHH()));
									}else{
										lab= new Label("$"+formateador.format(0));
									}
									this.addField(lab);
																	
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
	
	private CursoEJB getServiceCurso(){
		if(cursoEJB == null)
			cursoEJB = BeanFactory.<CursoEJB>getObject("fmEjbCurso");
		return cursoEJB;
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

	
	private void seteoTercerFila(Integer idCurso, List<PulsoMaipuDetalle> productosList){
		int posicion=1;

		this.addField(new Label(" "));
		
		Label tot =new Label("Tot");
		tot.setStyle("font-weight: bold;");
		this.addField(tot);
			
		 DecimalFormat formateador = new DecimalFormat("###,###"); 

		if(productosList != null){
			for (PulsoMaipuDetalle prod : productosList) {
				
				// llego al mes en el que estoy parado
				if(prod.getPosicionId() == posicion ){
					
					if(prod.getList() != null){
						
						Double ingresosTotales=(double) 0;
						for (PulsoMaipuDetalleParte2 pulsoClinicaDetalle : prod.getList()) {
							// si estoy parado ahora en la actividad Fila/Columna deseada
							if(pulsoClinicaDetalle.getIdCurso() == idCurso.intValue()){
								
								if(pulsoClinicaDetalle.getCantidadIngresoEfectivo() != null){
									ingresosTotales=ingresosTotales +pulsoClinicaDetalle.getCantidadIngresoEfectivo(); 	
									
								}
								
								if(pulsoClinicaDetalle.getCantidadEgresoRRHH() != null){
									ingresosTotales=ingresosTotales - pulsoClinicaDetalle.getCantidadEgresoRRHH(); 	
									
								}
							}	
						}
						Label lab= new Label("$"+formateador.format(ingresosTotales) );
						lab.setStyle("font-weight: bold;");
						this.addField(lab);
					}
					posicion= posicion +1;
				}
			}
		}			
		super.addRow();		
	}
	
	private void seteoUltimaFila(List<PulsoMaipuDetalle> productosList){
		int posicion=1;

		
		RequiredLabel tot =new RequiredLabel("TOTAL CONSOLIDADO x QUINCENA");
		tot.setStyle("font-weight: bold;");
		this.addField(tot);
		RequiredLabel eeee=new RequiredLabel(" ------------> ");
		eeee.setStyle("font-weight: bold;");
		this.addField(eeee);

		 DecimalFormat formateador = new DecimalFormat("###,###"); 

		if(productosList != null){
			for (PulsoMaipuDetalle prod : productosList) {
				
				// llego al mes en el que estoy parado
				if(prod.getPosicionId() == posicion ){
					
					if(prod.getList() != null){
						
						
						Double ingresoTotal=(double) 0;
						for (PulsoMaipuDetalleParte2 pulsoClinicaDetalle : prod.getList()) {

							if(pulsoClinicaDetalle.getIdCurso() != 99){
								
								if(pulsoClinicaDetalle.getCantidadIngresoEfectivo() != null 
										&& pulsoClinicaDetalle.getCantidadIngresoEfectivo() >0){
									ingresoTotal= (ingresoTotal+ pulsoClinicaDetalle.getCantidadIngresoEfectivo());
								}
								
								if(pulsoClinicaDetalle.getCantidadEgresoRRHH() != null 
										&& pulsoClinicaDetalle.getCantidadEgresoRRHH() >0){
									ingresoTotal= (ingresoTotal -  pulsoClinicaDetalle.getCantidadEgresoRRHH());
								}
							}
						}
						RequiredLabel lab= new RequiredLabel("$"+formateador.format(ingresoTotal) );
						lab.setStyle("font-weight: bold;");
						this.addField(lab);

					}
					posicion= posicion +1;
				}
			}
		}			
		super.addRow();		
	}
	
	private void seteoIngreosEgresos(TipoMovimientoCajaEnum tipo, List<PulsoMaipuDetalle> productosList){
		 DecimalFormat formateador = new DecimalFormat("###,###"); 
		 int posicion=1;
//		 this.addField(new Label("INGRESOS MAIPU "));
		 if(tipo.toInt() == TipoMovimientoCajaEnum.INGRESO.toInt()){
			Label mes =new Label("INGRESOS EN CAJA");
			mes.setStyle("font-weight: bold;");
			this.addField(mes);
			
			Label ing =new Label("Ingresos caja");
			ing.setStyle("font-weight: bold;");
			this.addField(ing);
			 
		 }else{
			Label mes =new Label("EGRESOS EN CAJA");
			mes.setStyle("font-weight: bold;");
			this.addField(mes);
			
			Label ing =new Label("Egresos caja");
			ing.setStyle("font-weight: bold;");
			this.addField(ing);
		 }
		
		if(productosList != null){
			for (PulsoMaipuDetalle prod : productosList) {
				
				// llego al mes en el que estoy parado
				if(prod.getPosicionId() == posicion ){
					
//					como saber aca que la quincena con la que estoy trabajando esta ordenada, o sea, estoy por mostrarla en la posicion correcta
					if(prod.getList() != null){
						
						Double tota=(double) 0;
						for (PulsoMaipuDetalleParte2 pulsoClinicaDetalle : prod.getList()) {
							
							if(pulsoClinicaDetalle.getIdCurso() == 98){
								 if(tipo.toInt() == TipoMovimientoCajaEnum.INGRESO.toInt()){
									 if(pulsoClinicaDetalle.getCantidadIngresoEfectivo() != null && 
												pulsoClinicaDetalle.getCantidadIngresoEfectivo().intValue() >0){
											tota= tota + pulsoClinicaDetalle.getCantidadIngresoEfectivo();
										}
								 }else{
										if(pulsoClinicaDetalle.getCantidadEgresoRRHH() != null && 
												pulsoClinicaDetalle.getCantidadEgresoRRHH().intValue() >0){
											tota= tota + pulsoClinicaDetalle.getCantidadEgresoRRHH();
										}
									 
								 }
							}
						}
						Label lab= null;
						 if(tipo.toInt() == TipoMovimientoCajaEnum.INGRESO.toInt())
							lab= new Label("$"+formateador.format(tota));
						else{
							if(tota ==0)
								lab= new Label("$"+formateador.format(tota));
							else
								lab= new Label("$-"+formateador.format(tota));
						}
						lab.setStyle("font-weight: bold;");
						this.addField(lab);
					}
					posicion= posicion +1;

				}
			}
		}			
		super.addRow();		
	}
	
	private void seteoPrimeraFila(Integer idCurso, List<PulsoMaipuDetalle> productosList){
		 int posicion=1;
		 DecimalFormat formateador = new DecimalFormat("###,###"); 

		// Seteo el Primero curso
		if(idCurso != null){
			Curso curso=getServiceCurso().findById(idCurso.longValue());
			Label mes = null;
			if(curso != null)
				mes =new Label(curso.getNombre());
			else
				mes =new Label(" ");
			mes.setStyle("font-weight: bold;");
			this.addField(mes);
		}else
			this.addField(new Label(" "));	

		this.addField(new Label("Ingresos"));
		
		if(productosList != null){
			for (PulsoMaipuDetalle prod : productosList) {
				
				// llego al mes en el que estoy parado
				if(prod.getPosicionId() == posicion ){
					
//					como saber aca que la quincena con la que estoy trabajando esta ordenada, o sea, estoy por mostrarla en la posicion correcta
					
					if(prod.getList() != null){
						
						for (PulsoMaipuDetalleParte2 pulsoClinicaDetalle : prod.getList()) {
							
							// si estoy parado ahora en la actividad Fila/Columna deseada
							if(pulsoClinicaDetalle.getIdCurso() == idCurso.intValue()){
								Label lab= null;
								
								if(pulsoClinicaDetalle.getCantidadIngresoEfectivo() != null){
									lab= new Label("$"+formateador.format(pulsoClinicaDetalle.getCantidadIngresoEfectivo()));
								}else{
									lab= new Label("$"+formateador.format(0));
								}
								this.addField(lab);
																
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

