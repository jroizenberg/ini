import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;


public class mainn {  
        public static void main(String[] args) throws Exception{
      
            StringTokenizer tokens = new StringTokenizer("1", ";");
            String campos = "";
           while (tokens.hasMoreTokens()) {
                campos = campos + "tabla" + ".pot" + tokens.nextToken();
                if (tokens.hasMoreTokens()) {
                    campos = campos + " + ";
                }
            }
        System.out.println(campos); 

//				PrintService service = null;
//				
//			    PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null); //Obtenemos los servicios de impresion del sistema 
//			    for (PrintService impresora : printServices){ //Recorremos el array de servicios de impresion
//			        if(impresora.getName().contentEquals("POS-58")){ // Si el nombre del servicio es el mismo que el que buscamos
//			        	service= impresora; // Nos devuelve el servicio 
//			        }
//			    }
//
//				
//				//Le decimos el tipo de datos que vamos a enviar a la impresora
//				//Tipo: bytes Subtipo: autodetectado
////				DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
//			    DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
//				//Creamos un trabajo de impresi�n
//				DocPrintJob pj = service.createPrintJob();
//				//Nuestro trabajo de impresi�n env�a una cadena de texto
//				
//				Calendar ahoraCal= Calendar.getInstance();
//				ahoraCal.setTime(new Date());
//				int mes=ahoraCal.get(Calendar.MONTH) + 1;
//
//				String fechaDia=ahoraCal.get(Calendar.DATE)+"/"+mes+"/"+ahoraCal.get(Calendar.YEAR);
//				
//				Date hora=new Date();
//				DateFormat hourdateFormat = new SimpleDateFormat("dd/MM/yyyy");
//				
//				DateFormat hourdateFormat2 = new SimpleDateFormat("HH:mm");
//
////				System.out.println("Hora y fecha: "+hourdateFormat.format(hora));
//			      
//				String ss=new String(
//						"\n"+hourdateFormat.format(hora)+ "                 " + hourdateFormat2.format(hora)+"\n" +
////						  "   INSTITUTO\n" +  
//						"      INSTITUTO SWISZCZ\n" +
//						"  - Comprob de Inscripci�n -\n" +
//						//"               Fecha:" +fechaDia +
//						"\nCliente:ANA MARIA GALVEZ" +
//						"\nActividad: RPG" +
//						"\nTipo: ORDEN/PARTICULAR " +   // max 27 caracteres
//						"\nAbonado: $200 \n"+
//						"\n    MUCHAS GRACIAS!! \n\n\n\n\n\n");
//
//				byte[] bytes;
//				//Transformamos el texto a bytes que es lo que soporta la impresora
//				bytes=ss.getBytes();
//				//Creamos un documento (Como si fuese una hoja de Word para imprimir)
//				Doc doc=new SimpleDoc(bytes,flavor,null);
//
//				//Obligado coger la excepci�n PrintException
//				try {
//				  //Mandamos a impremir el documento
//				  pj.print(doc, null);
//				}catch (PrintException e) {
//				  System.out.println("Error al imprimir: "+e.getMessage());
//				}
        	
        	
        	
        	PrintService service = null;
			
		    PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null); //Obtenemos los servicios de impresion del sistema 
		    for (PrintService impresora : printServices){ //Recorremos el array de servicios de impresion
		        if(impresora.getName().contentEquals("POS-58")){ // Si el nombre del servicio es el mismo que el que buscamos
		        	service= impresora; // Nos devuelve el servicio 
		        }
		    }

			
			//Le decimos el tipo de datos que vamos a enviar a la impresora
			//Tipo: bytes Subtipo: autodetectado
//			DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
		    DocFlavor flavor = DocFlavor.INPUT_STREAM.PNG;
			//Creamos un trabajo de impresi�n
			DocPrintJob pj = service.createPrintJob();
			//Nuestro trabajo de impresi�n env�a una cadena de texto
			
			Calendar ahoraCal= Calendar.getInstance();
			ahoraCal.setTime(new Date());
			int mes=ahoraCal.get(Calendar.MONTH) + 1;

			String fechaDia=ahoraCal.get(Calendar.DATE)+"/"+mes+"/"+ahoraCal.get(Calendar.YEAR);
			
			Date hora=new Date();
			DateFormat hourdateFormat = new SimpleDateFormat("dd/MM/yyyy");
			
			DateFormat hourdateFormat2 = new SimpleDateFormat("HH:mm");

//			System.out.println("Hora y fecha: "+hourdateFormat.format(hora));

		    Path paths = Paths.get("C://Users//Jonathan R//Desktop//prueba//codigoBarra.png");
			byte[] bytes = Files.readAllBytes(paths);

			 File initialFile = new File("C://Users//Jonathan R//Desktop//prueba//codigoBarra.png");
			   InputStream targetStream = new FileInputStream(initialFile);
			    
//			String ss=new String(
//					"\n"+hourdateFormat.format(hora)+ "                 " + hourdateFormat2.format(hora)+"\n" +
////					  "   INSTITUTO\n" +  
//					"      INSTITUTO SWISZCZ\n" +
//					"  - Comprob de Inscripci�n -\n" +
//					//"               Fecha:" +fechaDia +
//					"\nCliente:ANA MARIA GALVEZ" +
//					"\nActividad: RPG" +
//					"\nTipo: ORDEN/PARTICULAR " +   // max 27 caracteres
//					"\nAbonado: $200 \n"+
//					"\n    MUCHAS GRACIAS!! \n\n\n\n\n\n");

//			byte[] bytes;
			//Transformamos el texto a bytes que es lo que soporta la impresora
//			bytes=ss.getBytes();
			//Creamos un documento (Como si fuese una hoja de Word para imprimir)
			   
//			   PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
//	            pras.add(new Copies(1));         
//	            
//	            DocPrintJob docPrintJob = service.createPrintJob();
//	            Doc doc = new SimpleDoc(targetStream, DocFlavor.INPUT_STREAM.PNG, null);
//	            docPrintJob.print(doc, pras);
	            
//			   PrintService service = PrintServiceLookup.lookupDefaultPrintService();
			 //Indicamos que lo que vamos a imprimir es un objeto imprimible
			 DocFlavor flavor2 = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
			 DocPrintJob pj2 = service.createPrintJob();
			 //Creamos el documento a imprimir que contendr� el objeto
			 Doc doc=new SimpleDoc(new ObjetoaImprimir(),flavor2,null);

			 try {
				 pj2.print(doc, null);
			 }
			 catch (PrintException e) {
			   System.out.println("Error al imprimir: "+e.getMessage());
			 }
	            
//	            FileOutputStream os = new FileOutputStream("C://Users//Jonathan R//Desktop//prueba//codigoBarra.png");
//	            PrintStream ps = new PrintStream(os);
//	            ps.println("prueba de impresi�n realizada");
//	            ps.println("prueba de impresi�n realizada");
//
//	            ps.close();
	            
	            
//			Doc doc=new SimpleDoc(targetStream,flavor,null);
//
//			//Obligado coger la excepci�n PrintException
//			try {
//			  //Mandamos a impremir el documento
//			  pj.print(doc, null);
//			}catch (PrintException e) {
//			  System.out.println("Error al imprimir: "+e.getMessage());
//			}
				
		
    }
    
    
        
        
        
}
