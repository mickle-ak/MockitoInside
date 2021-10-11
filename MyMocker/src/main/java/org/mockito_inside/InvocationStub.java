package org.mockito_inside;

import org.eclipse.jdt.annotation.Nullable;


public interface InvocationStub<R> {

	Invocation getStabbedInvocation();

	void setReturnValue( @Nullable R value );

	@Nullable R getReturnValue();
}
