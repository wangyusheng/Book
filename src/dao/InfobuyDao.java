package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import util.MySqlConnectPool;
import bean.InfobuyBean;

public class InfobuyDao {
	private static int SIZE = 10;

	/**
	 * 添加俱乐部信息
	 * 
	 * @param InfobuyBean
	 * @return
	 */
	public int insert(InfobuyBean InfobuyBean) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = 0;
		try {
			conn = MySqlConnectPool.getConnection();
			conn.setAutoCommit(false);
			// 添加用户
			pstmt = conn
					.prepareStatement("insert into infobuy_info(user_id,book_name,book_price,"
							+ "address,generate_time) values(?,?,?,?,?,?)");

			pstmt.setInt(1, InfobuyBean.getUser_id());
			pstmt.setString(2, InfobuyBean.getbookname());
			pstmt.setString(3, InfobuyBean.getprice());
			pstmt.setString(4, InfobuyBean.getAddress());
			pstmt.setString(5, InfobuyBean.getGenerate_time());
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

	public JSONArray retrievalInfobuy(int InfobuyId, int type) {
		System.out.println("book_id=" + InfobuyId + "," + "type=" + type);
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
						.prepareStatement("select * from Infobuy_info where state=0 and type=0 order by Infobuy_id desc limit ?");
				pstmt.setInt(1, SIZE);

			} else if (type == 1) {
				pstmt = conn
						.prepareStatement("select * from Infobuy_info where Infobuy_id <? and state=0 and type=1 order by Infobuy_id desc limit ?");
				pstmt.setInt(1, InfobuyId);
				pstmt.setInt(2, SIZE);
			} else if (type == 2) {
				pstmt = conn
						.prepareStatement("select * from Infobuy_info where Infobuy_id >? and state=0  and type=2 order by Infobuy_id desc limit ?");
				pstmt.setInt(1, InfobuyId);
				pstmt.setInt(2, SIZE);
			}
			rs = pstmt.executeQuery();
			// 判断结果
			while (rs.next()) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("Infobuy_id", rs.getInt(1));
				jsonObject.put("user_id", rs.getInt(2));
				jsonObject.put("book_name", rs.getString(3));
				jsonObject.put("book_price", rs.getString(4));
				jsonObject.put("address", rs.getString(5));
				jsonObject.put("concern_num", rs.getInt(6));
				jsonObject.put("accusation_num", rs.getInt(7));
				jsonObject.put("generate_time", rs.getString(8));
				jsonObject.put("state", rs.getInt(9));
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

}
