package com.github.lynnleecn.tmall.jdbc.utils;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class ConnectionHelper {

	private static final DataSource c3p0pool;

	static {
		c3p0pool = new ComboPooledDataSource();
	}

	public static Connection getConncetion() {
		try {
			return c3p0pool.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
