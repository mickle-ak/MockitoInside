package org.mockito_inside;

public interface MockingContext {

	static MockingContext get() {
		return MockingContextImpl.MOCKING_CONTEXT.get();
	}

	void pushInvocationForProcess( Invocation invocation );

	Invocation pullInvocationForProcess() throws IllegalStateException;
}
