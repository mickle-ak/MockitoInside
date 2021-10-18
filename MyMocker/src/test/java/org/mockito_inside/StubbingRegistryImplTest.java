package org.mockito_inside;

import org.junit.jupiter.api.Test;
import org.mockito_inside_prod.VeryDifficultToCreateService;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito_inside.MyMocker.mock;


class StubbingRegistryImplTest {

	private final StubbingRegistry registry = new StubbingRegistryImpl();

	@Test
	void findStub_founds_stub_per_method_WithoutParameters() throws NoSuchMethodException {
		InvocationStub<?> stub_getObject = registry.createStubFor( createInvocation( "getObject" ) );
		InvocationStub<?> stub_getInt = registry.createStubFor( createInvocation( "getInt" ) );

		assertThat( registry.findStubFor( createInvocation( "getObject" ) ) ).isSameAs( stub_getObject );
		assertThat( registry.findStubFor( createInvocation( "getInt" ) ) ).isSameAs( stub_getInt );
		assertThat( registry.findStubFor( createInvocation( "getList" ) ) ).isNull();
	}

	@Test
	void findStub_returns_last_stub_for_the_same_method() throws NoSuchMethodException {
		InvocationStub<?> stub_getObject_1 = registry.createStubFor( createInvocation( "getObject" ) );
		InvocationStub<?> stub_getObject_2 = registry.createStubFor( createInvocation( "getObject" ) );

		assertThat( registry.findStubFor( createInvocation( "getObject" ) ) ).isSameAs( stub_getObject_2 );
	}

	@Test
	void findStub_return_stub_for_arguments() throws NoSuchMethodException {
		InvocationStub<?> stubA = registry.createStubFor( createInvocation( "getObject", "a" ) );
		InvocationStub<?> stubB = registry.createStubFor( createInvocation( "getObject", "b" ) );

		assertThat( registry.findStubFor( createInvocation( "getObject", "a" ) ) ).isSameAs( stubA );
		assertThat( registry.findStubFor( createInvocation( "getObject", "b" ) ) ).isSameAs( stubB );
		assertThat( registry.findStubFor( createInvocation( "getObject", "c" ) ) ).isNull(); // not found
	}


	private Invocation createInvocation( String methodName, Object... arguments ) throws NoSuchMethodException {
		Method method = findMethod( methodName, arguments );
		return new InvocationImpl( mock( VeryDifficultToCreateService.class ), method, arguments, registry );
	}

	private Method findMethod( String methodName, Object... arguments ) throws NoSuchMethodException {
		for( Method methodCandidate : VeryDifficultToCreateService.class.getMethods() ) {
			if( methodCandidate.getName().equals( methodName ) && matchArguments( methodCandidate, arguments ) ) {
				return methodCandidate;
			}
		}
		throw new NoSuchMethodException( methodName + "(" + arguments.length + " arguments)" );
	}

	private boolean matchArguments( Method methodCandidate, Object[] arguments ) {
		if( methodCandidate.getParameterCount() != arguments.length ) return false;

		for( int i = 0; i < arguments.length; i++ ) {
			Object argument = arguments[i];
			Class<?> argumentCandidateClass = methodCandidate.getParameterTypes()[i];
			if( !argumentCandidateClass.isInstance( argument ) ) {
				return false;
			}
		}
		return true;
	}
}
