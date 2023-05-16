package me.jdbc.basic.JDBC.Basic;

import me.jdbc.basic.JDBC.Basic.dao.JDBCTemplateDao;
import me.jdbc.basic.JDBC.Basic.dao.JDBCTranService;
import me.jdbc.basic.JDBC.Basic.dao.MultiRefcusorDao;
import me.jdbc.basic.JDBC.Basic.dao.MultiRefcusorWithToMapDao;
import me.jdbc.basic.JDBC.Basic.dto.CustomerRequest;
import me.jdbc.basic.JDBC.Basic.dto.ResponseResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@SpringBootTest
class JdbcBasicApplicationTests {

	@Autowired
	private JDBCTranService jdbcTranService;

	@Test
	void tranTest() {
		jdbcTranService.run();
	}

	@Test
	void contextTranTest() throws SQLException {
		jdbcTranService.contextRun();
	}

	@Autowired
	private MultiRefcusorDao multiRefcusorDao;

	@Autowired
	JDBCTemplateDao jDBCTemplateDao;

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
		String query = "SELECT * FROM public.ref_fun_test(); FETCH ALL FROM refcursor_1; FETCH ALL FROM refcursor_2; FETCH ALL FROM refcursor_3;";

		Map<String, List<Map<String, String>>> result = jDBCTemplateDao.executeProcedure(query);

		List<Map<String, String>> ref1 = result.get("refcursor_1");
		List<Map<String, String>> ref2 = result.get("refcursor_2");
		List<Map<String, String>> ref3 = result.get("refcursor_3");
	}


}
