package me.jdbc.basic.JDBC.Basic.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class TestDao {

    @Autowired
    private JdbcContext context;

    public void execute(String name, int value) throws SQLException {

        String query = "select fn_test(?, ?);";

        context.workWithStatementStrategy(c -> {
            PreparedStatement statement = c.prepareStatement(query);
            statement.setString(1, name);
            statement.setInt(2, value);

            return statement;
        });
    }
}
