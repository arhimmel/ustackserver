package com.untzuntz.ustackserverapi.auth;

import com.untzuntz.ustack.aaa.Authentication;
import com.untzuntz.ustack.aaa.Authorization;
import com.untzuntz.ustack.data.UserAccount;
import com.untzuntz.ustack.exceptions.AuthenticationException;
import com.untzuntz.ustack.exceptions.AuthorizationException;
import com.untzuntz.ustackserverapi.APIException;
import com.untzuntz.ustackserverapi.CallParameters;
import com.untzuntz.ustackserverapi.MethodDefinition;
import com.untzuntz.ustackserverapi.params.ParamNames;

public class UserNamePasswordAuth implements AuthenticationInt<UserAccount> {

	@Override
	public UserAccount authenticationAuthorization(MethodDefinition method, CallParameters params) throws APIException {
		
		UserAccount user = null;
		try {
			user = Authentication.authenticateUser(params.get(ParamNames.username), params.get(ParamNames.password));
		} catch (AuthenticationException e) {
			throw new APIAuthenticationException("Bad Username/Password");
		}
		
		if (method.isAuthorizationRequired())
		{
			try {
				Authorization.authorizeUser(user, "*", null, method.getAuthGroup());
			} catch (AuthorizationException e) {
				throw new APIAuthorizationException("Not Authorized");
			}
		}
		
		return user;
	}
	
}
