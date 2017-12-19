package com.institucion.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.zkoss.zk.ui.util.Clients;

import com.institucion.model.Cliente;
import com.institucion.model.TipoCursoEnum;

public class Imprimir {

	public static String imprimirComprobante (String subtitulo, Cliente cli, String actividad , boolean isOrden, 
			String monto,  String montoAdicional, boolean isActividadImp, TipoCursoEnum tipoCurso, int disponibles, boolean imprimirAhora){
		String ss= "";
		
		if(subtitulo.equalsIgnoreCase("Comprob para el Profesional")){
			if(tipoCurso.toInt() != TipoCursoEnum.TRATAMIENTOS_KINESICOS.toInt()){
				isActividadImp= false;
			}
		}
		if(isActividadImp){
			Date hora=new Date();
			DateFormat hourdateFormat = new SimpleDateFormat("dd/MM/yyyy");
			
			DateFormat hourdateFormat2 = new SimpleDateFormat("HH:mm");
			String nom="";
			String act="";
			String tipo="";
			String subtitu="";

			if(cli != null ){
				if(cli.getApellido() != null)
					nom= cli.getApellido();
				if(cli.getNombre() != null)
					nom= nom +" "+ cli.getNombre();
				
				if(nom.length() > 20){
					nom=nom.substring(0, 20);
				}
			}
			if(actividad != null){
				act= actividad;
				if(act.length() > 16){
					act=act.substring(0, 16);
				}
			}
			if(subtitulo != null){
				subtitu= subtitulo;

			}
			
			if(isOrden){
				tipo="ORDEN";
			}else{
				tipo="PARTICULAR";
			}
			
			String espacios="";
			if(subtitulo.equalsIgnoreCase("Comprob para el Profesional")){
				espacios="";
			}else{
				espacios="&nbsp;&nbsp;";
			}

			
			ss=new String(
					""+hourdateFormat.format(hora)+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + hourdateFormat2.format(hora)+"<br/>" +
					"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;INSTITUTO SWISZCZ<br/>" +
					espacios+"  - "+subtitu+" -<br/>" +  // Comprob de Inscripcion 
					"<br/>Cliente:"+nom +  // ANA MARIA GALVEZ
					"<br/>Actividad: " +act +   // RPG
					"<br/>Tipo:" +tipo ) ;    // ORDEN/PARTICULAR

			if(monto != null && !subtitulo.equalsIgnoreCase("Comprob para el Profesional"))
				ss= ss+"<br/>Abonado:$"+monto;
				
//			if(montoAdicional != null && !montoAdicional.trim().equalsIgnoreCase("")){
//				int sss=Integer.valueOf(montoAdicional);
//				if(sss > 0){
//					ss= ss+" -copago:$"+ montoAdicional;	
//				}
//			}

			if(subtitulo.equalsIgnoreCase("Comprob para el Profesional")){
				ss= ss+"<br/>Disponibles: "+disponibles;
			}

			if(tipoCurso.toInt() == TipoCursoEnum.NATACION.toInt() || tipoCurso.toInt() == TipoCursoEnum.ACTIVIDADES_FISICAS.toInt()){
				ss= ss+"<br/>Fecha Desde:.............";
				ss= ss+"<br/>Fecha Hasta:.............";
			}
			ss= ss+	"<br/> <br/> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  ¡¡MUCHAS GRACIAS!! <br/><br/> ";	
			
			if(imprimirAhora)
				Clients.evalJavaScript("ImprimirVar(' "+ss+" ')" );	
		}
		return ss;
	}
	
	public static void imprimirComprobante (String cosasAImprimir){
		if(cosasAImprimir != null){
			Clients.evalJavaScript("ImprimirVar(' "+cosasAImprimir+" ')" );	
		}
	} 

	
}

