package org.mockito_inside;

import org.eclipse.jdt.annotation.Nullable;
import org.mockito_inside.argument_mathchers.ArgumentMatcher;
import org.mockito_inside.argument_mathchers.Equals;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@SuppressWarnings( "rawtypes" )
public class StubbingRegistryImpl implements StubbingRegistry {

	private final List<InvocationStub> stubs = new ArrayList<>();

	@Override
	public <R> InvocationStub<R> createStubFor( Invocation stabbedInvocation, @Nullable List<ArgumentMatcher> argumentMatchers ) {
		if( argumentMatchers == null ) argumentMatchers = argumentMatchersFromArguments( stabbedInvocation.getArgs() );
		InvocationStub<R> newInvocationStub = new InvocationStubImpl<>( stabbedInvocation, argumentMatchers );
		stubs.add( newInvocationStub );
		return newInvocationStub;
	}

	private List<ArgumentMatcher> argumentMatchersFromArguments( Object[] args ) {
		return Stream.of( args ).map( Equals::new ).collect( Collectors.toList() );
	}


	@Override
	@Nullable
	public InvocationStub<?> findStubFor( Invocation interceptedInvocation ) {
		for( ListIterator<InvocationStub> iterator = stubs.listIterator( stubs.size() ); iterator.hasPrevious(); ) {
			InvocationStub stubCandidate = iterator.previous();
			if( matchInvocation( stubCandidate, interceptedInvocation ) ) {
				return stubCandidate;
			}
		}
		return null;
	}

	private boolean matchInvocation( InvocationStub<?> stub, Invocation interceptedInvocation ) {
		return Objects.equals( stub.getStabbedInvocation().getMethod(), interceptedInvocation.getMethod() )
		       && matchArguments( stub.getArgumentMatchers(), interceptedInvocation.getArgs() );
	}

	private boolean matchArguments( List<ArgumentMatcher> stabbedArgs, Object[] interceptedArgs ) {
		assert stabbedArgs.size() == interceptedArgs.length;
		for( int i = 0; i < stabbedArgs.size(); i++ ) {
			ArgumentMatcher argumentMatcher = stabbedArgs.get( i );
			if( !argumentMatcher.matches( interceptedArgs[i] ) ) return false;
		}
		return true;
	}
}
