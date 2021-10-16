package org.mockito_inside;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


class Mockito_spy_real_method_invocation_Test {

	private final List<String> realMethodInvocations = new ArrayList<>();

	class ObjectToSpy {

		public Object foo( Object o ) {
			realMethodInvocations.add( "foo(" + o + ")" );
			return o;
		}
	}

	@Test
	void whenStubbing_by_spy_CALLS_REAL_METHOD() {
		ObjectToSpy ots = spy( new ObjectToSpy() );

		assertThat( ots.foo( "1" ) ).isEqualTo( "1" );

		when( ots.foo( eq( "2" ) ) ).thenReturn( "3" ); // it calls real method! NOSONAR

		assertThat( ots.foo( "2" ) ).isEqualTo( "3" );  // stubbed => don't call real method
		assertThat( ots.foo( "3" ) ).isEqualTo( "3" );  // not stubbed => call real method

		assertThat( realMethodInvocations ).containsExactly( "foo(1)", "foo(null)", "foo(3)" ); // does not contain "foo(2)"

		ArgumentCaptor<Object> captor = ArgumentCaptor.forClass( Object.class );
		verify( ots, times( 3 ) ).foo( captor.capture() ); // all not stubbing invocations
		assertThat( captor.getAllValues() ).containsExactly( "1", "2", "3" );
	}

	@Test
	void doStubbing_by_spy_DOES_NOT_CALL_REAL_METHOD() {
		ObjectToSpy ots = spy( new ObjectToSpy() );

		assertThat( ots.foo( "1" ) ).isEqualTo( "1" );

		doReturn( "3" ).when( ots ).foo( eq( "2" ) ); // real "foo" does not invoke! NOSONAR

		assertThat( ots.foo( "2" ) ).isEqualTo( "3" );  // stubbed => don't call real method
		assertThat( ots.foo( "3" ) ).isEqualTo( "3" );  // not stubbed => call real method

		assertThat( realMethodInvocations ).containsExactly( "foo(1)", "foo(3)" ); // does not contain "foo(2)" at all!

		ArgumentCaptor<Object> captor = ArgumentCaptor.forClass( Object.class );
		verify( ots, times( 3 ) ).foo( captor.capture() ); // all not stubbing invocations
		assertThat( captor.getAllValues() ).containsExactly( "1", "2", "3" );
	}
}
