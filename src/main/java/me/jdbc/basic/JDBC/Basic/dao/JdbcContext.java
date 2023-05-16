package me.jdbc.basic.JDBC.Basic.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.ConnectionProxy;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

@Repository
public class JdbcContext {

    @Autowired
    DataSource dataSource;

    private int fetchSize = -1;
    private int maxRows = -1;
    private int queryTimeout = -1;
    public int getQueryTimeout() {
        return this.queryTimeout;
    }
    public int getFetchSize() {
        return fetchSize;
    }

    public int getMaxRows() {
        return maxRows;
    }

    public DataSource getDataSource() {
        return dataSource;
    }


    protected DataSource obtainDataSource() {
        DataSource dataSource = this.getDataSource();
        Assert.state(dataSource != null, "No DataSource set");
        return dataSource;
    }

    protected Connection createConnectionProxy(Connection con) {
        return (Connection) Proxy.newProxyInstance(ConnectionProxy.class.getClassLoader(), new Class[]{ConnectionProxy.class}, new JdbcContext.CloseSuppressingInvocationHandler(con));
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void workWithStatementStrategy(StatementStrategy stmt) throws SQLException {
        Connection con = DataSourceUtils.getConnection(this.obtainDataSource());


        try {
            Connection conToUse = this.createConnectionProxy(con);
            PreparedStatement ps = stmt.makePreparedStatement(conToUse);

            ps.execute();

        } catch (SQLException var8) {
            DataSourceUtils.releaseConnection(con, this.getDataSource());
            con = null;
            throw new RuntimeException("!!!");
        } finally {
            DataSourceUtils.releaseConnection(con, this.getDataSource());
        }
    }

    private class CloseSuppressingInvocationHandler implements InvocationHandler {
        private final Connection target;

        public CloseSuppressingInvocationHandler(Connection target) {
            this.target = target;
        }

        @Nullable
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            switch (method.getName()) {
                case "equals":
                    return proxy == args[0];
                case "hashCode":
                    return System.identityHashCode(proxy);
                case "close":
                    return null;
                case "isClosed":
                    return false;
                case "getTargetConnection":
                    return this.target;
                case "unwrap":
                    return ((Class)args[0]).isInstance(proxy) ? proxy : this.target.unwrap((Class)args[0]);
                case "isWrapperFor":
                    return ((Class)args[0]).isInstance(proxy) || this.target.isWrapperFor((Class)args[0]);
                default:
                    try {
                        Object retVal = method.invoke(this.target, args);
                        if (retVal instanceof Statement) {
                            JdbcContext.this.applyStatementSettings((Statement)retVal);
                        }

                        return retVal;
                    } catch (InvocationTargetException var6) {
                        throw var6.getTargetException();
                    }
            }
        }
    }
    protected void applyStatementSettings(Statement stmt) throws SQLException {
        int fetchSize = this.getFetchSize();
        if (fetchSize != -1) {
            stmt.setFetchSize(fetchSize);
        }

        int maxRows = this.getMaxRows();
        if (maxRows != -1) {
            stmt.setMaxRows(maxRows);
        }

        DataSourceUtils.applyTimeout(stmt, this.getDataSource(), this.getQueryTimeout());
    }

}

