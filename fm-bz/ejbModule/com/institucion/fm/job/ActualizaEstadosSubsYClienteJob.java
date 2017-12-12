package com.institucion.fm.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.institucion.bz.InscripcionEJB;

public class ActualizaEstadosSubsYClienteJob extends QuartzJobBean
{
	private static Log log = LogFactory.getLog(ActualizaEstadosSubsYClienteJob.class);
	private InscripcionEJB inscripcionEJB;

	protected void executeInternal(JobExecutionContext context) throws JobExecutionException{
		try {
			log.error("Inicializa Correctamente el Job de ActualizaEstadosSubsYClienteJob");
			log.info("Inicializa Correctamente el Job de ActualizaEstadosSubsYClienteJob");
			inscripcionEJB.actualizarEstadosSubsYClientesDeJob();
			log.error("Termina Correctamente  el Job de ActualizaEstadosSubsYClienteJob");
			log.info("Termina Correctamente  el Job de ActualizaEstadosSubsYClienteJob");

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