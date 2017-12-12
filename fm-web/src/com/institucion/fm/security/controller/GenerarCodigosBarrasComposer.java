package com.institucion.fm.security.controller;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Window;

import com.institucion.bz.ClienteEJB;
import com.institucion.fm.conf.Session;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.CrudComposer;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.MessageBox;
import com.institucion.fm.security.model.ClienteDaDFilter;
import com.institucion.fm.security.model.ClienteDaDPanel;
import com.institucion.model.Cliente;

public class GenerarCodigosBarrasComposer extends CrudComposer {
	
	private static Log log = LogFactory.getLog(GenerarCodigosBarrasComposer.class);
	
	private ClienteDaDPanel leftPanel;
	private ClienteDaDPanel rightPanel;
	private ClienteEJB clienteEjb = null;
	List<Cliente> clients = null;
	private Window win = null;

	public GenerarCodigosBarrasComposer() {
	}
	
	private String getClienstsAssigned(){
		String result=null;
		
		if(rightPanel.getList() != null && rightPanel.getList().size() >0){
	
			Iterator ite=rightPanel.getList().iterator();
			while(ite.hasNext()){
				Cliente cli=(Cliente)ite.next();
				if(cli != null){
					if(result == null){
						result= ""+cli.getId();
					}else{
						result= result + ", "+ cli.getId();;
					}
				}
			}			
		}
		
		if(result != null){
			result= "("+ result + ")";
		}
		return result;
	}
	
	public void onFind(Event evt) throws Exception {
		String  actionConditions= " select id, apellido, nombre, fechanacimiento  from cliente  where 1=1 ";

			String clientesInRight= getClienstsAssigned();
			
			if(clientesInRight != null){
				actionConditions= actionConditions +" and  id not in "+clientesInRight ;
			}
			
			String nombre=((ClienteDaDFilter)leftPanel.getPanelFilter()).getApellido().getValue();
			String apell=((ClienteDaDFilter)leftPanel.getPanelFilter()).getMaster().getValue();
			boolean isCheckedSoloConCupos=((ClienteDaDFilter)leftPanel.getPanelFilter()).getConCupos().isChecked();

			// aplicarle aca a la consulta los filtros por nombre y apellido
			
			if((nombre != null && !nombre.trim().equalsIgnoreCase("")) 
				|| (apell != null && !apell.trim().equalsIgnoreCase("")) ){
				if(nombre != null && !nombre.trim().equalsIgnoreCase("")){
					actionConditions= actionConditions +" and  nombre ilike '%"+nombre.trim()+"%'" ;	
				}
				if(apell != null && !apell.trim().equalsIgnoreCase("")){
					actionConditions= actionConditions +" and  apellido ilike '%"+apell.trim()+"%'" ;	
				}
	
			}else{
				   actionConditions= actionConditions +" and id in (select idcliente from subscripcion )  " ;

			}
			
			if(isCheckedSoloConCupos){
				actionConditions= actionConditions +" and 0 <  ( select count(*) from subscripcion s "+ 
						"	inner join cupoactividad ca on (ca.idsubscripcion= s.id) "+
						"	inner join curso c on (ca.idcurso= c.id) "+
						"	 where  "+
						"	 c.esConCodigosDeBarras is true "+
						"	and s.idcliente= cliente.id  and  	ca.estado in (3, 10)  ) ";  
			}else{
				actionConditions= actionConditions +" and id in  ( select s.idcliente from subscripcion s "+ 
						"	inner join cupoactividad ca on (ca.idsubscripcion= s.id) "+
						"	inner join curso c on (ca.idcurso= c.id) "+
						"	 where  "+
						"	 c.esConCodigosDeBarras is true   and s.idcliente= cliente.id  ) ";  

				
			}
			
			// si no tuvo ningun filtro, traer todos
			clients=clienteEjb.findAllConJdbc2(actionConditions.toString());
			leftPanel.setList(clients);

//		}		
	}

	public void onCreate() throws Exception {
		
		if(Session.getAttribute("sucursalPrincipalSeleccionada") == null){
			MessageBox.validation("¡Debe seleccionar con que sucursal desea operar!", I18N.getLabel("selector.actionwithoutitem.title"));
			Executions.getCurrent().sendRedirect(null);
		}

		this.clienteEjb = BeanFactory.<ClienteEJB>getObject("fmEjbCliente");
		// eliminar todos los archivos de un directorio
		onFind(null);
	}
	
	 private static void borrarArchivoDirectorio() {
	        String direccion = "/home/ubuntu/codigosBarras/";

	        File directorio = new File(direccion);
	        File f;
	        if (directorio.isDirectory()) {
	            String[] files = directorio.list();
	            if (files.length > 0) {
	                for (String archivo : files) {
	                    f = new File(direccion + File.separator + archivo);
                        f.delete();
                        f.deleteOnExit();
	                }
	            }
	        }
	 }
	 
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
	}

	public void onGenerateCodBarras(Event event) {
		borrarArchivoDirectorio();
		File f=null;
		try{
			if(rightPanel.getList() != null && rightPanel.getList().size() >0){
				String ffss=this.clienteEjb.generarCodigosDeBarras(rightPanel.getList());
				f= new File(ffss);
				Iframe iframeReporte = new Iframe();
				
				iframeReporte.setWidth("0%");
		        iframeReporte.setHeight("0%");
				iframeReporte.setId("reporte");
				AMedia amedia = new AMedia(f.getName(), "doc", "application/msword", f, true);
//				f.deleteOnExit();
				if (win == null) {
					win = (Window) (Executions.getCurrent()).createComponents("institucion/report-popup.zul", super.self, null);	
				}
				iframeReporte = (Iframe) win.getFellow("reporte");
				iframeReporte.setContent(amedia);
				try {
					win.doEmbedded();
					win.setVisible(false);
				} catch (SuspendNotAllowedException e) {
					e.printStackTrace();
				}
	           if(rightPanel.getList() != null)
	        	   rightPanel.removeAll();
	   			onFind(null);

			}else{
				MessageBox.info("Se debe asignar al menos 1 cliente ", I18N.getLabel("selector.actionwithoutitem.title"));
				return;

			}
			}catch (Exception e){
				log.error("errr " + e.toString() + " " + e.getMessage());
			}finally{
//				if(f != null)
//					f.delete();
			}
				
	}
	public void onClearFilter(Event event) throws Exception {
//		aca tengo que limpiar los filtros
		(leftPanel.getPanelFilter()).getApellido().setText("");
		(leftPanel.getPanelFilter()).getApellido().setValue("");
		(leftPanel.getPanelFilter()).getMaster().setText("");
		(leftPanel.getPanelFilter()).getMaster().setValue("");
//		(leftPanel.getPanelFilter()).getConCupos().setChecked(false);

		if(rightPanel.getList() != null)
			rightPanel.getList().clear();
		
		if(leftPanel.getList() != null)
			leftPanel.getList().clear();
		
		onFind(null);

	}
	
	public void onMoveRight(Event event) throws Exception {
		List<Cliente> perms = leftPanel.getSelectedAndRemoveThem();
		rightPanel.addList(perms);
	}

	public void onMoveAllRight(Event event) throws Exception {
		leftPanel.selectAllItems();
		this.onMoveRight(event);
	}

	public void onMoveLeft(Event event) throws Exception {
		List<Cliente> perms = rightPanel.getSelectedAndRemoveThem();
		leftPanel.addList(perms);
	}

	public void onMoveAllLeft(Event event) throws Exception {
		rightPanel.selectAllItems();
		this.onMoveLeft(event);
	}

}