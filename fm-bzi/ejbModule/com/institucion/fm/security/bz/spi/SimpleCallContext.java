package com.institucion.fm.security.bz.spi;

import java.io.Serializable;

import com.institucion.fm.security.bz.CallContext;
import com.institucion.fm.security.bz.SecurityCallType;

public class SimpleCallContext implements CallContext, Serializable {
	private static final long serialVersionUID = 1L;
	private SecurityCallType callType;
	private String userName;
	
	public SimpleCallContext(SecurityCallType callType, String userName) {
		this.callType = callType;
		this.userName = userName;
	}

	public SecurityCallType getCallType() {
		return this.callType;
	}

	public String getUserID() {
		return this.userName;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("user=[").append(this.userName);
		buf.append("], callType=[").append(this.callType).append("]");
		return buf.toString();
	}
}
