package org.mockito_inside;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mockito_inside.argument_mathchers.ArgumentMatcher;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.List;


@NoArgsConstructor( access = AccessLevel.PRIVATE )
public class MyMocker {

	@SuppressWarnings( "unchecked" )
	public static <M> M mock( Class<M> interfaceToMock ) {
		InvocationHandler invocationHandler = new MockInvocationHandler();
		return (M) Proxy.newProxyInstance( Thread.currentThread().getContextClassLoader(),
		                                   new Class[]{ interfaceToMock },
		                                   invocationHandler );
	}

	public static <R> Stubber<R> when( @SuppressWarnings( "unused" ) R unused ) {
		StubbedInvocation invocation = MockingContext.get().pullInvocationForProcess();
		List<ArgumentMatcher> argumentMatchers = MockingContext.get().popAllArgumentMatchers();
		StubbingRegistry stubbingRegistry = invocation.getStubbingRegistry();
		InvocationStub<R> invocationStub = stubbingRegistry.createStubFor( invocation, argumentMatchers );
		return new StubberIml<>( invocationStub );
	}
}
