package com.institucion.fm.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class EmailJob extends QuartzJobBean
{
	private static Log log = LogFactory.getLog(EmailJob.class);
//	
//	private BulletinBoardEJB bulletinBoardEJB;
//	
//	public void setBulletinBoardEJB(BulletinBoardEJB bulletinBoardEJB) { this.bulletinBoardEJB = bulletinBoardEJB; }
//	public BulletinBoardEJB getBulletinBoardEJB() { return bulletinBoardEJB; }

	protected void executeInternal(JobExecutionContext context) throws JobExecutionException
	{
		try {
//			bulletinBoardEJB.sendPendingEmails();
		
		} catch (Exception e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new JobExecutionException(e);
		}
	}
}