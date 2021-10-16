package org.mockito_inside;

import org.junit.jupiter.api.Test;
import org.mockito_inside_prod.VeryDifficultToCreateService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class Mockito_whenStubbing_Test {

	@Test
	void whenStubbing_get_last_stubbed_value() {
		VeryDifficultToCreateService mock = mock( VeryDifficultToCreateService.class );

		when( mock.getObject( any() ) ).thenReturn( "1", "2", "3" );
		when( mock.getObject( "5" ) ).thenReturn( "555" ); // it 'eats' first return value!

		assertThat( mock.getObject( "1" ) ).isEqualTo( "2" ); // not "1" !
		assertThat( mock.getObject( "1" ) ).isEqualTo( "3" );
		assertThat( mock.getObject( "1" ) ).isEqualTo( "3" );
		assertThat( mock.getObject( "5" ) ).isEqualTo( "555" );
	}
}
