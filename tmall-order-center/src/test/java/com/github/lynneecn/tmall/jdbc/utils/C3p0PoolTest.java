package com.github.lynneecn.tmall.jdbc.utils;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class C3p0PoolTest extends TestCase {

	public void dbConnectionTest() {

		ComboPooledDataSource cpds = new ComboPooledDataSource();
		QueryRunner runner = new QueryRunner(cpds, true);
		try {
			List<Map<String, Object>> result = runner.query("select * from order_taobao", new MapListHandler());
			for (Map<String, Object> row : result) {
				String tid = String.valueOf(row.get("tid"));
				String trade_name = String.valueOf(row.get("trade_name"));
				System.out.println(tid + ":" + trade_name);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
