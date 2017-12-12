package com.institucion.controller;

import javax.ejb.EJBException;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;

import com.institucion.bz.ClaseEJB;
import com.institucion.fm.conf.Session;
import com.institucion.fm.conf.exception.WebExceptionHandler;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.CrudComposer;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.PanelCrud;
import com.institucion.model.Actividad;
import com.institucion.model.Clase;
import com.institucion.model.ClaseCrud;
import com.institucion.model.Cliente;

public class ClaseCrudComposer extends CrudComposer {

	private ClaseEJB claseEJB;
	public final String idClase = "idClase";
	private Clase clase;

	private PanelCrud crud;	
	
	public ClaseCrudComposer() {
		claseEJB = BeanFactory.<ClaseEJB>getObject("fmEjbClase");
	}

	private ClaseCrud getClaseCrud() {
		return (ClaseCrud) (crud.getGridCrud());
	}


	public void onCreate() {
		Clase clase= (Clase) Session.getAttribute(idClase);
		setClase(clase);
		this.fromModelToView();
	}
	
	private void fromModelToView() {
		if(clase != null){
			
			if(clase.getNombre() != null){
				getClaseCrud().getNombre().setValue(clase.getNombre());
			}
			
			if(clase.getNombreAdic() != null){
				getClaseCrud().getNombreAdic().setValue(clase.getNombreAdic().toUpperCase());
			}
			
			if(clase.getActividad().getIdTipoCurso() != null){
				getClaseCrud().setSelectedTipoActividad(clase.getActividad().getIdTipoCurso());
			}

			if(clase.getActividad() != null){
				getClaseCrud().setSelectedActividad(clase.getActividad());
			}	
			
			getClaseCrud().setSelectedDisponible(clase.isDisponible());

			
			if(clase.getEsClaseSinHora() != null && clase.getEsClaseSinHora().booleanValue()){
				getClaseCrud().setSelectedEsClaseSinHora(clase.getEsClaseSinHora());
				
			}else{
				getClaseCrud().setSelectedEsClaseSinHora(false);
				// Del crud de horarios
				if(clase.getHoraDesde() != null){
					getClaseCrud().setHoraDesde(clase.getHoraDesde());
				}			
				
				if(clase.getHoraHasta() != null){
					getClaseCrud().setHoraHasta(clase.getHoraHasta());
				}	
			}
			
			getClaseCrud().setLunes(clase.getLunes());
			getClaseCrud().setMartes(clase.getMartes());
			getClaseCrud().setMiercoles(clase.getMiercoles());
			getClaseCrud().setJueves(clase.getJueves());
			getClaseCrud().setViernes(clase.getViernes());
			getClaseCrud().setSabado(clase.getSabado());
			getClaseCrud().setDomingo(clase.getDomingo());
			
			
			getClaseCrud().getActividadCurso().setDisabled(true);
			getClaseCrud().getTipoCurso().setDisabled(true);
			getClaseCrud().getEsClaseSinHora().setDisabled(true);
			getClaseCrud().getHoraDesdeTb().setDisabled(true);
			getClaseCrud().getHoraHastaTb().setDisabled(true);
			getClaseCrud().getLunes().setDisabled(true);
			getClaseCrud().getMartes().setDisabled(true);
			getClaseCrud().getMiercoles().setDisabled(true);
			getClaseCrud().getJueves().setDisabled(true);
			getClaseCrud().getViernes().setDisabled(true);
			getClaseCrud().getSabado().setDisabled(true);
			getClaseCrud().getDomingo().setDisabled(true);


		}		
	}
	public void setearNombreClase(){
		String diasDeSemana="";
		String nomAdicional=clase.getNombreAdic();
		
		if(clase.getLunes()) diasDeSemana=diasDeSemana+"Lunes";
		if(clase.getMartes()){ 
			if(diasDeSemana.equalsIgnoreCase(""))diasDeSemana=diasDeSemana+"Martes"; else diasDeSemana=diasDeSemana+"-Martes";
		} 
		if(clase.getMiercoles()){
			if(diasDeSemana.equalsIgnoreCase(""))diasDeSemana=diasDeSemana+"Miercoles"; else diasDeSemana=diasDeSemana+"-Miercoles";
		} 
		if(clase.getJueves()){
			if(diasDeSemana.equalsIgnoreCase(""))diasDeSemana=diasDeSemana+"Jueves"; else diasDeSemana=diasDeSemana+"-Jueves";
		} 
		if(clase.getViernes()){
			if(diasDeSemana.equalsIgnoreCase(""))diasDeSemana=diasDeSemana+"Viernes"; else diasDeSemana=diasDeSemana+"-Viernes";
		} 
		if(clase.getSabado()){
			if(diasDeSemana.equalsIgnoreCase(""))diasDeSemana=diasDeSemana+"Sabado"; else diasDeSemana=diasDeSemana+"-Sabado";
		} 
		if(clase.getDomingo()){
			if(diasDeSemana.equalsIgnoreCase(""))diasDeSemana=diasDeSemana+"Domingo"; else diasDeSemana=diasDeSemana+"-Domingo";
		} 
		
		if(clase.getEsClaseSinHora() != null && clase.getEsClaseSinHora().booleanValue()){
			if(nomAdicional != null && !nomAdicional.trim().equalsIgnoreCase("")){
				String nombreClase= clase.getActividad().getNombre() +" "+nomAdicional.toUpperCase()+" |"+ diasDeSemana ;
				clase.setNombre(nombreClase);
				
			}else{
				String nombreClase= clase.getActividad().getNombre() +" |"+ diasDeSemana ;
				clase.setNombre(nombreClase);
	
			}			
		}else{
			String horaDesde= null;
			if(clase.getHoraDesde().getMinutes() < 10){ 
				horaDesde= clase.getHoraDesde().getHours() +":0"+clase.getHoraDesde().getMinutes();	
			}else{
				horaDesde= clase.getHoraDesde().getHours() +":"+clase.getHoraDesde().getMinutes();
			}
			
			String horaHasta= null;
			if(clase.getHoraHasta().getMinutes() < 10){ 
				horaHasta= clase.getHoraHasta().getHours() +":0"+clase.getHoraHasta().getMinutes();	
			}else{
				horaHasta= clase.getHoraHasta().getHours() +":"+clase.getHoraHasta().getMinutes();
			}
			
			if(nomAdicional != null && !nomAdicional.trim().equalsIgnoreCase("")){
				String nombreClase= clase.getActividad().getNombre()+" "+nomAdicional.toUpperCase() +" |"+ diasDeSemana + "| " + horaDesde +"-"+ horaHasta  ;
				clase.setNombre(nombreClase);

			}else{
				String nombreClase= clase.getActividad().getNombre() +" |"+ diasDeSemana + "| " + horaDesde +"-"+ horaHasta  ;
				clase.setNombre(nombreClase);
				
			}
		}
	}
	
	public void onSave(Event event) throws Exception {
		try{
			validateCrud();
			
			if (Sessions.getCurrent().getAttribute(idClase) != null) {
				// es una modificacion
					clase = (Clase) Sessions.getCurrent().getAttribute(idClase);
					this.fromViewToModel(clase);
					// poner nombre a la actividad
					setearNombreClase();
									
					claseEJB.save(clase);
					
					if (Sessions.getCurrent().getAttribute("isClienteFromIngresoInscripcion") != null){ 
						Sessions.getCurrent().setAttribute("isClienteFromIngresoInscripcion", null);
						super.gotoPage("/institucion/clientes-selector.zul");
					}else	
						super.gotoPage("/institucion/clase-selector.zul");
					
			} else {
				// es nuevo 
				clase= new Clase();
				this.fromViewToModel(clase);
				// poner nombre a la actividad
				setearNombreClase();
				
				claseEJB.save(clase);
				
				Cliente cli= (Cliente)com.institucion.fm.conf.Session.getAttribute("idCliente");
				Boolean cliFromIngreso= (Boolean)com.institucion.fm.conf.Session.getAttribute("isClaseFromIngresoInscripcion");

				if(cliFromIngreso != null && cliFromIngreso){
					super.gotoPage("/institucion/ingresoInscripcion-selector.zul");
					return ;
				}
				
				if(cli != null){
					super.gotoPage("/institucion/subscripcion-crud.zul");
					return ;
				}
				
				if (Sessions.getCurrent().getAttribute("isClaseFromIngresoInscripcion") != null){
					Sessions.getCurrent().setAttribute("isClaseFromIngresoInscripcion", null);
					super.gotoPage("/institucion/ingresoInscripcion-selector.zul");
				}else{
					super.gotoPage("/institucion/clase-selector.zul");
				}
			}
			
		} catch (EJBException ex) {
			WebExceptionHandler.handleThis(ex);
		}
	}
		
	public void onBack(Event event) {
		if (Sessions.getCurrent().getAttribute("isClaseFromIngresoInscripcion") != null){ 
			Sessions.getCurrent().setAttribute("isClaseFromIngresoInscripcion", null);
			super.gotoPage("/institucion/ingresoInscripcion-selector.zul");
		}else	
			super.gotoPage("/institucion/clase-selector.zul");
	}

	private Clase fromViewToModel(Clase cliente) {
		
		if(getClaseCrud().getNombre().getValue() != null){
			cliente.setNombre(getClaseCrud().getNombre().getValue());	
		}
	
		if(getClaseCrud().getNombreAdic().getValue() != null){
			cliente.setNombreAdic(getClaseCrud().getNombreAdic().getValue());	
		}
		
		if(getClaseCrud().getDisponible().getSelectedItem() != null)
			cliente.setDisponible(((Boolean)getClaseCrud().getDisponible().getSelectedItem().getValue()));

		if(getClaseCrud().getActividadCurso().getSelectedItem() != null)
			cliente.setActividad((Actividad)(getClaseCrud().getActividadCurso().getSelectedItem()).getValue());	
	
		if(getClaseCrud().getEsClaseSinHora().getSelectedItem() != null){

			if((Boolean)getClaseCrud().getEsClaseSinHora().getSelectedItem().getValue()){
				cliente.setEsClaseSinHora(true);
			}else{
				cliente.setEsClaseSinHora(false);
				if(getClaseCrud().getHoraDesde() != null)
					cliente.setHoraDesde(getClaseCrud().getHoraDesde());	

				if(getClaseCrud().getHoraHasta() != null)
					cliente.setHoraHasta(getClaseCrud().getHoraHasta());	

			}
			
		}else{
			cliente.setEsClaseSinHora(false);

			if(getClaseCrud().getHoraDesde() != null)
				cliente.setHoraDesde(getClaseCrud().getHoraDesde());	

			if(getClaseCrud().getHoraHasta() != null)
				cliente.setHoraHasta(getClaseCrud().getHoraHasta());	
		}
		
		cliente.setLunes(getClaseCrud().getLunes().isChecked());
		cliente.setMartes(getClaseCrud().getMartes().isChecked());
		cliente.setMiercoles(getClaseCrud().getMiercoles().isChecked());
		cliente.setJueves(getClaseCrud().getJueves().isChecked());
		cliente.setViernes(getClaseCrud().getViernes().isChecked());
		cliente.setSabado(getClaseCrud().getSabado().isChecked());
		cliente.setDomingo(getClaseCrud().getDomingo().isChecked());
				
		return cliente;
	}

	private boolean validateCrud() {

		
		if(getClaseCrud().getTipoCurso().getSelectedItem() == null)
			throw new WrongValueException(getClaseCrud().getTipoCurso(), I18N.getLabel("error.empty.field"));
			
		if(getClaseCrud().getDisponible().getSelectedItem() == null)
			throw new WrongValueException(getClaseCrud().getDisponible(), I18N.getLabel("error.empty.field"));

		if(getClaseCrud().getActividadCurso().getSelectedItem() == null)
			throw new WrongValueException(getClaseCrud().getActividadCurso(), I18N.getLabel("error.empty.field"));
	
		if(getClaseCrud().getEsClaseSinHora().getSelectedItem() == null){
			throw new WrongValueException(getClaseCrud().getEsClaseSinHora(), I18N.getLabel("error.empty.field"));
		
		}else if((Boolean)getClaseCrud().getEsClaseSinHora().getSelectedItem().getValue()){
			
		}else{
			if(getClaseCrud().getHoraDesde() == null)
				throw new WrongValueException(getClaseCrud().getHoraDesdeTb(), I18N.getLabel("error.empty.field"));
		
			if(getClaseCrud().getHoraHasta() == null)
				throw new WrongValueException(getClaseCrud().getHoraHastaTb(), I18N.getLabel("error.empty.field"));

			if(!getClaseCrud().isValidHourFromTo())
				throw new WrongValueException(getClaseCrud().getHoraDesdeTb(), I18N.getLabel("error.fechaincorrecta"));
		}
		
		if(!getClaseCrud().isDaysChecked())			
			throw new WrongValueException(getClaseCrud().getLunes(), I18N.getLabel("error.clasesnoseleccionada"));
	
		return true;
	}
	
	public Clase getClase() {
		return clase;
	}

	public void setClase(Clase clase) {
		this.clase = clase;
	}

}
