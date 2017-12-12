package com.institucion.model;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

import com.institucion.bz.ClaseEJB;
import com.institucion.bz.CursoEJB;
import com.institucion.desktop.delegated.ClienteDelegate;
import com.institucion.desktop.helper.EdadViewHelper;
import com.institucion.desktop.helper.EstadoClienteViewHelper;
import com.institucion.fm.conf.Session;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.validator.TextConstraint;
import com.institucion.fm.desktop.view.GridFilter;
import com.institucion.fm.desktop.view.RequiredLabel;
import com.institucion.fm.filteradv.model.CriteriaClause;

public class ClientFilter extends GridFilter {
	private static final long serialVersionUID = 1L;

	private Textbox nombre;
	private Textbox apellido;
//	private Textbox codBarras;
//	private Textbox dni;
	private Combobox edadDesde;
	private Combobox edadHasta;
	private Combobox mesFechaNac;

	private Combobox curso;   // los que se dan de alta desde la otra pantalla de cursos q estan asociados a 1 tipo Curso
	private Combobox estado;  // Adeuda, C/cupos Disp, S/cupos D.
	private Combobox clases;  // Adeuda, C/cupos Disp, S/cupos D.
//	private Label codbarrasLabel;
	private CursoEJB cursoEJB;
	private ClaseEJB claseEJB;
	private ClienteDelegate actionComposerDelegate;	

	private Datebox fechaVentaD;
	private Datebox fechaVentaH;
	
//	agregar 2 filtros que sean, cumpleanios contratados: ANIO:   MES
	
	public ClientFilter()	{
		super();
		cursoEJB = BeanFactory.<CursoEJB>getObject("fmEjbCurso");
		claseEJB = BeanFactory.<ClaseEJB>getObject("fmEjbClase");
		buildFilter();
	}

	private void buildFilter() {

		Row row1 = new Row();
	
		String apelLabel = I18N.getLabel("client.apellido");
		row1.appendChild(new RequiredLabel(apelLabel));
		apellido = new Textbox();
	
		apellido.setConstraint(new TextConstraint());
		apellido.setWidth("90px");
		apellido.addEventListener(Events.ON_CHANGING, new EventListener() {
			public void onEvent(Event evt){
				apellido.setValue(((InputEvent)evt).getValue());	

				actionComposerDelegate.buscar(evt, false);
			}
		});	

		row1.appendChild(apellido);
		
		String nameLabel = I18N.getLabel("client.nombre");
		row1.appendChild(new RequiredLabel(nameLabel));
		nombre = new Textbox();
		nombre.setConstraint(new TextConstraint());
		nombre.setWidth("90px");	

		nombre.addEventListener(Events.ON_CHANGING, new EventListener() {
			public void onEvent(Event evt){
//				codBarras.setText(null);
				nombre.setValue(((InputEvent)evt).getValue());	
				actionComposerDelegate.buscar(evt, false);

			}
		});	
		row1.appendChild(nombre);

		String edadLabel =  I18N.getLabel("client.edad") + " desde";
		Label aaa= new Label(edadLabel);
		aaa.setWidth("20px");
		row1.appendChild(aaa);
		edadDesde = EdadViewHelper.getComboBox();
		edadDesde.setConstraint("strict");
		edadDesde.setWidth("43px");	
		edadDesde.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
//				codBarras.setText(null);
				actionComposerDelegate.buscar(evt, false);
				}
		});	
		row1.appendChild(edadDesde);


		String edadLabel2 =  I18N.getLabel("client.edad") + " hasta";
		Label aaa2= new Label(edadLabel2);
		aaa2.setWidth("20px");
		row1.appendChild(aaa2);
		edadHasta = EdadViewHelper.getComboBox();
		edadHasta.setConstraint("strict");
		edadHasta.setWidth("43px");	
		edadHasta.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
//				codBarras.setText(null);
				actionComposerDelegate.buscar(evt, false);
				}
		});	
		row1.appendChild(edadHasta);

		this.addRow(row1);

		
		Row row6 = new Row();

		row6.appendChild(new Label("Mes cumpleaños"));
		mesFechaNac = EdadViewHelper.getComboBoxMes();
		mesFechaNac.setConstraint("strict");
		mesFechaNac.setWidth("60px");	
		mesFechaNac.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
//				codBarras.setText(null);
				actionComposerDelegate.buscar(evt, false);
			}
		});	
		row6.appendChild(mesFechaNac);		
		
		
		String cursoLabel =  I18N.getLabel("client.curso.actividad");
		row6.appendChild(new Label(cursoLabel));
		
		curso= new Combobox();
		curso.setConstraint("strict");
		curso.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
//				codBarras.setText(null);
				actionComposerDelegate.buscar(evt, false);
			}
		});	
		row6.appendChild(curso);
		
		List<Actividad> actividades=cursoEJB.findAllActividad();
		if(actividades != null){
			setCurso(getComboBoxActividad(actividades, curso));						
		}
		
		String clasessLabel =  I18N.getLabel("clases.crud.tab222");
		row6.appendChild(new Label(clasessLabel));
		
		clases= new Combobox();
		clases.setConstraint("strict");
		clases.setWidth("90px");	
		clases.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
//				codBarras.setText(null);
				actionComposerDelegate.buscar(evt, false);
			}
		});	
		row6.appendChild(clases);
		

		boolean esMaipu=false;
		boolean esCentro= false;
		if(Session.getAttribute("sucursalPrincipalSeleccionada") != null){
			
			if((Session.getAttribute("sucursalPrincipalSeleccionada")) instanceof SucursalEnum ){
				if( ((SucursalEnum)Session.getAttribute("sucursalPrincipalSeleccionada")).equals(SucursalEnum.MAIPU)){
					esMaipu= true;
				}else if( ((SucursalEnum)Session.getAttribute("sucursalPrincipalSeleccionada")).equals(SucursalEnum.CENTRO)){
					esCentro= true;
				}
			}
		}

		List<Clase> clasesList= null;
		if(!esMaipu){
			clasesList=claseEJB.findAllConActividadTomaListaDelDia(true);	
		}
		
		List<Quincena> nombreCursos= null;
		if(!esCentro){
			nombreCursos=claseEJB.findAllConActividadTomaListaDelDiaNombreCurso();	
		}	
		if(clasesList != null ||  nombreCursos != null){
			setClases(getComboBoxClases(clasesList, nombreCursos,  clases));						
		}
		
		String estadoLabel =  I18N.getLabel("client.estado");
		row6.appendChild(new Label(estadoLabel));
		estado = EstadoClienteViewHelper.getComboBox();
//		estado.setId("estadoxx");
		estado.setWidth("60px");	
		estado.setConstraint("strict");
		estado.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
//				codBarras.setText(null);
				actionComposerDelegate.buscar(evt, false);
			}
		});	
		row6.appendChild(estado);
	
		
		this.addRow(row6);
		
		Row row3 = new Row();
								
		Label fechaDLabel= new Label("Fecha ventas desde");
		row3.appendChild(fechaDLabel);
		this.fechaVentaD = new Datebox();
		this.fechaVentaD.setWidth("70px");
		fechaVentaD.setFormat(I18N.getDateFormat());
		fechaVentaD.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
//				codBarras.setText(null);
				actionComposerDelegate.buscar(evt, false);
			}
		});	

		row3.appendChild(fechaVentaD);

		
		Label fechaHLabel= new Label("Fecha ventas hasta");
		row3.appendChild(fechaHLabel);
		this.fechaVentaH = new Datebox();
		this.fechaVentaH.setWidth("70px");
		fechaVentaH.setFormat(I18N.getDateFormat());
		fechaVentaH.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
//				codBarras.setText(null);
				actionComposerDelegate.buscar(evt, false);
			}
		});	

		row3.appendChild(fechaVentaH);
		
		row3.appendChild(new Label(""));
		row3.appendChild(new Label(""));

		
		row3.appendChild(new Label(""));
		row3.appendChild(new Label(""));
		this.addRow(row3);
	}

	public static Combobox getComboBoxActividad(List<Actividad> actividades, Combobox act) {
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


	public static Combobox getComboBoxClases(List<Clase> clasesCB,List<Quincena> cursos,  Combobox act) {
		Constraint brandC = act.getConstraint();
		act.setConstraint("");
		act.setText("");
		act.setConstraint(brandC);
		act.getItems().clear();
		if(clasesCB != null){
			for (Clase classe : clasesCB) {
				
				Comboitem item;
				item = new Comboitem(classe.getNombre());
				item.setValue(classe);
				act.appendChild(item);	
			}			
		}
		
		Calendar calendarDate = Calendar.getInstance();  
		calendarDate.setTime(new Date());  
		int mess=calendarDate.get(Calendar.MONTH);
		int anioQueQueda= calendarDate.get(Calendar.YEAR);
		
		if(mess >= 4){
			anioQueQueda=anioQueQueda +1;
		}
		
		if(cursos != null){
			for (Quincena classe : cursos) {
				Comboitem item;
				item = new Comboitem("MAIPU: Temporada "+anioQueQueda+" - "+classe.getNombre());
				item.setValue(classe);
				act.appendChild(item);	
			}	
		}
		
		return act;
	}
	public CriteriaClause getCriteriaFilters(){
		CriteriaClause criteria = new CriteriaClause();

		super.setPredicate(criteria, nombre, "nombre");
		
		return criteria;
	}
	
	

	
	public String getFiltersForExportingExcelClientes(){

		StringBuilder actionConditions= new StringBuilder("select id, apellido, nombre, dni, fechanacimiento, " +
			"domicilio, celular, telefonofijo, facebook, mail, null as fechayhoracreacion, null as idusuariocreosubscripcion from cliente clientexx ");
					actionConditions.append("where 1=1  ");
			
						if (nombre.getValue()!= null && !nombre.getValue().trim().equals("") && !nombre.getValue().trim().equals(" ")) {
							actionConditions.append(" and  Upper(clientexx.nombre) like Upper('%"+nombre.getValue().trim()+"%') ");
						}
						
						if (apellido.getValue()!= null && !apellido.getValue().trim().equals("") && !apellido.getValue().trim().equals(" ")) {
							actionConditions.append(" and  Upper(clientexx.apellido) like Upper('%"+apellido.getValue().trim()+"%') ");
						}
								
						if (estado.getSelectedIndex() >= 0) {
							ClienteEstadoEnum stateType= ((ClienteEstadoEnum)estado.getSelectedItem().getValue());

							if(stateType.equals(ClienteEstadoEnum.ADEUDA)){
								actionConditions.append(" and clientexx.id in ( ");
								actionConditions.append(" select distinct(c.id) from cliente c inner join subscripcion s on (c.id= s.idcliente) "); 
								actionConditions.append(" inner join cupoactividad ca on (ca.idsubscripcion= s.id) where ca.estado in (0, 1, 10) ");

							}else	if(stateType.equals(ClienteEstadoEnum.C_CLASES_DISP)){
								actionConditions.append(" and 0 < ( select count(*) from cliente c inner join subscripcion s on (c.id= s.idcliente) "); 
								actionConditions.append(" inner join cupoactividad ca on (ca.idsubscripcion= s.id) where ca.estado in (3, 10) ");

							}else	if(stateType.equals(ClienteEstadoEnum.S_CLASES_DISP)){
								actionConditions.append(" and 0 = ( select count(*) from cliente c inner join subscripcion s on (c.id= s.idcliente) "); 
								actionConditions.append(" inner join cupoactividad ca on (ca.idsubscripcion= s.id) where ca.estado in (3, 10) ");
							}
							actionConditions.append(" and c.id=clientexx.id )");
						}

						if (curso.getSelectedIndex() >= 0) {
							Actividad stateType= ((Actividad)curso.getSelectedItem().getValue());
							actionConditions.append(" and "+ stateType.getId()  +" in " +
									"	(select con.idactividad from concepto con where con.idsubscripcion  in (select subs.id from subscripcion subs where subs.idcliente=clientexx.id )) ");
						}
			   
						if (edadDesde.getSelectedIndex() >= 0) {
							String stateType= ((String)edadDesde.getSelectedItem().getValue());
							if(stateType != null){
								actionConditions.append("  and date_part('year',  age(now(),clientexx.fechanacimiento)) >="+ stateType); 
							}
						}
						if (edadHasta.getSelectedIndex() >= 0) {
							String stateType= ((String)edadHasta.getSelectedItem().getValue());
							if(stateType != null){
								actionConditions.append("  and date_part('year',  age(now(),clientexx.fechanacimiento)) <="+ stateType); 
							}
						}
						if (mesFechaNac.getSelectedIndex() >= 0) {
							String stateType= ((String)mesFechaNac.getSelectedItem().getValue());
							if(stateType != null){
								actionConditions.append("	and to_char(fechanacimiento, 'MM') =  '"+stateType+"'  ");											
							}
						}
						
						if (fechaVentaD.getValue() != null && fechaVentaH.getValue() != null) {
							Date stateTypeDesde= (fechaVentaD.getValue());
							Date stateTypeHasta= (fechaVentaH.getValue());
							
							Calendar ahoraCalDesde= Calendar.getInstance();
							ahoraCalDesde.setTime(stateTypeDesde);
							
							Calendar ahoraCalHasta= Calendar.getInstance();
							ahoraCalHasta.setTime(stateTypeHasta);

							
							String fechaNacDesde=ahoraCalDesde.get(Calendar.YEAR)+"-"+ahoraCalDesde.get(Calendar.MONTH)+ 1 +"-"+ahoraCalHasta.get(Calendar.DATE) ;

							String fechaNacHasta=ahoraCalHasta.get(Calendar.YEAR)+"-"+ahoraCalHasta.get(Calendar.MONTH)+ 1 +"-"+ahoraCalHasta.get(Calendar.DATE) ;
							
							actionConditions.append(" and  clientexx.id in ( " +
									"	select subs.idcliente from subscripcion subs  " +
									" where  to_char(fechayhoracreacion,'YYYY-MM-DD') >='"+fechaNacDesde +"'  and" +
											"   to_char(fechayhoracreacion,'YYYY-MM-DD')<= '"+fechaNacHasta+"' )");
								
							
						}else 	if (fechaVentaD.getValue() != null) {
							Date stateTypeDesde= (fechaVentaD.getValue());
							
							Calendar ahoraCalDesde= Calendar.getInstance();
							ahoraCalDesde.setTime(stateTypeDesde);
							
							String fechaNacDesde=ahoraCalDesde.get(Calendar.YEAR)+"-"+ahoraCalDesde.get(Calendar.MONTH)+ 1 +"-"+ahoraCalDesde.get(Calendar.DATE) ;
							
							actionConditions.append(" and  clientexx.id in ( " +
									"	select subs.idcliente from subscripcion subs  " +
									" where  to_char(fechayhoracreacion,'YYYY-MM-DD') >='"+fechaNacDesde +"'  )");
							
						}else 	if (fechaVentaH.getValue() != null) {
							Date stateTypeHasta= (fechaVentaH.getValue());
							
							Calendar ahoraCalHasta= Calendar.getInstance();
							ahoraCalHasta.setTime(stateTypeHasta);

							String fechaNacHasta=ahoraCalHasta.get(Calendar.YEAR)+"-"+ahoraCalHasta.get(Calendar.MONTH)+ 1 +"-"+ahoraCalHasta.get(Calendar.DATE) ;
							
							actionConditions.append(" and  clientexx.id in ( " +
									"	select subs.idcliente from subscripcion subs  " +
									" where  to_char(fechayhoracreacion,'YYYY-MM-DD')<= '"+fechaNacHasta+"' )");

						}
						if (clases.getSelectedIndex() >= 0) {
							if(clases.getSelectedItem().getValue() instanceof Clase){
								Clase clase= ((Clase)clases.getSelectedItem().getValue());
								actionConditions.append(" and  clientexx.id in ( " +
										"	select subs.idcliente from subscripcion subs  " +
										"  inner join SubscripcionDeClases subsClases on (subs.id= subsClases.idsubscripcion) "+
										"  inner join SubscripcionDeClasesPorActividad subsclasexact on (subsClases.idsubscripcion = subsclasexact.idsubscripcion2) "+
										" where  ");
								
								if(clase.getDomingo() != null && clase.getDomingo().booleanValue()) // dom
									actionConditions.append("subsclasexact.idclasedomingo="+clase.getId());
								if(clase.getLunes() != null && clase.getLunes().booleanValue()) // lunes
									actionConditions.append("subsclasexact.idclaselunes="+clase.getId());
								if(clase.getMartes() != null && clase.getMartes().booleanValue()) // martes
									actionConditions.append("subsclasexact.idclasemartes="+clase.getId());
								if(clase.getMiercoles() != null && clase.getMiercoles().booleanValue()) // miercoles
									actionConditions.append("subsclasexact.idclasemiercoles="+clase.getId());
								if(clase.getJueves() != null && clase.getJueves().booleanValue()) // jueves
									actionConditions.append("subsclasexact.idclasejueves="+clase.getId());
								if(clase.getViernes() != null && clase.getViernes().booleanValue()) // viernes
									actionConditions.append("subsclasexact.idclaseviernes="+clase.getId());
								if(clase.getSabado() != null && clase.getSabado().booleanValue()) // sabado
									actionConditions.append("subsclasexact.idclasesabado="+clase.getId());
								actionConditions.append(" )  ");
							}else 	if(clases.getSelectedItem().getValue() instanceof Quincena){
								
								Calendar calendarDate = Calendar.getInstance();  
								calendarDate.setTime(new Date());  
								int mess=calendarDate.get(Calendar.MONTH);
								int anioQueQueda= calendarDate.get(Calendar.YEAR);
								
								if(mess >= 4){
									anioQueQueda=anioQueQueda +1;
								}else{
									anioQueQueda=anioQueQueda -1;
								}

								
								actionConditions.append(" and  clientexx.id in ( " +
									"	select s.idcliente from cupoactividad  ca  "	+
									"   inner join subscripcion s on (ca.idsubscripcion= s.id )  "	+
									"   inner join curso c  on (ca.idcurso=c.id)  "	+
									"  inner join concepto conce on ( conce.idsubscripcion=s.id and conce.idcurso=c.id)  "+ 
									"   where " +
									// "c.vencimiento=5  and estado in (3, 10) and conce.idquincena=23 )");
									" conce.idquincena="+((Quincena)clases.getSelectedItem().getValue()).getId() +
									" and to_char(s.fechayhoracreacion,'YYYY-MM') >='"+anioQueQueda+"-"+mess+"' )");
							}
						}
						
						actionConditions.append(" order by clientexx.apellido, clientexx.nombre ");
				
					

		return actionConditions.toString();
	}


	
	public String getFilters(Integer firstResult, Integer MaxResult,String id, boolean order){
		
		StringBuilder actionConditions= new StringBuilder("select clientexx.id  from cliente clientexx   ");
					actionConditions.append("where 1=1  ");
			
						if (nombre.getValue()!= null && !nombre.getValue().trim().equals("") && !nombre.getValue().trim().equals(" ")) {
							actionConditions.append(" and  Upper(clientexx.nombre) like Upper('%"+nombre.getValue().trim()+"%') ");
						}
						
						if (apellido.getValue()!= null && !apellido.getValue().trim().equals("") && !apellido.getValue().trim().equals(" ")) {
							actionConditions.append(" and  Upper(clientexx.apellido) like Upper('%"+apellido.getValue().trim()+"%') ");
						}
								
						if (estado.getSelectedIndex() >= 0) {
							ClienteEstadoEnum stateType= ((ClienteEstadoEnum)estado.getSelectedItem().getValue());

							if(stateType.equals(ClienteEstadoEnum.ADEUDA)){
								actionConditions.append(" and clientexx.id in ( ");
								actionConditions.append(" select distinct(c.id) from cliente c inner join subscripcion s on (c.id= s.idcliente) "); 
								actionConditions.append(" inner join cupoactividad ca on (ca.idsubscripcion= s.id) where ca.estado in (0, 1, 10) ");

							}else	if(stateType.equals(ClienteEstadoEnum.C_CLASES_DISP)){
								actionConditions.append(" and 0 < ( select count(*) from cliente c inner join subscripcion s on (c.id= s.idcliente) "); 
								actionConditions.append(" inner join cupoactividad ca on (ca.idsubscripcion= s.id) where ca.estado in (3, 10) ");

							}else	if(stateType.equals(ClienteEstadoEnum.S_CLASES_DISP)){
								actionConditions.append(" and 0 = ( select count(*) from cliente c inner join subscripcion s on (c.id= s.idcliente) "); 
								actionConditions.append(" inner join cupoactividad ca on (ca.idsubscripcion= s.id) where ca.estado in (3, 10) ");
							}
							actionConditions.append(" and c.id=clientexx.id )");
						}

						if (curso.getSelectedIndex() >= 0) {
							Actividad stateType= ((Actividad)curso.getSelectedItem().getValue());
							actionConditions.append(" and "+ stateType.getId()  +" in " +
									"	(select con.idactividad from concepto con where con.idsubscripcion  in (select subs.id from subscripcion subs where subs.idcliente=clientexx.id )) ");
						}
			   
						if (edadDesde.getSelectedIndex() >= 0) {
							String stateType= ((String)edadDesde.getSelectedItem().getValue());
							if(stateType != null){
								actionConditions.append("  and date_part('year',  age(now(),clientexx.fechanacimiento)) >="+ stateType); 
							}
						}
						if (edadHasta.getSelectedIndex() >= 0) {
							String stateType= ((String)edadHasta.getSelectedItem().getValue());
							if(stateType != null){
								actionConditions.append("  and date_part('year',  age(now(),clientexx.fechanacimiento)) <="+ stateType); 
							}
						}
						if (mesFechaNac.getSelectedIndex() >= 0) {
							String stateType= ((String)mesFechaNac.getSelectedItem().getValue());
							if(stateType != null){
								actionConditions.append("	and to_char(fechanacimiento, 'MM') =  '"+stateType+"'  ");											
							}
						}
						
						if (fechaVentaD.getValue() != null && fechaVentaH.getValue() != null) {
							Date stateTypeDesde= (fechaVentaD.getValue());
							Date stateTypeHasta= (fechaVentaH.getValue());
							
							Calendar ahoraCalDesde= Calendar.getInstance();
							ahoraCalDesde.setTime(stateTypeDesde);
							
							Calendar ahoraCalHasta= Calendar.getInstance();
							ahoraCalHasta.setTime(stateTypeHasta);

							
							String fechaNacDesde=ahoraCalDesde.get(Calendar.YEAR)+"-"+ahoraCalDesde.get(Calendar.MONTH)+ 1 +"-"+ahoraCalHasta.get(Calendar.DATE) ;

							String fechaNacHasta=ahoraCalHasta.get(Calendar.YEAR)+"-"+ahoraCalHasta.get(Calendar.MONTH)+ 1 +"-"+ahoraCalHasta.get(Calendar.DATE) ;
							
							actionConditions.append(" and  clientexx.id in ( " +
									"	select subs.idcliente from subscripcion subs  " +
									" where  to_char(fechayhoracreacion,'YYYY-MM-DD') >='"+fechaNacDesde +"'  and" +
											"   to_char(fechayhoracreacion,'YYYY-MM-DD')<= '"+fechaNacHasta+"' )");
								
							
						}else 	if (fechaVentaD.getValue() != null) {
							Date stateTypeDesde= (fechaVentaD.getValue());
							
							Calendar ahoraCalDesde= Calendar.getInstance();
							ahoraCalDesde.setTime(stateTypeDesde);
							
							String fechaNacDesde=ahoraCalDesde.get(Calendar.YEAR)+"-"+ahoraCalDesde.get(Calendar.MONTH)+ 1 +"-"+ahoraCalDesde.get(Calendar.DATE) ;
							
							actionConditions.append(" and  clientexx.id in ( " +
									"	select subs.idcliente from subscripcion subs  " +
									" where  to_char(fechayhoracreacion,'YYYY-MM-DD') >='"+fechaNacDesde +"'  )");
							
						}else 	if (fechaVentaH.getValue() != null) {
							Date stateTypeHasta= (fechaVentaH.getValue());
							
							Calendar ahoraCalHasta= Calendar.getInstance();
							ahoraCalHasta.setTime(stateTypeHasta);

							String fechaNacHasta=ahoraCalHasta.get(Calendar.YEAR)+"-"+ahoraCalHasta.get(Calendar.MONTH)+ 1 +"-"+ahoraCalHasta.get(Calendar.DATE) ;
							
							actionConditions.append(" and  clientexx.id in ( " +
									"	select subs.idcliente from subscripcion subs  " +
									" where  to_char(fechayhoracreacion,'YYYY-MM-DD')<= '"+fechaNacHasta+"' )");

						}
						if (clases.getSelectedIndex() >= 0) {
							if(clases.getSelectedItem().getValue() instanceof Clase){
								Clase clase= ((Clase)clases.getSelectedItem().getValue());
								actionConditions.append(" and  clientexx.id in ( " +
										"	select subs.idcliente from subscripcion subs  " +
										"  inner join SubscripcionDeClases subsClases on (subs.id= subsClases.idsubscripcion) "+
										"  inner join SubscripcionDeClasesPorActividad subsclasexact on (subsClases.idsubscripcion = subsclasexact.idsubscripcion2) "+
										" where  ");
								if(clase.getDomingo() != null && clase.getDomingo().booleanValue()) // dom
									actionConditions.append("subsclasexact.idclasedomingo="+clase.getId());
								if(clase.getLunes() != null && clase.getLunes().booleanValue()) // lunes
									actionConditions.append("subsclasexact.idclaselunes="+clase.getId());
								if(clase.getMartes() != null && clase.getMartes().booleanValue()) // martes
									actionConditions.append("subsclasexact.idclasemartes="+clase.getId());
								if(clase.getMiercoles() != null && clase.getMiercoles().booleanValue()) // miercoles
									actionConditions.append("subsclasexact.idclasemiercoles="+clase.getId());
								if(clase.getJueves() != null && clase.getJueves().booleanValue()) // jueves
									actionConditions.append("subsclasexact.idclasejueves="+clase.getId());
								if(clase.getViernes() != null && clase.getViernes().booleanValue()) // viernes
									actionConditions.append("subsclasexact.idclaseviernes="+clase.getId());
								if(clase.getSabado() != null && clase.getSabado().booleanValue()) // sabado
									actionConditions.append("subsclasexact.idclasesabado="+clase.getId());
								actionConditions.append(" )  ");

							}else 	if(clases.getSelectedItem().getValue() instanceof Quincena){
								
								Calendar calendarDate = Calendar.getInstance();  
								calendarDate.setTime(new Date());  
								int mess=calendarDate.get(Calendar.MONTH);
								int anioQueQueda= calendarDate.get(Calendar.YEAR);
								
								if(mess >= 4){
									anioQueQueda=anioQueQueda +1;
								}else{
									anioQueQueda=anioQueQueda -1;
								}

								
								actionConditions.append(" and  clientexx.id in ( " +
									"	select s.idcliente from cupoactividad  ca  "	+
									"   inner join subscripcion s on (ca.idsubscripcion= s.id )  "	+
									"   inner join curso c  on (ca.idcurso=c.id)  "	+
									"  inner join concepto conce on ( conce.idsubscripcion=s.id and conce.idcurso=c.id)  "+ 
									"   where " +
									// "c.vencimiento=5  and estado in (3, 10) and conce.idquincena=23 )");
									" conce.idquincena="+((Quincena)clases.getSelectedItem().getValue()).getId() +
									" and to_char(s.fechayhoracreacion,'YYYY-MM') >='"+anioQueQueda+"-"+mess+"' )");
							}
						}
						
						if(id != null && (!id.equalsIgnoreCase("inscriptoEnAlgo") && !id.equalsIgnoreCase("deuda"))){
							if(order){
								actionConditions.append(" order by clientexx."+id +" desc");

							}else{
								actionConditions.append(" order by clientexx."+id +" asc");
							}
							
						}else{
							actionConditions.append(" order by clientexx.apellido, clientexx.nombre ");
						}
						
						if(MaxResult == -10){
							
						}else
							actionConditions.append( " Limit " + MaxResult + " offset "	+ firstResult );
				
					

		return actionConditions.toString();
	}
	
	public String getFiltersCount(){
		
		StringBuilder actionConditions= new StringBuilder("select count(*) from cliente clientexx   ");
					actionConditions.append("where 1=1  ");
				
			
						if (nombre.getValue()!= null && !nombre.getValue().trim().equals("") && !nombre.getValue().trim().equals(" ")) {
							actionConditions.append(" and  Upper(clientexx.nombre) like Upper('%"+nombre.getValue().trim()+"%') ");
						}
						
						if (apellido.getValue()!= null && !apellido.getValue().trim().equals("") && !apellido.getValue().trim().equals(" ")) {
							actionConditions.append(" and  Upper(clientexx.apellido) like Upper('%"+apellido.getValue().trim()+"%') ");
						}
						
//						if (dni.getValue()!= null && !dni.getValue().trim().equals("") && !dni.getValue().trim().equals(" ")) {
//							actionConditions.append(" and  Upper(cliente.dni) like Upper('%"+dni.getValue()+"%') ");
//						}
										
						if (estado.getSelectedIndex() >= 0) {
							ClienteEstadoEnum stateType= ((ClienteEstadoEnum)estado.getSelectedItem().getValue());
							if(stateType.equals(ClienteEstadoEnum.ADEUDA)){
								actionConditions.append(" and clientexx.id in ( ");
								actionConditions.append(" select distinct(c.id) from cliente c inner join subscripcion s on (c.id= s.idcliente) "); 
								actionConditions.append(" inner join cupoactividad ca on (ca.idsubscripcion= s.id) where ca.estado in (0, 1, 10) ");

							}else	if(stateType.equals(ClienteEstadoEnum.C_CLASES_DISP)){
								actionConditions.append(" and 0 < ( select count(*) from cliente c inner join subscripcion s on (c.id= s.idcliente) "); 
								actionConditions.append(" inner join cupoactividad ca on (ca.idsubscripcion= s.id) where ca.estado in (3, 10) ");

							}else	if(stateType.equals(ClienteEstadoEnum.S_CLASES_DISP)){
								actionConditions.append(" and 0 = ( select count(*) from cliente c inner join subscripcion s on (c.id= s.idcliente) "); 
								actionConditions.append(" inner join cupoactividad ca on (ca.idsubscripcion= s.id) where ca.estado in (3, 10) ");
							}
							actionConditions.append(" and c.id=clientexx.id )");
						}

						if (curso.getSelectedIndex() >= 0) {
							Actividad stateType= ((Actividad)curso.getSelectedItem().getValue());
							actionConditions.append(" and "+ stateType.getId()  +" in " +
									"	(select con.idactividad from concepto con where con.idsubscripcion  in (select subs.id from subscripcion subs where subs.idcliente=clientexx.id )) ");
						}
			   
						if (edadDesde.getSelectedIndex() >= 0) {
							String stateType= ((String)edadDesde.getSelectedItem().getValue());
							if(stateType != null){
								actionConditions.append("  and date_part('year',  age(now(),clientexx.fechanacimiento)) >="+ stateType); 
							}
						}
						if (edadHasta.getSelectedIndex() >= 0) {
							String stateType= ((String)edadHasta.getSelectedItem().getValue());
							if(stateType != null){
								actionConditions.append("  and date_part('year',  age(now(),clientexx.fechanacimiento)) <="+ stateType); 
							}
						}

				
						if (mesFechaNac.getSelectedIndex() >= 0) {
							String stateType= ((String)mesFechaNac.getSelectedItem().getValue());
							if(stateType != null){
								actionConditions.append("	and to_char(fechanacimiento, 'MM') =  '"+stateType+"'  ");											
							}
						}
						
						if (fechaVentaD.getValue() != null && fechaVentaH.getValue() != null) {
							Date stateTypeDesde= (fechaVentaD.getValue());
							Date stateTypeHasta= (fechaVentaH.getValue());
							
							Calendar ahoraCalDesde= Calendar.getInstance();
							ahoraCalDesde.setTime(stateTypeDesde);
							
							Calendar ahoraCalHasta= Calendar.getInstance();
							ahoraCalHasta.setTime(stateTypeHasta);

							
							String fechaNacDesde=ahoraCalDesde.get(Calendar.YEAR)+"-"+ahoraCalDesde.get(Calendar.MONTH)+ 1 +"-"+ahoraCalHasta.get(Calendar.DATE) ;

							String fechaNacHasta=ahoraCalHasta.get(Calendar.YEAR)+"-"+ahoraCalHasta.get(Calendar.MONTH)+ 1 +"-"+ahoraCalHasta.get(Calendar.DATE) ;
							
							actionConditions.append(" and  clientexx.id in ( " +
									"	select subs.idcliente from subscripcion subs  " +
									" where  to_char(fechayhoracreacion,'YYYY-MM-DD') >='"+fechaNacDesde +"'  and" +
											"   to_char(fechayhoracreacion,'YYYY-MM-DD')<= '"+fechaNacHasta+"' )");
								
							
						}else 	if (fechaVentaD.getValue() != null) {
							Date stateTypeDesde= (fechaVentaD.getValue());
							
							Calendar ahoraCalDesde= Calendar.getInstance();
							ahoraCalDesde.setTime(stateTypeDesde);
							
							String fechaNacDesde=ahoraCalDesde.get(Calendar.YEAR)+"-"+ahoraCalDesde.get(Calendar.MONTH)+ 1 +"-"+ahoraCalDesde.get(Calendar.DATE) ;
							
							actionConditions.append(" and  clientexx.id in ( " +
									"	select subs.idcliente from subscripcion subs  " +
									" where  to_char(fechayhoracreacion,'YYYY-MM-DD') >='"+fechaNacDesde +"'  )");
							
						}else 	if (fechaVentaH.getValue() != null) {
							Date stateTypeHasta= (fechaVentaH.getValue());
							
							Calendar ahoraCalHasta= Calendar.getInstance();
							ahoraCalHasta.setTime(stateTypeHasta);

							String fechaNacHasta=ahoraCalHasta.get(Calendar.YEAR)+"-"+ahoraCalHasta.get(Calendar.MONTH)+ 1 +"-"+ahoraCalHasta.get(Calendar.DATE) ;
							
							actionConditions.append(" and  clientexx.id in ( " +
									"	select subs.idcliente from subscripcion subs  " +
									" where  to_char(fechayhoracreacion,'YYYY-MM-DD')<= '"+fechaNacHasta+"' )");

						}
						
						if (clases.getSelectedIndex() >= 0) {
							if(clases.getSelectedItem().getValue() instanceof Clase){
								Clase clase= ((Clase)clases.getSelectedItem().getValue());
								actionConditions.append(" and  clientexx.id in ( " +
										"	select subs.idcliente from subscripcion subs  " +
										"  inner join SubscripcionDeClases subsClases on (subs.id= subsClases.idsubscripcion) "+
										"  inner join SubscripcionDeClasesPorActividad subsclasexact on (subsClases.idsubscripcion = subsclasexact.idsubscripcion2) "+
										" where  ");
								
								if(clase.getDomingo() != null && clase.getDomingo().booleanValue()) // dom
									actionConditions.append("subsclasexact.idclasedomingo="+clase.getId());
								if(clase.getLunes() != null && clase.getLunes().booleanValue()) // lunes
									actionConditions.append("subsclasexact.idclaselunes="+clase.getId());
								if(clase.getMartes() != null && clase.getMartes().booleanValue()) // martes
									actionConditions.append("subsclasexact.idclasemartes="+clase.getId());
								if(clase.getMiercoles() != null && clase.getMiercoles().booleanValue()) // miercoles
									actionConditions.append("subsclasexact.idclasemiercoles="+clase.getId());
								if(clase.getJueves() != null && clase.getJueves().booleanValue()) // jueves
									actionConditions.append("subsclasexact.idclasejueves="+clase.getId());
								if(clase.getViernes() != null && clase.getViernes().booleanValue()) // viernes
									actionConditions.append("subsclasexact.idclaseviernes="+clase.getId());
								if(clase.getSabado() != null && clase.getSabado().booleanValue()) // sabado
									actionConditions.append("subsclasexact.idclasesabado="+clase.getId());
								actionConditions.append(" )  ");

								
							}else 	if(clases.getSelectedItem().getValue() instanceof Quincena){ 
								
								Calendar calendarDate = Calendar.getInstance();  
								calendarDate.setTime(new Date());  
								int mess=calendarDate.get(Calendar.MONTH);
								int anioQueQueda= calendarDate.get(Calendar.YEAR);
								
								if(mess >= 4){
									anioQueQueda=anioQueQueda +1;
								}else{
									anioQueQueda=anioQueQueda -1;
								}
								actionConditions.append(" and  clientexx.id in ( " +
									"	select s.idcliente from cupoactividad  ca  "	+
									"   inner join subscripcion s on (ca.idsubscripcion= s.id )  "	+
									"   inner join curso c  on (ca.idcurso=c.id)  "	+
									"  inner join concepto conce on ( conce.idsubscripcion=s.id and conce.idcurso=c.id)  "+ 
									"   where " +
									// "c.vencimiento=5  and estado in (3, 10) and conce.idquincena=23 )");
									" conce.idquincena="+((Quincena)clases.getSelectedItem().getValue()).getId()+ 
									" and to_char(s.fechayhoracreacion,'YYYY-MM') >='"+anioQueQueda+"-"+mess+"' )");

							}
						}
		
		return actionConditions.toString();
	}
	
	public void clear(){
		Constraint c;
		
	//	nombre, apellido, dni, edad, tipoCurso, curso, estado
		
		c =nombre.getConstraint();
		nombre.setConstraint("");
		nombre.setText(null);
		nombre.setConstraint(c);
		
		c = apellido.getConstraint();
		apellido.setConstraint("");
		apellido.setText(null);
		apellido.setConstraint(c);
//		
//		c= dni.getConstraint();
//		dni.setConstraint("");
//		dni.setText(null);
//		dni.setConstraint(c);
		
		c= edadDesde.getConstraint();
		edadDesde.setConstraint("");
		edadDesde.setSelectedItem(null);
		edadDesde.setConstraint(c);
		
		c= edadHasta.getConstraint();
		edadHasta.setConstraint("");
		edadHasta.setSelectedItem(null);
		edadHasta.setConstraint(c);

		c= mesFechaNac.getConstraint();
		mesFechaNac.setConstraint("");
		mesFechaNac.setSelectedItem(null);
		mesFechaNac.setConstraint(c);

		c= curso.getConstraint();
		curso.setConstraint("");
		curso.setSelectedItem(null);
		curso.setConstraint(c);
		
		c= clases.getConstraint();
		clases.setConstraint("");
		clases.setSelectedItem(null);
		clases.setConstraint(c);
		
		c= estado.getConstraint();
		estado.setConstraint("");
		estado.setSelectedItem(null);
		estado.setConstraint(c);
		
		
		c= fechaVentaD.getConstraint();
		fechaVentaD.setConstraint("");
		fechaVentaD.setValue(null);
		fechaVentaD.setText(null);
		
		c= fechaVentaH.getConstraint();
		fechaVentaH.setConstraint("");
		fechaVentaH.setValue(null);
		fechaVentaH.setText(null);
	}


	public void clear2(){
		Constraint c;
		
	//	nombre, apellido, dni, edad, tipoCurso, curso, estado
		
		c =nombre.getConstraint();
		nombre.setConstraint("");
		nombre.setText(null);
		nombre.setConstraint(c);
		
		c = apellido.getConstraint();
		apellido.setConstraint("");
		apellido.setText(null);
		apellido.setConstraint(c);

//		c = codBarras.getConstraint();
//		codBarras.setConstraint("");
//		codBarras.setText(null);
//		codBarras.setConstraint(c);

		c= edadDesde.getConstraint();
		edadDesde.setConstraint("");
		edadDesde.setSelectedItem(null);
		edadDesde.setConstraint(c);
		
		c= fechaVentaD.getConstraint();
		fechaVentaD.setConstraint("");
		fechaVentaD.setValue(null);
		fechaVentaD.setText(null);
		
		c= fechaVentaH.getConstraint();
		fechaVentaH.setConstraint("");
		fechaVentaH.setValue(null);
		fechaVentaH.setText(null);

		
		c= edadHasta.getConstraint();
		edadHasta.setConstraint("");
		edadHasta.setSelectedItem(null);
		edadHasta.setConstraint(c);
		c= mesFechaNac.getConstraint();
		mesFechaNac.setConstraint("");
		mesFechaNac.setSelectedItem(null);
		mesFechaNac.setConstraint(c);

		c= curso.getConstraint();
		curso.setConstraint("");
		curso.setSelectedItem(null);
		curso.setConstraint(c);
		
		c= clases.getConstraint();
		clases.setConstraint("");
		clases.setSelectedItem(null);
		clases.setConstraint(c);
		
		c= estado.getConstraint();
		estado.setConstraint("");
		estado.setSelectedItem(null);
		estado.setConstraint(c);
		
		
	}
	public Combobox getCurso() {
		return curso;
	}

	public void setCurso(Combobox curso) {
		this.curso = curso;
	}

	public Textbox getNombre() {
		return nombre;
	}

	public void setNombre(Textbox nombre) {
		this.nombre = nombre;
	}

	public Textbox getApellido() {
		return apellido;
	}

	public void setApellido(Textbox apellido) {
		this.apellido = apellido;
	}

	public Combobox getClases() {
		return clases;
	}

	public void setClases(Combobox clases) {
		this.clases = clases;
	}

	public ClienteDelegate getActionComposerDelegate() {
		return actionComposerDelegate;
	}

	public void setActionComposerDelegate(ClienteDelegate actionComposerDelegate) {
		this.actionComposerDelegate = actionComposerDelegate;
	}

}