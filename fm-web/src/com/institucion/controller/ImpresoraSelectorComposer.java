package com.institucion.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.zk.ui.event.Event;

import com.institucion.bz.ClienteEJB;
import com.institucion.bz.CursoEJB;
import com.institucion.fm.conf.Session;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.SelectorFEComposer;
import com.institucion.fm.desktop.view.PanelFilter;
import com.institucion.model.Cliente;
import com.institucion.model.ImpresoraFilter;
import com.institucion.model.TipoCursoEnum;

public class ImpresoraSelectorComposer extends SelectorFEComposer {
	
//	private ClaseList clasepanelListGrid;
//	private List<Clase> pharmacyList;
	private PanelFilter filter;
	private static Log log = LogFactory.getLog(ImpresoraSelectorComposer.class);
	private ClienteEJB clienteEjb = null;
	private  CursoEJB cursoEJB;

//	private Image imagen= null;
//	private ClaseEJB claseEJB;

	public ImpresoraSelectorComposer(){
		super();
//		claseEJB = BeanFactory.<ClaseEJB>getObject("fmEjbClase");
		this.clienteEjb = BeanFactory.<ClienteEJB>getObject("fmEjbCliente");
		this.cursoEJB = BeanFactory.<CursoEJB>getObject("fmEjbCurso");	

	}

	private ImpresoraFilter getFilter() {
		return (ImpresoraFilter)(filter.getGridFilter());
	}
	public void onCreate() {
		if(Session.getDesktopPanel().getMessage().equals("menu")){
			clear();	
		}
		onFind();	
	}

	
	public void onUpdate(Event event) throws Exception{
		Cliente t=this.clienteEjb.findById(new Long(2271));
		List aaaaa= new ArrayList();
		aaaaa.add(t);
		String ffss=this.clienteEjb.generarCodigosDeBarras(aaaaa);
		File f= new File(ffss);
//		AMedia amedia = new AMedia("sas", "doc", "application/msword", f, true);
		
		imprimirFile(f, ffss);	
		
	}

	public void onDelete(Event event) {
	}

	public void onInsert(Event event) throws Exception {
		//Nuestro trabajo de impresi�n env�a una cadena de texto
		Cliente t=this.clienteEjb.findById(new Long(2172));
		Boolean bool=cursoEJB.findImprimible();

		String a=Imprimir.imprimirComprobante("Comprob de Ingreso", t, 
				"Hidroterapia", true, String.valueOf(0), String.valueOf(10), true, TipoCursoEnum.NATACION, 0, false);
		
		String ab=Imprimir.imprimirComprobante("Comprob de Ingreso", t, 
				"Natacion", true, String.valueOf(0), String.valueOf(10), true, TipoCursoEnum.NATACION, 0, false);

		Imprimir.imprimirComprobante(a + ab);
		}

	
	private void imprimir(String ss){
		
		PrintService service = null;
		log.error("Comienza impresora");

	    PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null); //Obtenemos los servicios de impresion del sistema 
		log.error("Comienza impresora printServices "+ printServices);

	    for (PrintService impresora : printServices){ //Recorremos el array de servicios de impresion
	      
			log.error("Comienza impresora impresora "+ impresora);
			if(impresora.getName().contentEquals("POS-58")){ // Si el nombre del servicio es el mismo que el que buscamos
	        	service= impresora; // Nos devuelve el servicio
	        	log.error("Comienza impresora: LA ENCONTRO");
	        }
	    }

	    if(service == null)
	    	log.error("Comienza impresora: NO ENCONTRO IMPRESORA!");
		//Le decimos el tipo de datos que vamos a enviar a la impresora
		//Tipo: bytes Subtipo: autodetectado
//		DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
	    DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
		//Creamos un trabajo de impresi�n
	    log.error("Comienza impresora: por createPrintJob");

		DocPrintJob pj = service.createPrintJob();
	    log.error("Comienza impresora: createPrintJob");

	
		byte[] bytes;
		//Transformamos el texto a bytes que es lo que soporta la impresora
		bytes=ss.getBytes();
		//Creamos un documento (Como si fuese una hoja de Word para imprimir)
		log.error("Comienza impresora: SimpleDoc ");

		Doc doc=new SimpleDoc(bytes,flavor,null);

		//Obligado coger la excepci�n PrintException
		try {
			log.error("Comienza impresora: print comienza ");
		  //Mandamos a impremir el documento
		  pj.print(doc, null);
		  log.error("Comienza impresora: print termina ");
		}catch (PrintException e) {
		  System.out.println("Error al imprimir: "+e.getMessage());
		}
	}
	
	private void imprimirFile(File  f, String path) throws IOException{
		
		PrintService service = null;
		log.error("Comienza impresora");

	    PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null); //Obtenemos los servicios de impresion del sistema 
	    for (PrintService impresora : printServices){ //Recorremos el array de servicios de impresion
	        if(impresora.getName().contentEquals("POS-58")){ // Si el nombre del servicio es el mismo que el que buscamos
	        	service= impresora; // Nos devuelve el servicio
	        	log.error("Comienza impresora: LA ENCONTRO");
	        }
	    }

		//Le decimos el tipo de datos que vamos a enviar a la impresora
		//Tipo: bytes Subtipo: autodetectado
//		DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
	    DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
		//Creamos un trabajo de impresi�n
	    log.error("Comienza impresora: por createPrintJob");

		DocPrintJob pj = service.createPrintJob();
	    log.error("Comienza impresora: createPrintJob");

	
//		byte[] bytes;
	    Path paths = Paths.get(path);
		byte[] bytes = Files.readAllBytes(paths);
		//Transformamos el texto a bytes que es lo que soporta la impresora
//		bytes=ss.getBytes();
		//Creamos un documento (Como si fuese una hoja de Word para imprimir)
		log.error("Comienza impresora: SimpleDoc ");

		Doc doc=new SimpleDoc(bytes,flavor,null);

		//Obligado coger la excepci�n PrintException
		try {
			log.error("Comienza impresora: print comienza ");
		  //Mandamos a impremir el documento
		  pj.print(doc, null);
		  log.error("Comienza impresora: print termina ");
		}catch (PrintException e) {
		  System.out.println("Error al imprimir: "+e.getMessage());
		}
	}
	public void onDoubleClickEvt(Event event) throws Exception {
	}


	public void onFind(Event evt) {
		Session.setAttribute("pag", false);
		this.onFind();
	}
	
	public void onFind() {
//		Logger log=Logger.getLogger(this.getClass());
//		log.info("Creando listado de farmacia en la version modificada");
//		pharmacyList= new ArrayList<Clase>();
//		
////		if(getFilter().validateHaveFilters()){
//			// hago la consulta jdbc con los filtros, devuelvo IDs, y despues hago la busqueda por ids con hibernate
//		pharmacyList =claseEJB.findAllConJdbc(getFilter().getFilters());	
//			
////		}else{
////			pharmacyList =claseEJB.findAll();	
////		}
//		
//		clasepanelListGrid.setList(pharmacyList, null);
//			
//		log.info("Fin Creando listado de farmacia en la version modificada");
	
	}
	
	
	
	public void clear(){
		getFilter().clear();
//		getFilter().clear();

	}
	public void onClearFilter(Event evt){
		
		getFilter().clear();
//		if (isCallFromMenu()){
//			getFilter().clear();
//		}else{
//			this.onFind();
//		}		
	}

}