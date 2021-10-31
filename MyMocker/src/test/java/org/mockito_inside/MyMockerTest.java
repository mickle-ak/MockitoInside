package org.mockito_inside;

import org.junit.jupiter.api.Test;
import org.mockito_inside_prod.VeryDifficultToCreateService;

import java.util.AbstractList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito_inside.MyMocker.mock;
import static org.mockito_inside.MyMocker.when;
import static org.mockito_inside.argument_mathchers.ArgumentMatchers.*;


class MyMockerTest {

	private final VeryDifficultToCreateService mock = mock( VeryDifficultToCreateService.class );

	@Test
	void defaultValues() {
		assertAll(
				() -> assertThat( mock.getInt() ).isZero(),
				() -> assertThat( mock.getObject() ).isNull(),
				() -> assertThat( mock.getString() ).isEmpty(),
				() -> assertThat( mock.getList() ).isEmpty(),
				() -> assertThat( mock.getArrayList() ).isInstanceOf( AbstractList.class ).isEmpty()
		);
	}

	@Test
	void stubbing_withoutArguments() {
		when( mock.getInt() ).thenReturn( 1 );
		when( mock.getObject() ).thenReturn( mock );
		when( mock.getString() ).thenReturn( "abc" );

		assertThat( mock.getInt() ).isEqualTo( 1 );
		assertThat( mock.getObject() ).isSameAs( mock );
		assertThat( mock.getString() ).isEqualTo( "abc" );
	}

	@Test
	void separate_stubbing_for_separate_mocks() {
		VeryDifficultToCreateService anotherMockForSameClass = mock( VeryDifficultToCreateService.class );

		when( mock.getObject() ).thenReturn( "a" );
		when( anotherMockForSameClass.getObject() ).thenReturn( "b" );

		assertThat( mock.getObject() ).isEqualTo( "a" );
		assertThat( anotherMockForSameClass.getObject() ).isEqualTo( "b" );
	}

	@Test
	void customAnswer() {
		int[] counter = new int[]{ 0 };
		when( mock.getInt() ).then( invocation -> ++counter[0] );

		assertThat( mock.getInt() ).isEqualTo( 1 );
		assertThat( mock.getInt() ).isEqualTo( 2 );
		assertThat( mock.getInt() ).isEqualTo( 3 );
	}

	@Test
	void thenThrow() {
		when( mock.getString() ).thenThrow( new IllegalStateException( "unknown" ) );

		assertThatThrownBy( mock::getString ).isInstanceOf( IllegalStateException.class ).hasMessage( "unknown" );
	}

	@Test
	void stabbing_with_arguments() {
		when( mock.getObject( "a" ) ).thenReturn( ">a" );
		when( mock.getObject( "b" ) ).thenReturn( ">b" );

		assertThat( mock.getObject( "a" ) ).isEqualTo( ">a" );
		assertThat( mock.getObject( "b" ) ).isEqualTo( ">b" );
		assertThat( mock.getObject( "c" ) ).isNull();
	}

	@Test
	void stub_can_return_many_answers_and_repeat_last_answer() {
		when( mock.getObject() ).thenReturn( "1", "2", null )
		                        .thenThrow( new IllegalArgumentException() )
		                        .thenReturn( null )
		                        .then( invocation -> "4" );

		assertThat( mock.getObject() ).isEqualTo( "1" );
		assertThat( mock.getObject() ).isEqualTo( "2" );
		assertThat( mock.getObject() ).isNull();
		assertThrows( IllegalArgumentException.class, mock::getObject );
		assertThat( mock.getObject() ).isNull();
		assertThat( mock.getObject() ).isEqualTo( "4" );
		assertThat( mock.getObject() ).isEqualTo( "4" ); // repeat last answer
		assertThat( mock.getObject() ).isEqualTo( "4" );
	}

	@Test
	void whenWithoutThen_exception_thrown_by_next_method_invocation() {
		when( mock.getObject() );

		assertThrows( IllegalStateException.class, mock::getObject );
	}

	@Test
	void argumentsMatchers() {
		when( mock.getObject( any(), any( String.class ), anyInt() ) ).thenReturn( "matched!" );
		when( mock.getObject( eq( "a" ), eq( "b" ), eq( "c" ) ) ).thenReturn( "abc" );

		assertThat( mock.getObject( new Object(), "any-string", 1 ) ).isEqualTo( "matched!" );
		assertThat( mock.getObject( "a", "b", "c" ) ).isEqualTo( "abc" );
		assertThat( mock.getObject( 3L, "another-string", 99 ) ).isEqualTo( "matched!" ); // another values, matched
		assertThat( mock.getObject( new Object(), 2L, 1 ) ).isNull(); // not natches - p2 not a string
		assertThat( mock.getObject( new Object(), "2L", "non-int" ) ).isNull(); // not natches - p3 not an integer
	}

	@Test
	void last_stub_covers_previous() {
		when( mock.getObject( any() ) ).thenReturn( "any" );
		when( mock.getObject( any( String.class ) ) ).thenReturn( "any(String)-first" );
		when( mock.getObject( any( String.class ) ) ).thenReturn( "any(String)" );
		when( mock.getObject( anyInt() ) ).thenReturn( "anyInt" );
		when( mock.getObject( eq( "a" ) ) ).thenReturn( "eq(a)" );
		when( mock.getObject( "a" ) ).thenReturn( "a" );
		when( mock.getObject( "b" ) ).thenReturn( "b" );

		assertThat( mock.getObject( "a" ) ).isEqualTo( "a" );
		assertThat( mock.getObject( "b" ) ).isEqualTo( "b" );
		assertThat( mock.getObject( 456 ) ).isEqualTo( "anyInt" );
		assertThat( mock.getObject( "cde" ) ).isEqualTo( "any(String)" );
		assertThat( mock.getObject( new Object() ) ).isEqualTo( "any" );
		assertThat( mock.getObject( null ) ).isEqualTo( "any" );
	}

	@Test
	void argumentMatchers_or() {
		when( mock.getObject( or( eq( "1" ), any( Double.class ) ) ) ).thenReturn( "1_or_double" );
		when( mock.getObject( or( eq( "a" ), eq( "b" ), anyInt() ) ) ).thenReturn( "a_or_b_or_int" );

		assertThat( mock.getObject( "1" ) ).isEqualTo( "1_or_double" );
		assertThat( mock.getObject( 1d ) ).isEqualTo( "1_or_double" );

		assertThat( mock.getObject( "a" ) ).isEqualTo( "a_or_b_or_int" );
		assertThat( mock.getObject( "b" ) ).isEqualTo( "a_or_b_or_int" );
		assertThat( mock.getObject( 99 ) ).isEqualTo( "a_or_b_or_int" );

		assertThat( mock.getObject( 1L ) ).isNull(); // long is not implicit assignable to int or double
		assertThat( mock.getObject( "c" ) ).isNull();
	}
}
