package com.guuidea.component.ip.util;

import com.guuidea.component.ip.domain.IPProviderInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {
	public static String dbUrl = "jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&amp;characterEncoding=utf-8";
	public static String dbDriver = "com.mysql.jdbc.Driver";
	public static String dbUsername = "root";
	public static String dbPassword = "123456";

	/**
	 * 设置数据库连接属性
	 * @param ipProviderInfo
	 */
	public static void setDbConfig(IPProviderInfo ipProviderInfo) {
		dbUrl = ipProviderInfo.getDbUrl();
		dbDriver = ipProviderInfo.getDbDriver();
		dbUsername = ipProviderInfo.getDbUsername();
		dbPassword = ipProviderInfo.getDbPassword();
	}

	// 将获得的数据库与java的链接返回（返回的类型为Connection）
	public static Connection getConnection() throws SQLException, ClassNotFoundException {
		Class.forName(dbDriver);
		return DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
	}

	public static void closeConnection(Connection connection, Statement statement, ResultSet result) {
		try {
			if (result != null) {
				result.close();
			}
		} catch (Exception e) {
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (Exception e2) {
			} finally {
				try {
					if (result != null) {
						connection.close();
					}
				} catch (Exception e3) {
				}
			}
		}
	}
}
