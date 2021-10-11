package org.mockito_inside;

import org.eclipse.jdt.annotation.Nullable;


public interface Stubber<R> {

	void thenReturn( @Nullable R i );
}
