package me.jdbc.basic.JDBC.Basic;

import me.jdbc.basic.JDBC.Basic.dao.MultiRefcusorDao;
import me.jdbc.basic.JDBC.Basic.dao.MultiRefcusorWithToMapDao;
import me.jdbc.basic.JDBC.Basic.dto.CustomerRequest;
import me.jdbc.basic.JDBC.Basic.dto.ResponseResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;

@SpringBootTest
class JdbcBasicApplicationTests {

	@Autowired
	private MultiRefcusorDao multiRefcusorDao;

	@Test
	void greenplumRefcursorDaoCleanTest() {
		multiRefcusorDao.execution();
	}

	@Autowired
	private MultiRefcusorWithToMapDao multiRefcusorWithToMapDao;

	@Test
	void greenplumRefcursorDaoCleanTableTest() {
		ResponseResponse responseResponse = multiRefcusorWithToMapDao.getResponseDto();
	}


	@Test
	void createCustomerTest() throws SQLException {
		CustomerRequest customerRequest = new CustomerRequest();
		customerRequest.setName("John");
		customerRequest.setAge(10);

		multiRefcusorWithToMapDao.createCustomer(customerRequest);
	}


	@Test
	void callableTest() throws SQLException {
		multiRefcusorWithToMapDao.callableTest();
	}


}
