package com.institucion.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;

import com.institucion.bz.ClaseEJB;
import com.institucion.bz.ClienteEJB;
import com.institucion.bz.CursoEJB;
import com.institucion.fm.conf.Session;
import com.institucion.fm.conf.exception.WebExceptionHandler;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.service.SelectorFEComposer;
import com.institucion.fm.desktop.view.MessageBox;
import com.institucion.fm.desktop.view.PanelCrud;
import com.institucion.fm.desktop.view.PanelFilter;
import com.institucion.fm.desktop.view.PanelList;
import com.institucion.fm.desktop.view.PanelReport;
import com.institucion.model.Actividad;
import com.institucion.model.AlumnosEnClaseCrud;
import com.institucion.model.AlumnosEnClaseFilter;
import com.institucion.model.AlumnosPorClaseList;
import com.institucion.model.Clase;
import com.institucion.model.ClaseConListaAlumnos;
import com.institucion.model.ClaseConListaAlumnosHistorico;
import com.institucion.model.Cliente;
import com.institucion.model.ClienteListaEncontradoEnPileta;
import com.institucion.model.ClienteListaEncontradoEnPiletaHistorico;
import com.institucion.model.ClienteNoEncontradoEnPileta;
import com.institucion.model.ClienteNoEncontradoEnPiletaHistorico;
import com.institucion.model.ClientesNoEncontradosEnListaPresenteCrud;
import com.institucion.model.IngresoInscripcionClaseList;

public class AlumnosEnClaseSelectorComposer extends SelectorFEComposer{
	
	private PanelList clasepanelListGrid;
	private PanelReport clientespanelList;
	private List<Clase> pharmacyList;
	private PanelFilter filter;
	private ClaseEJB claseEJB;
	private CursoEJB cursoEJB;
	private PanelCrud alumnosEnClase1;
	public Tabbox tabpanel;
	public Tab clasesCrudtab;
	public Tab alumnosEnClaseCrudtab;
	public ClientesNoEncontradosEnListaPresenteCrud crudAlumnosNoEncontrados;
	private boolean esNuevo=false; 
	private ClaseConListaAlumnos listaClaseconListaOrig;
	private boolean seAplicoBusquedaConFechas= false;
	private ClienteEJB clienteEJB;

	public AlumnosEnClaseSelectorComposer(){
		super();
		claseEJB = BeanFactory.<ClaseEJB>getObject("fmEjbClase");
		cursoEJB = BeanFactory.<CursoEJB>getObject("fmEjbCurso");
		  clienteEJB = BeanFactory.<ClienteEJB>getObject("fmEjbCliente");

	}

	private IngresoInscripcionClaseList getClaseListCrud() {
		return (IngresoInscripcionClaseList) (clasepanelListGrid.getGridList());
	}

	private AlumnosPorClaseList getAlumnosorClaseListCrud() {
		return (AlumnosPorClaseList) (clientespanelList.getGridReport());
	}
	
	private AlumnosEnClaseCrud getAlumnosEnClaseCrud() {
		return (AlumnosEnClaseCrud) (alumnosEnClase1.getGridCrud());
	}
	
	private AlumnosEnClaseFilter getFilter() {
		return (AlumnosEnClaseFilter)(filter.getGridFilter());
	}
	
	public void clear(){
		getFilter().clear();
	}
	
	public void onCreate() {
		
		setCallFromMenu(Session.getDesktopPanel().getMessage().equals("menu")); // enlazado con DesktopMenu
		setCallFromMenu(false);
		clear();
		getFilter().getConsultarPorFechaExacta().setChecked(false);
		
		String dia33=" ";

		Date fechas2=new Date();
		if(fechas2 != null){
		
			Integer stateType= fechas2.getDay();
			if(stateType == 0){
				dia33="domingo";
			}else if(stateType == 1){
				dia33="lunes";
			}else if(stateType == 2){
				dia33="martes";
			}else if(stateType == 3){
				dia33="miercoles";
			}else if(stateType == 4){
				dia33="jueves";
			}else if(stateType == 5){
				dia33="viernes";
			}else{
				dia33="sabado";
			}
			getFilter().getDia().setValue(dia33);
			getFilter().getDia().setAttribute("numeroDia", fechas2.getDay());
		}
		
		// obtener las actividades que esten habilitadas para tomar Lista
		List<Actividad> actividadesList=cursoEJB.findAllActividadQueTomenLista();
		getFilter().getComboBoxCurso(actividadesList, getFilter().getCurso());
		onFind();
			
		filter.getInnerPanel().setOpen(true);
		filter.setOpen(true);
		alumnosEnClaseCrudtab.addEventListener(Events.ON_CLICK, new EventListener() {
			public void onEvent(Event evt) {			
				
				if(getClaseListCrud().getSelectedItem() != null){
					getClaseListCrud().getSelectedItem().setValue(
							clienteEJB.loadLazy(((Clase)getClaseListCrud().getSelectedItem().getValue()), true, true, false, false));

					Listitem item=getClaseListCrud().getSelectedItem();
					
					if(item != null){
						Sessions.getCurrent().setAttribute("idClase", ((Clase)getClaseListCrud().getSelectedItem().getValue()));
						clasesCrudtab.setDisabled(true);
						comenzarATomarList((Clase)getClaseListCrud().getSelectedItem().getValue());
						
					}else if(((Clase)getClaseListCrud().getSelectedItem().getValue()).getClienesEnClase() == null 
							|| (((Clase)getClaseListCrud().getSelectedItem().getValue()).getClienesEnClase() != null && 
									((Clase)getClaseListCrud().getSelectedItem().getValue()).getClienesEnClase().size() <=0)){
						MessageBox.info(I18N.getLabel("curso.error.seleccion.select.seleccionar.una.clase.conclientes"), I18N.getLabel("selector.actionwithoutitem.title"));
						tabpanel.setSelectedIndex(0);
						return;
					}else{
						Sessions.getCurrent().setAttribute("idClase", ((Clase)getClaseListCrud().getSelectedItem().getValue()));
						clasesCrudtab.setDisabled(true);
						comenzarATomarList((Clase)getClaseListCrud().getSelectedItem().getValue());
					}	
				
			}else{
				MessageBox.info(I18N.getLabel("curso.error.seleccion.select.seleccionar.una.clase.conclientes"), I18N.getLabel("selector.actionwithoutitem.title"));
				tabpanel.setSelectedIndex(0);
				return;
			}
		}		
	  });	
		
	}

	public void onVerListaPresentesClass(Event evt){
		
		
	}
	
	public void onTomarListaPresentesClass(Event evt){
		if(getClaseListCrud().getSelectedItem() != null){
			
			getClaseListCrud().getSelectedItem().setValue(
					clienteEJB.loadLazy(((Clase)getClaseListCrud().getSelectedItem().getValue()), true, true, false, false));

			if(((Clase)getClaseListCrud().getSelectedItem().getValue()).getClienesEnClase() == null 
					|| (((Clase)getClaseListCrud().getSelectedItem().getValue()).getClienesEnClase() != null && 
							((Clase)getClaseListCrud().getSelectedItem().getValue()).getClienesEnClase().size() <=0)){
				MessageBox.info(I18N.getLabel("curso.error.seleccion.select.seleccionar.una.clase.conclientes"), I18N.getLabel("selector.actionwithoutitem.title"));
				tabpanel.setSelectedIndex(0);
				return;
			}else{
				comenzarATomarList((Clase)getClaseListCrud().getSelectedItem().getValue());
			}	
		
		}else{
			MessageBox.info(I18N.getLabel("curso.error.seleccion.select.seleccionar.una.clase.conclientes"), I18N.getLabel("selector.actionwithoutitem.title"));
			tabpanel.setSelectedIndex(0);
			return;
		}
		
	}
	
	private ClaseConListaAlumnos fromViewToModel(ClaseConListaAlumnos claseconListaAlum, Clase clase) {
			
		if((getAlumnosEnClaseCrud().getProfe().getValue() !=  null ))
			claseconListaAlum.setProfeNombre(getAlumnosEnClaseCrud().getProfe().getValue());
		
		if((getAlumnosEnClaseCrud().getComentariosProfeGenerales().getValue() !=  null ))
			claseconListaAlum.setComentarioGeneralDelProfe(getAlumnosEnClaseCrud().getComentariosProfeGenerales().getValue());
		
		claseconListaAlum.setClase(clase);
		claseconListaAlum.setFecha(new Date());
		
		List<ClienteListaEncontradoEnPileta>  listaPile =getAlumnosorClaseListCrud().getListClientesEnClase();
		if(listaPile != null){
			
			for (ClienteListaEncontradoEnPileta iterable_element : listaPile) {
				iterable_element.setClase(claseconListaAlum);
			}
			
			claseconListaAlum.setClienesListaEncontradosEnPiletaList(new HashSet(listaPile));	
		}else{
			claseconListaAlum.setClienesListaEncontradosEnPiletaList(new HashSet());
		}
		Set<ClienteNoEncontradoEnPileta>  clientesNoEnc=crudAlumnosNoEncontrados.getProducts();		
		if(clientesNoEnc != null){
			for (ClienteNoEncontradoEnPileta iterable_element : clientesNoEnc) {
				iterable_element.setClase(claseconListaAlum);
			}
			
			claseconListaAlum.setClienesNoEncontradosEnPiletaList(clientesNoEnc);
		}else{
			claseconListaAlum.setClienesNoEncontradosEnPiletaList(new HashSet());
		}
		
		if(claseconListaAlum.getClienesListaEncontradosEnPiletaList() != null){
			int cantdadAsistencias=0;
			for (ClienteListaEncontradoEnPileta obj : claseconListaAlum.getClienesListaEncontradosEnPiletaList()) {
				if(obj.getAsistencia() != null && obj.getAsistencia())
					cantdadAsistencias= cantdadAsistencias+1;
			}
			claseconListaAlum.setCantPresentes(cantdadAsistencias);
		}else{
			claseconListaAlum.setCantPresentes(0);
		
		}
		if(claseconListaAlum.getClienesNoEncontradosEnPiletaList() != null)
			claseconListaAlum.setCantPresentesNoEncontrados(claseconListaAlum.getClienesNoEncontradosEnPiletaList().size());
		else
			claseconListaAlum.setCantPresentesNoEncontrados(0);

		
		return claseconListaAlum;
	}
	
	public void onSave(Event event) throws Exception {
		try{

			Listitem item=getClaseListCrud().getSelectedItem();
			if(item == null){
				MessageBox.info("Debe Seleccionar una clase", "Accion Cancelada");
				return;
			}
			if(item != null && item.getAttribute("esClaseHitorica") != null && (Boolean)item.getAttribute("esClaseHitorica")){
				MessageBox.info("No se puede modificar una clase donde ya se le tomo Lista.", "Accion Cancelada");
				return;
			}
			
			if(getFilter().getConsultarPorFechaExacta() == null
					||	(getFilter().getConsultarPorFechaExacta() != null  && !getFilter().getConsultarPorFechaExacta().isChecked())
					||	(getFilter().getConsultarPorFechaExacta() != null  && !getFilter().getConsultarPorFechaExacta().isChecked() 
							&&   getFilter().getFecha1().getValue() == null )){
				
				
				if (MessageBox.question(I18N.getLabel("subscripcion.list.title.anular.title.listaPre.desea"), I18N.getLabel("subscripcion.list.title.anular.title.listaPre"))){
					validateOnSaveCrud();
					
					Clase claseBD=claseEJB.findById(((Clase)getClaseListCrud().getSelectedItem().getValue()).getId());

					if(esNuevo){
						ClaseConListaAlumnos claseconListaAlum= new ClaseConListaAlumnos();
						claseconListaAlum= this.fromViewToModel(claseconListaAlum, claseBD);
						
						if(claseBD.getClaseConListaAlumnosList() == null)
							claseBD.setClaseConListaAlumnosList(new HashSet());

						claseEJB.save(claseconListaAlum);
					}else{
						ClaseConListaAlumnos claseconListaAlum= null;

						for (ClaseConListaAlumnos iterable_element : claseBD.getClaseConListaAlumnosList()) {
							if(iterable_element.getId().equals(listaClaseconListaOrig.getId())){
								claseconListaAlum= iterable_element;
								break;
							}
						}
						claseconListaAlum = this.fromViewToModel(claseconListaAlum, claseBD);

						claseEJB.save(claseconListaAlum);
					}
					
					MessageBox.info(I18N.getLabel("curso.lista.presentes.guardada"), I18N.getLabel("selector.actionwithoutitem.title.realizada"));
					clasesCrudtab.setDisabled(false);

					tabpanel.setSelectedIndex(0);
					onFind();
				}
				
			}
				
		} catch (EJBException ex) {
			WebExceptionHandler.handleThis(ex);
		}

	}
	
	private void validateOnSaveCrud() {
		
		// validar que tenga por lo menos 1 concepto.
		if((getAlumnosEnClaseCrud().getProfe().getValue() ==  null || 
				(getAlumnosEnClaseCrud().getProfe().getValue() !=  null && getAlumnosEnClaseCrud().getProfe().getValue().equalsIgnoreCase("")))){
			throw new WrongValueException(getAlumnosEnClaseCrud().getProfe(), I18N.getLabel("error.empty.field"));
		}
	}

	public void onBack(Event evt){
		// aca deberia balnquear todo y borrar todo, en caso de que ya haya guardado algo.
		clasesCrudtab.setDisabled(false);
		tabpanel.setSelectedIndex(0);
		onFind(evt);
	}
	
	private List<Cliente> tengoClientesNuevosQueNoTeniaEnBaseDeDatos( Set<Cliente>  clientesNuevos, List<ClienteListaEncontradoEnPileta> listaClientesBD){
		List <Cliente> cli= new ArrayList();
		
		if(clientesNuevos != null){
			for (Cliente cliente : clientesNuevos) {
				if(existe(listaClientesBD, cliente)){
					
				}else{
					cli.add(cliente);	
				}	
			}
		}
		return cli;
	}
	
	
	private boolean existe(List<ClienteListaEncontradoEnPileta> listaClientesBD, Cliente clienteNuevo){
		
		if(listaClientesBD != null){
			for (ClienteListaEncontradoEnPileta clienteListaEncontradoEnPileta : listaClientesBD) {
				
				if(clienteListaEncontradoEnPileta.getCliente().getId().equals(clienteNuevo.getId()))
					return true;
			}
		}
		return false;	
	}
	
	private void comenzarATomarList(Clase claseSeleccionada){
	
		getAlumnosEnClaseCrud().clear();
		getAlumnosorClaseListCrud().clear();
		crudAlumnosNoEncontrados.getGridList().removeAll();
	
		getAlumnosEnClaseCrud().getNombreClase().setValue(claseSeleccionada.getNombre());
		getAlumnosEnClaseCrud().getNombreClase().setReadonly(true);
		
		// si la fecha es vieja, lo obtengo directamente de la base de datos y lo muestro deshabilitando botones guardar, etc.
		Date fechaHoy = new Date();

		Calendar cal= Calendar.getInstance();
		cal.set(Calendar.YEAR, 1910);
		
		Listitem item=getClaseListCrud().getSelectedItem();
		
		if(item != null && item.getAttribute("esClaseHitorica") != null && (Boolean)item.getAttribute("esClaseHitorica")){
			/*
			 * Si consulto por fechas exactas, no podria crear, sino que solo para visualizar.
			 */
			ClaseConListaAlumnosHistorico listaClaseconLista = null;
			if(getFilter().getFecha1().getValue() != null && getFilter().getFecha1().getValue().after(cal.getTime()))
				listaClaseconLista =claseEJB.findAllClaseConListaAlumnosByClaseAndFechaHistorico(claseSeleccionada.getId(), getFilter().getFecha1().getValue(), true);
			else
				listaClaseconLista =claseEJB.findAllClaseConListaAlumnosByClaseAndFechaHistorico(claseSeleccionada.getId(), new Date(), true);
				
			if(listaClaseconLista != null){
					if(	listaClaseconLista.getComentarioGeneralDelProfe() != null){
						getAlumnosEnClaseCrud().getComentariosProfeGenerales().setValue(listaClaseconLista.getComentarioGeneralDelProfe());
						getAlumnosEnClaseCrud().getComentariosProfeGenerales().setReadonly(true);
					}
					
					if(	listaClaseconLista.getProfeNombre() != null){
						getAlumnosEnClaseCrud().getProfe().setValue(listaClaseconLista.getProfeNombre());
						getAlumnosEnClaseCrud().getProfe().setReadonly(true);
					}

					List<ClienteListaEncontradoEnPiletaHistorico> listaClientesEnc= null;
					if(getFilter().getFecha1().getValue() != null && getFilter().getFecha1().getValue().after(cal.getTime()))
						listaClientesEnc =claseEJB.findAllByClaseAndFechaHistorico(listaClaseconLista.getId(),null, getFilter().getFecha1().getValue(), true);
					else
						listaClientesEnc =claseEJB.findAllByClaseAndFechaHistorico(listaClaseconLista.getId(),null,  new Date(), true);
					
					if(listaClientesEnc != null){
						getAlumnosorClaseListCrud().setField(new HashSet(listaClientesEnc), true);	
					}

					crudAlumnosNoEncontrados.getGridList().setDisabled(true);
					crudAlumnosNoEncontrados.setButtonsVisible(false);
					
					List<ClienteNoEncontradoEnPiletaHistorico> listaNoEnc= null;
					if(getFilter().getFecha1().getValue() != null && getFilter().getFecha1().getValue().after(cal.getTime()))
						listaNoEnc=claseEJB.findAllByClaseAndFechaClienteNoEncontradoEnPiletaHistorico(listaClaseconLista.getId(), getFilter().getFecha1().getValue(), true);
					else
						listaNoEnc=claseEJB.findAllByClaseAndFechaClienteNoEncontradoEnPiletaHistorico(listaClaseconLista.getId(),new Date(), true);
					
					if(listaNoEnc != null){
						crudAlumnosNoEncontrados.fillListBox(new HashSet(listaNoEnc));
					}
				}

		}else{
			ClaseConListaAlumnos listaClaseconLista= null;
			
			if(getFilter().getFecha1().getValue() != null && getFilter().getFecha1().getValue().after(cal.getTime())){
				listaClaseconLista =claseEJB.findAllClaseConListaAlumnosByClaseAndFecha(claseSeleccionada.getId(), getFilter().getFecha1().getValue(), true);
				
			}else{
				listaClaseconLista =claseEJB.findAllClaseConListaAlumnosByClaseAndFecha(claseSeleccionada.getId(), fechaHoy, false);

			}	
	
			getAlumnosEnClaseCrud().getComentariosProfeGenerales().setReadonly(false);
			getAlumnosEnClaseCrud().getProfe().setReadonly(false);
//			getAlumnosEnClase22Crud().getCantAlumnosEnClase().setReadonly(false);
			crudAlumnosNoEncontrados.getGridList().setDisabled(false);
			crudAlumnosNoEncontrados.setButtonsVisible(true);
			
			if(listaClaseconLista != null){
				// es por que ya se habia guardo con anterioridad ese mismo dia algo
				listaClaseconListaOrig=listaClaseconLista;
				
				if(	listaClaseconLista.getComentarioGeneralDelProfe() != null){
					getAlumnosEnClaseCrud().getComentariosProfeGenerales().setValue(listaClaseconLista.getComentarioGeneralDelProfe());
				}
				
				if(	listaClaseconLista.getProfeNombre() != null){
					getAlumnosEnClaseCrud().getProfe().setValue(listaClaseconLista.getProfeNombre());
					getAlumnosEnClaseCrud().getProfe().setReadonly(true);
				}

				List<ClienteListaEncontradoEnPileta> listaClientesEnc =null;

				if(getFilter().getFecha1().getValue() != null && getFilter().getFecha1().getValue().after(cal.getTime())){
					listaClientesEnc= claseEJB.findAllByClaseAndFecha(listaClaseconListaOrig.getId(), getFilter().getFecha1().getValue(), false);
				}else{
					listaClientesEnc =claseEJB.findAllByClaseAndFecha(listaClaseconListaOrig.getId(), fechaHoy, false);
				}
				
				
				// verifico si tengo clientes nuevos de los que tenia en base de datos.
				List<Cliente> listaClientesQueFaltanAgregar =
									tengoClientesNuevosQueNoTeniaEnBaseDeDatos( claseSeleccionada.getClienesEnClase(),listaClientesEnc);
				
				if(listaClientesQueFaltanAgregar != null && listaClientesQueFaltanAgregar.size() >0){
					Set<ClienteListaEncontradoEnPileta>  clieListaEnc2=convertirClientes(new HashSet(listaClientesQueFaltanAgregar), claseSeleccionada, fechaHoy, listaClaseconLista);
					if(listaClientesEnc != null){
						listaClientesEnc.addAll(clieListaEnc2);
					}else{
						listaClientesEnc= new ArrayList();
						listaClientesEnc.addAll(clieListaEnc2);
					}	
				}
					
				if(listaClientesEnc != null && listaClientesEnc.size() >0){
					getAlumnosorClaseListCrud().cleanRows();
					getAlumnosorClaseListCrud().setField(new HashSet(listaClientesEnc), false);	
				}

				List<ClienteNoEncontradoEnPileta> listaNoEnc =null;

				if(getFilter().getFecha1().getValue() != null && getFilter().getFecha1().getValue().after(cal.getTime())){
					listaNoEnc= claseEJB.findAllByClaseAndFechaClienteNoEncontradoEnPileta(listaClaseconListaOrig.getId(), getFilter().getFecha1().getValue(), false);
				}else{
					listaNoEnc =claseEJB.findAllByClaseAndFechaClienteNoEncontradoEnPileta(listaClaseconListaOrig.getId(), fechaHoy, false);
				}
				
				if(listaNoEnc != null && listaNoEnc.size() >0){
					crudAlumnosNoEncontrados.getGridList().removeAll();
					crudAlumnosNoEncontrados.fillListBox(new HashSet(listaNoEnc));
				}
				
				esNuevo= false;

			}else{
				// es por que es nuevo, la primera vez que se esta usando la clase -fecha
				Set<ClienteListaEncontradoEnPileta>  clieListaEnc=convertirClientes(claseSeleccionada.getClienesEnClase(), claseSeleccionada, fechaHoy, null);
				getAlumnosorClaseListCrud().setField(clieListaEnc, false);
				esNuevo= true;
			}
		}
	}
	
	
	
	private Set<ClienteListaEncontradoEnPileta>  convertirClientes(Set<Cliente> setClientesQueTieneLaClaseAhora, Clase clase, Date fecha, ClaseConListaAlumnos listaClaseconLista){
		Set<ClienteListaEncontradoEnPileta> clientList= new HashSet();
		ClienteListaEncontradoEnPileta cc= null;
		if(setClientesQueTieneLaClaseAhora != null){
			for (Cliente cli : setClientesQueTieneLaClaseAhora) {
				cc= new ClienteListaEncontradoEnPileta();
				cc.setClase(listaClaseconLista);
				cc.setCliente(cli);
				cc.setFecha(fecha);
				
				clientList.add(cc);
			}
		}
		return clientList;
	}
	
	public void onFind(Event evt) {
		Session.setAttribute("pag", false);
		this.onFind();
	}
	
	public void onFind() {
		Logger log=Logger.getLogger(this.getClass());
		log.info("Creando listado de farmacia en la version modificada");
		pharmacyList= new ArrayList<Clase>();
		pharmacyList =claseEJB.findAllConJdbc(getFilter().getFilters());	
			
		if(getFilter().getConsultarPorFechaExacta().isChecked() && getFilter().getFecha1().getValue() != null){
			seAplicoBusquedaConFechas= true;
			
			List<Clase> clasesPasadasHistoricasList =claseEJB.findAllConJdbc(getFilter().getFiltersConFecha(getFilter().getFecha1().getValue(), true, seAplicoBusquedaConFechas));	

			getClaseListCrud().setList(pharmacyList, true, getFilter().getFecha1().getValue(), clasesPasadasHistoricasList, null);
		}else{
			// pharmacyList: Nuevas clases
			// clasesPasadasHistoricasList: Clases pasadas a historico
			// clasesTomadasListasAunNoPasadasHistorico: clases tomadas lista pero no pasadas a historico
			
			seAplicoBusquedaConFechas= false;
			Date fecha= new Date();

			List<Clase> clasesPasadasHistoricasList =claseEJB.findAllConJdbc(getFilter().getFiltersConFecha(fecha, true, seAplicoBusquedaConFechas));	
			
			List<Clase> clasesTomadasListasAunNoPasadasHistorico =claseEJB.findAllConJdbc(getFilter().getFiltersConFecha(fecha, false, seAplicoBusquedaConFechas));	

			getClaseListCrud().setList(pharmacyList, false, null, clasesPasadasHistoricasList, clasesTomadasListasAunNoPasadasHistorico);
		}
	}
	
	public void onClearFilter(Event evt){
		
		getFilter().clear();
		seAplicoBusquedaConFechas= false;
		if (isCallFromMenu()){
			getFilter().clear();
		}else{
			this.onFind();
		}		
	}
}