package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.Properties;

/**
 * 连接MySQL
 * 
 */
public class MySqlConnectPool {

	public static Properties properties;

	static {
		properties = new Properties();
		InputStream is = null;
		try {
			URL url = new URL(MySqlConnectPool.class.getClassLoader()
					.getResource("")
					+ "/config/mysql.properties");
			File file = new File(url.toURI());
			is = new FileInputStream(file);
			properties.load(is);
			is.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private MySqlConnectPool() {
	}

	public static Connection getConnection() {
		try {
			// 加载驱动程序

			Class.forName(properties.getProperty("driverClassName"));

			// 连续数据库
			Connection conn = DriverManager.getConnection(properties
					.getProperty("url"), properties.getProperty("username"),
					properties.getProperty("password"));
			if (!conn.isClosed()) {
				return conn;
			}
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;

	}

	public static void main(String[] args) {

		try {
			Connection conn = MySqlConnectPool.getConnection();

			Statement statement;

			statement = conn.createStatement();

			// 要执行的SQL语句

			String sql = "select * from orderinfo";

			// 结果集

			ResultSet rs = statement.executeQuery(sql);
			System.out.println("-----------------");
			System.out.println("执行结果如下所示:");
			System.out.println("-----------------");
			System.out.println(" 学号" + "\t" + " 姓名");
			System.out.println("-----------------");
			String name = null;
			while (rs.next()) {
				// 选择sname这列数据

				// rs.getString(0)
				//
				// name = rs.getString("sname");
				//
				// // 输出结果
				//
				// System.out.println(rs.getString("sno") + "\t" + name);
				//				
				// StringBuffer sbf=new StringBuffer();
				// for(int i=0;i<9;i++){
				// sbf.append(rs.getString(i)+"\t");
				// }
				//				
				System.out.println(rs.getString("orderno"));
			}
			rs.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();

		}
	}
}
