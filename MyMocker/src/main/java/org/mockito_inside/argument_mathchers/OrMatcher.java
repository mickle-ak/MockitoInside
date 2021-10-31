package org.mockito_inside.argument_mathchers;

import org.eclipse.jdt.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class OrMatcher implements ArgumentMatcher {

	private final List<ArgumentMatcher> matchers = new ArrayList<>();

	public OrMatcher( ArgumentMatcher m1, ArgumentMatcher m2, ArgumentMatcher... otherMatchers ) {
		matchers.add( m1 );
		matchers.add( m2 );
		matchers.addAll( Arrays.asList( otherMatchers ) );
	}

	@Override
	public boolean matches( @Nullable Object actual ) {
		return matchers.stream().anyMatch( m -> m.matches( actual ) );
	}
}
