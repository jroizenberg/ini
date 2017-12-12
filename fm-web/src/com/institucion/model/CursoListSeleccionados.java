package com.institucion.model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import com.institucion.bz.InscripcionEJB;
import com.institucion.bz.ObraSocialEJB;
import com.institucion.desktop.delegated.CursosSeleccionadosDelegate;
import com.institucion.desktop.helper.ObraSocialViewHelper;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridList;

public class CursoListSeleccionados extends GridList {
	private static final long serialVersionUID = 1L;
	private InscripcionEJB inscripcionEJB;
	private CursosSeleccionadosDelegate delegate;
//	private Intbox importeOriginal;
//	private Listcell importeConDescuento;
	DecimalFormat formateador = new DecimalFormat("###,###");

	private ObraSocialEJB obraSocialEJB;

	public CursoListSeleccionados() {
		
		super();
		super.addHeader(new Listheader(I18N.getLabel("curso.concepto"))).setWidth("34%");
		super.addHeader(new Listheader(I18N.getLabel("precioOriginal"))).setWidth("12%");
		super.addHeader(new Listheader(I18N.getLabel("precioConDescuento.1"))).setWidth("15%");
		super.addHeader(new Listheader("Quincena/Fecha cumple/Descuento")).setWidth("23%");
		super.addHeader(new Listheader("Comentario")).setWidth("30%");
		setMultiple(false);
		this.setPageSize(20);
		inscripcionEJB = BeanFactory.<InscripcionEJB>getObject("fmEjbInscripcion");


	}


	private ObraSocialEJB getService() {
		if (obraSocialEJB == null) {
			this.obraSocialEJB = BeanFactory.<ObraSocialEJB>getObject("fmEjbObraSocial");
		}
		return this.obraSocialEJB;
	}
	
	public void actualizarIntbos(int prodID, String type){
		
		Iterator<Listitem> itCursos =super.getItems().iterator();
		while (itCursos.hasNext()) {
			Listitem cursosDeLaLista=(Listitem)itCursos.next();
			Concepto cursoSeleccionado=(Concepto)cursosDeLaLista.getValue();

			// busco y actualizo el campo quincena
			if(cursoSeleccionado != null  && cursoSeleccionado.getCurso() != null 
					&& cursoSeleccionado.getCurso().getId().intValue() == prodID){
				if(cursosDeLaLista != null && cursosDeLaLista.getChildren() != null){
					Iterator iteee=cursosDeLaLista.getChildren().iterator();
					while(iteee.hasNext()){
						Listcell cel=	(Listcell)iteee.next();
						
						if(type.equalsIgnoreCase("suma")){
							cel.setStyle("color: red;font-weight: bold;");
							cursosDeLaLista.setStyle("color: red;font-weight: bold;");
						}else{
							cel.setStyle("color: black;font-weight: normal;");
							cursosDeLaLista.setStyle("color: black;font-weight: normal;");
						}					
						break;
					}
				}
			}
		}			
	}
	
	public void setList(Concepto curso, boolean esUnaModificacionDeCumple) {
			Listitem row = new Listitem();
			row.setValue(curso);
			if(curso.getCurso() != null)
				row.setAttribute("prodID", String.valueOf(curso.getCurso().getId()));
			
			if(curso.getCurso() != null){
				row.setSelected(true);
			}
			
			if(curso.getCurso() == null){
				Listcell nombrecell = new Listcell(curso.getConcepto().toUpperCase());
				row.appendChild(nombrecell);
				
			}else{
				Listcell nombrecell = new Listcell("Curso:"+curso.getConcepto().toUpperCase());
				row.appendChild(nombrecell);
				row.setSelected(true);
			}

			if((curso.getCurso() != null && curso.getCurso().getNombre().contains("CUMPLEA")) 
					||  (curso.getId() == null   && curso.getCurso() != null && curso.getCurso() .getVencimiento() != null  && curso.getCurso() != null 
						  &&((curso.getCurso() .getVencimiento().toInt() == VencimientoCursoEnum.QUINCENAL_VENCE_A_LA_QUINCENA.toInt()))) ){
				
				Intbox	importeOriginal= new Intbox();
				importeOriginal.setStyle("color: red ; font-weight: bold ; ");
				importeOriginal.setConstraint("/(([0-9]+)?)+/");
				importeOriginal.setAttribute("prodID", String.valueOf(curso.getCurso().getId()));
				importeOriginal.addEventListener(Events.ON_CHANGE, new EventListener(){
					public void onEvent(Event evt){
						
						if(((InputEvent)evt).getValue() != null){
							String valor=((InputEvent)evt).getValue();
							if(valor.contains(",")){
								String s=((InputEvent)evt).getValue();
								s=s.substring(0, s.indexOf(","));
								((Intbox)evt.getTarget()).setValue(Integer.parseInt(s));
								((Intbox)evt.getTarget()).setText(s);
								
								String cursoID=(String)((Intbox)evt.getTarget()).getAttribute("prodID");

//								importeConDescuento.setValue("$"+formateador.format(Integer.parseInt(s)));
								actualizaPrecioConDescuento( Integer.parseInt(cursoID), Integer.parseInt(s));

								delegate.actualizarPreciosPorModificacionEnCumples(Integer.parseInt(s), Integer.parseInt(cursoID));
								
							}else{
//								importeConDescuento.setValue("$"+formateador.format(Integer.parseInt(valor)));
								String cursoID=(String)((Intbox)evt.getTarget()).getAttribute("prodID");
								actualizaPrecioConDescuento( Integer.parseInt(cursoID), Integer.parseInt(valor));

								delegate.actualizarPreciosPorModificacionEnCumples(Integer.parseInt(valor), Integer.parseInt(cursoID));

							}
						}
					}
				}); 
				Listcell pagasubscell2 = new Listcell();
				importeOriginal.setValue(curso.getImporteOriginal());
//				importeOriginal.setText(Stringcurso.getImporteOriginal());
				pagasubscell2.appendChild(importeOriginal);
				row.appendChild(pagasubscell2);

			}else{
//				Listcell desccell = new Listcell("$"+formateador.format(curso.getImporteOriginal()));//String.valueOf(curso.getImporteOriginal()));
//				row.appendChild(desccell);		
				
				Intbox	importeOriginal= new Intbox();
				importeOriginal.setStyle("color: red ; font-weight: bold ; ");
				importeOriginal.setDisabled(true);
				importeOriginal.setConstraint("/(([0-9]+)?)+/");
				importeOriginal.setValue(curso.getImporteOriginal());
				
				Listcell pagasubscell2 = new Listcell();
				importeOriginal.setValue(curso.getImporteOriginal());
//				importeOriginal.setText(Stringcurso.getImporteOriginal());
				pagasubscell2.appendChild(importeOriginal);
				row.appendChild(pagasubscell2);

			}

			if((curso.getCurso() != null && curso.getCurso().getNombre().contains("CUMPLEA")) 
					||  (curso.getId() == null && curso.getCurso() != null && curso.getCurso() .getVencimiento() != null &&((curso.getCurso() .getVencimiento().toInt() == VencimientoCursoEnum.QUINCENAL_VENCE_A_LA_QUINCENA.toInt()))) ){
//				Listcell importeConDescuento = new Listcell("$"+formateador.format(curso.getImporteConDescuento()));
//				row.appendChild(importeConDescuento);

				
				Intbox	importeOriginal= new Intbox();
				importeOriginal.setStyle("color: red ; font-weight: bold ; ");
				importeOriginal.setDisabled(true);
				importeOriginal.setConstraint("/(([0-9]+)?)+/");
				importeOriginal.setValue(curso.getImporteConDescuento());
				
				Listcell pagasubscell2 = new Listcell();
				importeOriginal.setValue(curso.getImporteOriginal());
//				importeOriginal.setText(Stringcurso.getImporteOriginal());
				pagasubscell2.appendChild(importeOriginal);
				row.appendChild(pagasubscell2);


				
			}else{
//				Listcell pagasubscell = new Listcell("$"+formateador.format(curso.getImporteConDescuento()));
//				row.appendChild(pagasubscell);
				
				Intbox	importeOriginal= new Intbox();
				importeOriginal.setStyle("color: red ; font-weight: bold ; ");
				importeOriginal.setConstraint("/(([0-9]+)?)+/");
				if(curso.getCurso() != null)
					importeOriginal.setAttribute("prodID", String.valueOf(curso.getCurso().getId()));
				importeOriginal.setDisabled(true);
				importeOriginal.addEventListener(Events.ON_CHANGE, new EventListener(){
					public void onEvent(Event evt){
						
						if(((InputEvent)evt).getValue() != null){
							String valor=((InputEvent)evt).getValue();
							if(valor.contains(",")){
								String s=((InputEvent)evt).getValue();
								s=s.substring(0, s.indexOf(","));
								((Intbox)evt.getTarget()).setValue(Integer.parseInt(s));
								((Intbox)evt.getTarget()).setText(s);
								
								Integer val= null;

								String cursoID=(String)((Intbox)evt.getTarget()).getAttribute("prodID");
								if(cursoID != null )
									val= Integer.parseInt(cursoID);
								
								if(!s.trim().equalsIgnoreCase(""))
									delegate.actualizarPreciosPorModificacionEnCumples(Integer.parseInt(s), val);
								
							}else{
//								importeConDescuento.setValue("$"+formateador.format(Integer.parseInt(valor)));
								Integer val= null;
								String cursoID=(String)((Intbox)evt.getTarget()).getAttribute("prodID");
								if(cursoID != null )
									val= Integer.parseInt(cursoID);
								if(!valor.trim().equalsIgnoreCase(""))
									delegate.actualizarPreciosPorModificacionEnCumples(Integer.parseInt(valor), val);

							}
						}
					}
				}); 
				Listcell pagasubscell2 = new Listcell();
				importeOriginal.setValue(curso.getImporteConDescuento());
				pagasubscell2.appendChild(importeOriginal);
				row.appendChild(pagasubscell2);
			}

			if(curso.getCurso() != null && curso.getCurso().getVencimiento().equals(VencimientoCursoEnum.QUINCENAL_VENCE_A_LA_QUINCENA)){
				Combobox cc= createCombo(curso.getQuincena());
				cc.addEventListener(Events.ON_CHANGE, new EventListener() {
					public void onEvent(Event evt){
						if(((Combobox)evt.getTarget()).getSelectedItem() != null){
							int index=(int)((Combobox)evt.getTarget()).getSelectedIndex();
							modifiAll(index);	
						}else{
							modifiAll(-1);	

						}
						
					}
				});	
				cc.setStyle("width:60%; ");
				cc.setWidth("60%");

				Listcell pagasubscell2 = new Listcell();
				pagasubscell2.appendChild(cc);
				row.appendChild(pagasubscell2);
			}else if(curso.getCurso() != null && curso.getCurso().getNombre().contains("CUMPLEA")){
				Datebox cc= createDate(curso.getFechaCumple());
				cc.setStyle("width:60%; ");
				cc.setWidth("60%");

				Listcell pagasubscell2 = new Listcell();
				pagasubscell2.appendChild(cc);
				row.appendChild(pagasubscell2);
			
			}else{
//				Listcell pagasubscell3 = new Listcell(" ");
//				row.appendChild(pagasubscell3);
				
				Combobox cc = new Combobox();
				if(curso.getId() == null){
					ObraSocialViewHelper.getComboBox(getService().findAllByActividadDisponible(curso.getActividadDelConcepto()), cc, curso.getActividadDelConcepto(), curso.getCurso());
					cc.addEventListener(Events.ON_CHANGE, new EventListener() {
						public void onEvent(Event evt){
							if(((Combobox)evt.getTarget()).getSelectedItem() != null){
								Concepto concep=(Concepto)((Listitem)(evt.getTarget().getParent().getParent())).getValue();
								
								if(((Combobox)evt.getTarget()).getSelectedItem().getValue() instanceof  ObraSocial){
									// aca debo llamar a la funcion que actualiza segun obra social
									delegate.actualizarCursoSegunDescuento((ObraSocial)((Combobox)evt.getTarget()).getSelectedItem().getValue(), null, concep);
									
								}else if(((Combobox)evt.getTarget()).getSelectedItem().getValue() instanceof  String){
									// aca debo llamar a la funcion que actualiza segun obra social
									
									if(((String)((Combobox)evt.getTarget()).getSelectedItem().getValue()).equalsIgnoreCase("SIN descuento")){
										delegate.actualizarCursoSegunDescuento(null, "SIN descuento", concep);
										
									}else if(((String)((Combobox)evt.getTarget()).getSelectedItem().getValue()).equalsIgnoreCase("Descuento General/Manual")){
										delegate.actualizarCursoSegunDescuento(null, "Descuento General/Manual", concep);
									}
								}
//								modifiAll(index);	
							}else{
//								modifiAll(-1);	

							}
							
						}
					});	
					cc.setStyle("width:60%; ");
					cc.setWidth("60%");

					Listcell pagasubscell2 = new Listcell();
					pagasubscell2.appendChild(cc);
					row.appendChild(pagasubscell2);		
					cc.setVisible(true);
				}else{
					Listcell pagasubscell2 = new Listcell();
					pagasubscell2.appendChild(cc);
					row.appendChild(pagasubscell2);	
					cc.setVisible(false);
				}
			}

			if(curso.getCurso() != null && curso.getComentarioDescuento() != null && !curso.getComentarioDescuento().equalsIgnoreCase("null")){
				Listcell pagasubsce3ll = new Listcell(String.valueOf(curso.getComentarioDescuento()));
				row.appendChild(pagasubsce3ll);
			}else{
				Listcell pagasubscell22 = new Listcell(" ");
				row.appendChild(pagasubscell22);
			}

			super.addRow(row);
			
			if(curso.getCurso() != null){
				row.setSelected(true);
				setSelectedItem(row);
			}
			
	}	
	
	public void actualizaPrecioConDescuento(int cursoid, int precio) {
		Iterator<Listitem> itCursos =super.getItems().iterator();
		while (itCursos.hasNext()) {
			Listitem cursosDeLaLista=(Listitem)itCursos.next();
			Concepto cursoSeleccionado=(Concepto)cursosDeLaLista.getValue();
			
			if(cursoSeleccionado.getCurso() != null && cursoSeleccionado.getCurso().getId().intValue() == cursoid ){
				if(cursosDeLaLista != null && cursosDeLaLista.getChildren() != null){
					Iterator iteee=cursosDeLaLista.getChildren().iterator();
					int posicion= 1;
					while(iteee.hasNext()){
						Listcell cel=	(Listcell)iteee.next();
						if(posicion == 3){
							if(cel.getChildren().size() > 0){
								Iterator iteee2=cel.getChildren().iterator();
								while(iteee2.hasNext()){
									Object obj=	(Object)iteee2.next();
									if(obj instanceof Intbox){
										((Intbox)obj).setValue(precio);
									}
								}
							}else{
								cel.setLabel("$"+formateador.format(precio));
								break;			
							}
						}
						posicion++;
					}
				}
			}
		}
	}	
	
	public void actualizoElValueYLosListCell(Concepto concep) {
		Iterator<Listitem> itCursos =super.getItems().iterator();
		while (itCursos.hasNext()) {
			Listitem cursosDeLaLista=(Listitem)itCursos.next();
			Concepto cursoSeleccionado=(Concepto)cursosDeLaLista.getValue();
			
			if((cursoSeleccionado.getCurso() != null && concep.getCurso() != null && cursoSeleccionado.getCurso().getId().intValue() == concep.getCurso().getId().intValue()) 
					|| (concep.getCurso() == null &&   cursoSeleccionado.getCurso() == null )){
				cursosDeLaLista.setValue(concep);
				
				if(cursosDeLaLista != null && cursosDeLaLista.getChildren() != null){
					Iterator iteee=cursosDeLaLista.getChildren().iterator();
					int posicion= 1;
					while(iteee.hasNext()){
						Listcell cel=	(Listcell)iteee.next();
						if(posicion == 3){
							if(cel.getChildren().size() > 0){
								Iterator iteee2=cel.getChildren().iterator();
								while(iteee2.hasNext()){
									Object obj=	(Object)iteee2.next();
									if(obj instanceof Intbox){
										((Intbox)obj).setValue(concep.getImporteConDescuento());
									}
								}
							}else{
								cel.setLabel("$"+formateador.format(concep.getImporteConDescuento()));								
							}
							
						}else 	if(posicion == 5){
							if(concep.getComentarioDescuento() != null){
								cel.setLabel(concep.getComentarioDescuento());	
							}else{
								cel.setLabel("");
							}
						} 
						posicion++;
					}
				}
			}
		}
	}	

	public void convertirCampoAIntBox(Concepto concep, boolean disable, boolean esReadOnly) {
		Iterator<Listitem> itCursos =super.getItems().iterator();
		while (itCursos.hasNext()) {
			Listitem cursosDeLaLista=(Listitem)itCursos.next();
			Concepto cursoSeleccionado=(Concepto)cursosDeLaLista.getValue();
			
			if((cursoSeleccionado.getCurso() != null  && concep.getCurso() != null 
					&& cursoSeleccionado.getCurso().getId().intValue() == concep.getCurso().getId().intValue()) 
					 || (concep.getCurso() == null &&   cursoSeleccionado.getCurso() == null )){
//				cursosDeLaLista.setValue(concep);
				
				if(cursosDeLaLista != null && cursosDeLaLista.getChildren() != null){
					Iterator iteee=cursosDeLaLista.getChildren().iterator();
					int posicion= 1;
					while(iteee.hasNext()){
						Listcell cel=	(Listcell)iteee.next();
						if(posicion == 3){
							
							if(cel.getChildren().size() > 0){
								Iterator iteee2=cel.getChildren().iterator();
								while(iteee2.hasNext()){
									Object obj=	(Object)iteee2.next();

									if(obj instanceof Intbox){
										((Intbox)obj).setDisabled(disable);
										((Intbox)obj).setReadonly(esReadOnly);
										((Intbox)obj).setFocus(true);
									}
								}
							}
						}
						posicion++;
					}
				}
			}
		}
	}	

	
	private int actualizoCosas(int importeTotal,Listitem cursosDeLaLista, boolean loSumo, Concepto cursoSeleccionado ){
		if(cursosDeLaLista != null && cursosDeLaLista.getChildren() != null){
//			int posicion= 1;

			Iterator iteee=cursosDeLaLista.getChildren().iterator();
			while(iteee.hasNext()){
				Listcell cel=	(Listcell)iteee.next();
				
				if(cel.getChildren().size() == 0){
//					importeTotal=ci+cursoSeleccionado.getImporteConDescuento();
				}else{
					Iterator iteee2=cel.getChildren().iterator();
					while(iteee2.hasNext()){
						Object obj=	(Object)iteee2.next();

						if(obj instanceof Intbox &&
								(!((Intbox)obj).isDisabled() || ((Intbox)obj).isReadonly() )){
							Integer ci=((Intbox)obj).getValue();
							if(ci == null){
								throw new WrongValueException(((Intbox)cel.getChildren().get(0)), I18N.getLabel("error.empty.field"));
								
							}else{
								importeTotal=ci+importeTotal;
								loSumo= true;
								
							}
						}
					}
				}

			}
		}
		
	if(!loSumo){
		importeTotal=cursoSeleccionado.getImporteConDescuento()+ importeTotal;
//		loSumo=false;
	}
	return importeTotal;
	}
	
	public int obtenerImporteFinalDeLaListaMenosDelCurso(Integer cursoID) {
		
		int importeTotal= 0;
		Iterator<Listitem> itCursos =super.getItems().iterator();
		while (itCursos.hasNext()) {
			Listitem cursosDeLaLista=(Listitem)itCursos.next();
			boolean loSumo=false;

			Concepto cursoSeleccionado=(Concepto)cursosDeLaLista.getValue();
			
			if(( cursoID == null)){
				// es matricula, sumar todos menos la matricula
				if((cursoSeleccionado.getCurso() != null )){
					// sumo todos
					importeTotal=actualizoCosas(importeTotal, cursosDeLaLista, loSumo, cursoSeleccionado);
					
				}else{
					// no la sumo por que es la matricula existente en el concepto
				}				
			
			}else if((cursoSeleccionado.getCurso() == null)){
				// la edicion que se estaba haciendo es de un curso normal, y estamos iterando una matricula.
				// sumo todos
				importeTotal=actualizoCosas(importeTotal, cursosDeLaLista, loSumo, cursoSeleccionado);
			}else if((cursoSeleccionado.getCurso().getId().intValue() != cursoID.intValue())){
				// sumo todos				
				importeTotal=actualizoCosas(importeTotal, cursosDeLaLista, loSumo, cursoSeleccionado);
			}
		}
		return importeTotal;
	}	
	
	private Datebox createDate(Date fechaCumple){

		Datebox fechaCumpleanios  = new Datebox();
		fechaCumpleanios .setMaxlength(20);
		fechaCumpleanios.setId("sasas222545");
		fechaCumpleanios.setFormat(I18N.getDateFormat());
		fechaCumpleanios.setValue(fechaCumple);
		return fechaCumpleanios;
	}
	
	private void modifiAll(int index){
		Iterator<Listitem> itCursos =super.getItems().iterator();
		while (itCursos.hasNext()) {
			Listitem cursosDeLaLista=(Listitem)itCursos.next();
			Concepto cursoSeleccionado=(Concepto)cursosDeLaLista.getValue();
			
			// busco y actualizo el campo quincena
			if(cursoSeleccionado != null && cursoSeleccionado.getCurso() != null 
					&& cursoSeleccionado.getCurso().getVencimiento().equals(VencimientoCursoEnum.QUINCENAL_VENCE_A_LA_QUINCENA) ){
				if(cursosDeLaLista != null && cursosDeLaLista.getChildren() != null){
					Iterator iteee=cursosDeLaLista.getChildren().iterator();
					while(iteee.hasNext()){
						Listcell cel=	(Listcell)iteee.next();
						if(cel.getChildren() != null && cel.getChildren().size() ==1 ){
							if(cel.getChildren().get(0) instanceof Combobox){
								if(index == -1)
									((Combobox)cel.getChildren().get(0)).setSelectedItem(null);
								else
									((Combobox)cel.getChildren().get(0)).setSelectedIndex(index);//tem(qq);
//								((Combobox)cel.getChildren().get(0)).setSelec
							}
						}
					}
				}
			}
		}
	}
	
	
	private Combobox createCombo(Quincena quincenaSelected){
		
		Combobox cb = new Combobox();
		cb.setConstraint("strict");
		List<Quincena> quincenas =inscripcionEJB.obtenerQuincenalesList();
		if(quincenas != null){
			Comboitem comboSelected= null;
			for (Quincena quincena : quincenas) {
				Comboitem item;
				item = new Comboitem(quincena.getNombre());
				item.setValue(quincena);

				if(quincenaSelected != null && quincenaSelected.getId().equals(quincena.getId())){
					comboSelected= item;
				}
				cb.appendChild(item);
			}
			
			if(comboSelected != null ){
				cb.setSelectedItem(comboSelected);	
			}
		}
		return cb;
	}
	
	public List<Concepto>  getList(boolean esModificacionCumple){
		List conceptos= new ArrayList(); 
		Iterator<Listitem> itCursos =super.getItems().iterator();
		while (itCursos.hasNext()) {
			Listitem cursosDeLaLista=(Listitem)itCursos.next();
			Concepto cursoSeleccionado=(Concepto)cursosDeLaLista.getValue();
			if(cursoSeleccionado.getTipoDescuento() == null){
				cursoSeleccionado.setTipoDescuento(TipoDescuentosEnum.NO);
			}
			if(cursoSeleccionado.getSubscripcionDescuentoGeneral() == null){
				cursoSeleccionado.setSubscripcionDescuentoGeneral(SubscripcionDescuentoGeneralEnum.SIN_DESC);
			}	
			
			// si es 
		
			
			// busco y actualizo el campo quincena o cumpleanios
//			if(cursoSeleccionado != null && cursoSeleccionado.getCurso() != null 
//					&& 
//					(cursoSeleccionado.getCurso().getVencimiento().equals(VencimientoCursoEnum.QUINCENAL_VENCE_A_LA_QUINCENA)
//							||  cursoSeleccionado.getCurso().getNombre().contains("CUMPLEA")) ){
				if(cursosDeLaLista != null && cursosDeLaLista.getChildren() != null){
					Iterator iteee=cursosDeLaLista.getChildren().iterator();
					
					int posicion= 1;
					
					while(iteee.hasNext()){
						Listcell cel=	(Listcell)iteee.next();
						
						if(cel.getChildren().size() > 0){
							Iterator iteee2=cel.getChildren().iterator();
							while(iteee2.hasNext()){
								Object obj=	(Object)iteee2.next();
								
								
								if(posicion == 2){  // importe original
									
									if(obj instanceof Intbox &&  !((Intbox)obj).isDisabled()){
										Integer ci=((Intbox)obj).getValue();
										if(ci == null){
											throw new WrongValueException(((Intbox)obj), I18N.getLabel("error.empty.field"));
											
										}else{
//											cursoSeleccionado.setImporteConDescuento(ci);
											cursoSeleccionado.setImporteOriginal(ci);
										}
									}
								}
								
								
								if(posicion == 3){  // importe descuento
									
									if(obj instanceof Intbox &&  !((Intbox)obj).isDisabled()){
										Integer ci=((Intbox)obj).getValue();
										if(ci == null){
											throw new WrongValueException(((Intbox)obj), I18N.getLabel("error.empty.field"));
											
										}else{
//											cursoSeleccionado.setImporteConDescuento(ci);
											cursoSeleccionado.setImporteConDescuento(ci);
										}
									}
								}
							}
							
							if((cursoSeleccionado.getCurso() != null &&
									(cursoSeleccionado.getCurso().getVencimiento().equals(VencimientoCursoEnum.QUINCENAL_VENCE_A_LA_QUINCENA)
									||  cursoSeleccionado.getCurso().getNombre().contains("CUMPLEA"))) ){
										
										if(cel.getChildren().get(0) instanceof Combobox){
											Comboitem ci=((Combobox)cel.getChildren().get(0)).getSelectedItem();
											if(ci == null){
												throw new WrongValueException(((Combobox)cel.getChildren().get(0)), I18N.getLabel("error.empty.field"));
												
											}else{
												cursoSeleccionado.setQuincena((Quincena)ci.getValue());
											}
										}
										if(cel.getChildren().get(0) instanceof Datebox){
											Date fecha=((Datebox)cel.getChildren().get(0)).getValue();
											if(fecha == null){
												throw new WrongValueException(((Datebox)cel.getChildren().get(0)), I18N.getLabel("error.empty.field"));
												
											}else{
												cursoSeleccionado.setFechaCumple(fecha);
											}
										}										
								}
						}
						posicion++;
					}
				}
//			}
			conceptos.add(cursoSeleccionado);	
		}
	return conceptos;
	}
	
	public void eliminarDeList(Concepto curso) {
		Iterator<Listitem> itCursos =super.getItems().iterator();
		while (itCursos.hasNext()) {
			Listitem cursosDeLaLista=(Listitem)itCursos.next();
			Concepto cursoSeleccionado=(Concepto)cursosDeLaLista.getValue();
			
			if(cursoSeleccionado.getCurso() != null && cursoSeleccionado.getCurso().getId().intValue() == curso.getCurso().getId().intValue() ){
				itCursos.remove();		
				break;
			}
		}
		
		if(!haveCursosConMatriculaEnSI()){
			// ver si debo eliminar tambien la matricula
			Iterator<Listitem> itCursos2 =super.getItems().iterator();
			while (itCursos2.hasNext()) {
				Listitem cursosDeLaLista=(Listitem)itCursos2.next();
				Concepto cursoSeleccionado=(Concepto)cursosDeLaLista.getValue();
				
				if(cursoSeleccionado.getCurso() == null )
					itCursos2.remove();				
			}	
		}		
	}	
	
	public void eliminarDeListPorIdCurso(int cursoid) {
		Iterator<Listitem> itCursos =super.getItems().iterator();
		while (itCursos.hasNext()) {
			Listitem cursosDeLaLista=(Listitem)itCursos.next();
			Concepto cursoSeleccionado=(Concepto)cursosDeLaLista.getValue();
			
			if(cursoSeleccionado.getCurso() != null && cursoSeleccionado.getCurso().getId().intValue() == cursoid ){
				itCursos.remove();		
				break;
			}
		}
		
		if(!haveCursosConMatriculaEnSI()){
			// ver si debo eliminar tambien la matricula
			Iterator<Listitem> itCursos2 =super.getItems().iterator();
			while (itCursos2.hasNext()) {
				Listitem cursosDeLaLista=(Listitem)itCursos2.next();
				Concepto cursoSeleccionado=(Concepto)cursosDeLaLista.getValue();
				
				if(cursoSeleccionado.getCurso() == null )
					itCursos2.remove();				
			}	
		}		
	}	
	
	public boolean seleccionarList(int idCurso) {
		boolean pude= false;
		Iterator<Listitem> itCursos =super.getItems().iterator();
		while (itCursos.hasNext()) {
			Listitem cursosDeLaLista=(Listitem)itCursos.next();
			Concepto cursoSeleccionado=(Concepto)cursosDeLaLista.getValue();
			
			if(cursoSeleccionado.getCurso() != null && cursoSeleccionado.getCurso().getId().intValue() == idCurso ){
				super.setSelectedItem(cursosDeLaLista);
				pude= true;
				break;
			}
		}
		return pude;
	}
	
	private boolean haveCursosConMatriculaEnSI(){
		Iterator<Listitem> itCursos =super.getItems().iterator();
		while (itCursos.hasNext()) {
			Listitem cursosDeLaLista=(Listitem)itCursos.next();
			Concepto cursoSeleccionado=(Concepto)cursosDeLaLista.getValue();
			
			if(cursoSeleccionado.getCurso() != null && cursoSeleccionado.getCurso().isPagaSubscripcion() )
				return true;			
		}
		return false;
	} 
	
	public boolean existeMatriculaEnLista() {
			Iterator<Listitem> itCursos =super.getItems().iterator();
			while (itCursos.hasNext()) {
				Listitem cursosDeLaLista=(Listitem)itCursos.next();
				Concepto cursoSeleccionado=(Concepto)cursosDeLaLista.getValue();
				if(cursoSeleccionado.getCurso() == null ){
					return true;
				}
			}
		return false;	
	}	
	
	public float obtenerSaldoMatriculaEnLista() {
		Iterator<Listitem> itCursos =super.getItems().iterator();
		while (itCursos.hasNext()) {
			Listitem cursosDeLaLista=(Listitem)itCursos.next();
			Concepto cursoSeleccionado=(Concepto)cursosDeLaLista.getValue();
			if(cursoSeleccionado.getCurso() == null ){
				return cursoSeleccionado.getImporteOriginal();
			}
		}
	return 0;	
}	
	
	public boolean existeCursoEnLista(Concepto concepto) {
		Iterator<Listitem> itCursos =super.getItems().iterator();
		while (itCursos.hasNext()) {
			Listitem cursosDeLaLista=(Listitem)itCursos.next();
			Concepto cursoSeleccionado=(Concepto)cursosDeLaLista.getValue();
			
			if(cursoSeleccionado.getCurso() != null && cursoSeleccionado.getCurso().getId().intValue() == concepto.getCurso().getId().intValue() )
				return true;
		}
		return false;	
	}

	public CursosSeleccionadosDelegate getDelegate() {
		return delegate;
	}

	public void setDelegate(CursosSeleccionadosDelegate delegate) {
		this.delegate = delegate;
	}	
	
}