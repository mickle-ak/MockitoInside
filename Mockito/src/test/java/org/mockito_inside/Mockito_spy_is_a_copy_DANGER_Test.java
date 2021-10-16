package org.mockito_inside;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.spy;


class Mockito_spy_is_a_copy_DANGER_Test {

	private interface Listener {

		void somethingHappened( EventSource source );
	}

	private static class EventSource {

		private final List<Listener> listeners = new ArrayList<>();

		public void addListener( Listener listener ) { listeners.add( listener ); }

		public void sendEvent() { listeners.forEach( l -> l.somethingHappened( this ) ); }

	}

	private static class Observer implements Listener {

		private final EventSource eventSource;

		public Observer( EventSource eventSource ) {
			this.eventSource = eventSource;
			this.eventSource.addListener( this );
		}

		@Override
		public void somethingHappened( EventSource source ) {
			if( source != eventSource ) throw new IllegalArgumentException( "unexpected source" );
		}
	}

	@SuppressWarnings( "unused" )
	@Test
	void spy_is_a_COPY_of_spied_object() {
		// somewhere in production code
		EventSource eventSource = new EventSource();
		Observer observer = new Observer( eventSource );

		// in test
		EventSource spiedEventSource = spy( eventSource ); // because we need to spy some calls of this object

		assertThrows( IllegalArgumentException.class, spiedEventSource::sendEvent );

		assertThat( eventSource.listeners ).isSameAs( spiedEventSource.listeners );
	}
}
