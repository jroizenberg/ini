package com.institucion.fm.conf;

import com.institucion.fm.security.bz.CallContext;
import com.institucion.fm.security.bz.SecurityCallType;
import com.institucion.fm.security.bz.spi.SimpleCallContext;

public class CallContextHelper
{
	public static CallContext getCallContext()
	{
		return new SimpleCallContext(SecurityCallType.WEB_CONTAINER, Session.getUsername());
	}
}