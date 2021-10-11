package org.mockito_inside.reflection;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.eclipse.jdt.annotation.Nullable;

import java.lang.reflect.Method;


@NoArgsConstructor( access = AccessLevel.PRIVATE )
public final class ReflectionUtils {


	public static Method findMethod( Class<?> clazz, String methodName, @Nullable Object... arguments ) {
		for( Method methodCandidate : clazz.getMethods() ) {
			if( methodCandidate.getName().equals( methodName ) && matchArguments( methodCandidate, arguments ) ) {
				return methodCandidate;
			}
		}
		throw new IllegalArgumentException( methodName + "[with" + arguments.length + " arguments] not found in class " + clazz.getSimpleName() );
	}

	public static boolean matchArguments( Method methodCandidate, @Nullable Object[] arguments ) {
		if( methodCandidate.getParameterCount() != arguments.length ) return false;

		for( int i = 0; i < arguments.length; i++ ) {
			Object argument = arguments[i];
			Class<?> argumentCandidateClass = methodCandidate.getParameterTypes()[i];
			if( argument != null && !argumentCandidateClass.isInstance( argument ) ) {
				return false;
			}
		}
		return true;
	}
}
