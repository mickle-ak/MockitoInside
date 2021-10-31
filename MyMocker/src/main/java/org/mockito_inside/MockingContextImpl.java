package org.mockito_inside;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.eclipse.jdt.annotation.Nullable;
import org.mockito_inside.argument_mathchers.ArgumentMatcher;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor( access = AccessLevel.PACKAGE )
public class MockingContextImpl implements MockingContext {

	static final ThreadLocal<MockingContext> MOCKING_CONTEXT = ThreadLocal.withInitial( MockingContextImpl::new );

	@Nullable
	private StubbedInvocation invocationForProcess;

	@Nullable
	private List<ArgumentMatcher> matchers;


	@Override
	public void pushInvocationForProcess( StubbedInvocation invocation ) throws IllegalStateException {
		invocationForProcess = invocation;
	}

	@Override
	public StubbedInvocation pullInvocationForProcess() throws IllegalStateException {
		if( invocationForProcess == null ) throw new IllegalStateException( "Invocation must be stored before used" );
		@Nullable StubbedInvocation result = invocationForProcess;
		invocationForProcess = null;
		return result;
	}

	@Override
	public void pushArgumentMatcher( ArgumentMatcher matcher ) {
		if( matchers == null ) matchers = new ArrayList<>();
		matchers.add( matcher );
	}

	@Override
	public ArgumentMatcher popArgumentMatcher() {
		if( matchers != null && !matchers.isEmpty() ) {
			return matchers.remove( matchers.size() - 1 );
		}
		else throw new IllegalStateException( "No matchers to pop" );
	}

	@Override
	@Nullable
	public List<ArgumentMatcher> pullAllArgumentMatchers() throws IllegalStateException {
		List<ArgumentMatcher> result = matchers;
		matchers = null;
		return result;
	}
}
