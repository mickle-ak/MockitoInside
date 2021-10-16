package org.mockito_inside;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@RequiredArgsConstructor
public class InvocationStubImpl<R> implements InvocationStub<R> {

	@Getter
	private final Invocation stabbedInvocation;

	@Getter
	@Setter
	private Answer<R> answer;

}
