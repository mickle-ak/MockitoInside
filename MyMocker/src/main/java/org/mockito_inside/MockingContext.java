package org.mockito_inside;

public interface MockingContext {

	static MockingContext get() {
		return MockingContextImpl.MOCKING_CONTEXT.get();
	}

	void pushInvocationForProcess( StubbedInvocation invocation );

	StubbedInvocation pullInvocationForProcess() throws IllegalStateException;
}
