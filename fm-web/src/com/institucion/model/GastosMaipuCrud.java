package com.institucion.model;

import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import com.institucion.bz.ClaseEJB;
import com.institucion.desktop.delegated.GastosDelegate;
import com.institucion.desktop.helper.GastosViewHelper;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.validator.TextConstraint;
import com.institucion.fm.desktop.view.GridCrud;
import com.institucion.fm.desktop.view.RequiredLabel;

public class GastosMaipuCrud extends GridCrud{

	private static final long serialVersionUID = 1L;
	
	private Combobox anio;   
	private Combobox quincena;
	private Combobox tipoGastoCB;

	private Textbox comentario;
	private Intbox dinero;
	private GastosDelegate delegate;
	private Combobox empleadoCB;
	private Label empleado;

	private ClaseEJB claseEJB;

	public GastosMaipuCrud (){
		super();
		claseEJB = BeanFactory.<ClaseEJB>getObject("fmEjbClase");

		this.makeFields();
	}
	    
	private void makeFields(){
					
		Label anioL= new RequiredLabel("Temporada:" );
		anio = GastosViewHelper.getComboBoxAnio();
		this.addField(anioL, anio);

		Label mesL= new RequiredLabel("Quincena");
		
		List<Quincena> nombreCursos=claseEJB.findAllConActividadTomaListaDelDiaNombreCurso();

		quincena= new Combobox();
		quincena = getComboBoxQuincenas(nombreCursos);
		quincena.setConstraint("strict");
		this.addField(mesL, quincena);
		
		String tipoGasto =  I18N.getLabel("gastos.tipogasto");
		tipoGastoCB= new Combobox();
		tipoGastoCB = GastosViewHelper.getComboBoxTipoGastoMaipu(tipoGastoCB, false, false);
		tipoGastoCB.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt) {

				if(tipoGastoCB.getSelectedItem() != null && 
						(((GastosMaipuEnum)tipoGastoCB.getSelectedItem().getValue())).toInt() == GastosMaipuEnum.SUELDOS.toInt()){
					
					empleadoCB.setSelectedItem(null);
					empleadoCB.setVisible(true);
					empleado.setVisible(true);
				}else{
					empleadoCB.setSelectedItem(null);
					empleadoCB.setVisible(false);
					empleado.setVisible(false);
				}
			}
		});	

		this.addField(new RequiredLabel(tipoGasto), tipoGastoCB);
		
		empleadoCB= new Combobox();
		empleadoCB= GastosViewHelper.getComboBoxEmpleados(empleadoCB);
		
		empleado= new RequiredLabel("Empleado");
		empleado.setVisible(false);
		empleadoCB.setVisible(false);
		this.addField(empleado, empleadoCB);

		dinero = new Intbox();
		dinero.setConstraint("/(([0-9]+)?)+/"); //, /(([0-9]+)?)+/"); //"/([0-9])/"); //   ([0-9]+)?)+/");
		dinero.addEventListener(Events.ON_CHANGE, new EventListener(){
			public void onEvent(Event evt){
				
				if(((InputEvent)evt).getValue() != null){
					if(((InputEvent)evt).getValue().contains(",")){
						String s=((InputEvent)evt).getValue();
						s=s.substring(0, s.indexOf(","));
						dinero.setValue(Integer.parseInt(s));
						dinero.setText(s);
						((Intbox)evt.getTarget()).setValue(Integer.parseInt(s));
					}
				}
			}
		});
		
		this.addField(new RequiredLabel(I18N.getLabel("gastos.list.dinero")), dinero);
		
		comentario= new Textbox();
		comentario.setRows(3);
		comentario.setMaxlength(150);
		comentario.setWidth("auto");
		comentario.setConstraint(new TextConstraint());
		
		this.addField(new Label(I18N.getLabel("gastos.list.comentario")), comentario); 
	}

	public  Combobox getComboBoxQuincenas(List<Quincena> cursos) {
		Constraint brandC = quincena.getConstraint();
		quincena.setConstraint("");
		quincena.setText("");
		quincena.setConstraint(brandC);
		quincena.getItems().clear();

		if(cursos != null){
			for (Quincena classe : cursos) {
				Comboitem item;
				item = new Comboitem("MAIPU: "+classe.getNombre());
				item.setValue(classe);
				quincena.appendChild(item);	
			}	
		}
		return quincena;
	}
	
	public void setSelectedEmpleado (Empleado selectedHPType){
		boolean found = false;
		int i = 0;
		while (!found && i<empleadoCB.getItemCount()){
			if(selectedHPType.getId().intValue() == (((Empleado) empleadoCB.getItemAtIndex(i).getValue()).getId().intValue())){
				found = true;
				empleadoCB.setSelectedIndex(i);
			}
			i++;
		}
	}

	public void setSelectedTipoGasto (GastosMaipuEnum selectedHPType){
		boolean found = false;
		int i = 0;
		while (!found && i<tipoGastoCB.getItemCount()){
			if(selectedHPType.toInt() == (((GastosMaipuEnum) tipoGastoCB.getItemAtIndex(i).getValue()).toInt())){
				found = true;
				tipoGastoCB.setSelectedIndex(i);
			}
			i++;
		}
	}
	
	public void setSelectedQuincena (Quincena selectedHPType){
		boolean found = false;
		int i = 0;
		while (!found && i<quincena.getItemCount()){
			if(selectedHPType.getId().intValue()  == (((Quincena) quincena.getItemAtIndex(i).getValue())).getId().intValue()){
				found = true;
				quincena.setSelectedIndex(i);
			}
			i++;
		}
	}
	
	public void setSelectedAnio (Long selectedHPType){
		boolean found = false;
		int i = 0;
		while (!found && i<anio.getItemCount()){
			if(selectedHPType.intValue()  == (((Long) anio.getItemAtIndex(i).getValue())).intValue()){
				found = true;
				anio.setSelectedIndex(i);
			}
			i++;
		}
	}
	
	public Combobox getAnio() {
		return anio;
	}


	public void setAnio(Combobox anio) {
		this.anio = anio;
	}


	public Combobox getQuincena() {
		return quincena;
	}


	public void setQuincena(Combobox quince) {
		this.quincena = quince;
	}


	public Combobox getTipoGastoCB() {
		return tipoGastoCB;
	}


	public void setTipoGastoCB(Combobox tipoGastoCB) {
		this.tipoGastoCB = tipoGastoCB;
	}


	public Textbox getComentario() {
		return comentario;
	}


	public void setComentario(Textbox comentario) {
		this.comentario = comentario;
	}


	public Intbox getDinero() {
		return dinero;
	}


	public void setDinero(Intbox dinero) {
		this.dinero = dinero;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public GastosDelegate getDelegate() {
		return delegate;
	}

	public void setDelegate(GastosDelegate delegate) {
		this.delegate = delegate;
	}

	public Combobox getEmpleadoCB() {
		return empleadoCB;
	}

	public void setEmpleadoCB(Combobox empleadoCB) {
		this.empleadoCB = empleadoCB;
	}

	public Label getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Label empleado) {
		this.empleado = empleado;
	}

//	public Datebox getFecha() {
//		return fecha;
//	}
//
//	public void setFecha(Datebox fecha) {
//		this.fecha = fecha;
//	}
}