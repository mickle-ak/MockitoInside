package org.mockito_inside;

import org.junit.jupiter.api.Test;
import org.mockito_inside_prod.VeryDifficultToCreateService;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito_inside.MyMocker.mock;


class StubbingRegistryImplTest {

	private final StubbingRegistry registry = new StubbingRegistryImpl();

	@Test
	void findWithoutParameters() throws NoSuchMethodException {
		InvocationStub<?> stub_getObject = registry.createStubFor( createInvocation( "getObject" ) );
		InvocationStub<?> stub_getInt = registry.createStubFor( createInvocation( "getInt" ) );

		assertThat( registry.findStubFor( createInvocation( "getObject" ) ) ).isSameAs( stub_getObject );
		assertThat( registry.findStubFor( createInvocation( "getInt" ) ) ).isSameAs( stub_getInt );
		assertThat( registry.findStubFor( createInvocation( "getList" ) ) ).isNull();
	}

	private Invocation createInvocation( String objectName ) throws NoSuchMethodException {
		Method method = findMethod( objectName );
		return new InvocationImpl( mock( VeryDifficultToCreateService.class ), method, new Object[0], registry );
	}

	private Method findMethod( String objectName ) throws NoSuchMethodException {
		return VeryDifficultToCreateService.class.getMethod( objectName );
	}
}
