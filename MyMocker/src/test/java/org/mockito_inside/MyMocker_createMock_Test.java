package org.mockito_inside;

import org.junit.jupiter.api.Test;
import org.mockito_inside_prod.VeryDifficultToCreateService;

import static org.assertj.core.api.Assertions.assertThat;


class MyMocker_createMock_Test {

	@Test
	void createMock() {
		Object mock = MyMocker.mock( VeryDifficultToCreateService.class );
		assertThat( mock ).isInstanceOf( VeryDifficultToCreateService.class );
	}
}
