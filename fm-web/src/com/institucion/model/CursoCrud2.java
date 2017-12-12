package com.institucion.model;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Intbox;

import com.institucion.desktop.delegated.CursosDelegate;
import com.institucion.desktop.helper.BooleanViewHelper;
import com.institucion.desktop.helper.CursosVencimientoViewHelper;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridCrud;
import com.institucion.fm.desktop.view.RequiredLabel;

public class CursoCrud2 extends GridCrud{

	private static final long serialVersionUID = 1L;
	private Intbox precio;
	private Combobox vencimiento;
	private CursosDelegate delegate;
	private Combobox esConCodigosBarras;

	private RequiredLabel req;
	private Intbox cantClasesCursosCursosSemanales;

	public CursoCrud2 (){
		super();
		this.makeFields();
	}
	
	    
	private void makeFields(){

		esConCodigosBarras = BooleanViewHelper.getComboBox();
		esConCodigosBarras.setConstraint("strict");
		this.addField(new RequiredLabel("Es con codigos Barras?"), esConCodigosBarras);

		vencimiento = CursosVencimientoViewHelper.getComboBox();
		vencimiento.setConstraint("strict");
		vencimiento.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt) {
				
				if(vencimiento.getSelectedItem() != null){
					VencimientoCursoEnum vencimientoEnum= (VencimientoCursoEnum) vencimiento.getSelectedItem().getValue() ;
					if(vencimientoEnum.toInt() == VencimientoCursoEnum.LIBRE_VENCE_A_LA_QUINCENA.toInt() || 
							vencimientoEnum.toInt() == VencimientoCursoEnum.LIBRE_VENCE_A_LA_SEMANA.toInt() ||
							vencimientoEnum.toInt() == VencimientoCursoEnum.LIBRE_VENCE_A_LOS_3_MES.toInt() ||
							vencimientoEnum.toInt() == VencimientoCursoEnum.LIBRE_VENCE_AL_MES.toInt()){
						
						cantClasesCursosCursosSemanales.setVisible(false);
						req.setVisible(false);
						cantClasesCursosCursosSemanales.setValue(0);
						delegate.setearClasesCantidades(true, true);
						
					}else{
						if(vencimientoEnum.toInt()== VencimientoCursoEnum.QUINCENAL_VENCE_A_LA_QUINCENA.toInt() ){
							delegate.setearPrimeros10DiasDeClasesCantidades(vencimientoEnum, true);
							cantClasesCursosCursosSemanales.setVisible(false);
							req.setVisible(false);
							cantClasesCursosCursosSemanales.setValue(0);

						}else if(vencimientoEnum.toInt()== VencimientoCursoEnum.SEMANAL_VENCE_AL_MES.toInt() 
								||   vencimientoEnum.toInt() == VencimientoCursoEnum.SEMANAL_VENCE_AL_ANo.toInt()
								|| vencimientoEnum.toInt()== VencimientoCursoEnum.SEMANAL_VENCE_A_LOS_3_MES.toInt()
								|| vencimientoEnum.toInt()== VencimientoCursoEnum.SEMANAL_VENCE_A_LA_SEMANA.toInt()){
							cantClasesCursosCursosSemanales.setVisible(true);
							req.setVisible(true);
							cantClasesCursosCursosSemanales.setValue(0);
							delegate.setearClasesCantidadesSemanales(true);
						}else{
							cantClasesCursosCursosSemanales.setVisible(false);
							req.setVisible(false);
							cantClasesCursosCursosSemanales.setValue(0);
							delegate.setearClasesCantidades(false, true);
						}
					}
				}
			}
		});	
		
		this.addField(new RequiredLabel(I18N.getLabel("curso.vencimiento")), vencimiento);

		precio = new Intbox();
		this.addField(new RequiredLabel(I18N.getLabel("curso.precio")), precio);
	
		req=new RequiredLabel("Cantidad de Clases Semanales");
		cantClasesCursosCursosSemanales= new Intbox();
		cantClasesCursosCursosSemanales.setVisible(false);
		req.setVisible(false);
		this.addField(req, cantClasesCursosCursosSemanales);
		
	}

	public void setSelectedUsaCodigoBarras (boolean tieneCertficadobool){
		boolean found = false;
		int i = 0;
		while(!found && i<esConCodigosBarras.getItemCount()){
			if(esConCodigosBarras.getItemAtIndex(i) != null 
					&&(Boolean)esConCodigosBarras.getItemAtIndex(i).getValue() == tieneCertficadobool){
				found = true;
				esConCodigosBarras.setSelectedIndex(i);
			}
			i++;
		}
	}	
	
	public void setSelectedVencimiento (VencimientoCursoEnum selectedHPType){
		boolean found = false;
		int i = 0;
		while (!found && i<vencimiento.getItemCount()){
			if(selectedHPType.toInt() == (((VencimientoCursoEnum) vencimiento.getItemAtIndex(i).getValue()).toInt())){
				found = true;
				vencimiento.setSelectedIndex(i);
			}
			i++;
		}
	}

	public Intbox getPrecio() {
		return precio;
	}

	public void setPrecio(Intbox precio) {
		this.precio = precio;
	}

	public Combobox getVencimiento() {
		return vencimiento;
	}

	public void setVencimiento(Combobox vencimiento) {
		this.vencimiento = vencimiento;
	}


	public CursosDelegate getDelegate() {
		return delegate;
	}


	public void setDelegate(CursosDelegate delegate) {
		this.delegate = delegate;
	}


	public RequiredLabel getReq() {
		return req;
	}


	public void setReq(RequiredLabel req) {
		this.req = req;
	}


	public Intbox getCantClasesCursosCursosSemanales() {
		return cantClasesCursosCursosSemanales;
	}


	public void setCantClasesCursosCursosSemanales(
			Intbox cantClasesCursosCursosSemanales) {
		this.cantClasesCursosCursosSemanales = cantClasesCursosCursosSemanales;
	}


	public Combobox getEsConCodigosBarras() {
		return esConCodigosBarras;
	}


	public void setEsConCodigosBarras(Combobox esConCodigosBarras) {
		this.esConCodigosBarras = esConCodigosBarras;
	}

}
