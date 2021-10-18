package org.mockito_inside;

public class StubberIml<R> implements Stubber<R> {

	private final InvocationStub<R> invocationStub;

	public StubberIml( InvocationStub<R> invocationStub ) {
		this.invocationStub = invocationStub;
	}

	@Override
	public Stubber<R> then( Answer<R> answer ) {
		invocationStub.addAnswer( answer );
		return this;
	}
}
