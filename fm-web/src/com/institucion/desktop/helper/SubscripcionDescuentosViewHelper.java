//package com.institucion.desktop.helper;
//
//import org.zkoss.zul.Combobox;
//import org.zkoss.zul.Comboitem;
//
//import com.institucion.model.SubscripcionDescuentoGeneralEnum;
//
//public class SubscripcionDescuentosViewHelper {
//
//	public static Combobox getDescuentosGralesComboBox() {
//		Combobox cb = new Combobox();
//		cb.setConstraint("strict");
//
//		Comboitem item5;
//		item5 = new Comboitem(SubscripcionDescuentoGeneralEnum.toString(SubscripcionDescuentoGeneralEnum.SIN_DESC.toInt()));
//		item5.setValue(SubscripcionDescuentoGeneralEnum.SIN_DESC);
//		cb.setSelectedItem(item5);
//		cb.appendChild(item5);
//		cb.setSelectedItem(item5);
//		
//		Comboitem item;
//		item = new Comboitem(SubscripcionDescuentoGeneralEnum.toString(SubscripcionDescuentoGeneralEnum.GENERAL_5.toInt()));
//		item.setValue(SubscripcionDescuentoGeneralEnum.GENERAL_5);
//		// cb.appendChild(item);
//		cb.appendChild(item);
//
//		item = new Comboitem(SubscripcionDescuentoGeneralEnum.toString(SubscripcionDescuentoGeneralEnum.GENERAL_10.toInt()));
//		item.setValue(SubscripcionDescuentoGeneralEnum.GENERAL_10);
//		// cb.appendChild(item);
//		cb.appendChild(item);
//		
//		item = new Comboitem(SubscripcionDescuentoGeneralEnum.toString(SubscripcionDescuentoGeneralEnum.GENERAL_15.toInt()));
//		item.setValue(SubscripcionDescuentoGeneralEnum.GENERAL_15);
//		// cb.appendChild(item);
//		cb.appendChild(item);
//
//		item = new Comboitem(SubscripcionDescuentoGeneralEnum.toString(SubscripcionDescuentoGeneralEnum.GENERAL_20.toInt()));
//		item.setValue(SubscripcionDescuentoGeneralEnum.GENERAL_20);
//		// cb.appendChild(item);
//		cb.appendChild(item);
//
//		item = new Comboitem(SubscripcionDescuentoGeneralEnum.toString(SubscripcionDescuentoGeneralEnum.GENERAL_25.toInt()));
//		item.setValue(SubscripcionDescuentoGeneralEnum.GENERAL_25);
//		// cb.appendChild(item);
//		cb.appendChild(item);
//		
//		item = new Comboitem(SubscripcionDescuentoGeneralEnum.toString(SubscripcionDescuentoGeneralEnum.GENERAL_30.toInt()));
//		item.setValue(SubscripcionDescuentoGeneralEnum.GENERAL_30);
//		// cb.appendChild(item);
//		cb.appendChild(item);
//		
//		item = new Comboitem(SubscripcionDescuentoGeneralEnum.toString(SubscripcionDescuentoGeneralEnum.GENERAL_35.toInt()));
//		item.setValue(SubscripcionDescuentoGeneralEnum.GENERAL_35);
//		// cb.appendChild(item);
//		cb.appendChild(item);
//		
//		item = new Comboitem(SubscripcionDescuentoGeneralEnum.toString(SubscripcionDescuentoGeneralEnum.GENERAL_50.toInt()));
//		item.setValue(SubscripcionDescuentoGeneralEnum.GENERAL_50);
//		// cb.appendChild(item);
//		cb.appendChild(item);
//		
//		item = new Comboitem(SubscripcionDescuentoGeneralEnum.toString(SubscripcionDescuentoGeneralEnum.GENERAL_70.toInt()));
//		item.setValue(SubscripcionDescuentoGeneralEnum.GENERAL_70);
//		// cb.appendChild(item);
//		cb.appendChild(item);
//		
//		item = new Comboitem(SubscripcionDescuentoGeneralEnum.toString(SubscripcionDescuentoGeneralEnum.GENERAL_80.toInt()));
//		item.setValue(SubscripcionDescuentoGeneralEnum.GENERAL_80);
//		// cb.appendChild(item);
//		cb.appendChild(item);
//		
//		item = new Comboitem(SubscripcionDescuentoGeneralEnum.toString(SubscripcionDescuentoGeneralEnum.GENERAL_90.toInt()));
//		item.setValue(SubscripcionDescuentoGeneralEnum.GENERAL_90);
//		// cb.appendChild(item);
//		cb.appendChild(item);
//		
//		item = new Comboitem(SubscripcionDescuentoGeneralEnum.toString(SubscripcionDescuentoGeneralEnum.GENERAL_100.toInt()));
//		item.setValue(SubscripcionDescuentoGeneralEnum.GENERAL_100);
//		// cb.appendChild(item);
//		cb.appendChild(item);
//	//	OrderCombobox.orderCombobox(itemsSort, cb);
//		return cb;
//	}
//	
//	
//}