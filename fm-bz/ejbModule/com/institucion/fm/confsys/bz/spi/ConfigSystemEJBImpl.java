package com.institucion.fm.confsys.bz.spi;

import java.util.List;
import java.util.Locale;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import com.institucion.fm.cobj.PromotionLineCObj;
import com.institucion.fm.conf.exception.EJBExceptionHandler;
import com.institucion.fm.confsys.bz.ConfigSystemEJBLocal;
import com.institucion.fm.confsys.bz.ConfigSystemEJBRemote;
import com.institucion.fm.confsys.dao.ConfigRegionalDAO;
import com.institucion.fm.confsys.dao.FlagDAO;
import com.institucion.fm.confsys.model.ConfigRegional;
import com.institucion.fm.confsys.model.Flag;
import com.institucion.fm.security.bz.CallContext;
/**
 * Session Bean implementation class ConfigSystemEJB
 */
@Stateless
@Interceptors( { SpringBeanAutowiringInterceptor.class, EJBExceptionHandler.class })
public class ConfigSystemEJBImpl implements ConfigSystemEJBRemote, ConfigSystemEJBLocal {
	static Log log = LogFactory.getLog(ConfigSystemEJBImpl.class);

	@Autowired
	private ConfigRegionalDAO configRegionalDAO;
	@Autowired
	private FlagDAO flagDAO;

	public ConfigSystemEJBImpl() {		
	}
	/*------------------------- Metodos sobre Subsidiary ---------------------------------- */
	
	
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Boolean getIsUserFromSubdivision(Long idSubdivision) {
    	return null;
    }

	
	public String getTimeZoneGmtId(){
		return null;
	}

	/*------------------------- Metodos sobre ConfigRegional ---------------------------------- */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void delete(CallContext ctx, ConfigRegional configRegional) {
		configRegionalDAO.delete(configRegional);
	}
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<ConfigRegional> findConfigRegional(CallContext ctx) {
		return configRegionalDAO.findAll();
	}
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ConfigRegional findConfigRegionalById(CallContext ctx, String id) {
		return configRegionalDAO.findById(new Long(id));
	}
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<ConfigRegional> getConfigRegional(CallContext ctx) {
		return configRegionalDAO.findAll();
	}
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void save(CallContext ctx, ConfigRegional configRegional) {
		configRegionalDAO.save(configRegional);
	}
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void update(CallContext ctx, ConfigRegional configRegional) {
		configRegionalDAO.update(configRegional);
	}
	/**
	 * Gets the locale.
	 * 
	 * @return the locale
	 */
	/**
	 * Gets the locale.
	 * 
	 * @return the locale
	 */

	public  Locale getLocale()
	{
		return  configRegionalDAO.getLocale();
	}

	public void initServerConfigRegional()
	{
		if (ConfigRegional.instance() == null)
		{
			List<ConfigRegional> configRegionals = configRegionalDAO.findAll();
			ConfigRegional.setConfigRegional(configRegionals.get(0));
		}
	}

	/*------------------------- Metodos para Flag ----------------------------------------- */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Flag getFlag()
	{
		return flagDAO.getFlag();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void setFlag(Flag flag)
	{
		flagDAO.save(flag);
	}
	
	@Override
	public List<PromotionLineCObj> getActivesPromotionLines() {
		return null;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public void persisteBrandsegmentOrder(List orderObject,	PromotionLineCObj prom) {
	}
}