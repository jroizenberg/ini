package com.institucion.fm.confsys.bz;

import java.util.List;
import java.util.Locale;

import com.institucion.fm.cobj.PromotionLineCObj;
import com.institucion.fm.confsys.model.ConfigRegional;
import com.institucion.fm.confsys.model.Flag;
import com.institucion.fm.security.bz.CallContext;

public interface ConfigSystemEJB{

	/*------------------------- Metodos sobre ConfigRegional ---------------------------------- */
	public List<ConfigRegional> findConfigRegional(CallContext ctx);
	public List<ConfigRegional> getConfigRegional(CallContext ctx);
	public ConfigRegional findConfigRegionalById(CallContext ctx, String id);
	public void save(CallContext ctx, ConfigRegional configRegional);
	public void update(CallContext ctx, ConfigRegional configRegional);
	public void delete(CallContext ctx, ConfigRegional configRegional);
	public abstract void initServerConfigRegional();
	public  Locale getLocale();
	/*------------------------- Metodos para Flag ----------------------------------------- */
	public Flag getFlag();
	public void setFlag(Flag flag);
	/*---------------------------Metodos para delete motives log------------------------------*/
	public String getTimeZoneGmtId();
	public List<PromotionLineCObj> getActivesPromotionLines();
	public void persisteBrandsegmentOrder(List orderObject,	PromotionLineCObj prom);

}