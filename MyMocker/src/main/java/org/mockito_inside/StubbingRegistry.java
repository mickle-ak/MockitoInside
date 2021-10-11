package org.mockito_inside;

import org.eclipse.jdt.annotation.Nullable;


public interface StubbingRegistry {

	<R> InvocationStub<R> createStubFor( Invocation invocation );

	@Nullable InvocationStub<?> findStubFor( Invocation invocation );
}
