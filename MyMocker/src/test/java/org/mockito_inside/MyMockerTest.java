package org.mockito_inside;

import org.junit.jupiter.api.Test;
import org.mockito_inside_prod.VeryDifficultToCreateService;

import java.util.AbstractList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
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
}
