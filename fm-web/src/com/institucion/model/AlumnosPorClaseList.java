package com.institucion.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Rows;

import com.institucion.desktop.helper.EdadViewHelper;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.PanelRoutingCrud;

public class AlumnosPorClaseList  extends PanelRoutingCrud<ClienteListaEncontradoEnPileta> {
	private static final long serialVersionUID = 1L;

	public AlumnosPorClaseList() {
		super();
		this.setCheckbox();
		this.setUIEvent();
		this.setDisable(false);

	}
	@Override
	protected boolean setPanelTitle() {
		this.addAuxheader(I18N.getLabel("client.apellido")		, 1,1);
		this.addAuxheader(I18N.getLabel("client.nombre")			, 1,1);
		this.addAuxheader(I18N.getLabel("client.dni")			, 1,1);
		this.addAuxheader(I18N.getLabel("client.fechaNac")	, 1,1);
		this.addAuxheader(I18N.getLabel("client.edad")	, 1,1, "8%");
		this.addAuxheader(I18N.getLabel("client.asistencia")	, 1,1, "8%");

		return false;//esto se usa para no dibujar el ruteo
	}
	
	@Override
	protected void doEvent(Event event) {
	}

	public void setField(Set<ClienteListaEncontradoEnPileta> productosList, boolean habilitarCheckbox) {
		cleanRows();

		if(productosList != null){
			for (Object client : productosList) {
				
				if(client instanceof ClienteListaEncontradoEnPileta){
					ClienteListaEncontradoEnPileta 	cliente2= (ClienteListaEncontradoEnPileta) client;
					
					Cliente cliente=cliente2.getCliente();
					
					if(cliente.getApellido() != null)
						this.addField(new Label(cliente.getApellido().toUpperCase()));
					else
						this.addField(new Label(" "));

					if(cliente.getNombre() != null)
						this.addField(new Label(cliente.getNombre().toUpperCase()));
					else
						this.addField(new Label(" "));
					
					if(cliente.getDni() != null)
						this.addField(new Label(cliente.getDni()));
					else
						this.addField(new Label(" "));
					
					if(cliente.getFechaNacimiento() != null){
						Calendar ahoraCal= Calendar.getInstance();
						ahoraCal.setTime(cliente.getFechaNacimiento());
						int mes=ahoraCal.get(Calendar.MONTH) + 1;

						String fechaNac=ahoraCal.get(Calendar.DATE)+"/"+mes+"/"+ahoraCal.get(Calendar.YEAR);
						
						this.addField(new Label(fechaNac));
						this.addField(new Label(String.valueOf(EdadViewHelper.calcularEdad(cliente.getFechaNacimiento(), new Date()))));		
						
					}else{
						this.addField(new Label(" "));
						this.addField(new Label(" "));
					}
					
					Checkbox check = new Checkbox();
					check.setId(cliente.getId() +"-" + new Date() );
					check.setAttribute("check", cliente2);
					if(cliente2.getAsistencia() != null && cliente2.getAsistencia()){
						check.setChecked(cliente2.getAsistencia());	
					}
					
					check.setDisabled(habilitarCheckbox); 
					this.addField(check);

					super.addRow();					
					
				}else if(client instanceof ClienteListaEncontradoEnPiletaHistorico){
					ClienteListaEncontradoEnPiletaHistorico cliente2= (ClienteListaEncontradoEnPiletaHistorico) client;
					
					Cliente cliente=cliente2.getCliente();
					
					if(cliente.getApellido() != null)
						this.addField(new Label(cliente.getApellido().toUpperCase()));
					else
						this.addField(new Label(" "));

					if(cliente.getNombre() != null)
						this.addField(new Label(cliente.getNombre()));
					else
						this.addField(new Label(" "));
					
					if(cliente.getDni() != null)
						this.addField(new Label(cliente.getDni()));
					else
						this.addField(new Label(" "));
					
					if(cliente.getFechaNacimiento() != null){
						Calendar ahoraCal= Calendar.getInstance();
						ahoraCal.setTime(cliente.getFechaNacimiento());
						int mes=ahoraCal.get(Calendar.MONTH) + 1;

						String fechaNac=ahoraCal.get(Calendar.DATE)+"/"+mes+"/"+ahoraCal.get(Calendar.YEAR);
						
						this.addField(new Label(fechaNac));
						this.addField(new Label(String.valueOf(EdadViewHelper.calcularEdad(cliente.getFechaNacimiento(), new Date()))));		
						
					}else{
						this.addField(new Label(" "));
						this.addField(new Label(" "));
					}
					
					Checkbox check = new Checkbox();
					check.setId(cliente.getId() +"-" + new Date() );
					check.setAttribute("check", cliente2);
					if(cliente2.getAsistencia() != null && cliente2.getAsistencia()){
						check.setChecked(cliente2.getAsistencia());	
					}
					
					check.setDisabled(habilitarCheckbox); 
					this.addField(check);

					super.addRow();
					
				}
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