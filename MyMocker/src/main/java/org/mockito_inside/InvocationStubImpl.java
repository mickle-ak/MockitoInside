package org.mockito_inside;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.eclipse.jdt.annotation.Nullable;


@RequiredArgsConstructor
public class InvocationStubImpl<R> implements InvocationStub<R> {

	@Getter
	private final Invocation stabbedInvocation;

	@Nullable
	@Getter
	@Setter
	private R returnValue;

}
