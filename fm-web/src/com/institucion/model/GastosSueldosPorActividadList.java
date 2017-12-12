package com.institucion.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.PanelRoutingCrud;

public class GastosSueldosPorActividadList extends PanelRoutingCrud<GastosSueldos> {
	private static final long serialVersionUID = 1L;
	
	public GastosSueldosPorActividadList() {
		super();
		this.setCheckbox();
		this.setUIEvent();
		this.setDisable(false);

	}
	@Override
	protected boolean setPanelTitle() {
//		this.addAuxheader(" ", 1,1, "2%");
		this.addAuxheader(I18N.getLabel("gastos.actividad")		, 1,1);
		this.addAuxheader(I18N.getLabel("gastos.list.dinero")			, 1,1);
//		this.addAuxheader(I18N.getLabel("gastos.list.comentario")	, 1,1);

		return false;//esto se usa para no dibujar el ruteo
	}
	
	@Override
	protected void doEvent(Event event) {

	}
	
	
	@SuppressWarnings("unchecked")
	public List<GastosSueldos>getListSueldos(){
//		Checkbox check=null;
		List<GastosSueldos>routings = new ArrayList<GastosSueldos>();
		for (Object obj: super.getRows().getChildren()) {
			Row row = (Row)obj;
			GastosSueldos routing= null;
			for (Object obj2: row.getChildren()) {
				Component c = (Component)obj2;
				if (c instanceof Label){
					routing = (GastosSueldos) c.getAttribute("check");
//					if (routing !=null){
//						routings.add(routing);
//	
//					}
//								
				}else if (c instanceof Intbox){
					routing.setDinero(((Intbox)c).getValue());
					if (routing !=null){
						routings.add(routing);
	
					}
				}
//				else if (c instanceof Textbox){
//					routing.setComentario(((Textbox)c).getValue());
//					
//					if (routing !=null){
//						routings.add(routing);
//	
//					}
//				}
			}
			
		}
		return routings;
	}

	
	
	public void setField(List<GastosSueldos> productosList) {
//		Checkbox check = null;
		cleanRows();

		if(productosList != null){
			for (GastosSueldos prod : productosList) {

				if(prod.getActividad() != null){
					Label act=new Label(prod.getActividad().getNombre());
					act.setAttribute("check", prod);
					this.addField(act);
				}else{
					Label act=new Label(" ");
					act.setAttribute("check", prod);
					this.addField(act);
				}
				
				if(prod.getDinero() != null)
					this.addField(new Intbox(prod.getDinero())); 
				else
					this.addField(new Intbox(prod.getDinero()));
				
//				if(prod.getComentario() != null)
//					this.addField(new Textbox(prod.getComentario()));
//				else
//					this.addField(new Textbox(" "));
			
				
				super.addRow();
				
			}		
		}
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
