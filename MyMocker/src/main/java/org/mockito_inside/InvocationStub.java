package org.mockito_inside;

public interface InvocationStub<R> {

	Invocation getStabbedInvocation();

	void setAnswer( Answer<R> answer );

	Answer<R> getAnswer();
}
