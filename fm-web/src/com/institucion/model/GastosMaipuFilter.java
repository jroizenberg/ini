package com.institucion.model;

import java.util.List;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;

import com.institucion.bz.ClaseEJB;
import com.institucion.desktop.helper.GastosViewHelper;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridFilter;
import com.institucion.fm.desktop.view.RequiredLabel;
import com.institucion.fm.filteradv.model.CriteriaClause;

public class GastosMaipuFilter extends GridFilter {
	private static final long serialVersionUID = 1L;

	private Combobox anio;   
	private Combobox quincena;
	private Combobox tipoGastoCB;   
	private ClaseEJB claseEJB;

	public GastosMaipuFilter()	{
		super();
		claseEJB = BeanFactory.<ClaseEJB>getObject("fmEjbClase");

		buildFilter();
	}
	
	private void buildFilter() {
				
		Row row3 = new Row();
			
		String estadoLabel =  "Temporada";
		row3.appendChild(new RequiredLabel(estadoLabel));
		anio = GastosViewHelper.getComboBoxAnio();
		anio.setConstraint("strict");
		row3.appendChild(anio);
		
		quincena= new Combobox();
		quincena.setConstraint("strict");
//		quincena.setVisible(false);
		quincena.setSelectedItem(null);				

		List<Quincena> nombreCursos=claseEJB.findAllConActividadTomaListaDelDiaNombreCurso();
		
		if(nombreCursos != null){
			getComboBoxQuincenas(nombreCursos);						
		}
		row3.appendChild(new RequiredLabel("Quincena"));
		row3.appendChild(quincena);
		this.addRow(row3);
		
		String tipoGasto =  I18N.getLabel("gastos.tipogasto");
		row3.appendChild(new Label(tipoGasto));
//		tipoGastoCB = GastosViewHelper.getComboBoxTipoGastoMaipu(true);
		tipoGastoCB.setConstraint("strict");
		row3.appendChild(tipoGastoCB);
		
		this.addRow(row3);
	}
	
	
	public  Combobox getComboBoxQuincenas(List<Quincena> cursos) {
		Constraint brandC = quincena.getConstraint();
		quincena.setConstraint("");
		quincena.setText("");
		quincena.setConstraint(brandC);
		quincena.getItems().clear();

		if(cursos != null){
			for (Quincena classe : cursos) {
				Comboitem item;
				item = new Comboitem("MAIPU: "+classe.getNombre());
				item.setValue(classe);
				quincena.appendChild(item);	
			}	
		}
		return quincena;
	}
	
	public CriteriaClause getCriteriaFilters(){
		CriteriaClause criteria = new CriteriaClause();

		super.setPredicate(criteria, quincena, "nombre");
		
		return criteria;
	}

	public boolean validateHaveFilters(){
	
		if (anio.getSelectedIndex() >= 0) {
			return true;
		}

		if (quincena.getSelectedIndex() >= 0) {
			return true;		}

		if (tipoGastoCB.getSelectedIndex() >= 0) {
			return true;		
		}
		
		return false;
	}
	
	public String getFilters(){
		
		StringBuilder actionConditions= new StringBuilder("select gas.id  from gastosmaipu gas   ");
			actionConditions.append("where 1=1  ");
		
			if (anio.getSelectedIndex() >= 0) {
				Long stateType= ((Long)anio.getSelectedItem().getValue());
				actionConditions.append(" and gas.anio= "+stateType);
			}
			
			if (quincena.getSelectedIndex() >= 0) {
				Quincena stateType= ((Quincena)quincena.getSelectedItem().getValue());
				actionConditions.append(" and gas.quincena= "+stateType.getId());
			}
			
			if (tipoGastoCB.getSelectedIndex() >= 0) {
				if(tipoGastoCB.getSelectedItem().getValue() instanceof GastosMaipuEnum){
					GastosMaipuEnum stateType= ((GastosMaipuEnum)tipoGastoCB.getSelectedItem().getValue());
					actionConditions.append(" and gas.tipogasto= '"+stateType.toInt()+"' ");	
				}
			}
			
			actionConditions.append(" order by gas.anio, gas.quincena desc , gas.tipogasto ");
			
		return actionConditions.toString();
	}
	
	
	public void clear(){
		Constraint c;
	
		c= anio.getConstraint();
		anio.setConstraint("");
		anio.setSelectedItem(null);
		anio.setConstraint(c);
		
		c= quincena.getConstraint();
		quincena.setConstraint("");
		quincena.setSelectedItem(null);
		quincena.setConstraint(c);
		
		c= tipoGastoCB.getConstraint();
		tipoGastoCB.setConstraint("");
		tipoGastoCB.setSelectedItem(null);
		tipoGastoCB.setConstraint(c);
	}

	public Combobox getAnio() {
		return anio;
	}

	public void setAnio(Combobox anio) {
		this.anio = anio;
	}

	public Combobox getTipoGastoCB() {
		return tipoGastoCB;
	}

	public void setTipoGastoCB(Combobox tipoGastoCB) {
		this.tipoGastoCB = tipoGastoCB;
	}

	public Combobox getQuincena() {
		return quincena;
	}

	public void setQuincena(Combobox quincena) {
		this.quincena = quincena;
	}


}