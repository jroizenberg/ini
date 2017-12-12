package com.institucion.model;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Intbox;

import com.institucion.desktop.delegated.SubscripcionDelegate;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridCrud;
import com.institucion.fm.desktop.view.RequiredLabel;

public class SubscripcionPorClaseCrud extends GridCrud{

	private static final long serialVersionUID = 1L;
	private Intbox diasQueParticipara;
	private SubscripcionDelegate delegate;

	public SubscripcionDelegate getDelegate() {
		return delegate;
	}

	public void setDelegate(SubscripcionDelegate delegate) {
		this.delegate = delegate;
	}
	
	public SubscripcionPorClaseCrud (){
		super();
		this.makeFields();
	}
	
	private void makeFields(){	
		
		diasQueParticipara = new Intbox();
		diasQueParticipara.setConstraint("no negative");
		diasQueParticipara.setValue(1);
		diasQueParticipara.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt) {
				
				if(((Intbox)evt.getTarget()).getValue() != null && ((Intbox)evt.getTarget()).getValue().intValue() > 0 &&
						((Intbox)evt.getTarget()).getValue().intValue() < 10 ){
					int valorCurso=delegate.getValorOriginalCurso();
					
					int totalClases=valorCurso *  diasQueParticipara.getValue().intValue() ;
					
//					importeTotalCurso.setValue(totalClases);
//					importeTotalCurso.setStyle("color: red;");
					
				}else{
					throw new WrongValueException(diasQueParticipara, I18N.getLabel("error.empty.field.mayores.y.menores.a.3.clases"));
					
				}
			}
		});	
		diasQueParticipara.addEventListener(Events.ON_OK, new EventListener() {
            public void onEvent(Event evt) {
            	
            	if(((Intbox)evt.getTarget()).getValue() != null && ((Intbox)evt.getTarget()).getValue().intValue() > 0 &&
            			((Intbox)evt.getTarget()).getValue().intValue() < 10 ){
                    getDelegate().continuarAgregandoCursoPorClase();
            	}
            }
        });

		this.addFieldClases(new RequiredLabel("Cantidad de Clases/Sesiones"),diasQueParticipara, 1);

	}


	public void clear (){
		setComboPArticiparaQuincenas();
	}

	
	private void setComboPArticiparaQuincenas(){
		
		Comboitem item;
		item = new Comboitem(I18N.getLabel("boolean.true"));
		item.setValue(true);
		
		item = new Comboitem(I18N.getLabel("boolean.false"));
		item.setValue(false);
		
	}
	

	public Intbox getDiasQueParticipara() {
		return diasQueParticipara;
	}

	public void setDiasQueParticipara(Intbox diasQueParticipara) {
		this.diasQueParticipara = diasQueParticipara;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


}
