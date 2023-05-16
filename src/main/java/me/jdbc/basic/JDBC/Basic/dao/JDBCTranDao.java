package me.jdbc.basic.JDBC.Basic.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JDBCTranDao {

    private final JdbcTemplate jdbcTemplate;

    public JDBCTranDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void execute(String name, int value)
    {

        jdbcTemplate.execute("select fn_test('"+ name +"', "+ value +");");
        //jdbcTemplate.update("insert into hello(name, count) values (?, ?)", name, value);
    }
}
