package com.institucion.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJBException;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;

import com.institucion.bz.ClienteEJB;
import com.institucion.bz.InscripcionEJB;
import com.institucion.fm.conf.Session;
import com.institucion.fm.conf.exception.WebExceptionHandler;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.CrudComposer;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.MessageBox;
import com.institucion.fm.desktop.view.PanelCrud;
import com.institucion.model.Cliente;
import com.institucion.model.ClienteCrud;
import com.institucion.model.Inscripcion;
import com.institucion.model.MatriculaList;

public class ClientCrudComposer extends CrudComposer {

	private PanelCrud panelCrud;
	public final String idCliente = "idCliente";
	private Cliente cliente;
	private ClienteEJB clienteEJB;
	private InscripcionEJB inscripcionEJB;
	private MatriculaList matriculaspanelListGrid;

	
	public ClientCrudComposer() {
		clienteEJB = BeanFactory.<ClienteEJB>getObject("fmEjbCliente");
		inscripcionEJB = BeanFactory.<InscripcionEJB>getObject("fmEjbInscripcion");

	}

	public void onCreate() {
		Cliente cliente= (Cliente) Session.getAttribute(idCliente);
		setCliente(cliente);
		this.fromModelToView(cliente);
		if(cliente != null && cliente.getId() != null){
			List<Inscripcion> listaIns=inscripcionEJB.findByIdClienteAndAnio(cliente.getId());
			matriculaspanelListGrid.setList(listaIns);	
		}else{
			matriculaspanelListGrid.setList(null);
		}
	}
	
	private void fromModelToView(Cliente cliente) {
		// si lo que obtuve de bd es != null
		if(cliente != null){
			
			if(cliente.getApellido() != null){
				getClienteCrud().getApellido().setValue(cliente.getApellido().toUpperCase());
			}
			if(cliente.getCelular() != null){
				getClienteCrud().getCelular().setValue(cliente.getCelular());
			}
			if(cliente.getDni() != null){
				getClienteCrud().getDni().setValue(cliente.getDni());
			}
			if(cliente.getDomicilio() != null){
				getClienteCrud().getDomicilio().setValue(cliente.getDomicilio());
			}
			if(cliente.getFacebook() != null){
				getClienteCrud().getFacebook().setValue(cliente.getFacebook());	
			}
			if(cliente.getFechaNacimiento() != null){
				getClienteCrud().getFechaNacimiento().setValue(cliente.getFechaNacimiento());
			}
			if(cliente.getMail() != null){
				getClienteCrud().getMail().setValue(cliente.getMail());
			}
			if(cliente.getNombre() != null){
				getClienteCrud().getNombre().setValue(cliente.getNombre().toUpperCase());
			}
			if(cliente.getTelefonoFijo() != null){
				getClienteCrud().getTelefono().setValue(cliente.getTelefonoFijo());
			}
			
			if(cliente.isEntregoCertificado()){
				getClienteCrud().setSelectedTieneCertificado(cliente.isEntregoCertificado());
			}
		}		
	}
	
	public void onSave(Event event) throws Exception {
		try{
			if (Sessions.getCurrent().getAttribute(idCliente) != null) {
				// es una modificacion
					cliente = (Cliente) Sessions.getCurrent().getAttribute(idCliente);
					this.fromViewToModel(cliente);
					validateCrud();
					clienteEJB.save(cliente);

					if (Sessions.getCurrent().getAttribute("isClienteFromIngresoInscripcion") != null){ 
						Sessions.getCurrent().setAttribute("isClienteFromIngresoInscripcion", null);
						super.gotoPage("/institucion/ingresoInscripcion-selector.zul");
					}else	
						super.gotoPage("/institucion/clientes-selector.zul");
					
			} else {
				// es nuevo 
				cliente= new Cliente();
				validateCrud();
				this.fromViewToModel(cliente);

				String query= "select id from cliente" +
						"  where (Upper(apellido) ilike ('"+cliente.getApellido()+"%') " +
						"  AND Upper(nombre) ilike  Upper('"+cliente.getNombre()+"%') )" +
						"  OR fechanacimiento ='"+cliente.getFechaNacimiento()+ "' " ;
				
				if(cliente.getDni() != null && !cliente.getDni().trim().equalsIgnoreCase("")){
					query= query+ "  OR dni ='"+cliente.getDni()+ "' " ;
				}
				if(cliente.getCelular() != null && !cliente.getCelular().trim().equalsIgnoreCase("")){
					query= query+ "  OR celular ='"+cliente.getCelular()+ "' " ;
				}		
				if(cliente.getTelefonoFijo() != null && !cliente.getTelefonoFijo().trim().equalsIgnoreCase("")){
					query= query+ "  OR telefonofijo ='"+cliente.getTelefonoFijo()+ "' " ;
				}		
				if(cliente.getDomicilio() != null && !cliente.getDomicilio().trim().equalsIgnoreCase("")){
					query= query+ "  OR domicilio  ilike '%"+cliente.getDomicilio()+ "%' " ;
				}		

				// verifica que el cliente exista.
				List<Cliente> lista =clienteEJB.findAllConJdbc(query);
				
				if(lista != null && lista.size() > 0){
					String clientes=null;
					int i=1;
					for (Cliente cliente : lista) {
						if(clientes == null){
							clientes= i +") "; 
						}else
							clientes=clientes + " - "+ i +") ";
						
						clientes= clientes+ cliente.getApellido().toUpperCase() +" "+ cliente.getNombre().toUpperCase();
						
						if(cliente.getFechaNacimiento() != null){
							Calendar ahoraCal= Calendar.getInstance();
							ahoraCal.setTime(cliente.getFechaNacimiento());
							int mes=ahoraCal.get(Calendar.MONTH) + 1;

							String fechaNac=ahoraCal.get(Calendar.DATE)+"/"+mes+"/"+ahoraCal.get(Calendar.YEAR);
							clientes= clientes +" FECHA NAC: "+fechaNac;
						}						
						
						if(cliente.getDni() != null)
							clientes=clientes + " DNI: "+cliente.getDni();
						
						if(cliente.getDomicilio() != null)
							clientes=clientes + " DOMICILIO: "+cliente.getDomicilio();
								
					}
					
					if (MessageBox.question("Se encontraron "+lista.size() +" clientes posiblemente iguales:  " +clientes+
							"     �Esta seguro que el cliente que esta agregando es distinto al existente ? Si NO esta seguro cancele y busque el cliente."
							,"Cliente posiblemente ya existente.")){
					
						Long idCliente=(Long)clienteEJB.createSubs(cliente);
						Cliente cli=clienteEJB.findById(idCliente);
						Sessions.getCurrent().setAttribute( "idCliente", cli);
						
						if (Sessions.getCurrent().getAttribute("isClienteFromIngresoInscripcion") != null){
							Sessions.getCurrent().setAttribute("isClienteFromIngresoInscripcion", null);
							Sessions.getCurrent().setAttribute("vieneDeCrearCliente", cli);
							super.gotoPage("/institucion/ingresoInscripcion-selector.zul");
						}else{
							super.gotoPage("/institucion/clientes-selector.zul");
						}
						
					}else{
						super.gotoPage("/institucion/clientes-selector.zul");
					}		
				}
//				if(cliente.getEstado() == null){
//					cliente.setEstado(ClienteEstadoEnum.S_CLASES_DISP); 
//				}
				Long idCliente=(Long)clienteEJB.createSubs(cliente);
				Cliente cli=clienteEJB.findById(idCliente);
				Sessions.getCurrent().setAttribute( "idCliente", cli);
				
				if (Sessions.getCurrent().getAttribute("isClienteFromIngresoInscripcion") != null){
					Sessions.getCurrent().setAttribute("isClienteFromIngresoInscripcion", null);
					Sessions.getCurrent().setAttribute("vieneDeCrearCliente", cli);

					super.gotoPage("/institucion/ingresoInscripcion-selector.zul");
				}else{
					super.gotoPage("/institucion/clientes-selector.zul");
				}

			}
			
		} catch (EJBException ex) {
			WebExceptionHandler.handleThis(ex);
		}
	}
		
	public void onBack(Event event) {
		if (Sessions.getCurrent().getAttribute("isClienteFromIngresoInscripcion") != null){ 
			Sessions.getCurrent().setAttribute("isClienteFromIngresoInscripcion", null);
			super.gotoPage("/institucion/ingresoInscripcion-selector.zul");
		}else	
			super.gotoPage("/institucion/clientes-selector.zul");
	}

	private ClienteCrud getClienteCrud() {
		return (ClienteCrud) (panelCrud.getGridCrud());
	}

	private Cliente fromViewToModel(Cliente cliente) {
		
		if(getClienteCrud().getNombre().getValue() != null)
			cliente.setNombre(getClienteCrud().getNombre().getValue());	
		
		if(getClienteCrud().getApellido().getValue() != null)
			cliente.setApellido(getClienteCrud().getApellido().getValue());
		
		if(getClienteCrud().getDni().getValue() != null)
			cliente.setDni(getClienteCrud().getDni().getValue());
	
		if(getClienteCrud().getFechaNacimiento() != null)
			cliente.setFechaNacimiento(getClienteCrud().getFechaNacimiento().getValue());
				
		if(getClienteCrud().getDomicilio().getValue()  != null)
			cliente.setDomicilio(getClienteCrud().getDomicilio().getValue());

		if(getClienteCrud().getCelular().getValue()  != null)
			cliente.setCelular(getClienteCrud().getCelular().getValue());
		
		if(getClienteCrud().getFacebook().getValue()  != null)
			cliente.setFacebook(getClienteCrud().getFacebook().getValue());
		
		if(getClienteCrud().getMail().getValue()  != null)
			cliente.setMail(getClienteCrud().getMail().getValue());
		
		if(getClienteCrud().getTelefono().getValue()  != null)
			cliente.setTelefonoFijo(getClienteCrud().getTelefono().getValue());
		
		if(getClienteCrud().getTieneCertificado().getSelectedItem() != null && getClienteCrud().getTieneCertificado().getSelectedItem().getValue()  != null)
			cliente.setEntregoCertificado((Boolean)getClienteCrud().getTieneCertificado().getSelectedItem().getValue() );
		
	
		return cliente;

	}

	private void validateCrud() {
	
		
		if(getClienteCrud().getNombre().getValue()  == null || 
				(getClienteCrud().getNombre().getValue() != null && getClienteCrud().getNombre().getValue().trim().equalsIgnoreCase("")))
			throw new WrongValueException(getClienteCrud().getNombre(), I18N.getLabel("error.empty.field"));
		
		if(getClienteCrud().getApellido().getValue()  == null || 
				(getClienteCrud().getApellido().getValue() != null && getClienteCrud().getApellido().getValue().trim().equalsIgnoreCase("")))
			throw new WrongValueException(getClienteCrud().getApellido(), I18N.getLabel("error.empty.field"));
		
		if(getClienteCrud().getFechaNacimiento().getValue() == null || 
				(getClienteCrud().getFechaNacimiento().getValue() != null && getClienteCrud().getFechaNacimiento().getValue().after(new Date() )))
			throw new WrongValueException(getClienteCrud().getFechaNacimiento(), I18N.getLabel("error.empty.field"));
			
		if(getClienteCrud().getDomicilio().getValue()  == null || 
				(getClienteCrud().getDomicilio().getValue() != null && getClienteCrud().getDomicilio().getValue().trim().equalsIgnoreCase("")))
			throw new WrongValueException(getClienteCrud().getDomicilio(), I18N.getLabel("error.empty.field"));
		
		if(getClienteCrud().getCelular().getValue()  == null || 
				(getClienteCrud().getCelular().getValue() != null && getClienteCrud().getCelular().getValue().trim().equalsIgnoreCase("")))
			throw new WrongValueException(getClienteCrud().getCelular(), I18N.getLabel("error.empty.field"));
	}
	
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

}
