package org.mockito_inside;

import org.eclipse.jdt.annotation.Nullable;

import java.util.*;


@SuppressWarnings( "rawtypes" )
public class StubbingRegistryImpl implements StubbingRegistry {

	private final List<InvocationStub> stubs = new ArrayList<>();

	@Override
	public <R> InvocationStub<R> createStubFor( Invocation stabbedInvocation ) {
		InvocationStub<R> newInvocationStub = new InvocationStubImpl<>( stabbedInvocation );
		stubs.add( newInvocationStub );
		return newInvocationStub;
	}

	@Override
	@Nullable
	public InvocationStub<?> findStubFor( Invocation interceptedInvocation ) {
		for( ListIterator<InvocationStub> iterator = stubs.listIterator( stubs.size() ); iterator.hasPrevious(); ) {
			InvocationStub stubCandidate = iterator.previous();
			if( stubMatchesInvocation( stubCandidate, interceptedInvocation ) ) {
				return stubCandidate;
			}
		}
		return null;
	}

	private boolean stubMatchesInvocation( InvocationStub<?> stubCandidate, Invocation interceptedInvocation ) {
		return Objects.equals( stubCandidate.getStabbedInvocation().getMethod(), interceptedInvocation.getMethod() )
		       && matchArguments( stubCandidate.getStabbedInvocation().getArgs(), interceptedInvocation.getArgs() );
	}

	private boolean matchArguments( Object[] stabbedArgs, Object[] interceptedArgs ) {
		if( stabbedArgs.length != interceptedArgs.length ) return false;
		for( int i = 0; i < stabbedArgs.length; i++ ) {
			Object stabbedArg = stabbedArgs[i];
			Object interceptedArg = interceptedArgs[i];
			if( !Objects.equals( stabbedArg, interceptedArg ) ) return false;
		}
		return true;
	}
}
