package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import util.MySqlConnectPool;
import bean.GiveBookOrderBefore;
import bean.GiveBookBean;


public class GiveBookDao {
	private static int SIZE = 10;

	public int[] insertBookgiveInfo(GiveBookOrderBefore giveBookInfo) {
		Connection conn = null;
		Statement sm = null;
		int[] result = null;
		try {
			conn = MySqlConnectPool.getConnection();
			// conn.setAutoCommit(false);

			sm = conn.createStatement();

			
			sm.addBatch("insert into book_give_info(release_user,obtain_user,release_book,"
					+ "obtain_msg,generation_time,release_bookname) values("
					+ giveBookInfo.getRelease_user()
					+ ","
					+ giveBookInfo.getObtain_user()
					+ ","
					+ giveBookInfo.getRelease_book()
					+ ",'"
					+ giveBookInfo.getObtain_msg()
					+ "','"
					+ giveBookInfo.getGenerate_time()
					+ "','"
					+ giveBookInfo.getRelease_bookname() + "')");

			// �ύ����
			result = sm.executeBatch();
			System.out.println("ִ����");
		} catch (SQLException se) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
			throw new RuntimeException(se);
		} finally {
			try {

				if (sm != null) {
					sm.close();
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
	 * �Ƿ���Ϊrelease_book ������Ԥ����
	 * 
	 * @param release_book
	 * @return
	 */
	private boolean isReserved(int release_book) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// data.getJSONArray(key)
		try {
			conn = MySqlConnectPool.getConnection();
			// ���statement����
			pstmt = conn
					.prepareStatement("select * from book_give_info where release_book=?");
			pstmt.setInt(1, release_book);
			rs = pstmt.executeQuery();
			// �жϽ��
			if (rs.next()) {
				return true;
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

		return false;
	}

	public JSONArray retrieval(int book_id, int infoId, int type) {
		System.out.println("book_id=" + book_id + "," + "type=" + type
				+ ",infoId=" + infoId);
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		JSONArray jsonArray = new JSONArray();
		// data.getJSONArray(key)

		try {

			conn = MySqlConnectPool.getConnection();
			// ���statement����
			if (type == 0) {
				// ��һ�β�ѯ

				pstmt = conn
						.prepareStatement("select * from book_give_info where release_book=? order by info_id desc limit ?");
				pstmt.setInt(1, book_id);
				pstmt.setInt(2, SIZE);

			} else if (type == 1) {
				pstmt = conn
						.prepareStatement("select * from book_give_info where info_id <? and release_book=? order by info_id desc limit ?");
				pstmt.setInt(1, infoId);
				pstmt.setInt(2, book_id);
				pstmt.setInt(3, SIZE);
			} else if (type == 2) {
				pstmt = conn
						.prepareStatement("select * from book_give_info where info_id >? and release_book=? order by info_id desc limit ?");
				pstmt.setInt(1, infoId);
				pstmt.setInt(2, book_id);
				pstmt.setInt(3, SIZE);
			}
			rs = pstmt.executeQuery();
			// �жϽ��
			while (rs.next()) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("info_id", rs.getInt(1));
				jsonObject.put("release_user", rs.getInt(2));
				jsonObject.put("obtain_user", rs.getInt(3));
				jsonObject.put("release_book", rs.getInt(4));			
				jsonObject.put("obtain_msg", rs.getString(5));
				jsonObject.put("generation_time", rs.getString(6));				
				jsonObject.put("release_bookname", rs.getString(7));
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
		System.out.println("�����:" + jsonArray.size());
		return jsonArray;
	}

	/**
	 * ���붩��
	 * 
	 * @param giveBookBean
	 */
	public int[] insertgiveBookOrder(GiveBookBean giveBookBean) {
		System.out.println("insertgiveBookOrder="
				+ giveBookBean.getRelease_book());
		Connection conn = null;
		Statement sm = null;
		int[] result = null;
		
		try {
			conn = MySqlConnectPool.getConnection();

			sm = conn.createStatement();
			
			sm.addBatch("update book_info set state=1 where book_id="
					+ giveBookBean.getRelease_book());
			
			sm.addBatch("delete from book_give_info where release_book ="
					+ giveBookBean.getRelease_book());

			// ���
			sm.addBatch("insert into book_give" +
					"(release_user,obtain_user,release_book,"
					+ "release_msg,obtain_msg,generate_time) values("
					+ giveBookBean.getRelease_user()				
					+ ","
					+ giveBookBean.getRelease_book()					
					+ ",'"
					+ giveBookBean.getRelease_msg()
					+ "','"
					+ giveBookBean.getObtain_msg()
					+ "','"
					+ giveBookBean.getGenerate_time() + "')");

			// �ύ����
			result = sm.executeBatch();
		} catch (SQLException se) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
			throw new RuntimeException(se);
		} finally {
			try {

				if (sm != null) {
					sm.close();
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
	 * �� book_give_info�л�� ��������release_book�� obtain_book
	 * 
	 * @param release_book
	 * @return
	 */
/*	public ArrayList<Integer> getAllObtainBookId(int release_book) {
		ArrayList<Integer> allObtainBookId = new ArrayList<Integer>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// data.getJSONArray(key)

		try {

			conn = MySqlConnectPool.getConnection();
			// ���statement����

			pstmt = conn
					.prepareStatement("select obtain_book from book_give_info where release_book=?");
			pstmt.setInt(1, release_book);

			rs = pstmt.executeQuery();
			// �жϽ��
			while (rs.next()) {
				allObtainBookId.add(rs.getInt(1));
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
		return allObtainBookId;
	}
*/
	public ArrayList<Integer> getAllObtainUser(int release_Book,int release_user) {
		ArrayList<Integer> allObtainUser = new ArrayList<Integer>();

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			conn = MySqlConnectPool.getConnection();
			// ���statement����
			StringBuilder sqlString = new StringBuilder(
					"select obtain_user from book_give_info where obtain_book in(");
			StringBuilder sb = new StringBuilder();
			
			rs = pstmt.executeQuery();
			// �жϽ��
			while (rs.next()) {
				allObtainUser.add(rs.getInt(1));
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
		return allObtainUser;

	}
	
/**
 * book_give
 * @param book_Id
 * @return
 */
	public JSONObject retrivealBookOrder(int book_Id) {
		JSONObject jsonObject = new JSONObject();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			conn = MySqlConnectPool.getConnection();
			// ���statement����

			pstmt = conn
					.prepareStatement("select * from book_give where release_book=?");
			pstmt.setInt(1, book_Id);
			//pstmt.setInt(2, book_Id);

			rs = pstmt.executeQuery();
			// �жϽ��
			// һ����¼
			if (rs.next()) {
				jsonObject.put("give_id", rs.getInt(1));
				jsonObject.put("release_user", rs.getInt(2));
				jsonObject.put("obtain_user", rs.getInt(3));
				jsonObject.put("release_book", rs.getInt(4));
				jsonObject.put("release_msg", rs.getString(5));
				jsonObject.put("obtain_msg", rs.getString(6));
				jsonObject.put("generate_time", rs.getString(7));
				String temp = rs.getString(8);
				if (temp == null) {
					temp = " ";
				}
				jsonObject.put("rfinish_time", temp);
				temp = rs.getString(9);
				if (temp == null) {
					temp = " ";
				}
				jsonObject.put("ofinish_time", temp);
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

		return jsonObject;
	}

	/**
	 * ȷ�� �ջ� type 0 ���� release 1���� obtain ���� 1���ҵ��Է��Ƿ��Ѿ�ȷ���ջ� 2����Է�û�������
	 * 3����Է�����ת�Ƽ�¼
	 * 
	 * @param book_id
	 * @param type
	 * @return
	 */
	public boolean confirmOrder(int book_id, boolean type) {
		boolean otherConfirmed = getOtherConfirm(book_id, type);
		System.out.println("otherConfirmed:" + otherConfirmed);
		if (otherConfirmed) {
			// �Է�ȷ����
			int[] result = confirmOrderState1(book_id, type);
			for (int i = 0, len = result.length; i < len; i++) {
				if (result[0] != 1) {
					return false;
				}
			}
			return true;
		} else {
			// �Է�û��ȷ��
			int result = confirmOrderState2(book_id, type);
			if (result != 1) {
				return false;
			}
			return true;
		}
	}

	/**
	 * �Է�ȷ���� ������������ �Ȼ����Ϣ Ȼ����뵽Finish���� Ȼ��ɾ��order���� Ȼ�����book info����
	 * 
	 * @param book_id
	 * @param type
	 */
	private int[] confirmOrderState1(int book_id, boolean type) {
		// ����˶�����Ϣ
		JSONObject jsonObject = retrivealBookOrder(book_id, type);

		Connection conn = null;
		Statement sm = null;
		int[] result = null;
		try {
			conn = MySqlConnectPool.getConnection();
			sm = conn.createStatement();
			String nowTime = System.currentTimeMillis() + "";
			// ���
			if (type) {
				// release
				sm.addBatch("insert into book_give_finished(give_id,release_user,obtain_user,release_book,obtain_book,"
						+ "release_msg,obtain_msg,generate_time,rfinish_time,ofinish_time) values("
						+ jsonObject.getInt("give_id")
						+ ","
						+ jsonObject.getInt("release_user")
						+ ","
						+ jsonObject.getInt("obtain_user")
						+ ","
						+ jsonObject.getInt("release_book")
						+ ","
						+ jsonObject.getInt("obtain_book")
						+ ",'"
						+ jsonObject.getString("release_msg")
						+ "','"
						+ jsonObject.getString("obtain_msg")
						+ "','"
						+ jsonObject.getString("generate_time")
						+ "','"
						+ nowTime
						+ "','"
						+ jsonObject.getString("ofinish_time") + "')");
			} else {
				// obtain
				sm.addBatch("insert into book_give_finished(give_id,release_user,obtain_user,release_book,obtain_book,"
						+ "release_msg,obtain_msg,generate_time,rfinish_time,ofinish_time) values("
						+ jsonObject.getInt("give_id")
						+ ","
						+ jsonObject.getInt("release_user")
						+ ","
						+ jsonObject.getInt("obtain_user")
						+ ","
						+ jsonObject.getInt("release_book")
						+ ","
						+ jsonObject.getInt("obtain_book")
						+ ",'"
						+ jsonObject.getString("release_msg")
						+ "','"
						+ jsonObject.getString("obtain_msg")
						+ "','"
						+ jsonObject.getString("generate_time")
						+ "','"
						+ jsonObject.getString("rfinish_time")
						+ "','"
						+ nowTime + "')");

			}
			// ɾ��
			if (type) {
				sm.addBatch("delete from book_give where release_book="
						+ book_id);
			} else {
				sm.addBatch("delete from book_give where obtain_book="
						+ book_id);
			}
			// ����
			sm.addBatch("update book_info set state=2 where book_id="
					+ jsonObject.getInt("release_book"));

			sm.addBatch("update book_info set state=2 where book_id="
					+ jsonObject.getInt("obtain_book"));

			// �ύ����
			result = sm.executeBatch();
			System.out.println("ִ����");
		} catch (SQLException se) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
			throw new RuntimeException(se);
		} finally {
			try {

				if (sm != null) {
					sm.close();
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
	 * ���Ҷ�����¼ Ҫ���type��ʲô��˼
	 * 
	 * @param book_id
	 * @param type
	 * @return
	 */
	private JSONObject retrivealBookOrder(int book_id, boolean type) {
		JSONObject jsonObject = new JSONObject();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {

			conn = MySqlConnectPool.getConnection();
			// ���statement����
			if (type) {
				pstmt = conn
						.prepareStatement("select * from book_give where release_book=?");
			} else {
				pstmt = conn
						.prepareStatement("select * from book_give where obtain_book=?");
			}
			pstmt.setInt(1, book_id);

			rs = pstmt.executeQuery();
			// �жϽ��
			// һ����¼
			if (rs.next()) {
				jsonObject.put("give_id", rs.getInt(1));
				jsonObject.put("release_user", rs.getInt(2));
				jsonObject.put("obtain_user", rs.getInt(3));
				jsonObject.put("release_book", rs.getInt(4));
				//jsonObject.put("obtain_book", rs.getInt(5));
				jsonObject.put("release_msg", rs.getString(5));
				jsonObject.put("obtain_msg", rs.getString(6));
				jsonObject.put("generate_time", rs.getString(7));
				String temp = rs.getString(8);
				if (temp == null) {
					temp = " ";
				}
				jsonObject.put("rfinish_time", temp);
				temp = rs.getString(9);
				if (temp == null) {
					temp = " ";
				}
				jsonObject.put("ofinish_time", temp);
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

		return jsonObject;
	}

	/**
	 * �Է�û��ȷ��
	 * 
	 * @param book_id
	 * @param type
	 */
	private int confirmOrderState2(int book_id, boolean type) {
		System.out.println("confirmOrderState2");
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = 0;
		try {
			conn = MySqlConnectPool.getConnection();
			conn.setAutoCommit(false);
			// ����û�
			if (type) {
				// ע�� release
				pstmt = conn
						.prepareStatement("update book_give set rfinish_time=? where release_book=?");
			} else {
				pstmt = conn
						.prepareStatement("update book_give set ofinish_time=? where obtain_book=?");
			}

			pstmt.setString(1, System.currentTimeMillis() + "");
			pstmt.setInt(2, book_id);

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
	 * �Է������Ƿ�ȷ���ջ� ��������˶�ȷ���ջ��� ��������ͽ�������һ������ typeֻ�ܴ�0��1
	 * 
	 * @param book_id
	 * @param type
	 * @return
	 */
	private boolean getOtherConfirm(int book_id, boolean type) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			conn = MySqlConnectPool.getConnection();
			// ���statement����
			if (type) {
				// relase
				pstmt = conn
						.prepareStatement("select ofinish_time from book_give where release_book=?");

			} else {
				// obtain
				pstmt = conn
						.prepareStatement("select rfinish_time from book_give where obtain_book=?");
			}
			pstmt.setInt(1, book_id);

			rs = pstmt.executeQuery();
			// �жϽ��
			// һ����¼
			if (rs.next()) {
				String temp = rs.getString(1);
				if (temp != null) {
					return true;
				}
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
		return false;
	}

	public JSONObject retrivealBookOrderFinish(int book_Id) {
		JSONObject jsonObject = new JSONObject();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			conn = MySqlConnectPool.getConnection();
			// ���statement����

			pstmt = conn
					.prepareStatement("select * from book_give_finished where release_book=? or obtain_book=?");
			pstmt.setInt(1, book_Id);
			pstmt.setInt(2, book_Id);

			rs = pstmt.executeQuery();
			// �жϽ��
			// һ����¼
			if (rs.next()) {
				jsonObject.put("finish_id", rs.getInt(1));
				jsonObject.put("give_id", rs.getInt(2));
				jsonObject.put("release_user", rs.getInt(3));
				jsonObject.put("obtain_user", rs.getInt(4));
				jsonObject.put("release_book", rs.getInt(5));
				jsonObject.put("obtain_book", rs.getInt(6));
				jsonObject.put("release_msg", rs.getString(7));
				jsonObject.put("obtain_msg", rs.getString(8));
				jsonObject.put("generate_time", rs.getString(9));
				String temp = rs.getString(10);
				if (temp == null) {
					temp = " ";
				}
				jsonObject.put("rfinish_time", temp);
				temp = rs.getString(11);
				if (temp == null) {
					temp = " ";
				}
				jsonObject.put("ofinish_time", temp);
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

		return jsonObject;
	}
}
