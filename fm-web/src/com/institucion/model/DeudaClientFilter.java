package com.institucion.model;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

import com.institucion.bz.CursoEJB;
import com.institucion.desktop.delegated.ClienteDelegate;
import com.institucion.fm.conf.Session;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.validator.TextConstraint;
import com.institucion.fm.desktop.view.GridFilter;
import com.institucion.fm.desktop.view.RequiredLabel;
import com.institucion.fm.filteradv.model.CriteriaClause;
import com.institucion.fm.security.bz.SecurityAAEJB;

public class DeudaClientFilter extends GridFilter {
	private static final long serialVersionUID = 1L;

	private Textbox nombre;
	private Textbox apellido;
	private Combobox curso;   // los que se dan de alta desde la otra pantalla de cursos q estan asociados a 1 tipo Curso
//	private Combobox estado;  // Adeuda, C/cupos Disp, S/cupos D.
//	private Combobox clases;  // Adeuda, C/cupos Disp, S/cupos D.
//	private Label codbarrasLabel;
	private CursoEJB cursoEJB;
//	private ClaseEJB claseEJB;
	private ClienteDelegate actionComposerDelegate;	
	private Datebox fechaVentaD;
	private Datebox fechaVentaH;
	private Combobox usuarios;
	private SecurityAAEJB securityService;

	public DeudaClientFilter()	{
		super();
		cursoEJB = BeanFactory.<CursoEJB>getObject("fmEjbCurso");
//		claseEJB = BeanFactory.<ClaseEJB>getObject("fmEjbClase");
		securityService= BeanFactory.<SecurityAAEJB>getObject("fm.ejb.securityAAService");

		buildFilter();
	}

	private void buildFilter() {

		Row row1 = new Row();
	
		row1.appendChild(new RequiredLabel("Responsable"));
		usuarios= new Combobox();
		usuarios.setConstraint("strict");
		usuarios.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
				if(usuarios.getSelectedItem() != null){
					actionComposerDelegate.buscar(evt, false);
				}
			}
		});	
		
		row1.appendChild(usuarios);
		
		
		String apelLabel = I18N.getLabel("client.apellido");
		row1.appendChild(new RequiredLabel(apelLabel));
		apellido = new Textbox();
//		apellido.setWidth("65px");
		apellido.setConstraint(new TextConstraint());
		apellido.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
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
				actionComposerDelegate.buscar(evt, false);
			}
		});	
		row1.appendChild(curso);
		
		List<Actividad> actividades=cursoEJB.findAllActividad();
		if(actividades != null){
			setCurso(getComboBoxActividad(actividades, curso));						
		}
		
		Label fechaDLabel= new Label("Fecha ventas desde");
		row1.appendChild(fechaDLabel);
		this.fechaVentaD = new Datebox();
		this.fechaVentaD.setWidth("70px");
		fechaVentaD.setFormat(I18N.getDateFormat());
		fechaVentaD.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
//				codBarras.setText(null);
				actionComposerDelegate.buscar(evt, false);
			}
		});	

		row1.appendChild(fechaVentaD);

		
		Label fechaHLabel= new Label("Fecha ventas hasta");
		row1.appendChild(fechaHLabel);
		this.fechaVentaH = new Datebox();
		this.fechaVentaH.setWidth("70px");
		fechaVentaH.setFormat(I18N.getDateFormat());
		fechaVentaH.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
//				codBarras.setText(null);
				actionComposerDelegate.buscar(evt, false);
			}
		});	

		row1.appendChild(fechaVentaH);
		
	
		
		List<com.institucion.fm.security.model.User> userList=securityService.getUsers();
		if(userList != null){
			setUsuarios(getComboBoxUsuarios(userList, usuarios));						
		}

//		row3.appendChild(new Label(""));
//		row3.appendChild(new Label(""));
//		row3.appendChild(new Label(""));
//		row3.appendChild(new Label(""));

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
	
	public String getFilters(String id, boolean order){
		
		StringBuilder actionConditions= new StringBuilder("select distinct clientexx.id, s111.fechayhoracreacion  from cliente clientexx   ");
		actionConditions.append(" inner join subscripcion s111 on (clientexx.id= s111.idcliente) ");
		actionConditions.append(" inner join cupoactividad ca11 on (ca11.idsubscripcion= s111.id) ");
		actionConditions.append(" where ca11.estado in (0, 1, 10)    ");
		
		if(usuarios != null && usuarios.getSelectedItem() != null 
					&& !((usuarios.getSelectedItem().getValue()) instanceof String)){
				com.institucion.fm.security.model.User u= ((com.institucion.fm.security.model.User)usuarios.getSelectedItem().getValue());
				actionConditions.append(" and  s111.idusuariocreosubscripcion=" +u.getId());
	
		}		
				
		if (nombre.getValue()!= null && !nombre.getValue().trim().equals("") && !nombre.getValue().trim().equals(" ")) {
			actionConditions.append(" and  Upper(clientexx.nombre) like Upper('%"+nombre.getValue().trim()+"%') ");
		}
		
		if (apellido.getValue()!= null && !apellido.getValue().trim().equals("") && !apellido.getValue().trim().equals(" ")) {
			actionConditions.append(" and  Upper(clientexx.apellido) like Upper('%"+apellido.getValue().trim()+"%') ");
		}

		if (curso.getSelectedIndex() >= 0) {
			Actividad stateType= ((Actividad)curso.getSelectedItem().getValue());
			actionConditions.append(" and "+ stateType.getId()  +" in " +
					"	(select con.idactividad from concepto con where con.idsubscripcion  in (select subs.id from subscripcion subs where subs.idcliente=clientexx.id )) ");
			}
   

		if (fechaVentaD.getValue() != null && fechaVentaH.getValue() != null) {
			Date stateTypeDesde= (fechaVentaD.getValue());
			Date stateTypeHasta= (fechaVentaH.getValue());
			
			Calendar ahoraCalDesde= Calendar.getInstance();
			ahoraCalDesde.setTime(stateTypeDesde);
			
			Calendar ahoraCalHasta= Calendar.getInstance();
			ahoraCalHasta.setTime(stateTypeHasta);

			int sasd= ahoraCalDesde.get(Calendar.MONTH)+ 1;
			int sash= ahoraCalHasta.get(Calendar.MONTH)+ 1;
			String fechaNacHasta= null;
			if(sash <10)
				fechaNacHasta=ahoraCalHasta.get(Calendar.YEAR)+"-0"+sash +"-"+ahoraCalHasta.get(Calendar.DATE) ;
			else
				fechaNacHasta=ahoraCalHasta.get(Calendar.YEAR)+"-"+sash +"-"+ahoraCalHasta.get(Calendar.DATE) ;

			String fechaNacDesde= null;
			if(sasd <10)
				fechaNacDesde=ahoraCalDesde.get(Calendar.YEAR)+"-0"+sasd +"-"+ahoraCalDesde.get(Calendar.DATE) ;
			else
				fechaNacDesde=ahoraCalDesde.get(Calendar.YEAR)+"-"+sasd +"-"+ahoraCalDesde.get(Calendar.DATE) ;
			
			actionConditions.append(" and  (to_char(s111.fechayhoracreacion,'YYYY-MM-DD') >='"+fechaNacDesde +"'  and" +
							"   to_char(s111.fechayhoracreacion,'YYYY-MM-DD')<= '"+fechaNacHasta+"' )");
				
			
		}else 	if (fechaVentaD.getValue() != null) {
			Date stateTypeDesde= (fechaVentaD.getValue());
			
			Calendar ahoraCalDesde= Calendar.getInstance();
			ahoraCalDesde.setTime(stateTypeDesde);
			int sasd= ahoraCalDesde.get(Calendar.MONTH)+ 1;

			String fechaNacDesde= null;
			if(sasd <10)
				fechaNacDesde=ahoraCalDesde.get(Calendar.YEAR)+"-0"+sasd +"-"+ahoraCalDesde.get(Calendar.DATE) ;
			else
				fechaNacDesde=ahoraCalDesde.get(Calendar.YEAR)+"-"+sasd +"-"+ahoraCalDesde.get(Calendar.DATE) ;
			
			
			
			actionConditions.append(" and (to_char(s111.fechayhoracreacion,'YYYY-MM-DD') >='"+fechaNacDesde +"'  )");
			
		}else 	if (fechaVentaH.getValue() != null) {
			Date stateTypeHasta= (fechaVentaH.getValue());
			
			Calendar ahoraCalHasta= Calendar.getInstance();
			ahoraCalHasta.setTime(stateTypeHasta);
			int sas=ahoraCalHasta.get(Calendar.MONTH) +1;
			String fechaNacHasta= null;
			if(sas <10)
				fechaNacHasta=ahoraCalHasta.get(Calendar.YEAR)+"-0"+sas +"-"+ahoraCalHasta.get(Calendar.DATE) ;
			else
				fechaNacHasta=ahoraCalHasta.get(Calendar.YEAR)+"-"+sas +"-"+ahoraCalHasta.get(Calendar.DATE) ;
			
			actionConditions.append(" and  (to_char(s111.fechayhoracreacion,'YYYY-MM-DD')<= '"+fechaNacHasta+"' )");

		}
		
			if(id != null && id.equals("dias")){
				if(order){
					actionConditions.append(" order by s111.fechayhoracreacion desc");
	
				}else{
					actionConditions.append(" order by s111.fechayhoracreacion asc");
				}				
			}else
				actionConditions.append(" order by s111.fechayhoracreacion desc ");
		
		return actionConditions.toString();
	}
	
	public String getFiltersDeudasTotales(){
		
		StringBuilder actionConditions= new StringBuilder("select  distinct clientexx.id, s111.fechayhoracreacion  from cliente clientexx   ");
		
			actionConditions.append(" inner join subscripcion s111 on (clientexx.id= s111.idcliente) ");
			actionConditions.append(" inner join cupoactividad ca11 on (ca11.idsubscripcion= s111.id) ");
			actionConditions.append(" where ca11.estado in (0, 1, 10)    ");
			
		if(usuarios != null && usuarios.getSelectedItem() != null 
					&& !((usuarios.getSelectedItem().getValue()) instanceof String)){
				com.institucion.fm.security.model.User u= ((com.institucion.fm.security.model.User)usuarios.getSelectedItem().getValue());
				actionConditions.append(" and  s111.idusuariocreosubscripcion=" +u.getId());
	
		}		

		if (nombre.getValue()!= null && !nombre.getValue().trim().equals("") && !nombre.getValue().trim().equals(" ")) {
			actionConditions.append(" and  Upper(clientexx.nombre) like Upper('%"+nombre.getValue().trim()+"%') ");
		}
		
		if (apellido.getValue()!= null && !apellido.getValue().trim().equals("") && !apellido.getValue().trim().equals(" ")) {
			actionConditions.append(" and  Upper(clientexx.apellido) like Upper('%"+apellido.getValue().trim()+"%') ");
		}
	
		if (curso.getSelectedIndex() >= 0) {
			Actividad stateType= ((Actividad)curso.getSelectedItem().getValue());
			actionConditions.append(" and "+ stateType.getId()  +" in " +
					"	(select con.idactividad from concepto con where con.idsubscripcion  in (select subs.id from subscripcion subs where subs.idcliente=clientexx.id )) ");
			}
   
			if (fechaVentaD.getValue() != null && fechaVentaH.getValue() != null) {
				Date stateTypeDesde= (fechaVentaD.getValue());
				Date stateTypeHasta= (fechaVentaH.getValue());
				
				Calendar ahoraCalDesde= Calendar.getInstance();
				ahoraCalDesde.setTime(stateTypeDesde);
				
				Calendar ahoraCalHasta= Calendar.getInstance();
				ahoraCalHasta.setTime(stateTypeHasta);

				
				int sasd= ahoraCalDesde.get(Calendar.MONTH)+ 1;
				int sash= ahoraCalHasta.get(Calendar.MONTH)+ 1;
				String fechaNacHasta= null;
				if(sash <10)
					fechaNacHasta=ahoraCalHasta.get(Calendar.YEAR)+"-0"+sash +"-"+ahoraCalHasta.get(Calendar.DATE) ;
				else
					fechaNacHasta=ahoraCalHasta.get(Calendar.YEAR)+"-"+sash +"-"+ahoraCalHasta.get(Calendar.DATE) ;

				String fechaNacDesde= null;
				if(sasd <10)
					fechaNacDesde=ahoraCalDesde.get(Calendar.YEAR)+"-0"+sasd +"-"+ahoraCalDesde.get(Calendar.DATE) ;
				else
					fechaNacDesde=ahoraCalDesde.get(Calendar.YEAR)+"-"+sasd +"-"+ahoraCalDesde.get(Calendar.DATE) ;

				actionConditions.append(" and  (to_char(s111.fechayhoracreacion,'YYYY-MM-DD') >='"+fechaNacDesde +"'  and" +
								"   to_char(s111.fechayhoracreacion,'YYYY-MM-DD')<= '"+fechaNacHasta+"' )");
					
				
			}else 	if (fechaVentaD.getValue() != null) {
				Date stateTypeDesde= (fechaVentaD.getValue());
				
				Calendar ahoraCalDesde= Calendar.getInstance();
				ahoraCalDesde.setTime(stateTypeDesde);
				int sasdesde=ahoraCalDesde.get(Calendar.MONTH) +1;

				String fechaNacDesde= null;
				if(sasdesde <10)
					fechaNacDesde=ahoraCalDesde.get(Calendar.YEAR)+"-0"+sasdesde +"-"+ahoraCalDesde.get(Calendar.DATE) ;
				else
					fechaNacDesde=ahoraCalDesde.get(Calendar.YEAR)+"-"+sasdesde +"-"+ahoraCalDesde.get(Calendar.DATE) ;
				
				
				
				actionConditions.append(" and (to_char(s111.fechayhoracreacion,'YYYY-MM-DD') >='"+fechaNacDesde +"'  )");
				
			}else 	if (fechaVentaH.getValue() != null) {
				Date stateTypeHasta= (fechaVentaH.getValue());
				
				Calendar ahoraCalHasta= Calendar.getInstance();
				ahoraCalHasta.setTime(stateTypeHasta);
				int sashasta=ahoraCalHasta.get(Calendar.MONTH) +1;

				String fechaNacHasta= null;
				if(sashasta <10)
					fechaNacHasta=ahoraCalHasta.get(Calendar.YEAR)+"-0"+sashasta +"-"+ahoraCalHasta.get(Calendar.DATE) ;
				else
					fechaNacHasta=ahoraCalHasta.get(Calendar.YEAR)+"-"+sashasta +"-"+ahoraCalHasta.get(Calendar.DATE) ;
				
				actionConditions.append(" and  (to_char(s111.fechayhoracreacion,'YYYY-MM-DD')<= '"+fechaNacHasta+"' )");

			}
		
		actionConditions.append(" order by s111.fechayhoracreacion desc ");
		return actionConditions.toString(); 
	}
	
	public String getFiltersForExportingExcelClientes(String id, boolean order){
		StringBuilder actionConditions= new StringBuilder("select distinct clientexx.id, clientexx.apellido, clientexx.nombre, clientexx.fechanacimiento, " +
				"clientexx.celular, clientexx.telefonofijo, s111.fechayhoracreacion,s111.idusuariocreosubscripcion , " +
				" clientexx.dni, clientexx.fechanacimiento, clientexx.domicilio, clientexx.facebook, clientexx.mail   from cliente clientexx ");
		
					actionConditions.append(" inner join subscripcion s111 on (clientexx.id= s111.idcliente) ");
					actionConditions.append(" inner join cupoactividad ca11 on (ca11.idsubscripcion= s111.id) ");
					actionConditions.append(" where ca11.estado in (0, 1, 10)    ");
					
				if(usuarios != null && usuarios.getSelectedItem() != null 
							&& !((usuarios.getSelectedItem().getValue()) instanceof String)){
						com.institucion.fm.security.model.User u= ((com.institucion.fm.security.model.User)usuarios.getSelectedItem().getValue());
						actionConditions.append(" and  s111.idusuariocreosubscripcion=" +u.getId());
			
				}		

				if (nombre.getValue()!= null && !nombre.getValue().trim().equals("") && !nombre.getValue().trim().equals(" ")) {
					actionConditions.append(" and  Upper(clientexx.nombre) like Upper('%"+nombre.getValue().trim()+"%') ");
				}
				
				if (apellido.getValue()!= null && !apellido.getValue().trim().equals("") && !apellido.getValue().trim().equals(" ")) {
					actionConditions.append(" and  Upper(clientexx.apellido) like Upper('%"+apellido.getValue().trim()+"%') ");
				}

				if (curso.getSelectedIndex() >= 0) {
					Actividad stateType= ((Actividad)curso.getSelectedItem().getValue());
					actionConditions.append(" and "+ stateType.getId()  +" in " +
							"	(select con.idactividad from concepto con where con.idsubscripcion  in (select subs.id from subscripcion subs where subs.idcliente=clientexx.id )) ");
				}
	   
				if (fechaVentaD.getValue() != null && fechaVentaH.getValue() != null) {
					Date stateTypeDesde= (fechaVentaD.getValue());
					Date stateTypeHasta= (fechaVentaH.getValue());
					
					Calendar ahoraCalDesde= Calendar.getInstance();
					ahoraCalDesde.setTime(stateTypeDesde);
					
					Calendar ahoraCalHasta= Calendar.getInstance();
					ahoraCalHasta.setTime(stateTypeHasta);

					int sasd= ahoraCalDesde.get(Calendar.MONTH)+ 1;
					int sash= ahoraCalHasta.get(Calendar.MONTH)+ 1;
					String fechaNacHasta= null;
					if(sash <10)
						fechaNacHasta=ahoraCalHasta.get(Calendar.YEAR)+"-0"+sash +"-"+ahoraCalHasta.get(Calendar.DATE) ;
					else
						fechaNacHasta=ahoraCalHasta.get(Calendar.YEAR)+"-"+sash +"-"+ahoraCalHasta.get(Calendar.DATE) ;

					String fechaNacDesde= null;
					if(sasd <10)
						fechaNacDesde=ahoraCalDesde.get(Calendar.YEAR)+"-0"+sasd +"-"+ahoraCalDesde.get(Calendar.DATE) ;
					else
						fechaNacDesde=ahoraCalDesde.get(Calendar.YEAR)+"-"+sasd +"-"+ahoraCalDesde.get(Calendar.DATE) ;

					
					actionConditions.append(" and  (to_char(s111.fechayhoracreacion,'YYYY-MM-DD') >='"+fechaNacDesde +"'  and" +
									"   to_char(s111.fechayhoracreacion,'YYYY-MM-DD')<= '"+fechaNacHasta+"' )");
						
					
				}else 	if (fechaVentaD.getValue() != null) {
					Date stateTypeDesde= (fechaVentaD.getValue());
					
					Calendar ahoraCalDesde= Calendar.getInstance();
					ahoraCalDesde.setTime(stateTypeDesde);
					
					int sasdesde=ahoraCalDesde.get(Calendar.MONTH) +1;

					String fechaNacDesde= null;
					if(sasdesde <10)
						fechaNacDesde=ahoraCalDesde.get(Calendar.YEAR)+"-0"+sasdesde +"-"+ahoraCalDesde.get(Calendar.DATE) ;
					else
						fechaNacDesde=ahoraCalDesde.get(Calendar.YEAR)+"-"+sasdesde +"-"+ahoraCalDesde.get(Calendar.DATE) ;
					
					
					
					actionConditions.append(" and (to_char(s111.fechayhoracreacion,'YYYY-MM-DD') >='"+fechaNacDesde +"'  )");
					
				}else 	if (fechaVentaH.getValue() != null) {
					Date stateTypeHasta= (fechaVentaH.getValue());
					
					Calendar ahoraCalHasta= Calendar.getInstance();
					ahoraCalHasta.setTime(stateTypeHasta);
					int sashasta=ahoraCalHasta.get(Calendar.MONTH) +1;

					String fechaNacHasta= null;
					if(sashasta <10)
						fechaNacHasta=ahoraCalHasta.get(Calendar.YEAR)+"-0"+sashasta +"-"+ahoraCalHasta.get(Calendar.DATE) ;
					else
						fechaNacHasta=ahoraCalHasta.get(Calendar.YEAR)+"-"+sashasta +"-"+ahoraCalHasta.get(Calendar.DATE) ;
					
					actionConditions.append(" and  (to_char(s111.fechayhoracreacion,'YYYY-MM-DD')<= '"+fechaNacHasta+"' )");

				}
				if(id != null && id.equals("dias")){
					if(order){
						actionConditions.append(" order by s111.fechayhoracreacion desc");
		
					}else{
						actionConditions.append(" order by s111.fechayhoracreacion asc");
					}				
				}else
					actionConditions.append(" order by s111.fechayhoracreacion desc ");								
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
//
		c= usuarios.getConstraint();
		usuarios.setConstraint("");
		usuarios.setSelectedItem(null);
		usuarios.setConstraint(c);
//
		c= curso.getConstraint();
		curso.setConstraint("");
		curso.setSelectedItem(null);
		curso.setConstraint(c);
		
//		c= clases.getConstraint();
//		clases.setConstraint("");
//		clases.setSelectedItem(null);
//		clases.setConstraint(c);
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
		
		c= usuarios.getConstraint();
		usuarios.setConstraint("");
		usuarios.setSelectedItem(null);
		usuarios.setConstraint(c);

		c = apellido.getConstraint();
		apellido.setConstraint("");
		apellido.setText(null);
		apellido.setConstraint(c);

//
		c= curso.getConstraint();
		curso.setConstraint("");
		curso.setSelectedItem(null);
		curso.setConstraint(c);
		
//		c= clases.getConstraint();
//		clases.setConstraint("");
//		clases.setSelectedItem(null);
//		clases.setConstraint(c);	
		
		c= fechaVentaD.getConstraint();
		fechaVentaD.setConstraint("");
		fechaVentaD.setValue(null);
		fechaVentaD.setText(null);
		
		c= fechaVentaH.getConstraint();
		fechaVentaH.setConstraint("");
		fechaVentaH.setValue(null);
		fechaVentaH.setText(null);
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

//	public Combobox getClases() {
//		return clases;
//	}
//
//	public void setClases(Combobox clases) {
//		this.clases = clases;
//	}

	public ClienteDelegate getActionComposerDelegate() {
		return actionComposerDelegate;
	}

	public void setActionComposerDelegate(ClienteDelegate actionComposerDelegate) {
		this.actionComposerDelegate = actionComposerDelegate;
	}


	public Combobox getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(Combobox usuarios) {
		this.usuarios = usuarios;
	}

	public Datebox getFechaVentaD() {
		return fechaVentaD;
	}

	public void setFechaVentaD(Datebox fechaVentaD) {
		this.fechaVentaD = fechaVentaD;
	}

	public Datebox getFechaVentaH() {
		return fechaVentaH;
	}

	public void setFechaVentaH(Datebox fechaVentaH) {
		this.fechaVentaH = fechaVentaH;
	}

}