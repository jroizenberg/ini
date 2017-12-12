package com.institucion.model;

import java.util.Calendar;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;

import com.institucion.bz.ClaseEJB;
import com.institucion.desktop.delegated.SucursalDelegate;
import com.institucion.desktop.helper.GastosViewHelper;
import com.institucion.desktop.helper.SucursalViewHelper;
import com.institucion.fm.conf.Session;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridFilter;
import com.institucion.fm.desktop.view.RequiredLabel;
import com.institucion.fm.filteradv.model.CriteriaClause;

public class SueldosFilter extends GridFilter {
	private static final long serialVersionUID = 1L;

	private Combobox sucursal;
	private Combobox anio;   
	private Combobox mes;
	private Combobox empleadoCB;   

	private Combobox quincena;
	private ClaseEJB claseEJB;
	private SucursalDelegate delegate;
	
	public SueldosFilter()	{
		super();
		claseEJB = BeanFactory.<ClaseEJB>getObject("fmEjbClase");

		buildFilter();
	}
	
	private void buildFilter() {
				
		Row row3 = new Row();
			
		String sucursalLabel =  "Sucursal";
		row3.appendChild(new Label(sucursalLabel));
		sucursal= SucursalViewHelper.getComboBoxConOpcionTodos();			
		sucursal.setConstraint("strict");
		sucursal.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
				
				if(sucursal.getSelectedItem() != null 
						&& sucursal.getSelectedItem().getValue() instanceof SucursalEnum
						&& ((SucursalEnum)sucursal.getSelectedItem().getValue()).toInt() == SucursalEnum.CENTRO.toInt()){
					anio.setVisible(true);
					mes.setVisible(true);
//					tipoGastoCB.setVisible(true);
//					
//					tipoGastoCB.getItems().clear();
//					tipoGastoCB = GastosViewHelper.getComboBoxTipoGasto(tipoGastoCB, true);
					quincena.setVisible(false);
				}else if(sucursal.getSelectedItem() != null 
						&& sucursal.getSelectedItem().getValue() instanceof SucursalEnum
						&& ((SucursalEnum)sucursal.getSelectedItem().getValue()).toInt() ==	SucursalEnum.MAIPU.toInt()){
//					tipoGastoCB.getItems().clear();
//					tipoGastoCB = GastosViewHelper.getComboBoxTipoGastoMaipu(tipoGastoCB, true);
					quincena.setVisible(true);
					mes.setVisible(false);
					anio.setVisible(true);
//					tipoGastoCB.setVisible(true);
					
				}else{
//					tipoGastoCB.getItems().clear();
//					tipoGastoCB.setVisible(false);
					mes.setVisible(true);
					quincena.setVisible(true);
					
				}
				
				delegate.actualizarLitado();
			}
		});	

		if(Session.getAttribute("sucursalPrincipalSeleccionada") != null){
			
			if((Session.getAttribute("sucursalPrincipalSeleccionada")) instanceof SucursalEnum ){
				SucursalEnum suc=((SucursalEnum)Session.getAttribute("sucursalPrincipalSeleccionada"));
				if( (suc.equals(SucursalEnum.CENTRO))){
					sucursal.setSelectedIndex(0);
				}else{
					sucursal.setSelectedIndex(1);
				}
				sucursal.setDisabled(true);
				
			}else if((Session.getAttribute("sucursalPrincipalSeleccionada")) instanceof String ){
//				String suc=((String)Session.getAttribute("sucursalPrincipalSeleccionada"));
				sucursal.setSelectedIndex(2);
			}
		}

		row3.appendChild(sucursal);

		String estadoLabel =  I18N.getLabel("gastos.anio");
		row3.appendChild(new Label(estadoLabel));
		anio = GastosViewHelper.getComboBoxAnio();
		anio.setConstraint("strict");
		anio.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
				delegate.actualizarLitado();
			}
		});	
		row3.appendChild(anio);

		
		String estadoLabelMes =  I18N.getLabel("gastos.mes");
		row3.appendChild(new Label(estadoLabelMes));
		mes = GastosViewHelper.getComboBoxMes();
		mes.setConstraint("strict");
		mes.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
				delegate.actualizarLitado();
			}
		});	

		row3.appendChild(mes);
		

		this.addRow(row3);

		Row row4 = new Row();

		
		quincena= new Combobox();
		quincena.setConstraint("strict");
//		quincena.setVisible(false);
		quincena.setSelectedItem(null);				
		quincena.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
				delegate.actualizarLitado();
			}
		});	

		List<Quincena> nombreCursos=claseEJB.findAllConActividadTomaListaDelDiaNombreCurso();
		
		if(nombreCursos != null){
			getComboBoxQuincenas(nombreCursos);						
		}
		row4.appendChild(new Label("Quincena"));
		row4.appendChild(quincena);
		
//		String tipoGasto =  I18N.getLabel("gastos.tipogasto");
//		row4.appendChild(new Label(tipoGasto));
//		tipoGastoCB = new Combobox();
//		tipoGastoCB.setConstraint("strict");
//		tipoGastoCB.addEventListener(Events.ON_CHANGE, new EventListener() {
//			public void onEvent(Event evt){
//				
//				if(tipoGastoCB.getSelectedItem() != null &&
//						(( tipoGastoCB.getSelectedItem().getValue() instanceof  GastosEnum && 
//								(((GastosEnum)tipoGastoCB.getSelectedItem().getValue())).toInt() == GastosEnum.SUELDOS.toInt())
//							|| 
//							( tipoGastoCB.getSelectedItem().getValue() instanceof  GastosMaipuEnum && 
//									(((GastosMaipuEnum)tipoGastoCB.getSelectedItem().getValue())).toInt() == GastosMaipuEnum.SUELDOS.toInt()))){
//					empleadoCB.setVisible(true);
//					empleadoCB.setSelectedItem(null);
//				}else{
//					empleadoCB.setVisible(false);
//					empleadoCB.setSelectedItem(null);
//				}
//				delegate.actualizarLitado();
//			}
//		});	
//
//		row4.appendChild(tipoGastoCB);
//		
		empleadoCB= new Combobox();
		empleadoCB= GastosViewHelper.getComboBoxEmpleados(empleadoCB);
		empleadoCB.setVisible(true);
		empleadoCB.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
				delegate.actualizarLitado();
			}
		});	

		row4.appendChild(new RequiredLabel("Empleado"));
		row4.appendChild(empleadoCB);
		
		if(sucursal.getSelectedItem() != null 
				&& sucursal.getSelectedItem().getValue() instanceof SucursalEnum
				&& ((SucursalEnum)sucursal.getSelectedItem().getValue()).toInt() == SucursalEnum.CENTRO.toInt()){
			anio.setVisible(true);
			mes.setVisible(true);
//			tipoGastoCB.setVisible(true);
//			
//			tipoGastoCB.getItems().clear();
//			tipoGastoCB = GastosViewHelper.getComboBoxTipoGasto(tipoGastoCB, true);
			quincena.setVisible(false);
		}else if(sucursal.getSelectedItem() != null 
				&& sucursal.getSelectedItem().getValue() instanceof SucursalEnum
				&& ((SucursalEnum)sucursal.getSelectedItem().getValue()).toInt() ==	SucursalEnum.MAIPU.toInt()){
//			tipoGastoCB.getItems().clear();
//			tipoGastoCB = GastosViewHelper.getComboBoxTipoGastoMaipu(tipoGastoCB, true);
			quincena.setVisible(true);
			mes.setVisible(false);
			anio.setVisible(true);
//			tipoGastoCB.setVisible(true);
			
		}else{
//			tipoGastoCB.getItems().clear();
//			tipoGastoCB.setVisible(false);
			mes.setVisible(false);
			quincena.setVisible(false);
			
		}
		this.addRow(row4);
	}
	
	
	public void actualizarTodoTemaSucursales(){
		
		if(Session.getAttribute("sucursalPrincipalSeleccionada") != null){
			
			if((Session.getAttribute("sucursalPrincipalSeleccionada")) instanceof SucursalEnum ){
				SucursalEnum suc=((SucursalEnum)Session.getAttribute("sucursalPrincipalSeleccionada"));
				if( (suc.equals(SucursalEnum.CENTRO))){
					sucursal.setSelectedIndex(0);
				}else{
					sucursal.setSelectedIndex(1);
				}
				sucursal.setDisabled(true);
				
			}else if((Session.getAttribute("sucursalPrincipalSeleccionada")) instanceof String ){
//				String suc=((String)Session.getAttribute("sucursalPrincipalSeleccionada"));
				sucursal.setSelectedIndex(2);
			}
		}

		if(sucursal.getSelectedItem() != null 
				&& sucursal.getSelectedItem().getValue() instanceof SucursalEnum
				&& ((SucursalEnum)sucursal.getSelectedItem().getValue()).toInt() == SucursalEnum.CENTRO.toInt()){
			anio.setVisible(true);
			mes.setVisible(true);
//			tipoGastoCB.setVisible(true);
//			
//			tipoGastoCB.getItems().clear();
//			tipoGastoCB = GastosViewHelper.getComboBoxTipoGasto(tipoGastoCB, true);
			quincena.setVisible(false);
		}else if(sucursal.getSelectedItem() != null 
				&& sucursal.getSelectedItem().getValue() instanceof SucursalEnum
				&& ((SucursalEnum)sucursal.getSelectedItem().getValue()).toInt() ==	SucursalEnum.MAIPU.toInt()){
//			tipoGastoCB.getItems().clear();
//			tipoGastoCB = GastosViewHelper.getComboBoxTipoGastoMaipu(tipoGastoCB, true);
			quincena.setVisible(true);
			mes.setVisible(false);
			anio.setVisible(true);
//			tipoGastoCB.setVisible(true);
			
		}else{
//			tipoGastoCB.getItems().clear();
//			tipoGastoCB.setVisible(false);
			mes.setVisible(true);
			quincena.setVisible(true);
			
		}
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
	public CriteriaClause getCriteriaFilters(){
		CriteriaClause criteria = new CriteriaClause();

		super.setPredicate(criteria, mes, "nombre");
		
		return criteria;
	}

	
	public boolean validateHaveFiltersAmbos(){
		
			if (anio.getSelectedIndex() >= 0) {
				return true;
			}
		return false;
	}
	
	public boolean validateHaveFilters(){
	
		if (sucursal.getSelectedItem() != null && sucursal.getSelectedItem().getValue() instanceof SucursalEnum) {
			if(sucursal.getSelectedItem() != null && ((SucursalEnum)sucursal.getSelectedItem().getValue()).toInt() == 
					SucursalEnum.CENTRO.toInt()){
				
				if (anio.getSelectedIndex() >= 0) {
					return true;
				}

				if (mes.getSelectedIndex() >= 0) {
					return true;		}

//				if (tipoGastoCB.getSelectedIndex() >= 0) {
//					return true;		
//				}	
				

				if (empleadoCB.getSelectedIndex() >= 0) {
					return true;		
				}	
				
				
			}
		}
		return false;
	}
	
	
	
	public boolean validateHaveFiltersMaipu(){
		
		if (sucursal.getSelectedItem() != null && sucursal.getSelectedItem().getValue() instanceof SucursalEnum) {

			if(sucursal.getSelectedItem() != null && ((SucursalEnum)sucursal.getSelectedItem().getValue()).toInt() ==SucursalEnum.MAIPU.toInt()){
				if (anio.getSelectedIndex() >= 0) {
					return true;
				}

//				if (tipoGastoCB.getSelectedIndex() >= 0) {
//					return true;		
//				}
				
				if (quincena.getSelectedIndex() >= 0) {
					return true;		
				}
				if (empleadoCB.getSelectedIndex() >= 0) {
					return true;		
				}	

			}
		}
		return false;
	}

	public String getFilters(){
		StringBuilder actionConditions= new StringBuilder(" ");
		if(sucursal.getSelectedItem() != null 
				 && sucursal.getSelectedItem().getValue() instanceof SucursalEnum
				 && ((SucursalEnum)sucursal.getSelectedItem().getValue()).toInt() ==	SucursalEnum.CENTRO.toInt()){
			actionConditions.append("select gas.id  from gastos gas   ");
			actionConditions.append("where 1=1  ");
		
			if (anio.getSelectedIndex() >= 0) {
				Long stateType= ((Long)anio.getSelectedItem().getValue());
				actionConditions.append(" and gas.anio= "+stateType);
			}
			
			if (mes.getSelectedIndex() >= 0) {
				Long stateType= ((Long)mes.getSelectedItem().getValue());
				actionConditions.append(" and gas.mes= "+stateType);
			}
			
			actionConditions.append(" and gas.tipogasto= '"+GastosEnum.SUELDOS.toInt()+"' ");	

//			if (tipoGastoCB.getSelectedIndex() >= 0) {
//				if(tipoGastoCB.getSelectedItem().getValue() instanceof GastosEnum){
//					GastosEnum stateType= ((GastosEnum)tipoGastoCB.getSelectedItem().getValue());
//					actionConditions.append(" and gas.tipogasto= '"+stateType.toInt()+"' ");	
//				}
//			}
			
			if (empleadoCB.getSelectedIndex() >= 0) {
				if(empleadoCB.getSelectedItem().getValue() instanceof Empleado){
					Empleado stateType= ((Empleado)empleadoCB.getSelectedItem().getValue());
					actionConditions.append(" and gas.idempleado= '"+stateType.getId()+"' ");	
				}
			}
			actionConditions.append(" order by gas.anio, gas.mes desc ,gas.id desc, gas.tipogasto ");	
			
		}else if(sucursal.getSelectedItem() != null 
				 && sucursal.getSelectedItem().getValue() instanceof SucursalEnum
				 && ((SucursalEnum)sucursal.getSelectedItem().getValue()).toInt() ==SucursalEnum.MAIPU.toInt()){
			
			actionConditions.append("select gas.id  from gastosmaipu gas   ");
			actionConditions.append("where 1=1  ");
		
			if (anio.getSelectedIndex() >= 0) {
				Long stateType= ((Long)anio.getSelectedItem().getValue());
				actionConditions.append(" and gas.anio= "+stateType);
			}
			
			if (quincena.getSelectedIndex() >= 0) {
				Quincena stateType= ((Quincena)quincena.getSelectedItem().getValue());
				actionConditions.append(" and gas.quincena= "+stateType.getId());
			}
			
			actionConditions.append(" and gas.tipogasto= '"+GastosMaipuEnum.SUELDOS.toInt()+"' ");
//			if (tipoGastoCB.getSelectedIndex() >= 0) {
//				if(tipoGastoCB.getSelectedItem().getValue() instanceof GastosMaipuEnum){
//					GastosMaipuEnum stateType= ((GastosMaipuEnum)tipoGastoCB.getSelectedItem().getValue());
//					actionConditions.append(" and gas.tipogasto= '"+stateType.toInt()+"' ");	
//				}
//			}
			if (empleadoCB.getSelectedIndex() >= 0) {
				if(empleadoCB.getSelectedItem().getValue() instanceof Empleado){
					Empleado stateType= ((Empleado)empleadoCB.getSelectedItem().getValue());
					actionConditions.append(" and gas.idempleado= '"+stateType.getId()+"' ");	
				}
			}

			actionConditions.append(" order by gas.anio, gas.quincena desc  ,gas.id desc, gas.tipogasto ");
			
			
		}else{
//			// es null o es todas.
//			actionConditions.append("select gas.id  from gastos gas   ");
//			actionConditions.append("where 1=1  ");
//		
//			if (anio.getSelectedIndex() >= 0) {
//				Long stateType= ((Long)anio.getSelectedItem().getValue());
//				actionConditions.append(" and gas.anio= "+stateType);
//			}
//			actionConditions.append(" order by gas.anio, gas.mes desc , gas.tipogasto ");	
//			
//			actionConditions.append("	union all  ");
//
//			actionConditions.append("select gas.id  from gastosmaipu gas   ");
//			actionConditions.append("where 1=1  ");
//		
//			if (anio.getSelectedIndex() >= 0) {
//				Long stateType= ((Long)anio.getSelectedItem().getValue());
//				actionConditions.append(" and gas.anio= "+stateType);
//			}
//			actionConditions.append(" order by gas.anio, gas.quincena desc , gas.tipogasto ");
			
		}
			
		return actionConditions.toString();
	}
	
	
	public String getFiltersTODASMaipu(boolean mostrarQuincena){
		StringBuilder actionConditions= new StringBuilder(" ");
	
		actionConditions.append("select gas.id  from gastosmaipu gas   ");
			actionConditions.append("where 1=1  ");
		
			if (anio.getSelectedIndex() >= 0) {
				Long stateType= ((Long)anio.getSelectedItem().getValue());
				actionConditions.append(" and gas.anio= "+stateType);
			}
			if(!mostrarQuincena){
				Calendar cal= Calendar.getInstance();
				int month=cal.get(Calendar.MONTH);

				// si el numero de mes es dic muestro quincenas:
				if(month == 11){
					actionConditions.append(" and gas.quincena in (21,22)");
				}
				// si el numero de mes es enero muestro quincenas:
				if(month == 0){
					actionConditions.append(" and gas.quincena in (23,24)");
				}
				
				// si el numero de mes es feb muestro quincenas:
				if(month == 1){
					actionConditions.append(" and gas.quincena in (25,26)");
				}

			}else{
				if (quincena.getSelectedIndex() >= 0) {
					Quincena stateType= ((Quincena)quincena.getSelectedItem().getValue());
					actionConditions.append(" and gas.quincena= "+stateType.getId());
				}
			}
			
			if (empleadoCB.getSelectedIndex() >= 0) {
				if(empleadoCB.getSelectedItem().getValue() instanceof Empleado){
					Empleado stateType= ((Empleado)empleadoCB.getSelectedItem().getValue());
					actionConditions.append(" and gas.idempleado= '"+stateType.getId()+"' ");	
				}
			}

			actionConditions.append(" and gas.tipogasto= '"+GastosMaipuEnum.SUELDOS.toInt()+"' ");

			actionConditions.append(" order by gas.anio, gas.quincena desc  ,gas.id desc, gas.tipogasto ");
		return actionConditions.toString();
	}
	
	public String getFiltersTODASCentro(){
		StringBuilder actionConditions= new StringBuilder(" ");
	
		actionConditions.append("select gas.id  from gastos gas   ");
			actionConditions.append("where 1=1  ");
		
			if (anio.getSelectedIndex() >= 0) {
				Long stateType= ((Long)anio.getSelectedItem().getValue());
				actionConditions.append(" and gas.anio= "+stateType);
			}
			if (mes.getSelectedIndex() >= 0) {
				Long stateType= ((Long)mes.getSelectedItem().getValue());
				actionConditions.append(" and gas.mes= "+stateType);
			}

			actionConditions.append(" and gas.tipogasto= '"+GastosEnum.SUELDOS.toInt()+"' ");
			if (empleadoCB.getSelectedIndex() >= 0) {
				if(empleadoCB.getSelectedItem().getValue() instanceof Empleado){
					Empleado stateType= ((Empleado)empleadoCB.getSelectedItem().getValue());
					actionConditions.append(" and gas.idempleado= '"+stateType.getId()+"' ");	
				}
			}

			actionConditions.append(" order by gas.anio, gas.mes desc  ,gas.id desc,  gas.tipogasto ");	
		return actionConditions.toString();
	}
	
	public void clear(){
		Constraint c;
	
		c= sucursal.getConstraint();
		sucursal.setConstraint("");
		sucursal.setSelectedItem(null);
		sucursal.setConstraint(c);

		c= anio.getConstraint();
		anio.setConstraint("");
		anio.setSelectedItem(null);
		anio.setConstraint(c);
		
		c= mes.getConstraint();
		mes.setConstraint("");
		mes.setSelectedItem(null);
		mes.setConstraint(c);
		
//		c= tipoGastoCB.getConstraint();
//		tipoGastoCB.setConstraint("");
//		tipoGastoCB.setSelectedItem(null);
//		tipoGastoCB.setConstraint(c);
		
		c= quincena.getConstraint();
		quincena.setConstraint("");
		quincena.setSelectedItem(null);
		quincena.setConstraint(c);

//		c= empleadoCB.getConstraint();
//		empleadoCB.setConstraint("");
//		empleadoCB.setSelectedItem(null);
//		empleadoCB.setConstraint(c);

	}

	public Combobox getAnio() {
		return anio;
	}

	public void setAnio(Combobox anio) {
		this.anio = anio;
	}

	public Combobox getMes() {
		return mes;
	}

	public void setMes(Combobox mes) {
		this.mes = mes;
	}

//	public Combobox getTipoGastoCB() {
//		return tipoGastoCB;
//	}
//
//	public void setTipoGastoCB(Combobox tipoGastoCB) {
//		this.tipoGastoCB = tipoGastoCB;
//	}

	public Combobox getSucursal() {
		return sucursal;
	}

	public void setSucursal(Combobox sucursal) {
		this.sucursal = sucursal;
	}

	public Combobox getQuincena() {
		return quincena;
	}

	public void setQuincena(Combobox quincena) {
		this.quincena = quincena;
	}

	public SucursalDelegate getDelegate() {
		return delegate;
	}

	public void setDelegate(SucursalDelegate delegate) {
		this.delegate = delegate;
	}

}