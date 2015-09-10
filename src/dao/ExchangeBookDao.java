package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import util.MySqlConnectPool;
import bean.ExchangeBookBean;
import bean.ExchangeBookOrderBefore;
import java.sql.Statement;
import java.util.ArrayList;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ExchangeBookDao {
	private static int SIZE = 10;

	public int[] insertBookExchangeInfo(ExchangeBookOrderBefore exchangeBookInfo) {
		Connection conn = null;
		Statement sm = null;
		int[] result = null;
		try {
			conn = MySqlConnectPool.getConnection();
			// conn.setAutoCommit(false);

			sm = conn.createStatement();

			// sm.addBatch("update book_info set state=2 where book_id="
			// + exchangeBookInfo.getRelease_book());
			// // 更新
			// boolean sign = isReserved(exchangeBookInfo.getObtain_book());
			// if (sign) {
			// // 更新
			// sm.addBatch("update book_info set state=3 where book_id="
			// + exchangeBookInfo.getObtain_book());
			// } else {
			// // 更新
			// sm.addBatch("update book_info set state=1 where book_id="
			// + exchangeBookInfo.getObtain_book());
			// }

			// 添加
			sm.addBatch("insert into book_exchange_info(release_user,obtain_user,release_book,obtain_book,"
					+ "obtain_msg,generation_time,obtain_bookname,obtain_picture,release_bookname) values("
					+ exchangeBookInfo.getRelease_user()
					+ ","
					+ exchangeBookInfo.getObtain_user()
					+ ","
					+ exchangeBookInfo.getRelease_book()
					+ ","
					+ exchangeBookInfo.getObtain_book()
					+ ",'"
					+ exchangeBookInfo.getObtain_msg()
					+ "','"
					+ exchangeBookInfo.getGenerate_time()
					+ "','"
					+ exchangeBookInfo.getObtain_bookname()
					+ "','"
					+ exchangeBookInfo.getObtain_picture()
					+ "','"
					+ exchangeBookInfo.getRelease_bookname() + "')");

			// 提交更改
			result = sm.executeBatch();
			System.out.println("执行了");
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
	 * 是否作为release_book 被别人预定了
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
			// 获得statement对象
			pstmt = conn
					.prepareStatement("select * from book_exchange_info where release_book=?");
			pstmt.setInt(1, release_book);
			rs = pstmt.executeQuery();
			// 判断结果
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
			// 获得statement对象
			if (type == 0) {
				// 第一次查询

				pstmt = conn
						.prepareStatement("select * from book_exchange_info where release_book=? order by info_id desc limit ?");
				pstmt.setInt(1, book_id);
				pstmt.setInt(2, SIZE);

			} else if (type == 1) {
				pstmt = conn
						.prepareStatement("select * from book_exchange_info where info_id <? and release_book=? order by info_id desc limit ?");
				pstmt.setInt(1, infoId);
				pstmt.setInt(2, book_id);
				pstmt.setInt(3, SIZE);
			} else if (type == 2) {
				pstmt = conn
						.prepareStatement("select * from book_exchange_info where info_id >? and release_book=? order by info_id desc limit ?");
				pstmt.setInt(1, infoId);
				pstmt.setInt(2, book_id);
				pstmt.setInt(3, SIZE);
			}
			rs = pstmt.executeQuery();
			// 判断结果
			while (rs.next()) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("info_id", rs.getInt(1));
				jsonObject.put("release_user", rs.getInt(2));
				jsonObject.put("obtain_user", rs.getInt(3));
				jsonObject.put("release_book", rs.getInt(4));
				jsonObject.put("obtain_book", rs.getInt(5));
				jsonObject.put("obtain_msg", rs.getString(6));
				jsonObject.put("generation_time", rs.getString(7));
				jsonObject.put("obtain_bookname", rs.getString(8));
				jsonObject.put("obtain_picture", rs.getString(9));
				jsonObject.put("release_bookname", rs.getString(10));
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
		System.out.println("结果是:" + jsonArray.size());
		return jsonArray;
	}

	/**
	 * 插入订单
	 * 
	 * @param exchangeBookBean
	 */
	public int[] insertExchangeBookOrder(ExchangeBookBean exchangeBookBean,
			ArrayList<Integer> allObtainBookId) {
		System.out.println("insertExchangeBookOrder="
				+ exchangeBookBean.getRelease_book() + ","
				+ exchangeBookBean.getObtain_book());
		Connection conn = null;
		Statement sm = null;
		int[] result = null;
		// Map<Integer,Integer> flag= new HashMap<Integer,Integer>();
		try {
			conn = MySqlConnectPool.getConnection();

			sm = conn.createStatement();
			// 更新
			// sm.addBatch("update book_info set state=1 where book_id="
			// + exchangeBookBean.getRelease_book());
			// 更新
			// sm.addBatch("update book_info set state=4 where book_id="
			// + exchangeBookBean.getRelease_book());
			sm.addBatch("update book_info set state=1 where book_id="
					+ exchangeBookBean.getRelease_book());
			// 更新 Obtain_book家族 注意

			// for (int temp_ObtainBookId : allObtainBookId) {
			// sm.addBatch("update book_info set state=0 where book_id="
			// + temp_ObtainBookId);
			// flag.put(temp_ObtainBookId, 0);

			// }
			// 注意
			sm.addBatch("update book_info set state=1 where book_id="
					+ exchangeBookBean.getObtain_book());
			// flag.put(exchangeBookBean.getObtain_book(),1);

			// 删除

			sm.addBatch("delete from book_exchange_info where release_book ="
					+ exchangeBookBean.getRelease_book());

			sm.addBatch("delete from book_exchange_info where release_book ="
					+ exchangeBookBean.getObtain_book());
			sm.addBatch("delete from book_exchange_info where obtain_book ="
					+ exchangeBookBean.getRelease_book());
			sm.addBatch("delete from book_exchange_info where obtain_book ="
					+ exchangeBookBean.getObtain_book());

			// 添加
			sm.addBatch("insert into book_exchange(release_user,obtain_user,release_book,obtain_book,"
					+ "release_msg,obtain_msg,generate_time) values("
					+ exchangeBookBean.getRelease_user()
					+ ","
					+ exchangeBookBean.getObtain_user()
					+ ","
					+ exchangeBookBean.getRelease_book()
					+ ","
					+ exchangeBookBean.getObtain_book()
					+ ",'"
					+ exchangeBookBean.getRelease_msg()
					+ "','"
					+ exchangeBookBean.getObtain_msg()
					+ "','"
					+ exchangeBookBean.getGenerate_time() + "')");

			// 提交更改
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
	 * 从 book_exchange_info中获得 所有属于release_book的 obtain_book
	 * 
	 * @param release_book
	 * @return
	 */
	public ArrayList<Integer> getAllObtainBookId(int release_book) {
		ArrayList<Integer> allObtainBookId = new ArrayList<Integer>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// data.getJSONArray(key)

		try {

			conn = MySqlConnectPool.getConnection();
			// 获得statement对象

			pstmt = conn
					.prepareStatement("select obtain_book from book_exchange_info where release_book=?");
			pstmt.setInt(1, release_book);

			rs = pstmt.executeQuery();
			// 判断结果
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

	public ArrayList<Integer> getAllObtainUser(
			ArrayList<Integer> allObtainBookId) {
		ArrayList<Integer> allObtainUser = new ArrayList<Integer>();

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			conn = MySqlConnectPool.getConnection();
			// 获得statement对象
			StringBuilder sqlString = new StringBuilder(
					"select obtain_user from book_exchange_info where obtain_book in(");
			StringBuilder sb = new StringBuilder();
			for (int i = 0, size = allObtainBookId.size(); i < size; i++) {
				sb.append("?,");

			}
			sqlString.append(sb.substring(0, sb.length() - 1));
			sqlString.append(")");

			pstmt = conn.prepareStatement(sqlString.toString());
			for (int i = 0, size = allObtainBookId.size(); i < size; i++) {
				pstmt.setInt(i, allObtainBookId.get(i));
			}
			rs = pstmt.executeQuery();
			// 判断结果
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

	public JSONObject retrivealBookOrder(int book_Id) {
		JSONObject jsonObject = new JSONObject();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			conn = MySqlConnectPool.getConnection();
			// 获得statement对象

			pstmt = conn
					.prepareStatement("select * from book_exchange where release_book=? or obtain_book=?");
			pstmt.setInt(1, book_Id);
			pstmt.setInt(2, book_Id);

			rs = pstmt.executeQuery();
			// 判断结果
			// 一条记录
			if (rs.next()) {
				jsonObject.put("exchange_id", rs.getInt(1));
				jsonObject.put("release_user", rs.getInt(2));
				jsonObject.put("obtain_user", rs.getInt(3));
				jsonObject.put("release_book", rs.getInt(4));
				jsonObject.put("obtain_book", rs.getInt(5));
				jsonObject.put("release_msg", rs.getString(6));
				jsonObject.put("obtain_msg", rs.getString(7));
				jsonObject.put("generate_time", rs.getString(8));
				String temp = rs.getString(9);
				if (temp == null) {
					temp = " ";
				}
				jsonObject.put("rfinish_time", temp);
				temp = rs.getString(10);
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
	 * 确认 收货 type 0 代表 release 1代表 obtain 流程 1查找到对方是否已经确认收货 2如果对方没有则更新
	 * 3如果对方有则转移记录
	 * 
	 * @param book_id
	 * @param type
	 * @return
	 */
	public boolean confirmOrder(int book_id, boolean type) {
		boolean otherConfirmed = getOtherConfirm(book_id, type);
		System.out.println("otherConfirmed:" + otherConfirmed);
		if (otherConfirmed) {
			// 对方确认了
			int[] result = confirmOrderState1(book_id, type);
			for (int i = 0, len = result.length; i < len; i++) {
				if (result[0] != 1) {
					return false;
				}
			}
			return true;
		} else {
			// 对方没有确认
			int result = confirmOrderState2(book_id, type);
			if (result != 1) {
				return false;
			}
			return true;
		}
	}

	/**
	 * 对方确认了 流程是这样的 先获得信息 然后插入到Finish表中 然后删除order表中 然后更新book info表中
	 * 
	 * @param book_id
	 * @param type
	 */
	private int[] confirmOrderState1(int book_id, boolean type) {
		// 获得了订单信息
		JSONObject jsonObject = retrivealBookOrder(book_id, type);

		Connection conn = null;
		Statement sm = null;
		int[] result = null;
		try {
			conn = MySqlConnectPool.getConnection();
			sm = conn.createStatement();
			String nowTime = System.currentTimeMillis() + "";
			// 添加
			if (type) {
				// release
				sm.addBatch("insert into book_exchange_finished(exchange_id,release_user,obtain_user,release_book,obtain_book,"
						+ "release_msg,obtain_msg,generate_time,rfinish_time,ofinish_time) values("
						+ jsonObject.getInt("exchange_id")
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
				sm.addBatch("insert into book_exchange_finished(exchange_id,release_user,obtain_user,release_book,obtain_book,"
						+ "release_msg,obtain_msg,generate_time,rfinish_time,ofinish_time) values("
						+ jsonObject.getInt("exchange_id")
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
			// 删除
			if (type) {
				sm.addBatch("delete from book_exchange where release_book="
						+ book_id);
			} else {
				sm.addBatch("delete from book_exchange where obtain_book="
						+ book_id);
			}
			// 更新
			sm.addBatch("update book_info set state=2 where book_id="
					+ jsonObject.getInt("release_book"));

			sm.addBatch("update book_info set state=2 where book_id="
					+ jsonObject.getInt("obtain_book"));

			// 提交更改
			result = sm.executeBatch();
			System.out.println("执行了");
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
	 * 查找订单记录 要清楚type是什么意思
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
			// 获得statement对象
			if (type) {
				pstmt = conn
						.prepareStatement("select * from book_exchange where release_book=?");
			} else {
				pstmt = conn
						.prepareStatement("select * from book_exchange where obtain_book=?");
			}
			pstmt.setInt(1, book_id);

			rs = pstmt.executeQuery();
			// 判断结果
			// 一条记录
			if (rs.next()) {
				jsonObject.put("exchange_id", rs.getInt(1));
				jsonObject.put("release_user", rs.getInt(2));
				jsonObject.put("obtain_user", rs.getInt(3));
				jsonObject.put("release_book", rs.getInt(4));
				jsonObject.put("obtain_book", rs.getInt(5));
				jsonObject.put("release_msg", rs.getString(6));
				jsonObject.put("obtain_msg", rs.getString(7));
				jsonObject.put("generate_time", rs.getString(8));
				String temp = rs.getString(9);
				if (temp == null) {
					temp = " ";
				}
				jsonObject.put("rfinish_time", temp);
				temp = rs.getString(10);
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
	 * 对方没有确认
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
			// 添加用户
			if (type) {
				// 注意 release
				pstmt = conn
						.prepareStatement("update book_exchange set rfinish_time=? where release_book=?");
			} else {
				pstmt = conn
						.prepareStatement("update book_exchange set ofinish_time=? where obtain_book=?");
			}

			pstmt.setString(1, System.currentTimeMillis() + "");
			pstmt.setInt(2, book_id);

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
	 * 对方订单是否确认收货 如果两个人都确认收货了 这个订单就进入另外一个表中 type只能传0和1
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
			// 获得statement对象
			if (type) {
				// relase
				pstmt = conn
						.prepareStatement("select ofinish_time from book_exchange where release_book=?");

			} else {
				// obtain
				pstmt = conn
						.prepareStatement("select rfinish_time from book_exchange where obtain_book=?");
			}
			pstmt.setInt(1, book_id);

			rs = pstmt.executeQuery();
			// 判断结果
			// 一条记录
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
			// 获得statement对象

			pstmt = conn
					.prepareStatement("select * from book_exchange_finished where release_book=? or obtain_book=?");
			pstmt.setInt(1, book_Id);
			pstmt.setInt(2, book_Id);

			rs = pstmt.executeQuery();
			// 判断结果
			// 一条记录
			if (rs.next()) {
				jsonObject.put("finish_id", rs.getInt(1));
				jsonObject.put("exchange_id", rs.getInt(2));
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
