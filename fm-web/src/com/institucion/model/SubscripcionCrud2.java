//package com.institucion.model;
//
//import org.zkoss.zul.Combobox;
//import org.zkoss.zul.Label;
//import org.zkoss.zul.Textbox;
//
//import com.institucion.desktop.delegated.SubscripcionDelegate;
//import com.institucion.desktop.helper.SubscripcionDescuentosViewHelper;
//import com.institucion.fm.desktop.service.I18N;
//import com.institucion.fm.desktop.validator.TextConstraint;
//import com.institucion.fm.desktop.view.GridCrud;
//
//public class SubscripcionCrud2 extends GridCrud{
//
//	private static final long serialVersionUID = 1L;
//	
//	//private Combobox comboAplicaDescuentos;
//	private Combobox comboDescuentos;
//	private Textbox comentarioDescuento;
////	private Textbox aprobadoPor;
//	private SubscripcionDelegate delegate;
//
//
//	public SubscripcionDelegate getDelegate() {
//		return delegate;
//	}
//
//	public void setDelegate(SubscripcionDelegate delegate) {
//		this.delegate = delegate;
//	}
//	
//	public SubscripcionCrud2 (){
//		super();
//		this.makeFields();
//	}
//	
//	private void makeFields(){		
//		comboDescuentos =  SubscripcionDescuentosViewHelper.getDescuentosGralesComboBox();
//		comboDescuentos.setConstraint("strict");
//		this.addField(new Label(I18N.getLabel("descuento.desc")), comboDescuentos);
//		
////		aprobadoPor= new Textbox();
////		aprobadoPor.setMaxlength(100);
////		aprobadoPor.setConstraint(new TextConstraint());
////		this.addField(new RequiredLabel(I18N.getLabel("comentario.aprobadopor")), aprobadoPor);
//
//		comentarioDescuento= new Textbox();
//		comentarioDescuento.setMaxlength(150);
//		comentarioDescuento.setRows(2);
//		comentarioDescuento.setWidth("80%");
//		comentarioDescuento.setConstraint(new TextConstraint());
//		this.addField(new Label(I18N.getLabel("comentario.descuento")), comentarioDescuento);
//
//	}
//
////
////	public Combobox getComboDescuentos() {
////		return comboDescuentos;
////	}
////
////	public void setComboDescuentos(Combobox comboDescuentos) {
////		this.comboDescuentos = comboDescuentos;
////	}
////
////	public Textbox getComentarioDescuento() {
////		return comentarioDescuento;
////	}
////
////	public void setComentarioDescuento(Textbox comentarioDescuento) {
////		this.comentarioDescuento = comentarioDescuento;
////	}
////
////	public void setComentarioDescuento(String comentarioDescuento) {
////		this.comentarioDescuento.setText(comentarioDescuento);
////		this.comentarioDescuento.setValue(comentarioDescuento);
////	}
////	
////	public Textbox getAprobadoPor() {
////		return aprobadoPor;
////	}
////
////	public void setAprobadoPor(Textbox aprobadoPor) {
////		this.aprobadoPor = aprobadoPor;
////	}
//
////	public void setAprobadoPor(String aprobadoPor) {
////		this.aprobadoPor.setText(aprobadoPor);
////		this.aprobadoPor.setValue(aprobadoPor);
////	}
////	public void setselectedComboDescuentos (SubscripcionDescuentoGeneralEnum selectedHPType){
////		boolean found = false;
////		int i = 0;
////		while (!found && i<comboDescuentos.getItemCount()){
////			if(selectedHPType == null){
////				comboDescuentos.setSelectedItem(null);
////			}else if(selectedHPType.toInt() == (((SubscripcionDescuentoGeneralEnum) comboDescuentos.getItemAtIndex(i).getValue()).toInt())){
////				found = true;
////				comboDescuentos.setSelectedIndex(i);
////			}
////			i++;
////		}
////		
////	}
////	
//}
