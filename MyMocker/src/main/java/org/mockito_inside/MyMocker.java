package org.mockito_inside;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.eclipse.jdt.annotation.NonNull;
import org.mockito_inside.argument_mathchers.ArgumentMatcher;
import org.objenesis.ObjenesisStd;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.List;


@NoArgsConstructor( access = AccessLevel.PRIVATE )
public class MyMocker {

	@NonNull
	public static <M> M mock( Class<M> interfaceToMock ) {
		//return mockWithJavaProxy(interfaceToMock);
		return mockWithByteBuddy(interfaceToMock);
	}

	@SuppressWarnings({"unchecked", "unused"})
	static <M> M mockWithJavaProxy(Class<M> interfaceToMock) {
		InvocationHandler invocationHandler = new MockInvocationHandler();
		return (M) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
										  new Class[]{interfaceToMock},
										  invocationHandler);
	}

	@SuppressWarnings("unused")
	static <M> M mockWithByteBuddy(Class<M> classToMock) {
		InvocationHandler invocationHandler = new MockInvocationHandler();
		Class<? extends M> mockType = new ByteBuddy()
			.subclass(classToMock)
			.method(ElementMatchers.any()).intercept(MethodDelegation.to(invocationHandler))
			.make()
			.load(classToMock.getClassLoader())
			.getLoaded();
		return new ObjenesisStd().newInstance(mockType);
	}

	@NonNull
	public static <R> Stubber<R> when( @SuppressWarnings( "unused" ) R unused ) {
		StubbedInvocation invocation = MockingContext.get().pullInvocationForProcess();
		List<ArgumentMatcher> argumentMatchers = MockingContext.get().pullAllArgumentMatchers();
		StubbingRegistry stubbingRegistry = invocation.getStubbingRegistry();
		InvocationStub<R> invocationStub = stubbingRegistry.createStubFor( invocation, argumentMatchers );
		return new StubberIml<>( invocationStub );
	}
}
