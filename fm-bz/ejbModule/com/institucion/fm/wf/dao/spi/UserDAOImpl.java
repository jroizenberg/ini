package com.institucion.fm.wf.dao.spi;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.institucion.fm.cobj.UserClObj;
import com.institucion.fm.cobj.ValidateUserinRoleCObj;
import com.institucion.fm.cobj.WsPermissionCObj;
import com.institucion.fm.cobj.WsValidationCObj;
import com.institucion.fm.conf.dao.BaseActiveService;
import com.institucion.fm.conf.dao.SqlExecutor;
import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.conf.exception.LocalizeValidationException;
import com.institucion.fm.conf.exception.WorkFlowException;
import com.institucion.fm.filteradv.model.CriteriaClause;
import com.institucion.fm.security.model.UserState;
import com.institucion.fm.wf.dao.UserDAO;
import com.institucion.fm.wf.model.ApprovalType;
import com.institucion.fm.wf.model.Role;
import com.institucion.fm.wf.model.RoleType;
import com.institucion.fm.wf.model.User;

/**
 * The Class UserDAOImpl.
 */
public class UserDAOImpl extends BaseActiveService<User> implements UserDAO {

	private static Log log = LogFactory.getLog(UserDAOImpl.class);
	
	/** The sql. */
	private SqlExecutor sql;

	/**
	 * Gets the sql.
	 * 
	 * @return the sql
	 */
	public SqlExecutor getSql() {
		return this.sql;
	}

	/**
	 * Sets the sql.
	 * 
	 * @param sql
	 *            the new sql
	 */
	public void setSql(SqlExecutor sql) {
		this.sql = sql;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.institucion.fm.wf.dao.UserDAO#findAll()
	 */
	@Override
	public List<User> findAll() throws DAOException {
		// log.debug("findAll");
		return this.findAll(User.class);
		// return
		// this.find("from from com.institucion.fm.wf.model.User order by user.user.name",
		// null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.institucion.fm.wf.dao.UserDAO#findAllSupervisors()
	 */
	public List<User> findAllSupervisors() throws DAOException {
		// log.debug("findAllSupervisors");
		return this
				.find(
						"select usr from com.institucion.fm.wf.model.User as usr where usr.role.type = 2 order by usr.user.name",
						null);
	}

	public List<User> findUsersByType(RoleType type) throws DAOException {
		// log.debug("findAllSupervisors");
		return this
				.find(
						"select usr from com.institucion.fm.wf.model.User as usr where usr.role.type = " + type.toInt() + " order by usr.user.name",
						null);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.institucion.fm.wf.dao.UserDAO#findUsersBySupervisor(com.institucion.fm.
	 * wf.model.User)
	 */
	public List<User> findUsersBySupervisor(User supervisor)
			throws DAOException {
		// log.debug("findUsersBySupervisor");
		loadLazyAttribute(supervisor.getRole());
		return this
				.find(
						"select usr from com.institucion.fm.wf.model.User as usr where usr.role.treeCode like '"
								+ supervisor.getRole().getTreeCode()
								+ "%' and usr.role.type = 1 order by usr.user.name",
						null);
	}
	
	public List<User> findBySupervisorAndCriteria (User supervisor, CriteriaClause criteria){
		String nom = "";
		if(!criteria.getCriteriaParameters().isEmpty()){
			nom=((criteria.getCriteriaParameters().values()).toArray())[0].toString();
		}
		this.loadLazyAttribute(supervisor.getRole());
		StringBuffer consulta = new StringBuffer("");
		consulta.append("from com.institucion.fm.wf.model.User AS wfuser ");
		consulta.append("INNER JOIN fetch wfuser.user AS modeluser ");
		consulta.append("INNER JOIN fetch wfuser.role AS userrol ");
		consulta.append(" WHERE userrol.treeCode like '");
		consulta.append(supervisor.getRole().getTreeCode());
		consulta.append("%'");
		consulta.append(" AND (upper(modeluser.firstName) like upper('%");
		consulta.append(nom);
		consulta.append("%') ");
		consulta.append("OR upper(modeluser.lastName) like upper('%");
		consulta.append(nom);
		consulta.append("%'))");
		List<User> users =  super.find(consulta.toString(),null);//, criteria.getCriteriaParameters());
		return users;

	}
	
	public List<User> findUsersBySupervisorsAndCriteria(List<User> supervisors, CriteriaClause criteria) throws DAOException{
		ArrayList<User> users = new ArrayList<User>();
		for (User sup: supervisors){
			loadLazyAttribute(sup.getRole());
			users.addAll(this.findBySupervisorAndCriteria (sup, criteria));
		}
		return users;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.institucion.fm.wf.dao.UserDAO#findById(java.lang.Long)
	 */
	@Override
	public User findById(Long id) throws DAOException {
		return this.findById(id, User.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.institucion.fm.conf.dao.BaseActiveService#loadLazyAttribute(java.lang
	 * .Object)
	 */
	@Override
	public void loadLazyAttribute(Object attribute) throws DAOException {
		super.loadLazyAttribute(attribute);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.institucion.fm.wf.dao.UserDAO#findWFUserByUserId(java.lang.Long)
	 */
	@Override
	public User findWFUserByUserId(Long userid) throws DAOException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userid", userid);
		List<User> list = this
				.find(
						"from com.institucion.fm.wf.model.User wfuser where wfuser.user.id = :userid",
						params);
		if (list.size() > 0)
			return list.get(0);
		return null;
	}

	@Override
	public List<User> findWFUserByUserId(List<Long> userIds) throws DAOException {
		StringBuffer query = new StringBuffer();
		query.append("from com.institucion.fm.wf.model.User wfuser where wfuser.user.id in (");
		
    	boolean first = true;
    	
    	for (Long userId : userIds) {
    		if (!first) {
    			query.append(", ");
    		} else {
    			first = false;
    		}
    		query.append(userId);
		}
    	query.append(" )");

		List<User> list = this.find(query.toString(), null);
		return list;
	}
	
	@Override
	public List<User> findWFUsersByWfUsersId(List<Integer> wfUsersIds) throws DAOException {
		List<User> result = new ArrayList<User>();
		if (wfUsersIds.size() > 0 ) {
			StringBuffer query = new StringBuffer();
			query.append("from com.institucion.fm.wf.model.User wfuser where wfuser.id in (");
			
	    	boolean first = true;
	    	for (Integer userId : wfUsersIds) {
	    		if (!first) {
	    			query.append(", ");
	    		} else {
	    			first = false;
	    		}
	    		query.append(userId);
			}
	    	query.append(" )");

			result = this.find(query.toString(), null);
			for (User user : result) {
				this.loadLazyAttribute(user);
				this.loadLazyAttribute(user.getRole());
			}
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Long> findUsersPerPromotionLine(Long promotionLineId)throws DAOException{
		
		List<Integer> list = (List<Integer>) sql.findSQL("select distinct u.id from portfolio p, wf_user wfu, users u where p.id_hpa = wfu.id and wfu.userid = u.id and p.id_promotionline = " + promotionLineId, null);
		List<Long> result = null;
		if (list !=null){
			result = new ArrayList<Long>();
			for (Integer integer: list){
				result.add(new Long(integer));
			}
		}
		return result;
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.institucion.fm.wf.dao.UserDAO#findByCriteria(com.institucion.fm.filteradv
	 * .model.CriteriaClause)
	 */
	@Override
	public List<User> findByCriteria(CriteriaClause criteria) {
		// return
		// super.find("from com.institucion.fm.wf.model.User AS wfuser INNER JOIN com.institucion.fm.security.model.User AS user "+criteria.getCriteria(),
		// criteria.getCriteriaParameters());
		// Map<String, Object> params = new HashMap<String, Object>();

		// return
		// super.find("select wfuser from com.institucion.fm.wf.model.User AS wfuser INNER JOIN fetch wfuser.user AS modeluser INNER JOIN wfuser.role as role "+criteria.getCriteria(),
		// criteria.getCriteriaParameters());
		return super
				.find(
						"from com.institucion.fm.wf.model.User AS wfuser INNER JOIN fetch wfuser.user AS modeluser  "
								+ criteria.getCriteria(), criteria
								.getCriteriaParameters());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.institucion.fm.wf.dao.UserDAO#findUserByCriteria(com.institucion.fm.filteradv
	 * .model.CriteriaClause)
	 */
	@Override
	public List<User> findUserByCriteria(CriteriaClause criteria) {
		return super
				.find(
						"select wfuser from com.institucion.fm.wf.model.User AS wfuser INNER JOIN fetch wfuser.user AS modeluser INNER JOIN wfuser.role as role "
								+ criteria.getCriteria(), criteria
								.getCriteriaParameters());
		// return
		// super.find("from com.institucion.fm.wf.model.User AS wfuser INNER JOIN fetch wfuser.user AS modeluser  "+criteria.getCriteria(),
		// criteria.getCriteriaParameters());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.institucion.fm.wf.dao.UserDAO#findUsersByTreeCode(com.institucion.fm.filteradv
	 * .model.CriteriaClause)
	 */
	@Override
	public List<User> findUsersByTreeCode(CriteriaClause criteria) {
		List<User>users=super.find("from com.institucion.fm.wf.model.User "
				+ criteria.getCriteria(), criteria.getCriteriaParameters());
		return users;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.institucion.fm.wf.dao.UserDAO#findByName(java.lang.String)
	 */
	public User findByName(String username) throws DAOException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", username);
		return super.findUnique("from com.institucion.fm.wf.model.User AS wfuser INNER JOIN fetch wfuser.user AS modeluser where modeluser.name = :username", params);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.institucion.fm.wf.dao.UserDAO#getRoleType(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public RoleType getRoleType(String username) {
//		String query = "SELECT roleid FROM users INNER JOIN wf_user ON users.id = wf_user.userid "
//				+ "INNER JOIN wf_role ON wf_role.id = wf_user.roleid "
//				+ " WHERE users.name = '" + username + "'";
		
		String query = " select type from wf_role " +
						"inner join wf_user on (wf_user.roleid=wf_role.id) " +
						"inner join users on (users.id=wf_user.userid) " +
						" WHERE Upper(users.name) = '" + username.toUpperCase() + "'";
		List<Integer> roles = (List<Integer>) sql.findSQL(query);
		if (roles.size() > 0)
			return RoleType.fromInt(roles.get(0));

		return null;
	}

	public List<Integer> findWfUsersIdByCriteriaFilters(String name, String description) throws DAOException {
		boolean where = false;
		StringBuffer query = new StringBuffer();
		query.append("select uwf.id from wf_user uwf");
		query.append(" inner join users us on (uwf.userid = us.id)");
		query.append(" inner join wf_role r on (uwf.roleid = r.id)");
		
		if (description != null && !"".equalsIgnoreCase(description)) {
			query.append(" where");
			query.append(" Upper(us.firstname) || Upper(us.lastname) like '%" + description.toUpperCase() + "%'");
			where = true;
		}

		if (name != null && !"".equalsIgnoreCase(name)) {
			if (!where) {
				query.append(" where");
				query.append(" Upper(r.name) like '%" + name.toUpperCase() + "%'");
			} else {
				query.append(" and Upper(r.name) like '%" + name.toUpperCase() + "%'");	
			}
		}
		query.append(" and uwf.type != 1");
		List<Integer> ids = (List<Integer>) sql.findSQL(query.toString());
		return ids;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.institucion.fm.wf.dao.UserDAO#getWfUserCob(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public UserClObj getWfUserClObj(final String userName) {
		final String USER_NAME = "userName";
		List<UserClObj> usersClObj = getHibernateTemplate().findByNamedParam(
						"select new com.institucion.fm.cobj.UserClObj(wfu.id, wfu.user.name, wfu.user.lastName, wfu.role.type, wfu.role.treeCode) "
								+ "from com.institucion.fm.wf.model.User wfu "
								+ "left join wfu.user u where u.state=0 and u.name=:"
								+ USER_NAME, USER_NAME, userName);
		return usersClObj.size() > 0 ? usersClObj.get(0) : null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.institucion.fm.wf.dao.UserDAO#getAllWfUserClObj()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<UserClObj> getAllWfUserClObj() {
		final String ROLE_LABEL = "role";
		final RoleType ROLE_VALUE = com.institucion.fm.wf.model.RoleType.fromInt(1);
		return this.getHibernateTemplate().findByNamedParam("select new com.institucion.fm.cobj.UserClObj(u.id, u.user.firstName, u.user.lastName) from com.institucion.fm.wf.model.User u where u.user.state=0 and u.role.type =:" + ROLE_LABEL + " order by u.user.firstName asc", ROLE_LABEL, ROLE_VALUE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.institucion.fm.wf.dao.UserDAO#getUserClObjByHierachy(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<UserClObj> getUserClObjByHierachy(final String userRoleTreeCode) {
		final String USER_ROLE_TREE_CODE_LABEL = "treeCode";
		return this.getHibernateTemplate().findByNamedParam("select new com.institucion.fm.cobj.UserClObj(u.id, u.user.firstName, u.user.lastName) from com.institucion.fm.wf.model.User u inner join u.role role where upper(role.treeCode) like upper(:" + USER_ROLE_TREE_CODE_LABEL + ") order by u.user.firstName asc", USER_ROLE_TREE_CODE_LABEL, userRoleTreeCode + ".%");
	}
	
	
	public ValidateUserinRoleCObj validateWfUser(String userName)throws LocalizeValidationException
	{
		String query="select new  com.institucion.fm.cobj.ValidateUserinRoleCObj(u.id,u.role.parent.id) from com.institucion.fm.wf.model.User u left join u.user secUser where secUser.name=:name";
		List user=getHibernateTemplate().findByNamedParam(query, new String[]{"name"}, new Object[]{userName});
		if (user==null ||user.size()==0)
			throw new LocalizeValidationException("error.nedd.to.be.wf.user");
		return (ValidateUserinRoleCObj)user.get(0);

	}
	
	public Boolean validateWfuserWithoutException(String name){
		String query="select secUser.id from com.institucion.fm.wf.model.User u left join u.user secUser where secUser.name=:name";
		List id=getHibernateTemplate().findByNamedParam(query, new String[]{"name"}, new Object[]{name});
		if (id==null ||id.size()==0)
			return false;
		else
			return true;
	}
	
	public Long isInAdminRoles(String name){
		String query="select new java.lang.Long(u.id) from com.institucion.fm.security.model.User u left join u.groups gr where ((gr.role=:FMADMIN or gr.role=:WFMANAGER or gr.role=:SYSADMIN) and (u.name='"+name+"'))";
		List res=getHibernateTemplate().findByNamedParam(query,new String[]{"FMADMIN","WFMANAGER","SYSADMIN"},new Object[]{com.institucion.fm.security.model.RoleType.FMADMIN,com.institucion.fm.security.model.RoleType.WFMANAGER,com.institucion.fm.security.model.RoleType.SYSADMIN});
		return res!=null && res.size()>0?(Long)res.get(0):null;
	}
	
	@Override
	public Long getWfUserIdByRoleId(Long roleId) {
		String query="select u.id from com.institucion.fm.wf.model.User u where u.role.id=:roleId";
		return (Long)getHibernateTemplate().findByNamedParam(query, new String[]{"roleId"}, new Object[]{roleId}).get(0);
	}

	@Override
	public void validateWorkfowUser(String userName) {
	
		String query="select new java.lang.Long(wfu.id) from com.institucion.fm.wf.model.User wfu left join wfu.user u where u.name=:name";
		List user=getHibernateTemplate().findByNamedParam(query, new String[]{"name"}, new Object[]{userName});
		if (user.size()==0)
			throw new LocalizeValidationException("error.nedd.to.be.wf.user");
			
		
	}

	public User getWfUserByRoleIdAndNotPending(Long roleId) throws DAOException {
		User wfuser = null;
			List<User> list = this.find("from com.institucion.fm.wf.model.User wfus where wfus.role.id = " + roleId +
					" and wfus.type = 2", null);
			if(list.size()>0) {
				wfuser = list.get(0);
				return wfuser;	
			}
			return wfuser;
	}
	public User getWfUserById(Long wfUserId) throws DAOException {
		User wfuser = null;
			List<User> list = this.find("from com.institucion.fm.wf.model.User wfus where wfus.id = " + wfUserId, null);
			if(list.size()>0) {
				wfuser = list.get(0);
				return wfuser;	
			}
			return wfuser;
	}
	
	public User getWfUserId(String userName){
		String query="select new com.institucion.fm.wf.model.User(wfu.id) from com.institucion.fm.wf.model.User wfu left join wfu.user u where u.name=:name";
		List user=getHibernateTemplate().findByNamedParam(query, new String[]{"name"}, new Object[]{userName});
		if(user.size()==0)
			throw new LocalizeValidationException("error.nedd.to.be.wf.user");
		else
			return (User)user.get(0);
	}
	
	public List<User> findAllSimpleSupervisors() throws DAOException {
		//log.debug("findAllSupervisors");
		return this.find("select new com.institucion.fm.wf.model.User(usr.id, usr.user.id, usr.user.name, usr.user.firstName, usr.user.lastName, usr.role) from com.institucion.fm.wf.model.User as usr inner join usr.role where usr.role.type = 2 order by usr.user.name", null);
	}
	
//	public List<User> findSupervisorsByPromotionLine (PromotionLine promotionLine) throws DAOException{
//		StringBuilder hqlResponsible = new StringBuilder("select new com.institucion.fm.wf.model.User(usr.id, usr.user.id, usr.user.name, usr.user.firstName, usr.user.lastName, usr.role) ");
//		hqlResponsible.append("from com.institucion.fm.wf.model.User usr inner join usr.user ");
//		hqlResponsible.append("where usr.id = ");
//		hqlResponsible.append(promotionLine.getResponsibleID().toString());
//		User promotionLineResponsible = (User)this.findUnique(hqlResponsible.toString(),null); 
//		loadLazyAttribute(promotionLineResponsible.getRole());
//		
//		StringBuilder hql = new StringBuilder("Select new com.institucion.fm.wf.model.User(usr.id, usr.user.id, usr.user.name, usr.user.firstName, usr.user.lastName, usr.role) ");
//		hql.append("from com.institucion.fm.wf.model.User usr inner join usr.role ");
//		hql.append("where usr.role.treeCode like '");
//		hql.append(promotionLineResponsible.getRole().getTreeCode());
//		hql.append(".%' and usr.role.type = 2 order by usr.user.name");
//		
//		return this.find(hql.toString(), null);
//	}
	
	public User findSimpleUserByName(String username) throws DAOException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", username);
		
		StringBuilder hql = new StringBuilder(
				"select new com.institucion.fm.wf.model.User(usr.id, usr.user.id, usr.user.name, usr.user.firstName, usr.user.lastName, usr.role) ")
			.append("from com.institucion.fm.wf.model.User as usr inner join usr.role ")
			.append("inner join usr.user AS modeluser where modeluser.name = :username");
		
		return super.findUnique(hql.toString(), params);		
	}
	
	public List<User> findAllSimpleUsers() throws DAOException {
		StringBuilder hql  = new StringBuilder("select new com.institucion.fm.wf.model.User(usr.id, usr.user.id , usr.user.name, usr.user.firstName, usr.user.lastName, usr.role) ")
		.append("from com.institucion.fm.wf.model.User as usr inner join usr.role ")
		.append(" where usr.role.type = 1 order by usr.user.name");
		
		return this.find(hql.toString(), null);
	}
	
	public List<User> findSimpleUsersBySupervisor(User supervisor) throws DAOException {
		loadLazyAttribute(supervisor.getRole());
		StringBuilder hql  = new StringBuilder("select new com.institucion.fm.wf.model.User(usr.id, usr.user.id , usr.user.name, usr.user.firstName, usr.user.lastName, usr.role) ")
		.append("from com.institucion.fm.wf.model.User as usr inner join usr.role inner join usr.user ")
		.append(" where usr.role.treeCode like '")
		.append(supervisor.getRole().getTreeCode())
		.append("%' and usr.role.type = 1 ")
		.append("and usr.user.state = ")
		.append(UserState.ACTIVE.toInt())
		.append("order by usr.user.name ");
		
		return this.find(hql.toString(), null);
	}
	
	public List<User> findUsersByRole(Role role) throws DAOException {
		StringBuilder hql = new StringBuilder("select new com.institucion.fm.wf.model.User(usr.id, usr.user.id, ")
			.append("usr.user.name, usr.user.firstName, usr.user.lastName, usr.role) ")
			.append("from com.institucion.fm.wf.model.User as usr inner join usr.role ")
			.append("where usr.role = ")
			.append(role.getId());
		
		return this.find(hql.toString(), null);
	}
	
	@Override
	public List<WsPermissionCObj> getPermissionsNames(String userName) {
		return (List<WsPermissionCObj>)getHibernateTemplate().findByNamedParam("select new com.institucion.fm.cobj.WsPermissionCObj(p.name,p.type) from com.institucion.fm.security.model.User u left join u.groups g left join g.permissions p where u.name=:name", new String[]{"name"}, new Object[]{userName});
	}
	
	public List<com.institucion.fm.security.model.User>getSupervisorDependants(String userName){
		String firstQuery="select wfu.role.parent.id,wfu.role.treeCode,wfu.id from com.institucion.fm.wf.model.User wfu left join wfu.user u where u.name=:name";
		List l=getHibernateTemplate().findByNamedParam(firstQuery, new String[]{"name"}, new Object[]{userName});
		if (l.size()==0){
			throw new LocalizeValidationException("error.nedd.to.be.wf.user");
		}
		Object[] supId=(Object[])l.get(0);
		
		
		/**
		 * Nota sobre el treecode
		 * El treecode identifica a usuarios por niveles
		 * 
		 * Ejemplo
		 *                     Treecode
		 * Sup1                 1
		 * |
		 * |__APM1				1.1
		 * |
		 * |__APM2				1.1
		 * |
		 * |Sup2				2		
		 * |
		 * |__APM3				2.1
		 * |
		 * |__APM4				2.1
		 * 
		 * En este caso se ve como dos usuarios distintos pueden tener el mismo treeCode. 
		 * En el ejemplo se ve como el treeCode 1.1 esta indicando que dos APM dependen del mismo supervisor.
		 * 
		 * Para la query de abajo notese que en el like se pone un punto al final. El objetivo de esto es que cuando se loguee un 
		 * APM no traiga a otros APMs en su mismo nivel
		 * 
		 * Ejemplo:
		 * Si no se puesiera un punto al final podriamos tener el siguiente caso:
		 * 
		 * select new com.institucion.fm.security.model.User(u.id,u.firstName,u.lastName) from com.institucion.fm.wf.model.User wfu left join wfu.user u where wfu.role.treeCode like '1.1%'
		 * Esto estara dando como resultado un listado con los APMs APM1 y APM2. 
		 * 
		 * A fin de evitar esto se pone el treecode con el punto, pero esto estara evitando que se cargue el usuario logueado 
		 * 
		 * select new com.institucion.fm.security.model.User(u.id,u.firstName,u.lastName) from com.institucion.fm.wf.model.User wfu left join wfu.user u where wfu.role.treeCode like '1.1.%'
		 * Notese que no hay ningun treeCode que comienze con 1.1.,para esto se agrega el id de usuario logueado, por lo que tanto si es un supervisor
		 * o un APM trae tambien al usuario logueado.
		 * 
		 * 
		 * 
		 */
		String query="select new com.institucion.fm.security.model.User(u.id,u.firstName,u.lastName) from com.institucion.fm.wf.model.User wfu left join wfu.user u where wfu.role.treeCode like '"+supId[1]+".%' or wfu.id="+supId[2];
//		String query1="select new com.institucion.fm.wf.model.User(wfu.id,wfu.user) from com.institucion.fm.wf.model.User wfu left join wfu.user u left join fetch u.firstName left join fetch u.lastName left join fetch u.id where wfu.role.treeCode like '"+supId[1]+"%'";
//		List<com.institucion.fm.wf.model.User>userWf=getHibernateTemplate().find(query1);
		List<com.institucion.fm.security.model.User>us=getHibernateTemplate().find(query);//findByNamedParam(query, new String[]{"roleIdParent","treeCode"}, new Object[]{supId[0],supId[1]});
		return us;

	}
	
	@Override
	public String getUserPassword(String name) throws Exception{
		String password=null;
		try{
			password=(String)getHibernateTemplate().findByNamedParam("select new java.lang.String(u.password) from com.institucion.fm.security.model.User u where u.name=:name", new String[]{"name"}, new Object[]{name}).get(0);
		}catch (Throwable t){
			log.error("Mensaje: " + t.getMessage() + "StackTrace: " + t.getStackTrace());
			throw new Exception("No se pudo obtener la contrasenia del usuario: "+ name);
		}
		return password;
	}
	
	public ValidateUserinRoleCObj getWfUserAsCObj(String name){
		String query1="select new com.institucion.fm.cobj.ValidateUserinRoleCObj(u.id,u.role.treeCode,u.role.parent.treeCode,u.role.parent.parent.id) from com.institucion.fm.wf.model.User u left join u.user usr where usr.name='"+name+"'";
		List res=getHibernateTemplate().find(query1);
		return res!=null && res.size()>0?(ValidateUserinRoleCObj)res.get(0):null;
	}
	
	@Override
	public WsValidationCObj validateUserAndPassword(String name) {
		
		
		List l=getHibernateTemplate().findByNamedParam("select new com.institucion.fm.cobj.WsValidationCObj(u.user.userSecurity.firstLogin,u.user.state,u.id,u.role.type) from com.institucion.fm.wf.model.User u where u.user.name=:name", new String[]{"name"}, new Object[]{name});
		if (l.size()==0)
			l=getHibernateTemplate().findByNamedParam("select new com.institucion.fm.cobj.WsValidationCObj(u.userSecurity.firstLogin,u.state,null,null) from com.institucion.fm.security.model.User u where u.name=:name", new String[]{"name"}, new Object[]{name});
		WsValidationCObj cobj=(WsValidationCObj)l.get(0);
		return cobj;
	}
	
	public Boolean validateUserInRole(String name,ApprovalType taskType){
		List userInRole= userInAdminRoles(name);
		if (userInRole !=null && userInRole.size()>0)
			return true;
		
//		String query1="select new com.institucion.fm.cobj.ValidateUserinRoleCObj(u.id,u.role.treeCode,u.role.parent.treeCode,u.role.parent.parent.id) from com.institucion.fm.wf.model.User u left join u.user usr where usr.name='"+name+"'";
		
		ValidateUserinRoleCObj cobj=getWfUserAsCObj(name);
		
		if (cobj ==null || cobj.getId()==null)
			throw new WorkFlowException("wf.notwfuser");
		
		if (cobj.getGrandFatherId()==null)
			return true;
		
		if (taskType == ApprovalType.SSFF && cobj.getFatherTreeCode()==null)
			throw new LocalizeValidationException("error.user.not.have.superior");
		
		return false;
	}
	
	private List userInAdminRoles(String name){
		
		String query="select distinct usr.id from  com.institucion.fm.security.model.User usr left join usr.groups g where ((g.role=:FMADMIN or g.role=:WFMANAGER or g.role=:SYSADMIN) and (usr.name='"+name+"'))";
//		String query="select distinct new com.institucion.fm.cobj.ValidateUserinRoleCObj(u.id,u.role.treeCode,u.role.parent.treeCode,u.role.parent.parent.id) from com.institucion.fm.wf.model.User u left join u.user usr left join usr.groups g where ((g.role=:FMADMIN or g.role=:WFMANAGER or g.role=:SYSADMIN) and (usr.name='"+name+"'))";
		return getHibernateTemplate().findByNamedParam(query,new String[]{"FMADMIN","WFMANAGER","SYSADMIN"},new Object[]{com.institucion.fm.security.model.RoleType.FMADMIN,com.institucion.fm.security.model.RoleType.WFMANAGER,com.institucion.fm.security.model.RoleType.SYSADMIN});
//		return getHibernateTemplate().find(query);
	}
	
	public com.institucion.fm.wf.model.User save(com.institucion.fm.wf.model.User userWf) throws DAOException{
		return super.save(userWf);
	}
	
	public com.institucion.fm.wf.model.User update(com.institucion.fm.wf.model.User userWf) throws DAOException {
		return super.update(userWf);
	}

	public void update(Connection cnx, com.institucion.fm.wf.model.User userWf) throws DAOException {
		String queryDistribution= null;
		Statement stat = null;
		try {
			stat = cnx.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	        queryDistribution= "update wf_user set pendiente_de_proceso = " + userWf.getPendienteDeProceso() + ", " +
	       
	        " where id = " + userWf.getId();
	        stat.executeUpdate(queryDistribution);
	        
		} catch (SQLException e) {
            e.printStackTrace();
		}catch (Exception e) {
            e.printStackTrace();
	    } finally {
            try {
                if (stat != null)
                    stat.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
	    }
	}

}