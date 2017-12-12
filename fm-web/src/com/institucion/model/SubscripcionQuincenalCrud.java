package com.institucion.model;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;

import com.institucion.desktop.delegated.SubscripcionDelegate;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridCrud;

public class SubscripcionQuincenalCrud extends GridCrud{

	private static final long serialVersionUID = 1L;

	private Intbox diasQueParticipara;
	private Intbox importeTotalCurso;

	private SubscripcionDelegate delegate;


	public SubscripcionDelegate getDelegate() {
		return delegate;
	}

	public void setDelegate(SubscripcionDelegate delegate) {
		this.delegate = delegate;
	}
	
	public SubscripcionQuincenalCrud (){
		super();
		this.makeFields();
	}
	
	private void makeFields(){	

		diasQueParticipara = new Intbox();
		diasQueParticipara.setConstraint("no negative");
		diasQueParticipara.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt) {
				
				if(diasQueParticipara.getValue() != null && diasQueParticipara.getValue().intValue() > 0 &&
						diasQueParticipara.getValue().intValue() <= 10 ){
					int valorCurso=delegate.getValorOriginalCurso();
					int cantDiasNOVienenAlCurso=10- diasQueParticipara.getValue();
					
					int valorPorDia=valorCurso/ 10;
					
					// si los dos dias sale...., se le cobrara el 30% de lo original
					int valorOriginalDeLoQueNoPagariaElCliente=valorPorDia * cantDiasNOVienenAlCurso; 
					
					// al valor original se le aplica %
					String porcQueSeLeAplicaAdicPorClase=I18N.getLabel("porcentaje.que.se.cobra.por.clase.por.separado.quincenal");
					int porc=Integer.valueOf(porcQueSeLeAplicaAdicPorClase);
					int valorQseSuma=(valorOriginalDeLoQueNoPagariaElCliente * porc )/100;

					int valorTOT=(diasQueParticipara.getValue() * valorPorDia) +valorQseSuma;
					importeTotalCurso.setValue(valorTOT);
					importeTotalCurso.setStyle("color: red;");
					
				}else{
					throw new WrongValueException(diasQueParticipara, I18N.getLabel("error.empty.field.mayores.y.menores.a.10"));
					
				}
			}
		});	
		this.addFieldClases(new Label(I18N.getLabel("combo.anula.subscripcion.cant.dias")),diasQueParticipara, 1);

		importeTotalCurso = new Intbox();
		importeTotalCurso.setConstraint("no negative");
		this.addFieldClases(new Label(I18N.getLabel("combo.pospone.subscripcion..curso.importe.tot")), importeTotalCurso, 2);	

		
	}

	public void clear (){
	}

	public Intbox getDiasQueParticipara() {
		return diasQueParticipara;
	}

	public void setDiasQueParticipara(Intbox diasQueParticipara) {
		this.diasQueParticipara = diasQueParticipara;
	}

	public Intbox getImporteTotalCurso() {
		return importeTotalCurso;
	}

	public void setImporteTotalCurso(Intbox importeTotalCurso) {
		this.importeTotalCurso = importeTotalCurso;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


}
