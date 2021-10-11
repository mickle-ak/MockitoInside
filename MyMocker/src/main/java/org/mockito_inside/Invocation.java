package org.mockito_inside;

import java.lang.reflect.Method;


public interface Invocation {

	Object getMock();
	Method getMethod();
	Object[] getArgs();
	StubbingRegistry getStubbingRegistry();
}
