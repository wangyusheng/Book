package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import bean.AddressDealBean;
import util.MySqlConnectPool;

public class BaseDao {

	public HashMap<Integer, String> getAllBaseInfo(String tableName) {
		HashMap<Integer, String> values = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		try {

			conn = MySqlConnectPool.getConnection();
			// 获得statement对象
			pstmt = conn.prepareStatement("select * from " + tableName);
			rs = pstmt.executeQuery();
			values = new HashMap<Integer, String>();
			// 判断结果

			// ResultSetMetaData rsmd = rs.getMetaData();
			// int columnCount = rsmd.getColumnCount();
			// System.out.println("columnCount=" + columnCount);
			while (rs.next()) {
				// 添加
				values.put(rs.getInt(1), rs.getString(2));
			}
			return values;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public int getIdByValue(String table_Name, String value_item, String value) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		try {

			conn = MySqlConnectPool.getConnection();
			// 获得statement对象
			pstmt = conn.prepareStatement("select * from " + table_Name
					+ " where " + value_item + "=?");
			pstmt.setString(1, value);
			rs = pstmt.executeQuery();
			// 判断结果
			if (!rs.next()) {
				return -1;// 该用户不存在
			}
			// 该用户存在
			return rs.getInt(1);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return -1;
	}

	public int addAddress(AddressDealBean address) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = 0;
		try {
			conn = MySqlConnectPool.getConnection();
			conn.setAutoCommit(false);
			// 添加用户
			pstmt = conn
					.prepareStatement("insert into address_deal(user_id,address1,address2,name,"
							+ "phone,flag) values(?,?,?,?,?,?)");
			pstmt.setInt(1, address.getUserId());
			pstmt.setString(2, address.getAddress1());
			pstmt.setString(3, address.getAddress2());
			pstmt.setString(4, address.getContact());
			pstmt.setString(5, address.getPhone());
			byte flag = 0;
			if (address.isFlag()) {
				flag = 1;
			}
			pstmt.setByte(6, flag);

			result = pstmt.executeUpdate();
			// 提交更改
			conn.commit();
		} catch (SQLException se) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
			throw new RuntimeException(se);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (null != conn) {
					conn.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		return result;
	}

	/**
	 * 
	 * @param book_id
	 * @param type
	 * @return
	 */
	public JSONArray retrievalAddress(int userId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		JSONArray jsonArray = new JSONArray();

		try {

			conn = MySqlConnectPool.getConnection();
			// 获得statement对象
			// 第一次查询

			pstmt = conn
					.prepareStatement("select * from address_deal where user_id= ?");
			pstmt.setInt(1, userId);

			rs = pstmt.executeQuery();
			// 判断结果
			while (rs.next()) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("address_id", rs.getInt(1));
				jsonObject.put("user_id", rs.getInt(2));
				jsonObject.put("address1", rs.getString(3));
				jsonObject.put("address2", rs.getString(4));
				jsonObject.put("flag", rs.getInt(5));
				jsonObject.put("name", rs.getString(6));
				jsonObject.put("phone", rs.getString(7));
				jsonArray.add(jsonObject);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return jsonArray;

	}

	/**
	 * 根据 用户ID查找 ChannelId
	 * 
	 * @param releaseUser
	 * @return
	 */
	public String getChannelId(int releaseUser) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = MySqlConnectPool.getConnection();
			conn.setAutoCommit(false);
			// 获得statement对象
			pstmt = conn
					.prepareStatement("select channel_id from channelid where user_id=?");
			pstmt.setInt(1, releaseUser);
			rs = pstmt.executeQuery();
			// 判断结果
			if (rs.next()) {
				return rs.getString(1);
			}
			return null;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static void main(String args[]) {
		// AddressDealBean address = new AddressDealBean();
		// address.setUserId(4);
		// address.setAddress1("江西省萍乡市上栗县");
		// address.setAddress2("金山镇张芳村");
		// address.setContact("哩封信");
		// address.setPhone("17722595175");
		// address.setFlag(true);
		System.out.println(new BaseDao().retrievalAddress(2));
	}

	// public static void main(String args[]) {
	// try {
	// String temp = "教材教辅";
	// String xmString;
	//
	// xmString = new String(temp.getBytes("UTF-8"));
	//
	// int interest_Id = new BaseDao().getIdByValue("interest",
	// "interes_name", temp);
	// System.out.println("interest_Id=" + interest_Id);
	//
	// System.out.println(new BaseDao().test());
	// } catch (UnsupportedEncodingException e) {
	// e.printStackTrace();
	// }
	//
	// // System.out.println(new BaseDao().add("测试一下"));
	// }

	public int insertOrUpdateChannelId(int userId, String channel_id) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		int result = 0;
		try {

			conn = MySqlConnectPool.getConnection();
			conn.setAutoCommit(false);
			// 获得statement对象
			pstmt = conn
					.prepareStatement("select * from channelid where user_id=?");
			pstmt.setInt(1, userId);
			rs = pstmt.executeQuery();
			boolean sign = false;
			// 判断结果
			if (rs.next()) {
				sign = true;
			}
			if (sign) {
				// 更新
				pstmt = conn
						.prepareStatement("update channelid set channel_id=? where user_id=?");
				pstmt.setString(1, channel_id);
				pstmt.setInt(2, userId);
				System.out
						.println("update channelid set channel_id=? where user_id=?");
			} else {
				// 插入
				pstmt = conn
						.prepareStatement("insert into channelid(user_id,channel_id) values(?,?)");
				pstmt.setInt(1, userId);
				pstmt.setString(2, channel_id);
				System.out
						.println("insert into channelid(user_id,channel_id) values(?,?)");

			}
			result = pstmt.executeUpdate();
			// 提交更改
			conn.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public String retrieval_ImAccount(int userId) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = MySqlConnectPool.getConnection();
			conn.setAutoCommit(false);
			// 获得statement对象
			pstmt = conn
					.prepareStatement("select im_account from user_info where user_id=?");
			pstmt.setInt(1, userId);
			rs = pstmt.executeQuery();
			// 判断结果
			if (rs.next()) {
				return rs.getString(1);
			}
			return null;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
