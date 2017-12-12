package com.institucion.fm.security.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import com.institucion.fm.confsys.model.ConfigRegional;
import com.institucion.model.SucursalEnum;

public class User implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public static final String USER_FIRSTNAME = "firstname";
	public static final String USER_LASTNAME = "lastname";
	public static final String ID = "id";
	private Long id;
	private String name;
	private String lastName;
	private String firstName;
	private String password;
	private String telephone;
	private String cellphone;
	private String address;
	private UserState state;
	private ConfigRegional configRegional;
	private String email;
	private Set<Group> groups = new LinkedHashSet<Group>();
	private UserSecurity userSecurity;
	private String code;
	private boolean userWF;
	private boolean validatePass;
	private int sucursal;

	public User(Long id, String name, String lastName, String firstName,
			String password, UserState state,boolean validatePass,ConfigRegional configRegional) {
		super();
		this.id = id;
		this.name = name;
		this.lastName = lastName;
		this.firstName = firstName;
		this.password = password;
		this.state = state;
		this.validatePass = validatePass;
		this.configRegional=configRegional;
	}


	public User(Long id, String name, String lastName, String firstName,
			String password, UserState state,boolean validatePass,ConfigRegional configRegional, int sucursal) {
		super();
		this.id = id;
		this.name = name;
		this.lastName = lastName;
		this.firstName = firstName;
		this.password = password;
		this.state = state;
		this.validatePass = validatePass;
		this.configRegional=configRegional;
		this.sucursal=sucursal;
	}

	public User() {}


	
	
	public User(Long id,String name){
		this.id=id;
		this.name=name;
	}
	
	public User(String name){
		setName(name);
		setState(UserState.ACTIVE);
	}
	
	public User(Long id, String firstName, String lastName) {
		setId(id);
		setFirstName(firstName);
		setLastName(lastName);
	}

	public Long getId() { return id; }
	@SuppressWarnings("unused")
	private void setId(Long id) { this.id = id; }

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		if(telephone == null){
			this.telephone = null;
		}
		else{
			this.telephone = telephone.trim();
		}
	}
		
	
	public String getCellphone() {
		return this.cellphone;
	}

	public void setCellphone(String cellphone) {
		if(cellphone == null){
			this.cellphone = null;
		}
		else{
			this.cellphone = cellphone.trim();
		}
	}

	public UserState getState() {
		return this.state;
	}

	public void setState(UserState state) {
		this.state = state;
	}

	public String getName() { return name; }
	public void setName(String name) {
		if (name == null) {
			this.name = null;
		} else {
			this.name = name.trim();
		}
	}

	public String getPassword() { return password; }
	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() { return state == UserState.ACTIVE; }

	
	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		if(lastName == null){
			this.lastName = null;
		}
		else{
			this.lastName = lastName.trim();
		}
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		if(firstName == null){
			this.firstName = null;
		}
		else{
			this.firstName = firstName.trim();
		}
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		if(email == null){
			this.email = null;
		}
		else{
			this.email = email.trim();
		}
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		if(address == null){
			this.address = null;
		}
		else{
			this.address = address.trim();
		}
	}
	
	public Set<Group> getGroups() { return groups; }
	public void setGroups(Set<Group> groups) { this.groups = groups; }

//	public Set<PromotionZone> getPromotionZones() { return promotionZones; }
//	public void setPromotionZones(Set<PromotionZone> promotionZones) { this.promotionZones = promotionZones; }
	
	/**
	 * Retornar los permisos que tiene el usuario considerando a todos los grupos.
	 * Si un usuario tiene mas de un grupo, entonces los permisos se suman. 
	 */
	public Set<Permission> getPermissions()
	{
		Set<Permission> userPermissions = new HashSet<Permission>();

		Iterator<Group> itgroup = getGroups().iterator();
		while (itgroup.hasNext()) {
			Group group = itgroup.next();
			for (Permission permission : group.getPermissions()) {
				userPermissions.add(permission);
			}
		}

		return userPermissions;
	}
	
	public Set<Permission> getPermissionsByType(PermissionType type) {
		Set<Permission> userPermissions = new HashSet<Permission>();

		Iterator<Group> itgroup = getGroups().iterator();
		while (itgroup.hasNext()) {
			Group group = itgroup.next();
			for (Permission permission : group.getPermissions()) {
				if (permission.getType() == type) {
					userPermissions.add(permission);
				}
			}
		}
		return userPermissions;
	}


	public void setConfigRegional(ConfigRegional configRegional) {
		this.configRegional = configRegional;
	}

	public ConfigRegional getConfigRegional() {
		return configRegional;
	}
	
	public String getFullName() {
		StringBuffer buf = new StringBuffer();
		if (this.firstName != null) {
			buf.append(this.firstName);
		}
		if (this.lastName != null) {
			buf.append(" ").append(this.lastName);
		}
		return buf.toString();
	}
	
	public boolean containsRole(RoleType role) {
		for (Group group: this.getGroups()) {
			if (role == group.getRole()) {
				return true;
			}
		}
		return false;
	}
	

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

	public void setUserSecurity(UserSecurity userSecurity)
	{
		this.userSecurity = userSecurity;
	}

	public UserSecurity getUserSecurity()
	{
		return userSecurity;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public boolean isUserWF() {
		return userWF;
	}

	public void setUserWF(boolean userWF) {
		this.userWF = userWF;
	}


	public boolean isValidatePass() {
		return validatePass;
	}

	public int getSucursal() {
		return sucursal;
	}

	public SucursalEnum getsucursalEnum(){
		return SucursalEnum.fromInt(sucursal);
	}


	public void setSucursal(int sucursal) {
		this.sucursal = sucursal;
	}




	public void setValidatePass(boolean validatePass) {
		this.validatePass = validatePass;
	}

}