package com.institucion.desktop.helper;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;

import com.institucion.fm.desktop.helper.OrderCombobox;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.model.TipoDescuentosEnum;
import com.institucion.model.TipoMovimientoCajaEnum;

/**
 * The Class BooleanViewHelper.
 */
public class BooleanViewHelper {
	
	/** The cb. */
	static private Combobox cb = null;
	static private Combobox cb2 = null;

	/**
	 * Gets the state string.
	 * 
	 * @param state
	 *            the state
	 * @return the state string
	 */
	public static String getStateString(Boolean state) {
		return state ? I18N.getLabel("festate.active") : I18N
				.getLabel("festate.inactive");
	}

	/**
	 * Gets the boolean string.
	 * 
	 * @param state
	 *            the state
	 * @return the boolean string
	 */
	public static String getBooleanString(Boolean state) {
		return state ? I18N.getLabel("boolean.true") : I18N
				.getLabel("boolean.false");
	}

	/**
	 * Gets the boolean.
	 *
	 * @param boolValue the bool value
	 * @return the boolean
	 */
	public static Boolean getBoolean(String boolValue) {
		return I18N.getLabel("boolean.true").equals(boolValue);
	}

	/**
	 * Gets the combo box.
	 *
	 * @return the combo box
	 */
	public static Combobox getComboBox() {
		cb = new Combobox();
		cb.setConstraint("strict");
		List<Comboitem> itemsSort = new ArrayList<Comboitem>();

		Comboitem item;
		item = new Comboitem(I18N.getLabel("boolean.true"));
		item.setValue(true);
		// cb.appendChild(item);
		itemsSort.add(item);

		item = new Comboitem(I18N.getLabel("boolean.false"));
		item.setValue(false);
		// cb.appendChild(item);
		itemsSort.add(item);
		OrderCombobox.orderCombobox(itemsSort, cb);
		return cb;
	}
	
	public static Combobox getComboBox(boolean selected) {
		cb = new Combobox();
		cb.setConstraint("strict");
		List<Comboitem> itemsSort = new ArrayList<Comboitem>();

		Comboitem item2;
		item2 = new Comboitem(I18N.getLabel("boolean.true"));
		item2.setValue(true);
		itemsSort.add(item2);
		Comboitem item;

		item = new Comboitem(I18N.getLabel("boolean.false"));
		item.setValue(false);
		// cb.appendChild(item);
		itemsSort.add(item);
		OrderCombobox.orderCombobox(itemsSort, cb);
		
		cb.setSelectedItem(item2);
		return cb;
	}

	
	public static Combobox getComboTieneDescuentoBox() {
		Combobox cb = new Combobox();
		cb.setConstraint("strict");

		Comboitem item3 = new Comboitem(TipoDescuentosEnum.NO.toString());
		item3.setValue(TipoDescuentosEnum.NO);
		cb.setSelectedItem(item3);
		cb.appendChild(item3);
		cb.setSelectedItem(item3);
		
		
		Comboitem item2 = new Comboitem(TipoDescuentosEnum.GENERAL.toString());
		item2.setValue(TipoDescuentosEnum.GENERAL);
		cb.appendChild(item2);
		
		Comboitem item = new Comboitem(TipoDescuentosEnum.OBRA_SOCIAL.toString());
		item.setValue(TipoDescuentosEnum.OBRA_SOCIAL);
		cb.appendChild(item);
		
		cb.setSelectedItem(item3);
		
		return cb;
	}
	
	/**
	 * Select value.
	 *
	 * @param combobox the combobox
	 * @param value the value
	 */
	public static void selectValue(Combobox combobox, boolean value) {
		if (combobox.getItemAtIndex(0).getValue().equals(value))
			combobox.setSelectedIndex(0);
		else
			combobox.setSelectedIndex(1);
	}

	/**
	 * Gets the radiogroup.
	 *
	 * @return the radiogroup
	 */
	public static Radiogroup getRadiogroup() {
		return getRadiogroup(null);
	}

	/**
	 * Gets the radiogroup.
	 *
	 * @param value the value
	 * @return the radiogroup
	 */
	public static Radiogroup getRadiogroup(Boolean value) {
		Radiogroup radioGroup = new Radiogroup();
		Radio trueRadio = new Radio(I18N.getLabel("boolean.true"));
		if (value != null)
			trueRadio.setSelected(value);
		trueRadio.setAttribute("value", true);
		radioGroup.appendChild(trueRadio);
		Radio falseRadio = new Radio(I18N.getLabel("boolean.false"));
		falseRadio.setAttribute("value", false);
		radioGroup.appendChild(falseRadio);
		return radioGroup;
	}

	/**
	 * Gets the radiogroup disable.
	 *
	 * @param value the value
	 * @return the radiogroup disable
	 */
	public static Radiogroup getRadiogroupDisable(Boolean value) {
		Radiogroup radioGroup = new Radiogroup();
		Radio trueRadio = new Radio(I18N.getLabel("boolean.true"));
		if (value != null)
			trueRadio.setSelected(value);
		trueRadio.setAttribute("value", true);
		trueRadio.setDisabled(true);
		radioGroup.appendChild(trueRadio);
		Radio falseRadio = new Radio(I18N.getLabel("boolean.false"));
		falseRadio.setAttribute("value", false);
		falseRadio.setDisabled(true);
		radioGroup.appendChild(falseRadio);
		return radioGroup;
	}
}