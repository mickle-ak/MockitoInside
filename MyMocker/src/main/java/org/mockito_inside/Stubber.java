package org.mockito_inside;

import org.eclipse.jdt.annotation.Nullable;


public interface Stubber<R> {

	void then( Answer<R> answer );

	default void thenReturn( @Nullable R returnValue ) {
		then( invocation -> returnValue );
	}

	default void thenThrow( Throwable throwable ) {
		then( invocation -> {throw throwable;} );
	}
}
