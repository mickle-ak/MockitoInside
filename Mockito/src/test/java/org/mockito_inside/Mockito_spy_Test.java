package org.mockito_inside;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Answers.CALLS_REAL_METHODS;
import static org.mockito.Mockito.*;


class Mockito_spy_Test {

	private final List<String> list = new ArrayList<>();
	private final List<String> spy  = spy( list );

	@Test
	void spy_allowed_verify() {
		spy.add( "a" );
		spy.add( "b" );

		verify( spy, times( 2 ) ).add( any() );
	}

	@Test
	void spy_allowed_whenStubbing() {
		when( spy.size() ).thenReturn( 42 );
		when( spy.add( any() ) ).thenReturn( true );

		spy.add( "1" );
		spy.add( "2" );

		assertThat( spy.size() ).isEqualTo( 42 );
		assertThat( spy ).containsExactly( (String) null ); // only one invocation during stubbing
	}

	@Test
	void spy_allowed_doStubbing() {
		doReturn( 42 ).when( spy ).size();
		doReturn( true ).when( spy ).add( any() );

		spy.add( "1" );
		spy.add( "2" );

		assertThat( spy.size() ).isEqualTo( 42 );
		assertThat( spy ).isEmpty();
	}

	@Test
	void whenStubbing_is_dangerous_by_spies_use_doStubbing() {
		assertThrows( IndexOutOfBoundsException.class,
		              () -> when( spy.get( 0 ) ).thenReturn( "fake object" ) ); // because list is empty

		doReturn( "fake object" ).when( spy ).get( 0 );
		assertThat( spy.get( 0 ) ).isEqualTo( "fake object" );
	}

	@SuppressWarnings( "unchecked" )
	@Test
	void spy_is_synonym_for_mock_with_parameters() {
		List<String> mock = mock( ArrayList.class, withSettings().spiedInstance( list ).defaultAnswer( CALLS_REAL_METHODS ) );
		mock.add( "c" );
		mock.add( "d" );

		assertThat( mock ).containsExactly( "c", "d" );

		verify( mock, times( 2 ) ).add( any() );
	}

	@Test
	void spy_is_a_COPY_of_spied_object() {
		list.add( "a" );
		list.add( "b" );
		spy.add( "c" );
		spy.add( "d" );

		assertThat( list ).containsExactly( "a", "b" );
		assertThat( spy ).containsExactly( "c", "d" );
	}
}
