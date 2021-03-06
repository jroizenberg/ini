package com.institucion.bz.spi;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.interceptor.Interceptors;

import net.sourceforge.jbarcodebean.JBarcodeBean;
import net.sourceforge.jbarcodebean.model.Code128;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import org.springframework.orm.hibernate3.AbstractSessionFactoryBean;

import com.institucion.bz.ClienteEJBLocal;
import com.institucion.bz.ClienteEJBRemote;
import com.institucion.bz.InscripcionEJB;
import com.institucion.dao.ClienteDAO;
import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.conf.exception.EJBExceptionHandler;
import com.institucion.fm.security.model.User;
import com.institucion.model.ActividadYClase;
import com.institucion.model.Clase;
import com.institucion.model.Cliente;
import com.institucion.model.ClienteView;
import com.institucion.model.Concepto;
import com.institucion.model.CupoActividad;
import com.institucion.model.CupoActividadEstadoEnum;
import com.institucion.model.Empleado;
import com.institucion.model.Gastos;
import com.institucion.model.ObraSocial;
import com.institucion.model.Subscripcion;
import com.institucion.model.SubscripcionDeClases;
import com.institucion.model.VencimientoCursoEnum;
import com.institucion.model.VentaProducto;

/**
 * The Class HealthProfessionalEJBImpl.
 */
@Stateless
@Interceptors( { SpringBeanAutowiringInterceptor.class,	EJBExceptionHandler.class })
public class ClienteEJBImpl implements ClienteEJBRemote,ClienteEJBLocal {

	private static Log log = LogFactory.getLog(ClienteEJBImpl.class);

	/** The session. */
	@Autowired
	AbstractSessionFactoryBean session;

	@Autowired
	InscripcionEJB subscripcionEJB;
	
	@Autowired
	private ClienteDAO clienteDao = null;
	
	public ClienteEJBImpl() {	}
	
	@Override
	public void create(Cliente c) throws DAOException{
        clienteDao.create(c);
	}
	
	@Override
	public Long createSubs(Cliente c) throws DAOException{
        return clienteDao.createSubs(c);
	}
	@Override
	public void delete(Cliente c) throws DAOException{
        clienteDao.delete(c);
	}
	
	@Override
	public void save(Cliente c) throws DAOException{
        clienteDao.save(c);	
	}
	
	@Override
	public Cliente findById(Long id) throws DAOException{
        return clienteDao.findById(id);
    }

	@Override
	public List<Cliente> findAllConJdbc(String filters) throws DAOException{
        return clienteDao.findAllConJdbc(filters);
	}
	
	public List<ClienteView> findAllConJdbcSinHibernate(String filters) throws DAOException{
		return clienteDao.findAllConJdbcSinHibernate(filters);
	}

	@Override
	public List<Cliente> findAllConJdbc2(String filters) throws DAOException{
        return clienteDao.findAllConJdbc2(filters);
	}
	@Override
	public Map<Integer, LinkedList<String>> findAllActividadesConJdbc(String filters) throws DAOException{
		return clienteDao.findAllActividadesConJdbc(filters);
	}

	
	public Long getCount(String query){
		return clienteDao.getCount(query);
	}
		
	private boolean debeVolverAlEstadoDeNuevo(Subscripcion subs, CupoActividad cupoAct){
		
		if(subs != null){
			for (Concepto con : subs.getConceptoList()) {
				
				if(con.getCurso() != null){
					int cantiClasesDelCurso=0;
					
					if(con.getCurso().getId().intValue() == cupoAct.getCurso().getId().intValue()
							&&con.getCurso().getActividadYClaseList() != null){
						for (ActividadYClase iterable_element : con.getCurso().getActividadYClaseList()) {
							cantiClasesDelCurso=iterable_element.getCantClases();
						}
						
						if(cantiClasesDelCurso == 1){
							// es un curso por Clase
							if(con.getCurso().getVencimiento().toInt() == VencimientoCursoEnum.QUINCENAL_VENCE_A_LA_QUINCENA.toInt()){
								if(con.getCantSesiones() >0 )
									cantiClasesDelCurso= con.getCantSesiones();
								
							}else{
								if( con.getCantSesiones() >0 )
									cantiClasesDelCurso= con.getCantSesiones();
									
							}
						}
						
						if(cupoAct.getCupos() == cantiClasesDelCurso)
							return true;
					}
				}
			}
			
	
		}
		return false;
	}
		
	public Subscripcion actualizarSubscripcionQuitarDeClase(Subscripcion subs, ActividadYClase actYClase, int cupo){
		/* distintos cambios que puede que llegaroin hasta aca: 
		. Se desconto un cupo
		. Se anulo una subscripcion
		. se vencio una subs
		*/
		// valida primero los cupos y actualiza el estado si no tiene mas cupos	
		
		// Ver que pasa cuando una subscricion tiene mas de un curso
			if(subs != null && subs.getCupoActividadList() != null){
			
				for (CupoActividad cupoAct : subs.getCupoActividadList()) {
				
				if((actYClase == null) || 
						(actYClase != null && cupoAct.getCurso() != null && actYClase.getCurso() != null 
						&& cupoAct.getCurso().getId().intValue() == actYClase.getCurso().getId().intValue())){
					cupoAct.setCupos(cupo);
					actualizarFecha(cupoAct);
				
					if(cupoAct.getEstado().toInt()  == CupoActividadEstadoEnum.ANULADA.toInt()){
					
					}else{
				
						if(cupoAct.getCupos() > 0){
				
							// si se devolvio el cupo, y debe volver al estado origianl poner en null las fechas
							if(debeVolverAlEstadoDeNuevo(subs, cupoAct)){
							cupoAct.setFechaComienzo(null);
							cupoAct.setFechaFin(null);
							}
							
							if(cupoAct.getEstado().toInt()  == CupoActividadEstadoEnum.ADEUDA_Y_SIN_CUPOS.toInt()){
							cupoAct.setEstado(CupoActividadEstadoEnum.C_DEUDAS_Y_C_CUPOS);
							
							}else if(cupoAct.getEstado().toInt()  == CupoActividadEstadoEnum.S_CUPOS.toInt()){
							cupoAct.setEstado(CupoActividadEstadoEnum.C_CUPOS);
							}
							
						}else{
							if(cupoAct.getEstado().toInt()  == CupoActividadEstadoEnum.C_DEUDAS_Y_C_CUPOS.toInt()){
								cupoAct.setEstado(CupoActividadEstadoEnum.ADEUDA_Y_SIN_CUPOS);
							}else if(cupoAct.getEstado().toInt()  == CupoActividadEstadoEnum.C_CUPOS.toInt()){
								cupoAct.setEstado(CupoActividadEstadoEnum.S_CUPOS);
							}
						}			
					}
				}
			}
		}
		return subs;
	}

	public Subscripcion actualizarSubscripcion(Subscripcion subs, ActividadYClase actYClase){
		/* distintos cambios que puede que llegaroin hasta aca: 
										. Se desconto un cupo
										. Se anulo una subscripcion
										. se vencio una subs
		*/
		// valida primero los cupos y actualiza el estado si no tiene mas cupos	
		
		// Ver que pasa cuando una subscricion tiene mas de un curso
		if(subs != null && subs.getCupoActividadList() != null){
			
			for (CupoActividad cupoAct : subs.getCupoActividadList()) {
				
				if((actYClase == null) || 
						(actYClase != null && cupoAct.getCurso() != null && actYClase.getCurso() != null 
						 && cupoAct.getCurso().getId().intValue() == actYClase.getCurso().getId().intValue())){

					actualizarFecha(cupoAct);

					if(cupoAct.getEstado().toInt()  == CupoActividadEstadoEnum.ANULADA.toInt()){
						
					}else{
						
						if(cupoAct.getCupos() > 0){
							
							// si se devolvio el cupo, y debe volver al estado origianl poner en null las fechas
							if(debeVolverAlEstadoDeNuevo(subs, cupoAct)){
								cupoAct.setFechaComienzo(null);
								cupoAct.setFechaFin(null);
							}
							
							if(cupoAct.getEstado().toInt()  == CupoActividadEstadoEnum.ADEUDA_Y_SIN_CUPOS.toInt()){
								cupoAct.setEstado(CupoActividadEstadoEnum.C_DEUDAS_Y_C_CUPOS);

							}else if(cupoAct.getEstado().toInt()  == CupoActividadEstadoEnum.S_CUPOS.toInt()){
								cupoAct.setEstado(CupoActividadEstadoEnum.C_CUPOS);
							}

							
						}else{
							if(cupoAct.getEstado().toInt()  == CupoActividadEstadoEnum.C_DEUDAS_Y_C_CUPOS.toInt()){
								cupoAct.setEstado(CupoActividadEstadoEnum.ADEUDA_Y_SIN_CUPOS);
							}

							else if(cupoAct.getEstado().toInt()  == CupoActividadEstadoEnum.C_CUPOS.toInt()){
								cupoAct.setEstado(CupoActividadEstadoEnum.S_CUPOS);
							}
						}			
					}
				}
			}
		}
		return subs;
	}

	
	/*
	 *  Actualizo la fecha de Inicio y vencimiento de cada actividad
	 */
	public CupoActividad actualizarFecha(CupoActividad cupoAct){
		if(cupoAct.getFechaComienzo() == null){
			
			cupoAct.setFechaComienzo(new Date());
			VencimientoCursoEnum venc= cupoAct.getCurso().getVencimiento();
			
			// vencimiento semanal
			if(venc.equals(VencimientoCursoEnum.LIBRE_VENCE_A_LA_SEMANA) || venc.equals(VencimientoCursoEnum.SEMANAL_VENCE_A_LA_SEMANA)){
				Calendar calendarDate = Calendar.getInstance();  
				calendarDate.setTime(cupoAct.getFechaComienzo());  
				calendarDate.add(Calendar.DAY_OF_YEAR, 7); 
				
				cupoAct.setFechaFin(calendarDate.getTime());
			
			} else if(venc.equals(VencimientoCursoEnum.QUINCENAL_VENCE_A_LA_QUINCENA)){
				Calendar calendarDate = Calendar.getInstance();  
				calendarDate.setTime(cupoAct.getFechaComienzo());
				
				// aca falta ver si hace los 14 dias o hace parte, dependiendo de lo que se agregue en la subscripcion
				
				calendarDate.add(Calendar.DAY_OF_YEAR, 19); 			
				cupoAct.setFechaFin(calendarDate.getTime());

			} else if(venc.equals(VencimientoCursoEnum.LIBRE_VENCE_A_LA_QUINCENA)){
				Calendar calendarDate = Calendar.getInstance();  
				calendarDate.setTime(cupoAct.getFechaComienzo());
				
				// aca falta ver si hace los 14 dias o hace parte, dependiendo de lo que se agregue en la subscripcion
				
				calendarDate.add(Calendar.DAY_OF_YEAR, 14); 			
				cupoAct.setFechaFin(calendarDate.getTime());
				
			} else if(venc.equals(VencimientoCursoEnum.LIBRE_VENCE_AL_MES) || venc.equals(VencimientoCursoEnum.SEMANAL_VENCE_AL_MES)
					|| venc.equals(VencimientoCursoEnum.MENSUAL_VENCE_AL_MES) ){
//					|| venc.equals(VencimientoCursoEnum.SOLO_POR_CLASE_VENCE_AL_MES)){
				Calendar calendarDate = Calendar.getInstance();  
				calendarDate.setTime(cupoAct.getFechaComienzo());
				calendarDate.add(Calendar.MONTH, 1); 
				cupoAct.setFechaFin(calendarDate.getTime());						

				
			} else if(venc.equals(VencimientoCursoEnum.LIBRE_VENCE_A_LOS_3_MES) || venc.equals(VencimientoCursoEnum.SEMANAL_VENCE_A_LOS_3_MES)
					|| venc.equals(VencimientoCursoEnum.MENSUAL_VENCE_A_LOS_3_MES) ){
				Calendar calendarDate = Calendar.getInstance();  
				calendarDate.setTime(cupoAct.getFechaComienzo());
				calendarDate.add(Calendar.MONTH, 3); 
				cupoAct.setFechaFin(calendarDate.getTime());						

			}else if(venc.equals(VencimientoCursoEnum.SEMANAL_VENCE_AL_ANo)|| venc.equals(VencimientoCursoEnum.MENSUAL_VENCE_AL_ANo ) ){
				Calendar calendarDate = Calendar.getInstance();  
				calendarDate.setTime(cupoAct.getFechaComienzo());
				calendarDate.add(Calendar.MONTH, 12); 
				cupoAct.setFechaFin(calendarDate.getTime());						

			}
		}
	return cupoAct;
	}
	
	public boolean isAdeudaAlgo(Long clientID){
		boolean result= false;
		String query= "	 select count(*) from cliente c  " +
				" inner join subscripcion s on (c.id= s.idcliente) 	" +
				" inner join cupoactividad ca on (ca.idsubscripcion= s.id)" +
				"	where ca.estado in (0, 1, 10) and c.id="+ clientID;
		Long cou=clienteDao.getCount(query);
		if(cou != null && cou.intValue() > 0)
			result= true;
		
		return result;
	}
	
	public String getResponsableAdeuda(Long clientID){
		String result= null;
		String query= "	 select distinct(s.idusuariocreosubscripcion) from cliente c  " +
				"inner join subscripcion s on (c.id= s.idcliente) 	" +
				"inner join cupoactividad ca on (ca.idsubscripcion= s.id)	" +
				"where ca.estado in (0, 1, 10) and c.id="+ clientID;
		List<Integer> cou=clienteDao.getResponsableAdeuda(query);
		if(cou != null && cou.size()  > 0){
			for (Integer long1 : cou) {
				if(long1 != null){
					User u=subscripcionEJB.findByName(long1);	
					if(u != null){
						if(result == null)
							result = u.getName();
						else
							result = result + ", "+ u.getName();
					}
				}
			}
		}
		return result;
	}

	public Date getFechaVentaAdeuda(Long clientID){
		String query= "	 select min(fechayhoracreacion) from cliente c  " +
				" inner join subscripcion s on (c.id= s.idcliente) 	" +
				" inner join cupoactividad ca on (ca.idsubscripcion= s.id)" +
				" 	where ca.estado in (0, 1, 10) and c.id="+ clientID;
		Date cou=clienteDao.getFechaVentaAdeuda(query);
		if(cou != null){
			return cou;
		}else
			return null;
	}	
	
	public boolean isTieneDisponibles(Long clientID){
		boolean result= false;
		String query= " select count(*) from cliente c inner join subscripcion s on (c.id= s.idcliente)  "+
		  "  inner join cupoactividad ca on (ca.idsubscripcion= s.id) where ca.estado in (3, 10)  and  c.id="+clientID   ;
		Long cou=clienteDao.getCount(query);
		if(cou != null && cou.intValue() > 0)
			result= true;
		
		return result;
	}
	
	public Gastos loadLazy(Gastos os){
		if(os != null){
			clienteDao.loadLazyAttribute(os.getPagaSueldosPorMes());
			clienteDao.loadLazyAttribute(os.getGastosSueldoList());

		}
		return os;
	}

	public VentaProducto loadLazy(VentaProducto vt, boolean pagos, boolean productos){
		if(vt != null){
			if(pagos)
				clienteDao.loadLazyAttribute(vt.getPagosPorSubscripcionList());

			if(productos)
				clienteDao.loadLazyAttribute(vt.getProductosList());

		}
		return vt;
	}

	public Empleado loadLazy(Empleado os){
		if(os != null){
			clienteDao.loadLazyAttribute(os.getActividades());
		}
		return os;	
	}
	

	
	public ObraSocial loadLazy(ObraSocial os){
		if(os != null){
			clienteDao.loadLazyAttribute(os.getPreciosActividadesObraSocial());

		}
		return os;
	}

	
	public Cliente loadLazy(Cliente cli, boolean subs, boolean CupoActividad, boolean pagosPorSubs, boolean ingresoInscripcion){
		
		if(cli != null){
			if(subs){
				clienteDao.loadLazyAttribute(cli.getSubscripcionesList());
				
				if(CupoActividad || pagosPorSubs){
					for (Subscripcion subss : cli.getSubscripcionesList()) {
						if(CupoActividad)
							clienteDao.loadLazyAttribute(subss.getCupoActividadList());
						
						if(pagosPorSubs)
							clienteDao.loadLazyAttribute(subss.getPagosPorSubscripcionList());
					}
					
				}
				
			}
			
			if(ingresoInscripcion)
				clienteDao.loadLazyAttribute(cli.getInscripcionesList());
		}
		return cli;
	}
	
	public Clase loadLazy(Clase clase, boolean clientesEnClase, boolean ingresoAClaseSinFechas, boolean claseConListaAlumnosList, boolean claseConListaAlumnosHistoryList){
		
		if(clase != null){
			
			if(clientesEnClase)
				clienteDao.loadLazyAttribute(clase.getClienesEnClase());

			if(ingresoAClaseSinFechas)
				clienteDao.loadLazyAttribute(clase.getIngresoAClaseSinFechas());

			if(claseConListaAlumnosList)
				clienteDao.loadLazyAttribute(clase.getClaseConListaAlumnosList());

			if(claseConListaAlumnosHistoryList)
				clienteDao.loadLazyAttribute(clase.getClaseConListaAlumnosHistoryList());

		}
		return clase;
	}
	
	public Subscripcion loadLazy(Subscripcion subscripcion,boolean CupoActividad, boolean pagosPorSubs, boolean concepto, 
			boolean subsDeClase, boolean clasesSelecList){
		
		if(subscripcion != null){
				
				if(CupoActividad || pagosPorSubs || concepto || subsDeClase || clasesSelecList){
					if(CupoActividad)
						clienteDao.loadLazyAttribute(subscripcion.getCupoActividadList());
					
					if(pagosPorSubs)
						clienteDao.loadLazyAttribute(subscripcion.getPagosPorSubscripcionList());
					
					if(concepto)
						clienteDao.loadLazyAttribute(subscripcion.getConceptoList());
					
					if(subsDeClase){
						clienteDao.loadLazyAttribute(subscripcion.getSubscripcionDeClases());
						if(clasesSelecList){
							for (SubscripcionDeClases iterable_element : subscripcion.getSubscripcionDeClases()) {
								clienteDao.loadLazyAttribute(iterable_element.getClaseSeleccionadasList());
								
							}
						}
					}

				}
		}
		return subscripcion;
	}
	@Override
	public String  generarCodigosDeBarras(List<Cliente> clientList) throws Exception{
		XWPFDocument doc = new XWPFDocument();
		
        XWPFParagraph title = doc.createParagraph();    
        XWPFRun run = title.createRun();
        title.setAlignment(ParagraphAlignment.CENTER);
        
		Iterator ite=clientList.iterator();
		while(ite.hasNext()){
			Cliente cli=(Cliente)ite.next();
			if(cli != null){
				String id=String.valueOf(cli.getId());
				int totalaAgregarCeros=6 - id.length();
				String nuevoVal=null;
				for(int i=0; i<= totalaAgregarCeros; i++){
					if(nuevoVal== null)
						nuevoVal="0";
					else
						nuevoVal=nuevoVal+ "0";
				}
				String nuevoVal2= nuevoVal + id;
				
				String nombreFinal=null;
				if(cli.getApellido() != null)
						nombreFinal= cli.getApellido();
				if(cli.getNombre() != null){
					if(nombreFinal != null)
						nombreFinal= nombreFinal +", "+ cli.getNombre();
					else
						nombreFinal= cli.getNombre();
				}
				if(nombreFinal != null && nombreFinal.length() > 20){
					nombreFinal= nombreFinal.substring(0, 20);
				} 
				if(nombreFinal != null ){
					nombreFinal=nombreFinal.toUpperCase();
				}
				
				BufferedImage  buff= obtenerCodigoBarras(nuevoVal2,nombreFinal );
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				ImageIO.write(buff, "png", os);
				InputStream is = new ByteArrayInputStream(os.toByteArray());
				run.addPicture(is, XWPFDocument.PICTURE_TYPE_PNG, null, Units.toEMU(120), Units.toEMU(50)); 
	            os.close();
	            is.close();
			}
		}	
		Calendar ahoraCal= Calendar.getInstance();
		   int mes=ahoraCal.get(Calendar.MONTH) + 1;
		   String fechaNac=ahoraCal.get(Calendar.DATE)+"-"+mes+"-"+ahoraCal.get(Calendar.YEAR)+"_"+
				   ahoraCal.get(Calendar.HOUR_OF_DAY);

		try{
			String folder="/home/ubuntu/codigosBarras";
//			String folder="C://Users//Jonathan R//Desktop//prueba";
			File f = new File(folder);
			f.mkdir();
			
			String archivo="codigosBarras"+"-"+fechaNac+".doc";
			File f2 = new File(folder+"//"+archivo);
			FileOutputStream fos = new FileOutputStream(f2);
			 doc.write(fos);
			 fos.close();  
			 doc.close();
			return f2.getAbsolutePath();
		}catch (Exception ex){
		ex.printStackTrace();
		}
		return null;
	}
	

	

	public BufferedImage obtenerCodigoBarras(String id, String nomYApe){
		
	    JBarcodeBean barcode = new JBarcodeBean();
	    // nuestro tipo de codigo de barra
	    barcode.setCodeType(new Code128());
	    // nuestro valor a codificar y algunas configuraciones mas
	    barcode.setCode(id);
	    barcode.setCheckDigit(true);
	    barcode.setShowText(false);
	    barcode.setEnabled(true);
	    BufferedImage bufferedImage = barcode.draw(new BufferedImage(180, 100, BufferedImage.TYPE_INT_RGB));
		Graphics2D g2 = bufferedImage.createGraphics();
		g2.setColor(Color.BLACK);
		
		Font currentFont = g2.getFont();
		Font newFont = currentFont.deriveFont(currentFont.getSize() * 1.1F);
		g2.setFont(newFont);
		if(nomYApe != null)
			g2.drawString(nomYApe,10, 10);

		g2.dispose();
	
		return bufferedImage;
	}

}