package com.institucion.fm.fe.dao.spi;

import java.util.List;

import com.institucion.fm.conf.dao.BaseActiveService;
/**
 * Abstract Super class for the HealthProffesional, Pharmacy and Institution DAOs
 * @author hhidvegi
 *
 * @param <T>
 */
public abstract class ClientDao<T> extends BaseActiveService<T> {
	
	/**
	 * With a list of IDs, it builds a String like: 1,2,45,67,32
	 * @param Long idsList
	 * @return String
	 */
	protected String buildString_IN_clauseForQuery(List<Long> idsList) {
		StringBuilder sbIds = new StringBuilder();
		
		for(int i = 0; i < idsList.size(); i++){
			Long clientId = idsList.get(i);
			sbIds.append(clientId);
			if(!((i + 1) ==  idsList.size())  )
				sbIds.append(',');
		}
		return sbIds.toString();
	}

}
