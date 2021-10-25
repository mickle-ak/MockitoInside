package org.mockito_inside;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.eclipse.jdt.annotation.Nullable;


@NoArgsConstructor( access = AccessLevel.PACKAGE )
public class MockingContextImpl implements MockingContext {

	static final ThreadLocal<MockingContext> MOCKING_CONTEXT = ThreadLocal.withInitial( MockingContextImpl::new );

	@Nullable
	private StubbedInvocation invocationForProcess;

	@Override
	public void pushInvocationForProcess( StubbedInvocation invocation ) throws IllegalStateException {
		invocationForProcess = invocation;
	}

	@Override
	public StubbedInvocation pullInvocationForProcess() throws IllegalStateException {
		if( invocationForProcess == null ) throw new IllegalStateException( "Invocation must be stored before used" );
		@Nullable StubbedInvocation result = invocationForProcess;
		invocationForProcess = null;
		return result;
	}
}
