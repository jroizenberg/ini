package com.institucion.model;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

import com.institucion.desktop.delegated.CursosCrudDelegate;
import com.institucion.desktop.helper.BooleanViewHelper;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.validator.TextConstraint;
import com.institucion.fm.desktop.view.GridFilter;
import com.institucion.fm.desktop.view.RequiredLabel;
import com.institucion.fm.filteradv.model.CriteriaClause;

public class ProductosFilter extends GridFilter {
	private static final long serialVersionUID = 1L;
	private Textbox descripcion;
	private Combobox conStock;
	private Textbox codigo;
	private CursosCrudDelegate delegate;
	
	
	public ProductosFilter()	{
		super();
		buildFilter();
	}

	private void buildFilter() {
		
		Row row1 = new Row();

		String nameLabel2 = I18N.getLabel("producto.codigo");
		row1.appendChild(new RequiredLabel(nameLabel2));

		codigo = new Textbox();
		codigo.setConstraint(new TextConstraint());
		codigo.addEventListener(Events.ON_CHANGING, new EventListener() {
			public void onEvent(Event evt){
				codigo.setValue(((InputEvent)evt).getValue());	
				delegate.buscar();
			}
		});	

		row1.appendChild(codigo);

		String descripcionLabel = I18N.getLabel("producto.descripcion");
		row1.appendChild(new RequiredLabel(descripcionLabel));
	
		descripcion = new Textbox();
		descripcion.setConstraint(new TextConstraint());
		descripcion.addEventListener(Events.ON_CHANGING, new EventListener() {
			public void onEvent(Event evt){
				descripcion.setValue(((InputEvent)evt).getValue());	
				delegate.buscar();
			}
		});	
		row1.appendChild(descripcion);
		
		String estadoLabel =  I18N.getLabel("producto.stock");
		row1.appendChild(new Label(estadoLabel));
		conStock = BooleanViewHelper.getComboBox();
		conStock.setConstraint("strict");
		conStock.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
				delegate.buscar();
			}
		});	
		row1.appendChild(conStock);
		
		
		this.addRow(row1);

	}

	
	public boolean validateHaveFilters(){
		if (descripcion.getValue()!= null && !descripcion.getValue().trim().equals("") && !descripcion.getValue().trim().equals(" ")) {
			return true;
		}

		if (codigo.getValue()!= null && !codigo.getValue().trim().equals("") && !codigo.getValue().trim().equals(" ")) {
			return true;
		}
		
		if (conStock.getSelectedIndex() >= 0) {
			return true;
		}
	
		return false;
	}
	
	public String getFilters(){
					
		StringBuilder actionConditions= new StringBuilder( "select * from producto producto   ");
				actionConditions.append(" where 1=1  ");
				
				if (descripcion.getValue()!= null && !descripcion.getValue().trim().equals("") && !descripcion.getValue().trim().equals(" ")) {
					actionConditions.append(" and  Upper(producto.descripcion) like Upper('%"+descripcion.getValue()+"%') ");
				}
				
				if (codigo.getValue()!= null && !codigo.getValue().trim().equals("") && !codigo.getValue().trim().equals(" ")) {
					actionConditions.append(" and  Upper(producto.codigo) like Upper('%"+codigo.getValue()+"%') ");
				}
				
				if (conStock.getSelectedIndex() >= 0) {
					Boolean stateType= ((Boolean)conStock.getSelectedItem().getValue());
					if(stateType)
						actionConditions.append(" and producto.stock > 0");
					else
						actionConditions.append(" and producto.stock <= 0 ");
				}

				actionConditions.append(" order by producto.codigo ");						
		return actionConditions.toString();
	}

	
	public String getFilters2(){
		
		StringBuilder actionConditions= new StringBuilder( "select * from producto producto  order by producto.codigo ");
		return actionConditions.toString();
	}
	
	public CriteriaClause getCriteriaFilters(){
		CriteriaClause criteria = new CriteriaClause();

		super.setPredicate(criteria, codigo, "companyName");

		return criteria;
	}

	
	public void clear()	{
		Constraint c;
		c =descripcion.getConstraint();
		descripcion.setConstraint("");
		descripcion.setText(null);
		descripcion.setConstraint(c);
		
		c =codigo.getConstraint();
		codigo.setConstraint("");
		codigo.setText(null);
		codigo.setConstraint(c);
		
		c = conStock.getConstraint();
		conStock.setConstraint("");
		conStock.setSelectedItem(null);
		conStock.setConstraint(c);
				
	}

	public Textbox getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(Textbox descripcion) {
		this.descripcion = descripcion;
	}

	public Textbox getCodigo() {
		return codigo;
	}

	public void setCodigo(Textbox codigo) {
		this.codigo = codigo;
	}

	public CursosCrudDelegate getDelegate() {
		return delegate;
	}

	public void setDelegate(CursosCrudDelegate delegate) {
		this.delegate = delegate;
	}

}