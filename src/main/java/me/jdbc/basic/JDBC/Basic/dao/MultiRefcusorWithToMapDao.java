package me.jdbc.basic.JDBC.Basic.dao;

import lombok.extern.slf4j.Slf4j;
import me.jdbc.basic.JDBC.Basic.dto.CustomerRequest;
import me.jdbc.basic.JDBC.Basic.dto.ResponseResponse;
import me.jdbc.basic.JDBC.Basic.model.PgTable;
import me.jdbc.basic.JDBC.Basic.model.Result1;
import me.jdbc.basic.JDBC.Basic.model.Result2;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


// https://mkyong.com/jdbc/jdbc-callablestatement-postgresql-stored-function/
//

@Service
@Slf4j
public class MultiRefcusorWithToMapDao {

    private final DataSource dataSource;

    public MultiRefcusorWithToMapDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public ResponseResponse getResponseDto() {
        String refCursorQuery = "SELECT * FROM public.ref_fun_test(); FETCH ALL FROM refcursor_1; FETCH ALL FROM refcursor_2; FETCH ALL FROM refcursor_3;";
        ResponseResponse responseResponse = new ResponseResponse();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement refCursorStmt = conn.prepareStatement(refCursorQuery)) {

            boolean isExecution = refCursorStmt.execute();
            int sqlIndex = 0;

            while (isExecution) {
                try (ResultSet rs = refCursorStmt.getResultSet()) {
                    if (sqlIndex == 1) {
                        // refcursor_1 result
                        List<Result1> result1s = mapToResult1(rs);
                        responseResponse.setResult1List(result1s);
                    } else if (sqlIndex == 2) {
                        // refcursor_2 result
                        List<Result2> result2s = mapToResult2(rs);
                        responseResponse.setResult2List(result2s);
                    } else if (sqlIndex == 3) {
                        // refcursor_3 result
                        List<PgTable> pgTables = mapToPgTable(rs);
                        responseResponse.setPgTableList(pgTables);
                    }

                    sqlIndex++;

                    if (!refCursorStmt.getMoreResults()) {
                        break;
                    }
                }
            }

        } catch (SQLException e) {
            log.error("Failed to execute GreenplumRefcursorDaoCleanTable.", e);
        }

        return responseResponse;
    }

    private List<PgTable> mapToPgTable(ResultSet rs) throws SQLException {
        List<PgTable> results = new ArrayList<>();

        while (rs.next()) {
            PgTable pgTable = PgTable.builder()
                    .schemaName(rs.getString("schemaname"))
                    .tableName(rs.getString("tablename"))
                    .build();

            log.info(pgTable.toString());

            results.add(pgTable);
        }

        return results;
    }

    private List<Result2> mapToResult2(ResultSet rs) throws SQLException {
        List<Result2> results = new ArrayList<>();

        while (rs.next()) {
            Result2 result2 = Result2.builder()
                    .cursorName(rs.getString("cursor_name"))
                    .no(rs.getInt("no"))
                    .build();

            log.info(result2.toString());

            results.add(result2);
        }

        return results;
    }

    private List<Result1> mapToResult1(ResultSet rs) throws SQLException {
        List<Result1> results = new ArrayList<>();

        while (rs.next()) {
            Result1 results1 = Result1.builder()
                    .cursorName(rs.getString("cursor_name"))
                    .no(rs.getInt("no"))
                    .currentTimestamp(rs.getTimestamp("current_timestamp").toLocalDateTime())
                    .build();

            log.info(results1.toString());

            results.add(results1);
        }

        return results;
    }

    public void createCustomer(CustomerRequest customerRequest) {
        String runFunction = "{ ? = call public.customer_c(?, ?) }";

        try (Connection conn = dataSource.getConnection();
             CallableStatement cs = conn.prepareCall(runFunction)) {

            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, customerRequest.getName());
            cs.setInt(3, customerRequest.getAge());
            cs.executeUpdate();

            int result = cs.getInt(1);
            System.out.println("-------------------");
            System.out.println(result);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}