package org.mockito_inside;

public interface InvocationStub<R> {

	Invocation getStabbedInvocation();

	void addAnswer( Answer<R> answer );

	Answer<R> getNextAnswer();
}
