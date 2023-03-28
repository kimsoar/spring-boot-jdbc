package me.jdbc.basic.JDBC.Basic.dao;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;

@Service
@Slf4j
public class MultiRefcusorDao {

    private final DataSource dataSource;

    public MultiRefcusorDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void execution() {
        String refCursorQuery = "SELECT * FROM public.ref_fun_test(); FETCH ALL FROM refcursor_1; FETCH ALL FROM refcursor_2; FETCH ALL FROM refcursor_3;";

        int columnWidth = 25;
        String line = StringUtils.repeat("-", 75);

        try (Connection conn = dataSource.getConnection();
             PreparedStatement refCursorStmt = conn.prepareStatement(refCursorQuery)) {

            // 레프커서 실행
            boolean isExecution = refCursorStmt.execute();

            int sqlIndex = 0;
            while (isExecution) {
                try (ResultSet rs = refCursorStmt.getResultSet()) {
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();

                    // 레프커서별 컬럼개수를 출력한다.
                    System.out.println(sqlIndex + "] ColumnCount : " + columnCount);

                    // 컬럼명을 출력한다.
                    System.out.println(line);
                    for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                        String columnName = metaData.getColumnName(columnIndex);
                        System.out.print(StringUtils.rightPad(columnName, columnWidth, " "));
                    }
                    System.out.println("");
                    System.out.println(line);

                    // 데이터를 출력한다.
                    while (rs.next()) {
                        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                            String columnTypeName = metaData.getColumnTypeName(columnIndex);

                            if (StringUtils.equalsIgnoreCase(columnTypeName, "refcursor")) {
                                // 컬럼유형이 레프커서이면 String으로 출력한다.
                                String columnValue = rs.getString(columnIndex);
                                System.out.print(StringUtils.rightPad(columnValue, columnWidth, " "));
                            } else {
                                String columnValue = rs.getObject(columnIndex).toString();
                                System.out.print(StringUtils.rightPad(columnValue, columnWidth, " "));
                            }
                        }
                        System.out.println("");
                    }

                    System.out.println("");
                    sqlIndex++;

                    // 실행할 쿼리가 없으면 while 문을 빠져 나간다.
                    if (!refCursorStmt.getMoreResults()) {
                        break;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
}
