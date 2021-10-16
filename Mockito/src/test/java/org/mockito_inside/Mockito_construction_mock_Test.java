package org.mockito_inside;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@SuppressWarnings( "unused" )
class Mockito_construction_mock_Test {

	private static class VeryHeavyObject {

		private static int doSomethingCounter;

		public int getInt( int i ) { return i; }

		public void doSomething() {
			doSomethingCounter++;
		}
	}

	@BeforeEach
	void setUp() {
		VeryHeavyObject.doSomethingCounter = 0;
	}


	@Test
	void mock_construction() {
		try( MockedConstruction<VeryHeavyObject> unused = mockConstruction( VeryHeavyObject.class ) ) {
			VeryHeavyObject vhoMock = new VeryHeavyObject();

			doReturn( 1 ).when( vhoMock ).getInt( anyInt() );

			assertThat( vhoMock.getInt( 100 ) ).isEqualTo( 1 );
			assertThat( vhoMock.getInt( -100 ) ).isEqualTo( 1 );
		}
		assertThat( new VeryHeavyObject().getInt( 10 ) ).isEqualTo( 10 ); // again use not mocked constructor
	}


	private static class Container {

		private final VeryHeavyObject vho = new VeryHeavyObject();

		public int getInt( int i ) {
			vho.doSomething();
			return vho.getInt( i );
		}
	}

	@Test
	void mock_construction_with_stubbing() {
		try( MockedConstruction<VeryHeavyObject> unused = mockConstruction(
				VeryHeavyObject.class,
				( mock, context ) -> doReturn( -1 ).when( mock ).getInt( anyInt() ) ) ) {

			Container c = new Container();

			assertThat( c.getInt( 10 ) ).isEqualTo( -1 );
			assertThat( c.getInt( 135 ) ).isEqualTo( -1 );
		}

		assertThat( new Container().getInt( 10 ) ).isEqualTo( 10 ); // again use not mocked constructor
	}


	@Nested
	class MultiTestsWithMockedConstruction {

		private MockedConstruction<VeryHeavyObject> mockedConstruction;

		@SuppressWarnings( "NotNullFieldNotInitialized" )
		private VeryHeavyObject veryHeavyObjectMock;

		@BeforeEach
		void setUp() {
			mockedConstruction = mockConstruction( VeryHeavyObject.class,
			                                       withSettings().defaultAnswer( CALLS_REAL_METHODS ),
			                                       ( mock, context ) -> veryHeavyObjectMock = mock );
		}

		@AfterEach
		void tearDown() {
			mockedConstruction.close();
		}

		@Test
		void test_with_mocked_internal_object() {
			Container c = new Container(); // set veryHeavyObjectMock to mock hier

			doReturn( -2 ).when( veryHeavyObjectMock ).getInt( anyInt() );

			assertThat( c.getInt( 1 ) ).isEqualTo( -2 );
			assertThat( c.getInt( 2 ) ).isEqualTo( -2 );

			assertThat( VeryHeavyObject.doSomethingCounter ).isEqualTo( 2 );
		}
	}
}
