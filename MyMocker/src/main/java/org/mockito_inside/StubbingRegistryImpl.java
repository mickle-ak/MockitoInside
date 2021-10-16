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

	private boolean stubMatchesInvocation( InvocationStub<?> stubCandidate, Invocation invocation ) {
		return Objects.equals( stubCandidate.getStabbedInvocation().getMethod(), invocation.getMethod() );
	}
}
