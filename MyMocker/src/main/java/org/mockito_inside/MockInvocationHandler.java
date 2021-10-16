package org.mockito_inside;

import org.eclipse.jdt.annotation.Nullable;
import org.mockito_inside.reflection.TypeUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


public class MockInvocationHandler implements InvocationHandler {

	private final StubbingRegistry stubbingRegistry = new StubbingRegistryImpl();

	@Nullable
	@Override
	public Object invoke( Object mock, Method method, Object[] args ) throws Throwable {

		Invocation invocation = new InvocationImpl( mock, method, args, stubbingRegistry );
		MockingContext.get().pushInvocationForProcess( invocation );

		return findReturnValue( invocation );
	}

	@Nullable
	private Object findReturnValue( Invocation invocation ) throws Throwable {
		InvocationStub<?> invocationStub = stubbingRegistry.findStubFor( invocation );
		return invocationStub != null
		       ? invocationStub.getAnswer().answer( invocation )
		       : TypeUtils.getDefaultValue( invocation.getMethod().getReturnType() );
	}
}
