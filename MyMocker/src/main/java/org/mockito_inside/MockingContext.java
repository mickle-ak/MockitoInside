package org.mockito_inside;


import org.eclipse.jdt.annotation.Nullable;
import org.mockito_inside.argument_mathchers.ArgumentMatcher;

import java.util.List;


public interface MockingContext {

	static MockingContext get() {
		return MockingContextImpl.MOCKING_CONTEXT.get();
	}

	void pushInvocationForProcess( StubbedInvocation invocation );

	StubbedInvocation pullInvocationForProcess() throws IllegalStateException;

	void pushArgumentMatcher( ArgumentMatcher matcher );

	@Nullable
	List<ArgumentMatcher> popAllArgumentMatchers() throws IllegalStateException;
}
