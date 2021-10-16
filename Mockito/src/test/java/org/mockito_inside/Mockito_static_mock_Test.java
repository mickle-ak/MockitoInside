package org.mockito_inside;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


class Mockito_static_mock_Test {

	private static class VeryHeavyService {

		private static final class LazyHolder {
			public static final VeryHeavyService INSTANCE = new VeryHeavyService();
		}

		public static VeryHeavyService getInstance() { return LazyHolder.INSTANCE; }

		public static int getIntStatic( int i ) { return getInstance().getInt( i ); }

		public int getInt( int i ) { return i; }
	}

	@Test
	void mock_static_in_single_test() {
		try( MockedStatic<VeryHeavyService> mockedStaticService = mockStatic( VeryHeavyService.class ) ) {

			mockedStaticService.when( VeryHeavyService::getInstance ).thenReturn( mock( VeryHeavyService.class ) );
			mockedStaticService.when( () -> VeryHeavyService.getIntStatic( anyInt() ) ).thenReturn( 2 );

			VeryHeavyService service = VeryHeavyService.getInstance();
			doReturn( 1 ).when( service ).getInt( anyInt() );

			assertThat( service.getInt( 1 ) ).isEqualTo( 1 );
			assertThat( service.getInt( 2 ) ).isEqualTo( 1 );
			assertThat( service.getInt( 3 ) ).isEqualTo( 1 );

			assertThat( VeryHeavyService.getIntStatic( 99 ) ).isEqualTo( 2 );
		}

		assertThat( VeryHeavyService.getIntStatic( 99 ) ).isEqualTo( 99 );
	}

	@Nested
	class MultiTestsWithMockedStatic {

		private MockedStatic<VeryHeavyService> mockedStaticService;

		@BeforeEach
		void setUp() {
			mockedStaticService = mockStatic( VeryHeavyService.class );
		}

		@AfterEach
		void tearDown() {
			mockedStaticService.close();
		}

		@Test
		void test_with_static_mocked_service() {
			mockedStaticService.when( VeryHeavyService::getInstance ).thenReturn( mock( VeryHeavyService.class ) );
			mockedStaticService.when( () -> VeryHeavyService.getIntStatic( anyInt() ) ).thenReturn( 2 );

			VeryHeavyService service = VeryHeavyService.getInstance();
			doReturn( 1 ).when( service ).getInt( anyInt() );

			assertThat( service.getInt( 1 ) ).isEqualTo( 1 );
			assertThat( service.getInt( 2 ) ).isEqualTo( 1 );
			assertThat( service.getInt( 3 ) ).isEqualTo( 1 );

			assertThat( VeryHeavyService.getIntStatic( 99 ) ).isEqualTo( 2 );
		}
	}
}
