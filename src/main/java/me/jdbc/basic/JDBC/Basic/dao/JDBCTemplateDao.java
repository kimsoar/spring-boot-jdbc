package me.jdbc.basic.JDBC.Basic.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.sql.*;
import java.util.*;
import java.util.concurrent.SynchronousQueue;

@Repository
@Slf4j
public class JDBCTemplateDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    //https://stackoverflow.com/questions/16149672/jdbctemplate-multiple-result-sets
    public Map<String, List<Map<String, String>>> executeProcedure(final String sql)
    {
        return jdbcTemplate.execute(con -> con.prepareCall(sql), (CallableStatementCallback<Map<String, List<Map<String, String>>>>) cs -> {
            Map<String, List<Map<String, String>>> resultMap = new LinkedHashMap<>();
            Set<ResultSet> results = new HashSet<>();
            boolean resultsAvailable = cs.execute();
            Queue<String> cursorNames = new LinkedList<>();
            ResultSet rs = cs.getResultSet();
            ResultSetMetaData rmd = cs.getMetaData();

            System.out.println(rmd.getColumnCount());
            System.out.println(rs.getMetaData().getColumnCount());
            ResultSetMetaData ss = rs.getMetaData();

            System.out.println(ss.toString());

            while(rs.next()) {
                System.out.println(rs.getString("ref_fun_test"));
                //System.out.println(rs.getObject(0));

            }

            while (resultsAvailable)
            {
                rs = cs.getResultSet();
                rmd = cs.getMetaData();
                int count = rmd.getColumnCount();

                List<Map<String, String>> table = new ArrayList<>();
                System.out.println("=====");
                while(rs.next()) {
                    ResultSetMetaData mm = rs.getMetaData();

                    System.out.println(mm.getColumnName(0));
                    System.out.println(rs.getString("cursor_name"));
                    System.out.println(rs.getInt("no"));
                    System.out.println(rs.getTimestamp("current_timestamp"));
                    Map<String, String> row = new LinkedHashMap<>();
                    for(int i = 0; i < count; i++) {
                        //row.put(rmd.getColumnName(i), rs.getString(i));
                    }
                    table.add(row);
                }

                resultMap.put(cursorNames.poll(), table);

                results.add(cs.getResultSet());
                resultsAvailable = cs.getMoreResults();
            }
            return resultMap;
        });
    }
}
