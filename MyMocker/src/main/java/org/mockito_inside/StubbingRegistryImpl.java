package org.mockito_inside;

import org.eclipse.jdt.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


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
		return stubs.stream().filter( stubCandidate -> stubMatchesInvocation( stubCandidate, interceptedInvocation ) ).findFirst().orElse( null );
	}

	private boolean stubMatchesInvocation( InvocationStub<?> stubCandidate, Invocation invocation ) {
		return Objects.equals( stubCandidate.getStabbedInvocation().getMethod(), invocation.getMethod() );
	}
}
