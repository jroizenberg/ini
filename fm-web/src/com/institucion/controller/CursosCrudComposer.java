package com.institucion.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJBException;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;

import com.institucion.bz.CursoEJB;
import com.institucion.desktop.delegated.CursosDelegate;
import com.institucion.fm.conf.Session;
import com.institucion.fm.conf.exception.WebExceptionHandler;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.CrudComposer;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.MessageBox;
import com.institucion.fm.desktop.view.PanelCrud;
import com.institucion.model.Actividad;
import com.institucion.model.ActividadYClase;
import com.institucion.model.ActividadesDelCursoCrud;
import com.institucion.model.Cliente;
import com.institucion.model.Curso;
import com.institucion.model.CursoCrud;
import com.institucion.model.CursoCrud2;
import com.institucion.model.TipoCursoEnum;
import com.institucion.model.VencimientoCursoEnum;

public class CursosCrudComposer extends CrudComposer implements CursosDelegate{

	public final String idCurso = "idCurso";
	private Curso curso;
	private CursoEJB cursoEJB;
	private ActividadesDelCursoCrud crudActividades;

	private PanelCrud crud;
	private PanelCrud cursocrud;
	
	public CursosCrudComposer() {
		cursoEJB = BeanFactory.<CursoEJB>getObject("fmEjbCurso");
	}

	private CursoCrud getCursoCrud() {
		return (CursoCrud)(crud.getGridCrud());
	}
  
	private CursoCrud2 getCursoCrud2() {
		return (CursoCrud2)(cursocrud.getGridCrud());
	}
	
	public void onCreate() {
		Curso curso= (Curso) Session.getAttribute(idCurso);
		crudActividades.setDelegate(this);
		getCursoCrud2().setDelegate(this);
		setCurso(curso);
		fromModelToView();
	}
	
	private void fromModelToView() {
		// si lo que obtuve de bd es != null
		if(curso != null){

			if(curso.getNombre() != null){
				getCursoCrud().getNombreCb().setValue(curso.getNombre().toUpperCase());
			}
			
			if(curso.getNombre() != null  
					&& (curso.getNombre().equalsIgnoreCase("INI VERANO-TRANSPORTE")  
							|| curso.getNombre().equalsIgnoreCase("INI VERANO-CANTINA")
							|| curso.getNombre().equalsIgnoreCase("INI VERANO")
							|| curso.getNombre().contains("CUMPLE"))){
				getCursoCrud().getNombreCb().setDisabled(true);
			}
			
			if(curso.getDescripcion() != null){
				getCursoCrud().getDescripcionCb().setValue(curso.getDescripcion());
			}
			getCursoCrud2().getPrecio().setValue(curso.getPrecio());

			getCursoCrud().setSelectedPagaSubscripcion(curso.isPagaSubscripcion());
			
			getCursoCrud2().setSelectedUsaCodigoBarras(curso.isEsConCodigosDeBarras());
			
			getCursoCrud().setSelectedDisponible(curso.isDisponible());

			
			if(curso.getIdTipoCurso() != null){
				getCursoCrud().setSelectedTipoDeCurso(curso.getIdTipoCurso());
			}

			if(curso.getVencimiento() != null){
				getCursoCrud2().setSelectedVencimiento(curso.getVencimiento());
				VencimientoCursoEnum vencimientoEnum= curso.getVencimiento() ;

				if(vencimientoEnum.toInt()== VencimientoCursoEnum.SEMANAL_VENCE_AL_MES.toInt() 
						||   vencimientoEnum.toInt() == VencimientoCursoEnum.SEMANAL_VENCE_AL_ANo.toInt()
						|| vencimientoEnum.toInt()== VencimientoCursoEnum.SEMANAL_VENCE_A_LOS_3_MES.toInt()
						|| vencimientoEnum.toInt()== VencimientoCursoEnum.SEMANAL_VENCE_A_LA_SEMANA.toInt()){
					getCursoCrud2().getReq().setVisible(true);
					getCursoCrud2().getCantClasesCursosCursosSemanales().setVisible(true);
					
					if(curso.getCantClasesPorSemana() != null)
						getCursoCrud2().getCantClasesCursosCursosSemanales().setValue(curso.getCantClasesPorSemana());
					else
						getCursoCrud2().getCantClasesCursosCursosSemanales().setValue(0);
					
				}
			}				
			fillListBoxProducts(curso);
			
			if(curso.getVencimiento()  != null){
				VencimientoCursoEnum vencimientoEnum= curso.getVencimiento()  ;
				if(vencimientoEnum.toInt() == VencimientoCursoEnum.LIBRE_VENCE_A_LA_QUINCENA.toInt() || 
						vencimientoEnum.toInt() == VencimientoCursoEnum.LIBRE_VENCE_A_LA_SEMANA.toInt() ||
						vencimientoEnum.toInt() == VencimientoCursoEnum.LIBRE_VENCE_A_LOS_3_MES.toInt() ||
						vencimientoEnum.toInt() == VencimientoCursoEnum.LIBRE_VENCE_AL_MES.toInt()){
					
					getCursoCrud2().getCantClasesCursosCursosSemanales().setVisible(false);
					getCursoCrud2().getReq().setVisible(false);
					setearClasesCantidades(true, false);
					
				}else{
					if(vencimientoEnum.toInt()== VencimientoCursoEnum.QUINCENAL_VENCE_A_LA_QUINCENA.toInt() ){
						setearPrimeros10DiasDeClasesCantidades(vencimientoEnum, false);
						getCursoCrud2().getCantClasesCursosCursosSemanales().setVisible(false);
						getCursoCrud2().getReq().setVisible(false);

					}else if(vencimientoEnum.toInt()== VencimientoCursoEnum.SEMANAL_VENCE_AL_MES.toInt() 
							||   vencimientoEnum.toInt() == VencimientoCursoEnum.SEMANAL_VENCE_AL_ANo.toInt()
							|| vencimientoEnum.toInt()== VencimientoCursoEnum.SEMANAL_VENCE_A_LOS_3_MES.toInt()
							|| vencimientoEnum.toInt()== VencimientoCursoEnum.SEMANAL_VENCE_A_LA_SEMANA.toInt()){
						getCursoCrud2().getCantClasesCursosCursosSemanales().setVisible(true);
						getCursoCrud2().getReq().setVisible(true);
						setearClasesCantidadesSemanales(false);
					}else{
						getCursoCrud2().getCantClasesCursosCursosSemanales().setVisible(false);
						getCursoCrud2().getReq().setVisible(false);
						setearClasesCantidades(false, false);
					}
				}
			}
			getCursoCrud2().getVencimiento().setDisabled(true);
			getCursoCrud2().getCantClasesCursosCursosSemanales().setReadonly(true);
			crudActividades.setButtonsVisible(false);
			crudActividades.getGridList().setDisabled(true);
			crudActividades.getTipoActividad().setDisabled(true);
			crudActividades.getActividad().setDisabled(true);
			crudActividades.getCantidadClases().setDisabled(true);

			
		}		
		
	}
	
	private void fillListBoxProducts(Curso curso) {
		Set<ActividadYClase> result = new HashSet<ActividadYClase>();
		if (curso != null) {
		
			for (ActividadYClase dpp : curso.getActividadYClaseList()) {
				result.add(dpp);
			}
		}
		crudActividades.fillListBox(result);
	}
	
	public void onDoubleClickEvt(Event event) throws Exception{
		crudActividades.updateFields();
	}

	public void onSave(Event event) throws Exception {
		try{
			if (Sessions.getCurrent().getAttribute(idCurso) != null) {
				// es una modificacion
					curso = (Curso) Sessions.getCurrent().getAttribute(idCurso);
					this.fromViewToModel(curso);
					if(!validateCrud()){
						MessageBox.info(I18N.getLabel("curso.error"), I18N.getLabel("selector.actionwithoutitem.title"));
						return;
					}
					cursoEJB.save(curso);
					
					if (Sessions.getCurrent().getAttribute("isClienteFromCurso") != null){ 
						Sessions.getCurrent().setAttribute("isClienteFromCurso", null);
						super.gotoPage("/institucion/cursos-selector.zul");
					}else	
						super.gotoPage("/institucion/cursos-selector.zul");

			} else {
				// es nuevo 
				curso= new Curso();
				
				if(getCursoCrud().getNombreCb().getValue() != null  
						&& (getCursoCrud().getNombreCb().getValue().equalsIgnoreCase("INI VERANO-TRANSPORTE")  
								|| getCursoCrud().getNombreCb().getValue().equalsIgnoreCase("INI VERANO-CANTINA")
								|| getCursoCrud().getNombreCb().getValue().equalsIgnoreCase("INI VERANO")
								|| getCursoCrud().getNombreCb().getValue().contains("CUMPLE"))){
					
					MessageBox.info("No se puede crear un nuevo curso con ese nombre", I18N.getLabel("selector.actionwithoutitem.title"));
					return;
				}
				
				this.fromViewToModel(curso);
				if(!validateCrud()){
					MessageBox.info(I18N.getLabel("curso.error"), I18N.getLabel("selector.actionwithoutitem.title"));
					return;
				}
				cursoEJB.save(curso);
				Cliente cli= (Cliente)com.institucion.fm.conf.Session.getAttribute("idCliente");

				if(cli != null){
					super.gotoPage("/institucion/subscripcion-crud.zul");
					return;
				}
				
				if (Sessions.getCurrent().getAttribute("isClienteFromCurso") != null){ 
					Sessions.getCurrent().setAttribute("isClienteFromCurso", null);
					super.gotoPage("/institucion/cursos-selector.zul");
				}else	
					super.gotoPage("/institucion/cursos-selector.zul");
			}
		} catch (EJBException ex) {
			WebExceptionHandler.handleThis(ex);
		}
	}
		
	public void onBack(Event event) {
		super.gotoPage("/institucion/cursos-selector.zul");
	}


	private Curso fromViewToModel(Curso curso) {
		
		if(getCursoCrud().getNombreCb().getValue() != null)
			curso.setNombre(getCursoCrud().getNombreCb().getValue());
		
		if(getCursoCrud().getDescripcionCb().getValue() != null)
			curso.setDescripcion(getCursoCrud().getDescripcionCb().getValue());
		
		if(getCursoCrud().getPagaSubscripcion().getSelectedItem() != null)
			curso.setPagaSubscripcion(((Boolean)getCursoCrud().getPagaSubscripcion().getSelectedItem().getValue()));
		
		if(getCursoCrud().getDisponible().getSelectedItem() != null)
			curso.setDisponible(((Boolean)getCursoCrud().getDisponible().getSelectedItem().getValue()));
		
		if(getCursoCrud2().getEsConCodigosBarras().getSelectedItem() != null)
			curso.setEsConCodigosDeBarras(((Boolean)getCursoCrud2().getEsConCodigosBarras().getSelectedItem().getValue()));
		
		if(getCursoCrud().getTipoCurso().getSelectedItem() != null)
			curso.setIdTipoCurso(((TipoCursoEnum)getCursoCrud().getTipoCurso().getSelectedItem().getValue()));
	
		if(getCursoCrud2().getPrecio().getValue() != null)
			curso.setPrecio(getCursoCrud2().getPrecio().getValue());
		
		if(getCursoCrud2().getVencimiento().getSelectedItem() != null){
			curso.setVencimiento(((VencimientoCursoEnum)getCursoCrud2().getVencimiento().getSelectedItem().getValue()));
		
			VencimientoCursoEnum vencimientoEnum= curso.getVencimiento() ;
	
			if(vencimientoEnum.toInt()== VencimientoCursoEnum.SEMANAL_VENCE_AL_MES.toInt() 
					||   vencimientoEnum.toInt() == VencimientoCursoEnum.SEMANAL_VENCE_AL_ANo.toInt()
					|| vencimientoEnum.toInt()== VencimientoCursoEnum.SEMANAL_VENCE_A_LOS_3_MES.toInt()
					|| vencimientoEnum.toInt()== VencimientoCursoEnum.SEMANAL_VENCE_A_LA_SEMANA.toInt()){
			
				if(getCursoCrud2().getCantClasesCursosCursosSemanales().getValue() != null)
					curso.setCantClasesPorSemana(getCursoCrud2().getCantClasesCursosCursosSemanales().getValue());
			}
		}
		curso.setActividadYClaseList(crudActividades.getProducts());
		return curso;
	}
	
	private boolean validateCrud() {
			
		if(getCursoCrud().getNombreCb().getValue() == null ||
				(getCursoCrud().getNombreCb().getValue() != null  && getCursoCrud().getNombreCb().getValue().equalsIgnoreCase("")))
			throw new WrongValueException(getCursoCrud().getNombreCb(), I18N.getLabel("error.empty.field"));
		
		if(getCursoCrud().getPagaSubscripcion().getSelectedItem() == null)
			throw new WrongValueException(getCursoCrud().getPagaSubscripcion(), I18N.getLabel("error.empty.field"));

		if(getCursoCrud().getDisponible().getSelectedItem() == null)
			throw new WrongValueException(getCursoCrud().getDisponible(), I18N.getLabel("error.empty.field"));
		
		if(getCursoCrud2().getEsConCodigosBarras().getSelectedItem() == null)
			throw new WrongValueException(getCursoCrud2().getEsConCodigosBarras(), I18N.getLabel("error.empty.field"));

		if(getCursoCrud2().getPrecio().getValue() == null)
			throw new WrongValueException(getCursoCrud2().getPrecio(), I18N.getLabel("error.empty.field"));
		
		if(getCursoCrud2().getVencimiento().getSelectedItem() == null){
			throw new WrongValueException(getCursoCrud2().getVencimiento(), I18N.getLabel("error.empty.field"));
		}else{
			VencimientoCursoEnum vencimientoEnum= (VencimientoCursoEnum)getCursoCrud2().getVencimiento().getSelectedItem().getValue() ;

			if(vencimientoEnum.toInt()== VencimientoCursoEnum.SEMANAL_VENCE_AL_MES.toInt() 
					||   vencimientoEnum.toInt() == VencimientoCursoEnum.SEMANAL_VENCE_AL_ANo.toInt()
					|| vencimientoEnum.toInt()== VencimientoCursoEnum.SEMANAL_VENCE_A_LOS_3_MES.toInt()
					|| vencimientoEnum.toInt()== VencimientoCursoEnum.SEMANAL_VENCE_A_LA_SEMANA.toInt()){
				
				if(getCursoCrud2().getCantClasesCursosCursosSemanales().getValue() == null)
					throw new WrongValueException(getCursoCrud2().getCantClasesCursosCursosSemanales(), I18N.getLabel("error.empty.field"));
				else{
					if(getCursoCrud2().getCantClasesCursosCursosSemanales().getValue() != null &&
							getCursoCrud2().getCantClasesCursosCursosSemanales().getValue() <= 0)
						throw new WrongValueException(getCursoCrud2().getCantClasesCursosCursosSemanales(), "Debe ser mayor a 0");
				}
			}			
		}
		
		if (crudActividades.getProducts().size() < 1) {
			throw new WrongValueException(crudActividades.getGridList(), I18N.getLabel("error.empty.field"));
		}		
		return true;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	@Override
	public void actualizarTipoCurso() {
		
		if(crudActividades.getProducts() == null ||
				(crudActividades.getProducts() != null && crudActividades.getProducts().size() == 0)){
			
//			getCursoCrud().setSelectedTipoDeCurso(null);
					
		}else{
			
			Set<ActividadYClase> actYClases =crudActividades.getProducts();
			Map <Integer, List<Actividad>>  mapaActividadesPorTipoActividad= new HashMap<Integer, List<Actividad>> ();
			mapaActividadesPorTipoActividad.put(TipoCursoEnum.ACTIVIDADES_FISICAS.toInt(), new ArrayList<Actividad>());
			mapaActividadesPorTipoActividad.put(TipoCursoEnum.MASAJES.toInt(), new ArrayList <Actividad>());
			mapaActividadesPorTipoActividad.put(TipoCursoEnum.TRATAMIENTOS_KINESICOS.toInt(), new ArrayList<Actividad>());
			mapaActividadesPorTipoActividad.put(TipoCursoEnum.NATACION.toInt(), new ArrayList<Actividad>());
			mapaActividadesPorTipoActividad.put(TipoCursoEnum.OTROS.toInt(), new ArrayList<Actividad>());
			
			for (ActividadYClase actividadYClase : actYClases) {
				mapaActividadesPorTipoActividad.get(actividadYClase.getActiv().getIdTipoCurso().toInt()).add(actividadYClase.getActiv());		
			}
			
			if(mapaActividadesPorTipoActividad.get(TipoCursoEnum.ACTIVIDADES_FISICAS.toInt()).size() > 0 
					&& mapaActividadesPorTipoActividad.get(TipoCursoEnum.MASAJES.toInt()).size() ==0
					&& mapaActividadesPorTipoActividad.get(TipoCursoEnum.TRATAMIENTOS_KINESICOS.toInt()).size() ==0 
					&& mapaActividadesPorTipoActividad.get(TipoCursoEnum.NATACION.toInt()).size() ==0 ){
				// seteo en el combo ACTIVIDADES FISICAS
				getCursoCrud().setSelectedTipoDeCurso(TipoCursoEnum .ACTIVIDADES_FISICAS);
				
			}else if(mapaActividadesPorTipoActividad.get(TipoCursoEnum.ACTIVIDADES_FISICAS.toInt()).size() == 0 
					&& mapaActividadesPorTipoActividad.get(TipoCursoEnum.MASAJES.toInt()).size() > 0
					&& mapaActividadesPorTipoActividad.get(TipoCursoEnum.TRATAMIENTOS_KINESICOS.toInt()).size() ==0 
					&& mapaActividadesPorTipoActividad.get(TipoCursoEnum.NATACION.toInt()).size() ==0 ){
				// seteo en el combo MASAJEs
				getCursoCrud().setSelectedTipoDeCurso(TipoCursoEnum .MASAJES);
			}else if(mapaActividadesPorTipoActividad.get(TipoCursoEnum.ACTIVIDADES_FISICAS.toInt()).size() == 0 
					&& mapaActividadesPorTipoActividad.get(TipoCursoEnum.MASAJES.toInt()).size() == 0
					&& mapaActividadesPorTipoActividad.get(TipoCursoEnum.TRATAMIENTOS_KINESICOS.toInt()).size() >0 
					&& mapaActividadesPorTipoActividad.get(TipoCursoEnum.NATACION.toInt()).size() ==0 ){
				// seteo en el combo MEDICINA
				getCursoCrud().setSelectedTipoDeCurso(TipoCursoEnum .TRATAMIENTOS_KINESICOS);
			}else if(mapaActividadesPorTipoActividad.get(TipoCursoEnum.ACTIVIDADES_FISICAS.toInt()).size() == 0 
					&& mapaActividadesPorTipoActividad.get(TipoCursoEnum.MASAJES.toInt()).size() == 0
					&& mapaActividadesPorTipoActividad.get(TipoCursoEnum.TRATAMIENTOS_KINESICOS.toInt()).size() ==0 
					&& mapaActividadesPorTipoActividad.get(TipoCursoEnum.NATACION.toInt()).size() >0 ){
				// seteo en el combo NATACION
				getCursoCrud().setSelectedTipoDeCurso(TipoCursoEnum .NATACION);
			}else{
				// seteo en el combo COMBINADO
				getCursoCrud().setSelectedTipoDeCurso(TipoCursoEnum .COMBO);
			}
		
		}
	}

	@Override
	public void muestraLasSubscripciones() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setearClasesCantidades(boolean esLibre, boolean clear){
		crudActividades.setCantidadClases(esLibre, clear);
	}

	@Override
	public void setearClasesCantidadesSemanales(boolean clear){
		crudActividades.setearClasesCantidadesSemanales(clear);
	}

	@Override
	public VencimientoCursoEnum getTipoCursoEnum(){
		if(getCursoCrud2().getVencimiento().getSelectedItem() != null)
			return (VencimientoCursoEnum)getCursoCrud2().getVencimiento().getSelectedItem().getValue();
		else
			return null;
	}
	
	@Override
	public void setearPrimeros10DiasDeClasesCantidades(VencimientoCursoEnum vencimientoEnum, boolean clear){
		crudActividades.setCantidadClasesPrimeros10Dias(vencimientoEnum, clear);
	}
	

	@Override
	public void buscarSubscripcionesDesdeFiltro() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCheckFiltersClasesDoing(boolean check,
			boolean esCheckTodosLosDias) {
		// TODO Auto-generated method stub
		
	}
}
