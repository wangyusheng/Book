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
			// ���statement����
			pstmt = conn.prepareStatement("select * from " + tableName);
			rs = pstmt.executeQuery();
			values = new HashMap<Integer, String>();
			// �жϽ��

			// ResultSetMetaData rsmd = rs.getMetaData();
			// int columnCount = rsmd.getColumnCount();
			// System.out.println("columnCount=" + columnCount);
			while (rs.next()) {
				// ���
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
			// ���statement����
			pstmt = conn.prepareStatement("select * from " + table_Name
					+ " where " + value_item + "=?");
			pstmt.setString(1, value);
			rs = pstmt.executeQuery();
			// �жϽ��
			if (!rs.next()) {
				return -1;// ���û�������
			}
			// ���û�����
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
			// ����û�
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
			// �ύ����
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
			// ���statement����
			// ��һ�β�ѯ

			pstmt = conn
					.prepareStatement("select * from address_deal where user_id= ?");
			pstmt.setInt(1, userId);

			rs = pstmt.executeQuery();
			// �жϽ��
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
	 * ���� �û�ID���� ChannelId
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
			// ���statement����
			pstmt = conn
					.prepareStatement("select channel_id from channelid where user_id=?");
			pstmt.setInt(1, releaseUser);
			rs = pstmt.executeQuery();
			// �жϽ��
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
		// address.setAddress1("����ʡƼ����������");
		// address.setAddress2("��ɽ���ŷ���");
		// address.setContact("������");
		// address.setPhone("17722595175");
		// address.setFlag(true);
		System.out.println(new BaseDao().retrievalAddress(2));
	}

	// public static void main(String args[]) {
	// try {
	// String temp = "�̲Ľ̸�";
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
	// // System.out.println(new BaseDao().add("����һ��"));
	// }

	public int insertOrUpdateChannelId(int userId, String channel_id) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		int result = 0;
		try {

			conn = MySqlConnectPool.getConnection();
			conn.setAutoCommit(false);
			// ���statement����
			pstmt = conn
					.prepareStatement("select * from channelid where user_id=?");
			pstmt.setInt(1, userId);
			rs = pstmt.executeQuery();
			boolean sign = false;
			// �жϽ��
			if (rs.next()) {
				sign = true;
			}
			if (sign) {
				// ����
				pstmt = conn
						.prepareStatement("update channelid set channel_id=? where user_id=?");
				pstmt.setString(1, channel_id);
				pstmt.setInt(2, userId);
				System.out
						.println("update channelid set channel_id=? where user_id=?");
			} else {
				// ����
				pstmt = conn
						.prepareStatement("insert into channelid(user_id,channel_id) values(?,?)");
				pstmt.setInt(1, userId);
				pstmt.setString(2, channel_id);
				System.out
						.println("insert into channelid(user_id,channel_id) values(?,?)");

			}
			result = pstmt.executeUpdate();
			// �ύ����
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
			// ���statement����
			pstmt = conn
					.prepareStatement("select im_account from user_info where user_id=?");
			pstmt.setInt(1, userId);
			rs = pstmt.executeQuery();
			// �жϽ��
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
