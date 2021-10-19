package org.mockito_inside.argument_mathchers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.eclipse.jdt.annotation.Nullable;
import org.mockito_inside.MockingContext;
import org.mockito_inside.reflection.TypeUtils;


@NoArgsConstructor( access = AccessLevel.PRIVATE )
public class ArgumentMatchers {

	@Nullable
	public static Object any() {
		pushArgumentMatcher( new Any() );
		return null;
	}

	@Nullable
	public static <T> T any( Class<T> clazz ) {
		pushArgumentMatcher( new AnyInstanceOf( clazz ) );
		return TypeUtils.getDefaultValue( clazz );
	}

	public static int anyInt() {
		pushArgumentMatcher( new AnyInstanceOf( Integer.class ) );
		return 0;
	}

	@Nullable
	public static <T> T eq( @Nullable T expectedValue ) {
		pushArgumentMatcher( new Equals<>( expectedValue ) );
		return expectedValue;
	}


	private static void pushArgumentMatcher( ArgumentMatcher matcher ) {
		MockingContext.get().pushArgumentMatcher( matcher );
	}
}
