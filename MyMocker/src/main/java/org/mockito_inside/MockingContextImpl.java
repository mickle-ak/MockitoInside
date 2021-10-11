package org.mockito_inside;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.eclipse.jdt.annotation.Nullable;


@NoArgsConstructor( access = AccessLevel.PACKAGE )
public class MockingContextImpl implements MockingContext {

	static final ThreadLocal<MockingContext> MOCKING_CONTEXT = ThreadLocal.withInitial( MockingContextImpl::new );

	@Nullable
	private Invocation invocationForProcess;

	@Override
	public void pushInvocationForProcess( Invocation invocation ) throws IllegalStateException {
		invocationForProcess = invocation;
	}

	@Override
	public Invocation pullInvocationForProcess() throws IllegalStateException {
		if( invocationForProcess == null ) throw new IllegalStateException( "Invocation must be stored before used" );
		@Nullable Invocation result = invocationForProcess;
		invocationForProcess = null;
		return result;
	}
}
