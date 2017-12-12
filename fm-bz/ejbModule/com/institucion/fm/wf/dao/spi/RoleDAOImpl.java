package com.institucion.fm.wf.dao.spi;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.institucion.fm.conf.dao.BaseActiveService;
import com.institucion.fm.conf.dao.SqlExecutor;
import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.wf.dao.RoleDAO;
import com.institucion.fm.wf.model.Role;


public class RoleDAOImpl extends BaseActiveService<Role> implements RoleDAO {

	private SqlExecutor executor;

	public SqlExecutor getExecutor() {
		return this.executor;
	}

	public void setExecutor(SqlExecutor executor) {
		this.executor = executor;
	}
	
	public List<Role> findAll() throws DAOException {
		//log.debug("findAll");
		//return this.findAll(Role.class);
		return this.find("from Role order by name",null);
	}

	public Role findById(Long id) throws DAOException {
		Role role = this.findById(id, Role.class);
		super.loadLazyAttribute(role.getWfusers());
		return role;
	}

	public Role findByName(String name) throws DAOException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);
		List<Role> list = this.find("from Role role where role.name = :name", params);
		if(list.size()>0) {
			Role role = list.get(0);
			super.loadLazyAttribute(role.getWfusers());
			return role;	
		}
		return null;	
	}
	@Override
	public Role save(Role c) throws DAOException {
		return super.save(c);
	}
	
	@Override
	public Role update(Role c) throws DAOException {
		return super.update(c);
	}

	public void update(Connection cnx, Role c) throws DAOException {
		String queryDistribution= null;
		Statement stat = null;
		try {
			stat = cnx.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	        queryDistribution= "update wf_role set description = '" + c.getDescription() + "'" +
	        " where id = " + c.getId();
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

	@Override
	public void delete(Role c) throws DAOException {
		super.delete(c);
	}
	
	@Override
	public ResultSet findRoles(Connection cnx) throws DAOException, SQLException {
		
		Statement stat = null;
		
		String query =  " SELECT r.type as type, " +
						" r.description as description, " +
						" r.id as id, " +
						" r.name as name, " +
						" r.treecode as treecode" +
						" FROM wf_role as r"+
						" ORDER BY treecode";
		
		stat = cnx.createStatement();
		
		return stat.executeQuery(query);
	}
	
//	public List<RoleModel> getRoleModel(Connection cnx){
//		Statement stat = null;
//		Statement statWFUser = null;
//		ResultSet rsRoles = null;
//		try {
//			statWFUser = cnx.createStatement();
//			String query =  " SELECT r.type as type, " +
//							" r.description as description, " +
//							" r.id as id, " +
//							" r.name as name, " +
//							" r.treecode as treecode" +
//							" FROM wf_role as r"+
//							" ORDER BY treecode";
//			stat = cnx.createStatement();
//			rsRoles =stat.executeQuery(query);
//			List<RoleModel> listRoles = new ArrayList<RoleModel>();
//				
//			while(rsRoles.next()){
//				RoleModel roleModel = new RoleModel();
//				if(rsRoles.getObject("id")!=null)
//					roleModel.setId(rsRoles.getLong("id"));
//				if(rsRoles.getObject("description")!=null)
//					roleModel.setDescription(rsRoles.getString("description"));
//				if(rsRoles.getObject("treecode")!=null)
//					roleModel.setTreeCode(rsRoles.getString("treecode"));
//				if(rsRoles.getObject("name")!=null)
//					roleModel.setName(rsRoles.getString("name"));
//				if(rsRoles.getObject("type")!=null)
//					roleModel.setRoleType((RoleType.fromInt(rsRoles.getInt("type"))));
//				
//				String queryWFUser = " SELECT u.id as userid, " 
//						 + " wf.id as wfuserid, "
//						 + " u.name as wfname, " 
//						 + " u.firstname as firstname, "
//						 + " u.lastname	as lastname," 
//						 + "  wf.type as type "  
//						 + " FROM wf_user as wf "
//						 + " inner join users u on(u.id=wf.userid) "
//						 + " WHERE wf.roleid =" + roleModel.getId()
//						 + " and wf.type <>"+ SalesForceStateType.PENDINGNODRAW.toInt();
//
//				ResultSet rsWFUser = statWFUser.executeQuery(queryWFUser);
//				List <WFUserWSModel> listWFUser = new ArrayList<WFUserWSModel>();
//				int i = 0;
//				while(rsWFUser.next()){
//					WFUserWSModel wfUser = new WFUserWSModel();
//					if (rsWFUser.getInt("type")==SalesForceStateType.NORMAL.toInt() || rsWFUser.getInt("type")==SalesForceStateType.ALERTDRAW.toInt()){
//						if(rsWFUser.getObject("firstname")!=null)
//							wfUser.setFirstName(rsWFUser.getString("firstname"));
//						if(rsWFUser.getString("lastname")!=null)
//							wfUser.setLastName(rsWFUser.getString("lastname"));
//						if(rsWFUser.getString("wfname")!=null)
//							wfUser.setName(rsWFUser.getString("wfname"));
//						if(rsWFUser.getString("userid")!=null)
//							wfUser.setId_user(rsWFUser.getLong("userid"));
//						if(rsWFUser.getString("wfuserid")!=null)
//							wfUser.setId(rsWFUser.getLong("wfuserid"));
//						listWFUser.add(wfUser);
//					} else if (rsWFUser.getInt("type")==SalesForceStateType.VACANCYDRAW.toInt()){
////						wfUser.setFirstName(rsWFUser.getString("firstname"));
////						wfUser.setLastName(rsWFUser.getString("lastname"));
//						wfUser.setName("vacancy");
////						wfUser.setId_user(rsWFUser.getLong("userid"));
//						wfUser.setId(rsWFUser.getLong("wfuserid"));
//						listWFUser.add(wfUser);
//					}
//				}
//				rsWFUser.close();
//				WFUserWSModel[] wfUserRole = new WFUserWSModel[listWFUser.size()]; 
//				for (WFUserWSModel wfUserModel : listWFUser) {
//					wfUserRole[i] = wfUserModel;
//					i++;
//				}
//				roleModel.setWfusers(wfUserRole);
//				listRoles.add(roleModel);
//			}
//			return listRoles;
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				if (rsRoles !=null)
//					rsRoles.close();
//				if (stat !=null)
//					stat.close();
//				if (statWFUser!=null)
//					statWFUser.close();
//			}catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		return null;
//	}

	@Override
	public ResultSet findWFUser(Connection cnx, Long roleId)  {
		
		Statement stat = null;
		try {
			String query = " SELECT u.id as userid, " 
						 + " wf.id as wfuserid, "
						 + " u.name as wfname, " 
						 + " u.firstname as firstname, "
						 + " u.lastname	as lastname " 
						 + " FROM wf_user as wf "
						 + " inner join users u on(u.id=wf.userid) "
						 + " WHERE wf.roleid = " + roleId;
	
			
				stat = cnx.createStatement();
				return stat.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try{
				if (stat!=null)
					stat.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
		
	}
	
	@Override
	public Role findRoot() throws DAOException {
		List<Role> list = this.find("from Role role where role.parent is null",null);
		if(list.size()>0){
			Role role = list.get(0);
//			super.loadLazyAttribute(role.getWfusers());
			return role;	
		}
		throw new DAOException("Root of Role not found");	
	}
}
