package com.institucion.fm.fe.bz.spi;

import java.util.ArrayList;
import java.util.List;

public class ClientCriteriaUtil {
	
	public static String getCriteriaAsString(List<Long> originalList) {
		if (originalList == null || originalList.size() == 0)
			return null;
		
		StringBuffer stringBuffer = new StringBuffer();
		
		stringBuffer.append("( ");
		for (Long item : originalList) {
			stringBuffer.append(" ").append(item).append(",");
		}
		stringBuffer.append(" 0 ) ");
		return stringBuffer.toString();
	}
	
	public static List<Long> getCriteria(List<Long> originalList) {
		List<Long> list = new ArrayList<Long>();
		list.addAll(originalList);
		list.add(0L);
		
		return list;
	}

}
