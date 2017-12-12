package com.institucion.fm.security.dao.spi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.institucion.fm.conf.dao.BaseActiveService;
import com.institucion.fm.conf.dao.SqlExecutor;
import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.filteradv.model.CriteriaClause;
import com.institucion.fm.security.dao.UserDAO;
import com.institucion.fm.security.model.User;
import com.institucion.fm.wf.model.RoleType;

public class UserDAOImpl extends BaseActiveService<User> implements UserDAO
{
	private static Log log = LogFactory.getLog(UserDAOImpl.class);
	private SqlExecutor executor;
	
	
	public SqlExecutor getExecutor() {
		return this.executor;
	}

	public void setExecutor(SqlExecutor executor) {
		this.executor = executor;
	}

	public User findByName(String name) throws DAOException
	{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);
		return super.findUnique("from com.institucion.fm.security.model.User where name = :name", params);
	}

	public User findByName(Integer userID) throws DAOException
	{
		Map<String, Object> params = new HashMap<String, Object>();
		if(userID != null){
			params.put("userID", new Long(userID));
			return super.findUnique("from com.institucion.fm.security.model.User where id = :userID", params);
		}else 
			return null;
	}

	
	public User getUserWithOutCollections(String userName){
		String query="select new com.institucion.fm.security.model.User(u.id,u.name, u.lastName, u.firstName,u.password, u.state,"+
	"	 u.validatePass,u.configRegional, u.sucursal) from com.institucion.fm.security.model.User u where u.name=:name" ;
		List l=getHibernateTemplate().findByNamedParam(query, new String[]{"name"}, new Object[]{userName});
		if (l.size()==0)
			return null;
		return (User)l.get(0);
	}
	
	
	public List<String>getUserPermissionsTokens(String userName){
	
		String query="select new java.lang.String(p.token) from com.institucion.fm.security.model.User u left join u.groups g left join g.permissions p where u.name=:name";
		return getHibernateTemplate().findByNamedParam(query, new String[]{"name"}, new Object[]{userName});
	}

	public List<User> findAll() throws DAOException
	{
		log.debug("findAll");
		return this.findAll(User.class);
	}
	
	@Override
	public List<User> getUsersWithOutCollections() {
		StringBuilder query= new StringBuilder(
				"select new com.institucion.fm.security.model.User(u.id,u.name, u.lastName, u.firstName, ")
			.append("u.password, u.state, u.validatePass,u.configRegional) ")
			.append("from com.institucion.fm.security.model.User u ");
		
		return super.find(query.toString(), null);
	}
	
	@Override
	public User save(User c) throws DAOException {
		return super.save(c);
	}
	@Override
	public User update(User c) throws DAOException {
		return super.update(c);
	}
	@Override
	public User findById(Long id) throws DAOException {
		User user = this.findById(id, User.class); 
		this.loadLazyAttribute(user.getGroups());
//		this.loadLazyAttribute(user.getPromotionZones());
		return user;
	}

	@Override
	public List<User> findByCriteria(CriteriaClause criteria) throws DAOException {
		return super.find("from com.institucion.fm.security.model.User "+criteria.getCriteria(), criteria.getCriteriaParameters());
	}

	@Override
	public void delete(User c) throws DAOException {
		super.delete(c);
	}
	


	
	@Override
	public List<String> getUserTxPermissionsTokens(String userName) {
		String query="select new java.lang.String(p.token) from com.institucion.fm.security.model.User u left join u.groups g left join g.permissions p where u.name=:name";
		return getHibernateTemplate().findByNamedParam(query, new String[]{"name"}, new Object[]{userName});
	}
	
	public com.institucion.fm.security.model.User getSupervisorName(String userName){
		
		String query="select wfu.role.treeCode from com.institucion.fm.wf.model.User wfu left join wfu.user u where u.name=:name";
		List l=getHibernateTemplate().findByNamedParam(query, new String[]{"name"}, new Object[]{userName});
		String treeCode=null;
		if (l!=null && l.size()>0)
			treeCode=(String)l.get(0);
		else
			return null;
		String fatherTreeCode=treeCode.substring(0, treeCode.lastIndexOf('.'));
		String queryForSupervisor="select new com.institucion.fm.security.model.User(u.id,u.firstName,u.lastName) from com.institucion.fm.wf.model.User wfu left join wfu.user u where wfu.role.treeCode=:treeCode and wfu.role.type=:supType";
		List lista=getHibernateTemplate().findByNamedParam(queryForSupervisor, new String[]{"treeCode","supType"}, new Object[]{fatherTreeCode,RoleType.SUPERVISOR});
		com.institucion.fm.security.model.User user=null;
		if (lista.size()>0)
			user=(com.institucion.fm.security.model.User)lista.get(0);
		return user;
	}
	
	
	public com.institucion.fm.security.model.User getSupervisorNameByActionId(Long id){
		String query="select ac.SalesForce.role.treeCode from com.institucion.fm.action.model.Action ac where ac.id=:actId";
		List lista=getHibernateTemplate().findByNamedParam(query, new String[]{"actId"}, new Object[]{id});
		String treeCode=null;
		if (lista.size()>0){
			treeCode=(String)lista.get(0);
			String fatherTreeCode=treeCode.substring(0, treeCode.length()-2);
			String queryForSupervisor="select new com.institucion.fm.security.model.User(u.id,u.firstName,u.lastName) from com.institucion.fm.wf.model.User wfu left join wfu.user u where wfu.role.treeCode=:treeCode and wfu.role.type=:supType";
			List listaSup=getHibernateTemplate().findByNamedParam(queryForSupervisor, new String[]{"treeCode","supType"}, new Object[]{fatherTreeCode,RoleType.SUPERVISOR});
			com.institucion.fm.security.model.User user=null;
			if (listaSup.size()>0)
				user=(com.institucion.fm.security.model.User)listaSup.get(0);
			return user;
		}else
			return null;
	}
		
	
	@Override
	public List<Long> getPromotionZonesIds(String userName) {
		List l=getHibernateTemplate().findByNamedParam("select p.id from com.institucion.fm.security.model.User u left join u.promotionZones p where u.name=:name", new String[]{"name"}, new Object[]{userName});
		if (l.size()==1 && null==l.get(0))
			return new ArrayList<Long>();
		return l;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getSpecialitiesId(String userName) {
		List l=getHibernateTemplate().findByNamedParam("select p.id from com.institucion.fm.security.model.User u left join u.specialities p where u.name=:name", new String[]{"name"}, new Object[]{userName});
	
		if (l.size()==1 && null==l.get(0))
			return new ArrayList<Long>();
		return l;
	}

	@Override
	public boolean containsRole(String username,
			com.institucion.fm.security.model.RoleType roleType) {
		String query = 
			"SELECT users.name, role "+
			"  FROM users INNER JOIN user_group ON users.id = user_id "+
			"             INNER JOIN groups ON group_id = groups.id "+
			" WHERE users.name = '"+username+"' AND role = "+roleType.toInt();

		if (this.executor.findSQL(query).size() > 0)
			return true;

		return false;
	}
	

	public void setAccesCountToZero(User user){
		String query="update com.institucion.fm.security.model.UserSecurity us set us.denyAccessCount=0 where us.user.id=?";
		getHibernateTemplate().bulkUpdate(query, new Object[]{user.getId()});
	}

	
	@Override
	public Boolean isFirstLogin(User user) {
		String query="select new java.lang.Boolean(us.firstLogin) from com.institucion.fm.security.model.UserSecurity us where us.user.id=:userId";
		List l=getHibernateTemplate().findByNamedParam(query, new String[]{"userId"}, new Object[]{user.getId()});
		if (l.size()==0)
			return null;
		return (Boolean)l.get(0);
	}
	
	private Integer getDenyAccesCount(User user) {
		String query="select new java.lang.Integer(us.denyAccessCount) from com.institucion.fm.security.model.UserSecurity us where us.user.id=:userId";
		List l=getHibernateTemplate().findByNamedParam(query, new String[]{"userId"}, new Object[]{user.getId()});
		if (l.size()==0)
			return null;
		return (Integer)l.get(0);
	}
	
	@Override
	public void handleWrongPassword(User u,Integer denyAccessLimit) {
		if(u.isValidatePass()){
			Integer denyAccessCount=getDenyAccesCount(u);
			if(denyAccessCount.equals(denyAccessLimit))
				blockUser(u,denyAccessCount);
			else
				incrementUserFailedTries(u,denyAccessCount);
			
//			securityAAService.addDenyAccess(user);
//			
//			if (denyAccessCount+1 == denyAccessLimit)
//				securityAAService.block(user);
		}	
	}
	
	
	private void blockUser(User u,Integer denyAccessCount){
		String query="update com.institucion.fm.security.model.User u set u.state=2 where u.id=?";
		String query1="update com.institucion.fm.security.model.UserSecurity u set denyAccessCount=? where u.user.id=?";
		 
		getHibernateTemplate().bulkUpdate(query,u.getId());
		getHibernateTemplate().bulkUpdate(query1, new Object[]{denyAccessCount+1,u.getId()});
	}
	
	private void incrementUserFailedTries(User u,Integer denyAccessCount){
		String query="update com.institucion.fm.security.model.UserSecurity us set us.denyAccessCount=? where us.user.id=?";
		getHibernateTemplate().bulkUpdate(query, new Object[]{denyAccessCount+1,u.getId()});
	}


}