package com.institucion.model;

import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import com.institucion.bz.ClaseEJB;
import com.institucion.desktop.delegated.SubscripcionDelegate;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridCrud;

public class SubscripcionDeClasesCrud extends GridCrud{

	private static final long serialVersionUID = 1L;
	private Combobox horaDesdeLunes;
	private Combobox horaDesdeMartes;
	private Combobox horaDesdeMiercoles;
	private Combobox horaDesdeJueves;
	private Combobox horaDesdeViernes;
	private Combobox horaDesdeSabado;
	private SubscripcionDelegate delegate;
	private Textbox nombreActividad;
	
	private ClaseEJB claseEJB;

	public SubscripcionDelegate getDelegate() {
		return delegate;
	}

	public void limpiar(){
		horaDesdeLunes.getItems().clear();
		horaDesdeLunes.setConstraint("");
		horaDesdeLunes.setText("");
		horaDesdeLunes.setConstraint("strict");
//		horaDesdeLunes.setDisabled(true);
		horaDesdeLunes.clearErrorMessage();
		
		horaDesdeMartes.getItems().clear();
		horaDesdeMartes.setConstraint("");
		horaDesdeMartes.setText("");
		horaDesdeMartes.setConstraint("strict");
//		horaDesdeMartes.setDisabled(true);
		horaDesdeMartes.clearErrorMessage();
		
		horaDesdeMiercoles.getItems().clear();
		horaDesdeMiercoles.setConstraint("");
		horaDesdeMiercoles.setText("");
		horaDesdeMiercoles.setConstraint("strict");
//		horaDesdeMiercoles.setDisabled(true);
		horaDesdeMiercoles.clearErrorMessage();
		
		horaDesdeJueves.getItems().clear();
		horaDesdeJueves.setConstraint("");
		horaDesdeJueves.setText("");
		horaDesdeJueves.setConstraint("strict");
//		horaDesdeJueves.setDisabled(true);
		horaDesdeJueves.clearErrorMessage();
		
		horaDesdeViernes.getItems().clear();
		horaDesdeViernes.setConstraint("");
		horaDesdeViernes.setText("");
		horaDesdeViernes.setConstraint("strict");
//		horaDesdeViernes.setDisabled(true);
		horaDesdeViernes.clearErrorMessage();
		
		horaDesdeSabado.getItems().clear();
		horaDesdeSabado.setConstraint("");
		horaDesdeSabado.setText("");
		horaDesdeSabado.setConstraint("strict");
//		horaDesdeSabado.setDisabled(true);
		horaDesdeSabado.clearErrorMessage();
		
	}
	
	public void setDelegate(SubscripcionDelegate delegate) {
		this.delegate = delegate;
	}
	
	public SubscripcionDeClasesCrud (){
		super();
		this.setStyle("  width:95%;  align:center;  margin-left: 2%;");
		super.setStyle("  width:95%;   align:center;  margin-left: 2%;");
		claseEJB = BeanFactory.<ClaseEJB>getObject("fmEjbClase");
		this.makeFields();
	}
	
	public static Combobox getComboBox(Combobox combo, List<Clase> clases) {
		Constraint brandC = combo.getConstraint();
		combo.setConstraint("");
		combo.setConstraint(brandC);
		combo.getItems().clear();
		
		if(clases != null && clases.size() > 0){
			combo.setVisible(true);
		}else{
			combo.setVisible(false);
		}
		for (Clase clase : clases) {
			Comboitem item;
			
			if(clase.getEsClaseSinHora() != null && !clase.getEsClaseSinHora().booleanValue()){
				String horaDesde= null;
				String horaHasta= null;

				if(clase.getHoraDesde().getMinutes() < 10){ 
					horaDesde= clase.getHoraDesde().getHours() +":0"+clase.getHoraDesde().getMinutes();	
				}else{
					horaDesde= clase.getHoraDesde().getHours() +":"+clase.getHoraDesde().getMinutes();
				}
				
				if(clase.getHoraHasta().getMinutes() < 10){ 
					horaHasta= clase.getHoraHasta().getHours() +":0"+clase.getHoraHasta().getMinutes();	
				}else{
					horaHasta= clase.getHoraHasta().getHours() +":"+clase.getHoraHasta().getMinutes();
				}
				if(clase.getNombreAdic() != null && !clase.getNombreAdic().trim().equalsIgnoreCase(""))
					item = new Comboitem(clase.getNombreAdic().toUpperCase() + "."+horaDesde +"-"+horaHasta);
				else
					item = new Comboitem(" "+horaDesde +"-"+horaHasta);
				item.setValue(clase);
				combo.appendChild(item);					
			}
		}

		return combo;
	}
	
	
	private boolean validarCantidadDiasSeleccionados(int cantDiasQTeniaQTener){
		int i=0;
		
		if(this.horaDesdeLunes.getSelectedItem() != null) i++;
		if(this.horaDesdeMartes.getSelectedItem() != null) i++;
		if(this.horaDesdeMiercoles.getSelectedItem() != null) i++;
		if(this.horaDesdeJueves.getSelectedItem() != null) i++;
		if(this.horaDesdeViernes.getSelectedItem() != null) i++;
		if(this.horaDesdeSabado.getSelectedItem() != null) i++;
		if(i > cantDiasQTeniaQTener)
			return false;
		return true;
		
	}
	
	// Se obtendran de la base de datos las clases de la actividad que seleccione y pongo aca entonces, dependiendo
	// habilitando los dias que se dictan esa categoria y las horas de inicio que se dictan las categorias.
	// luego permito seleccionar la cantidad de dias dependiendo del curso que se esta conntratando
	// Actividad actividad=getDelegate().getActividadDelCursoSeleccionado();
	
	
	public void completarCombosDeHorarios(){
		Actividad act=(Actividad)nombreActividad.getAttribute("actividad");

			List<Clase> clasesLunes=claseEJB.findAllByActividad(new Long(act.getId()), 1);
			this.horaDesdeLunes=getComboBox(horaDesdeLunes, clasesLunes);

			List<Clase> clasesMartes=claseEJB.findAllByActividad(new Long(act.getId()), 2);
			this.horaDesdeMartes=getComboBox(horaDesdeMartes, clasesMartes);

			List<Clase> clasesMier=claseEJB.findAllByActividad(new Long(act.getId()), 3);
			this.horaDesdeMiercoles=getComboBox(horaDesdeMiercoles, clasesMier);
		
			List<Clase> clasesJ=claseEJB.findAllByActividad(new Long(act.getId()), 4);
			this.horaDesdeJueves=getComboBox(horaDesdeJueves, clasesJ);

			List<Clase> clasesV=claseEJB.findAllByActividad(new Long(act.getId()), 5);
			this.horaDesdeViernes=getComboBox(horaDesdeViernes, clasesV);
			List<Clase> clasesS=claseEJB.findAllByActividad(new Long(act.getId()), 6);
			this.horaDesdeSabado=getComboBox(horaDesdeSabado, clasesS);
	}
	
	private void makeFields(){
		
		nombreActividad= new Textbox();
		nombreActividad.setDisabled(true);
		
		this.addFieldClases(new Label(I18N.getLabel("dayofweek.monday").toUpperCase()), new Label(I18N.getLabel("dayofweek.tuesday").toUpperCase()), 0);
		this.addFieldClases(new Label(I18N.getLabel("dayofweek.wednesday").toUpperCase()), new Label(I18N.getLabel("dayofweek.thursday").toUpperCase()), 1);
		this.addFieldClases(new Label(I18N.getLabel("dayofweek.friday").toUpperCase()), new Label(I18N.getLabel("dayofweek.saturday").toUpperCase()), 2);
		
		this.horaDesdeLunes= new Combobox();
		this.horaDesdeLunes.setConstraint("strict");
		this.horaDesdeLunes.setWidth("60%");
		this.horaDesdeLunes.addEventListener(Events.ON_CHANGE, new EventListener()	{
			public void onEvent(Event evt){
				if (((Combobox)evt.getTarget()).getSelectedItem() != null){
					int cantClase=(Integer)nombreActividad.getAttribute("cantidadClases");
					if(validarCantidadDiasSeleccionados(cantClase)){
						// obtener concepto seleccionado
					}else{
						((Combobox)evt.getTarget()).setSelectedItem(null);
					}
				}else{
					
				}
			}
		});

		
		this.horaDesdeMartes= new Combobox();
		this.horaDesdeMartes.setConstraint("strict");
		this.horaDesdeMartes.setWidth("60%");
		this.horaDesdeMartes.addEventListener(Events.ON_CHANGE, new EventListener()	{
			public void onEvent(Event evt){
				if (((Combobox)evt.getTarget()).getSelectedItem() != null){
					int cantClase=(Integer)nombreActividad.getAttribute("cantidadClases");
					if(validarCantidadDiasSeleccionados(cantClase)){
						// obtener concepto seleccionado
					}else{
						((Combobox)evt.getTarget()).setSelectedItem(null);
					}
				}else{
					
				}
			}
		});
		this.addFieldClases(horaDesdeLunes,horaDesdeMartes, 0);

		this.horaDesdeMiercoles= new Combobox();
		this.horaDesdeMiercoles.setConstraint("no empty, strict");
		this.horaDesdeMiercoles.setWidth("60%");
		this.horaDesdeMiercoles.addEventListener(Events.ON_CHANGE, new EventListener()	{
			public void onEvent(Event evt){
				if (((Combobox)evt.getTarget()).getSelectedItem() != null){
					int cantClase=(Integer)nombreActividad.getAttribute("cantidadClases");
					if(validarCantidadDiasSeleccionados(cantClase)){
						// obtener concepto seleccionado
					}else{
						((Combobox)evt.getTarget()).setSelectedItem(null);
					}
				}else{
					
				}
			}
		});
		this.horaDesdeJueves= new Combobox();
		this.horaDesdeJueves.setConstraint("strict");
		this.horaDesdeJueves.setWidth("60%");
		this.horaDesdeJueves.addEventListener(Events.ON_CHANGE, new EventListener()	{
			public void onEvent(Event evt){
				if (((Combobox)evt.getTarget()).getSelectedItem() != null){
					int cantClase=(Integer)nombreActividad.getAttribute("cantidadClases");
					if(validarCantidadDiasSeleccionados(cantClase)){
						// obtener concepto seleccionado
					}else{
						((Combobox)evt.getTarget()).setSelectedItem(null);
					}
				}else{
					
				}
			}
		});
		this.addFieldClases(horaDesdeMiercoles,horaDesdeJueves, 1);

		this.horaDesdeViernes= new Combobox();
		this.horaDesdeViernes.setConstraint("strict");
		this.horaDesdeViernes.setWidth("60%");
		this.horaDesdeViernes.addEventListener(Events.ON_CHANGE, new EventListener()	{
			public void onEvent(Event evt){
				if (((Combobox)evt.getTarget()).getSelectedItem() != null){
					int cantClase=(Integer)nombreActividad.getAttribute("cantidadClases");
					if(validarCantidadDiasSeleccionados(cantClase)){
						// obtener concepto seleccionado
					}else{
						((Combobox)evt.getTarget()).setSelectedItem(null);
					}
				}else{
					
				}
			}
		});
		this.horaDesdeSabado= new Combobox();
		this.horaDesdeSabado.setConstraint("strict");
		this.horaDesdeSabado.setWidth("60%");
		this.horaDesdeSabado.addEventListener(Events.ON_CHANGE, new EventListener()	{
			public void onEvent(Event evt){
				if (((Combobox)evt.getTarget()).getSelectedItem() != null){
					int cantClase=(Integer)nombreActividad.getAttribute("cantidadClases");
					if(validarCantidadDiasSeleccionados(cantClase)){
						// obtener concepto seleccionado
					}else{
						((Combobox)evt.getTarget()).setSelectedItem(null);
					}
				}else{
					
				}
			}
		});

		this.addFieldClases(horaDesdeViernes,horaDesdeSabado, 2);
				
	}

	public void setSelectedLunesClase (Clase selectedHPType){
		boolean found = false;
		int i = 0;
		while (!found && i<horaDesdeLunes.getItemCount()){
			if(selectedHPType.getId().intValue() == (((Clase) horaDesdeLunes.getItemAtIndex(i).getValue()).getId().intValue())){
				found = true;
				horaDesdeLunes.setSelectedIndex(i);
			}
			i++;
		}
	}
	
	public void setSelectedMartesClase (Clase selectedHPType){
		boolean found = false;
		int i = 0;
		while (!found && i<horaDesdeMartes.getItemCount()){
			if(selectedHPType.getId().intValue() == (((Clase) horaDesdeMartes.getItemAtIndex(i).getValue()).getId().intValue())){
				found = true;
				horaDesdeMartes.setSelectedIndex(i);
			}
			i++;
		}
	}
	
	public void setSelectedMiercolesClase (Clase selectedHPType){
		boolean found = false;
		int i = 0;
		while (!found && i<horaDesdeMiercoles.getItemCount()){
			if(selectedHPType.getId().intValue() == (((Clase) horaDesdeMiercoles.getItemAtIndex(i).getValue()).getId().intValue())){
				found = true;
				horaDesdeMiercoles.setSelectedIndex(i);
			}
			i++;
		}
	}
	
	public void setSelectedJuevesClase (Clase selectedHPType){
		boolean found = false;
		int i = 0;
		while (!found && i<horaDesdeJueves.getItemCount()){
			if(selectedHPType.getId().intValue() == (((Clase) horaDesdeJueves.getItemAtIndex(i).getValue()).getId().intValue())){
				found = true;
				horaDesdeJueves.setSelectedIndex(i);
			}
			i++;
		}
	}
	
	public void setSelectedViernesClase (Clase selectedHPType){
		boolean found = false;
		int i = 0;
		while (!found && i<horaDesdeViernes.getItemCount()){
			if(selectedHPType.getId().intValue() == (((Clase) horaDesdeViernes.getItemAtIndex(i).getValue()).getId().intValue())){
				found = true;
				horaDesdeViernes.setSelectedIndex(i);
			}
			i++;
		}
	}
	
	public void setSelectedSabadoClase (Clase selectedHPType){
		boolean found = false;
		int i = 0;
		while (!found && i<horaDesdeSabado.getItemCount()){
			if(selectedHPType.getId().intValue() == (((Clase) horaDesdeSabado.getItemAtIndex(i).getValue()).getId().intValue())){
				found = true;
				horaDesdeSabado.setSelectedIndex(i);
			}
			i++;
		}
	}
	
	public Combobox getHoraDesdeLunes() {
		return horaDesdeLunes;
	}

	public void setHoraDesdeLunes(Combobox horaDesdeLunes) {
		this.horaDesdeLunes = horaDesdeLunes;
	}

	public Combobox getHoraDesdeMartes() {
		return horaDesdeMartes;
	}

	public void setHoraDesdeMartes(Combobox horaDesdeMartes) {
		this.horaDesdeMartes = horaDesdeMartes;
	}

	public Combobox getHoraDesdeMiercoles() {
		return horaDesdeMiercoles;
	}

	public void setHoraDesdeMiercoles(Combobox horaDesdeMiercoles) {
		this.horaDesdeMiercoles = horaDesdeMiercoles;
	}

	public Combobox getHoraDesdeJueves() {
		return horaDesdeJueves;
	}

	public void setHoraDesdeJueves(Combobox horaDesdeJueves) {
		this.horaDesdeJueves = horaDesdeJueves;
	}

	public Combobox getHoraDesdeViernes() {
		return horaDesdeViernes;
	}

	public void setHoraDesdeViernes(Combobox horaDesdeViernes) {
		this.horaDesdeViernes = horaDesdeViernes;
	}

	public Combobox getHoraDesdeSabado() {
		return horaDesdeSabado;
	}

	public void setHoraDesdeSabado(Combobox horaDesdeSabado) {
		this.horaDesdeSabado = horaDesdeSabado;
	}

//	public Combobox getHoraDesdeDomingo() {
//		return horaDesdeDomingo;
//	}
//
//	public void setHoraDesdeDomingo(Combobox horaDesdeDomingo) {
//		this.horaDesdeDomingo = horaDesdeDomingo;
//	}

	public Textbox getNombreActividad() {
		return nombreActividad;
	}

	public void setNombreActividad(Textbox nombreActividad) {
		this.nombreActividad = nombreActividad;
	}

	public ClaseEJB getClaseEJB() {
		return claseEJB;
	}

	public void setClaseEJB(ClaseEJB claseEJB) {
		this.claseEJB = claseEJB;
	}

}
