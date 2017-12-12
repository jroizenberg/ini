package com.institucion.fm.wf.model;

import java.io.Serializable;

import com.institucion.fm.cobj.Identificable;

public class User implements Serializable, Identificable {
	private static final long serialVersionUID = 1L;
	public static String ID = "id";
	
	private Long id;
	private Role role;
	private Boolean pendienteDeProceso;
	private com.institucion.fm.security.model.User user;
	
//	private Set<PromotionLine> promotionLines = new LinkedHashSet<PromotionLine>();
//	private SalesForceStateType type;
	
	public User(Long id,com.institucion.fm.security.model.User user){
		this.id=id;
		this.user=user;
	}
	
	/**
	 * Horrible constructor para optimizar las queries.
	 * @param id
	 * @param userId
	 * @param userName
	 * @param userFirstName
	 * @param userLastName
	 */
	public User(Long id, Long userId, String userName, String userFirstName, String userLastName) {
		this.id = id;
		
		com.institucion.fm.security.model.User user = new com.institucion.fm.security.model.User(
				userId, userFirstName, userLastName);
		user.setName(userName);
		this.setUser(user);
	}
	
	/**
	 * Horrible constructor para optimizar las queries.
	 */
	public User(Long id, Long userId, String userName, String userFirstName, String userLastName,
			Role role) {
		this.id = id;
		
		com.institucion.fm.security.model.User user = new com.institucion.fm.security.model.User(
				userId, userFirstName, userLastName);
		user.setName(userName);
		this.setUser(user);
		
		this.role = role;
	}
	
	public User(Long id){
		this.id=id;
	}
	
	public User() {
	}

//	public SalesForceStateType getType() {
//		return type;
//	}
//
//	public void setType(SalesForceStateType type) {
//		this.type = type;
//	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getPendienteDeProceso() {
		return pendienteDeProceso;
	}

	public void setPendienteDeProceso(Boolean pendienteDeProceso) {
		this.pendienteDeProceso = pendienteDeProceso;
	}
//
//	public Set<Task> getTasks() {
//		return tasks;
//	}
//
//	public void setTasks(Set<Task> tasks) {
//		this.tasks = tasks;
//	}
//
//	public Set<Task> getPendingTasks() {
//		return pendingTasks;
//	}
//
//	public void setPendingTasks(Set<Task> pendingTasks) {
//		this.pendingTasks = pendingTasks;
//	}
	public com.institucion.fm.security.model.User getUser() {
		return user;
	}

	public void setUser(com.institucion.fm.security.model.User user) {
		this.user = user;
	}
//	public void setPromotionLines(Set<PromotionLine> promotionLines) {
//		this.promotionLines = promotionLines;
//	}
//
//	public Set<PromotionLine> getPromotionLines() {
//		return promotionLines;
//	}

	public int hashCode() {
		if (this.id == null) {
			return System.identityHashCode(this);
		}
		return this.id.hashCode();
	}
	
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof User)) {
			return false;
		}
		return obj.hashCode() == this.hashCode();
	}

}
