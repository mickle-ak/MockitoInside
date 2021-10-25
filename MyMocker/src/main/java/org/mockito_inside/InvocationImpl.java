package org.mockito_inside;

import lombok.Value;

import java.lang.reflect.Method;


@Value
public class InvocationImpl implements StubbedInvocation {

	Object           mock;
	Method           method;
	Object[]         args;
	StubbingRegistry stubbingRegistry;
}
