package com.institucion.fm.runnable;

import java.util.List;

public interface GenericCreateObject <T> {


	public T createObject(List<String> stringList) throws Exception;
}
