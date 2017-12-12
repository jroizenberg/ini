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
import com.institucion.desktop.delegated.IngresosEgresosDelegate;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.validator.TextConstraint;
import com.institucion.fm.desktop.view.GridCrud;
import com.institucion.fm.desktop.view.RequiredLabel;

public class IngresosEgresosCrud extends GridCrud{

	private static final long serialVersionUID = 1L;
	private Combobox tipoMovimiento;
	private Intbox saldo;
	private Textbox comentario;
    private IngresosEgresosDelegate delegate;
//	private Combobox sucursal;
	private Combobox quincena;
	private ClaseEJB claseEJB;
	private Label quincenalabel;
	public IngresosEgresosCrud (){
		super();
		claseEJB = BeanFactory.<ClaseEJB>getObject("fmEjbClase");

		this.makeFields();
		
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
	
	private void makeFields(){
	
//		sucursal= new Combobox();
//		sucursal.setConstraint("strict");
//		sucursal= SucursalViewHelper.getComboBox();			
//		
//		sucursal.addEventListener(Events.ON_CHANGE, new EventListener() {
//			public void onEvent(Event evt){
//				if(sucursal.getSelectedItem() != null && ((SucursalEnum)sucursal.getSelectedItem().getValue()).toInt() == 
//						SucursalEnum.CENTRO.toInt()){
//					quincena.setVisible(false);
//					quincena.setSelectedItem(null);				
//				}else{
//					quincena.setVisible(true);
//				}
//			}
//		});	
//
//		this.addFieldUnique(new RequiredLabel("Sucursal"), sucursal, new Label(" "), new Label(" "));

		
		tipoMovimiento= new Combobox();
		tipoMovimiento.setConstraint("strict");
		setTipoMovimiento(getComboBoxTipoMovimientos( tipoMovimiento));						
		
		saldo= new Intbox();
		saldo.setStyle("color: red ; font-weight: bold ; ");
		saldo.setConstraint("/(([0-9]+)?)+/"); //, /(([0-9]+)?)+/"); //"/([0-9])/"); //   ([0-9]+)?)+/");
		saldo.addEventListener(Events.ON_CHANGE, new EventListener(){
			public void onEvent(Event evt){
				
				if(((InputEvent)evt).getValue() != null){
					if(((InputEvent)evt).getValue().contains(",")){
						String s=((InputEvent)evt).getValue();
						s=s.substring(0, s.indexOf(","));
						saldo.setValue(Integer.parseInt(s));
						saldo.setText(s);
						((Intbox)evt.getTarget()).setValue(Integer.parseInt(s));
					}
				}
				
				delegate.actualizarLista(); 
			}
		});
		
		this.addFieldUnique(new RequiredLabel(I18N.getLabel("ingresos.egresos.tipo.movimiento")), tipoMovimiento, 
				new RequiredLabel(I18N.getLabel("ingresos.egresos.saldo")), saldo);		
		
		quincena= new Combobox();
		quincena.setConstraint("strict");
		quincena.setVisible(false);
		quincena.setSelectedItem(null);				

		List<Quincena> nombreCursos=claseEJB.findAllConActividadTomaListaDelDiaNombreCurso();
		
		if(nombreCursos != null){
			getComboBoxQuincenas(nombreCursos);						
		}
		
		comentario= new Textbox();
		comentario.setRows(3);
		comentario.setMaxlength(150);
		comentario.setWidth("auto");
		comentario.setConstraint(new TextConstraint());
			
		quincenalabel=new RequiredLabel("Quincena");
		quincenalabel.setVisible(false);
		this.addFieldUnique(quincenalabel, quincena, new RequiredLabel(I18N.getLabel("ingresos.egresos.comentario")), comentario);

//		if(Session.getAttribute("sucursal") != null){
//			if(((SucursalEnum)Session.getAttribute("sucursal")).toInt() == SucursalEnum.CENTRO.toInt() ){
//				sucursal.setSelectedIndex(0);
//				quincena.setVisible(false);
//				quincena.setSelectedItem(null);				
//
//			}else if(((SucursalEnum)Session.getAttribute("sucursal")).toInt() == SucursalEnum.MAIPU.toInt() ){
//				sucursal.setSelectedIndex(1);	
//				quincena.setVisible(true);
//			}
//		}
	}

	public Combobox getComboBoxTipoMovimientos(Combobox act) {
		Constraint brandC = act.getConstraint();
		act.setConstraint("");
		act.setText("");
		act.setConstraint(brandC);
		act.getItems().clear();
	
//		Comboitem item2;
//		item2 = new Comboitem("Todos");
//		item2.setValue(TipoMovimientoCajaEnum.TODOS);
//		act.setSelectedItem(item2);
//		act.appendChild(item2);	
//		act.setSelectedItem(item2);
		Comboitem item;
		item = new Comboitem("Ingresos");
		item.setValue(TipoMovimientoCajaEnum.INGRESO);
		act.appendChild(item);	

		item = new Comboitem("Egresos");
		item.setValue(TipoMovimientoCajaEnum.EGRESO);
		act.appendChild(item);	

//		act.setSelectedItem(item2);
		return act;
	}
	public Intbox getSaldo() {
		return saldo;
	}

	public void setSaldo(Intbox saldo) {
		this.saldo = saldo;
	}

	public Combobox getTipoMovimiento() {
		return tipoMovimiento;
	}

	public void setTipoMovimiento(Combobox tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}

	public Textbox getComentario() {
		return comentario;
	}

	public void setComentario(Textbox comentario) {
		this.comentario = comentario;
	}

	public IngresosEgresosDelegate getDelegate() {
		return delegate;
	}

//	public Combobox getSucursal() {
//		return sucursal;
//	}
//
//	public void setSucursal(Combobox sucursal) {
//		this.sucursal = sucursal;
//	}

	public Combobox getQuincena() {
		return quincena;
	}

	public void setQuincena(Combobox quincena) {
		this.quincena = quincena;
	}

	public void setDelegate(IngresosEgresosDelegate delegate) {
		this.delegate = delegate;
	}

	public Label getQuincenalabel() {
		return quincenalabel;
	}

	public void setQuincenalabel(Label quincenalabel) {
		this.quincenalabel = quincenalabel;
	}
}
