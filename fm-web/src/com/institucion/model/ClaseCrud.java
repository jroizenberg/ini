package com.institucion.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timebox;

import com.institucion.bz.CursoEJB;
import com.institucion.desktop.helper.BooleanViewHelper;
import com.institucion.desktop.helper.TipoCursosViewHelper;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridCrud;
import com.institucion.fm.desktop.view.RequiredLabel;

public class ClaseCrud extends GridCrud{

	private static final long serialVersionUID = 1L;
	
	private Combobox tipoCurso;
	private Combobox actividadCurso;	
	private Label nombre;
	private Textbox nombreAdic;

	private Timebox horaDesde;
	
	private Checkbox lunes;
	private Checkbox martes;
	private Checkbox miercoles;
	private Checkbox jueves;
	private Checkbox viernes;
	private Checkbox sabado;
	private Checkbox domingo;
	private Timebox horaHasta;
	private Combobox disponible;

	private Combobox esClaseSinHora;

	private Map <Integer, List<Actividad>>mapaActividadesPorTipoActividad;

	private CursoEJB cursoEJB;
	
	public ClaseCrud (){
		super();
		cursoEJB = BeanFactory.<CursoEJB>getObject("fmEjbCurso");
		this.makeFields();
	}
	    
	public Date getHoraHasta() {
		return horaHasta.getValue();
	}

	public void setHoraHasta(Date hourFrom) {
		this.horaHasta.setValue(hourFrom);
	}
	private void makeFields(){

		nombre= new Label();
		nombre.setMaxlength(50);
		this.addField(new RequiredLabel(I18N.getLabel("client.nombre")), nombre);

		disponible = BooleanViewHelper.getComboBox();
		disponible.setConstraint("strict");
		this.addField(new RequiredLabel("Disponible"), disponible);
		
		
		nombreAdic = new Textbox();
		this.addField(new Label("Texto Agregado al Nombre"), nombreAdic);

		
		tipoCurso =  TipoCursosViewHelper.getComboBoxTipoActividad();
		tipoCurso.setConstraint("strict");
		tipoCurso.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt) {
				
				if(tipoCurso.getSelectedItem() != null){
					List<Actividad> actividades=mapaActividadesPorTipoActividad.get(((TipoCursoEnum)tipoCurso.getSelectedItem().getValue()).toInt());
					if(actividades != null){
						setActividadCurso(getComboBoxActividad(actividades, actividadCurso));						
					}
				}
			}
		});	
		this.addField(new RequiredLabel(I18N.getLabel("curso.tipoCurso")), tipoCurso);
		
		List<Actividad> actividades=cursoEJB.findAllActividad();
		mapaActividadesPorTipoActividad= new HashMap<Integer, List<Actividad>> ();
		mapaActividadesPorTipoActividad.put(TipoCursoEnum.ACTIVIDADES_FISICAS.toInt(), new ArrayList<Actividad>());
		mapaActividadesPorTipoActividad.put(TipoCursoEnum.MASAJES.toInt(), new ArrayList <Actividad>());
		mapaActividadesPorTipoActividad.put(TipoCursoEnum.TRATAMIENTOS_KINESICOS.toInt(), new ArrayList<Actividad>());
		mapaActividadesPorTipoActividad.put(TipoCursoEnum.NATACION.toInt(), new ArrayList<Actividad>());
		mapaActividadesPorTipoActividad.put(TipoCursoEnum.OTROS.toInt(), new ArrayList<Actividad>());
		
		for (Actividad actividad : actividades) {
			mapaActividadesPorTipoActividad.get(actividad.getIdTipoCurso().toInt()).add(actividad);		
		}
		
		actividadCurso = new Combobox();
		actividadCurso.setConstraint("strict");
		this.addField(new RequiredLabel(I18N.getLabel("curso.actividad")),actividadCurso);
		
		esClaseSinHora = BooleanViewHelper.getComboBox();
		esClaseSinHora.setConstraint("strict");
		esClaseSinHora.setSelectedIndex(0);
		esClaseSinHora.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt) {
				
				if(esClaseSinHora.getSelectedItem() != null){
					if((Boolean)esClaseSinHora.getSelectedItem().getValue() ){
						horaDesde.setVisible(false);
						horaDesde.setValue(new Date(0,0,0,0,0));
						horaHasta.setVisible(false);
						horaHasta.setValue(new Date(0,0,0,0,0));
					}else{
						horaDesde.setVisible(true);
						horaDesde.setValue(new Date(0,0,0,0,0));
						horaHasta.setVisible(true);	
						horaHasta.setValue(new Date(0,0,0,0,0));

					}
				}else{
					horaDesde.setVisible(true);
					horaDesde.setValue(new Date(0,0,0,0,0));
					horaHasta.setVisible(true);
					horaHasta.setValue(new Date(0,0,0,0,0));
				}
			}
		});	
		this.addField(new RequiredLabel("Clase sin horarios"), esClaseSinHora);
		
		this.horaDesde= new Timebox();
		
		this.horaDesde.setButtonVisible(false);
		
		this.horaDesde.setValue(new Date(0,0,0,0,0));
//		this.horaDesde.setText(new Date());
		this.horaDesde.setConstraint("no empty");
		this.horaDesde.addEventListener(Events.ON_BLUR, new EventListener() {
			public void onEvent(Event evt)
			{
				
				System.out.println(((Timebox)evt.getTarget()).getValue());
				horaDesde.setValue(((Timebox)evt.getTarget()).getValue());
			}
			}); 
		this.addField(new RequiredLabel(I18N.getLabel("crud.schedule.hourfrom")), horaDesde);
	
		this.horaHasta= new Timebox();
		this.horaHasta.setValue(new Date(0,0,0,0,0));
		this.horaHasta.setButtonVisible(false);
		this.horaHasta.setConstraint("no empty");
		this.horaHasta.addEventListener(Events.ON_BLUR, new EventListener() {
			public void onEvent(Event evt){
				System.out.println(((Timebox)evt.getTarget()).getValue());
				horaHasta.setValue(((Timebox)evt.getTarget()).getValue());
			}
			}); 
		this.addField(new RequiredLabel(I18N.getLabel("crud.schedule.hourto")), horaHasta);
		
			
		this.lunes = new Checkbox();
		this.lunes.addEventListener(Events.ON_CHECK, new EventListener() {
			public void onEvent(Event evt) {
				
				if(lunes.isChecked()){
					martes.setChecked(false);
					miercoles.setChecked(false);
					jueves.setChecked(false);
					viernes.setChecked(false);
					sabado.setChecked(false);
					domingo.setChecked(false);
				}
			}
		});	
		this.addField(new Label(I18N.getLabel("dayofweek.monday")), this.lunes);
	
		this.martes = new Checkbox();
		this.martes.addEventListener(Events.ON_CHECK, new EventListener() {
			public void onEvent(Event evt) {
				if(martes.isChecked()){
					lunes.setChecked(false);
					miercoles.setChecked(false);
					jueves.setChecked(false);
					viernes.setChecked(false);
					sabado.setChecked(false);
					domingo.setChecked(false);
				}
			}
		});	
		this.addField(new Label(I18N.getLabel("dayofweek.tuesday")), this.martes);

		this.miercoles = new Checkbox();
		this.miercoles.addEventListener(Events.ON_CHECK, new EventListener() {
			public void onEvent(Event evt) {
				if(miercoles.isChecked()){
					martes.setChecked(false);
					lunes.setChecked(false);
					jueves.setChecked(false);
					viernes.setChecked(false);
					sabado.setChecked(false);
					domingo.setChecked(false);
				}
			}
		});	
		this.addField(new Label(I18N.getLabel("dayofweek.wednesday")), this.miercoles);

		this.jueves = new Checkbox();
		this.jueves.addEventListener(Events.ON_CHECK, new EventListener() {
			public void onEvent(Event evt) {
				if(jueves.isChecked()){
					martes.setChecked(false);
					miercoles.setChecked(false);
					lunes.setChecked(false);
					viernes.setChecked(false);
					sabado.setChecked(false);
					domingo.setChecked(false);
				}
			}
		});	
		this.addField(new Label(I18N.getLabel("dayofweek.thursday")), this.jueves);

		this.viernes = new Checkbox();
		this.viernes.addEventListener(Events.ON_CHECK, new EventListener() {
			public void onEvent(Event evt) {
				if(viernes.isChecked()){
					martes.setChecked(false);
					miercoles.setChecked(false);
					jueves.setChecked(false);
					lunes.setChecked(false);
					sabado.setChecked(false);
					domingo.setChecked(false);
				}
			}
		});	
		this.addField(new Label(I18N.getLabel("dayofweek.friday")), this.viernes);

		this.sabado = new Checkbox();
		this.sabado.addEventListener(Events.ON_CHECK, new EventListener() {
			public void onEvent(Event evt) {
				if(sabado.isChecked()){
					martes.setChecked(false);
					miercoles.setChecked(false);
					jueves.setChecked(false);
					viernes.setChecked(false);
					lunes.setChecked(false);
					domingo.setChecked(false);
				}
			}
		});	
		this.addField(new Label(I18N.getLabel("dayofweek.saturday")), this.sabado);

		this.domingo = new Checkbox();
		this.domingo.addEventListener(Events.ON_CHECK, new EventListener() {
			public void onEvent(Event evt) {
				
				if(domingo.isChecked()){
					martes.setChecked(false);
					miercoles.setChecked(false);
					jueves.setChecked(false);
					viernes.setChecked(false);
					lunes.setChecked(false);
					sabado.setChecked(false);	
				}
			}
		});	
		this.addField(new Label(I18N.getLabel("dayofweek.sunday")), this.domingo);

		this.addField(new Label("  "), new Label("  "));
		
	
	}

	
	public void setSelectedDisponible (boolean tieneCertficadobool){
		boolean found = false;
		int i = 0;
		while(!found && i<disponible.getItemCount()){
			if(disponible.getItemAtIndex(i) != null 
					&&(Boolean)disponible.getItemAtIndex(i).getValue() == tieneCertficadobool){
				found = true;
				disponible.setSelectedIndex(i);
			}
			i++;
		}
	}	
	

	public Combobox getDisponible() {
		return disponible;
	}


	public void setDisponible(Combobox disponible) {
		this.disponible = disponible;
	}
	
	@SuppressWarnings("deprecation")
	public void clear(){		
		this.domingo.setChecked(false);
		this.lunes.setChecked(false);
		this.martes.setChecked(false);
		this.miercoles.setChecked(false);
		this.jueves.setChecked(false);
		this.viernes.setChecked(false);
		this.sabado.setChecked(false);
		this.horaDesde.setConstraint("strict");
		this.horaDesde.setValue(new Date(0,0,0,0,0));
		this.horaHasta.setConstraint("strict");
		this.horaHasta.setValue(new Date(0,0,0,0,0));
		nombreAdic.setValue("");
		
	}
	
	public boolean isDaysChecked(){
		if (domingo.isChecked() || lunes.isChecked() || martes.isChecked() || miercoles.isChecked() || jueves.isChecked() 
				|| viernes.isChecked() || sabado.isChecked() )
			return true;
		return false;
	}
	
	public boolean isValidHourFromTo(){
		return this.horaDesde.getValue().before(this.horaHasta.getValue()); 		
	}
	
	public void setLunes(Boolean isChecked) {
		this.lunes.setChecked(isChecked);
	}

	public void setMartes(Boolean isChecked) {
		this.martes.setChecked(isChecked);
	}

	public void setMiercoles(Boolean isChecked) {
		this.miercoles.setChecked(isChecked);
	}

	public void setJueves(Boolean isChecked) {
		this.jueves.setChecked(isChecked);
	}

	public void setViernes(Boolean isChecked) {
		this.viernes.setChecked(isChecked);
	}

	public void setSabado(Boolean isChecked) {
		this.sabado.setChecked(isChecked);
	}

	public void setDomingo(Boolean isChecked) {
		this.domingo.setChecked(isChecked);
	}

	public Checkbox getLunes() {
		return lunes;
	}

	public Textbox getNombreAdic() {
		return nombreAdic;
	}

	public void setNombreAdic(Textbox nombreAdic) {
		this.nombreAdic = nombreAdic;
	}

	public void setLunes(Checkbox lunes) {
		this.lunes = lunes;
	}

	public Checkbox getMartes() {
		return martes;
	}

	public void setMartes(Checkbox martes) {
		this.martes = martes;
	}

	public Checkbox getMiercoles() {
		return miercoles;
	}

	public void setMiercoles(Checkbox miercoles) {
		this.miercoles = miercoles;
	}

	public Checkbox getJueves() {
		return jueves;
	}

	public void setJueves(Checkbox jueves) {
		this.jueves = jueves;
	}

	public Checkbox getViernes() {
		return viernes;
	}

	public void setViernes(Checkbox viernes) {
		this.viernes = viernes;
	}

	public Checkbox getSabado() {
		return sabado;
	}

	public void setSabado(Checkbox sabado) {
		this.sabado = sabado;
	}

	public Checkbox getDomingo() {
		return domingo;
	}

	public void setDomingo(Checkbox domingo) {
		this.domingo = domingo;
	}

	public void setHoraHasta(Timebox horaDesde) {
		this.horaHasta = horaDesde;
	}

	public Timebox getHoraHastaTb() {
		return horaHasta;
	}
	
	public Date getHoraDesde() {
		return horaDesde.getValue();
	}

	public Timebox getHoraDesdeTb() {
		return horaDesde;
	}
	
	public void setHoraDesde(Date hourFrom) {
		this.horaDesde.setValue(hourFrom);
	}
	
	public void setSelectedEsClaseSinHora (Boolean selectedHPType){
		boolean found = false;
		int i = 0;
		while (!found && i<esClaseSinHora.getItemCount()){
			if(selectedHPType == (((Boolean) esClaseSinHora.getItemAtIndex(i).getValue()))){
				found = true;
				esClaseSinHora.setSelectedIndex(i);
			}
			i++;
		}
	}
	
	public Combobox getComboBoxActividad(List<Actividad> actividades, Combobox act) {
		Constraint brandC = act.getConstraint();
		act.setConstraint("");
		act.setText("");
		act.setConstraint(brandC);
		act.getItems().clear();
		
		for (Actividad actividad : actividades) {
			Comboitem item;
			item = new Comboitem(actividad.getNombre());
			item.setValue(actividad);
			act.appendChild(item);		
		}

		return act;
	}
	
	public void setSelectedTipoActividad (TipoCursoEnum selectedHPType){
		boolean found = false;
		int i = 0;
		while (!found && i<tipoCurso.getItemCount()){
			if(selectedHPType.toInt() == (((TipoCursoEnum) tipoCurso.getItemAtIndex(i).getValue()).toInt())){
				found = true;
				tipoCurso.setSelectedIndex(i);
			}
			i++;
		}
		if(found){
			List<Actividad> actividades=mapaActividadesPorTipoActividad.get(((TipoCursoEnum)tipoCurso.getSelectedItem().getValue()).toInt());
			if(actividades != null){
				setActividadCurso(getComboBoxActividad(actividades, actividadCurso));						
			}
			
		}
	}
	
	public void setSelectedActividad (Actividad selectedHPType){
		boolean found = false;
		int i = 0;
		while (!found && i<actividadCurso.getItemCount()){
			if(selectedHPType.getId().intValue() == (((Actividad) actividadCurso.getItemAtIndex(i).getValue()).getId().intValue())){
				found = true;
				actividadCurso.setSelectedIndex(i);
			}
			i++;
		}
	}

	
	public Combobox getTipoCurso() {
		return tipoCurso;
	}


	public Combobox getEsClaseSinHora() {
		return esClaseSinHora;
	}

	public void setEsClaseSinHora(Combobox esClaseSinHora) {
		this.esClaseSinHora = esClaseSinHora;
	}

	public void setTipoCurso(Combobox tipoCurso) {
		this.tipoCurso = tipoCurso;
	}


	public Label getNombre() {
		return nombre;
	}

	public void setNombre(Label nombre) {
		this.nombre = nombre;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public Combobox getActividadCurso() {
		return actividadCurso;
	}

	public void setActividadCurso(Combobox actividadCurso) {
		this.actividadCurso = actividadCurso;
	}

	public CursoEJB getCursoEJB() {
		return cursoEJB;
	}

	public void setCursoEJB(CursoEJB cursoEJB) {
		this.cursoEJB = cursoEJB;
	}

}
