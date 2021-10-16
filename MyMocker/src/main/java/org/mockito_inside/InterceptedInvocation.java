package org.mockito_inside;

import java.lang.reflect.Method;


public interface InterceptedInvocation {

	Object getMock();
	Method getMethod();
	Object[] getArgs();
}
