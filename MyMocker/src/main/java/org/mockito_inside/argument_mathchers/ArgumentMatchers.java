package org.mockito_inside.argument_mathchers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.eclipse.jdt.annotation.Nullable;
import org.mockito_inside.MockingContext;
import org.mockito_inside.reflection.TypeUtils;

import java.util.stream.Stream;


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

	@SuppressWarnings( "unused" )
	@Nullable
	public static Object or( @Nullable Object o1, @Nullable Object o2, @Nullable Object... other ) {
		ArgumentMatcher m1 = MockingContext.get().popArgumentMatcher();
		ArgumentMatcher m2 = MockingContext.get().popArgumentMatcher();
		ArgumentMatcher[] otherMatchers = Stream.of( other )
		                                        .map( m -> MockingContext.get().popArgumentMatcher() )
		                                        .toArray( ArgumentMatcher[]::new );
		pushArgumentMatcher( new OrMatcher( m1, m2, otherMatchers ) );
		return null;
	}


	private static void pushArgumentMatcher( ArgumentMatcher matcher ) {
		MockingContext.get().pushArgumentMatcher( matcher );
	}
}
