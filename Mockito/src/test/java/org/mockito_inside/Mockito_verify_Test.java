package org.mockito_inside;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito_inside_prod.VeryComplexObject;
import org.mockito_inside_prod.VeryDifficultToCreateService;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.AdditionalMatchers.or;
import static org.mockito.Mockito.*;


class Mockito_verify_Test {

	private final VeryDifficultToCreateService mock = mock( VeryDifficultToCreateService.class );

	@Test
	void verification_mode_and_argument_matchers() {
		// somewhere in production
		mock.getObject( "1" );
		mock.getObject( "2" );
		mock.getObject( "3" );

		// in test
		verify( mock, times( 3 ) ).getObject( any() );

		verify( mock ).getObject( "1" );
		verify( mock ).getObject( "2" );
		verify( mock ).getObject( "3" );

		verify( mock, times( 2 ) ).getObject( or( eq( "1" ), eq( "2" ) ) );
		verify( mock, atLeastOnce() ).getObject( "2" );
		verify( mock, atMost( 1 ) ).getObject( "3" );

		verifyNoMoreInteractions( mock );
	}

	@Test
	void verificationMode_once() {
		// somewhere in production
		mock.getObject( "1" );
//		mock.getObject();

		// in test
		verify( mock, only() ).getObject( any() );
	}

	@Test
	void argument_captors_single_parameter() {
		// somewhere in production
		mock.getObject( "1" );
		mock.getObject( "2" );

		// in test
		ArgumentCaptor<Object> captor = ArgumentCaptor.forClass( Object.class );

		verify( mock, atLeastOnce() ).getObject( captor.capture() );
		assertThat( captor.getAllValues() ).containsExactly( "1", "2" );
		assertThat( captor.getValue() ).isEqualTo( "2" );
	}

	@Test
	void argument_captors_multi_parameter() {
		// somewhere in production
		mock.getObject( "a", "b", 1 );
		mock.getObject( "1", "2", 2 );
		mock.getObject( null, null, 3 );

		// in test
		ArgumentCaptor<Object> captor1 = ArgumentCaptor.forClass( Object.class );
		ArgumentCaptor<Object> captor2 = ArgumentCaptor.forClass( Object.class );
		ArgumentCaptor<Object> captor3 = ArgumentCaptor.forClass( Object.class );

		verify( mock, times( 3 ) ).getObject( captor1.capture(), captor2.capture(), captor3.capture() );

		assertThat( captor1.getValue() ).isNull();
		assertThat( captor2.getValue() ).isNull();
		assertThat( captor3.getValue() ).isEqualTo( 3 );
		assertThat( captor1.getAllValues() ).containsExactly( "a", "1", null );
		assertThat( captor2.getAllValues() ).containsExactly( "b", "2", null );
		assertThat( captor3.getAllValues() ).containsExactly( 1, 2, 3 );
	}

	@Test
	void verification_order() {
		VeryComplexObject mock2 = mock( VeryComplexObject.class );

		// somewhere in production
		mock.getObject( "1" );
		mock.getInt( 2 );
		mock2.getString();
		mock.getObject();
		mock.getObject( "2" );
		mock.getString();
		mock2.getComplexObject();
		mock.getObject( "x", "y", "z" );

		assertAll(
				() -> {
					InOrder orderAll = inOrder( mock, mock2 );
					orderAll.verify( mock ).getObject( "1" );
					orderAll.verify( mock ).getInt( 2 );
					orderAll.verify( mock2 ).getString();
					orderAll.verify( mock ).getObject();
					orderAll.verify( mock ).getObject( anyString() );
					orderAll.verify( mock ).getString();
					orderAll.verify( mock2 ).getComplexObject();
					orderAll.verify( mock ).getObject( eq( "x" ), anyString(), any() );
				},

				() -> {
					InOrder orderMock2 = inOrder( mock2 );
					orderMock2.verify( mock2 ).getString();
					orderMock2.verify( mock2 ).getComplexObject();
				}
		);
	}

	@Test
	void verify_ignores_invocations_for_stubbing() {
		when( mock.getObject() ).thenReturn( "abc" );
		verify( mock, never() ).getObject(); // verify ignores invocations during stubbing
	}

	@Test
	void do_not_verify_stub() {
		// in test
		when( mock.getObject() ).thenReturn( "abc" );

		// somewhere in production
		if( !Objects.equals( "abc", mock.getObject() ) ) throw new IllegalArgumentException();

		// in test
		verify( mock ).getObject(); // it is possible, but it is pointless
	}


	@Test
	void verify_after() {
		verify( mock, after( 1000 ).never() ).getObject();
	}

	@Test
	void verify_timeout() {
		new Thread( this::mockGetObjectAfter ).start();
		verify( mock, timeout( 1000 ).times( 1 ) ).getObject();
	}

	@SneakyThrows
	private void mockGetObjectAfter() {
		Thread.sleep( 300 ); // NOSONAR
		mock.getObject();

		Thread.sleep( 3000 ); // NOSONAR
		mock.getObject();
	}
}
