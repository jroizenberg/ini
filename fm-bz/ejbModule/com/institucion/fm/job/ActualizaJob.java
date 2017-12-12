package com.institucion.fm.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.institucion.bz.InscripcionEJB;

public class ActualizaJob extends QuartzJobBean
{
	private static Log log = LogFactory.getLog(ActualizaJob.class);
	private InscripcionEJB inscripcionEJB;

	protected void executeInternal(JobExecutionContext context) throws JobExecutionException{
		try {
			log.error("Inicializa Correctamente el Job de ActualizaJob");
			log.info("Inicializa Correctamente el Job de ActualizaJob");
			System.out.println("Inicializa Correctamente el Job de ActualizaJob");
			inscripcionEJB.actualizarEstadosDeJob();
			log.error("Termina Correctamente el Job de ActualizaJob");
			log.info("Termina Correctamente  el Job de ActualizaJob");
			System.out.println("Fin Correctamente el Job de ActualizaJob");
		
		} catch (Exception e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new JobExecutionException(e);
		}
	}

	public InscripcionEJB getInscripcionEJB() {
		return inscripcionEJB;
	}

	public void setInscripcionEJB(InscripcionEJB inscripcionEJB) {
		this.inscripcionEJB = inscripcionEJB;
	}
}