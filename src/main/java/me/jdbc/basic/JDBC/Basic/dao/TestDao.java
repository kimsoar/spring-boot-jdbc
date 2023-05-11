package me.jdbc.basic.JDBC.Basic.dao;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;

@Repository
public class TestDao {

    @Autowired
    private JdbcContext context;

    public void execute() throws SQLException {

        String query = "SELECT * FROM public.ref_fun_test(); FETCH ALL FROM refcursor_1; FETCH ALL FROM refcursor_2; FETCH ALL FROM refcursor_3;";

        context.workWithStatementStrategy(c -> {
            PreparedStatement statement = c.prepareStatement(query);
            statement.setString(1, "");
            statement.setString(1, "");
            statement.setString(1, "");

            return statement;
        });
    }
}
