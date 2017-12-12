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
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

import com.institucion.bz.ClaseEJB;
import com.institucion.bz.CursoEJB;
import com.institucion.desktop.delegated.ClienteDelegate;
import com.institucion.desktop.helper.EstadoClienteViewHelper;
import com.institucion.fm.conf.Session;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.validator.TextConstraint;
import com.institucion.fm.desktop.view.GridFilter;
import com.institucion.fm.desktop.view.RequiredLabel;
import com.institucion.fm.filteradv.model.CriteriaClause;

public class IngresoInscripcionClientFilter extends GridFilter {
	private static final long serialVersionUID = 1L;

	private Textbox nombre;
	private Textbox apellido;
	private Textbox codBarras;
//	private Textbox dni;
//	private Combobox edad;
//	private Combobox mesFechaNac;

	private Combobox curso;   // los que se dan de alta desde la otra pantalla de cursos q estan asociados a 1 tipo Curso
	private Combobox estado;  // Adeuda, C/cupos Disp, S/cupos D.
	private Combobox clases;  // Adeuda, C/cupos Disp, S/cupos D.
	private Label codbarrasLabel;
	private CursoEJB cursoEJB;
	private ClaseEJB claseEJB;
	private ClienteDelegate actionComposerDelegate;	

	
//	agregar 2 filtros que sean, cumpleanios contratados: ANIO:   MES
	
	public IngresoInscripcionClientFilter()	{
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
//		apellido.setWidth("65px");
		apellido.setConstraint(new TextConstraint());
		apellido.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
				codBarras.setText(null);
				actionComposerDelegate.buscar(evt, false);

			}
		});	

		row1.appendChild(apellido);
		
		String nameLabel = I18N.getLabel("client.nombre");
		row1.appendChild(new RequiredLabel(nameLabel));
		nombre = new Textbox();
//		nombre.setWidth("65px");
		nombre.setConstraint(new TextConstraint());
		nombre.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
				codBarras.setText(null);
				actionComposerDelegate.buscar(evt, false);

			}
		});	
		row1.appendChild(nombre);


		String cursoLabel =  I18N.getLabel("client.curso.actividad");
		row1.appendChild(new Label(cursoLabel));
		
		curso= new Combobox();
		curso.setConstraint("strict");
//		curso.setWidth("65px");
		curso.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
				codBarras.setText(null);
				actionComposerDelegate.buscar(evt, false);
			}
		});	
		row1.appendChild(curso);
		
		List<Actividad> actividades=cursoEJB.findAllActividad();
		if(actividades != null){
			setCurso(getComboBoxActividad(actividades, curso));						
		}
		
		String clasessLabel =  I18N.getLabel("clases.crud.tab222");
		row1.appendChild(new Label(clasessLabel));
		
		clases= new Combobox();
//		clases.setWidth("85px");

		clases.setConstraint("strict");
		clases.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
				codBarras.setText(null);
				actionComposerDelegate.buscar(evt, false);
			}
		});	
		row1.appendChild(clases);
		
		
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
			clasesList=claseEJB.findAllConActividadTomaListaDelDia(false);	
		}
		List<Quincena> nombreCursos= null;
		if(!esCentro){
			nombreCursos=claseEJB.findAllConActividadTomaListaDelDiaNombreCurso();	
		}
				
		if(clasesList != null ||  nombreCursos != null){
			setClases(getComboBoxClases(clasesList, nombreCursos,  clases));						
		}
			
		String estadoLabel =  I18N.getLabel("client.estado");
		row1.appendChild(new Label(estadoLabel));
		estado = EstadoClienteViewHelper.getComboBox();
		//estado.setId("estadoxx");
		estado.setConstraint("strict");
		estado.setWidth("60%");
		estado.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
				codBarras.setText(null);
				actionComposerDelegate.buscar(evt, false);
			}
		});	
		row1.appendChild(estado);
		codbarrasLabel= new Label("Codigo Barras");
		row1.appendChild(codbarrasLabel);
		codBarras = new Textbox();
//		codBarras.setWidth("55px");
		codBarras.setWidth("70%");
		codBarras.setConstraint(new TextConstraint());
		codBarras.addEventListener(Events.ON_CHANGING, new EventListener() {
			public void onEvent(Event evt){
				InputEvent eventtt=(InputEvent)evt;
				clear();
//				Textbox tb=(Textbox)evt.getTarget();
				try{
					if(eventtt.getValue() != null && !eventtt.getValue().trim().equalsIgnoreCase(""))
						Long.parseLong(eventtt.getValue());
				}catch(Exception e){
					Constraint c = codBarras.getConstraint();
					codBarras.setConstraint("");
					codBarras.setText("");
					codBarras.setValue("");
					codBarras.setConstraint(c);
				}
			}
		});	
		codBarras.setFocus(true);
		codBarras.addEventListener(Events.ON_OK, new EventListener() {
			public void onEvent(Event evt){
				if(codBarras.getValue() != null){
					if(codBarras.getValue().length() == 18){
						String aa= codBarras.getValue().substring(9, codBarras.getValue().length());
						codBarras.setValue(aa);	
						codBarras.setText(aa);	
					}else if(codBarras.getValue().length() == 16){
						String aa= codBarras.getValue().substring(8, codBarras.getValue().length());
						codBarras.setValue(aa);	
						codBarras.setText(aa);	
					}else if(codBarras.getValue().length() == 14){
						String aa= codBarras.getValue().substring(7, codBarras.getValue().length());
						codBarras.setValue(aa);	
						codBarras.setText(aa);	
					}else if(codBarras.getValue().length() == 12){
						String aa= codBarras.getValue().substring(6, codBarras.getValue().length());
						codBarras.setValue(aa);	
						codBarras.setText(aa);	
					}else if(codBarras.getValue().length() == 10){
						String aa= codBarras.getValue().substring(5, codBarras.getValue().length());
						codBarras.setValue(aa);	
						codBarras.setText(aa);	
					}else if(codBarras.getValue().length() == 8){
						String aa= codBarras.getValue().substring(4, codBarras.getValue().length());
						codBarras.setValue(aa);	
						codBarras.setText(aa);	
					}
					
				}
				clear();
				actionComposerDelegate.buscar(evt, true);
			}
		});	
		row1.appendChild(codBarras);
		this.addRow(row1);
		
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
	
	public String getFilters(Integer firstResult, Integer MaxResult,String id, boolean order){
		
		StringBuilder actionConditions= new StringBuilder("select clientexx.id  from cliente clientexx   ");
					actionConditions.append("where 1=1  ");
				
					
					if (codBarras.getValue()!= null && !codBarras.getValue().trim().equals("") && !codBarras.getValue().trim().equals(" ")) {
						String aa= codBarras.getValue();
				       	String nuevoVal=null;
			        	if(aa.startsWith("00000"))
			        		nuevoVal=aa.substring(5, aa.length());
			        	else if(aa.startsWith("0000"))
			        		nuevoVal=aa.substring(4, aa.length());
			        	else if(aa.startsWith("000"))
			        		nuevoVal=aa.substring(3, aa.length());
			        	else if(aa.startsWith("00"))
			        		nuevoVal=aa.substring(2, aa.length());
			        	else if(aa.startsWith("0"))
			        		nuevoVal=aa.substring(1, aa.length());
			        	else
			        		nuevoVal=aa;
			        	
						actionConditions.append(" and  clientexx.id='"+nuevoVal+"' ");
				
						
					}else{

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
			   
						if (clases.getSelectedIndex() >= 0) {
							if(clases.getSelectedItem().getValue() instanceof Clase){
								Clase clase= ((Clase)clases.getSelectedItem().getValue());
								actionConditions.append(" and  clientexx.id in ( " +
										"	select subs.idcliente from subscripcion subs  " +
										"  inner join SubscripcionDeClases subsClases on (subs.id= subsClases.idsubscripcion) "+
										"  inner join SubscripcionDeClasesPorActividad subsclasexact on (subsClases.idsubscripcion = subsclasexact.idsubscripcion2) "+
										" where  ");
								
								Calendar calendarDate = Calendar.getInstance();
								calendarDate.setTime(new Date());
								int day=calendarDate.getTime().getDay();
								if(day == 0) // dom
									actionConditions.append("subsclasexact.idclasedomingo="+clase.getId());
								if(day == 1) // lunes
									actionConditions.append("subsclasexact.idclaselunes="+clase.getId());
								if(day == 2) // martes
									actionConditions.append("subsclasexact.idclasemartes="+clase.getId());
								if(day == 3) // miercoles
									actionConditions.append("subsclasexact.idclasemiercoles="+clase.getId());
								if(day == 4) // jueves
									actionConditions.append("subsclasexact.idclasejueves="+clase.getId());
								if(day == 5) // viernes
									actionConditions.append("subsclasexact.idclaseviernes="+clase.getId());
								if(day == 6) // sabado
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
						
						if(id != null && (!id.equalsIgnoreCase("inscriptoEnAlgo") && !id.equalsIgnoreCase("deuda"))
								&& (id.equalsIgnoreCase("id")	|| id.equalsIgnoreCase("nombre") 	|| id.equalsIgnoreCase("apellido")||
										id.equalsIgnoreCase("dni")	|| id.equalsIgnoreCase("fechanacimiento") 	|| id.equalsIgnoreCase("domicilio")||
										id.equalsIgnoreCase("telefonofijo")	|| id.equalsIgnoreCase("celular") 	|| id.equalsIgnoreCase("facebook")||
										id.equalsIgnoreCase("mail")	|| id.equalsIgnoreCase("entregocertificado") 	|| id.equalsIgnoreCase("estado"))){

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
					}
					
					

		return actionConditions.toString();
	}
	
	
	
	public String getFiltersForExportingExcelClientes(Integer firstResult, Integer MaxResult,String id, boolean order){
		
		StringBuilder actionConditions= new StringBuilder("select id, apellido, nombre, dni, fechanacimiento, " +
				"domicilio, celular, telefonofijo, facebook, mail" +
				", null as fechayhoracreacion, null as idusuariocreosubscripcion from cliente clientexx ");
		
					actionConditions.append("where 1=1  ");
					
					if (codBarras.getValue()!= null && !codBarras.getValue().trim().equals("") && !codBarras.getValue().trim().equals(" ")) {
						String aa= codBarras.getValue();
				       	String nuevoVal=null;
			        	if(aa.startsWith("00000"))
			        		nuevoVal=aa.substring(5, aa.length());
			        	else if(aa.startsWith("0000"))
			        		nuevoVal=aa.substring(4, aa.length());
			        	else if(aa.startsWith("000"))
			        		nuevoVal=aa.substring(3, aa.length());
			        	else if(aa.startsWith("00"))
			        		nuevoVal=aa.substring(2, aa.length());
			        	else if(aa.startsWith("0"))
			        		nuevoVal=aa.substring(1, aa.length());
			        	else
			        		nuevoVal=aa;
			        	
						actionConditions.append(" and  clientexx.id='"+nuevoVal+"' ");
				
						
					}else{

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
			   
						if (clases.getSelectedIndex() >= 0) {
							if(clases.getSelectedItem().getValue() instanceof Clase){
								Clase clase= ((Clase)clases.getSelectedItem().getValue());
								actionConditions.append(" and  clientexx.id in ( " +
										"	select subs.idcliente from subscripcion subs  " +
										"  inner join SubscripcionDeClases subsClases on (subs.id= subsClases.idsubscripcion) "+
										"  inner join SubscripcionDeClasesPorActividad subsclasexact on (subsClases.idsubscripcion = subsclasexact.idsubscripcion2) "+
										" where  ");
								
								Calendar calendarDate = Calendar.getInstance();
								calendarDate.setTime(new Date());
								int day=calendarDate.getTime().getDay();
								if(day == 0) // dom
									actionConditions.append("subsclasexact.idclasedomingo="+clase.getId());
								if(day == 1) // lunes
									actionConditions.append("subsclasexact.idclaselunes="+clase.getId());
								if(day == 2) // martes
									actionConditions.append("subsclasexact.idclasemartes="+clase.getId());
								if(day == 3) // miercoles
									actionConditions.append("subsclasexact.idclasemiercoles="+clase.getId());
								if(day == 4) // jueves
									actionConditions.append("subsclasexact.idclasejueves="+clase.getId());
								if(day == 5) // viernes
									actionConditions.append("subsclasexact.idclaseviernes="+clase.getId());
								if(day == 6) // sabado
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
						
						if(id != null && (!id.equalsIgnoreCase("inscriptoEnAlgo") && !id.equalsIgnoreCase("deuda"))
								&& (id.equalsIgnoreCase("id")	|| id.equalsIgnoreCase("nombre") 	|| id.equalsIgnoreCase("apellido")||
										id.equalsIgnoreCase("dni")	|| id.equalsIgnoreCase("fechanacimiento") 	|| id.equalsIgnoreCase("domicilio")||
										id.equalsIgnoreCase("telefonofijo")	|| id.equalsIgnoreCase("celular") 	|| id.equalsIgnoreCase("facebook")||
										id.equalsIgnoreCase("mail")	|| id.equalsIgnoreCase("entregocertificado") 	|| id.equalsIgnoreCase("estado"))){							if(order){
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
					}
					
					

		return actionConditions.toString();
	}
	
	public String getFiltersCount(){
		
		StringBuilder actionConditions= new StringBuilder("select count(*) from cliente clientexx   ");
					actionConditions.append("where 1=1  ");
				
					if (codBarras.getValue()!= null && !codBarras.getValue().trim().equals("") && !codBarras.getValue().trim().equals(" ")) {
						String aa= codBarras.getValue();
				       	String nuevoVal=null;
			        	if(aa.startsWith("00000"))
			        		nuevoVal=aa.substring(5, aa.length());
			        	else if(aa.startsWith("0000"))
			        		nuevoVal=aa.substring(4, aa.length());
			        	else if(aa.startsWith("000"))
			        		nuevoVal=aa.substring(3, aa.length());
			        	else if(aa.startsWith("00"))
			        		nuevoVal=aa.substring(2, aa.length());
			        	else if(aa.startsWith("0"))
			        		nuevoVal=aa.substring(1, aa.length());
			        	else
			        		nuevoVal=aa;

						actionConditions.append(" and  clientexx.id='"+nuevoVal+ "' ");

					}else{
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
			   
						
						if (clases.getSelectedIndex() >= 0) {
							if(clases.getSelectedItem().getValue() instanceof Clase){
								Clase clase= ((Clase)clases.getSelectedItem().getValue());
								actionConditions.append(" and  clientexx.id in ( " +
										"	select subs.idcliente from subscripcion subs  " +
										"  inner join SubscripcionDeClases subsClases on (subs.id= subsClases.idsubscripcion) "+
										"  inner join SubscripcionDeClasesPorActividad subsclasexact on (subsClases.idsubscripcion = subsclasexact.idsubscripcion2) "+
										" where  ");
								
								Calendar calendarDate = Calendar.getInstance();
								calendarDate.setTime(new Date());
								int day=calendarDate.getTime().getDay();
								if(day == 0) // dom
									actionConditions.append("subsclasexact.idclasedomingo="+clase.getId());
								if(day == 1) // lunes
									actionConditions.append("subsclasexact.idclaselunes="+clase.getId());
								if(day == 2) // martes
									actionConditions.append("subsclasexact.idclasemartes="+clase.getId());
								if(day == 3) // miercoles
									actionConditions.append("subsclasexact.idclasemiercoles="+clase.getId());
								if(day == 4) // jueves
									actionConditions.append("subsclasexact.idclasejueves="+clase.getId());
								if(day == 5) // viernes
									actionConditions.append("subsclasexact.idclaseviernes="+clase.getId());
								if(day == 6) // sabado
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
		
//		c= edad.getConstraint();
//		edad.setConstraint("");
//		edad.setSelectedItem(null);
//		edad.setConstraint(c);
//
//		c= mesFechaNac.getConstraint();
//		mesFechaNac.setConstraint("");
//		mesFechaNac.setSelectedItem(null);
//		mesFechaNac.setConstraint(c);
//
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

		c = codBarras.getConstraint();
		codBarras.setConstraint("");
		codBarras.setText(null);
		codBarras.setConstraint(c);

//		c= edad.getConstraint();
//		edad.setConstraint("");
//		edad.setSelectedItem(null);
//		edad.setConstraint(c);
//
//		c= mesFechaNac.getConstraint();
//		mesFechaNac.setConstraint("");
//		mesFechaNac.setSelectedItem(null);
//		mesFechaNac.setConstraint(c);
//
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

	public Textbox getCodBarras() {
		return codBarras;
	}

	public void setCodBarras(Textbox codBarras) {
		this.codBarras = codBarras;
	}

	public Label getCodbarrasLabel() {
		return codbarrasLabel;
	}

	public void setCodbarrasLabel(Label codbarrasLabel) {
		this.codbarrasLabel = codbarrasLabel;
	}

//	public Textbox getDni() {
//		return dni;
//	}
//
//	public void setDni(Textbox dni) {
//		this.dni = dni;
//	}

}