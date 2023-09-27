package org.mockito_inside;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import org.eclipse.jdt.annotation.Nullable;
import org.mockito_inside.reflection.TypeUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


public class MockInvocationHandler implements InvocationHandler {

	private final StubbingRegistry stubbingRegistry = new StubbingRegistryImpl();

	@Nullable
	@RuntimeType
	@Override
	public Object invoke(@This Object mock,
						 @Origin Method method,
						 @AllArguments Object[] args ) throws Throwable {

		StubbedInvocation invocation = new InvocationImpl( mock, method, args, stubbingRegistry );
		MockingContext.get().pushInvocationForProcess( invocation );

		return findReturnValue( invocation );
	}

	@Nullable
	private Object findReturnValue( Invocation invocation ) throws Throwable {
		InvocationStub<?> invocationStub = stubbingRegistry.findStubFor( invocation );
		return invocationStub != null
		       ? invocationStub.getNextAnswer().answer( invocation )
		       : TypeUtils.getDefaultValue( invocation.getMethod().getReturnType() );
	}
}
