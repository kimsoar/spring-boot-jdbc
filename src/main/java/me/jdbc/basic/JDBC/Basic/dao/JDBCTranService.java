package me.jdbc.basic.JDBC.Basic.dao;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

@Service
@Transactional
public class JDBCTranService {
    private final JDBCTranDao jdbcTranDao;
    private final TestDao testDao;

    public JDBCTranService(JDBCTranDao jdbcTranDao, TestDao testDao) {
        this.jdbcTranDao = jdbcTranDao;
        this.testDao = testDao;
    }


    public void run() {
        jdbcTranDao.execute("t1", 1);
        jdbcTranDao.execute("t2", 2);
        int c = 1;
        if (c == 1) {
            throw new RuntimeException("runtime exception");
        }
        jdbcTranDao.execute("t3", 3);
    }


    public void contextRun() throws SQLException {
        testDao.execute("t1", 1);
        testDao.execute("t2", 2);
        int c = 1;
        if (c == 1) {
            throw new RuntimeException("runtime exception");
        }
        testDao.execute("t3", 3);
    }
}
