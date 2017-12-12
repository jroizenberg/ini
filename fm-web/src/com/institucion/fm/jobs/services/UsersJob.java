package com.institucion.fm.jobs.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.institucion.fm.security.service.AuthorizationService;

public class UsersJob extends QuartzJobBean
{
	private static Log log = LogFactory.getLog(UsersJob.class);
	AuthorizationService authService;
	

	public AuthorizationService getAuthService() {
		return authService;
	}


	public void setAuthService(AuthorizationService authService) {
		this.authService = authService;
	}


	@Override
	protected void executeInternal(JobExecutionContext context)	throws JobExecutionException {
		this.getAuthService().refresh();
	}

}
