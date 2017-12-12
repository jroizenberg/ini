package com.institucion.model;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Row;

import com.institucion.desktop.delegated.AnularSubscripcionDelegate;
import com.institucion.desktop.delegated.SubscripcionDelegate;
import com.institucion.fm.desktop.view.GridCrud;

public class IngresosEgresosPagarBotonesFilter extends GridCrud{
    
	private static final long serialVersionUID = 1L;
	public static final String SESSION_PRODUCT_ID = "session_product_id2";
    private Button pagarButton;

    /** The delegate. */
    private SubscripcionDelegate delegate;
	private AnularSubscripcionDelegate subsAnulardelegate;

	public IngresosEgresosPagarBotonesFilter() {
		super();
		setId("refresh3");
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
           toolbar.setAlign("center");

           // boton refrescar acciones de filtros
           pagarButton = new Button();
           pagarButton.setLabel("Registrar pago Dinero");
           pagarButton.setTooltip("Registrar pago Dinero");
           pagarButton.addEventListener(Events.ON_CLICK, new EventListener() {
                       public void onEvent(Event evt) {
                                   if( getDelegate() != null)
                                	   getDelegate().pagarCurso();
                                   else
                                	   getSubsAnulardelegate().pagarCurso();
                       }
                   });
           toolbar.appendChild(pagarButton);                 		   
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

		public Button getPagarButton() {
			return pagarButton;
		}

		public void setPagarButton(Button pagarButton) {
			this.pagarButton = pagarButton;
		}

		public AnularSubscripcionDelegate getSubsAnulardelegate() {
			return subsAnulardelegate;
		}

		public void setSubsAnulardelegate(AnularSubscripcionDelegate subsAnulardelegate) {
			this.subsAnulardelegate = subsAnulardelegate;
		}

}