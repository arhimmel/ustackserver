package com.untzuntz.ustackserverapi.params.types;

import com.untzuntz.ustackserverapi.APIException;
import com.untzuntz.ustackserverapi.params.exceptions.ParamValueException;

/**
 * Validates a boolean type ('true' or 'false')
 */
public class BooleanParam extends BaseParam implements ParameterDefinitionInt<Boolean>
{
	public BooleanParam(String n, String d) {
		super(n, d);
	}

	@Override
	public void validate(String data) throws APIException {
		
		if ("true".equalsIgnoreCase(data))
			return;
		if ("false".equalsIgnoreCase(data))
			return;

		throw new ParamValueException(this, "Boolean value must be 'true' or 'false'");
		
	}
	
	@Override
	public Boolean getValue(String data) {
		
		if ("true".equalsIgnoreCase(data))
			return true;

		return false;
	}
}