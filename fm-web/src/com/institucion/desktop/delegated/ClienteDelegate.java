package com.institucion.desktop.delegated;

import org.zkoss.zk.ui.event.Event;


public interface ClienteDelegate {
	
	public void  sortEvent(Event event);
	public void buscar(Event evt, boolean isFromCodigoBarras);

	public void venderNuevaSubs(Event evt, int idCliente);

	public void actualizarPaneldePrecioProducto();

}
