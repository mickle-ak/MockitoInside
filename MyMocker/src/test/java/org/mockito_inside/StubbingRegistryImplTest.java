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


	private Invocation createInvocation( String methodName ) throws NoSuchMethodException {
		Method method = findMethod( methodName );
		return new InvocationImpl( mock( VeryDifficultToCreateService.class ), method, new Object[0], registry );
	}

	private Method findMethod( String objectName ) throws NoSuchMethodException {
		return VeryDifficultToCreateService.class.getMethod( objectName );
	}
}
