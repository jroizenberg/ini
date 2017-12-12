package com.institucion.model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import com.institucion.desktop.delegated.ListaDeCursosDelegate;
import com.institucion.desktop.helper.BooleanViewHelper;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridList;

public class CursoSubscripcionList extends GridList {
	private static final long serialVersionUID = 1L;
	DecimalFormat formateador = new DecimalFormat("###,###");

	private ListaDeCursosDelegate delegate;
	
	public CursoSubscripcionList() {
		super();
		super.addHeader(new Listheader(I18N.getLabel("curso.nombre"))).setWidth("40%");
		super.addHeader(new Listheader(I18N.getLabel("curso.pagaSubscripcion"))).setWidth("8%");
		super.addHeader(new Listheader(I18N.getLabel("curso.precio"))).setWidth("7%");
//		super.addHeader(new Listheader(I18N.getLabel("curso.vencimiento"))).setWidth("12%");
		super.addHeader(new Listheader("Cant. Clases")).setWidth("6%");
	}

	public boolean seleccionarList(int idCurso) {
		boolean pude= false;
		Iterator<Listitem> itCursos =super.getItems().iterator();
		while (itCursos.hasNext()) {
			Listitem cursosDeLaLista=(Listitem)itCursos.next();
			Curso cursoSeleccionado=(Curso)cursosDeLaLista.getValue();
			
			if(cursoSeleccionado != null && cursoSeleccionado.getId().intValue() == idCurso ){
				super.setSelectedItem(cursosDeLaLista);
				pude= true;
				break;
			}
		}
		return pude;
	}
	
	private boolean yaSeEncontrabaSeleccionado(Curso curso,List<Concepto> listConceptosSeleccionados  ){
			
			if(listConceptosSeleccionados != null && listConceptosSeleccionados.size() >0){
				for (Concepto concepto : listConceptosSeleccionados) {
					if(concepto.getCurso() != null && concepto.getCurso().getId().intValue() == curso.getId().intValue())
						return true;
					
				}
			}
	return false;		
	}
	

	public void setList(List<Curso> cursoList, boolean isFromSubs, List<Concepto> listConceptosSeleccionados ) {
		if(isFromSubs){
			this.setMultiple(false);
			this.setPageSize(12);
		}else{
			this.setMultiple(true);
			this.setPageSize(24);
		}

		super.removeAll();
		if(cursoList != null){
			Iterator<Curso> itPharmacy = cursoList.iterator();

			while (itPharmacy.hasNext()) {
				Curso curso = itPharmacy.next();

				// Por cada registro lo meto abajo
				ActividadYClase actYClase= (ActividadYClase)new ArrayList(curso.getActividadYClaseList()).get(0);
				
				Listitem row = new Listitem();
				row.setAttribute("prodID", String.valueOf(curso.getId()));
				row.setValue(curso);
				
				row.addEventListener(Events.ON_RIGHT_CLICK, new EventListener(){
					public void onEvent(Event evt){
						String prodID=(String)((Listitem)evt.getTarget()).getAttribute("prodID");
						if(delegate.agregarNuevoCursoAConceptoEvento(evt, Integer.parseInt(prodID))){
							actualizarIntbos(Integer.parseInt(prodID), "suma");	
						}
					}
				});
				row.addEventListener(Events.ON_CLICK, new EventListener(){
					public void onEvent(Event evt){
						String prodID=(String)((Listitem)evt.getTarget()).getAttribute("prodID");
						if(delegate.quitarNuevoCursoAConceptoEvento(evt, Integer.parseInt(prodID)))
							actualizarIntbos(Integer.parseInt(prodID), "resta");	
					}
				});

				
				if(yaSeEncontrabaSeleccionado(curso, listConceptosSeleccionados)){
					row.setStyle("color: red;font-weight: bold;");
//					cursosDeLaLista.setStyle("color: red;font-weight: bold;");

				}
				
				Listcell nombrecell = new Listcell(curso.getNombre().toUpperCase());
				row.appendChild(nombrecell);

				Listcell pagasubscell = new Listcell(BooleanViewHelper.getBooleanString(curso.isPagaSubscripcion()));
				row.appendChild(pagasubscell);

				Listcell preciosubscell = new Listcell("$"+formateador.format(curso.getPrecio()));
				row.appendChild(preciosubscell);

//				if(curso != null && curso.getNombre().contains("CUMPLEA")){
//					Listcell vencsubscell = new Listcell(" ");
//					row.appendChild(vencsubscell);
//				}else{
//					Listcell vencsubscell = new Listcell(String.valueOf(VencimientoCursoEnum.toStringMes(curso.getVencimiento().toInt())));
//					row.appendChild(vencsubscell);
//				}
							
				Listcell cantd=null;
				if(actYClase.getCantClases() == 99 
						|| curso.getVencimiento().toInt() == VencimientoCursoEnum.LIBRE_VENCE_A_LA_QUINCENA.toInt() 
						|| curso.getVencimiento().toInt() == VencimientoCursoEnum.LIBRE_VENCE_A_LA_SEMANA.toInt()
						|| curso.getVencimiento().toInt() == VencimientoCursoEnum.LIBRE_VENCE_A_LOS_3_MES.toInt()
						|| curso.getVencimiento().toInt() == VencimientoCursoEnum.LIBRE_VENCE_AL_MES.toInt()){
					
					cantd=new Listcell(String.valueOf("LIBRE"));

				}else{
					cantd=new Listcell(String.valueOf(actYClase.getCantClases()));
				}
				
				if(curso.getNombre().contains("CUMPLEA")){
					row.appendChild(new Listcell(String.valueOf(" ")));
				}else
					row.appendChild(cantd);

				if(isFromSubs){
					Component nullComponent = null;
					row.addForward("onClick",nullComponent,"onClickSubscripcionesEvt");
					super.addRow2(row);
				}else
					super.addRow(row);
			}
		}
	}

	
	public void actualizarIntbos(int prodID, String type){
		
		Iterator<Listitem> itCursos =super.getItems().iterator();
		while (itCursos.hasNext()) {
			Listitem cursosDeLaLista=(Listitem)itCursos.next();
			Curso cursoSeleccionado=(Curso)cursosDeLaLista.getValue();

			// busco y actualizo el campo quincena
			if(cursoSeleccionado != null  && cursoSeleccionado.getId().intValue() == prodID){
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
	public ListaDeCursosDelegate getDelegate() {
		return delegate;
	}

	public void setDelegate(ListaDeCursosDelegate delegate) {
		this.delegate = delegate;
	}			
}