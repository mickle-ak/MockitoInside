package org.mockito_inside;

import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.exceptions.verification.SmartNullPointerException;
import org.mockito_inside_prod.VeryComplexObject;
import org.mockito_inside_prod.VeryDifficultToCreateService;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


class Mockito_default_values_Test {

	@Test
	void defaultValues_by_default_mocks_RETURNS_DEFAULTS() {
		VeryDifficultToCreateService mock = mock( VeryDifficultToCreateService.class );

		assertThat( mock.getObject() ).isNull();
		assertThat( mock.getString() ).isNull();
		assertThat( mock.getList() ).isInstanceOf( List.class ).isEmpty();
		assertThat( mock.getArrayList() ).isInstanceOf( ArrayList.class ).isEmpty();
		assertThat( mock.getVeryComplexObject() ).isNull();
	}

	@Test
	void defaultValues_by_RETURNS_MOCKS() {
		VeryDifficultToCreateService mock = mock( VeryDifficultToCreateService.class, Answers.RETURNS_MOCKS );

		assertThat( mock.getVeryComplexObject() ).isNotNull();
		assertThat( mock.getVeryComplexObject().getComplexObject() ).isNotNull();
		assertThat( mock.getVeryComplexObject().getComplexObject().getVeryDifficultToCreateService() ).isNotNull();

		assertThat( mock.getObject() ).isNotNull();
		assertThat( mockingDetails( mock.getObject() ).isMock() ).isTrue();
		assertThat( mock.getObject() ).isNotSameAs( mock.getObject() );

		assertThat( mock.getVeryComplexObject() ).isNotSameAs( mock.getVeryComplexObject() );
		assertThat( mockingDetails( mock.getVeryComplexObject() ).isMock() ).isTrue();

		assertThat( mock.getVeryComplexObject().getComplexObject() ).isNotSameAs( mock.getVeryComplexObject().getComplexObject() );
		assertThat( mockingDetails( mock.getVeryComplexObject().getComplexObject() ).isMock() ).isTrue();

		assertThat( mockingDetails( mock.getVeryComplexObject().getComplexObject().getVeryDifficultToCreateService() ).isMock() ).isTrue();
		assertThat( mock.getVeryComplexObject().getComplexObject().getVeryDifficultToCreateService() )
				.isNotSameAs( mock.getVeryComplexObject().getComplexObject().getVeryDifficultToCreateService() )
				.isNotSameAs( mock );

		assertThat( mock.getString() ).isEmpty();
		assertThat( mock.getVeryComplexObject().getString() ).isEmpty();
		assertThat( mockingDetails( mock.getString() ).isMock() ).isFalse();
		assertThat( mock.getString() ).isSameAs( mock.getString() );

		when( mock.getString() ).thenReturn( "aaa" );
		assertThat( mock.getString() ).isEqualTo( "aaa" );

		when( mock.getVeryComplexObject().getString() ).thenReturn( "bbb" );
		assertThat( mock.getVeryComplexObject().getString() ).isNotEqualTo( "bbb" ).isEmpty();

		assertThat( mock.getList() ).isInstanceOf( List.class ).isEmpty();
		assertThat( mock.getList() ).isNotSameAs( mock.getList() );
		assertThat( mock.getArrayList() ).isInstanceOf( ArrayList.class ).isEmpty();
		assertThat( mock.getArrayList() ).isNotSameAs( mock.getArrayList() );
	}

	@Test
	void defaultValues_by_RETURNS_DEEP_STUBS() {
		VeryDifficultToCreateService mock = mock( VeryDifficultToCreateService.class, Answers.RETURNS_DEEP_STUBS );

		assertThat( mock.getVeryComplexObject() ).isNotNull();
		assertThat( mock.getVeryComplexObject().getComplexObject() ).isNotNull();
		assertThat( mock.getVeryComplexObject().getComplexObject().getVeryDifficultToCreateService() ).isNotNull();

		assertThat( mock.getObject() ).isNull();
		assertThat( mockingDetails( mock.getObject() ).isMock() ).isFalse();
		assertThat( mock.getObject() ).isSameAs( mock.getObject() );

		assertThat( mock.getVeryComplexObject() ).isSameAs( mock.getVeryComplexObject() );
		assertThat( mockingDetails( mock.getVeryComplexObject() ).isMock() ).isTrue();

		assertThat( mock.getVeryComplexObject().getComplexObject() ).isSameAs( mock.getVeryComplexObject().getComplexObject() );
		assertThat( mockingDetails( mock.getVeryComplexObject().getComplexObject() ).isMock() ).isTrue();

		assertThat( mockingDetails( mock.getVeryComplexObject().getComplexObject().getVeryDifficultToCreateService() ).isMock() ).isTrue();
		assertThat( mock.getVeryComplexObject().getComplexObject().getVeryDifficultToCreateService() )
				.isSameAs( mock.getVeryComplexObject().getComplexObject().getVeryDifficultToCreateService() )
				.isNotSameAs( mock );

		assertThat( mock.getString() ).isNull();
		assertThat( mock.getVeryComplexObject().getString() ).isNull();
		assertThat( mockingDetails( mock.getString() ).isMock() ).isFalse();
		assertThat( mock.getString() ).isSameAs( mock.getString() );

		when( mock.getString() ).thenReturn( "aaa" );
		assertThat( mock.getString() ).isEqualTo( "aaa" );

		when( mock.getVeryComplexObject().getString() ).thenReturn( "aaa" );
		assertThat( mock.getVeryComplexObject().getString() ).isEqualTo( "aaa" );

		assertThat( mock.getList() ).isInstanceOf( List.class ).isEmpty();
		assertThat( mock.getList() ).isSameAs( mock.getList() );
		assertThat( mock.getArrayList() ).isInstanceOf( ArrayList.class ).isEmpty();
		assertThat( mock.getArrayList() ).isSameAs( mock.getArrayList() );
	}

	@SuppressWarnings( "ThrowablePrintedToSystemOut" )
	@Test
	void defaultValues_by_RETURNS_SMART_NULLS() {
		VeryDifficultToCreateService mock = mock( VeryDifficultToCreateService.class, Answers.RETURNS_SMART_NULLS );

		VeryComplexObject mockVeryComplexObject = mock.getVeryComplexObject();

		SmartNullPointerException smartNullPointerException = assertThrows( SmartNullPointerException.class,
		                                                                    mockVeryComplexObject::getComplexObject );
		System.out.println( smartNullPointerException );
	}
}
