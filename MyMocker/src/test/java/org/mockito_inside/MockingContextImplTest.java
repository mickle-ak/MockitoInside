package org.mockito_inside;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito_inside.MyMocker.mock;


class MockingContextImplTest {

	private final MockingContextImpl mockingContext = new MockingContextImpl();

	@Test
	void errorOnPullInvocationIfNotPushed() {
		Assertions.assertThrows( IllegalStateException.class, mockingContext::pullInvocationForProcess );
	}

	@Nested
	class MultiPulls {

		private final StubbedInvocation invocation1 = mock( StubbedInvocation.class );
		private final StubbedInvocation invocation2 = mock( StubbedInvocation.class );

		@BeforeEach
		void setUp() {
			mockingContext.pushInvocationForProcess( invocation1 );
			mockingContext.pushInvocationForProcess( invocation2 );
		}

		@Test
		void pullLastPush() {
			assertThat( mockingContext.pullInvocationForProcess() ).isSameAs( invocation2 );
		}

		@Test
		void onlyOnePullAllowed() {
			mockingContext.pullInvocationForProcess();  // first pull
			Assertions.assertThrows( IllegalStateException.class, mockingContext::pullInvocationForProcess );
		}
	}
}
