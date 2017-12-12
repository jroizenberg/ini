package com.institucion.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import com.institucion.bz.CursoEJB;
import com.institucion.desktop.delegated.CursosDelegate;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridCrud;
import com.institucion.fm.desktop.view.GridList;
import com.institucion.fm.desktop.view.PanelEntityCrudList;
import com.institucion.fm.desktop.view.RequiredLabel;

public class ActividadesDelCursoCrud extends PanelEntityCrudList<ActividadYClase>{

	private static final long serialVersionUID = 1L;
	public static final String ID = "gridproduct";
	private CursosDelegate delegate;
	private GridCrud productCrudGrid;
	private GridList productListBox;
	
	private Combobox tipoActividad;
	private Combobox actividad;
	private Combobox cantidadClases;
	
	private Map <Integer, List<Actividad>>mapaActividadesPorTipoActividad;
	private CursoEJB cursoEJB;

	public ActividadesDelCursoCrud(){
		cursoEJB = BeanFactory.<CursoEJB>getObject("fmEjbCurso");
	}

	private String updateMethod="onDoubleClickEvt";

	/**
	 * @return the productAmount
	 */

	public CursosDelegate getDelegate() {
		return delegate;
	}

	public void setDelegate(CursosDelegate delegate) {
		this.delegate = delegate;
	}
	

	
	@Override
	public void cleanCrud() {
		Constraint c = tipoActividad.getConstraint();
		tipoActividad.setConstraint("");
		tipoActividad.setText("");
		tipoActividad.setConstraint(c);
		
		Constraint brandC = actividad.getConstraint();
		actividad.setConstraint("");
		actividad.setText("");
		actividad.setConstraint(brandC);
		
		Constraint presentationC = cantidadClases.getConstraint();
		cantidadClases.setConstraint("");
		cantidadClases.setText("");
		cantidadClases.setConstraint(presentationC);
		
	}
	
	@Override
	protected Listitem createItem(ActividadYClase item) {
		Listitem rowProduct = new Listitem();
		rowProduct.appendChild(new Listcell(String.valueOf(item.getActiv().getIdTipoCurso())));
		rowProduct.appendChild(new Listcell(String.valueOf(item.getActiv().getNombre())));
		rowProduct.appendChild(new Listcell(String.valueOf(item.getCantClases())));
		rowProduct.setValue(item);
		rowProduct.addForward(Events.ON_DOUBLE_CLICK,(Component) null,getUpdateMethod());
		addIDUEvent(rowProduct);
		return rowProduct;
	}
	
	@Override
	protected ActividadYClase createObject() {
		ActividadYClase dpp = new ActividadYClase();
		TipoCursoEnum tipoActividad = (TipoCursoEnum) this.tipoActividad.getSelectedItem().getValue();
		Actividad actividad = (Actividad) this.actividad.getSelectedItem().getValue();
		dpp.setActiv(actividad);	
		dpp.setCantClases((Integer)this.cantidadClases.getSelectedItem().getValue());
		if (Sessions.getCurrent().getAttribute("idCurso") != null) {
			Curso cur=(Curso)Sessions.getCurrent().getAttribute("idCurso"); 
			dpp.setCurso(cur);
		}
	
		return dpp;
	}
	
	@Override
	protected ActividadYClase fillObject(ActividadYClase item) {
		return item;
	}
	
	@Override
	public GridList getGridList() {
		return productListBox;
	}
	@Override
	protected String getPanelTitle() {
		return I18N.getLabel("curso.clasesysessiones");
	}
	
	@Override
	protected boolean itemExists(Listitem item) {
		return false;
	}
	
	@Override
	protected boolean insert(){

		getGridList().removeAll();
		super.insert();
		actualizarTipoCurso();
		return true;
	}

	private void actualizarTipoCurso(){
		getDelegate().actualizarTipoCurso();
	}
	
	@Override
	protected void delete(){
		super.delete();
		actualizarTipoCurso();
	}

	public Set<ActividadYClase> getProducts() {
		ActividadYClase dpp = null;
		Set<ActividadYClase> dpps = new HashSet<ActividadYClase>();
		for (Object row : productListBox.getItems()) {
			dpp = (ActividadYClase)((Listitem)row).getValue();
			dpps.add(dpp);
		}
		return dpps;
	}
	
	@Override
	protected GridCrud makeFields() {
		
		productCrudGrid = new GridCrud();
		productCrudGrid.setFixedLayout(true);
		productCrudGrid.setWidth("auto");
				
		tipoActividad =new Combobox();
		tipoActividad.setConstraint("strict");
		tipoActividad.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt) {
				if(tipoActividad.getSelectedItem() != null){
					
					VencimientoCursoEnum vencimientoEnum=getDelegate().getTipoCursoEnum();
					//  si el vencimiento seleccionado arriba es igual a semanal, entonces acoto las actividades  
					if(vencimientoEnum != null && 
							(vencimientoEnum.toInt()== VencimientoCursoEnum.SEMANAL_VENCE_AL_MES.toInt() 
							|| vencimientoEnum.toInt()== VencimientoCursoEnum.SEMANAL_VENCE_A_LOS_3_MES.toInt()
							||   vencimientoEnum.toInt() == VencimientoCursoEnum.SEMANAL_VENCE_AL_ANo.toInt()
							|| vencimientoEnum.toInt()== VencimientoCursoEnum.SEMANAL_VENCE_A_LA_SEMANA.toInt())){
						List<Actividad> listActividades=cursoEJB.findAllActividadQueUsaCarnet(((TipoCursoEnum)tipoActividad.getSelectedItem().getValue()).toInt());
						if(listActividades != null){
							setActividad(getComboBoxActividad(listActividades, getActividad()));						
						}
						
						
					}else{
						List<Actividad> actividades=mapaActividadesPorTipoActividad.get(((TipoCursoEnum)tipoActividad.getSelectedItem().getValue()).toInt());
						if(actividades != null){
							setActividad(getComboBoxActividad(actividades, getActividad()));						
						}	
					}
				}
			}
		});	
		productCrudGrid.addField(new Label(I18N.getLabel("curso.tipoActividad")), tipoActividad);
	
		List<Actividad> actividades=cursoEJB.findAllActividad();
		mapaActividadesPorTipoActividad= new HashMap<Integer, List<Actividad>> ();
		mapaActividadesPorTipoActividad.put(TipoCursoEnum.ACTIVIDADES_FISICAS.toInt(), new ArrayList<Actividad>());
		mapaActividadesPorTipoActividad.put(TipoCursoEnum.MASAJES.toInt(), new ArrayList <Actividad>());
		mapaActividadesPorTipoActividad.put(TipoCursoEnum.TRATAMIENTOS_KINESICOS.toInt(), new ArrayList<Actividad>());
		mapaActividadesPorTipoActividad.put(TipoCursoEnum.NATACION.toInt(), new ArrayList<Actividad>());
		mapaActividadesPorTipoActividad.put(TipoCursoEnum.OTROS.toInt(), new ArrayList<Actividad>());
		for (Actividad actividad : actividades) {
			mapaActividadesPorTipoActividad.get(actividad.getIdTipoCurso().toInt()).add(actividad);		
		}
		
		actividad = new Combobox();
		actividad.setConstraint("strict");

		productCrudGrid.addField(new RequiredLabel(I18N.getLabel("curso.actividad")),actividad);
		
		cantidadClases= new Combobox();
		cantidadClases.setConstraint("strict");
		
		productCrudGrid.addField(new RequiredLabel(I18N.getLabel("actividad.cant")),cantidadClases);

		return productCrudGrid;
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
			// cb.appendChild(item);
			act.appendChild(item);		
		}

		return act;
	}
	
	@Override
	protected GridList makeList() {
		productListBox = new GridList();
		productListBox.setMultiple(false);
		productListBox.setPageSize(5);
		productListBox.addHeader((new Listheader(I18N.getLabel("curso.tipoActividad"))));
		productListBox.addHeader((new Listheader(I18N.getLabel("curso.actividad"))));
		productListBox.addHeader((new Listheader(I18N.getLabel("actividad.cant"))));
		productListBox.setHeight("150px"); 
		return productListBox;
	}
	
	public void setSelectedTipoActividad (TipoCursoEnum selectedHPType){
		boolean found = false;
		int i = 0;
		while (!found && i<tipoActividad.getItemCount()){
			if(selectedHPType.toInt() == (((TipoCursoEnum) tipoActividad.getItemAtIndex(i).getValue()).toInt())){
				found = true;
				tipoActividad.setSelectedIndex(i);
			}
			i++;
		}
		if(found){
			List<Actividad> actividades=mapaActividadesPorTipoActividad.get(((TipoCursoEnum)tipoActividad.getSelectedItem().getValue()).toInt());
			if(actividades != null){
				setActividad(getComboBoxActividad(actividades, actividad));						
			}
		}
	}
	
	public void setSelectedCantidadClases (int selectedHPType){
		boolean found = false;
		int i = 0;
		while (!found && i<cantidadClases.getItemCount()){
			if(selectedHPType  == (((Integer) cantidadClases.getItemAtIndex(i).getValue()))){
				found = true;
				cantidadClases.setSelectedIndex(i);
			}
			i++;
		}
	}
	
	public void setSelectedActividad (Actividad selectedHPType){
		boolean found = false;
		int i = 0;
		while (!found && i<actividad.getItemCount()){
			if(selectedHPType.getId().intValue() == (((Actividad) actividad.getItemAtIndex(i).getValue()).getId().intValue())){
				found = true;
				actividad.setSelectedIndex(i);
			}
			i++;
		}
	}

	@Override
	public void updateFields() {
		if (productListBox.getSelectedItem() == null)
			return;
		ActividadYClase dpp =(ActividadYClase)productListBox.getSelectedItem().getValue();
		
		//setSelectedTipoActividad(TipoCursoEnum.fromInt(dpp.getActiv().getIdTipoCurso().toInt()));		
		setSelectedTipoActividad(dpp.getActiv().getIdTipoCurso());
		
		if(tipoActividad.getSelectedItem() != null){
			List<Actividad> actividades=mapaActividadesPorTipoActividad.get(((TipoCursoEnum)tipoActividad.getSelectedItem().getValue()).toInt());
			if(actividades != null){
				setActividad(getComboBoxActividad(actividades, getActividad()));						
			}
		}
		
		setSelectedActividad(dpp.getActiv());
		
		setSelectedCantidadClases(dpp.getCantClases());
	}
	
	public void updateProductTotalDelivery() {
		Set<ActividadYClase> dppBackup = getProducts();
		this.getGridList().removeAll();
		getProducts().addAll(dppBackup);
		this.fillListBox(dppBackup);
	}

	@Override
	protected ActividadYClase updateList() {
		
		if (productListBox.getSelectedItem() == null)
			return null;
		
		if(tipoActividad.getSelectedItem() == null ||  actividad.getSelectedItem() == null || cantidadClases.getSelectedItem() == null)
			return null;
		
		ActividadYClase newActionProduct =(ActividadYClase)productListBox.getSelectedItem().getValue();
		
		
		newActionProduct.setActiv((Actividad)actividad.getSelectedItem().getValue());
		
		newActionProduct.setCantClases((Integer)cantidadClases.getSelectedItem().getValue());
		
		if (Sessions.getCurrent().getAttribute("idCurso") != null) {
			Curso cur=(Curso)Sessions.getCurrent().getAttribute("idCurso"); 
			newActionProduct.setCurso(cur);
		}
		
		actualizarTipoCurso();
		return newActionProduct;
	}
	
	@Override
	protected boolean validateCrud() {
			
		if (getTipoActividad().getSelectedItem()==null) {
				throw new WrongValueException(getTipoActividad(), I18N.getLabel("error.empty.field"));
		}
		if (getActividad().getSelectedItem()==null) {
			throw new WrongValueException(getActividad(), I18N.getLabel("error.empty.field"));
		}
		if (getCantidadClases().getSelectedItem()==null) {
			throw new WrongValueException(getCantidadClases(), I18N.getLabel("error.empty.field"));
		}
		return true;
	}
	
	public String getUpdateMethod() {
		return updateMethod;
	}
	public void setUpdateMethod(String updateMethod) {
		this.updateMethod = updateMethod;
	}

	public Combobox getTipoActividad() {
		return tipoActividad;
	}

	public void setTipoActividad(Combobox tipoActividad) {
		this.tipoActividad = tipoActividad;
	}

	public Combobox getActividad() {
		return actividad;
	}

	public void setActividad(Combobox actividad) {
		this.actividad = actividad;
	}

	public Combobox getCantidadClases() {
		return cantidadClases;
	}

	public void setCantidadClases(boolean isLibre, boolean clear) {
		setCantidadClases(getComboBox(cantidadClases, isLibre));
		
		setTipoCursos(getComboBoxTipoActividad(tipoActividad, isLibre, null));
		
		if(clear){
		// elimino todo lo que tengo en la lista
		 productListBox.getItems().clear();
		}
	}
	
	public void setCantidadClasesPrimeros10Dias(VencimientoCursoEnum vencimientoEnum, boolean clear) {
		setCantidadClases(getComboBox10Dias(cantidadClases));
		setTipoCursos(getComboBoxTipoActividad(tipoActividad, false, vencimientoEnum));
		
		if(clear){
		// elimino todo lo que tengo en la lista
		 productListBox.getItems().clear();
		}
	}
	
	public void setearClasesCantidadesSemanales(boolean clear) {
		// mostrar todas las actividades que tengan configurado usaCarnet en True
		List<Actividad> listActividades=cursoEJB.findAllActividadQueUsaCarnet(null);
		
		setCantidadClases(getComboBoxCantClases(cantidadClases, false));
		setTipoCursos(getComboBoxTipoActividad(tipoActividad, listActividades));
		if(clear){
		// elimino todo lo que tengo en la lista
		 productListBox.getItems().clear();
		}
	}
	
	public void setCantidadClases(Combobox cantidadClases) {
		this.cantidadClases = cantidadClases;
	}
	public void setTipoCursos(Combobox cantidadClases) {
		this.tipoActividad = cantidadClases;
	}

	public void setCursos(Combobox cantidadClases) {
		this.actividad = cantidadClases;
	}

	
	public Combobox getComboBoxCantClases(Combobox combo, boolean isLibre) {
		Constraint brandC = combo.getConstraint();
		combo.setConstraint("");
		combo.setText("");
		combo.setConstraint(brandC);
		combo.getItems().clear();
		
		if(isLibre){
			combo.setConstraint("strict");
			
			Comboitem item;
			item = new Comboitem("LIBRE");
			item.setValue(99);
			combo.appendChild(item);		
		}else{
			
			for(int i=2 ; i< 24 ; i++){
				Comboitem item;
				item = new Comboitem(String.valueOf(i));
				item.setValue(i);			
				combo.appendChild(item);		
			}
			
		}
		return combo;
	}
	
	public Combobox getComboBox(Combobox combo, boolean isLibre) {
		Constraint brandC = combo.getConstraint();
		combo.setConstraint("");
		combo.setText("");
		combo.setConstraint(brandC);
		combo.getItems().clear();
		
		if(isLibre){
			combo.setConstraint("strict");
			
			Comboitem item;
			item = new Comboitem("LIBRE");
			item.setValue(99);
			combo.appendChild(item);		
		}else{
			
			for(int i=1 ; i< 24 ; i++){
				Comboitem item;
				item = new Comboitem(String.valueOf(i));
				item.setValue(i);			
				combo.appendChild(item);		
			}
			
		}
		return combo;
	}
	
	public Combobox getComboBox10DiasEsQuincenal(Combobox combo) {
		Constraint brandC = combo.getConstraint();
		combo.setConstraint("");
		combo.setText("");
		combo.setConstraint(brandC);
		combo.getItems().clear();
		Comboitem item;
		item = new Comboitem(String.valueOf(10));
		item.setValue(10);			
		combo.appendChild(item);		
		return combo;
	}
	
	public Combobox getComboBox10Dias(Combobox combo) {
		Constraint brandC = combo.getConstraint();
		combo.setConstraint("");
		combo.setText("");
		combo.setConstraint(brandC);
		combo.getItems().clear();
		Comboitem item;
		item = new Comboitem(String.valueOf(10));
		item.setValue(10);			
		combo.appendChild(item);		
		return combo;
	}
	
	public Combobox getComboBoxTipoActividad(Combobox combo, boolean isLibre, VencimientoCursoEnum vencimientoEnum) {
		Constraint brandC = combo.getConstraint();
		combo.setConstraint("");
		combo.setText("");
		combo.setConstraint(brandC);
		combo.getItems().clear();
		
		if(isLibre || (vencimientoEnum != null && vencimientoEnum.toInt() == VencimientoCursoEnum.QUINCENAL_VENCE_A_LA_QUINCENA.toInt())){
			
			Comboitem item;
			item = new Comboitem(TipoCursoEnum.NATACION.toString());
			item.setValue(TipoCursoEnum.NATACION);
			combo.appendChild(item);
			
			item = new Comboitem(TipoCursoEnum.MASAJES.toString());
			item.setValue(TipoCursoEnum.MASAJES);
			combo.appendChild(item);

			item = new Comboitem(TipoCursoEnum.ACTIVIDADES_FISICAS.toString());
			item.setValue(TipoCursoEnum.ACTIVIDADES_FISICAS);
			combo.appendChild(item);

			item = new Comboitem(TipoCursoEnum.OTROS.toString());
			item.setValue(TipoCursoEnum.OTROS);
			combo.appendChild(item);

		}else{
			
			Comboitem item;
			item = new Comboitem(TipoCursoEnum.NATACION.toString());
			item.setValue(TipoCursoEnum.NATACION);
			combo.appendChild(item);

			item = new Comboitem(TipoCursoEnum.TRATAMIENTOS_KINESICOS.toString());
			item.setValue(TipoCursoEnum.TRATAMIENTOS_KINESICOS);
			combo.appendChild(item);
			
			item = new Comboitem(TipoCursoEnum.MASAJES.toString());
			item.setValue(TipoCursoEnum.MASAJES);
			combo.appendChild(item);

			item = new Comboitem(TipoCursoEnum.ACTIVIDADES_FISICAS.toString());
			item.setValue(TipoCursoEnum.ACTIVIDADES_FISICAS);
			combo.appendChild(item);

			item = new Comboitem(TipoCursoEnum.OTROS.toString());
			item.setValue(TipoCursoEnum.OTROS);
			combo.appendChild(item);

		}
		
		Constraint brandC2 = actividad.getConstraint();
		actividad.setConstraint("");
		actividad.setText("");
		actividad.setConstraint(brandC2);
		actividad.getItems().clear();
		return combo;
	}

	
	public boolean existeTipo(Combobox combo, TipoCursoEnum tipo){
		Iterator<Comboitem> itCursos =combo.getItems().iterator();
		while (itCursos.hasNext()) {
			Comboitem cursosDeLaLista=(Comboitem)itCursos.next();
			TipoCursoEnum cursoSeleccionado=(TipoCursoEnum)cursosDeLaLista.getValue();
			if(cursoSeleccionado.toInt() == tipo.toInt() ){
				return true;
			}
		}
	return false;	
	}	

	
	public Combobox getComboBoxTipoActividad(Combobox combo, List<Actividad> listActividades) {
		Constraint brandC = combo.getConstraint();
		combo.setConstraint("");
		combo.setText("");
		combo.setConstraint(brandC);
		combo.getItems().clear();

		if(listActividades != null){
			for (Actividad actividad : listActividades) {
				
				if(!existeTipo(combo, actividad.getIdTipoCurso())){
					Comboitem item;
					item = new Comboitem(actividad.getIdTipoCurso().toString());
					item.setValue(actividad.getIdTipoCurso());
					combo.appendChild(item);
				}
			}
		}
				
		Constraint brandC2 = actividad.getConstraint();
		actividad.setConstraint("");
		actividad.setText("");
		actividad.setConstraint(brandC2);
		actividad.getItems().clear();
		return combo;
	}

	@Override
	protected Listitem createItem2(ClienteNoEncontradoEnPiletaHistorico item) {
		// TODO Auto-generated method stub
		return null;
	}
}