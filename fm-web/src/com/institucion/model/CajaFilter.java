package com.institucion.model;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;

import com.institucion.bz.BancoPromocionEJB;
import com.institucion.bz.CursoEJB;
import com.institucion.desktop.delegated.CajaDelegate;
import com.institucion.desktop.helper.SucursalViewHelper;
import com.institucion.fm.conf.Session;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridFilter;
import com.institucion.fm.desktop.view.RequiredLabel;
import com.institucion.fm.filteradv.model.CriteriaClause;
import com.institucion.fm.security.bz.SecurityAAEJB;

public class CajaFilter extends GridFilter {
	private static final long serialVersionUID = 1L;
	DecimalFormat formateador = new DecimalFormat("###,###");

	private Combobox usuarios;
	private Checkbox obtenerMovimientosSoloOS;
	private Combobox curso;
	private Combobox tipoMovimiento;
	private Combobox promocionBanco;
	private Datebox fechaDesde;
	private Datebox fechaHasta;
	private SecurityAAEJB securityService;
	private Label ultimoEstadoCajaInicial; 
	private Combobox sucursal;

	private CursoEJB cursoEJB;
	private BancoPromocionEJB bancoPromocionEJB;

	private CajaDelegate actionComposerDelegate;	

	
	public CajaFilter()	{
		super();
		cursoEJB = BeanFactory.<CursoEJB>getObject("fmEjbCurso");
		bancoPromocionEJB = BeanFactory.<BancoPromocionEJB>getObject("fmEjbBancoPromocion");
		securityService= BeanFactory.<SecurityAAEJB>getObject("fm.ejb.securityAAService");
		buildFilter();
	}
	
	private void buildFilter() {
		Row row1 = new Row();	
		
		row1.appendChild(new RequiredLabel("Usuarios"));
		usuarios= new Combobox();
		usuarios.setConstraint("strict");
		usuarios.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
				if(usuarios.getSelectedItem() != null){
					actionComposerDelegate.onfindFromChangeSucursal(false);
				}
			}
		});	
		
		row1.appendChild(usuarios);
		
		row1.appendChild(new RequiredLabel("Obtener Movimientos de Obras Sociales"));
		obtenerMovimientosSoloOS= new Checkbox();
		obtenerMovimientosSoloOS.setChecked(false);
		obtenerMovimientosSoloOS.addEventListener(Events.ON_CHECK, new EventListener() {
			public void onEvent(Event evt) {
				
				if(obtenerMovimientosSoloOS.isChecked()){
					tipoMovimiento.setSelectedItem(tipoMovimiento.getItemAtIndex(0));
					tipoMovimiento.setDisabled(true);
					promocionBanco.setSelectedItem(promocionBanco.getItemAtIndex(0));
					promocionBanco.setDisabled(true);
					actionComposerDelegate.pongoVisibleCrudObrasSociales(true);
					
				}else{
					tipoMovimiento.setDisabled(false);
					promocionBanco.setDisabled(false);
					actionComposerDelegate.pongoVisibleCrudObrasSociales(false);
				}
				actionComposerDelegate.onfindFromChangeSucursal(false);
			}
		});	
		row1.appendChild(obtenerMovimientosSoloOS);
	
		this.addRow(row1);
		
		List<com.institucion.fm.security.model.User> userList=securityService.getUsers();
		if(userList != null){
			setUsuarios(getComboBoxUsuarios(userList, usuarios));						
		}
		
		Row row4 = new Row();
		String cursoLabel =  I18N.getLabel("client.curso.actividad");
		row4.appendChild(new RequiredLabel(cursoLabel));
		
		curso= new Combobox();
		curso.setConstraint("strict");
		row4.appendChild(curso);
		curso.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
				if(curso.getSelectedItem() != null){
					actionComposerDelegate.onfindFromChangeSucursal(false);
				}
			}
		});	

		List<Actividad> actividades=cursoEJB.findAllActividad();
		if(actividades != null){
			setCurso(getComboBoxActividad(actividades, curso));						
		}
		
		String tipoMov =  I18N.getLabel("caja.tipo.movimiento");
		row4.appendChild(new RequiredLabel(tipoMov));
		
		tipoMovimiento= new Combobox();
		tipoMovimiento.setConstraint("strict");
		tipoMovimiento.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
				if(tipoMovimiento.getSelectedItem() != null){
					actionComposerDelegate.onfindFromChangeSucursal(false);
				}
			}
		});	

		row4.appendChild(tipoMovimiento);
		setTipoMovimiento(getComboBoxTipoMovimientos( tipoMovimiento));						
		
		this.addRow(row4);

		Row row5 = new Row();
		row5.appendChild(new RequiredLabel("Promociones Bancos"));
		promocionBanco= new Combobox();
		promocionBanco.setConstraint("strict");
		promocionBanco.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
				if(promocionBanco.getSelectedItem() != null){
					actionComposerDelegate.onfindFromChangeSucursal(false);
				}
			}
		});	

		row5.appendChild(promocionBanco);
		
		List<BancoPromocion> proms=bancoPromocionEJB.findAll();
		if(proms != null){
			setPromocionBanco(getComboBoxBancoPromociones(proms, promocionBanco));						
		}
		row5.appendChild(new Label(" "));
		row5.appendChild(new Label(" "));
		this.addRow(row5);
		
		Row row3 = new Row();

		String edadLabel =  I18N.getLabel("caja.fechaD");
		row3.appendChild(new RequiredLabel(edadLabel));
		fechaDesde  = new Datebox();
		fechaDesde .setMaxlength(20);
		fechaDesde.setId("sasas");
		fechaDesde .setFormat(I18N.getDateFormat());
		fechaDesde.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
				if(fechaDesde.getValue() != null){
					actionComposerDelegate.onfindFromChangeSucursal(false);
				}
			}
		});	
		row3.appendChild(fechaDesde);

		String edadLabel2 =  I18N.getLabel("caja.fechaH");
		row3.appendChild(new RequiredLabel(edadLabel2));
		fechaHasta  = new Datebox();
		fechaHasta .setMaxlength(20);
		fechaHasta .setFormat(I18N.getDateFormat());
		fechaHasta.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
				if(fechaHasta.getValue() != null){
					actionComposerDelegate.onfindFromChangeSucursal(false);
				}
			}
		});	

		row3.appendChild(fechaHasta);
		
		
		this.addRow(row3);
		
		Row row11 = new Row();
		Label aaa=new Label(" dsd ");
		aaa.setVisible(false);
		
		row11.appendChild(aaa);
		row11.appendChild(aaa);
		row11.appendChild(aaa);
		row11.appendChild(aaa);
		this.addRow(row11);

		Row row12 = new Row();
//		Label aaa=new Label(" dsd ");
		aaa.setVisible(false);
		
		row12.appendChild(aaa);
		row12.appendChild(aaa);
		row12.appendChild(aaa);
		row12.appendChild(aaa);
		this.addRow(row12);

		
		Row row10 = new Row();
//		String edadLabelwww =  I18N.getLabel("caja.ultimo.estado");
		row10.appendChild(new RequiredLabel("Sucursal caja"));
		sucursal= SucursalViewHelper.getComboBoxConOpcionTodos();
		
		
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

		if(sucursal.getSelectedItem() == null){
			sucursal.setSelectedIndex(0);
		}
		
		sucursal.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
				if(sucursal.getSelectedItem() != null){
					actionComposerDelegate.onfindFromChangeSucursal(false);
				}
			}
		});	
		
		sucursal.setConstraint("strict");
		row10.appendChild(sucursal);

		String edadLabelwww =  I18N.getLabel("caja.ultimo.estado");
		row10.appendChild(new RequiredLabel(edadLabelwww));
		ultimoEstadoCajaInicial= new RequiredLabel("$"+formateador.format(0) + " (Ingresados por:  )");
		row10.appendChild(ultimoEstadoCajaInicial);

//		row10.appendChild( new Label(""));
//		row10.appendChild( new Label(""));
		this.addRow(row10);

	}

		
	public Combobox getComboBoxTipoMovimientos(Combobox act) {
		Constraint brandC = act.getConstraint();
		act.setConstraint("");
		act.setText("");
		act.setConstraint(brandC);
		act.getItems().clear();
	
		Comboitem item2;
		item2 = new Comboitem("Todos");
		item2.setValue(TipoMovimientoCajaEnum.TODOS);
		act.setSelectedItem(item2);
		act.appendChild(item2);	
		act.setSelectedItem(item2);
		Comboitem item;
		item = new Comboitem("Ingresos");
		item.setValue(TipoMovimientoCajaEnum.INGRESO);
		act.appendChild(item);	

		item = new Comboitem("Egresos");
		item.setValue(TipoMovimientoCajaEnum.EGRESO);
		act.appendChild(item);	

		act.setSelectedItem(item2);
		return act;
	}

	public Combobox getComboBoxBancoPromociones(List<BancoPromocion> listUsuarios, Combobox act) {
		Constraint brandC = act.getConstraint();
		act.setConstraint("");
		act.setText("");
		act.setConstraint(brandC);
		act.getItems().clear();
		
		Comboitem item2;
		item2 = new Comboitem("Todos los Pagos y Promociones");
		item2.setValue("Todos");
		act.setSelectedItem(item2);
		act.appendChild(item2);	
		act.setSelectedItem(item2);
	
		for (BancoPromocion usuario : listUsuarios) {
			Comboitem item;
			item = new Comboitem(usuario.getNombrePromo());
			item.setValue(usuario);
			act.appendChild(item);

		}		
		return act;
	}
	
	public Combobox getComboBoxUsuarios(List<com.institucion.fm.security.model.User> listUsuarios, Combobox act) {
		Constraint brandC = act.getConstraint();
		act.setConstraint("");
		act.setText("");
		act.setConstraint(brandC);
		act.getItems().clear();
		
		Comboitem item2;
		item2 = new Comboitem("Todos");
		item2.setValue("Todos");
		act.setSelectedItem(item2);
		act.appendChild(item2);	
		act.setSelectedItem(item2);
	
		for (com.institucion.fm.security.model.User usuario : listUsuarios) {
			
			if(!usuario.getName().equalsIgnoreCase("admin")){
				Comboitem item;
				item = new Comboitem(usuario.getFirstName() +" "+ usuario.getLastName());
				item.setValue(usuario);
				if(usuario.getName().equalsIgnoreCase(Session.getUsername()))
					act.setSelectedItem(item);
				act.appendChild(item);
				if(usuario.getName().equalsIgnoreCase(Session.getUsername()))
					act.setSelectedItem(item);
			}
		}		
		return act;
	}
	
	public Combobox getComboBoxActividad(List<Actividad> actividades, Combobox act) {
		Constraint brandC = act.getConstraint();
		act.setConstraint("");
		act.setText("");
		act.setConstraint(brandC);
		act.getItems().clear();
		
		Comboitem item2;
		item2 = new Comboitem("Todos");
		item2.setValue("Todos");
		act.setSelectedItem(item2);
		act.appendChild(item2);	
		act.setSelectedItem(item2);
		
		Comboitem item3;
		item3 = new Comboitem("Venta de Productos");
		item3.setValue("Venta de Productos");
		act.setSelectedItem(item3);
		act.appendChild(item3);	
		
		for (Actividad actividad : actividades) {
			Comboitem item;
			item = new Comboitem(actividad.getNombre());
			item.setValue(actividad);
			act.appendChild(item);	
		}

		return act;
	}

	
	public CriteriaClause getCriteriaFilters(){
		CriteriaClause criteria = new CriteriaClause();

		super.setPredicate(criteria, curso, "curso");
		
		return criteria;
	}

	public boolean validateHaveFilters(){
		
		if (usuarios.getSelectedIndex() >= 0) {
			return true;
		}
		
		if (tipoMovimiento.getSelectedIndex() >= 0) {
			return true;
		}

		if (curso.getSelectedIndex() >= 0) {
			return true;		
		}

		if (fechaDesde.getValue()!= null) {
			return true;
		}
		if (fechaHasta.getValue()!= null) {
			return true;
		}
		if(obtenerMovimientosSoloOS.isChecked())
			return true;
		
		if(promocionBanco.getSelectedIndex() >= 0)
			return true;

		return false;
	}
	
	
	public void clear(){
		Constraint c;
						
		c= tipoMovimiento.getConstraint();
		tipoMovimiento.setConstraint("");
		tipoMovimiento.setSelectedItem(null);
		tipoMovimiento.setConstraint(c);
		
		c= usuarios.getConstraint();
		usuarios.setConstraint("");
		usuarios.setSelectedItem(null);
		usuarios.setConstraint(c);
		
		c= curso.getConstraint();
		curso.setConstraint("");
		curso.setSelectedItem(null);
		curso.setConstraint(c);
		
		c= promocionBanco.getConstraint();
		promocionBanco.setConstraint("");
		promocionBanco.setSelectedItem(null);
		promocionBanco.setConstraint(c);
		
		c =fechaDesde.getConstraint();
		fechaDesde.setConstraint("");
		fechaDesde.setText(null);
		fechaDesde.setConstraint(c);
		fechaDesde.setValue(new Date(0,0,0,0,0));
		
		c =fechaHasta.getConstraint();
		fechaHasta.setConstraint("");
		fechaHasta.setText(null);
		fechaHasta.setConstraint(c);
		fechaHasta.setValue(new Date(0,0,0,0,0));

		obtenerMovimientosSoloOS.setChecked(false);
		
	}

	public Combobox getCurso() {
		return curso;
	}

	public void setCurso(Combobox curso) {
		this.curso = curso;
	}

	public Combobox getTipoMovimiento() {
		return tipoMovimiento;
	}

	public void setTipoMovimiento(Combobox tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}

	public Datebox getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Datebox fechaDesde) {
		this.fechaDesde = fechaDesde;
	}


	public Datebox getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Datebox fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	
	public CursoEJB getCursoEJB() {
		return cursoEJB;
	}

	public void setCursoEJB(CursoEJB cursoEJB) {
		this.cursoEJB = cursoEJB;
	}

	public Combobox getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(Combobox usuarios) {
		this.usuarios = usuarios;
	}

	public Checkbox getObtenerMovimientosSoloOS() {
		return obtenerMovimientosSoloOS;
	}

	public void setObtenerMovimientosSoloOS(Checkbox obtenerMovimientosSoloOS) {
		this.obtenerMovimientosSoloOS = obtenerMovimientosSoloOS;
	}

	public Combobox getPromocionBanco() {
		return promocionBanco;
	}

	public void setPromocionBanco(Combobox promocionBanco) {
		this.promocionBanco = promocionBanco;
	}

	public Combobox getSucursal() {
		return sucursal;
	}

	public void setSucursal(Combobox sucursal) {
		this.sucursal = sucursal;
	}

	public Label getUltimoEstadoCajaInicial() {
		return ultimoEstadoCajaInicial;
	}

	public void setUltimoEstadoCajaInicial(Label ultimoEstadoCajaInicial) {
		this.ultimoEstadoCajaInicial = ultimoEstadoCajaInicial;
	}

	public CajaDelegate getActionComposerDelegate() {
		return actionComposerDelegate;
	}

	public void setActionComposerDelegate(CajaDelegate actionComposerDelegate) {
		this.actionComposerDelegate = actionComposerDelegate;
	}

}