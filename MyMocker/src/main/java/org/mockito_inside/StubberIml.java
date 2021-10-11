package org.mockito_inside;

import org.eclipse.jdt.annotation.Nullable;


public class StubberIml<R> implements Stubber<R> {

	private final InvocationStub<R> invocationStub;

	public StubberIml( InvocationStub<R> invocationStub ) {
		this.invocationStub = invocationStub;
	}

	@Override
	public void thenReturn( @Nullable R value ) {
		invocationStub.setReturnValue( value );
	}
}
