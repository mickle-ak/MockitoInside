package org.mockito_inside.argument_mathchers;

import org.eclipse.jdt.annotation.Nullable;


public class Any implements ArgumentMatcher {

	@Override
	public boolean matches( @Nullable Object actual ) {
		return true;
	}
}
