package com.institucion.fm.security.service;

import javax.servlet.http.HttpServletRequest;

import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationException;
import org.acegisecurity.BadCredentialsException;
import org.acegisecurity.DisabledException;
import org.acegisecurity.LockedException;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.acegisecurity.ui.AbstractProcessingFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.institucion.fm.confsys.bz.ConfigSystemEJB;
import com.institucion.fm.confsys.model.Flag;
import com.institucion.fm.lang.ExtDate;
import com.institucion.fm.security.bz.SecurityAAEJB;
import com.institucion.fm.security.model.User;
import com.institucion.fm.security.model.UserState;
import com.institucion.fm.util.Cipher;

public class AuthenticationFilter extends AbstractProcessingFilter
{
	private static Log log = LogFactory.getLog(AuthenticationFilter.class);
	
	public static final String USERNAME_KEY = "username";
	public static final String PASSWORD_KEY = "password";

	private SecurityAAEJB securityAAService;
	private ConfigSystemEJB configSystemService;
	private AuthorizationService authorizationService;
	private int denyAccessLimit;

	public SecurityAAEJB getSecurityAAService() { return securityAAService; }
	public void setSecurityAAService(SecurityAAEJB securityAAService) { this.securityAAService = securityAAService; }

	public ConfigSystemEJB getConfigSystemService() { return configSystemService; }
	public void setConfigSystemService(ConfigSystemEJB configSystemService) { this.configSystemService = configSystemService; }

	public void setAuthorizationService(AuthorizationService authorizationService) { this.authorizationService = authorizationService; }
	public AuthorizationService getAuthorizationService() { return authorizationService; }

	public void setDenyAccessLimit(int denyAccessLimit) { this.denyAccessLimit = denyAccessLimit; }
	public int getDenyAccessLimit() { return denyAccessLimit; }

	public Authentication attemptAuthentication(HttpServletRequest request) throws AuthenticationException
	{
		String username = request.getParameter(USERNAME_KEY);
		String password = request.getParameter(PASSWORD_KEY);

		
		if (username == null)
			username = "";

		if (password == null)
			password = "";

		username = username.trim();

		
		User user = securityAAService.getUserWithOutCollections(username);//securityAAService.getUser(username);
		
		if (user == null)
		{
			setAuthenticationFailureUrl(getFailureUrl("erroruser"));
			throw new BadCredentialsException("User not found");
		}
		//String passString=Cipher.decrypt(user.getPassword());
		if (user.getState() == UserState.INACTIVE)
		{
			setAuthenticationFailureUrl(getFailureUrl("errorinactiveuser"));
			throw new BadCredentialsException("Inactive user");
		}

		if (user.getState() == UserState.BLOCKED)
		{
			setAuthenticationFailureUrl(getFailureUrl("errorblockeduser"));
			throw new BadCredentialsException("Blocked user");
		}
		refreshUsers();

		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, Cipher.encrypt(password));

		// Allow subclasses to set the "details" property
		setDetails(request, authRequest);
		Authentication authentication = null;
		try{
			authentication = this.getAuthenticationManager().authenticate(authRequest);
		}catch (AuthenticationException e){
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			if (e instanceof DisabledException){
				setAuthenticationFailureUrl(getFailureUrl("errordisableduser"));
			}else{
				setAuthenticationFailureUrl(getFailureUrl("errorpassword"));
				
				if(user.isValidatePass()){
					securityAAService.handleWrongPassword(user, denyAccessLimit);
				}
			}
			throw e;
		}

		if (authentication.isAuthenticated()){
			request.getSession(true).setAttribute("username", username);
			request.getSession(true).setAttribute("usernameID", user.getId());
			
			request.getSession(true).setAttribute("sucursalDelLoguin", user.getsucursalEnum());
			
			ExtDate.setUserName(username);
			request.getSession(true).setAttribute("ConfigRegional", user.getConfigRegional());
			setAuthenticationFailureUrl(getFailureUrl("errorlogin"));
			configSystemService.initServerConfigRegional();
			securityAAService.setAccesCountToZero(user);
			/**
			 * if the user is new or change the password, is force to change the password at the first login
			 * after he complete the  operation
			 * */
			if(user.isValidatePass()){
					Boolean firstLogin=securityAAService.isFirstLogin(user);
					if (firstLogin!=null && firstLogin){
					
					/**
					 * to lunch the zul page, is required to set the zul page as autenticationfailesrurl and then throw a
					 * AuthenticationException subClass
					 * */
					setAuthenticationFailureUrl("/security/force-change-password.zul");
					throw new LockedException("force-change-password.zul");
				}
			}
		}

		return authentication;
	}

	private String getFailureUrl(String error)
	{
		return "/login.zul?error="+error;
	}

   protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest)
   {
       authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
   }
  
   /** This filter by default responds to <code>/security_check</code>. */
   public String getDefaultFilterProcessesUrl() { return "/security_check"; }

	//public void init(FilterConfig filterConfig) throws ServletException { }

	private void refreshUsers()
	{
		Flag flag = configSystemService.getFlag();
		if (flag.isReloadPermission())
		{
			authorizationService.refresh();
			flag.setReloadPermission(false);
			configSystemService.setFlag(flag);
		}
	}
}