package org.mockito_inside;

import org.junit.jupiter.api.Test;
import org.mockito.exceptions.misusing.UnfinishedStubbingException;
import org.mockito_inside_prod.VeryDifficultToCreateService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SuppressWarnings( "ThrowablePrintedToSystemOut" )
class TempTest {

	private final VeryDifficultToCreateService mock = mock( VeryDifficultToCreateService.class );

	@Test
	void last_stub_processed_before_previous_stubs() {
		when( mock.getObject( any() ) ).thenReturn( "any" );
		when( mock.getObject( anyString() ) ).thenReturn( "anyString" );
		when( mock.getObject( anyInt() ) ).thenReturn( "anyInt" );
		when( mock.getObject( null ) ).thenReturn( "NULL!" );
		when( mock.getObject( "a" ) ).thenReturn( "a1" );
		when( mock.getObject( "a" ) ).thenReturn( "a2" );

		assertThat( mock.getObject( "a" ) ).isEqualTo( "a2" );
		assertThat( mock.getObject( "a" ) ).isEqualTo( "a2" );
		assertThat( mock.getObject( null ) ).isEqualTo( "NULL!" );
		assertThat( mock.getObject( "b" ) ).isEqualTo( "anyString" );
		assertThat( mock.getObject( 2 ) ).isEqualTo( "anyInt" );
		assertThat( mock.getObject( new Object() ) ).isEqualTo( "any" );
	}

	@Test
	void any_covers_all_previous_stubbing() {
		when( mock.getObject( null ) ).thenReturn( "NULL!" );
		when( mock.getObject( "a" ) ).thenReturn( "a" );
		when( mock.getObject( any() ) ).thenReturn( "b" );

		assertThat( mock.getObject( "a" ) ).isEqualTo( "b" );
		assertThat( mock.getObject( null ) ).isEqualTo( "b" );
		assertThat( mock.getObject( new Object() ) ).isEqualTo( "b" );
	}

	@Test
	void whenWithoutThen_exception_thrown_by_next_method_invocation() {
		when( mock.getObject() );

		System.out.println( assertThrows( UnfinishedStubbingException.class, mock::getInt ) );
	}
}
