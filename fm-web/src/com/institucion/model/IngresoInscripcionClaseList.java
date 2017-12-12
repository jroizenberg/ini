package com.institucion.model;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import com.institucion.bz.ClaseEJB;
import com.institucion.bz.ClienteEJB;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridList;

public class IngresoInscripcionClaseList extends GridList {
	private static final long serialVersionUID = 1L;
	private ClaseEJB claseEJB;
	private ClienteEJB cliEJB;

	public IngresoInscripcionClaseList() {
		super();
		
		super.addHeader(new Listheader(I18N.getLabel("clase.nombre"))).setWidth("20%");
		super.addHeader(new Listheader("Hora Inicio - Fin")).setWidth("10%");
		super.addHeader(new Listheader(I18N.getLabel("curso.cantAlumnos"))).setWidth("16%");

		super.addHeader(new Listheader("Presentes confirmados(P/Profe)")).setWidth("18%");
		super.addHeader(new Listheader("Alum No Encontrados en clase(P/Profe)")).setWidth("18%");
		super.addHeader(new Listheader("Resumen")).setWidth("19%");

		this.setMultiple(false);
		cliEJB = BeanFactory.<ClienteEJB>getObject("fmEjbCliente");	

		this.setPageSize(20);
	}

	private ClaseEJB getServiceClaseEJB(){
		if(claseEJB == null)
			claseEJB = BeanFactory.<ClaseEJB>getObject("fmEjbClase");
		return claseEJB;
	}
	

	private Clase existInOriginalClase(Clase clase, List<Clase> clases){
		if(clases != null){
			for (Clase clase2 : clases) {
				if(clase2.getId().intValue() == clase.getId().intValue()){
					return clase2;
				}
			}
		}
		return clase;
	}
	
	private boolean existInOriginalCla(Clase clase, List<Clase> clases){
		if(clases != null){
			for (Clase clase2 : clases) {
				if(clase2.getId().intValue() == clase.getId().intValue()){
					return true;
				}
			}
		}
		return false;
	}
	
	public void setList(List<Clase> claseList, boolean esFechaExacta, Date fecha, List<Clase> clasesYaEnHistorico, List<Clase> clasesTomadasLista) {
		super.removeAll();
		if(claseList != null){
			Iterator<Clase> itPharmacy = claseList.iterator();
			while (itPharmacy.hasNext()) {
				Clase clase = itPharmacy.next();
				clase= cliEJB.loadLazy(clase, true, true, true, true);

				Listitem row = new Listitem();

				if(existInOriginalCla(clase, clasesYaEnHistorico)){  // si existe en historico
					clase =existInOriginalClase(clase, clasesYaEnHistorico);	
					row.setAttribute("esClaseHitorica", true);
					row.setAttribute("esClaseQueSeTomoLista", false);

				}else if(existInOriginalCla(clase, clasesTomadasLista)){  // si es una clase que se tomo lista
					clase =existInOriginalClase(clase, clasesYaEnHistorico);	
					row.setAttribute("esClaseHitorica", false);
					row.setAttribute("esClaseQueSeTomoLista", true);
				}else{
					// es clase Nueva
					row.setAttribute("esClaseHitorica", false);
					row.setAttribute("esClaseQueSeTomoLista", false);
				}
					
				if(esFechaExacta && !existInOriginalCla(clase, clasesYaEnHistorico)){
					row.setAttribute("esClaseQueSeTomoLista", true);
					row.setAttribute("esClaseHitorica", false);
				}
				
				row.setValue(clase);
				Listcell companyCell = new Listcell(getNombreClase(clase));
				row.appendChild(companyCell);

				
				if(clase.getEsClaseSinHora() != null && clase.getEsClaseSinHora().booleanValue()){
					row.appendChild(new Listcell("Sin Horario Fijo"));
					
				}else{
					String horaDesde= null;
					if(clase.getHoraDesde().getMinutes() < 10){ 
						horaDesde= clase.getHoraDesde().getHours() +":"+clase.getHoraDesde().getMinutes() +"0";	
					}else{
						horaDesde= clase.getHoraDesde().getHours() +":"+clase.getHoraDesde().getMinutes();
					}
					
					String horaHasta= null;
					if(clase.getHoraHasta().getMinutes() < 10){ 
						horaHasta= clase.getHoraHasta().getHours() +":"+clase.getHoraHasta().getMinutes() +"0";	
					}else{
						horaHasta= clase.getHoraHasta().getHours() +":"+clase.getHoraHasta().getMinutes();
					}
					row.appendChild(new Listcell(horaDesde +" - "+ horaHasta));
					
				}
				List<ClienteListaEncontradoEnPiletaHistorico>  lis= null;
				
				if((row.getAttribute("esClaseQueSeTomoLista") != null && (Boolean)row.getAttribute("esClaseQueSeTomoLista"))){
					if(clase.getCantAlumnos() != null)
						row.appendChild(new Listcell(String.valueOf(clase.getCantAlumnos())));
					else
						row.appendChild(new Listcell(""));		
					
				}else if((row.getAttribute("esClaseHitorica") != null && (Boolean)row.getAttribute("esClaseHitorica"))){
					if(fecha == null)
						fecha= new Date();
					lis=getServiceClaseEJB().findAllByClaseAndFechaHistorico(null, clase.getId(), fecha, true);
					if(lis != null){
						row.appendChild(new Listcell(String.valueOf(lis.size())));
					}else{
						row.appendChild(new Listcell(""));		
					}
					
				}else{
					if(clase.getCantAlumnos() != null)
						row.appendChild(new Listcell(String.valueOf(clase.getCantAlumnos())));
					else
						row.appendChild(new Listcell(""));
				}

				if((row.getAttribute("esClaseQueSeTomoLista") != null && (Boolean)row.getAttribute("esClaseQueSeTomoLista")))
					procesoEsClaseQueSeTomoLista(clase, row);
				
				else if((row.getAttribute("esClaseHitorica") != null && (Boolean)row.getAttribute("esClaseHitorica"))){
						if(fecha == null)
							fecha= new Date();
						procesoEsClaseHistoricaQueSeTomoLista(clase, row, fecha, lis);
				
				}else
					procesoEsClaseQueSeTomoLista(clase, row);

				super.addRow(row);	
			}
		}
	}
	
	
	private void procesoEsClaseQueSeTomoLista(Clase clase,Listitem row ){
		
		if(clase.getClaseConListaAlumnosList() != null){
			int cantAlumnosPresentes=0;
			int cantAlumnosNoEncontrados=0;
			
			for (ClaseConListaAlumnos clase2 : clase.getClaseConListaAlumnosList()) {
				if(clase2.getCantPresentes() != null)
					cantAlumnosPresentes= clase2.getCantPresentes();
				if(clase2.getCantPresentesNoEncontrados() != null)
					cantAlumnosNoEncontrados= clase2.getCantPresentesNoEncontrados();
			}

			row.appendChild(new Listcell(String.valueOf(cantAlumnosPresentes)));
			Listcell list=new Listcell(String.valueOf(cantAlumnosNoEncontrados));
			if(cantAlumnosNoEncontrados > 0)
				list.setStyle(" color:#FF0000 !important; font-weight:bold;  ");
			row.appendChild(list);	
			
			// Secre no Ingreso ningun cliente
			if(clase.getCantAlumnos() != null && clase.getCantAlumnos() <= 0)
				row.appendChild(new Listcell(" No se ingresaron Clientes por Secretaria."));	
			
			else{
				
				String comentario= "";
				// Hay Clientes y no se tomo Lista							
				if(cantAlumnosPresentes == 0)
					comentario= comentario+ " Profe no tomo Lista de Presentes.";
				
				else if(cantAlumnosPresentes > 0){
					
					comentario= comentario+ " Presentes confirmados por Profes:"+cantAlumnosPresentes+".";
					if(clase.getCantAlumnos()!= null && clase.getCantAlumnos() > 0 
							&& clase.getCantAlumnos() > cantAlumnosPresentes){
						int faltantes =clase.getCantAlumnos().intValue() -cantAlumnosPresentes;
						comentario= comentario+ " Faltan confirmar:"+faltantes+".";	
					}
					if(cantAlumnosNoEncontrados > 0)
						comentario= comentario+ " Se encontraron "+cantAlumnosNoEncontrados+" clientes no registrados en Secretaria.";
					
				}else if(cantAlumnosNoEncontrados > 0)
					comentario= comentario+ " Se encontraron "+cantAlumnosNoEncontrados+" clientes no registrados en Secretaria.";
					
				Listcell lisss=new Listcell(comentario);
				if(cantAlumnosNoEncontrados >0){
					lisss.setStyle(" color:#FF0000 !important; font-weight:bold;  ");
				}
				row.appendChild(lisss);

			}
		}else{
			row.appendChild(new Listcell(""));
			row.appendChild(new Listcell(""));
			row.appendChild(new Listcell(""));

		}
	}
	
	private void procesoEsClaseHistoricaQueSeTomoLista(Clase clase,Listitem row , Date fecha, List<ClienteListaEncontradoEnPiletaHistorico>  lis){
		ClaseConListaAlumnosHistorico  claseListaAlumnos=getServiceClaseEJB().findAllClaseConListaAlumnosByClaseAndFechaHistorico(clase.getId(), fecha, true);
 		
		if(claseListaAlumnos != null){
			int cantAlumnosPresentes=0;
			int cantAlumnosNoEncontrados=0;
			
			if(claseListaAlumnos.getCantPresentes() != null)
				cantAlumnosPresentes= claseListaAlumnos.getCantPresentes();
			if(claseListaAlumnos.getCantPresentesNoEncontrados() != null)
				cantAlumnosNoEncontrados= claseListaAlumnos.getCantPresentesNoEncontrados();	
			
			
			row.appendChild(new Listcell(String.valueOf(cantAlumnosPresentes)));
			Listcell list=new Listcell(String.valueOf(cantAlumnosNoEncontrados));
			if(cantAlumnosNoEncontrados > 0)
				list.setStyle(" color:#FF0000 !important; font-weight:bold;  ");
			row.appendChild(list);		

			
			// Secre no Ingreso ningun cliente
			if(lis == null || ( lis != null && lis.size() <= 0))
				row.appendChild(new Listcell("No se ingresaron Clientes por Secretaria."));	
			
			else{
				String comentario= "";
				// Hay Clientes y no se tomo Lista							
				if(cantAlumnosPresentes == 0)
					comentario= comentario+ "Profe no tomo Lista de Presentes.";
				
				else if(cantAlumnosPresentes > 0){
					
					comentario= comentario+ " Presentes confirmados por Profes:"+cantAlumnosPresentes+".";
					if( lis != null && lis.size() > 0 && lis.size() > cantAlumnosPresentes){
						int faltantes =lis.size() -cantAlumnosPresentes;
						comentario= comentario+ " Faltan confirmar:"+faltantes+".";	
					}

					if(cantAlumnosNoEncontrados > 0)
						comentario= comentario+ " Se encontraron "+cantAlumnosNoEncontrados+" clientes no registrados en Secretaria.";
					
				}else if(cantAlumnosNoEncontrados > 0)
					comentario= comentario+ " Se encontraron "+cantAlumnosNoEncontrados+" clientes no registrados en Secretaria.";
					
				Listcell lisss=new Listcell(comentario);
				if(cantAlumnosNoEncontrados >0){
					lisss.setStyle(" color:#FF0000 !important; font-weight:bold;  ");
				}
				row.appendChild(lisss);

			}
		}else{
			row.appendChild(new Listcell(""));
			row.appendChild(new Listcell(""));
			row.appendChild(new Listcell(""));

		}
		
	}
	
	public String getNombreClase(Clase clase){
		String diasDeSemana="";
		if(clase.getLunes()) diasDeSemana=diasDeSemana+"Lunes";
		if(clase.getMartes()){ 
			if(diasDeSemana.equalsIgnoreCase(""))diasDeSemana=diasDeSemana+"Martes"; else diasDeSemana=diasDeSemana+"-Martes";
		} 
		if(clase.getMiercoles()){
			if(diasDeSemana.equalsIgnoreCase(""))diasDeSemana=diasDeSemana+"Miercoles"; else diasDeSemana=diasDeSemana+"-Miercoles";
		} 
		if(clase.getJueves()){
			if(diasDeSemana.equalsIgnoreCase(""))diasDeSemana=diasDeSemana+"Jueves"; else diasDeSemana=diasDeSemana+"-Jueves";
		} 
		if(clase.getViernes()){
			if(diasDeSemana.equalsIgnoreCase(""))diasDeSemana=diasDeSemana+"Viernes"; else diasDeSemana=diasDeSemana+"-Viernes";
		} 
		if(clase.getSabado()){
			if(diasDeSemana.equalsIgnoreCase(""))diasDeSemana=diasDeSemana+"Sabado"; else diasDeSemana=diasDeSemana+"-Sabado";
		} 
		if(clase.getDomingo()){
			if(diasDeSemana.equalsIgnoreCase(""))diasDeSemana=diasDeSemana+"Domingo"; else diasDeSemana=diasDeSemana+"-Domingo";
		} 
		
		String nombreClase= clase.getActividad().getNombre() +" |"+ diasDeSemana + "| " ;
		return nombreClase;	
	}
}