package org.mockito_inside;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;


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
		Invocation invocation = MockingContext.get().pullInvocationForProcess();
		StubbingRegistry stubbingRegistry = invocation.getStubbingRegistry();
		InvocationStub<R> invocationStub = stubbingRegistry.createStubFor( invocation );
		return new StubberIml<>( invocationStub );
	}
}
