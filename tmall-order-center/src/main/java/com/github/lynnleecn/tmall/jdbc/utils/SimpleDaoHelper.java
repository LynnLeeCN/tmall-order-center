package com.github.lynnleecn.tmall.jdbc.utils;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

public class SimpleDaoHelper {

	private static final QueryRunner runner = new QueryRunner(true);;

	private static final ThreadLocal<Connection> connectionHolder = new ThreadLocal<Connection>();

	// you will get the same connection if you acquire it in the same thread
	private Connection getConnection() {
		if (connectionHolder.get() == null) {
			Connection conncetion = ConnectionHelper.getConncetion();
			connectionHolder.set(conncetion);
			return conncetion;
		} else {
			return connectionHolder.get();
		}
	}

	/**
	 * Execute an SQL SELECT query with replacement parameters. The caller is
	 * responsible for closing the connection.
	 * 
	 * @param <T>
	 *            The type of object that the handler returns
	 * @param sql
	 *            The query to execute.
	 * @param rsh
	 *            The handler that converts the results into an object.
	 * @param params
	 *            The replacement parameters.
	 * @return The object returned by the handler.
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public <T> T query(String sql, ResultSetHandler<T> rsh, Object... params) throws SQLException {
		return runner.<T> query(getConnection(), sql, rsh, params);
	}

	/**
	 * Execute an SQL INSERT, UPDATE, or DELETE query.
	 *
	 * @param sql
	 *            The SQL to execute.
	 * @param params
	 *            The query replacement parameters.
	 * @return The number of rows updated.
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public int update(String sql, Object... params) throws SQLException {
		return runner.update(getConnection(), sql, params);
	}

	/**
	 * Execute a batch of SQL INSERT, UPDATE, or DELETE queries.
	 *
	 * @param sql
	 *            The SQL to execute.
	 * @param params
	 *            An array of query replacement parameters. Each row in this
	 *            array is one set of batch replacement values.
	 * @return The number of rows updated per statement.
	 * @throws SQLException
	 *             if a database access error occurs
	 * @since DbUtils 1.1
	 */
	public int[] batch(String sql, Object[][] params) throws SQLException {

		return runner.batch(getConnection(), sql, params);
	}

}
