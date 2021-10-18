package org.mockito_inside;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
public class InvocationStubImpl<R> implements InvocationStub<R> {

	@Getter
	private final Invocation stabbedInvocation;

	private final List<Answer<R>> answers = new ArrayList<>();

	@Override
	public void addAnswer( Answer<R> answer ) {
		answers.add( answer );
	}

	@Override
	public Answer<R> getNextAnswer() {
		if( answers.isEmpty() ) throw new IllegalStateException( "thenReturn() may be missing" );
		return answers.size() > 1 ? answers.remove( 0 ) : answers.get( 0 );
	}
}
