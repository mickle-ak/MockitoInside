package org.mockito_inside;

import org.eclipse.jdt.annotation.Nullable;


public interface Stubber<R> {

	Stubber<R> then( Answer<R> answer );

	@SuppressWarnings( "unchecked" )
	default Stubber<R> thenReturn( @Nullable R value, @Nullable R... values ) {
		then( invocation -> value );
		for( R v : values ) then( invocation -> v );
		return this;
	}

	default Stubber<R> thenThrow( Throwable throwable ) {
		return then( invocation -> {throw throwable;} );
	}
}
