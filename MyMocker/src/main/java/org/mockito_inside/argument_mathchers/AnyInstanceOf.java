package org.mockito_inside.argument_mathchers;

import lombok.RequiredArgsConstructor;
import org.eclipse.jdt.annotation.Nullable;


@RequiredArgsConstructor
public class AnyInstanceOf implements ArgumentMatcher {

	private final Class<?> clazz;

	@Override
	public boolean matches( @Nullable Object actual ) {
		return clazz.isInstance( actual );
	}
}
