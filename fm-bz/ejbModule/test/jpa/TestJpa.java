package test.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.apache.log4j.Logger;

public class TestJpa {

	/**
	 * @param args
	 */
	private static Logger log;
	public static void main(String[] args) {
	
		a();

	}
	
	private static void a(){
		log=Logger.getLogger(test.jpa.TestJpa.class);
		log.info("Entrando");
		EntityManagerFactory emf=Persistence.createEntityManagerFactory("pu");
		EntityManager em=emf.createEntityManager();
		em.getTransaction().begin();
//		Query q=em.createQuery("select distinct p from com.institucion.fm.security.model.User p left join fetch p.promotionZones left join fetch p.promotionZones left join fetch p.groups left join fetch p.userSecurity");
		
		StringBuilder hql = new StringBuilder(
		"select new com.institucion.fm.wf.model.User(usr.id, usr.user.id, usr.user.name, usr.user.firstName, usr.user.lastName, usr.role) ")
	.append("from com.institucion.fm.wf.model.User as usr inner join usr.role left join usr.role.children ")
	.append("inner join usr.user AS modeluser where modeluser.name = :username");
		
		Query q=em.createQuery(hql.toString());
		q.setParameter("username", "cmansilla");
		q.getResultList();
		
		
		StringBuilder sb=new StringBuilder();
			sb.append("select new com.institucion.fm.wf.model.User(usr.id, usr.user.id, usr.user.name, usr.user.firstName, usr.user.lastName, usr.role) ");
		sb.append("from com.institucion.fm.wf.model.User as usr inner join usr.role left join fetch usr.role.children ");
		sb.append("inner join usr.user AS modeluser where modeluser.name = 'cmansilla'");
		
		String roleQuery="select usr.role from com.institucion.fm.wf.model.User as usr left join fetch usr.role.children inner join usr.user AS modeluser where modeluser.name =:name";
		
		
		Query query=em.createQuery(roleQuery);
		query.setParameter("name", "cmansilla");
		query.getResultList();
		em.getTransaction().commit();
		em.close();
		emf.close();
		
		
	}

}
