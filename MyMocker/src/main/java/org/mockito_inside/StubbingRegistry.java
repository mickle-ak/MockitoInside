package org.mockito_inside;

import org.eclipse.jdt.annotation.Nullable;
import org.mockito_inside.argument_mathchers.ArgumentMatcher;

import java.util.List;


public interface StubbingRegistry {

	<R> InvocationStub<R> createStubFor( Invocation invocation, @Nullable List<ArgumentMatcher> argumentMatchers );

	@Nullable InvocationStub<?> findStubFor( Invocation invocation );
}
