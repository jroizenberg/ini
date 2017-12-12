package com.institucion.fm.confsys.dao.spi;

import java.util.List;

import com.institucion.fm.conf.dao.BaseActiveService;
import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.confsys.dao.FlagDAO;
import com.institucion.fm.confsys.model.Flag;

public class FlagDAOImpl extends BaseActiveService<Flag> implements FlagDAO
{
	public Flag save(Flag c) throws DAOException
	{
		return super.save(c);
	}

	public void delete(Flag c) throws DAOException
	{
		super.delete(c);
	}

	public Flag getFlag() throws DAOException
	{
		List<Flag> flags = super.findAll(Flag.class);
		if (flags.isEmpty())
		{
			Flag flag = new Flag();
			flag.setReloadPermission(false);
			this.save(flag);
			return flag;
		}
		return flags.get(0);
	}
}