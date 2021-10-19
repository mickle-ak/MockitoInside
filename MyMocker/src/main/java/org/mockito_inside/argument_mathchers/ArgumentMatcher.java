package org.mockito_inside.argument_mathchers;

import org.eclipse.jdt.annotation.Nullable;

@FunctionalInterface
public interface ArgumentMatcher {

	boolean matches( @Nullable Object actual );
}
