package org.mockito_inside.argument_mathchers;

import lombok.RequiredArgsConstructor;
import org.eclipse.jdt.annotation.Nullable;

import java.util.Objects;


@RequiredArgsConstructor
public class Equals<T> implements ArgumentMatcher {

	@Nullable
	private final T expectedValue;

	@Override
	public boolean matches( @Nullable Object argument ) {
		return Objects.equals( expectedValue, argument );
	}
}
