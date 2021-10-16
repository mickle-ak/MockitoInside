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
}
