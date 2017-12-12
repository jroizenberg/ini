package com.institucion.fm.cobj;

import java.io.Serializable;

import com.institucion.fm.wf.model.RoleType;

/**
 * The Class UserCobj.
 */
public class UserClObj implements Serializable, Identificable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	private Long id;

	/** The name. */
	private String name;

	/** The last name. */
	private String lastName;

	/** The role type. */
	private RoleType roleType;

	/** The role tree code. */
	private String roleTreeCode;

	/**
	 * Gets the serialversionuid.
	 * 
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * Instantiates a new user cobj.
	 * 
	 * @param id
	 *            the id
	 * @param name
	 *            the name
	 * @param lastName
	 *            the last name
	 * @param roleType
	 *            the role type
	 * @param treeCode
	 *            the tree code
	 */
	public UserClObj(Long id, String name, String lastName, RoleType roleType,
			String roleTreeCode) {
		super();
		this.id = id;
		this.name = name;
		this.lastName = lastName;
		this.roleType = roleType;
		this.roleTreeCode = roleTreeCode;
	}

	/**
	 * Instantiates a new user cobj.
	 * 
	 * @param id
	 *            the id
	 * @param name
	 *            the name
	 * @param lastName
	 *            the last name
	 */
	public UserClObj(Long id, String name, String lastName) {
		super();
		this.id = id;
		this.name = name;
		this.lastName = lastName;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the last name.
	 * 
	 * @return the last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the last name.
	 * 
	 * @param lastName
	 *            the new last name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Gets the role type.
	 * 
	 * @return the role type
	 */
	public RoleType getRoleType() {
		return roleType;
	}

	/**
	 * Sets the role type.
	 * 
	 * @param roleType
	 *            the new role type
	 */
	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}

	/**
	 * Gets the role tree code.
	 * 
	 * @return the role tree code
	 */
	public String getRoleTreeCode() {
		return roleTreeCode;
	}

	/**
	 * Sets the role tree code.
	 * 
	 * @param treeCode
	 *            the new role tree code
	 */
	public void setRoleTreeCode(String treeCode) {
		this.roleTreeCode = treeCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name + " " + lastName;
	}

}