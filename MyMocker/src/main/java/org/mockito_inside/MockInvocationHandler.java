package org.mockito_inside;

import org.eclipse.jdt.annotation.Nullable;
import org.mockito_inside.reflection.TypeUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


public class MockInvocationHandler implements InvocationHandler {

	@Override
	@Nullable
	public Object invoke( Object proxy, Method method, Object[] args ) {
		return findReturnValue( method );
	}

	@Nullable
	private Object findReturnValue( Method method ) {
		return TypeUtils.getDefaultValue( method.getReturnType() );
	}
}
