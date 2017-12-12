package com.institucion.fm.confsys.dao.spi;

import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.institucion.fm.conf.dao.BaseActiveService;
import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.confsys.dao.ConfigRegionalDAO;
import com.institucion.fm.confsys.model.ConfigRegional;


public class ConfigRegionalDAOImpl extends BaseActiveService<ConfigRegional> implements ConfigRegionalDAO
{

	private static Log log = LogFactory.getLog(ConfigRegionalDAOImpl.class);
	
	public List<ConfigRegional> findAll() throws DAOException
	{
		//log.debug("findAll");
		return this.findAll(ConfigRegional.class);
	}
	@Override
	public ConfigRegional save(ConfigRegional c) throws DAOException {
		return super.save(c);
	}
	@Override
	public ConfigRegional update(ConfigRegional c) throws DAOException {
		return super.update(c);
	}
	@Override
	public ConfigRegional findById(Long id) throws DAOException {
		return super.findById(id, ConfigRegional.class);
	}

	public Locale getLocale()
	{
		try
		{
			ConfigRegional configRegional = super.findUnique("from com.institucion.fm.confsys.model.ConfigRegional", null);
			return configRegional.getLocale();
		}
		catch (Exception e)
		{
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			return ConfigRegional.spanishLocale;
		}
	}
}