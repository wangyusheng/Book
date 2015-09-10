package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.Properties;

/**
 * ����MySQL
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
			// ������������

			Class.forName(properties.getProperty("driverClassName"));

			// �������ݿ�
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

			// Ҫִ�е�SQL���

			String sql = "select * from orderinfo";

			// �����

			ResultSet rs = statement.executeQuery(sql);
			System.out.println("-----------------");
			System.out.println("ִ�н��������ʾ:");
			System.out.println("-----------------");
			System.out.println(" ѧ��" + "\t" + " ����");
			System.out.println("-----------------");
			String name = null;
			while (rs.next()) {
				// ѡ��sname��������

				// rs.getString(0)
				//
				// name = rs.getString("sname");
				//
				// // ������
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
