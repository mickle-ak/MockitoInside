package org.mockito_inside;

import org.eclipse.jdt.annotation.Nullable;


@FunctionalInterface
public interface Answer<R> {

	@Nullable
	R answer( InterceptedInvocation invocation ) throws Throwable;
}
