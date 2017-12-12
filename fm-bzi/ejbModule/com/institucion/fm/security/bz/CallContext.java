package com.institucion.fm.security.bz;

public interface CallContext {
	String getUserID();
	SecurityCallType getCallType();
}
