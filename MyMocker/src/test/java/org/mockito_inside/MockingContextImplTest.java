package org.mockito_inside;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito_inside.argument_mathchers.ArgumentMatcher;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito_inside.MyMocker.mock;


class MockingContextImplTest {

	private final MockingContextImpl mockingContext = new MockingContextImpl();

	@Test
	void errorOnPullInvocationIfNotPushed() {
		assertThrows( IllegalStateException.class, mockingContext::pullInvocationForProcess );
	}

	@Nested
	class MultiPullsInvocations {

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
			assertThrows( IllegalStateException.class, mockingContext::pullInvocationForProcess );
		}
	}

	@Nested
	class ArgumentMatchers {

		ArgumentMatcher am1 = mock( ArgumentMatcher.class );
		ArgumentMatcher am2 = mock( ArgumentMatcher.class );
		ArgumentMatcher am3 = mock( ArgumentMatcher.class );

		@BeforeEach
		void setUp() {
			mockingContext.pushArgumentMatcher( am1 );
			mockingContext.pushArgumentMatcher( am2 );
			mockingContext.pushArgumentMatcher( am3 );
		}

		@Test
		void pullAllArgumentMatchers_returns_all_pushed_matchers() {
			List<ArgumentMatcher> matchers = mockingContext.pullAllArgumentMatchers();
			assertThat( matchers ).isNotNull().hasSize( 3 );
			assertThat( matchers.get( 0 ) ).isSameAs( am1 );
			assertThat( matchers.get( 1 ) ).isSameAs( am2 );
			assertThat( matchers.get( 2 ) ).isSameAs( am3 );
		}

		@Test
		void pullAllArgumentMatchers_second_pull_returns_null() {
			assertThat( mockingContext.pullAllArgumentMatchers() ).as( "first pull" ).isNotNull();
			assertThat( mockingContext.pullAllArgumentMatchers() ).as( "second pull => null" ).isNull();
		}

		@Test
		void popArgumentMatcher_pops_in_correct_order() {
			assertThat( mockingContext.popArgumentMatcher() ).isSameAs( am3 );
			assertThat( mockingContext.popArgumentMatcher() ).isSameAs( am2 );
			assertThat( mockingContext.popArgumentMatcher() ).isSameAs( am1 );
		}

		@Test
		void popArgumentMatcher_exception_if_nothing_to_pop() {
			mockingContext.popArgumentMatcher(); //  am1
			mockingContext.popArgumentMatcher(); //  am2
			mockingContext.popArgumentMatcher(); //  am3

			assertThrows( IllegalStateException.class, mockingContext::popArgumentMatcher );
		}
	}
}
