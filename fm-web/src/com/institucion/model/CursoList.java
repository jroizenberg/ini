package com.institucion.model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import com.institucion.desktop.helper.BooleanViewHelper;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridList;

public class CursoList extends GridList {
	private static final long serialVersionUID = 1L;
	DecimalFormat formateador = new DecimalFormat("###,###");

	public CursoList() {
		super();

//		super.addHeader(new Listheader("Curso/ Tratamiento")).setWidth("15%");
//		super.addHeader(new Listheader("Tipo actividad")).setWidth("15%");
		super.addHeader(new Listheader(I18N.getLabel("curso.nombre"))).setWidth("35%");
		super.addHeader(new Listheader(I18N.getLabel("curso.pagaSubscripcion"))).setWidth("8%");
		super.addHeader(new Listheader(I18N.getLabel("curso.precio"))).setWidth("7%");
		super.addHeader(new Listheader(I18N.getLabel("curso.vencimiento"))).setWidth("21%");
		super.addHeader(new Listheader("Cant. Clases")).setWidth("6%");
		super.addHeader(new Listheader("Disponible")).setWidth("8%");
	}

	public void setList(List<Curso> cursoList, boolean isFromSubs) {
		this.setMultiple(false);

		if(isFromSubs){
			this.setPageSize(15);
		}else{
//			this.setMultiple(true);
			this.setPageSize(24);
		}

		super.removeAll();
		if(cursoList != null){
			Iterator<Curso> itPharmacy = cursoList.iterator();

			while (itPharmacy.hasNext()) {
				Curso curso = itPharmacy.next();

				// Por cada registro lo meto abajo
				ActividadYClase actYClase= (ActividadYClase)new ArrayList(curso.getActividadYClaseList()).get(0);
				
				Listitem row = new Listitem();
				row.setValue(curso);

				Listcell nombrecell = new Listcell(curso.getNombre().toUpperCase());
				row.appendChild(nombrecell);

				Listcell pagasubscell = new Listcell(BooleanViewHelper.getBooleanString(curso.isPagaSubscripcion()));
				row.appendChild(pagasubscell);

				Listcell preciosubscell = new Listcell("$"+formateador.format(curso.getPrecio()));
				row.appendChild(preciosubscell);
				
				Listcell vencsubscell = new Listcell(String.valueOf(VencimientoCursoEnum.toString(curso.getVencimiento().toInt())));
				row.appendChild(vencsubscell);
							
				Listcell cantd=null;
				if(actYClase.getCantClases() == 99 
						|| curso.getVencimiento().toInt() == VencimientoCursoEnum.LIBRE_VENCE_A_LA_QUINCENA.toInt() 
						|| curso.getVencimiento().toInt() == VencimientoCursoEnum.LIBRE_VENCE_A_LA_SEMANA.toInt()
						|| curso.getVencimiento().toInt() == VencimientoCursoEnum.LIBRE_VENCE_A_LOS_3_MES.toInt()
						|| curso.getVencimiento().toInt() == VencimientoCursoEnum.LIBRE_VENCE_AL_MES.toInt()){
					
					cantd=new Listcell(String.valueOf("LIBRE"));

				}else{
					cantd=new Listcell(String.valueOf(actYClase.getCantClases()));
				}
				row.appendChild(cantd);
				
				Listcell pagasubscell4 = new Listcell(BooleanViewHelper.getBooleanString(curso.isDisponible()));

				if(!curso.isDisponible()){
					// pintar en rojo y negrita
					pagasubscell4.setStyle("color:#FF0000 !important; font-weight:bold; ");
				}else{
					// pintar en verde y negrita
					pagasubscell4.setStyle("color:#0B610B !important; font-weight:bold; ");
				}
				row.appendChild(pagasubscell4);
				
				if(isFromSubs){
					Component nullComponent = null;
					row.addForward("onClick",nullComponent,"onClickSubscripcionesEvt");
					super.addRow2(row);
				}else
					super.addRow(row);
			}
		}
	}			
}