package com.institucion.model;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Row;

import com.institucion.desktop.delegated.SubscripcionDelegate;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridCrud;

public class SubscripcionAgregarEliminarBotonesFilter extends GridCrud{
    
	private static final long serialVersionUID = 1L;
	public static final String SESSION_PRODUCT_ID = "session_product_id";
    private Button agregarButton;
//    private Button asignarButton;
    private Button eliminarButton;
//    private Button guardarButton;

    /** The delegate. */
    private SubscripcionDelegate delegate;

	public SubscripcionAgregarEliminarBotonesFilter() {
		super();
		setId("refresh2");
		makeFields();
	}
	
	private void makeFields(){
		
        Panel addressPanel = new Panel();
        Panelchildren addressPanelchild = new Panelchildren();
        addressPanel.appendChild(addressPanelchild);
        org.zkoss.zul.Toolbar toolbar = makeToolbar();
        addressPanelchild.appendChild(toolbar);

        Row listboxRow = new Row();
        listboxRow.appendChild(addressPanel);
        this.getRows().appendChild(listboxRow);
    	}

	   private org.zkoss.zul.Toolbar makeToolbar() {
           org.zkoss.zul.Toolbar toolbar = new org.zkoss.zul.Toolbar();
           toolbar.setAlign("end");

           // boton refrescar acciones de filtros
           agregarButton = new Button();
           agregarButton.setLabel(I18N.getLabel("curso.agregar"));
           agregarButton.setTooltip(I18N.getLabel("curso.agregar"));
           agregarButton.addEventListener(Events.ON_CLICK, new EventListener() {
                       public void onEvent(Event evt) {
                                   getDelegate().agregarCurso(false, null);
                       }
                   });
           toolbar.appendChild(agregarButton);

           eliminarButton = new Button();
           eliminarButton.setLabel(I18N.getLabel("curso.eliminar"));
           eliminarButton.setTooltip(I18N.getLabel("curso.eliminar"));
           eliminarButton.addEventListener(Events.ON_CLICK, new EventListener() {
                       public void onEvent(Event evt) {
                                   getDelegate().eliminarCurso();
                       }
                   });
           toolbar.appendChild(eliminarButton);
           
//           guardarButton = new Button();
//           guardarButton.setVisible(false);
//           guardarButton.setLabel("Guardar Cambios");
//           guardarButton.setTooltip("Guardar Cambios");
//           guardarButton.addEventListener(Events.ON_CLICK, new EventListener() {
//                       public void onEvent(Event evt) {
//                                   getDelegate().guardarCambios();
//                       }
//                   });
//           toolbar.appendChild(guardarButton);
           
           return toolbar;
       }
	   
	    /**
	     * Gets the delegate.
	     * 
	     * @return the delegate
	     */
	    public SubscripcionDelegate getDelegate() {
	        return delegate;
	    }

	    /**
	     * Sets the delegate.
	     * 
	     * @param delegate
	     *            the new delegate
	     */
	    public void setDelegate(SubscripcionDelegate delegate) {
	        this.delegate = delegate;
	    }

		public Button getAgregarButton() {
			return agregarButton;
		}

		public void setAgregarButton(Button agregarButton) {
			this.agregarButton = agregarButton;
		}

		public Button getEliminarButton() {
			return eliminarButton;
		}

//		public Button getGuardarButton() {
//			return guardarButton;
//		}
//
//		public void setGuardarButton(Button guardarButton) {
//			this.guardarButton = guardarButton;
//		}

		public void setEliminarButton(Button eliminarButton) {
			this.eliminarButton = eliminarButton;
		}

}