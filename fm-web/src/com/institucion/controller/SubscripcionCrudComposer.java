package com.institucion.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;
import org.jfree.util.Log;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;

import com.institucion.bz.CajaEJB;
import com.institucion.bz.ClaseEJB;
import com.institucion.bz.ClienteEJB;
import com.institucion.bz.CursoEJB;
import com.institucion.bz.InscripcionEJB;
import com.institucion.desktop.delegated.ClienteDelegate;
import com.institucion.desktop.delegated.CursosSeleccionadosDelegate;
import com.institucion.desktop.delegated.ListaDeCursosDelegate;
import com.institucion.desktop.delegated.SubscripcionDelegate;
import com.institucion.desktop.view.ToolbarFilters;
import com.institucion.desktop.view.ToolbarFiltersSubscripcion;
import com.institucion.fm.conf.Session;
import com.institucion.fm.conf.exception.WebExceptionHandler;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.CrudComposer;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.MessageBox;
import com.institucion.fm.desktop.view.PanelCrud;
import com.institucion.fm.desktop.view.PanelFilter;
import com.institucion.fm.security.model.User;
import com.institucion.model.Actividad;
import com.institucion.model.ActividadYClase;
import com.institucion.model.CajaMovimiento;
import com.institucion.model.Clase;
import com.institucion.model.Cliente;
import com.institucion.model.Concepto;
import com.institucion.model.CupoActividad;
import com.institucion.model.CupoActividadEstadoEnum;
import com.institucion.model.Curso;
import com.institucion.model.CursoListSeleccionados;
import com.institucion.model.CursoSubscripcionList;
import com.institucion.model.CursosFilter2;
import com.institucion.model.FormasDePagoCrud;
import com.institucion.model.FormasDePagoSubscripcionEnum;
import com.institucion.model.Inscripcion;
import com.institucion.model.Matricula;
import com.institucion.model.ObraSocial;
import com.institucion.model.ObraSocialesPrecio;
import com.institucion.model.PagosPorSubscripcion;
import com.institucion.model.Quincena;
import com.institucion.model.Subscripcion;
import com.institucion.model.SubscripcionAgregarEliminarBotonesFilter;
import com.institucion.model.SubscripcionAsignarBotonesFilter;
import com.institucion.model.SubscripcionContinuarCursoPorClaseBotonesFilter;
import com.institucion.model.SubscripcionCrud;
import com.institucion.model.SubscripcionCrud4;
import com.institucion.model.SubscripcionCumpleCrud;
import com.institucion.model.SubscripcionDeClases;
import com.institucion.model.SubscripcionDeClasesCrud;
import com.institucion.model.SubscripcionDeClasesPorActividad;
import com.institucion.model.SubscripcionPorClaseCrud;
import com.institucion.model.SubscripcionResponsableVentaCrud;
import com.institucion.model.SucursalEnum;
import com.institucion.model.TipoDescuentosEnum;
import com.institucion.model.TipoMovimientoCajaEnum;
import com.institucion.model.VencimientoCursoEnum;

public class SubscripcionCrudComposer extends CrudComposer implements SubscripcionDelegate, CursosSeleccionadosDelegate, ClienteDelegate, ListaDeCursosDelegate {
			// FALTA VER EL TEMA DE LOS LOS CANJES DE LOS PACIENTES QUE LLEGAN CON TEMAS MEDICOS
	private CursoEJB cursoEJB;
	DecimalFormat formateador = new DecimalFormat("###,###");

	private ClaseEJB claseEJB;
	private ClienteEJB clienteEJB;
//	private ObraSocialEJB obraSocialEJB;
	private InscripcionEJB subscripcionEJB;
	private CajaEJB cajaEJB;

	public final String idSubs = "idSubs";
	private Subscripcion subscripcion;
	private Cliente cliente;

	// crud del NOMBRE
	private PanelCrud crud;
	
	private PanelCrud responsableCrud;
	
	// List de los cursos
	private CursoSubscripcionList cursopanelListGrid;

	// List de los cursos pre-seleccionados
	private CursoListSeleccionados cursoseleccionadoListGrid;
	
	// mensaje para Avisar del pago de matriculas
	private Label matriculavencida;  // 'matricula.debe.vencida'
	private Label matriculadebe;   // 'matricula.debe'

	// crud Liquidacion
	private PanelCrud subs4crud;
	
	// crud formas de pago
	private FormasDePagoCrud crudFormasDePago;
	
	private PanelCrud agregarCursos; 
	private PanelCrud  botonAsignar;
	
	private Separator separadormatricula;
	// crud clases del curso 
	private PanelCrud subsDeClasecrud;
	private PanelCrud subsDeClasecrud2;
	private PanelCrud subsDeClasecrud3;
	private PanelCrud subsDeClasecrud4;
	private PanelCrud subsDeClasecrud5;
	
	private PanelCrud subscripcionporClasecrud;
	private PanelCrud  continuarPorClase;

	
	private Label subsDeClasecrudLabel; 
	private Label subsDeClasecrud2Label; 
	private Label subsDeClasecrud3Label; 
	private Label subsDeClasecrud4Label; 
	private Label subsDeClasecrud5Label; 
	
	
	private Separator subsDeClasecrudSeparador; 
	private Separator subsDeClasecrud2Separador; 
	private Separator subsDeClasecrud3Separador; 
	private Separator subsDeClasecrud4Separador; 
	private Separator subsDeClasecrud5Separador; 

	
	private PanelFilter filter;

	private ToolbarFiltersSubscripcion filters;
	private ToolbarFilters filters2;
	
	private List cursos;
	public Tabbox tabpanel;
	public Tab subscripcionCrudtab;
	public Tab clasesCrudtab;
	public Panel tituloListaCursos;
	
	private PanelCrud subscumplecrud;
	
	private Label debeadeudaTotal;
	
	
	public SubscripcionCrudComposer() {
		cursoEJB = BeanFactory.<CursoEJB>getObject("fmEjbCurso");
		subscripcionEJB = BeanFactory.<InscripcionEJB>getObject("fmEjbInscripcion");
		claseEJB = BeanFactory.<ClaseEJB>getObject("fmEjbClase");
		clienteEJB = BeanFactory.<ClienteEJB>getObject("fmEjbCliente");
		cajaEJB = BeanFactory.<CajaEJB>getObject("fmEjbCaja");
	}

	private CursosFilter2 getCursosFilter() {
		return (CursosFilter2)(filter.getGridFilter());
	}
	
	private SubscripcionPorClaseCrud getSubsPorClaseCrud() {
		return (SubscripcionPorClaseCrud) (subscripcionporClasecrud.getGridCrud());
	}

	
	private SubscripcionDeClasesCrud getSubsClaseCrud() {
		return (SubscripcionDeClasesCrud) (subsDeClasecrud.getGridCrud());
	}
	private SubscripcionDeClasesCrud getSubsClaseCrud2() {
		return (SubscripcionDeClasesCrud) (subsDeClasecrud2.getGridCrud());
	}
	private SubscripcionDeClasesCrud getSubsClaseCrud3() {
		return (SubscripcionDeClasesCrud) (subsDeClasecrud3.getGridCrud());
	}
	private SubscripcionDeClasesCrud getSubsClaseCrud4() {
		return (SubscripcionDeClasesCrud) (subsDeClasecrud4.getGridCrud());
	}
	private SubscripcionDeClasesCrud getSubsClaseCrud5() {
		return (SubscripcionDeClasesCrud) (subsDeClasecrud5.getGridCrud());
	}
	private SubscripcionAgregarEliminarBotonesFilter getAgregarCursosCrud() {
		return (SubscripcionAgregarEliminarBotonesFilter) (agregarCursos.getGridCrud());
	}
//	
	private SubscripcionAsignarBotonesFilter getAsignarCrud() {
		return (SubscripcionAsignarBotonesFilter) (botonAsignar.getGridCrud());
	}
	
	private SubscripcionContinuarCursoPorClaseBotonesFilter getContinuarCursoPorClaseCrud() {
		return (SubscripcionContinuarCursoPorClaseBotonesFilter) (continuarPorClase.getGridCrud());
	}

	
	private SubscripcionCrud getSubscripcionCrud() {
		return (SubscripcionCrud) (crud.getGridCrud());
	}
	
	private SubscripcionResponsableVentaCrud getSubscripcionResponsableCrud() {
		return (SubscripcionResponsableVentaCrud) (responsableCrud.getGridCrud());
	}
	
	private SubscripcionCrud4 getSubscripcion4Crud() {
		return (SubscripcionCrud4) (subs4crud.getGridCrud());
	}
	
	
	private SubscripcionCumpleCrud getSubscripcionCumpleCrud() {
		return (SubscripcionCumpleCrud) (subscumplecrud.getGridCrud());
	}
	

	
	public void onCreate() {
		Subscripcion clase= (Subscripcion) Session.getAttribute(idSubs);
		setSubscripcion(clase);	

		Session.setAttribute("claseDeSubs", null);
		Session.setAttribute("esNueva", null);
		Cliente cliente= (Cliente) Session.getAttribute("idCliente");
		setCliente(cliente);	
		
		if(clase != null && clase.getIdUsuarioCreoSubscripcion() != null){
			User u=subscripcionEJB.findByName(clase.getIdUsuarioCreoSubscripcion());
			String fechaNac= null;
			if(clase.getFechaYHoraCreacion() != null){
				Calendar ahoraCal= Calendar.getInstance();
				ahoraCal.setTime(clase.getFechaYHoraCreacion());
				int mes=ahoraCal.get(Calendar.MONTH) + 1;

				fechaNac=ahoraCal.get(Calendar.DATE)+"/"+mes+"/"+ahoraCal.get(Calendar.YEAR);
			}
			if(fechaNac != null){
				if(u != null)
					getSubscripcionResponsableCrud().getNombreLabel().setValue(fechaNac + " - "+ u.getName());
			}else{
				if(u != null)
					getSubscripcionResponsableCrud().getNombreLabel().setValue(u.getName());	
			}
			
		}else{
			if(clase != null){
				
			}else{
				// es nueva
				getSubscripcionResponsableCrud().getNombreLabel().setValue(Session.getUsername());
			}
		}
		getCursosFilter() .setActionComposerDelegate(this);
		getSubscripcionCrud().getCliente().setValue(cliente.getApellido().toUpperCase() + " "+ cliente.getNombre().toUpperCase());
		getSubscripcionCrud().getComentario().setVisible(false);
		cursopanelListGrid.setDelegate(this);
		debeadeudaTotal.setVisible(false);
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

//		cumpleaniosActualizarTotales.setVisible(false);
		cursos=cursoEJB.findAll2WithDisponible(esMaipu, esCentro);
		if(cursos != null)
			cursopanelListGrid.setList(cursos, true, null);

		cursoseleccionadoListGrid.setDelegate(this);
		matriculavencida.setVisible(false);
		matriculadebe.setVisible(false);
		separadormatricula.setVisible(false);

		// cumpleanios
		subscumplecrud.setVisible(false);

		getSubsPorClaseCrud().setDelegate(this);
		getAsignarCrud() .setDelegate(this);
		crudFormasDePago.setSubsdelegate(this);
		getAgregarCursosCrud().setDelegate(this);

		subscripcionporClasecrud.setVisible(false);
		continuarPorClase.setVisible(false);
		
		getSubsClaseCrud().setVisible(false);
		getSubsClaseCrud2().setVisible(false);
		getSubsClaseCrud3().setVisible(false);
		getSubsClaseCrud4().setVisible(false);
		getSubsClaseCrud5().setVisible(false);
		getContinuarCursoPorClaseCrud().setDelegate(this);
		subsDeClasecrudLabel.setVisible(false);
		subsDeClasecrud2Label.setVisible(false);
		subsDeClasecrud3Label.setVisible(false);
		subsDeClasecrud4Label.setVisible(false);
		subsDeClasecrud5Label.setVisible(false);
		
		
		subsDeClasecrudLabel.setStyle("font-weight:bold");
		subsDeClasecrud2Label.setStyle("font-weight:bold");
		subsDeClasecrud3Label.setStyle("font-weight:bold");
		subsDeClasecrud4Label.setStyle("font-weight:bold");
		subsDeClasecrud5Label.setStyle("font-weight:bold");
		
		
		subsDeClasecrudSeparador.setVisible(false);
		subsDeClasecrud2Separador.setVisible(false);
		subsDeClasecrud3Separador.setVisible(false);
		subsDeClasecrud4Separador.setVisible(false);
		subsDeClasecrud5Separador.setVisible(false);

		filter.getInnerPanel().setOpen(true);
		filter.setOpen(true);
		
		this.fromModelToView();


		if(subscripcion != null){
			// a
			crudFormasDePago.getGridCrud().setVisible(false);
			
			
			boolean tieneCumples= false;
			for (Concepto concepto : subscripcion.getConceptoList()) {
				Curso curso =concepto.getCurso();
				if(curso != null && curso.getNombre().contains("CUMPLEA")){
					tieneCumples= true;
				}
			}	
			
			if(tieneCumples){
				clasesCrudtab.setVisible(false);
			}
			// si es una modificacion 
			boolean tienecursosConvencimientosQuincenales=false;
			for (Concepto concepto : subscripcion.getConceptoList()) {
				
				if(concepto.getCurso() != null && concepto.getCurso().getVencimiento() != null &&
						concepto.getCurso().getVencimiento().equals(VencimientoCursoEnum.QUINCENAL_VENCE_A_LA_QUINCENA))
					tienecursosConvencimientosQuincenales= true;
			}
			
			if(subscripcion.getDetalleCumple() != null && !subscripcion.getDetalleCumple().trim().equalsIgnoreCase("")){ 
				getSubscripcionCumpleCrud().setDetalleCumple(subscripcion.getDetalleCumple());
				subscumplecrud.setVisible(true);
				
				filters.getButtonAbonar().setVisible(true);
				filters.getButtonAbonar().setLabelAndTooltip("Guardar Cambios");

//				getAgregarCursosCrud().getGuardarButton().setVisible(true);
				getAgregarCursosCrud().getAgregarButton().setVisible(false);
				getAgregarCursosCrud().getEliminarButton().setVisible(false);
			}else  if(tienecursosConvencimientosQuincenales){
				filters.getButtonAbonar().setVisible(true);
				filters.getButtonAbonar().setLabelAndTooltip("Guardar Cambios");
//				getAgregarCursosCrud().getGuardarButton().setVisible(true);
			}else
				filters.setVisiblePagar(false);
		}
		refreshEvents();
		clasesCrudtab.addEventListener(Events.ON_CLICK, new EventListener() {
			public void onEvent(Event evt) {			
				
				if(cursoseleccionadoListGrid.getSelectedItem() != null){
					
					if(((Concepto)cursoseleccionadoListGrid.getSelectedItem().getValue()).getCurso() == null){
						MessageBox.validation(I18N.getLabel("curso.error.seleccion.select"), I18N.getLabel("selector.actionwithoutitem.title"));
						tabpanel.setSelectedIndex(0);
						return;
					}else{

						asignarClasesYIr2Pantalla((Concepto)cursoseleccionadoListGrid.getSelectedItem().getValue());
						
					}	
				
			}else{
				MessageBox.validation(I18N.getLabel("curso.error.seleccion.select"), I18N.getLabel("selector.actionwithoutitem.title"));
				tabpanel.setSelectedIndex(0);
				return;
			}
		}		
	  });	
	}
	
	
	private void refreshEvents(){
		
//		getCursosFilter().getNombre().addEventListener(Events.ON_OK, new EventListener() {
//			public void onEvent(Event evt){
//				onFind();
//			}
//		});	

	}

	public void onMatriculaGratis(Event event) throws Exception {
		
		com.institucion.fm.conf.Session.setAttribute("idCliente", getCliente());
		com.institucion.fm.conf.Session.setAttribute("idClienteFromSubs", true);
		super.gotoPage("/institucion/matricula-gratis-crud.zul");
		
	}

	public void onAbonar(Event event) throws Exception {
		pagarCurso();		
	}
	
	
	
	public void asignarClasesYIr2Pantalla(Concepto concepto){
		// verificar si tiene algun curso de tipo natacion o no.
		Curso curso = concepto.getCurso();
		boolean tiene= false;
		for (ActividadYClase actYCl : curso.getActividadYClaseList()) {
			
			if(actYCl.getActiv().isUsaCarnet() 
					&&  (curso != null && curso.getVencimiento().toInt() == VencimientoCursoEnum.SEMANAL_VENCE_A_LA_SEMANA.toInt()
					||  curso.getVencimiento().toInt() == VencimientoCursoEnum.SEMANAL_VENCE_AL_MES.toInt()
					||  curso.getVencimiento().toInt() == VencimientoCursoEnum.SEMANAL_VENCE_AL_ANo.toInt()
					||  curso.getVencimiento().toInt() == VencimientoCursoEnum.SEMANAL_VENCE_A_LOS_3_MES.toInt())){ 
				tiene= true;
			}
		}
		if(!tiene){
			MessageBox.validation(I18N.getLabel("curso.error.seleccion.select"), I18N.getLabel("selector.actionwithoutitem.title"));
			tabpanel.setSelectedIndex(0);
			return;
		}
	
	subsDeClasecrudLabel.setValue("");
	subsDeClasecrud2Label.setValue("");
	subsDeClasecrud3Label.setValue("");
	subsDeClasecrud4Label.setValue("");
	subsDeClasecrud5Label.setValue("");
	
	getSubsClaseCrud().limpiar();
	getSubsClaseCrud2().limpiar();
	getSubsClaseCrud3().limpiar();
	getSubsClaseCrud4().limpiar();
	getSubsClaseCrud5().limpiar();
									
	Map <Long, SubscripcionDeClases> mapaSubsPorClase=(HashMap)Session.getAttribute("claseDeSubs");
	
	if(mapaSubsPorClase != null && mapaSubsPorClase.size() >0 ){
		// valido si encuentro la actividad
		
		if(mapaSubsPorClase.containsKey(curso.getId())){
			SubscripcionDeClases sub =mapaSubsPorClase.get(curso.getId());
			actualizoDatos(sub);
		
		}else{
			seteoLaPantallaComoNuevaLasClases(curso);
		}
		
	}else{
		// significa que no tengo ninguna clase guardada en memoria		
		seteoLaPantallaComoNuevaLasClases(curso);			
	}
	tabpanel.setSelectedIndex(1);	
	
	}
	
	public void onFind(String nombre) {
		Logger log=Logger.getLogger(this.getClass());
		log.info("Creando listado de farmacia en la version modificada");
		cursos= new ArrayList<Curso>();
//		getCursosFilter().desabilitarDisponible();
		
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
		// hago la consulta jdbc con los filtros, devuelvo IDs, y despues hago la busqueda por ids con hibernate
		cursos =cursoEJB.findAllConJdbc(getCursosFilter().getFilters(true, esMaipu, esCentro, nombre));	

		cursopanelListGrid.setList(cursos, true, cursoseleccionadoListGrid.getList(false));
	}
	
	public void onClearFilter(Event evt){
		getCursosFilter().clear();
		this.onFind(null);
	}
	
	
	private void setearDatosClasesPantallaTab(int ei, SubscripcionDeClasesPorActividad subsPorAct, SubscripcionDeClasesCrud crud, 
			Label subsDeClasecrudLabel, Curso curso, Separator separador){
		
		Integer cantClases=(Integer)crud.getNombreActividad().getAttribute("cantidadClases");
	
		crud.setVisible(true);
		subsDeClasecrudLabel.setVisible(true);
		separador.setVisible(true);
		int cantidadClases=0;
		for (ActividadYClase iterable_element : curso.getActividadYClaseList()) {
			if(iterable_element.getActiv().getId().intValue() ==subsPorAct.getActividad().getId().intValue() ){
				cantidadClases= iterable_element.getCantClases();
				
			}
		}
		
		if(cantClases != null)
			cantidadClases=cantClases;
		
		crud.getNombreActividad().setAttribute("actividad", subsPorAct.getActividad());
		getSubsClaseCrud().getNombreActividad().setAttribute("cantidadClases", curso.getCantClasesPorSemana());
		crud.getNombreActividad().setValue(subsPorAct.getActividad().getNombre());
		subsDeClasecrudLabel.setValue(subsPorAct.getActividad().getNombre() + " - Cantidad de Clases semanales del curso: "+cantidadClases + " . Seleccione los dias que participará: ");
		
		if(subsPorAct.isLunes()){
			List<Clase> clases=claseEJB.findAllByActividad(new Long(subsPorAct.getActividad().getId()), 1);
			crud.getComboBox(crud.getHoraDesdeLunes(), clases);	
			crud.setSelectedLunesClase(subsPorAct.getClaseLunes());
			crud.getComboBox(crud.getHoraDesdeMartes(), claseEJB.findAllByActividad(new Long(subsPorAct.getActividad().getId()), 2));
			crud.getComboBox(crud.getHoraDesdeMiercoles(), claseEJB.findAllByActividad(new Long(subsPorAct.getActividad().getId()), 3));
			crud.getComboBox(crud.getHoraDesdeJueves(), claseEJB.findAllByActividad(new Long(subsPorAct.getActividad().getId()), 4));
			crud.getComboBox(crud.getHoraDesdeViernes(), claseEJB.findAllByActividad(new Long(subsPorAct.getActividad().getId()), 5));
			crud.getComboBox(crud.getHoraDesdeSabado(), claseEJB.findAllByActividad(new Long(subsPorAct.getActividad().getId()), 6));

//			crud.getHoraDesdeLunes().setDisabled(false);
		}	
		if(subsPorAct.isMartes()){
			List<Clase> clases=claseEJB.findAllByActividad(new Long(subsPorAct.getActividad().getId()), 2);
			crud.getComboBox(crud.getHoraDesdeMartes(), clases);
			crud.setSelectedMartesClase(subsPorAct.getClaseMartes());
			
			crud.getComboBox(crud.getHoraDesdeLunes(), claseEJB.findAllByActividad(new Long(subsPorAct.getActividad().getId()), 1));
			crud.getComboBox(crud.getHoraDesdeMiercoles(), claseEJB.findAllByActividad(new Long(subsPorAct.getActividad().getId()), 3));
			crud.getComboBox(crud.getHoraDesdeJueves(), claseEJB.findAllByActividad(new Long(subsPorAct.getActividad().getId()), 4));
			crud.getComboBox(crud.getHoraDesdeViernes(), claseEJB.findAllByActividad(new Long(subsPorAct.getActividad().getId()), 5));
			crud.getComboBox(crud.getHoraDesdeSabado(), claseEJB.findAllByActividad(new Long(subsPorAct.getActividad().getId()), 6));
			
			
//			crud.getHoraDesdeMartes().setDisabled(false);
		}
		if(subsPorAct.isMiercoles()){
			List<Clase> clases=claseEJB.findAllByActividad(new Long(subsPorAct.getActividad().getId()), 3);
			crud.getComboBox(crud.getHoraDesdeMiercoles(), clases);
			crud.setSelectedMiercolesClase(subsPorAct.getClaseMiercoles());
//			crud.getHoraDesdeMiercoles().setDisabled(false);
			
			crud.getComboBox(crud.getHoraDesdeLunes(), claseEJB.findAllByActividad(new Long(subsPorAct.getActividad().getId()), 1));
			crud.getComboBox(crud.getHoraDesdeMartes(), claseEJB.findAllByActividad(new Long(subsPorAct.getActividad().getId()), 2));
			crud.getComboBox(crud.getHoraDesdeJueves(), claseEJB.findAllByActividad(new Long(subsPorAct.getActividad().getId()), 4));
			crud.getComboBox(crud.getHoraDesdeViernes(), claseEJB.findAllByActividad(new Long(subsPorAct.getActividad().getId()), 5));
			crud.getComboBox(crud.getHoraDesdeSabado(), claseEJB.findAllByActividad(new Long(subsPorAct.getActividad().getId()), 6));

		}
		if(subsPorAct.isJueves()){
			List<Clase> clases=claseEJB.findAllByActividad(new Long(subsPorAct.getActividad().getId()), 4);
			crud.getComboBox(crud.getHoraDesdeJueves(), clases);
			crud.setSelectedJuevesClase(subsPorAct.getClaseJueves());
//			crud.getHoraDesdeJueves().setDisabled(false);
			crud.getComboBox(crud.getHoraDesdeLunes(), claseEJB.findAllByActividad(new Long(subsPorAct.getActividad().getId()), 1));
			crud.getComboBox(crud.getHoraDesdeMartes(), claseEJB.findAllByActividad(new Long(subsPorAct.getActividad().getId()), 2));
			crud.getComboBox(crud.getHoraDesdeMiercoles(), claseEJB.findAllByActividad(new Long(subsPorAct.getActividad().getId()), 3));
			crud.getComboBox(crud.getHoraDesdeViernes(), claseEJB.findAllByActividad(new Long(subsPorAct.getActividad().getId()), 5));
			crud.getComboBox(crud.getHoraDesdeSabado(), claseEJB.findAllByActividad(new Long(subsPorAct.getActividad().getId()), 6));

		}
		if(subsPorAct.isViernes()){
			List<Clase> clases=claseEJB.findAllByActividad(new Long(subsPorAct.getActividad().getId()), 5);
			crud.getComboBox(crud.getHoraDesdeViernes(), clases);
			crud.setSelectedViernesClase(subsPorAct.getClaseViernes());
//			crud.getHoraDesdeViernes().setDisabled(false);
			crud.getComboBox(crud.getHoraDesdeLunes(), claseEJB.findAllByActividad(new Long(subsPorAct.getActividad().getId()), 1));
			crud.getComboBox(crud.getHoraDesdeMartes(), claseEJB.findAllByActividad(new Long(subsPorAct.getActividad().getId()), 2));
			crud.getComboBox(crud.getHoraDesdeMiercoles(), claseEJB.findAllByActividad(new Long(subsPorAct.getActividad().getId()), 3));
			crud.getComboBox(crud.getHoraDesdeJueves(), claseEJB.findAllByActividad(new Long(subsPorAct.getActividad().getId()), 4));
			crud.getComboBox(crud.getHoraDesdeSabado(), claseEJB.findAllByActividad(new Long(subsPorAct.getActividad().getId()), 6));

		}
		if(subsPorAct.isSabado()){
			List<Clase> clases=claseEJB.findAllByActividad(new Long(subsPorAct.getActividad().getId()), 6);
			crud.getComboBox(crud.getHoraDesdeSabado(), clases);
			crud.setSelectedSabadoClase(subsPorAct.getClaseSabado());
//			crud.getHoraDesdeSabado().setDisabled(false);
			crud.getComboBox(crud.getHoraDesdeLunes(), claseEJB.findAllByActividad(new Long(subsPorAct.getActividad().getId()), 1));
			crud.getComboBox(crud.getHoraDesdeMartes(), claseEJB.findAllByActividad(new Long(subsPorAct.getActividad().getId()), 2));
			crud.getComboBox(crud.getHoraDesdeMiercoles(), claseEJB.findAllByActividad(new Long(subsPorAct.getActividad().getId()), 3));
			crud.getComboBox(crud.getHoraDesdeJueves(), claseEJB.findAllByActividad(new Long(subsPorAct.getActividad().getId()), 4));
			crud.getComboBox(crud.getHoraDesdeViernes(), claseEJB.findAllByActividad(new Long(subsPorAct.getActividad().getId()), 5));

		}			
	}
	
	private void actualizoDatos(SubscripcionDeClases subsClases){
		int i=0;

		for (SubscripcionDeClasesPorActividad subsPorAct : subsClases.getClaseSeleccionadasList()) {
		
			if(i == 0){
				setearDatosClasesPantallaTab(i, subsPorAct,getSubsClaseCrud(), subsDeClasecrudLabel, subsClases.getCurso(), subsDeClasecrudSeparador );
			}
			if(i == 1){
				setearDatosClasesPantallaTab(i, subsPorAct,getSubsClaseCrud2(),   subsDeClasecrud2Label, subsClases.getCurso(), subsDeClasecrud2Separador);
			}
			if(i == 2){
				setearDatosClasesPantallaTab(i, subsPorAct,getSubsClaseCrud3(), subsDeClasecrud3Label , subsClases.getCurso(), subsDeClasecrud3Separador);
			}
			if(i == 3){
				setearDatosClasesPantallaTab(i, subsPorAct,getSubsClaseCrud4() , subsDeClasecrud4Label, subsClases.getCurso(), subsDeClasecrud4Separador);
			}
			if(i == 4){
				setearDatosClasesPantallaTab(i, subsPorAct,getSubsClaseCrud5(), subsDeClasecrud5Label , subsClases.getCurso(), subsDeClasecrud5Separador);
			}
			i++;
		}		
	}
	
	
	public void seteoLaPantallaComoNuevaLasClases(Curso curso){
		int i=0;
		for (ActividadYClase actYCl : curso.getActividadYClaseList()) {
				
			Integer cantClases=curso.getCantClasesPorSemana();
//			Integer cantClases=(Integer)crud.getNombreActividad().getAttribute("cantidadClases");
	
			int cantidadClases=actYCl.getCantClases();
			
			if(cantClases != null)
				cantidadClases=cantClases;
		
			if(actYCl.getActiv().isUsaCarnet() 
					&&  (curso.getVencimiento().toInt() == VencimientoCursoEnum.SEMANAL_VENCE_A_LA_SEMANA.toInt()
							||  curso.getVencimiento().toInt() == VencimientoCursoEnum.SEMANAL_VENCE_AL_MES.toInt()
							||  curso.getVencimiento().toInt() == VencimientoCursoEnum.SEMANAL_VENCE_AL_ANo.toInt()
							||  curso.getVencimiento().toInt() == VencimientoCursoEnum.SEMANAL_VENCE_A_LOS_3_MES.toInt())){
								
				if(i == 0){
					getSubsClaseCrud().setVisible(true);
					subsDeClasecrudLabel.setVisible(true);
					subsDeClasecrudSeparador.setVisible(true);

					subsDeClasecrudLabel.setValue(actYCl.getActiv().getNombre()  + " - Cantidad de Clases semanales del curso: "+cantidadClases + " . Seleccione los dias que participará: ");
					getSubsClaseCrud().getNombreActividad().setValue(actYCl.getActiv().getNombre());
					getSubsClaseCrud().getNombreActividad().setAttribute("actividad", actYCl.getActiv());
					getSubsClaseCrud().getNombreActividad().setAttribute("cantidadClases", cantidadClases);
					getSubsClaseCrud().completarCombosDeHorarios();
				}
				if(i == 1){
					getSubsClaseCrud2().setVisible(true);
					subsDeClasecrud2Label.setVisible(true);
					subsDeClasecrud2Separador.setVisible(true);
					subsDeClasecrud2Label.setValue(actYCl.getActiv().getNombre()+ " - Cantidad de Clases semanales del curso: "+cantidadClases + " . Seleccione los dias que participará: ");
					getSubsClaseCrud2().getNombreActividad().setValue(actYCl.getActiv().getNombre());
					getSubsClaseCrud2().getNombreActividad().setAttribute("actividad", actYCl.getActiv());
					getSubsClaseCrud2().getNombreActividad().setAttribute("cantidadClases", cantidadClases);
					getSubsClaseCrud2().completarCombosDeHorarios();

				}
				if(i == 2){
					getSubsClaseCrud3().setVisible(true);
					subsDeClasecrud3Label.setVisible(true);
					subsDeClasecrud3Separador.setVisible(true);
					subsDeClasecrud3Label.setValue(actYCl.getActiv().getNombre() + " - Cantidad de Clases semanales del curso: "+cantidadClases + " . Seleccione los dias que participará: ");
					getSubsClaseCrud3().getNombreActividad().setValue(actYCl.getActiv().getNombre());
					getSubsClaseCrud3().getNombreActividad().setAttribute("actividad", actYCl.getActiv());
					getSubsClaseCrud3().getNombreActividad().setAttribute("cantidadClases", cantidadClases);
					getSubsClaseCrud3().completarCombosDeHorarios();

				}
				if(i == 3){
					getSubsClaseCrud4().setVisible(true);
					subsDeClasecrud4Label.setVisible(true);
					subsDeClasecrud4Separador.setVisible(true);
					subsDeClasecrud4Label.setValue(actYCl.getActiv().getNombre() + " - Cantidad de Clases semanales del curso: "+cantidadClases + " . Seleccione los dias que participará: ");
					getSubsClaseCrud4().getNombreActividad().setValue(actYCl.getActiv().getNombre());
					getSubsClaseCrud4().getNombreActividad().setAttribute("actividad", actYCl.getActiv());
					getSubsClaseCrud4().getNombreActividad().setAttribute("cantidadClases", cantidadClases);
					getSubsClaseCrud4().completarCombosDeHorarios();
				}
				if(i == 4){
					getSubsClaseCrud5().setVisible(true);
					subsDeClasecrud5Label.setVisible(true);
					subsDeClasecrud5Separador.setVisible(true);
					subsDeClasecrud5Label.setValue(actYCl.getActiv().getNombre() + " - Cantidad de Clases semanales del curso: "+cantidadClases + " . Seleccione los dias que participará: ");
					getSubsClaseCrud5().getNombreActividad().setValue(actYCl.getActiv().getNombre());
					getSubsClaseCrud5().getNombreActividad().setAttribute("actividad", actYCl.getActiv());
					getSubsClaseCrud5().getNombreActividad().setAttribute("cantidadClases", cantidadClases);
					getSubsClaseCrud5().completarCombosDeHorarios();

				}

				i++;
			}
		}	
		
	}

	// se debe actualizar el combo de abajo de descuentos
	public void onClickSubscripcionesEvt(Event event) throws Exception{
		getSubsPorClaseCrud().getDiasQueParticipara().setValue(1);
//		getSubsPorClaseCrud().getImporteTotalCurso().setValue(0);
		subscripcionporClasecrud.setVisible(false);
		continuarPorClase.setVisible(false);
	}

	public boolean esModificacion(){
		if(subscripcion != null){
			return true;
		}
		return false;	
	}

	
	private boolean tieneAnuladoAlgo(Subscripcion subscripcion){
		
		if(subscripcion!= null && subscripcion.getCupoActividadList() != null ){
			for (CupoActividad cupos : subscripcion.getCupoActividadList()) {
				if(cupos.getEstado().toInt() == CupoActividadEstadoEnum.ANULADA.toInt()){
					return true;
				}
			}
		}
		return false;
	}
	
	private void fromModelToView() {
		
		if(subscripcion != null){
			subscripcion=clienteEJB.loadLazy(subscripcion, true, true, true, true, true);

			Session.setAttribute("esNueva", true);

			if(tieneAnuladoAlgo(subscripcion)){
				getSubscripcionCrud().getComentario().setVisible(true);
				getSubscripcionCrud().getComentarioLabel().setVisible(true);

				String fecha= "";
				if(subscripcion.getFechaYHoraAnulacion() != null){
					Calendar ahoraCal= Calendar.getInstance();
					ahoraCal.setTime(subscripcion.getFechaYHoraAnulacion());
					int mes=ahoraCal.get(Calendar.MONTH)+ 1;
					fecha=ahoraCal.get(Calendar.DATE)+"/"+mes+"/"+ahoraCal.get(Calendar.YEAR);
				}
					
				String textosComentario= "Fecha Anulación: "+ fecha +" .Valor Devuelto "+ subscripcion.getAnulaValorDevuelvo()
													+". Comentario: "+subscripcion.getAnulaComentario() ;
				
				getSubscripcionCrud().getComentario().setValue(textosComentario);
		
			}else{
				getSubscripcionCrud().getComentario().setVisible(false);
				getSubscripcionCrud().getComentarioLabel().setVisible(false);
			}


			if(subscripcion.getFechaYHoraSaldaSubscripcion() != null && subscripcion.getIdUsuarioSaldaSubscripcion() != null){
				getSubscripcionCrud().getComentarioAdeuda().setVisible(true);	
				getSubscripcionCrud().getComentarioAdeudaLabel().setVisible(true);
				
				String fecha= "";
				if(subscripcion.getFechaYHoraSaldaSubscripcion() != null){
					Calendar ahoraCal= Calendar.getInstance();
					ahoraCal.setTime(subscripcion.getFechaYHoraSaldaSubscripcion());
					int mes=ahoraCal.get(Calendar.MONTH)+ 1;
					fecha=ahoraCal.get(Calendar.DATE)+"/"+mes+"/"+ahoraCal.get(Calendar.YEAR);
				}	
				String textosComentario= "Se saldo la deuda: "+ fecha;
				getSubscripcionCrud().getComentarioAdeuda().setValue(textosComentario);

			}
			
			
			if(subscripcion.getCupoActividadList() != null ){
				
				String comentarioPosponer=null;

				for (CupoActividad iterable_element : subscripcion.getCupoActividadList()) {

					// muestro el campo Comentario Posponer
					if(iterable_element.getPosponeMeses() != null || iterable_element.getPosponeMeses() != null){
						getSubscripcionCrud().getComentarioPosponer().setVisible(true);
						getSubscripcionCrud().getComentarioPosponerLabel().setVisible(true);

						comentarioPosponer="Curso: "+iterable_element.getCurso().getNombre().toUpperCase();
						
						if(iterable_element.getPosponeMeses() != null){
							
							String fecha= "";
							if(iterable_element.getPosponefechaYHora() != null){
								Calendar ahoraCal= Calendar.getInstance();
								ahoraCal.setTime(iterable_element.getPosponefechaYHora());
								int mes=ahoraCal.get(Calendar.MONTH)+ 1;
								fecha=ahoraCal.get(Calendar.DATE)+"/"+mes+"/"+ahoraCal.get(Calendar.YEAR);
							}
							comentarioPosponer= comentarioPosponer + " - 1 vez Pospuso Venc Fecha: "+ fecha  ;
							
							
							if(	iterable_element.getPosponePagaAdicional() != null && iterable_element.getPosponePagaAdicional()){
								comentarioPosponer= comentarioPosponer+ ". Paga adicional $"+formateador.format(iterable_element.getPosponeAdicional());
							}
							
							comentarioPosponer= comentarioPosponer+". Comentario: "+iterable_element.getPosponeComentario()   
									+". Aprobado por:"+iterable_element.getPosponeAprobadoPor();						
						}
						
						if(iterable_element.getPosponeMeses2() != null){
							
							String fecha= "";
							if(iterable_element.getPosponefechaYHora2() != null){
								Calendar ahoraCal= Calendar.getInstance();
								ahoraCal.setTime(iterable_element.getPosponefechaYHora2());
								int mes=ahoraCal.get(Calendar.MONTH)+ 1;
								fecha=ahoraCal.get(Calendar.DATE)+"/"+mes+"/"+ahoraCal.get(Calendar.YEAR);
							}
							comentarioPosponer= comentarioPosponer +"- 2 vez Pospuso Vencimiento Fecha: "+ fecha  ;
							
							
							if(	iterable_element.getPosponePagaAdicional2() != null && iterable_element.getPosponePagaAdicional2()){
								comentarioPosponer= comentarioPosponer+ ". Paga adicional $"+formateador.format(iterable_element.getPosponePagaAdicional2());
							}
							
							comentarioPosponer= comentarioPosponer+". Comentario: "+iterable_element.getPosponeComentario2()   
									+". Aprobado por:"+iterable_element.getPosponeAprobadoPor2();
						}

					}
				}
				
				if(comentarioPosponer != null)
					getSubscripcionCrud().getComentarioPosponer().setValue(comentarioPosponer);

				else{
					getSubscripcionCrud().getComentarioPosponer().setVisible(false);
					getSubscripcionCrud().getComentarioPosponerLabel().setVisible(false);
				}
			}
				
			// deshabilitar el panel de curso
			cursopanelListGrid.setVisible(false);
			tituloListaCursos.setVisible(false);
			
			// deshabilitar combo tipo de pago
//			getSubscripcion5Crud().getComboDescuentos().setDisabled(true);
			
			// deshabilitar botones agregar curso y eliminar curso.
			getAgregarCursosCrud().getAgregarButton().setVisible(false);
			getAgregarCursosCrud().getEliminarButton().setVisible(false);
//			getAgregarCursosCrud().getAsignarButton().setDisabled(true);
			
			// dehsbilitar combo aplica descuento  dehsbilitar boton abonar y pagar
			crudFormasDePago.getGridList().setDisabled(true);
			crudFormasDePago.getPagoRapido().setDisabled(true);
			crudFormasDePago.setButtonsVisible(false);

			
			// si es cumpleanios o quincnal 
			if(subscripcion.getDetalleCumple() != null && !subscripcion.getDetalleCumple().trim().equalsIgnoreCase("")){
				clasesCrudtab.setVisible(false);
				filters.setVisiblePagar(false);
//
//				getPagarCursosCrud().setVisible(false);
			}
			filters.setVisiblePagar(false);
//			
//			getPagarCursosCrud().setVisible(false);
//			getSubscripcion5Crud().setVisible(false);

			if(subscripcion.getCliente() != null)
				getSubscripcionCrud().getCliente().setValue(cliente.getApellido().toUpperCase() + " "+ cliente.getNombre().toUpperCase());

			getSubscripcion4Crud().getPrecio().setValue("$"+formateador.format(subscripcion.getPrecioCursosMatricula()));
			getSubscripcion4Crud().getPrecioTOTALadicionalTarjeta().setValue("$"+formateador.format(subscripcion.getPrecioTOTALadicionalTarjeta()));
			getSubscripcion4Crud().getAdicionalTarjeta().setValue("$"+formateador.format(subscripcion.getValorAdicionaltarjeta()));
	
			fillListBoxConceptos(subscripcion);

			// si tiene cursos con vencimiento quincenal habilito el boton guardar cambios
				
			fillListBoxProducts(subscripcion);
			
			getSubscripcion4Crud().getPrecio().setValue("$"+formateador.format(subscripcion.getPrecioCursosMatricula()));
			getSubscripcion4Crud().getPrecioTOTALadicionalTarjeta().setValue("$"+formateador.format(subscripcion.getPrecioTOTALadicionalTarjeta()));
			getSubscripcion4Crud().getAdicionalTarjeta().setValue("$"+formateador.format(subscripcion.getValorAdicionaltarjeta()));
			
//			// si se saldo alguna deuda
			if(subscripcion.getIdUsuarioSaldaSubscripcion() != null && subscripcion.getFechaYHoraSaldaSubscripcion() != null){
				
			}
			
			Map <Long, SubscripcionDeClases>	mapaSubsPorClase = (HashMap<Long, SubscripcionDeClases> ) new HashMap();

			for (SubscripcionDeClases subs : subscripcion.getSubscripcionDeClases()) {
				guardarSubscripcionPorClasesNuevasFromModelToView(subs, mapaSubsPorClase);	

				
			}
			filter.getInnerPanel().setVisible(false);
			filter.setVisible(false);
			
			filters2.setEnabledFind(false);
			filters2.setEnabledOnCleanFilter(false);	
			
			int importe=obtenerImporteFinalDeLaLista(null);
			if(importe > 0 ){
				crudFormasDePago.setVisible(true);		
			}else
				crudFormasDePago.setVisible(false);
	
	
			// 
			int faltantei=-1;
			try{
				if(crudFormasDePago.getValorFaltantePesos().getValue()!= null 
						&& crudFormasDePago.getValorFaltantePesos().getValue().endsWith(",0")){
					String sas=crudFormasDePago.getValorFaltantePesos().getValue().substring(0, crudFormasDePago.getValorFaltantePesos().getValue().indexOf(","));
					faltantei= Integer.parseInt(sas);
				}else{
					faltantei=Integer.parseInt(crudFormasDePago.getValorFaltantePesos().getValue());	
				}
			}catch(Exception e){
				
			}
			if(faltantei > 0 && faltantei  < importe ){
				debeadeudaTotal.setVisible(true);
				debeadeudaTotal.setValue("DEBE EN TOTAL: $"+ formateador.format(faltantei));
			}
			
			if(subscripcion.getDetalleCumple() != null && !subscripcion.getDetalleCumple().trim().equalsIgnoreCase("")){ 
				getSubscripcionCumpleCrud().setDetalleCumple(subscripcion.getDetalleCumple());
				subscumplecrud.setVisible(true);
				
				filters.getButtonAbonar().setVisible(true);
				filters.getButtonAbonar().setLabelAndTooltip("Guardar Cambios");

//				getAgregarCursosCrud().getGuardarButton().setVisible(true);
				getAgregarCursosCrud().getAgregarButton().setVisible(false);
				getAgregarCursosCrud().getEliminarButton().setVisible(false);
			}
		}else{
			// cuando la subscripcion es nueva 
			crudFormasDePago.setVisible(false);
		}			
	}
	
	private void fillListBoxConceptos(Subscripcion subscripcion) {
		if (subscripcion != null) {
			
			for (Concepto dpp : subscripcion.getConceptoList()) {
				cursoseleccionadoListGrid.setList(dpp, true);
			}
		}
	}
	
	private void fillListBoxProducts(Subscripcion subscripcion) {
		Set<PagosPorSubscripcion> result = new HashSet<PagosPorSubscripcion>();
		if (subscripcion != null) {
		
			for (PagosPorSubscripcion dpp : subscripcion.getPagosPorSubscripcionList()) {
				result.add(dpp);
			}
			
			if(subscripcion.getPagosPorSubscripcionList() != null && subscripcion.getPagosPorSubscripcionList().size() ==0){
				// actualizo el rojo y el verde de la plata, no se hizo ningun pago aca.
				int importeFinal=obtenerImporteFinalDeLaLista(null);
				
				crudFormasDePago.getValorFaltantePesos().setValue(String.valueOf(importeFinal));
				crudFormasDePago.getValorSumadoPesos().setValue("0");
	
								
			}
			
		}
		crudFormasDePago.fillListBox(result);
	}
	

	public void onSave(Event event) throws Exception {
		try{
			if(filters.getButtonAbonar() != null && filters.getButtonAbonar().isVisible() &&
					filters.getButtonAbonar().getLabel().equalsIgnoreCase("Guardar Cambios")){
				guardarCambios();
			}else{
				// es nuevo 
				if(subscripcion != null){
					
				}else{
					subscripcion= new Subscripcion();
				}
				Subscripcion aaaa=this.fromViewToModel(subscripcion);
				
				if(aaaa != null && validateOnSaveCrud()){
					
					if(getSubscripcionCumpleCrud().getDetalleCumple() != null && !getSubscripcionCumpleCrud().getDetalleCumple().trim().equalsIgnoreCase("")){
						aaaa.setDetalleCumple(getSubscripcionCumpleCrud().getDetalleCumple());
					}

					// debe registrar el pago y la subscripcion.
					if(guardarMatricula()){
						if(!guardarInscripcion()){
							MessageBox.validation(I18N.getLabel("curso.error.problema.al.guardar"), I18N.getLabel("selector.actionwithoutitem.title"));
							return;
						}else{
							// debe generar un comprobante de pago
							MessageBox.info(I18N.getLabel("curso.guardado"), I18N.getLabel("selector.actionwithoutitem.title.realizada"));
							
						}		
						
					}else{
						MessageBox.validation(I18N.getLabel("curso.error.problema.al.guardar"), I18N.getLabel("selector.actionwithoutitem.title"));
						return;
					}
			
					if (Sessions.getCurrent().getAttribute("isSubsFromIngresoInscripcion") != null){
						Sessions.getCurrent().setAttribute("isSubsFromIngresoInscripcion", null);
						super.gotoPage("/institucion/ingresoInscripcion-selector.zul");
					}else if(com.institucion.fm.conf.Session.getAttribute("isSubsFromDeudasClientes") != null){	
						Sessions.getCurrent().setAttribute("isSubsFromDeudasClientes", null);
						super.gotoPage("/institucion/deuda-cliente-selector.zul");
					}else if(com.institucion.fm.conf.Session.getAttribute("isSubsFromDeudasClientes") != null){	
						Sessions.getCurrent().setAttribute("isSubsFromDeudasClientes", null);
						super.gotoPage("/institucion/deuda-cliente-selector.zul");
					}else	
						super.gotoPage("/institucion/clientes-selector.zul");
				}
			}
		} catch (EJBException ex) {
			WebExceptionHandler.handleThis(ex);
		}
	}
		
	
	private Integer getConceptoObraSocialYPrecio(Subscripcion subs, CupoActividad cupos ){
		
		if(subs.getConceptoList() != null){
			for (Concepto iterable_element : subs.getConceptoList()) {
				
				if(iterable_element.getCurso() != null 
						&& iterable_element.getCurso().getId().intValue() ==  cupos.getCurso().getId().intValue()){
					
					if(iterable_element.getConcepto() != null && !iterable_element.getConcepto().contains("Copago")){

							if(iterable_element.getTipoDescuento().toInt() == TipoDescuentosEnum.OBRA_SOCIAL.toInt()){
								return 10;
							}else{
									return null;
							}
					}
				}
			}
		}
		return null;
	}
	
	public boolean guardarInscripcion(){
		
		try{
			// valida los estados y los setea
			int debePagar= subscripcion.getPrecioTOTALadicionalTarjeta();
			
			double pagosRealizados=0;
			if(crudFormasDePago.getProducts() != null && crudFormasDePago.getProducts().size() > 0){
				for (PagosPorSubscripcion pagoPorSubscripcion: crudFormasDePago.getProducts()) {
					if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_15.toInt()){
						pagosRealizados= pagosRealizados+ pagoPorSubscripcion.getCantidadDinero();
						pagosRealizados= pagosRealizados+ pagoPorSubscripcion.getAdicional();
					}else if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_20.toInt()){
						pagosRealizados= pagosRealizados+ pagoPorSubscripcion.getCantidadDinero();
						pagosRealizados= pagosRealizados+ pagoPorSubscripcion.getAdicional();
					}else{
						pagosRealizados= pagosRealizados+ pagoPorSubscripcion.getCantidadDinero();
					}		
				}
			}
				
			if(pagosRealizados >= debePagar ){
				if(subscripcion.getCupoActividadList() != null ){
					for (CupoActividad cupos : subscripcion.getCupoActividadList()) {
						if(cupos.getCupos() > 0)
							cupos.setEstado(CupoActividadEstadoEnum.C_CUPOS);
						else
							cupos.setEstado(CupoActividadEstadoEnum.S_CUPOS);
					}
				}
			}else if(pagosRealizados < debePagar ){
				if(subscripcion.getCupoActividadList() != null ){
					for (CupoActividad cupos : subscripcion.getCupoActividadList()) {
						
						Integer cantidad= getConceptoObraSocialYPrecio(subscripcion, cupos);
						if(cantidad != null && cantidad.intValue() >0 ){
							if(cupos.getCupos() > 0){
								cupos.setEstado(CupoActividadEstadoEnum.C_CUPOS);
							}else{
								cupos.setEstado(CupoActividadEstadoEnum.S_CUPOS);
							}
							
						}else{
							if(cupos.getCupos() > 0){
								cupos.setEstado(CupoActividadEstadoEnum.C_DEUDAS_Y_C_CUPOS);
							}else{
								cupos.setEstado(CupoActividadEstadoEnum.ADEUDA_Y_SIN_CUPOS);
							}				
						}
					}
				}
			}
			
			
			
			

			subscripcion.setFechaIngresoSubscripcion(new Date());	
			subscripcion.setFechaYHoraCreacion(new Date());
			subscripcion.setIdUsuarioCreoSubscripcion(Session.getUsernameID().intValue());
			
			subscripcion.setSubscripcionDeClases(null);
			Long subsId=subscripcionEJB.createSubs(subscripcion);
			Subscripcion subs= subscripcionEJB.findSubscripcionById(subsId);
			
			subs=clienteEJB.loadLazy(subs, false, true, true, true, false);
			subscripcion=subs;
			
			List <SubscripcionDeClases> listaSubsDeClases= null;
			if(subscripcion.getSubscripcionDeClases() != null)
				listaSubsDeClases = new ArrayList(subscripcion.getSubscripcionDeClases());

			if(listaSubsDeClases != null && listaSubsDeClases.size() >0){
				for (SubscripcionDeClases object : listaSubsDeClases) {
					object.setSubscripcion(subs);
					if(object.getClaseSeleccionadasList() != null){
						for (SubscripcionDeClasesPorActividad iterable_element : object.getClaseSeleccionadasList()) {
							iterable_element.setSubscripcion2(subs);				
						}	
					}		
				}
				if(listaSubsDeClases != null)
					subs.setSubscripcionDeClases(new HashSet(listaSubsDeClases));
			}
			

			if(subs.getPagosPorSubscripcionList() != null){
				for (PagosPorSubscripcion subscripcionDeClases : subs.getPagosPorSubscripcionList() ) {
					subscripcionDeClases.setSucursal(getSucursal(subs));	
					
					// falta actualizar la quincena
					
					if(subscripcionDeClases.getSucursal() != null  &&
							subscripcionDeClases.getSucursal().toInt() == SucursalEnum.MAIPU.toInt()){
						Quincena quin=obtenerQuincenaDeConceptos(subs);
						if(quin != null){
							subscripcionDeClases.setQuincena(quin);	
						}
					}
				}
			}
			
			subscripcionEJB.save(subs);
			boolean tieneDescuentosOS= false;
			boolean tieneDescuentosGE= false;
			String act= "";
			if(subscripcion.getConceptoList() != null){
				for (Concepto concep : subscripcion.getConceptoList()) {
					if(concep.getTipoDescuento().toInt() == TipoDescuentosEnum.OBRA_SOCIAL.toInt()){
						tieneDescuentosOS= true;
						
						if(concep.getActividadDelConcepto() != null){
							act= act  + "  "+  concep.getActividadDelConcepto().getNombre() ;
							
							if(concep.getComentarioDescuento() != null && !concep.getComentarioDescuento().trim().equalsIgnoreCase("")){
								act= act +" :  "+concep.getComentarioDescuento()+ ".  ";
							}

						}
					
					}
					if(concep.getTipoDescuento().toInt() == TipoDescuentosEnum.GENERAL.toInt()){
						tieneDescuentosGE= true;
						if(concep.getActividadDelConcepto() != null){
							act= act + "  "+ concep.getActividadDelConcepto().getNombre() ;
							
							if(concep.getComentarioDescuento() != null && !concep.getComentarioDescuento().trim().equalsIgnoreCase("")){
								act= act +" : "+concep.getComentarioDescuento()+ ".  ";
							}
	
						}					
					}
				}
			} 
			// Guardo el movimiento en la caja ahora
			CajaMovimiento caja= new CajaMovimiento();
			
			String cursos=obtenerCursosContratados(subscripcion);
			// si tiene matricula sumar al concepto matricula
			if(cursoseleccionadoListGrid.existeMatriculaEnLista())
				caja.setConcepto("Nueva: Matricula + Inscripcion Cursos: "+cursos);
			else
				caja.setConcepto("Nueva: Inscripcion Cursos: "+cursos);
			
			if(tieneDescuentosOS || tieneDescuentosGE)
				caja.setConcepto(caja.getConcepto() + ". "+ act);

			caja.setFecha(subscripcion.getFechaYHoraCreacion());
			caja.setIdUsuarioGeneroMovimiento(subscripcion.getIdUsuarioCreoSubscripcion());
			caja.setTipoMovimiento(TipoMovimientoCajaEnum.INGRESO);
			caja.setValor(pagosRealizados);
			caja.setSucursal(getSucursal(subscripcion));

			caja.setCliente(subscripcion.getCliente());
			caja.setIdSubscripcion(subsId.intValue());

			cajaEJB.save(caja);
			
			// Si se aplico descuento por obraSocial, genero en la caja el movimiento EN ALGUN MOMENTO LE VA A PAGAR
			// Guardo el movimiento en la caja ahora
			
			if(subscripcion.getConceptoList() != null){
				for (Concepto concep : subscripcion.getConceptoList()) {
					if(concep.getTipoDescuento().toInt() == TipoDescuentosEnum.OBRA_SOCIAL.toInt()){
					
						CajaMovimiento caja2= new CajaMovimiento();
						String cursos2=concep.getCurso().getNombre().toUpperCase();
						// si tiene matricula sumar al concepto matricula
						caja2.setConcepto("A abonar por Obra Social "+ concep.getObraSocial().getNombre().toUpperCase() + " : "+cursos2);
						
						caja2.setFecha(subscripcion.getFechaYHoraCreacion());
						caja2.setIdUsuarioGeneroMovimiento(subscripcion.getIdUsuarioCreoSubscripcion());
						caja2.setTipoMovimiento(TipoMovimientoCajaEnum.INGRESO);
						double valor=concep.getCantSesiones() * concep.getPrecioPorClaseOSesionPagaObraSocial();
						caja2.setValor(valor);
						caja.setSucursal(getSucursal(subscripcion));

						caja2.setCliente(subscripcion.getCliente());
						caja2.setIdSubscripcion(subsId.intValue());

						cajaEJB.save(caja2);						
					}
				}
			}
			
//			Aca deberia de llamar a generar comprobante de pago
//			String conceptos=null;
//			boolean imprimeComprobante= false;
//			boolean tieneObraSocial= false;
			if(subscripcion.getConceptoList() != null){
				for (Concepto concep : subscripcion.getConceptoList()) {
					if(concep.getTipoDescuento().toInt() == TipoDescuentosEnum.OBRA_SOCIAL.toInt()){
//						tieneObraSocial= true;
						CajaMovimiento caja2= new CajaMovimiento();
						String cursos2=concep.getCurso().getNombre().toUpperCase();
						// si tiene matricula sumar al concepto matricula
						caja2.setConcepto("A abonar por Obra Social "+ concep.getObraSocial().getNombre().toUpperCase() + " : "+cursos2);
						
						caja2.setFecha(subscripcion.getFechaYHoraCreacion());
						caja2.setIdUsuarioGeneroMovimiento(subscripcion.getIdUsuarioCreoSubscripcion());
						caja2.setTipoMovimiento(TipoMovimientoCajaEnum.INGRESO);
						double valor=concep.getCantSesiones() * concep.getPrecioPorClaseOSesionPagaObraSocial();
						caja2.setValor(valor);
						caja.setSucursal(getSucursal(subscripcion));

						caja2.setCliente(subscripcion.getCliente());
						caja2.setIdSubscripcion(subsId.intValue());

						cajaEJB.save(caja2);
					}
//					if(concep.getCurso() != null && concep.getActividadDelConcepto() != null){
//						if(conceptos != null){
//							conceptos= conceptos+"/"+ concep.getActividadDelConcepto().getNombre();	
//						}else
//							conceptos= concep.getActividadDelConcepto().getNombre();
//						
//						if(concep.getActividadDelConcepto().isImprimeComprobante())
//							imprimeComprobante= true;
//					}
				}
			}
			Boolean bool=cursoEJB.findImprimible();

			if(bool != null && bool.booleanValue()){
				int precioMatricula=0;
				if(subscripcion.getConceptoList() != null){
					for (Concepto concep : subscripcion.getConceptoList()) {
						if(concep.getCurso() == null){
							precioMatricula=concep.getImporteConDescuento();
						}
					}
				}
				int precioDelCurso=0;
				double pagosRealizadosTotales= pagosRealizados;
				String aaaa=null;;
				if(subscripcion.getConceptoList() != null){
					for (Concepto concep : subscripcion.getConceptoList()) {
						boolean esObraSocial=false;
						if(concep.getTipoDescuento().toInt() == TipoDescuentosEnum.OBRA_SOCIAL.toInt()){
							esObraSocial= true;
						}

						if(concep.getCurso() != null && concep.getCurso().isPagaSubscripcion()){
							if(precioMatricula >0){
								precioDelCurso=concep.getImporteConDescuento();
								precioDelCurso= precioDelCurso+ precioMatricula;
								precioMatricula=0;
							}else{
								precioDelCurso=concep.getImporteConDescuento();
							}
						}else if(concep.getCurso() != null && !concep.getCurso().isPagaSubscripcion()){
							precioDelCurso=concep.getImporteConDescuento();
						}
						
						if(concep.getCurso() != null){
							// tengo que pagar precioDelCurso
							double nuevoVal=0;
							if(pagosRealizadosTotales > precioDelCurso){
								nuevoVal=precioDelCurso;
							}else if(pagosRealizadosTotales <= precioDelCurso){
								nuevoVal=pagosRealizadosTotales;
							}
							if(pagosRealizadosTotales > 0)
								pagosRealizadosTotales= pagosRealizadosTotales-nuevoVal;

							String aa=Imprimir.imprimirComprobante("Comprob de Actividad", subscripcion.getCliente(), 
									concep.getActividadDelConcepto().getNombre(), esObraSocial, String.valueOf(nuevoVal), null,
									concep.getActividadDelConcepto().isImprimeComprobante(), concep.getCurso().getIdTipoCurso(), 0, false);
							if(aaaa == null){
								aaaa= aa;	
							}else{
								aaaa= aaaa+"<br/>" + aa;	
							}
							
						}
					}
				}
				if(aaaa != null){
					Imprimir.imprimirComprobante(aaaa); 
				}
			}
			
			Cliente cli=subs.getCliente();
			if(cli != null){
				
				List<Subscripcion> listSubs=subscripcionEJB.findAllSubscripcionesByClient(cli.getId());
				if(listSubs != null)
					cli.setSubscripcionesList(new HashSet(listSubs));
				
				// guardar cliente
				clienteEJB.save(cli);
			}
			return true;
		}catch (Exception e) {
			Log.error("SubscripCrudCompo  "+ e.toString());
			return false;
		}

	}

	
	private Quincena obtenerQuincenaDeConceptos(Subscripcion subs){
		if(subs != null && subs.getConceptoList() != null){
			
			for (Concepto iterable_element : subs.getConceptoList()) {
				if(iterable_element.getQuincena() != null)
					return iterable_element.getQuincena(); 
			}
		}
	return null;	
	}
	
	private String obtenerCursosContratados(Subscripcion subscripcion){
		String cursos="";
		if(subscripcion.getConceptoList() != null){
			for (Concepto concep : subscripcion.getConceptoList()) {
				if(cursos.equalsIgnoreCase("")){
					if(concep.getCurso() != null)
						cursos= concep.getCurso().getNombre().toUpperCase();
				}else{
					if(concep.getCurso() != null)
						cursos= cursos+ ", "+concep.getCurso().getNombre().toUpperCase();
				}
			}
		}
		return "("+cursos +")";
	}
	
	public boolean guardarMatricula(){

		try{
			// debe registrar el pago de la matricula
			if(cursoseleccionadoListGrid.existeMatriculaEnLista()){
				
				Inscripcion insc= new Inscripcion();
				insc.setCliente(cliente);
				
				Calendar cal = Calendar.getInstance();
		        insc.setFecha(cal.getTime());
		        
				cal.add(Calendar.YEAR, 1);    //Adding 1 year to current date
				insc.setFechaVencimiento(cal.getTime());
				insc.setMatriculaGratis(false);

				insc.setSaldoAbonado(cursoseleccionadoListGrid.obtenerSaldoMatriculaEnLista());
				subscripcionEJB.save(insc);
				return true;
			}else
				return true;
		}catch(Exception e){
			return false;
		}
	}
	
	
	public void onBack(Event event) {
		if(com.institucion.fm.conf.Session.getAttribute("isFromCumples") != null){
			Sessions.getCurrent().setAttribute("isFromCumples", null);
			super.gotoPage("/institucion/cumples-selector.zul");
		}else if (Sessions.getCurrent().getAttribute("isSubsFromIngresoInscripcion") != null){ 
			Sessions.getCurrent().setAttribute("isSubsFromIngresoInscripcion", null);
			super.gotoPage("/institucion/ingresoInscripcion-selector.zul");
		}else if(com.institucion.fm.conf.Session.getAttribute("isSubsFromDeudasClientes") != null){	
			Sessions.getCurrent().setAttribute("isSubsFromDeudasClientes", null);
			super.gotoPage("/institucion/deuda-cliente-selector.zul");
		}else	
			super.gotoPage("/institucion/clientes-selector.zul");
	}


	private Subscripcion fromViewToModel(Subscripcion subs) {

		if(cliente != null)
			subscripcion.setCliente(cliente);	
		
		try{
			boolean esModiidfcacion= false;
			if(subscripcion != null && subscripcion.getId() != null){
				esModiidfcacion= true;
			}

		if(cursoseleccionadoListGrid.getList(esModiidfcacion) != null && cursoseleccionadoListGrid.getList(esModiidfcacion).size()>0)
			subscripcion.setConceptoList(new HashSet(cursoseleccionadoListGrid.getList(esModiidfcacion)));
		}catch(WrongValueException ee){
			MessageBox.validation("Debe seleccionar la quincena/cumpleaños dentro del concepto", I18N.getLabel("selector.actionwithoutitem.title"));
			return null;
		}
		if(crudFormasDePago.getProducts() != null && crudFormasDePago.getProducts().size()>0)
			subscripcion.setPagosPorSubscripcionList(crudFormasDePago.getProducts());		
		
		if(getSubscripcion4Crud().getPrecioTOTALadicionalTarjeta() != null){
			int precio= 0;
			if(getSubscripcion4Crud().getPrecioTOTALadicionalTarjeta().getValue().startsWith("$") && 
					getSubscripcion4Crud().getPrecioTOTALadicionalTarjeta().getValue().length() >1){
				String monto=getSubscripcion4Crud().getPrecioTOTALadicionalTarjeta().getValue().substring(1);
				if(monto.contains(",")){
					monto= monto.replace(",", "");
				}

				precio= Integer.parseInt(monto);	
			}
			subscripcion.setPrecioTOTALadicionalTarjeta(precio);
		}
		if(getSubscripcion4Crud().getAdicionalTarjeta() != null){
			int precio= 0;
			if(getSubscripcion4Crud().getAdicionalTarjeta().getValue().startsWith("$") && getSubscripcion4Crud().getAdicionalTarjeta().getValue().length() >1){
				String monto=getSubscripcion4Crud().getAdicionalTarjeta().getValue().substring(1);
				if(monto.contains(",")){
					monto= monto.replace(",", "");
				}

				precio= Integer.valueOf(monto);	
			}
			subscripcion.setValorAdicionaltarjeta(precio);
			
		}		
		
		Set <CupoActividad> cupoList = new HashSet ();
		// Por cada actividad, le seteo los cupos. Creando por cada uno un Objeto CupoActividad
		if(subscripcion != null && subscripcion.getConceptoList() != null){
			for (Concepto concepto : subscripcion.getConceptoList()) {
				
				if(	concepto.getCurso() != null){
					
					if(concepto.getCurso().getActividadYClaseList() != null){
						
						for (ActividadYClase actYClase : concepto.getCurso().getActividadYClaseList()) {
							CupoActividad cupo = new CupoActividad();
							cupo.setActividad(actYClase.getActiv());
							cupo.setCurso(concepto.getCurso());
							cupo.setSubscripcion(subscripcion);
							
							VencimientoCursoEnum vencimientoEnum= (VencimientoCursoEnum) concepto.getCurso().getVencimiento() ;
							if(vencimientoEnum.toInt() == VencimientoCursoEnum.LIBRE_VENCE_A_LA_QUINCENA.toInt() || 
									vencimientoEnum.toInt() == VencimientoCursoEnum.LIBRE_VENCE_A_LA_SEMANA.toInt() ||
									vencimientoEnum.toInt() == VencimientoCursoEnum.LIBRE_VENCE_A_LOS_3_MES.toInt() ||
									vencimientoEnum.toInt() == VencimientoCursoEnum.LIBRE_VENCE_AL_MES.toInt()){
								
								// creo el objeto seteando el numero 99 en la cantidad de cupos
								cupo.setCupos(99);	
								
							}else{
								if(vencimientoEnum.toInt() == VencimientoCursoEnum.QUINCENAL_VENCE_A_LA_QUINCENA.toInt()){
									
									if(concepto.getCantSesiones() >0)
										cupo.setCupos(concepto.getCantSesiones());
									else
										cupo.setCupos(actYClase.getCantClases());
								}else{
									
									if(concepto.getCantSesiones() >0)
										cupo.setCupos(concepto.getCantSesiones());
									else
										cupo.setCupos(actYClase.getCantClases());
								}
							}
							cupoList.add(cupo);
						}
					}
				}
				// guardo matricula
				int precioCursos=0;
				if(subscripcion.getPrecioCursosMatricula() != null)
					precioCursos=subscripcion.getPrecioCursosMatricula();
				
				subscripcion.setPrecioCursosMatricula(precioCursos +concepto.getImporteConDescuento());
			}	
		}
		
		if(cupoList != null && cupoList.size() >0)
			subscripcion.setCupoActividadList(cupoList);
		
		Map <Long, SubscripcionDeClases> mapaSubsPorClase=(HashMap)Session.getAttribute("claseDeSubs");
		if(mapaSubsPorClase != null && mapaSubsPorClase.size() > 0){
			for (SubscripcionDeClases iterable_element : mapaSubsPorClase.values()) {
				
				iterable_element.setSubscripcion(subscripcion);

				if(iterable_element.getClaseSeleccionadasList() != null){
					if(iterable_element.getClaseSeleccionadasList() != null){
						for (SubscripcionDeClasesPorActividad porAct : iterable_element.getClaseSeleccionadasList()) {
							//porAct.setIdsubscripcion(subsId);
							porAct.setSubscripcion2(subscripcion);
							porAct.setCurso(iterable_element.getCurso());
						}
					}
				}
				
			}	
			subscripcion.setSubscripcionDeClases(new HashSet(mapaSubsPorClase.values()));
		}
		
		return subs;
	}

	
	private void validateContinuarAgregandoCursoPorClase(){
		
		if(getSubsPorClaseCrud().getDiasQueParticipara().getValue() == null)
			throw new WrongValueException(getSubsPorClaseCrud().getDiasQueParticipara(), I18N.getLabel("error.empty.field"));
		else if(getSubsPorClaseCrud().getDiasQueParticipara().getValue() != null 
				&& getSubsPorClaseCrud().getDiasQueParticipara().getValue() <= 0)
			throw new WrongValueException(getSubsPorClaseCrud().getDiasQueParticipara(), "Se debe ingresar una cantidad mayor a 0");
	}


	public void continuarAgregandoCumpleanios(){
		subscumplecrud.setVisible(true);
	}

	public void continuarAgregandoCursoPorClase(){

		validateContinuarAgregandoCursoPorClase();
		int valorCurso=getValorOriginalCurso();
		Integer importe=valorCurso *  getSubsPorClaseCrud().getDiasQueParticipara().getValue().intValue() ;
		
		if(importe != null){
			
//			FALTA ACTUALIZAR LA CANTIDAD DE CLASES EN CUPO
			
			agregarCurso(true, importe);		
			// tengo que llamar al boton Agregarcurso / tratamiento para que siga funcionando normalmente
			subscripcionporClasecrud.setVisible(false);
			continuarPorClase.setVisible(false);
		}
	}
	
	
	private boolean validateParaModificarDiaCurso(){
		// validar que tenga las clases
		Map <Long, SubscripcionDeClases> mapaSubsPorClase=(HashMap)Session.getAttribute("claseDeSubs");
		
		// valido si tengo actividades que usan carnet
		
		for (Concepto concepto : subscripcion.getConceptoList()) {
			
			Curso curso =concepto.getCurso();
			
			if(curso != null && curso.getActividadYClaseList() != null){
				for (ActividadYClase actYCl : curso.getActividadYClaseList()) {
					
					if(actYCl.getActiv().isUsaCarnet() 
							&&  (curso.getVencimiento().toInt() == VencimientoCursoEnum.SEMANAL_VENCE_A_LA_SEMANA.toInt()
								||  curso.getVencimiento().toInt() == VencimientoCursoEnum.SEMANAL_VENCE_AL_MES.toInt()
								||  curso.getVencimiento().toInt() == VencimientoCursoEnum.SEMANAL_VENCE_AL_ANo.toInt()
								||  curso.getVencimiento().toInt() == VencimientoCursoEnum.SEMANAL_VENCE_A_LOS_3_MES.toInt())){ 
						
						if((mapaSubsPorClase != null && mapaSubsPorClase.size() > 0) ||
								(mapaSubsPorClase != null && mapaSubsPorClase.get(curso.getId()) != null)){
							// valido que tenga las clases seteadas
							SubscripcionDeClases subsClase=mapaSubsPorClase.get(curso.getId());
							
							if(subsClase.getClaseSeleccionadasList() == null){
								MessageBox.validation(I18N.getLabel("curso.error.seleccion.select.error.select.asignar.clases.cursos"),
										I18N.getLabel("selector.actionwithoutitem.title"));
								tabpanel.setSelectedIndex(1);
								return false;
							}
							for (SubscripcionDeClasesPorActividad iterable_element : subsClase.getClaseSeleccionadasList()) {
								if(iterable_element.getActividad().getId().intValue() == actYCl.getActiv().getId().intValue()){
										// ahora se que es el curso seleccionado, para la actividad seleccionada, que tiene carnet.
										// entonces valido aca si hay seteadas las mismas cantidad de clases y correctamente.
									
									Integer cantClasesQueDebeTener=curso.getCantClasesPorSemana();
									if(cantClasesQueDebeTener == null)
										cantClasesQueDebeTener=actYCl.getCantClases();
									
									if(!validarCantidadDiasSeleccionados(new ArrayList(subsClase.getClaseSeleccionadasList()), cantClasesQueDebeTener)){
										// Para el curso .., se debe asignar correctamente todas las clases
										Object[] params3 =  {curso.getNombre().toUpperCase()};	

										MessageBox.validation(I18N.getLabel("curso.error.seleccion.select.error.select.asignar.clases.curso", params3 ),
												I18N.getLabel("selector.actionwithoutitem.title"));
										tabpanel.setSelectedIndex(1);
										return false;
									}
								}
							}
							
						}else{
							MessageBox.validation(I18N.getLabel("curso.error.seleccion.select.error.select.asignar.clases"), I18N.getLabel("selector.actionwithoutitem.title"));
							tabpanel.setSelectedIndex(1);
							return false;
						}
						
					}
				}			
				
			}
			// DEBERIA ACA ARROJAR ERROR ??? INDIcANDO QuE SE DEBEN SELECCIONAR LAS CLASES ?????
			
			
		}
		return true;
	}
	
	private boolean  validateOnSaveCrud() {
		
	
		// validar que tenga por lo menos 1 concepto.
		if((cursoseleccionadoListGrid.getItems()) != null && (cursoseleccionadoListGrid.getItems().size() >0)){
			
		}else{
			// error, se debe agregar por lo menos 1 concepto a facturar.
			MessageBox.validation(I18N.getLabel("curso.error.seleccion.select.error.select"), I18N.getLabel("selector.actionwithoutitem.title"));
			tabpanel.setSelectedIndex(1);
			return false;
		}
		
		// validar que si hay cumpleanios, solo exista ese concepto
		boolean tieneCumples= false;
		for (Concepto concepto : subscripcion.getConceptoList()) {
			Curso curso =concepto.getCurso();
			if(curso != null && curso.getNombre().contains("CUMPLEA")){
				tieneCumples= true;
			}
		}		
		
		// si tiene cumpleaños 
		if(tieneCumples){
			if(getSubscripcionCumpleCrud().getDetalleCumple() == null || 
					(getSubscripcionCumpleCrud().getDetalleCumple() != null && getSubscripcionCumpleCrud().getDetalleCumple().trim().equalsIgnoreCase(""))){
				MessageBox.validation("Complete el campo Detalle cumpleaños con los datos importantes del mismo.", I18N.getLabel("selector.actionwithoutitem.title"));
				return false;	
			}
		}
		subscumplecrud.setVisible(true);

		if(tieneCumples && subscripcion.getConceptoList().size() >1){
			MessageBox.validation(" En ventas de cumpleaños solo se permite el concepto del cumpleaños.", I18N.getLabel("selector.actionwithoutitem.title"));
			return false;	
		}
		
		// validar si tiene ventas de ini
		boolean tieneVentasINI= false;
		boolean tieneOtrosCursosINI= false;

		for (Concepto concepto : subscripcion.getConceptoList()) {
			Curso curso =concepto.getCurso();
			if(curso != null  
					&& (curso.getNombre().equalsIgnoreCase("INI VERANO-TRANSPORTE")  
							|| curso.getNombre().equalsIgnoreCase("INI VERANO-CANTINA")
							|| curso.getNombre().equalsIgnoreCase("INI VERANO")))
				tieneVentasINI= true;
		}		
		
		
		if(tieneVentasINI){
			for (Concepto concepto : subscripcion.getConceptoList()) {
				Curso curso =concepto.getCurso();
				if(curso != null  
						&& !curso.getNombre().equalsIgnoreCase("INI VERANO-TRANSPORTE")  
						&& !curso.getNombre().equalsIgnoreCase("INI VERANO-CANTINA")
						&& !curso.getNombre().equalsIgnoreCase("INI VERANO"))
					tieneOtrosCursosINI= true;
			}		
			
		} 
		
		if(tieneVentasINI && tieneOtrosCursosINI){
			MessageBox.validation(" En una misma venta, no se permiten vender cursos de Maipu y Centro.", I18N.getLabel("selector.actionwithoutitem.title"));
			return false;	
		}

		
		// validar que tenga las clases
		Map <Long, SubscripcionDeClases> mapaSubsPorClase=(HashMap)Session.getAttribute("claseDeSubs");
		
		// valido si tengo actividades que usan carnet
		
		for (Concepto concepto : subscripcion.getConceptoList()) {
			
			Curso curso =concepto.getCurso();
			
			if(curso != null && curso.getActividadYClaseList() != null){
				for (ActividadYClase actYCl : curso.getActividadYClaseList()) {
					
					if(actYCl.getActiv().isUsaCarnet() 
							&&  (curso.getVencimiento().toInt() == VencimientoCursoEnum.SEMANAL_VENCE_A_LA_SEMANA.toInt()
								||  curso.getVencimiento().toInt() == VencimientoCursoEnum.SEMANAL_VENCE_AL_MES.toInt()
								||  curso.getVencimiento().toInt() == VencimientoCursoEnum.SEMANAL_VENCE_AL_ANo.toInt()
								||  curso.getVencimiento().toInt() == VencimientoCursoEnum.SEMANAL_VENCE_A_LOS_3_MES.toInt())){ 
						
						if((mapaSubsPorClase != null && mapaSubsPorClase.size() > 0) ||
								(mapaSubsPorClase != null && mapaSubsPorClase.get(curso.getId()) != null)){
							// valido que tenga las clases seteadas
							SubscripcionDeClases subsClase=mapaSubsPorClase.get(curso.getId());
							
							if(subsClase.getClaseSeleccionadasList() == null){
								MessageBox.validation(I18N.getLabel("curso.error.seleccion.select.error.select.asignar.clases.cursos"),
										I18N.getLabel("selector.actionwithoutitem.title"));
								tabpanel.setSelectedIndex(1);
								return false;
							}
							for (SubscripcionDeClasesPorActividad iterable_element : subsClase.getClaseSeleccionadasList()) {
								if(iterable_element.getActividad().getId().intValue() == actYCl.getActiv().getId().intValue()){
										// ahora se que es el curso seleccionado, para la actividad seleccionada, que tiene carnet.
										// entonces valido aca si hay seteadas las mismas cantidad de clases y correctamente.
									
									Integer cantClasesQueDebeTener=curso.getCantClasesPorSemana();
									if(cantClasesQueDebeTener == null)
										cantClasesQueDebeTener=actYCl.getCantClases();
									
									if(!validarCantidadDiasSeleccionados(new ArrayList(subsClase.getClaseSeleccionadasList()), cantClasesQueDebeTener)){
										// Para el curso .., se debe asignar correctamente todas las clases
										Object[] params3 =  {curso.getNombre().toUpperCase()};	

										MessageBox.validation(I18N.getLabel("curso.error.seleccion.select.error.select.asignar.clases.curso", params3 ),
												I18N.getLabel("selector.actionwithoutitem.title"));
										tabpanel.setSelectedIndex(1);
										return false;
									}
								}
							}
							
						}else{
							MessageBox.validation(I18N.getLabel("curso.error.seleccion.select.error.select.asignar.clases"), I18N.getLabel("selector.actionwithoutitem.title"));
							tabpanel.setSelectedIndex(1);
							return false;
						}
						
					}
				}			
				
			}
			// DEBERIA ACA ARROJAR ERROR ??? INDIcANDO QuE SE DEBEN SELECCIONAR LAS CLASES ?????
			
			
		}
		return true;
	}
	
	private boolean validarCantidadDiasSeleccionados(List< SubscripcionDeClasesPorActividad> subsClasexAct, int cantDiasQTeniaQTener){
		int i=0;
		for (SubscripcionDeClasesPorActividad subscripcionDeClasesPorActividad : subsClasexAct) {
			if(subscripcionDeClasesPorActividad.isLunes()){
				if(subscripcionDeClasesPorActividad.getClaseLunes() != null)
					i++;
			}
			if(subscripcionDeClasesPorActividad.isMartes()){
				if(subscripcionDeClasesPorActividad.getClaseMartes() != null)
					i++;
			}
			if(subscripcionDeClasesPorActividad.isMiercoles()){
				if(subscripcionDeClasesPorActividad.getClaseMiercoles() != null)
					i++;
			}
			if(subscripcionDeClasesPorActividad.isJueves()){
				if(subscripcionDeClasesPorActividad.getClaseJueves() != null)
					i++;
			}
			if(subscripcionDeClasesPorActividad.isViernes()){
				if(subscripcionDeClasesPorActividad.getClaseViernes() != null)
					i++;
			}
			if(subscripcionDeClasesPorActividad.isSabado()){
				if(subscripcionDeClasesPorActividad.getClaseSabado() != null)
					i++;
			}
			if(subscripcionDeClasesPorActividad.isDomingo()){
				if(subscripcionDeClasesPorActividad.getClaseDomingo() != null)
					i++;
			}
		}
	
		if(i != cantDiasQTeniaQTener )
			return false;
		return true;	
	}
	
	public Subscripcion getSubscripcion() {
		return subscripcion;
	}

	public void setSubscripcion(Subscripcion subscripcion) {
		this.subscripcion = subscripcion;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public void volverAlTabPrincipalYCancelar(){
		this.tabpanel.setSelectedIndex(0);
	}
	
	public SubscripcionDeClasesPorActividad obtenerClasePorActividad(SubscripcionDeClasesCrud subsDeClase){
		SubscripcionDeClasesPorActividad subsClasesPorAct = new SubscripcionDeClasesPorActividad();
	
		if(!subsDeClase.isVisible())
			return null;
		
		if (getSubsClaseCrud().getHoraDesdeLunes().getSelectedItem() != null){
			subsClasesPorAct.setLunes(true);
			if(subsDeClase.getHoraDesdeLunes().getSelectedItem() != null){
				Clase cl=((Clase)subsDeClase.getHoraDesdeLunes().getSelectedItem().getValue());
				subsClasesPorAct.setClaseLunes(cl);	
			}else{
				throw new WrongValueException(subsDeClase.getHoraDesdeLunes(), I18N.getLabel("error.empty.field"));
			}
		}else{
			subsClasesPorAct.setLunes(false);
		}
		
		if (getSubsClaseCrud().getHoraDesdeMartes().getSelectedItem() != null){
			subsClasesPorAct.setMartes(true);
			if(subsDeClase.getHoraDesdeMartes().getSelectedItem() != null){
				Clase cl=((Clase)subsDeClase.getHoraDesdeMartes().getSelectedItem().getValue());
				subsClasesPorAct.setClaseMartes(cl);
				
			}else{
				throw new WrongValueException(subsDeClase.getHoraDesdeMartes(), I18N.getLabel("error.empty.field"));
			}
		}else{
			subsClasesPorAct.setMartes(false);
		}
		
		if (getSubsClaseCrud().getHoraDesdeMiercoles().getSelectedItem() != null){
			subsClasesPorAct.setMiercoles(true);
			if(subsDeClase.getHoraDesdeMiercoles().getSelectedItem() != null){
				Clase cl=((Clase)subsDeClase.getHoraDesdeMiercoles().getSelectedItem().getValue());
				subsClasesPorAct.setClaseMiercoles(cl);
				
			}else{
				throw new WrongValueException(subsDeClase.getHoraDesdeMiercoles(), I18N.getLabel("error.empty.field"));
			}
		}else{
			subsClasesPorAct.setMiercoles(false);
			
		}
		
		if (getSubsClaseCrud().getHoraDesdeJueves().getSelectedItem() != null){
			subsClasesPorAct.setJueves(true);
			if(subsDeClase.getHoraDesdeJueves().getSelectedItem() != null){
				Clase cl=((Clase)subsDeClase.getHoraDesdeJueves().getSelectedItem().getValue());
				subsClasesPorAct.setClaseJueves(cl);
			}else{
				throw new WrongValueException(subsDeClase.getHoraDesdeJueves(), I18N.getLabel("error.empty.field"));
			}
		}else{
			subsClasesPorAct.setJueves(false);
		}
		
		if (getSubsClaseCrud().getHoraDesdeViernes().getSelectedItem() != null){

			subsClasesPorAct.setViernes(true);
			if(subsDeClase.getHoraDesdeViernes().getSelectedItem() != null){
				Clase cl=((Clase)subsDeClase.getHoraDesdeViernes().getSelectedItem().getValue());
				subsClasesPorAct.setClaseViernes(cl);
			}else{
				throw new WrongValueException(subsDeClase.getHoraDesdeViernes(), I18N.getLabel("error.empty.field"));
			}
		}else{
			subsClasesPorAct.setViernes(false);

		}
		
		if (getSubsClaseCrud().getHoraDesdeSabado().getSelectedItem() != null){
			subsClasesPorAct.setSabado(true);
			if(subsDeClase.getHoraDesdeSabado().getSelectedItem() != null){
				Clase cl=((Clase)subsDeClase.getHoraDesdeSabado().getSelectedItem().getValue());
				subsClasesPorAct.setClaseSabado(cl);
				
			}else{
				throw new WrongValueException(subsDeClase.getHoraDesdeSabado(), I18N.getLabel("error.empty.field"));
			}
		}else{
			subsClasesPorAct.setSabado(false);

		}
		
		subsClasesPorAct.setDomingo(false);
		
		Actividad act=(Actividad)subsDeClase.getNombreActividad().getAttribute("actividad");
		subsClasesPorAct.setActividad(act);

		return subsClasesPorAct;
	}
	
	@Override
	public void asignarClase() {
		
		// Si es una edicion, debo mostrar mensaje y guardar SOLAMENTE las modificaciones de clases
		if(!cursopanelListGrid.isVisible()){
			Subscripcion subs= subscripcionEJB.findSubscripcionById(subscripcion.getId());

			    /*
			     * Se debe verificar si hay en el hasmap subscripciones nuevas , supuestamente si tengo algo ahi, es por que es algo que se modifico 
			     * Si se modifico , entonces se deberia 
			     * 
			     */
			
			Map <Long, SubscripcionDeClases> mapaSubsPorClase=(HashMap)Session.getAttribute("claseDeSubs");
			if(mapaSubsPorClase == null){
				mapaSubsPorClase = (HashMap<Long, SubscripcionDeClases> ) new HashMap();
				
				if(((Concepto)cursoseleccionadoListGrid.getSelectedItem().getValue()).getCurso() != null){
					Curso curso=((Concepto)cursoseleccionadoListGrid.getSelectedItem().getValue()).getCurso();
					if(curso != null)
						guardarSubscripcionPorClasesNuevas(curso, mapaSubsPorClase);
				}
			}else{
				Curso curso=((Concepto)cursoseleccionadoListGrid.getSelectedItem().getValue()).getCurso();
				// tengo que actualizar la actividad en la q estoy parado ahora por la que tenia en memoria.
				
				if(mapaSubsPorClase.containsKey(curso.getId())){
					mapaSubsPorClase.remove(curso.getId());
					guardarSubscripcionPorClasesNuevas(curso, mapaSubsPorClase);
				}else{
					// lo guardo como nuevo en memoria
					if(curso != null)
						guardarSubscripcionPorClasesNuevas(curso, mapaSubsPorClase);			
				}
			}
			
			if(validateParaModificarDiaCurso()){

//				Curso curso= null;
//				SubscripcionDeClases iterable_elementModificado= null; 
				mapaSubsPorClase=(HashMap)Session.getAttribute("claseDeSubs");
				
				if(mapaSubsPorClase != null && mapaSubsPorClase.size() > 0){
					for (SubscripcionDeClases iterable_element : mapaSubsPorClase.values()) {
						
						iterable_element.setSubscripcion(subs);

						if(iterable_element.getClaseSeleccionadasList() != null){
							if(iterable_element.getClaseSeleccionadasList() != null){
								for (SubscripcionDeClasesPorActividad porAct : iterable_element.getClaseSeleccionadasList()) {
									//porAct.setIdsubscripcion(subsId);
									porAct.setSubscripcion2(subs);
									porAct.setCurso(iterable_element.getCurso());
//									curso=iterable_element.getCurso();
								}
							}
						}
					}
				}
				
				if(mapaSubsPorClase != null && mapaSubsPorClase.size() > 0){
					subs.setSubscripcionDeClases(new HashSet(mapaSubsPorClase.values()));

				}else{
					subs.setSubscripcionDeClases(new HashSet());
				}
					
				List <SubscripcionDeClases> listaSubsDeClases= null;
				if(subs.getSubscripcionDeClases() != null)
					listaSubsDeClases = new ArrayList(subs.getSubscripcionDeClases());
				
				if(listaSubsDeClases != null && listaSubsDeClases.size() >0){
					for (SubscripcionDeClases object : listaSubsDeClases) {
						object.setSubscripcion(subs);
						if(object.getClaseSeleccionadasList() != null){
							for (SubscripcionDeClasesPorActividad iterable_element : object.getClaseSeleccionadasList()) {
								iterable_element.setSubscripcion2(subs);				
							}	
						}		
					}
					if(listaSubsDeClases != null)
						subs.setSubscripcionDeClases(new HashSet(listaSubsDeClases));
				}
				subscripcionEJB.save(subs);
				
				MessageBox.info("Se guardan correctamente los cambios de las clases.", "Modificación de Inscripcion");
				super.gotoPage("/institucion/ingresoInscripcion-selector.zul");	
			}
			
		}else{
			Map <Long, SubscripcionDeClases> mapaSubsPorClase=(HashMap)Session.getAttribute("claseDeSubs");
			if(mapaSubsPorClase == null){
				mapaSubsPorClase = (HashMap<Long, SubscripcionDeClases> ) new HashMap();
				
				if(((Concepto)cursoseleccionadoListGrid.getSelectedItem().getValue()).getCurso() != null){
					Curso curso=((Concepto)cursoseleccionadoListGrid.getSelectedItem().getValue()).getCurso();
					if(curso != null)
						guardarSubscripcionPorClasesNuevas(curso, mapaSubsPorClase);
		
				}
			}else{
				Curso curso=((Concepto)cursoseleccionadoListGrid.getSelectedItem().getValue()).getCurso();
				// tengo que actualizar la actividad en la q estoy parado ahora por la que tenia en memoria.
				
				if(mapaSubsPorClase.containsKey(curso.getId())){
					mapaSubsPorClase.remove(curso.getId());
					guardarSubscripcionPorClasesNuevas(curso, mapaSubsPorClase);
				}else{
					// lo guardo como nuevo en memoria
					if(curso != null)
						guardarSubscripcionPorClasesNuevas(curso, mapaSubsPorClase);			
				}
			}
			// valida que este todo seteado bien segun las validaciones.
			// Asigna y Guarda clase
			this.tabpanel.setSelectedIndex(0);			
		}
		
	}
	
	private void guardarSubscripcionPorClasesNuevasFromModelToView(SubscripcionDeClases subs, Map <Long, SubscripcionDeClases> mapaSubsPorClase ){
		
		mapaSubsPorClase.put(subs.getCurso().getId(), subs);
		Session.setAttribute("claseDeSubs", mapaSubsPorClase);	
		
	}
	
	private void guardarSubscripcionPorClasesNuevas(Curso curso, Map <Long, SubscripcionDeClases> mapaSubsPorClase ){
		
		
		
		SubscripcionDeClases claseDeSubs = new SubscripcionDeClases();
		claseDeSubs.setCurso(curso);
		
		Set <SubscripcionDeClasesPorActividad> mapSubsPorClase = new HashSet<SubscripcionDeClasesPorActividad>();
		
		SubscripcionDeClasesPorActividad c0=obtenerClasePorActividad(getSubsClaseCrud());
		if(c0 != null ) mapSubsPorClase.add(c0);
		SubscripcionDeClasesPorActividad c1=obtenerClasePorActividad(getSubsClaseCrud2());
		if(c1 != null ) mapSubsPorClase.add(c1);
		SubscripcionDeClasesPorActividad c2=obtenerClasePorActividad(getSubsClaseCrud3());
		if(c2 != null ) mapSubsPorClase.add(c2);
		SubscripcionDeClasesPorActividad c3=obtenerClasePorActividad(getSubsClaseCrud4());
		if(c3 != null ) mapSubsPorClase.add(c3);
		SubscripcionDeClasesPorActividad c4=obtenerClasePorActividad(getSubsClaseCrud5());
		if(c4 != null ) mapSubsPorClase.add(c4);		
		
		claseDeSubs.setClaseSeleccionadasList(mapSubsPorClase);
		
		mapaSubsPorClase.put(claseDeSubs.getCurso().getId(), claseDeSubs);
		Session.setAttribute("claseDeSubs", mapaSubsPorClase);	
		
	}
	
	private int getCantClases(Curso curso){
		
		if(curso != null){
			if(curso.getActividadYClaseList() != null){
				
				for (ActividadYClase iterable_element : curso.getActividadYClaseList()) {
						return iterable_element.getCantClases();
					
				}
			}
		}
		return 0;
	}
	
	public List<Quincena> obtenerQuincenalesList(){
		return subscripcionEJB.obtenerQuincenalesList();
	}

	
	
	public boolean actualizarCursoSegunDescuento(ObraSocial oc, String type, Concepto concepto) {
		boolean result=true;
		
		if(oc != null){
			// debo aca simular que se aplico descuento x obra social
		
//			vuelvo a dejar el texto el segundo campo de descuento, actualizo el importe total del curso y del curso con descuento
			
			int precioQuePagaElClientePorSesion=-1;
			double precioQuePagaLaObraSocialPorSesion=-1;
			
			List<ObraSocialesPrecio> os=subscripcionEJB.findObraSocialesByObraSocialPrecioById(oc.getId());
		 					
			for (ObraSocialesPrecio precio : os) {
				if(concepto.getActividadDelConcepto() != null &&
						precio.getActividad().getId().intValue() == concepto.getActividadDelConcepto().getId().intValue()){
					precioQuePagaElClientePorSesion= precio.getPrecioQuePagaElCliente();
					precioQuePagaLaObraSocialPorSesion= precio.getPrecioQuePagaLaObraSocial();
				}
			}
//			for ( ActividadYClase  actYClase : curso.getActividadYClaseList()) {
//				cantClasesPorCurso=actYClase.getCantClases();
//			}
//			if (precioQuePagaElClientePorSesion != -1){
//				 						
//				precioConDescuentoObraSocial=precioQuePagaElClientePorSesion * cantClasesPorCurso;		
//			}
			
			concepto.setTipoDescuento(TipoDescuentosEnum.OBRA_SOCIAL);
			concepto.setObraSocial(oc);
			concepto.setComentarioDescuento(null);
			String comentario= "Descuento Obra Social: "+ concepto.getObraSocial().getNombre().toUpperCase();
			
			/* Cliente paga por Session $20 ( 3 sesiones = $ 60)*/
			if(precioQuePagaElClientePorSesion > 0){
				concepto.setObraSocial((clienteEJB.loadLazy(concepto.getObraSocial())));
				if(pagaCopagoSegunActividad(concepto.getObraSocial().getPreciosActividadesObraSocial(), concepto.getActividadDelConcepto()))
					comentario = comentario + ". Copago de $"+ formateador.format(precioQuePagaElClientePorSesion) + " una única vez al ingreso de la primer Sesion. )";
				else
					comentario = comentario + ". Copago de $"+ formateador.format(precioQuePagaElClientePorSesion) + " al ingreso de cada Sesion. )";
			}else{
				
				comentario = comentario + ". No se abona Copago ";
			}
			
			if(concepto.getComentarioDescuento() != null){
				concepto.setComentarioDescuento(concepto.getComentarioDescuento() +". "+ comentario);
			}else
				concepto.setComentarioDescuento(comentario);	
						
			if(precioQuePagaElClientePorSesion != -1)
				concepto.setPrecioPorClaseOSesionPagaCliente(precioQuePagaElClientePorSesion);
			
			if(precioQuePagaLaObraSocialPorSesion != -1)
				concepto.setPrecioPorClaseOSesionPagaObraSocial(new Double(precioQuePagaLaObraSocialPorSesion));
						
			concepto.setImporteConDescuento(0);

			
			cursoseleccionadoListGrid.convertirCampoAIntBox(concepto, false, true);

//			cursoseleccionadoListGrid.convertirCampoAIntBox(concepto, true);
			
			// actualizo el value en el row y ademas el listCell que se ve por pantalla
			cursoseleccionadoListGrid.actualizoElValueYLosListCell(concepto);
			
			int importeFinal=obtenerImporteFinalDeLaLista(null);
		
			getSubscripcion4Crud().getPrecio().setValue("$"+formateador.format(importeFinal));

			if(importeFinal > 0 ){
				crudFormasDePago.setVisible(true);		
			}else
				crudFormasDePago.setVisible(false);

			actualizarAdicionalyImporteTotal();					
		
		}else if(type.equalsIgnoreCase("SIN descuento")){
			
//			vuelvo a dejar el texto el segundo campo de descuento, actualizo el importe total del curso y del curso con descuento
//			dsd
			//	si es sin descuento, elimino el concepto y lo vuelvo a agregar
			concepto.setTipoDescuento(null);
			concepto.setObraSocial(null);
			
			

			if(concepto.getComentarioDescuento() != null && concepto.getComentarioDescuento().contains("Contratacion")){
				concepto.setComentarioDescuento("Contratacion por Clase, Clases contratadas:"+ concepto.getCantSesiones());

			}else{
				concepto.setComentarioDescuento(null);	
			}
				
			concepto.setPrecioPorClaseOSesionPagaCliente(null);
			concepto.setPrecioPorClaseOSesionPagaObraSocial(null);
					
			if(concepto.getCurso() == null) {
				// es una matricula
				concepto.setImporteConDescuento(concepto.getImporteOriginal());
			}else{
				if(concepto.getComentarioDescuento() != null && concepto.getComentarioDescuento().contains("Contratacion")){
					int nuevoVal=concepto.getCurso().getPrecio() * concepto.getCantSesiones();
					concepto.setImporteConDescuento(nuevoVal);
				}else{
					concepto.setImporteConDescuento(concepto.getCurso().getPrecio());					
				}
			}

			cursoseleccionadoListGrid.convertirCampoAIntBox(concepto, true, false);

			// actualizo el value en el row y ademas el listCell que se ve por pantalla
			cursoseleccionadoListGrid.actualizoElValueYLosListCell(concepto);

			int importeFinal=obtenerImporteFinalDeLaLista(null);

			getSubscripcion4Crud().getPrecio().setValue("$"+formateador.format(importeFinal));

			if(importeFinal > 0 ){
				crudFormasDePago.setVisible(true);		
			}else
				crudFormasDePago.setVisible(false);

			actualizarAdicionalyImporteTotal();		


		}else if(type.equalsIgnoreCase("Descuento General/Manual")){
			
			concepto.setTipoDescuento(TipoDescuentosEnum.GENERAL);
			concepto.setObraSocial(null);
			concepto.setPrecioPorClaseOSesionPagaCliente(null);
			concepto.setPrecioPorClaseOSesionPagaObraSocial(null);
			
			if(concepto.getComentarioDescuento() != null && concepto.getComentarioDescuento().contains("Contratacion")){
				concepto.setComentarioDescuento("Contratacion por Clase, Clases contratadas:"+ concepto.getCantSesiones()+ ". Se aplico descuento manual.");

			}else{
				concepto.setComentarioDescuento( "Se aplico descuento manual.");	
			}
			
			if(concepto.getCurso() == null) {
				// es una matricula
				concepto.setImporteConDescuento(concepto.getImporteOriginal());
			}else{
				if(concepto.getComentarioDescuento() != null && concepto.getComentarioDescuento().contains("Contratacion")){
					int nuevoVal=concepto.getCurso().getPrecio() * concepto.getCantSesiones();
					concepto.setImporteConDescuento(nuevoVal);
				}else{
					concepto.setImporteConDescuento(concepto.getCurso().getPrecio());					
				}
			}
			

			// actualizo el value en el row y ademas el listCell que se ve por pantalla
			cursoseleccionadoListGrid.actualizoElValueYLosListCell(concepto);

			int importeFinal=obtenerImporteFinalDeLaLista(null);
			getSubscripcion4Crud().getPrecio().setValue("$"+formateador.format(importeFinal));

			if(importeFinal > 0 ){
				crudFormasDePago.setVisible(true);		
			}else
				crudFormasDePago.setVisible(false);

			actualizarAdicionalyImporteTotal();		

			cursoseleccionadoListGrid.convertirCampoAIntBox(concepto, false, false);

		}else{
			// sin descuento deberia de ser.
			concepto.setTipoDescuento(null);
			concepto.setObraSocial(null);
			concepto.setComentarioDescuento(null);	
			concepto.setPrecioPorClaseOSesionPagaCliente(null);
			concepto.setPrecioPorClaseOSesionPagaObraSocial(null);
			
			if(concepto.getCurso() == null) {
				// es una matricula
				concepto.setImporteConDescuento(concepto.getImporteOriginal());
			}else{
				if(concepto.getComentarioDescuento() != null && concepto.getComentarioDescuento().contains("Contratacion")){
					int nuevoVal=concepto.getCurso().getPrecio() * concepto.getCantSesiones();
					concepto.setImporteConDescuento(nuevoVal);
				}else{
					concepto.setImporteConDescuento(concepto.getCurso().getPrecio());					
				}
			}
			

			cursoseleccionadoListGrid.convertirCampoAIntBox(concepto, true, false);

			// actualizo el value en el row y ademas el listCell que se ve por pantalla
			cursoseleccionadoListGrid.actualizoElValueYLosListCell(concepto);

			int importeFinal=obtenerImporteFinalDeLaLista(null);

			getSubscripcion4Crud().getPrecio().setValue("$"+formateador.format(importeFinal));

			if(importeFinal > 0 ){
				crudFormasDePago.setVisible(true);		
			}else
				crudFormasDePago.setVisible(false);

			actualizarAdicionalyImporteTotal();		

		}
		return result;
	}
	
	@Override
	public boolean agregarCurso(boolean isFromContinuar, Integer valorTotCurso) {
		boolean result=true;
		if(cursopanelListGrid.getSelectedItem() != null){
			cliente= clienteEJB.loadLazy(cliente, false, false, false, true);

			// obtengo el 
			Curso curso=(Curso)cursopanelListGrid.getSelectedItem().getValue();
				
			if(curso != null){
				
				// validaciones de cumpleaños
				if(curso != null && curso.getNombre().contains("CUMPLEA")){
					if(cursoseleccionadoListGrid.getList(true) != null && cursoseleccionadoListGrid.getList(true).size() >0){
						MessageBox.validation(" En ventas de cumpleaños solo se permite el concepto del cumpleaños. Elimine los conceptos que no sean cumpleaños.", 
								I18N.getLabel("selector.actionwithoutitem.title"));			
						result= false;
						return result;	
					}
					
				}else{
					// si el que estoy agregando NO es cumpleaños, pero el que tenia ERA cumpleaños
					boolean tengoCumpleanios= false;
					for (Concepto iterable_element : cursoseleccionadoListGrid.getList(true)) {
						if(iterable_element.getCurso() != null && 
								iterable_element.getCurso().getNombre().contains("CUMPLEA")){
							tengoCumpleanios= true;
						}
						
					}
					if(tengoCumpleanios){
						MessageBox.validation(" En ventas de cumpleaños solo se permite el concepto del cumpleaños. Elimine los conceptos que no sean cumpleaños.", 
								I18N.getLabel("selector.actionwithoutitem.title"));	
						result= false;
						return result;	
					}
				}
				// validar si tiene ventas de ini
				boolean tieneOtrosCursosINI= false;
				
				// validaciones de cursos maipu
				if(curso != null  
					&& (curso.getNombre().equalsIgnoreCase("INI VERANO-TRANSPORTE")  
						|| curso.getNombre().equalsIgnoreCase("INI VERANO-CANTINA")
						|| curso.getNombre().equalsIgnoreCase("INI VERANO"))){
					
					for (Concepto iterable_element : cursoseleccionadoListGrid.getList(false)) {
						if(iterable_element.getCurso() != null  
								&& !iterable_element.getCurso().getNombre().equalsIgnoreCase("INI VERANO-TRANSPORTE")  
								&& !iterable_element.getCurso().getNombre().equalsIgnoreCase("INI VERANO-CANTINA")
								&& !iterable_element.getCurso().getNombre().equalsIgnoreCase("INI VERANO"))
							tieneOtrosCursosINI= true;
					}
					
					if(tieneOtrosCursosINI){
						MessageBox.validation(" En una misma venta, no se permiten vender cursos de Maipu y Centro. Elimine los conceptos que no cumplan este requisito.", 
								I18N.getLabel("selector.actionwithoutitem.title"));						
						result= false;
						return result;	
					}
				}else{
					
					boolean tieneCursosMaipu= false;

					for (Concepto iterable_element : cursoseleccionadoListGrid.getList(false)) {
						if(iterable_element.getCurso() != null  
								&& (iterable_element.getCurso().getNombre().equalsIgnoreCase("INI VERANO-TRANSPORTE")  
										|| iterable_element.getCurso().getNombre().equalsIgnoreCase("INI VERANO-CANTINA")
										|| iterable_element.getCurso().getNombre().equalsIgnoreCase("INI VERANO")))
							tieneCursosMaipu= true;
					}
					
					if(tieneCursosMaipu){
						MessageBox.validation(" En una misma venta, no se permiten vender cursos de Maipu y Centro.. Elimine los conceptos que no cumplan este requisito.", 
								I18N.getLabel("selector.actionwithoutitem.title"));
						result= false;
						return result;	
					}
			}
	
			if(!isFromContinuar){
				
				if(curso.getVencimiento() != null && getCantClases(curso) == 1 &&
						((curso.getVencimiento().toInt() == VencimientoCursoEnum.MENSUAL_VENCE_A_LOS_3_MES.toInt())
						|| (curso.getVencimiento().toInt() == VencimientoCursoEnum.MENSUAL_VENCE_AL_ANo.toInt())
						|| (curso.getVencimiento().toInt() == VencimientoCursoEnum.MENSUAL_VENCE_AL_MES.toInt()))){
					subscripcionporClasecrud.setVisible(true);
					continuarPorClase.setVisible(true);
					getSubsPorClaseCrud().getDiasQueParticipara().setFocus(true);
					getSubsPorClaseCrud().getDiasQueParticipara().setValue(1);
					result= false;
					return result;
				}
			}
				
				int precioCurso=0;
				if( isFromContinuar && valorTotCurso != null) 
					precioCurso=valorTotCurso;
				else
					precioCurso=curso.getPrecio();
				
				String conceptoMatricula=null;
				Actividad actividadDelDescuentoOS= null;
	 				// en caso de que no tenga descuentos........
	 				for (ActividadYClase actYCl : curso.getActividadYClaseList()) {
						actividadDelDescuentoOS= actYCl.getActiv();
	 				}
	 			
	 			// ahora trata la matricula
	 			int precioMatricula= 0;
	 			if(curso.isPagaSubscripcion()){
	 				//  validar si pago subscripcion, validar la ultima fecha de pago, mostrar por pantalla 					
	 				List<Matricula> matric=subscripcionEJB.findAllMatriculas();
	 				precioMatricula=matric.get(0).getPrecio();
	 				if(cliente.getInscripcionesList() != null && cliente.getInscripcionesList().size() >0){
						boolean encontroVigente= false;
						Date fechaVigente= null;
						Date fechaMayorVencida= null;
	
						for (Inscripcion inscrip : cliente.getInscripcionesList()) {
	 						Date fecha= inscrip.getFechaVencimiento();
	 					//	fecha.setYear(inscrip.getFechaVencimiento().getYear()+ 1);
	 						
	 						if(fechaMayorVencida == null)
	 							fechaMayorVencida= fecha;
	 						
	 						if(fecha.after(new Date())){	//significa que la matricula se vencio
	 							encontroVigente= true;
	 							fechaVigente= fecha;
	 						}	
	 						
	 						if(fecha.after(fechaMayorVencida))
	 							fechaMayorVencida= fecha;
	 					}
						
	 					if(!encontroVigente){
	 						matriculavencida.setVisible(true);
							separadormatricula.setVisible(true);
							Calendar ahoraCal= Calendar.getInstance();
							ahoraCal.setTime(fechaMayorVencida);
							int mes=ahoraCal.get(Calendar.MONTH) + 1;
							String fechaMyorVenc=ahoraCal.get(Calendar.DATE)+"/"+mes+"/"+ahoraCal.get(Calendar.YEAR);

							
							matriculavencida.setValue("Debe abonar la matricula anual del institucion vencida el "+fechaMyorVenc); 
							conceptoMatricula="Matricula anual vencida "+ fechaMyorVenc;
	 					}else{
	 						matriculavencida.setVisible(true);
							separadormatricula.setVisible(true);
							Calendar ahoraCal= Calendar.getInstance();
							ahoraCal.setTime(fechaVigente);
							int mes=ahoraCal.get(Calendar.MONTH) + 1;

							String fechaVig=ahoraCal.get(Calendar.DATE)+"/"+mes+"/"+ahoraCal.get(Calendar.YEAR);

							matriculavencida.setValue("Matricula vigente. Vence:  "+fechaVig); 
	 					}
	 					
	 				}else{
	 					matriculadebe.setVisible(true);
	 					separadormatricula.setVisible(true);
	 					matriculadebe.setValue("Debe abonar la matrícula anual del Instituto. ");
						conceptoMatricula="Matrícula anual";
	 				}
	 			}			
	 					
	 			// Si el curso esta seleccionado Paga matricula y su matricula esta vencida
	 			if(conceptoMatricula != null ){
	 				Concepto concepto= new Concepto();
	 				concepto.setConcepto(conceptoMatricula);
	 				concepto.setCurso(null);
	 				concepto.setImporteOriginal(precioMatricula);
	 				concepto.setImporteConDescuento(precioMatricula);
	
	 				// si no existe ninguna matricula en la lista de conceptos
	 				if(!cursoseleccionadoListGrid.existeMatriculaEnLista()){
	 	 				cursoseleccionadoListGrid.setList(concepto, false);	
	 				}
	  			}
		
	 			List<Concepto> listaConcepto=cursoseleccionadoListGrid.getList(false);
				// si existe algun concepto de tipo de curso quincenal quincenal, y este tiene seleccionado una sucursal me la traigo
	 			
	 			Quincena seleccionadaAnterioremtne= null;
	 			if(listaConcepto != null){
	 				for (Concepto concepto : listaConcepto) {
						if(concepto.getQuincena() != null){
							seleccionadaAnterioremtne=concepto.getQuincena();
						}
	 					
					}
	 			}

	 			Date seleccionadaAnterioremtneFechaCumple= null;
	 			if(listaConcepto != null){
	 				for (Concepto concepto : listaConcepto) {
						if(concepto.getFechaCumple() != null){
							seleccionadaAnterioremtneFechaCumple=concepto.getFechaCumple();
						}
	 					
					}
	 			}

	 			Concepto concepto= new Concepto();
				concepto.setConcepto(curso.getNombre().toUpperCase());
				concepto.setCurso(curso);
				concepto.setImporteOriginal(precioCurso);
				concepto.setActividadDelConcepto(actividadDelDescuentoOS);


				if( isFromContinuar && valorTotCurso != null){
					
					if(curso != null && curso.getNombre().contains("CUMPLEA")){
//						Date ints=getCumpleaniosCrud().getFechaCumpleanios().getValue() ;
//						
						if(seleccionadaAnterioremtneFechaCumple != null){
							Calendar ahoraCal= Calendar.getInstance();
							ahoraCal.setTime(seleccionadaAnterioremtneFechaCumple);
							int mes=ahoraCal.get(Calendar.MONTH) + 1;
	
							String fecha=ahoraCal.get(Calendar.DATE)+"/"+mes+"/"+ahoraCal.get(Calendar.YEAR);
							concepto.setComentarioDescuento("Cumpleaños, Fecha: "+ fecha);
							
						}else
							concepto.setComentarioDescuento("Cumpleaños" );
						concepto.setCantSesiones(1);	

					}else if(curso.getVencimiento() != null &&  curso.getVencimiento().toInt() == VencimientoCursoEnum.QUINCENAL_VENCE_A_LA_QUINCENA.toInt() ){
//						Integer ints=getSubsQuincenalesCrud().getDiasQueParticipara().getValue() ;
						concepto.setComentarioDescuento("Cursos Quincenales ");
						concepto.setCantSesiones(10);	
						
					}else{
						Integer ints=getSubsPorClaseCrud().getDiasQueParticipara().getValue() ;
						concepto.setComentarioDescuento("Contratacion por Clase, Clases contratadas:"+ ints);
						concepto.setCantSesiones(ints);
					}
				} 
				
				concepto.setImporteConDescuento(precioCurso);
				
				int importeFinal=obtenerImporteFinalDeLaLista(concepto);
				
				if(importeFinal > 0 ){
					crudFormasDePago.setVisible(true);		
				}else
					crudFormasDePago.setVisible(false);
		
				
				// si no existe ningun curso en Lista
				if(!cursoseleccionadoListGrid.existeCursoEnLista(concepto)){
					getSubscripcion4Crud().getPrecio().setValue("$"+formateador.format(importeFinal));
					
					// Al agregarse un nuevo curso, se hace algo con la lista de pagos, 
	 				crudFormasDePago.getGridList().removeAll();
					getSubscripcion4Crud().getAdicionalTarjeta().setValue("$"+String.valueOf("0"));
	
					crudFormasDePago.getValorFaltantePesos().setValue(formateador.format(importeFinal));
					crudFormasDePago.getValorSumadoPesos().setValue("0");
					
					if(concepto.getCantSesiones() ==0 && concepto.getCurso() != null){
						int cantClases= 0;
					
						for (ActividadYClase iterable_element : concepto.getCurso().getActividadYClaseList()) {
							cantClases=iterable_element.getCantClases();
						}
						concepto.setCantSesiones(cantClases);
					} 
					actualizarAdicionalyImporteTotal();
					
					if(seleccionadaAnterioremtneFechaCumple != null)
						concepto.setFechaCumple(seleccionadaAnterioremtneFechaCumple);
					
					if(seleccionadaAnterioremtne != null)
						concepto.setQuincena(seleccionadaAnterioremtne);
					
	 				cursoseleccionadoListGrid.setList(concepto, false);		
	 				
	 				if(curso != null && curso.getNombre().contains("CUMPLEA")){
						getSubscripcionCumpleCrud().setVisible(true); 
						subscumplecrud.setVisible(true);
	 				}
					
	 				// SI el concepto que estoy agregando, getCurso es distinto de null
	 					// Si tiene un curso y el curso tiene actividades que requieran de setear una clase , entonces, redirecciono a la pantalla 
	 						// clases
	 				if(concepto.getCurso() != null ){
	 					
	 					boolean tiene= false;
						for (ActividadYClase actYCl : concepto.getCurso().getActividadYClaseList()) {
							
							if(actYCl.getActiv().isUsaCarnet() 
									&&  (curso.getVencimiento().toInt() == VencimientoCursoEnum.SEMANAL_VENCE_A_LA_SEMANA.toInt()
									||  curso.getVencimiento().toInt() == VencimientoCursoEnum.SEMANAL_VENCE_AL_MES.toInt()
											||  curso.getVencimiento().toInt() == VencimientoCursoEnum.SEMANAL_VENCE_AL_ANo.toInt()
											||  curso.getVencimiento().toInt() == VencimientoCursoEnum.SEMANAL_VENCE_A_LOS_3_MES.toInt())){ 
								tiene= true;
							}
						}
						
						if(tiene){
							asignarClasesYIr2Pantalla(concepto);
						}
					}

				}else{
					MessageBox.validation(I18N.getLabel("curso.error.seleccion.other.curse"), I18N.getLabel("selector.actionwithoutitem.title"));
					result= false;
					return result;	
				}		
			}else{
				MessageBox.validation(I18N.getLabel("curso.error.seleccion.add"), I18N.getLabel("selector.actionwithoutitem.title"));
				result= false;
				return result;
			}
		}else{
			MessageBox.validation(I18N.getLabel("curso.error.seleccion.add"), I18N.getLabel("selector.actionwithoutitem.title"));
			result= false;
			return result;	
		}
		
		if(cursopanelListGrid.getSelectedItem() != null){
			Curso curso=(Curso)cursopanelListGrid.getSelectedItem().getValue();
			cursopanelListGrid.actualizarIntbos(curso.getId().intValue(), "suma");
			
//			if(((TipoDescuentosEnum)getSubscripcion5Crud().getComboDescuentos().getSelectedItem().getValue()).equals(TipoDescuentosEnum.OBRA_SOCIAL)){
////				debo borrar la seleccion de actividad y volver a hacer busqueda de cursos
//				getCursosFilter().getCurso().setSelectedItem(null);
//				onFind(null);
//			}

		}
		return result;
	}
//	
//	private void setSelected (Combobox combo, Actividad act){
//		
//		if(combo != null && combo.getItems() != null){
//			for (Object iterable_element : combo.getItems()) {
//				if(((Actividad)((Comboitem)iterable_element).getValue()).getId().intValue() == act.getId().intValue()){
//					combo.setSelectedItem((Comboitem)iterable_element);
//					break;
//				}
//			}
//		}
//	}
//	
	
	private boolean pagaCopagoSegunActividad( Set<ObraSocialesPrecio>  listPrecios, Actividad act){
		
		if(listPrecios != null){
			for (ObraSocialesPrecio obraSocialesPrecio : listPrecios) {
				
				if(obraSocialesPrecio.getActividad().getId().intValue() == act.getId().intValue()){
					if(obraSocialesPrecio.getSePagaUnaUnicaVez() != null)
						return obraSocialesPrecio.getSePagaUnaUnicaVez();
					else
						return false;
				}
			}
		}
		return false;
	}
	
	
	private SucursalEnum getSucursal(Subscripcion subs){
		SucursalEnum suc= null;
		boolean esDeMaipu= false;
		if(subs != null && subs.getConceptoList() != null && subs.getConceptoList().size() > 0){
			
			for (Concepto iterable_element : subs.getConceptoList()) {
				
				if(iterable_element.getCurso() != null && iterable_element.getCurso().getNombre() != null  
						&& (iterable_element.getCurso().getNombre().equalsIgnoreCase("INI VERANO-TRANSPORTE")  
								|| iterable_element.getCurso().getNombre().equalsIgnoreCase("INI VERANO-CANTINA")
								|| iterable_element.getCurso().getNombre().equalsIgnoreCase("INI VERANO"))){
					esDeMaipu= true;
				}
			}		
		}
		if(esDeMaipu)
			suc= SucursalEnum.MAIPU;
		else
			suc= SucursalEnum.CENTRO;
		return suc;
	}
	
	public boolean seSeleccionoCurso(){
		
		if(cursoseleccionadoListGrid.getItems() != null)
			return true;
		else 
			return false;
	}
	
	public int obtenerImporteFinalDeLaLista(Concepto concepto){
		
		int importeTotal=cursoseleccionadoListGrid.obtenerImporteFinalDeLaListaMenosDelCurso(new Integer(-9));
		if(concepto != null)
			importeTotal= importeTotal + concepto.getImporteConDescuento();
	
		return importeTotal;			
	}

	@Override
	public void guardarCambios() {
		
		if(subscripcion.getFechaYHoraAnulacion() != null){
			MessageBox.info("No se permite realizar modificaciones a una subscripcion anulada", "Operacion cancelada");
			if(com.institucion.fm.conf.Session.getAttribute("isFromCumples") != null){
				Sessions.getCurrent().setAttribute("isFromCumples", null);
				super.gotoPage("/institucion/cumples-selector.zul");
			}else if (Sessions.getCurrent().getAttribute("isSubsFromIngresoInscripcion") != null){ 
				Sessions.getCurrent().setAttribute("isSubsFromIngresoInscripcion", null);
				super.gotoPage("/institucion/ingresoInscripcion-selector.zul");
			}else if(com.institucion.fm.conf.Session.getAttribute("isSubsFromDeudasClientes") != null){	
				Sessions.getCurrent().setAttribute("isSubsFromDeudasClientes", null);
				super.gotoPage("/institucion/deuda-cliente-selector.zul");
			}else	
				super.gotoPage("/institucion/clientes-selector.zul");

		}else{
			try{
				List<Concepto> lists= null;
				lists=cursoseleccionadoListGrid.getList(true);
				CupoActividadEstadoEnum estadoNuevo=null;

				if(lists != null && lists.size()>0){
				
					// si el curso es de cumpleanios 
					if(lists.size() == 1 
							&& ((Concepto)lists.get(0)).getCurso() != null 
							&& ((Concepto)lists.get(0)).getCurso().getNombre().contains("CUMPLEA")){

						if (MessageBox.question("Seguro desea guardar los cambios realizados al cumpleaños ?","Guardar Cambios")){
							// guardo la subscripcion
							
							for (Concepto concepto : lists) {
								cajaEJB.save(concepto);
							}
							Subscripcion eeee=subscripcionEJB.findSubscripcionById(subscripcion.getId());
							eeee=clienteEJB.loadLazy(eeee, true, true, false, false, false);
							if(getSubscripcionCumpleCrud().getDetalleCumple() != null){
								eeee.setDetalleCumple(getSubscripcionCumpleCrud().getDetalleCumple());
							}
							
							String monto=getSubscripcion4Crud().getPrecio().getValue().substring(1);
							if(monto.contains(",")){
								monto= monto.replace(",", "");
							}							
							
							int precio= Integer.parseInt(monto);	
							double precioOriginalDelaSubscripcionnn=eeee.getPrecioTOTALadicionalTarjeta();
							if(precio != eeee.getPrecioTOTALadicionalTarjeta()){
								
								
								eeee.setPrecioCursosMatricula(precio);
								eeee.setPrecioTOTALadicionalTarjeta(precio);
//									eeee.setValorAdicionaltarjeta(valorAdicionaltarjeta)
								
								double sonLosPagosQueHabiaRealizado=0;

								if(eeee.getPagosPorSubscripcionList() != null && eeee.getPagosPorSubscripcionList().size() > 0){
									for (PagosPorSubscripcion pagoPorSubscripcion: eeee.getPagosPorSubscripcionList()) {
//										if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA.toInt()){
//											sonLosPagosQueHabiaRealizado= sonLosPagosQueHabiaRealizado+ pagoPorSubscripcion.getCantidadDinero();
////											sonLosPagosQueHabiaRealizado= sonLosPagosQueHabiaRealizado+ pagoPorSubscripcion.getAdicional();
//										}else{
											sonLosPagosQueHabiaRealizado= sonLosPagosQueHabiaRealizado+ pagoPorSubscripcion.getCantidadDinero();
//										}		
									}
								}
								
								if(precio > sonLosPagosQueHabiaRealizado){ // o sea, si debo plata
									//  SI EL NUEVO PRECIO ES >a lo que habia pagado, si estaba en adeuda actualizo el estado como corresponda.
									if(eeee.getCupoActividadList().size() >0){
										if(((CupoActividad)new ArrayList(eeee.getCupoActividadList()).get(0)).getEstado().toInt() 
												== CupoActividadEstadoEnum.C_CUPOS.toInt()){
											estadoNuevo=CupoActividadEstadoEnum.C_DEUDAS_Y_C_CUPOS;
											
										}else if(((CupoActividad)new ArrayList(eeee.getCupoActividadList()).get(0)).getEstado().toInt() 
												== CupoActividadEstadoEnum.S_CUPOS.toInt()){
											estadoNuevo=CupoActividadEstadoEnum.ADEUDA_Y_SIN_CUPOS;
											
										}else if(((CupoActividad)new ArrayList(eeee.getCupoActividadList()).get(0)).getEstado().toInt() 
												== CupoActividadEstadoEnum.VENCIDA.toInt()){
											estadoNuevo=CupoActividadEstadoEnum.VENCIDA_CON_DEUDA;
										}
									}
															
								}else	if(precio == sonLosPagosQueHabiaRealizado){ // o sea, si tengo todo saldado.
									//  Si el nuevo precio es == a lo que tengo pagado, si estaba en adeuda actualizo el estado como corresponda
									
									if(eeee.getCupoActividadList().size() >0){
										if(((CupoActividad)new ArrayList(eeee.getCupoActividadList()).get(0)).getEstado().toInt() 
												== CupoActividadEstadoEnum.C_DEUDAS_Y_C_CUPOS.toInt()){
											estadoNuevo=CupoActividadEstadoEnum.C_CUPOS;
											
										}else if(((CupoActividad)new ArrayList(eeee.getCupoActividadList()).get(0)).getEstado().toInt() 
												== CupoActividadEstadoEnum.ADEUDA_Y_SIN_CUPOS.toInt()){
											estadoNuevo=CupoActividadEstadoEnum.S_CUPOS;
											
										}else if(((CupoActividad)new ArrayList(eeee.getCupoActividadList()).get(0)).getEstado().toInt() 
												== CupoActividadEstadoEnum.VENCIDA_CON_DEUDA.toInt()){
											estadoNuevo=CupoActividadEstadoEnum.VENCIDA;
										}else{
											estadoNuevo=CupoActividadEstadoEnum.C_CUPOS;
										}
									}

									
								}else	if(precio < sonLosPagosQueHabiaRealizado){ // tengo todo super saldado
									 /* 		 si el nuevo precio es < a lo que habia pagado:
										 * 				informo por pantalla y pregunto "Se habia dejado un adelanto mayor al precio del cumple, se desea devolver la diferencia de dinero de $... en EFECTIVO/tarjeta?"
										 * 						si pone que si: se le resta esa diferencia al pago y un movimiento de caja donde se vea salir la plata
										 * 						si pone que no: queda el pago asi
										 * 						Luego actualizo estados de la subscripcion a saldada */
									
									int diferencia= (int) (sonLosPagosQueHabiaRealizado - new Double(precio));
									
									CajaMovimiento caja= new CajaMovimiento();
									caja.setConcepto("Devolucion de dinero adelantado por modificacion de datos en cumpleaños: Precio Original:"+precioOriginalDelaSubscripcionnn + ", Nuevo precio:"+precio );
									caja.setFecha(new Date());
									caja.setIdUsuarioGeneroMovimiento(Session.getUsernameID().intValue());
									caja.setTipoMovimiento(TipoMovimientoCajaEnum.EGRESO);
									caja.setValor(new Double(diferencia));
									caja.setSucursal(getSucursal(subscripcion));
									caja.setCliente(subscripcion.getCliente());
									caja.setIdSubscripcion(subscripcion.getId().intValue());
									cajaEJB.save(caja);
			
									int diferenciaAIrDescontando=diferencia;
									// aca falta bajar la parte de los apgos
									// la teoria es que yo pague de mas, y ahora tengo que devolver esa diferencia
									if(eeee.getPagosPorSubscripcionList() != null && eeee.getPagosPorSubscripcionList().size() > 0){
										
										for (PagosPorSubscripcion pagoPorSubscripcion: eeee.getPagosPorSubscripcionList()) {
											
											if(pagoPorSubscripcion.getCantidadDinero() > diferenciaAIrDescontando){
												pagoPorSubscripcion.setCantidadDinero(pagoPorSubscripcion.getCantidadDinero() - diferenciaAIrDescontando);
												pagoPorSubscripcion.setEsCopago(false);

												if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_15.toInt()){
													
													int adicionalparte=0;
									   				if(pagoPorSubscripcion.getPorcInteres() != null){
									   					adicionalparte=(int)(pagoPorSubscripcion.getCantidadDinero() * pagoPorSubscripcion.getPorcInteres() )/100;
									   				}else{
									   					adicionalparte=(int)(pagoPorSubscripcion.getCantidadDinero() * 15 )/100;
									   				}
									   				pagoPorSubscripcion.setAdicional(adicionalparte);
												}else if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_20.toInt()){
													
													int adicionalparte=0;
									   				if(pagoPorSubscripcion.getPorcInteres() != null){
									   					adicionalparte=(int)(pagoPorSubscripcion.getCantidadDinero() * pagoPorSubscripcion.getPorcInteres() )/100;
									   				}else{
									   					adicionalparte=(int)(pagoPorSubscripcion.getCantidadDinero() * 20 )/100;
									   				}
									   				pagoPorSubscripcion.setAdicional(adicionalparte);
												}
												break;
											}else if(pagoPorSubscripcion.getCantidadDinero() == diferenciaAIrDescontando){
												pagoPorSubscripcion.setEsCopago(false);
												pagoPorSubscripcion.setCantidadDinero(0);
												if(pagoPorSubscripcion.getAdicional() >0){
													pagoPorSubscripcion.setAdicional(0);
												}
												break;
											}else{
												// quiere decir que saco el dinero de el pago anterior + un nuevo pago.
												pagoPorSubscripcion.setEsCopago(false);
												pagoPorSubscripcion.setCantidadDinero(0);
												diferenciaAIrDescontando = diferenciaAIrDescontando-pagoPorSubscripcion.getCantidadDinero(); // - pagoPorSubscripcion.getAdicional();
											
												
												if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_15.toInt()){
													
													int adicionalparte=0;
									   				if(pagoPorSubscripcion.getPorcInteres() != null){
									   					adicionalparte=(int)(pagoPorSubscripcion.getCantidadDinero() * pagoPorSubscripcion.getPorcInteres() )/100;
									   				}else{
									   					adicionalparte=(int)(pagoPorSubscripcion.getCantidadDinero() * 15 )/100;
									   				}
									   				pagoPorSubscripcion.setAdicional(adicionalparte);
												}else if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_20.toInt()){
													
													int adicionalparte=0;
									   				if(pagoPorSubscripcion.getPorcInteres() != null){
									   					adicionalparte=(int)(pagoPorSubscripcion.getCantidadDinero() * pagoPorSubscripcion.getPorcInteres() )/100;
									   				}else{
									   					adicionalparte=(int)(pagoPorSubscripcion.getCantidadDinero() * 20 )/100;
									   				}
									   				pagoPorSubscripcion.setAdicional(adicionalparte);
												}
											}
										}
										int adic=obtenerAdicionales(eeee);
										eeee.setValorAdicionaltarjeta(adic);
										eeee.setPrecioTOTALadicionalTarjeta(eeee.getPrecioCursosMatricula() +adic );
									}	

									MessageBox.info("Se registro la devolucion de esa diferencia de dinero $"+formateador.format(diferencia) + " en efectivo ", "Realizado");

									if(eeee.getCupoActividadList().size() >0){
										if(((CupoActividad)new ArrayList(eeee.getCupoActividadList()).get(0)).getEstado().toInt() 
												== CupoActividadEstadoEnum.C_DEUDAS_Y_C_CUPOS.toInt()){
											estadoNuevo=CupoActividadEstadoEnum.C_CUPOS;
											
										}else if(((CupoActividad)new ArrayList(eeee.getCupoActividadList()).get(0)).getEstado().toInt() 
												== CupoActividadEstadoEnum.ADEUDA_Y_SIN_CUPOS.toInt()){
											estadoNuevo=CupoActividadEstadoEnum.S_CUPOS;
											
										}else if(((CupoActividad)new ArrayList(eeee.getCupoActividadList()).get(0)).getEstado().toInt() 
												== CupoActividadEstadoEnum.VENCIDA_CON_DEUDA.toInt()){
											estadoNuevo=CupoActividadEstadoEnum.VENCIDA;
										}else{
											estadoNuevo=CupoActividadEstadoEnum.C_CUPOS;
										}
									}
								}
							}
							if(eeee.getCupoActividadList().size() >0 && estadoNuevo != null){
								((CupoActividad)new ArrayList(eeee.getCupoActividadList()).get(0)).setEstado(estadoNuevo);
							}
							subscripcionEJB.save(eeee);
							MessageBox.info("Los cambios se guardaron correctamente", "Realizado");
							if(com.institucion.fm.conf.Session.getAttribute("isFromCumples") != null){
								Sessions.getCurrent().setAttribute("isFromCumples", null);
								super.gotoPage("/institucion/cumples-selector.zul");
							}else if (Sessions.getCurrent().getAttribute("isSubsFromIngresoInscripcion") != null){ 
								Sessions.getCurrent().setAttribute("isSubsFromIngresoInscripcion", null);
								super.gotoPage("/institucion/ingresoInscripcion-selector.zul");
							}else if(com.institucion.fm.conf.Session.getAttribute("isSubsFromDeudasClientes") != null){	
								Sessions.getCurrent().setAttribute("isSubsFromDeudasClientes", null);
								super.gotoPage("/institucion/deuda-cliente-selector.zul");
							}else
								super.gotoPage("/institucion/clientes-selector.zul");
						}
					}else{
						if (MessageBox.question("Seguro desea guardar los cambios realizados en la quincenas ?","Guardar Cambios")){
							// guardo la subscripcion
							for (Concepto concepto : lists) {
								if(concepto.getCurso() != null && concepto.getCurso().getVencimiento() != null &&
										concepto.getCurso().getVencimiento().equals(VencimientoCursoEnum.QUINCENAL_VENCE_A_LA_QUINCENA))
									cajaEJB.save(concepto);
							}
							MessageBox.info("Los cambios se guardaron correctamente", "Realizado");
							if(com.institucion.fm.conf.Session.getAttribute("isFromCumples") != null){
								Sessions.getCurrent().setAttribute("isFromCumples", null);
								super.gotoPage("/institucion/cumples-selector.zul");
							}else if (Sessions.getCurrent().getAttribute("isSubsFromIngresoInscripcion") != null){ 
								Sessions.getCurrent().setAttribute("isSubsFromIngresoInscripcion", null);
								super.gotoPage("/institucion/ingresoInscripcion-selector.zul");
							}else if(com.institucion.fm.conf.Session.getAttribute("isSubsFromDeudasClientes") != null){	
								Sessions.getCurrent().setAttribute("isSubsFromDeudasClientes", null);
								super.gotoPage("/institucion/deuda-cliente-selector.zul");
							}else	
								super.gotoPage("/institucion/clientes-selector.zul");

						}								
					}
				}

			}catch(WrongValueException ee){
				MessageBox.validation("Debe seleccionar la quincena dentro del concepto", I18N.getLabel("selector.actionwithoutitem.title"));
			}			
			
		}
	}


	
	private int obtenerAdicionales(Subscripcion subs){
		int adic=0;
		if(subs.getPagosPorSubscripcionList() != null){
			for (PagosPorSubscripcion pagoPorSubscripcion : subs.getPagosPorSubscripcionList()) {
				
				if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_15.toInt()){
					adic= adic+ pagoPorSubscripcion.getAdicional();
				}	else 	if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_20.toInt()){
					adic= adic+ pagoPorSubscripcion.getAdicional();
				}		
			}
			
		}
		return adic;	
	}
	@Override
	public void eliminarCurso() {

		if(cursoseleccionadoListGrid.getSelectedItem() != null){
			

			// si esta habilitado el cumpleanios lo deshabilito.
			if(subscumplecrud.isVisible()){
				subscumplecrud.setVisible(false);
				getSubscripcionCumpleCrud().setDetalleCumple("");
			}
			
			if(((Concepto)cursoseleccionadoListGrid.getSelectedItem().getValue()).getCurso() == null){
				MessageBox.validation(I18N.getLabel("curso.error.seleccion.delete.no.curso"), I18N.getLabel("selector.actionwithoutitem.title"));
				return;
			}
			Curso cur=((Concepto)cursoseleccionadoListGrid.getSelectedItem().getValue()).getCurso();
			cursoseleccionadoListGrid.eliminarDeList((Concepto)cursoseleccionadoListGrid.getSelectedItem().getValue());	
			
			cursopanelListGrid.actualizarIntbos(cur.getId().intValue(), "resta");

			// falta aca eliminar las clases asociadas a ese curso- actividad
			Map <Long, SubscripcionDeClases> mapaSubsPorClase=(HashMap)Session.getAttribute("claseDeSubs");
			
			if(mapaSubsPorClase != null && mapaSubsPorClase.size() >0 ){
				// valido si encuentro la actividad
				if(mapaSubsPorClase.containsKey(cur.getId())){
					mapaSubsPorClase.remove(cur.getId());
				}
			}
			// actualizar los precios y los 3 valores.	
			int importeFinal=obtenerImporteFinalDeLaLista(null);
			getSubscripcion4Crud().getPrecio().setValue("$"+formateador.format(importeFinal));

			
			if(cursoseleccionadoListGrid.getItems() != null && cursoseleccionadoListGrid.getItems().size() > 0){
				// si se quedo con algun curso, actualizo la lista de pagos
				
				if(crudFormasDePago.getProducts() != null && crudFormasDePago.getProducts().size() > 0){
					
					crudFormasDePago.getGridList().removeAll();
					getSubscripcion4Crud().getAdicionalTarjeta().setValue("$"+String.valueOf("0"));
					int importeFinal2=obtenerImporteFinalDeLaLista(null);

					crudFormasDePago.getValorFaltantePesos().setValue(formateador.format(importeFinal2));
					crudFormasDePago.getValorSumadoPesos().setValue("0");
					
				}
		
			}else{
				// si se quedo sin cursos , elimino todos los items de pago
				crudFormasDePago.getGridList().removeAll();
				crudFormasDePago.getValorFaltantePesos().setValue(String.valueOf(0));
				crudFormasDePago.getValorSumadoPesos().setValue("0");
			}		
			
			actualizarAdicionalyImporteTotal();			
			
			
			int importeFinal2=obtenerImporteFinalDeLaLista(null);
			
			if(importeFinal2 > 0 ){
				crudFormasDePago.setVisible(true);		
			}else
				crudFormasDePago.setVisible(false);
			
		}else{
			MessageBox.validation(I18N.getLabel("curso.error.seleccion.delete"), I18N.getLabel("selector.actionwithoutitem.title"));
			return;	
		}
	}
	
	@Override
	public int getValorCurso() {
		return obtenerImporteFinalDeLaLista(null);
	}

	@Override
	public int getValorOriginalCurso() {
		int importeTotal= 0;
		Iterator<Listitem> itCursos =cursopanelListGrid.getItems().iterator();
		while (itCursos.hasNext()) {
			Listitem cursosDeLaLista=(Listitem)itCursos.next();
			if(cursosDeLaLista != null && cursosDeLaLista.isSelected()){
				Curso cursoSeleccionado=(Curso)cursosDeLaLista.getValue();
				return cursoSeleccionado.getPrecio();	
			}
		}
		return importeTotal;	
	}

	@Override
	public void actualizarAdicionalyImporteTotal() {

		if(getSubscripcion4Crud().getPrecio().getValue() != null){
			int adicional=0;
			if(crudFormasDePago.getProducts() != null && crudFormasDePago.getProducts().size() > 0){
				for (PagosPorSubscripcion pagoPorSubscripcion: crudFormasDePago.getProducts()) {
					if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_15.toInt()){
						
						int adicionalparte=0;
		   				if(pagoPorSubscripcion.getPorcInteres() != null){
		   					adicionalparte=(int)(pagoPorSubscripcion.getCantidadDinero() * pagoPorSubscripcion.getPorcInteres() )/100;
		   				}else{
		   					adicionalparte=(int)(pagoPorSubscripcion.getCantidadDinero() * 15 )/100;
		   				}

						Integer.toString(adicionalparte);
						pagoPorSubscripcion.setAdicional(adicionalparte);
						adicional= adicional+ adicionalparte;	
					}else 					if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_20.toInt()){
						
						int adicionalparte=0;
		   				if(pagoPorSubscripcion.getPorcInteres() != null){
		   					adicionalparte=(int)(pagoPorSubscripcion.getCantidadDinero() * pagoPorSubscripcion.getPorcInteres() )/100;
		   				}else{
		   					adicionalparte=(int)(pagoPorSubscripcion.getCantidadDinero() * 20 )/100;
		   				}

						Integer.toString(adicionalparte);
						pagoPorSubscripcion.setAdicional(adicionalparte);
						adicional= adicional+ adicionalparte;	
					}		
		
				}
				getSubscripcion4Crud().getAdicionalTarjeta().setValue("$"+formateador.format(adicional));
				int precio= 0;
				if(getSubscripcion4Crud().getPrecio().getValue().startsWith("$") && getSubscripcion4Crud().getPrecio().getValue().length() >1){
					String monto=getSubscripcion4Crud().getPrecio().getValue().substring(1);
					if(monto.contains(",")){
						monto= monto.replace(",", "");
					}
					precio= Integer.parseInt(monto);	
				}
				int valorTotFin=precio + adicional;
				getSubscripcion4Crud().getPrecioTOTALadicionalTarjeta().setValue("$"+formateador.format(valorTotFin));
			}else{
				getSubscripcion4Crud().getAdicionalTarjeta().setValue("$"+"0");
				int precio= 0;
				if(getSubscripcion4Crud().getPrecio().getValue().startsWith("$") && getSubscripcion4Crud().getPrecio().getValue().length() >1){
					String monto=getSubscripcion4Crud().getPrecio().getValue().substring(1);
					if(monto.contains(",")){
						monto= monto.replace(",", "");
					}
					precio= Integer.parseInt(monto);
				}
				int valorTotFin=precio + adicional;
				getSubscripcion4Crud().getPrecioTOTALadicionalTarjeta().setValue("$"+formateador.format(valorTotFin));
				
			}
		}
			
	}

	
	@Override
	public void pagarCurso() {
		
		Object[] params = {cliente.getApellido().toUpperCase() +" "+ cliente.getNombre().toUpperCase(), getSubscripcion4Crud().getPrecioTOTALadicionalTarjeta().getValue()};	
			
		
		String precio=getSubscripcion4Crud().getPrecioTOTALadicionalTarjeta().getValue();
		int debePagar= 0;
		if(precio.startsWith("$") && precio.length() >1){
			String monto=getSubscripcion4Crud().getPrecioTOTALadicionalTarjeta().getValue().substring(1);
			if(monto.contains(",")){
				monto= monto.replace(",", "");
			}

			debePagar= Integer.parseInt(monto);
		}
		int pagosRealizados=0;
		if(crudFormasDePago.getProducts() != null && crudFormasDePago.getProducts().size() > 0){
			for (PagosPorSubscripcion pagoPorSubscripcion: crudFormasDePago.getProducts()) {
				if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_15.toInt()){
					pagosRealizados= pagosRealizados+ pagoPorSubscripcion.getCantidadDinero();
					pagosRealizados= pagosRealizados+ pagoPorSubscripcion.getAdicional();
				}else if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_20.toInt()){
					pagosRealizados= pagosRealizados+ pagoPorSubscripcion.getCantidadDinero();
					pagosRealizados= pagosRealizados+ pagoPorSubscripcion.getAdicional();
				}else{
					pagosRealizados= pagosRealizados+ pagoPorSubscripcion.getCantidadDinero();
				}		
			}
		}
			
		if(pagosRealizados < debePagar ){
			int fantantePago =debePagar - pagosRealizados;
			Object[] params2 = {"$"+formateador.format(fantantePago)};
			Object[] params3 =  {cliente.getApellido().toUpperCase() +" "+ cliente.getNombre().toUpperCase(), getSubscripcion4Crud().getPrecioTOTALadicionalTarjeta().getValue(), "$"+formateador.format(fantantePago)};	

//			if (MessageBox.question(I18N.getLabel("selector.seguro.de.abonar.menor",params2),
//					I18N.getLabel("closeup.selector.abonar.y.guardar.title"))){
				if (MessageBox.question(I18N.getLabel("selector.seguro.de.abonar.con.deuda",params3),
						I18N.getLabel("closeup.selector.abonar.y.guardar.title"))){
					
					try {
						onSave(null);
					} catch (Exception e) {
						e.printStackTrace();
					}			
				}
//			}
		}else if(pagosRealizados == 0 && debePagar == 0){
			if (MessageBox.question(I18N.getLabel("selector.seguro.de.abonar.seguro"),
					"Confirmar")){
				try {
					onSave(null);
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}
		}else if (MessageBox.question(I18N.getLabel("selector.seguro.de.abonar",params),
				I18N.getLabel("closeup.selector.abonar.y.guardar.title"))){
			
			try {
				onSave(null);
			} catch (Exception e) {
				e.printStackTrace();
			}			
			
		}
	}

	@Override
	public Actividad getActividadDelCursoSeleccionado() {
		return null;
	}
	
	
	public void onInsertClass(Event event) throws Exception{
		
		// Se debe:
			//deribar la pantalla al abm de clases, 
			//setear algo en memoria que viene de la subscripcion, 
			//guardar toda la subscripcion antes de mandarla
			// recibir la subscripcion y setear los valores que tenia en memoria
		Cliente cliente= (Cliente) Session.getAttribute("idCliente");
		com.institucion.fm.conf.Session.setAttribute("idCliente",cliente );
		
		super.gotoPage("/institucion/clase-crud.zul");		
	}
	
	public void onInsertCurso(Event event) throws Exception{
		
		// Se debe:
			//deribar la pantalla al abm de clases, 
			//setear algo en memoria que viene de la subscripcion, 
			//guardar toda la subscripcion antes de mandarla
			// recibir la subscripcion y setear los valores que tenia en memoria
		
		// Estos botones estaran habilitados solo para la creacion que alli no existe un idSubs, existe solo un idCliente
		Cliente cliente= (Cliente) Session.getAttribute("idCliente");
		com.institucion.fm.conf.Session.setAttribute("idCliente",cliente );
		
		super.gotoPage("/institucion/cursos-crud.zul");
		
	}
		
	public void  actualizarPreciosPorModificacionEnCumples(int precio, Integer cursoID){
		// debe actualizar los labels de Liquidacion Total(son 3 )
		if((subscripcion == null )||  (subscripcion != null && subscripcion.getId() == null)){
			// debo limpiar toda la lista
			crudFormasDePago.setList(null);
			crudFormasDePago.getValorFaltantePesos().setValue("0");
			crudFormasDePago.getValorSumadoPesos().setValue("0");
			crudFormasDePago.cleanCrud();
			
			int importeFinalll=cursoseleccionadoListGrid.obtenerImporteFinalDeLaListaMenosDelCurso(cursoID);
			int nuevo=importeFinalll+precio;
			
			getSubscripcion4Crud().getPrecio().setValue("$"+formateador.format(nuevo));
			actualizarAdicionalyImporteTotal();
		}else{
			Subscripcion eeee=subscripcionEJB.findSubscripcionById(subscripcion.getId());
			eeee=clienteEJB.loadLazy(eeee, false, true, false, false, false);
			int importeFinalll=cursoseleccionadoListGrid.obtenerImporteFinalDeLaListaMenosDelCurso(cursoID);
			int nuevo=importeFinalll+precio;

			getSubscripcion4Crud().getPrecio().setValue("$"+formateador.format(nuevo));
			actualizarAdicionalyImporteTotal();
			
			double precioOriginalDelaSubscripcionnn=eeee.getPrecioTOTALadicionalTarjeta();
			if(precio != eeee.getPrecioTOTALadicionalTarjeta()){
				boolean elPrecioesmenosAlquehabiapagado=false;	
				eeee.setPrecioCursosMatricula(precio);
				eeee.setPrecioTOTALadicionalTarjeta(precio);
					
				double sonLosPagosQueHabiaRealizado=0;
	
					if(eeee.getPagosPorSubscripcionList() != null && eeee.getPagosPorSubscripcionList().size() > 0){
						for (PagosPorSubscripcion pagoPorSubscripcion: eeee.getPagosPorSubscripcionList()) {
								sonLosPagosQueHabiaRealizado= sonLosPagosQueHabiaRealizado+ pagoPorSubscripcion.getCantidadDinero();
						}
					}
				
					if(precio < sonLosPagosQueHabiaRealizado){ // tengo todo super saldado
						 /* 		 si el nuevo precio es < a lo que habia pagado:
							 * 				informo por pantalla y pregunto "Se habia dejado un adelanto mayor al precio del cumple, se desea devolver la diferencia de dinero de $... en EFECTIVO/tarjeta?"
							 * 						si pone que si: se le resta esa diferencia al pago y un movimiento de caja donde se vea salir la plata
							 * 						si pone que no: queda el pago asi
							 * 						Luego actualizo estados de la subscripcion a saldada */
						
						elPrecioesmenosAlquehabiapagado= true;
						int diferencia= (int) (sonLosPagosQueHabiaRealizado - new Double(precio));
						
							int diferenciaAIrDescontando=diferencia;
							// aca falta bajar la parte de los apgos
							// la teoria es que yo pague de mas, y ahora tengo que devolver esa diferencia
							if(eeee.getPagosPorSubscripcionList() != null && eeee.getPagosPorSubscripcionList().size() > 0){
								
								for (PagosPorSubscripcion pagoPorSubscripcion: eeee.getPagosPorSubscripcionList()) {
									
									if(pagoPorSubscripcion.getCantidadDinero() > diferenciaAIrDescontando){
										pagoPorSubscripcion.setCantidadDinero(pagoPorSubscripcion.getCantidadDinero() - diferenciaAIrDescontando);
										pagoPorSubscripcion.setEsCopago(false);

										if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_15.toInt()){
											
											int adicionalparte=0;
							   				if(pagoPorSubscripcion.getPorcInteres() != null){
							   					adicionalparte=(int)(pagoPorSubscripcion.getCantidadDinero() * pagoPorSubscripcion.getPorcInteres() )/100;
							   				}else{
							   					adicionalparte=(int)(pagoPorSubscripcion.getCantidadDinero() * 15 )/100;
							   				}
							   				pagoPorSubscripcion.setAdicional(adicionalparte);
										}else if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_20.toInt()){
											
											int adicionalparte=0;
							   				if(pagoPorSubscripcion.getPorcInteres() != null){
							   					adicionalparte=(int)(pagoPorSubscripcion.getCantidadDinero() * pagoPorSubscripcion.getPorcInteres() )/100;
							   				}else{
							   					adicionalparte=(int)(pagoPorSubscripcion.getCantidadDinero() * 20 )/100;
							   				}
							   				pagoPorSubscripcion.setAdicional(adicionalparte);
										}
										break;
									}else if(pagoPorSubscripcion.getCantidadDinero() == diferenciaAIrDescontando){
										pagoPorSubscripcion.setCantidadDinero(0);
										if(pagoPorSubscripcion.getAdicional() >0){
											pagoPorSubscripcion.setAdicional(0);
										}
										pagoPorSubscripcion.setEsCopago(false);

										break;
									}else{
										// quiere decir que saco el dinero de el pago anterior + un nuevo pago.
										pagoPorSubscripcion.setCantidadDinero(0);
										pagoPorSubscripcion.setEsCopago(false);

										diferenciaAIrDescontando = diferenciaAIrDescontando-pagoPorSubscripcion.getCantidadDinero(); // - pagoPorSubscripcion.getAdicional();
									
										
										if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_15.toInt()){
											
											int adicionalparte=0;
							   				if(pagoPorSubscripcion.getPorcInteres() != null){
							   					adicionalparte=(int)(pagoPorSubscripcion.getCantidadDinero() * pagoPorSubscripcion.getPorcInteres() )/100;
							   				}else{
							   					adicionalparte=(int)(pagoPorSubscripcion.getCantidadDinero() * 15 )/100;
							   				}
							   				pagoPorSubscripcion.setAdicional(adicionalparte);
										}else if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_20.toInt()){
											
											int adicionalparte=0;
							   				if(pagoPorSubscripcion.getPorcInteres() != null){
							   					adicionalparte=(int)(pagoPorSubscripcion.getCantidadDinero() * pagoPorSubscripcion.getPorcInteres() )/100;
							   				}else{
							   					adicionalparte=(int)(pagoPorSubscripcion.getCantidadDinero() * 20 )/100;
							   				}
							   				pagoPorSubscripcion.setAdicional(adicionalparte);
										}
									}
								}
								int adic=obtenerAdicionales(eeee);
								eeee.setValorAdicionaltarjeta(adic);
	//							eeee.setPrecioTOTALadicionalTarjeta(eeee.getPrecioCursosMatricula() +adic );
							}	
					}
			if(elPrecioesmenosAlquehabiapagado)
				crudFormasDePago.setList(new ArrayList(eeee.getPagosPorSubscripcionList())); 
			}
		}		
	}

	@Override
	public void sortEvent(Event event) {
	}

	@Override
	public void buscar(Event evt, boolean isFromCodigoBarras) {
		String nombre= null;
		if(evt instanceof InputEvent){
			nombre=((InputEvent)evt).getValue();	
		}
		onFind(nombre);
	}

	
	public boolean agregarNuevoCursoAConceptoEvento(Event evt, int idCurso){
		boolean result= false;
		if(subscripcion != null && subscripcion.getId() != null){
			result= false;
		}else{
			// seleccionar el curso idCurso
			if(cursopanelListGrid.seleccionarList(idCurso)){
				result=agregarCurso(false, null);
			}
		}
		return result;
	}
	
	
	public boolean quitarNuevoCursoAConceptoEvento(Event evt, int idCurso){
		boolean result= false;

		// selecciono el concepto con ese id de curso
		if(subscripcion != null && subscripcion.getId() != null){
			result= false;

		}else{
			if(cursoseleccionadoListGrid.seleccionarList(idCurso)){
				eliminarCurso();
				result= true;
			}
		}
		return result;
	}

	public void  quitarConceptoDeLaLista(int cursoID){

		if(subscripcion != null && subscripcion.getId() != null){
			
		}else{

			// debo volver a dejar el curso en color negro.
			if(cursoseleccionadoListGrid.seleccionarList(cursoID)){
				eliminarCurso();
				// vuelvo a poner en negro el curso
				cursopanelListGrid.actualizarIntbos(cursoID, "resta");
			}
		}
	}

	@Override
	public void venderNuevaSubs(Event evt, int idCliente) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actualizarPaneldePrecioProducto() {
		// TODO Auto-generated method stub
		
	}
	
}
