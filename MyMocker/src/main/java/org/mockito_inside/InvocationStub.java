package org.mockito_inside;

import org.mockito_inside.argument_mathchers.ArgumentMatcher;

import java.util.List;


public interface InvocationStub<R> {

	Invocation getStabbedInvocation();

	void addAnswer( Answer<R> answer );

	Answer<R> getNextAnswer();

	List<ArgumentMatcher> getArgumentMatchers();
}
