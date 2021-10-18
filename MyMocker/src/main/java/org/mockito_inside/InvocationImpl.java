package org.mockito_inside;

import lombok.Value;
import org.eclipse.jdt.annotation.Nullable;

import java.lang.reflect.Method;


@Value
public class InvocationImpl implements StubbedInvocation {

	Object           mock;
	Method           method;
	Object[]         args;
	StubbingRegistry stubbingRegistry;

	public InvocationImpl( Object mock, Method method, Object @Nullable [] args, StubbingRegistry stubbingRegistry ) {
		this.mock = mock;
		this.method = method;
		this.args = args == null ? new Object[0] : args;
		this.stubbingRegistry = stubbingRegistry;
	}
}
