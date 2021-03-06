package com.untzuntz.ustackserverapi.auth;

import java.util.List;

import org.apache.log4j.Logger;
import org.jboss.netty.handler.codec.http.HttpRequest;

import com.untzuntz.ustack.data.APIMapping;
import com.untzuntz.ustack.data.UserAccount;
import com.untzuntz.ustackserverapi.APIException;
import com.untzuntz.ustackserverapi.CallParameters;
import com.untzuntz.ustackserverapi.MethodDefinition;
import com.untzuntz.ustackserverapi.params.ParamNames;
import com.untzuntz.ustackserverapi.params.types.ParameterDefinitionInt;

/**
 * Validates an API client ID and it's key via Basic HTTP auth as well as a user Id and secret via parameters
 * 
 * 
 * @author jdanner
 *
 */
public class APIClientKeyTokenSecretAuth implements AuthenticationInt<UserAccount> {

    static Logger           		logger               	= Logger.getLogger(APIClientKeyAuth.class);
 
    public String getAuthenticationDescription() {
    	return "Your client ID/API Key via HTTP Basic Authentication and a user identifier and secret (in request parameters) required.";
    }
    
    public boolean isBasicAuth() {
    	return true;
    }

    /**
     * 
     */
    public List<ParameterDefinitionInt<?>> getAuthenticationParameters() {
    	
    	List<ParameterDefinitionInt<?>> ret = AuthTypes.ClientKey.getAuthenticationParameters();
    	
    	ret.add(ParamNames.user_identifier);
    	ret.add(ParamNames.secret);
    	
    	return ret;
    }
    
	@Override
	public UserAccount authenticate(MethodDefinition method, HttpRequest req, CallParameters params) throws APIException {

		// Do the client id and api key auth
		AuthTypes.ClientKey.authenticate(method, req, params);
	
		if (params.get(ParamNames.user_identifier) == null)
			throw new APIAuthorizationException("Missing User Idenfitier Value");
		
		// get user info
		UserAccount user = UserAccount.getByAPIToken(params.get(ParamNames.client_id), params.get(ParamNames.user_identifier));
		if (user == null)
			throw new APIAuthorizationException("Invalid User Idenfitier Value");
		
		// validate user API secret against provided
		APIMapping api = user.getAPIMapping(params.get(ParamNames.client_id));
		if (!api.checkAPIKey( params.get(ParamNames.secret) ))
			throw new APIAuthenticationException("Bad Token/Secret");

		return user;
	}
	

}
