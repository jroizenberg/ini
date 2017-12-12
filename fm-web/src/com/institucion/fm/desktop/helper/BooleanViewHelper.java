package com.institucion.fm.desktop.helper;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;

import com.institucion.fm.desktop.service.I18N;

/**
 * The Class BooleanViewHelper.
 */
public class BooleanViewHelper {
	
	/** The cb. */
	static private Combobox cb = null;

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

	
	public static Radiogroup getRadiogroup2(Boolean value) {
		Radiogroup radioGroup = new Radiogroup();
		Radio falseRadio = new Radio(I18N.getLabel("boolean.false"));
		if (value != null)
			falseRadio.setSelected(value);
		falseRadio.setAttribute("value", false);
		radioGroup.appendChild(falseRadio);
		return radioGroup;
	}
	
	public static Radiogroup getRadiogroup3(Boolean value) {
		Radiogroup radioGroup = new Radiogroup();
		Radio falseRadio = new Radio(I18N.getLabel("boolean.true"));
		if (value != null)
			falseRadio.setSelected(value);
		falseRadio.setAttribute("value", true);
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