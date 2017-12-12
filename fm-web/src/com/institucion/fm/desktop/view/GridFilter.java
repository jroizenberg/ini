package com.institucion.fm.desktop.view;

import java.util.Date;
import java.util.Iterator;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;

import com.institucion.fm.desktop.view.bb.CustomBandBox;
import com.institucion.fm.filteradv.dao.OperatorDAO;
import com.institucion.fm.filteradv.model.CriteriaClause;
import com.institucion.fm.filteradv.model.Predicate;

public abstract class GridFilter extends Grid {
	private static final long serialVersionUID = 1L;
	public static final String ID = "gridFilter";
	private String idBandBox;

	public GridFilter() {
		this.setId(ID);
		this.setWidth("auto");
		Rows rows = new Rows();
		this.appendChild(rows);
	}

	public void addRow(Row row) {
		this.getRows().appendChild(row);
	}

	public abstract CriteriaClause getCriteriaFilters();
	
	public abstract void clear();

	/**
	 * This method force change the field text, of comboBox for empty string. 
	 * @param combobox
	 */
	protected void forceClear(Combobox combobox) {
		Constraint constraint = combobox.getConstraint();
		combobox.setConstraint("");
		combobox.setText("");
		combobox.setConstraint(constraint);
	}

	protected void setPredicate(CriteriaClause clause, int enumValue,String fieldname) {
		Predicate predicate = new Predicate();
		predicate.setBitwise(Predicate.AND_BITWISE);
		predicate.setFieldType(Predicate.Type.LONG);
		predicate.setFieldName(fieldname);
		predicate.setExpression1(Integer.valueOf(enumValue).toString());
		predicate.setOperator(OperatorDAO.instance().findByName("equal"));
		clause.addPredicate(predicate);
	}

	protected void setPredicate(CriteriaClause clause, boolean booleanValue,String fieldname) {
		Predicate predicate = new Predicate();
		predicate.setBitwise(Predicate.AND_BITWISE);
		predicate.setFieldType(Predicate.Type.BOOLEAN);
		predicate.setFieldName(fieldname);
		predicate.setExpression1(booleanValue);
		predicate.setOperator(OperatorDAO.instance().findByName("equal"));
		clause.addPredicate(predicate);
	}

//	protected Predicate setPredicate(CriteriaClause clause, FEState state,	String fieldname) {
//		Predicate predicate = new Predicate();
//		predicate.setBitwise(Predicate.AND_BITWISE);
//		predicate.setFieldType(Predicate.Type.STATE);
//		predicate.setFieldName(fieldname);
//		// predicate.setExpression1(Integer.valueOf(state.toInt()).toString());
//		predicate.setExpression1(state.toString());
//		predicate.setOperator(OperatorDAO.instance().findByName("equal"));
//		clause.addPredicate(predicate);
//		return predicate;
//	}

	protected Predicate setPredicate(CriteriaClause clause, Textbox textbox,String fieldname) {
		if (textbox.getValue().length() > 0) {
			Predicate predicate = new Predicate();
			predicate.setBitwise(Predicate.AND_BITWISE);
			predicate.setFieldType(Predicate.Type.STRING);
			predicate.setFieldName(fieldname);
			predicate.setExpression1(textbox.getValue());
			predicate.setOperator(OperatorDAO.instance().findByName("like"));
			clause.addPredicate(predicate);
			return predicate;
		}
		return null;
	}

	protected void setPredicate(CriteriaClause clause, Textbox textbox,	String fieldname, String fieldname2) {
		if (textbox.getValue().length() > 0) {
			Predicate predicate = new Predicate();
			predicate.setBitwise(Predicate.OR_BITWISE);
			predicate.setFieldType(Predicate.Type.STRING);
			predicate.setFieldName(fieldname);
			predicate.setExpression1(textbox.getValue());
			predicate.setOperator(OperatorDAO.instance().findByName("like"));
			clause.addPredicate(predicate);

			Predicate predicate2 = new Predicate();
			predicate2.setBitwise(Predicate.AND_BITWISE);
			predicate2.setFieldType(Predicate.Type.STRING);
			predicate2.setFieldName(fieldname2);
			predicate2.setExpression1(textbox.getValue());
			predicate2.setOperator(OperatorDAO.instance().findByName("like"));
			clause.addPredicate(predicate2);
		}
	}

	protected void setPredicate(CriteriaClause clause, Textbox textbox,	String fieldname, String fieldname2, String bitwise1,String bitwise2) {
		if (textbox.getValue().length() > 0) {
			Predicate predicate = new Predicate();
			predicate.setBitwise(bitwise1);
			predicate.setFieldType(Predicate.Type.STRING);
			predicate.setFieldName(fieldname);
			predicate.setExpression1(textbox.getValue());
			predicate.setOperator(OperatorDAO.instance().findByName("like"));
			clause.addPredicate(predicate);

			Predicate predicate2 = new Predicate();
			predicate2.setBitwise(bitwise2);
			predicate2.setFieldType(Predicate.Type.STRING);
			predicate2.setFieldName(fieldname2);
			predicate2.setExpression1(textbox.getValue());
			predicate2.setOperator(OperatorDAO.instance().findByName("like"));
			clause.addPredicate(predicate2);
		}
	}

	protected void setPredicate(CriteriaClause clause, Datebox datebox,	String fieldname) {
		if (datebox.getText().length() > 0) {
			Predicate predicate = new Predicate();
			predicate.setBitwise(Predicate.AND_BITWISE);
			predicate.setFieldType(Predicate.Type.DATE);
			predicate.setFieldName(fieldname);
			predicate.setExpression1(datebox.getValue());
			predicate.setOperator(OperatorDAO.instance().findByName("equal"));
			clause.addPredicate(predicate);
		}
	}

	protected void setPredicate(CriteriaClause clause, String fieldname,Date fromDate, Date toDate) {
		Predicate predicate = new Predicate();
		predicate.setBitwise(Predicate.AND_BITWISE);
		predicate.setFieldType(Predicate.Type.DATE);
		predicate.setFieldName(fieldname);
		predicate.setExpression1(fromDate);
		predicate.setExpression2(toDate);
		predicate.setOperator(OperatorDAO.instance().findByName("between"));
		clause.addPredicate(predicate);
	}

	protected void setPredicate(CriteriaClause clause, Datebox datebox,	String fieldname, Datebox datebox2) {
		if (datebox.getText().length() > 0 && datebox2.getText().length() > 0) {
			Predicate predicate = new Predicate();
			predicate.setBitwise(Predicate.AND_BITWISE);
			predicate.setFieldType(Predicate.Type.DATE);
			predicate.setFieldName(fieldname);
			predicate.setExpression1(datebox.getValue());
			predicate.setExpression2(datebox2.getValue());
			predicate.setOperator(OperatorDAO.instance().findByName("between"));
			clause.addPredicate(predicate);
		}

		if (datebox2.getText().length() == 0 && datebox.getText().length() > 0) {
			Predicate predicate = new Predicate();
			predicate.setBitwise(Predicate.AND_BITWISE);
			predicate.setFieldType(Predicate.Type.DATE);
			predicate.setFieldName(fieldname);
			predicate.setExpression1(datebox.getValue());
			predicate.setOperator(OperatorDAO.instance().findByName(
					"greaterequalthan"));
			clause.addPredicate(predicate);

		}

		if (datebox.getText().length() == 0 && datebox2.getText().length() > 0) {
			Predicate predicate = new Predicate();
			predicate.setBitwise(Predicate.AND_BITWISE);
			predicate.setFieldType(Predicate.Type.DATE);
			predicate.setFieldName(fieldname);
			predicate.setExpression1(datebox2.getValue());
			predicate.setOperator(OperatorDAO.instance().findByName(
					"lessequalthan"));
			clause.addPredicate(predicate);

		}

	}

	protected void setPredicate(CriteriaClause clause, Intbox intbox,String fieldname) {
		if (intbox.getValue() != null) {
			Predicate predicate = new Predicate();
			predicate.setBitwise(Predicate.AND_BITWISE);
			predicate.setFieldType(Predicate.Type.INTEGER);
			predicate.setFieldName(fieldname);
			predicate.setExpression1(intbox.getValue().toString());
			predicate.setOperator(OperatorDAO.instance().findByName("equal"));
			clause.addPredicate(predicate);
		}
	}

	protected Predicate setPredicate(CriteriaClause clause, Enum<?> enumm,	String fieldname) {
		if (enumm != null) {
			Predicate predicate = new Predicate();
			predicate.setBitwise(Predicate.AND_BITWISE);
			predicate.setFieldType(Predicate.Type.ENUM);
			predicate.setFieldName(fieldname);
			predicate.setExpression1(enumm);
			predicate.setOperator(OperatorDAO.instance().findByName("equal"));
			clause.addPredicate(predicate);

			return predicate;
		}
		return null;
	}

	protected void setPredicate(CriteriaClause clause, String value,String fieldname) {
		if (value == null || "".equals(value)) {
			return;
		}
		Predicate predicate = new Predicate();
		predicate.setBitwise(Predicate.AND_BITWISE);
		predicate.setFieldType(Predicate.Type.STRING);
		predicate.setFieldName(fieldname);
		predicate.setExpression1(value);
		predicate.setOperator(OperatorDAO.instance().findByName("equal"));
		clause.addPredicate(predicate);
	}

	protected void setPredicate(CriteriaClause clause, Textbox textbox,	String fieldname, String fieldname2,String fieldname3, String bitwise1,	String bitwise2, String bitwise3) {
		if (textbox.getValue().length() > 0) {
			Predicate predicate = new Predicate();
			predicate.setBitwise(bitwise1);
			predicate.setFieldType(Predicate.Type.STRING);
			predicate.setFieldName(fieldname);
			predicate.setExpression1(textbox.getValue());
			predicate.setOperator(OperatorDAO.instance().findByName("like"));
			clause.addPredicate(predicate);

			Predicate predicate2 = new Predicate();
			predicate2.setBitwise(bitwise2);
			predicate2.setFieldType(Predicate.Type.STRING);
			predicate2.setFieldName(fieldname2);
			predicate2.setExpression1(textbox.getValue());
			predicate2.setOperator(OperatorDAO.instance().findByName("like"));
			clause.addPredicate(predicate2);

			Predicate predicate3 = new Predicate();
			predicate3.setBitwise(bitwise3);
			predicate3.setFieldType(Predicate.Type.STRING);
			predicate3.setFieldName(fieldname3);
			predicate3.setExpression1(textbox.getValue());
			predicate3.setOperator(OperatorDAO.instance().findByName("like"));
			clause.addPredicate(predicate3);
		}
	}
	
	protected void setPredicate(CriteriaClause clause, Combobox combobox,String fieldname) {
		if (combobox.getSelectedItem() != null) {
			Predicate predicate = new Predicate();
			predicate.setBitwise(Predicate.AND_BITWISE);

			Object value = combobox.getSelectedItem().getValue();
			Predicate.Type type = null;
			if (value instanceof String)
				type = Predicate.Type.STRING;
			else if (value instanceof Integer)
				type = Predicate.Type.INTEGER;
//			else if (value instanceof DomainTableDetail)
//				type = Predicate.Type.DT;
//			else if (value instanceof FEState)
//				type = Predicate.Type.STATE;
//			else if (value instanceof ActionType)
//				type = Predicate.Type.ENUM;
//			else if (value instanceof ProductType)
//				type = Predicate.Type.ENUM;
			else if (value instanceof Boolean)
				type = Predicate.Type.BOOLEAN;
			else if (value instanceof Long)
				type = Predicate.Type.LONG;
//			else if (value instanceof AttitudinalSegmentationType)
//				type = Predicate.Type.ENUM;
//			else if (value instanceof CorporativeLine)
//				type = Predicate.Type.CORPORATIVELINE;
//			else if (value instanceof PromotionLine)
//				type = Predicate.Type.PROMOTIONLINE;
//			else if (value instanceof HealthProfessionalContactType)
//				type = Predicate.Type.HEALTHPROF_CONTACT_TYPE;
			else
				throw new RuntimeException(
						"Modificar el metodo setPredicate para poder manejar el tipo de datos '" + value.getClass().getName() + "'");

			predicate.setFieldType(type);
			predicate.setFieldName(fieldname);
			predicate.setExpression1(value);
			predicate.setOperator(OperatorDAO.instance().findByName("equal"));
			clause.addPredicate(predicate);
//		} else if (fieldname.equalsIgnoreCase("state")){ //Si no hay filtro de estado, no trae los unificados
//			Predicate predicate = new Predicate();
//			predicate.setBitwise(Predicate.AND_BITWISE);
//			Object value = FEState.UNIFIED;
//			Predicate.Type type = null;
//			type = Predicate.Type.STATE;
//			predicate.setFieldType(type);
//			predicate.setFieldName(fieldname);
//			predicate.setExpression1(value);
//			predicate.setOperator(OperatorDAO.instance().findByName("notEqual"));
//			clause.addPredicate(predicate);
		}
	}

	
//	private boolean exist(Combobox combo, String value){
//		if(combo != null && combo.getItems() != null && value != null){
//			
//			for (Object comboit : combo.getItems()) {
//				
//				if(((String)(((Comboitem)comboit).getValue())).equalsIgnoreCase(value)){
//					return true;
//				}
//			} 			
//		}
//		return false;
//	}
	
	public void loadHistory() {
		String module = ((WindowSelector) this.getSpaceOwner().getFellow(WindowSelector.ID)).getEntity();
		String fieldId = null;
		@SuppressWarnings("unchecked")
		Iterator<Row> itrow = getRows().getChildren().iterator();
		while (itrow.hasNext()) {
			Row row = itrow.next();
			Iterator<?> itfield = row.getChildren().iterator();
			while (itfield.hasNext()) {
				Object component = itfield.next();
				if (component instanceof Label) {
					fieldId = ((Label) component).getValue().trim();
				} else if (component instanceof Combobox) {
					Combobox combobox = (Combobox) component;
					String fieldValue = (String) Sessions.getCurrent().getAttribute(module + "." + fieldId);
//					try{
						if (fieldValue != null && !fieldValue.trim().equalsIgnoreCase("")
								&& !fieldValue.trim().equalsIgnoreCase(" ") && combobox.getItems().size() > 0
//								&& exist(combobox, fieldValue)
								){
							combobox.setValue(fieldValue);
						}
//					}catch(java.lang.IllegalStateException e){
//						
//					}
				} else if (component instanceof Textbox) {
					Textbox textbox = (Textbox) component;
					String fieldValue = (String) Sessions.getCurrent().getAttribute(module + "." + fieldId);
					if (fieldValue != null)
						textbox.setValue(fieldValue);
				} else if (component instanceof Datebox) {
					Datebox datebox = (Datebox) component;
					Date fieldValue = (Date) Sessions.getCurrent().getAttribute(module + "." + fieldId);
					if (fieldValue != null)
						datebox.setValue(fieldValue);
				} else if (component instanceof Intbox) {
					Intbox intbox = (Intbox) component;
					Integer fieldValue = (Integer) Sessions.getCurrent().getAttribute(module + "." + fieldId);
					if (fieldValue != null)
						intbox.setValue(fieldValue);
				} else if (component instanceof CustomBandBox) {
					CustomBandBox<?> bandbox = (CustomBandBox<?>) component;
					String fieldValue = (String) Sessions.getCurrent().getAttribute(module + "." + fieldId);
					if (fieldValue != null)
						bandbox.setValue(fieldValue);
				}
			}
		}
	}

	public void saveHistory() {
		WindowSelector window = (WindowSelector) this.getSpaceOwner().getFellow(WindowSelector.ID);
		String module = window.getEntity();

		String fieldId = null;
		@SuppressWarnings("unchecked")
		Iterator<Row> itrow = this.getRows().getChildren().iterator();
		while (itrow.hasNext()) {
			Row row = itrow.next();
			Iterator<?> itfield = row.getChildren().iterator();
			while (itfield.hasNext()) {
				Object component = itfield.next();

				if (component instanceof Label) {
					fieldId = ((Label) component).getValue().trim();
				} else if (component instanceof Combobox) {
					Combobox combobox = (Combobox) component;
					Sessions.getCurrent().setAttribute(module + "." + fieldId,	combobox.getValue());
				} else if (component instanceof Textbox) {
					Textbox textbox = (Textbox) component;
					Sessions.getCurrent().setAttribute(module + "." + fieldId,	textbox.getValue());
				} else if (component instanceof Datebox) {
					Datebox datebox = (Datebox) component;
					Sessions.getCurrent().setAttribute(module + "." + fieldId,	datebox.getValue());
				} else if (component instanceof Intbox) {
					Intbox intbox = (Intbox) component;
					Sessions.getCurrent().setAttribute(module + "." + fieldId,	intbox.getValue());
				} else if (component instanceof CustomBandBox<?>) {
					CustomBandBox<?> bandbox = (CustomBandBox<?>) component;
					Sessions.getCurrent().setAttribute(module + "." + fieldId,	bandbox.getValue());
				}
			}
		}
	}

	/**
	 * @return the idBandBox
	 */
	public String getIdBandBox() {
		return idBandBox;
	}

	/**
	 * @param idBandBox the idBandBox to set
	 */
	public void setIdBandBox(String idBandBox) {
		this.idBandBox = idBandBox;
	}
	
	
}