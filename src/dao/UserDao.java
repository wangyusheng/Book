package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import bean.UserBean;
import util.MySqlConnectPool;

public class UserDao {
	/**
	 * 根据指定的用户属性字段及值查询该用户是否存在
	 * 
	 * @param field
	 *            要查询的字段
	 * @param value
	 *            相应的字段值
	 * @return 返回查询结果
	 */
	public boolean findUserByUserName(String value) {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		try {

			conn = MySqlConnectPool.getConnection();
			// 获得statement对象
			pstmt = conn
					.prepareStatement("select * from user_info where user_name"
							+ "=?");
			pstmt.setString(1, value);
			rs = pstmt.executeQuery();
			// 判断结果
			if (!rs.next()) {
				return false;// 该用户不存在
			}
			// 该用户存在
			return true;

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
		return true;
	}

	/**
	 * 添加用户
	 * 
	 * @param customer
	 *            要添加的用户对象
	 * @return 返回添加结果，1成功，0失败
	 */
	public int addUser(UserBean user) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = 0;
		try {
			conn = MySqlConnectPool.getConnection();
			conn.setAutoCommit(false);
			// 添加用户
			pstmt = conn
					.prepareStatement("insert into user_info(user_name,user_password,user_email,nickname,age,sex,vocation,interest,portraitpath,generation_time,im_account) values(?,?,?,?,?,?,?,?,?,?,?)");
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, user.getEmail());
			pstmt.setString(4, user.getNickname());
			pstmt.setString(5, user.getAge());
			pstmt.setString(6, user.getSex());
			pstmt.setString(7, user.getVocation());
			pstmt.setString(8, user.getInterest());
			pstmt.setString(9, user.getPotraitpath());
			pstmt.setString(10, user.getGeneration_time());
			pstmt.setString(11, user.getIm_account());

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
	 * 验证用户
	 * 
	 * @return
	 */
	public UserBean validateUser(String userName, String password) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// UserBean user = null;
		try {
			// 获得statement对象

			conn = MySqlConnectPool.getConnection();
			pstmt = conn
					.prepareStatement("select user_id,sex from user_info where user_name=? and user_password=?");
			pstmt.setString(1, userName);
			pstmt.setString(2, password);
			rs = pstmt.executeQuery();
			// 判断结果
			if (rs.next()) {
				UserBean userBean = new UserBean();
				userBean.setUser_id(rs.getInt(1));
				userBean.setSex(rs.getString(2));
				return userBean;
			}
			// 该用户存在
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
		// 默认返回不存在
		return null;
	}

	/**
	 * 验证用户
	 * 
	 * @return
	 */
	public String getPortraitpath(int userId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// UserBean user = null;
		try {
			// 获得statement对象

			conn = MySqlConnectPool.getConnection();
			pstmt = conn
					.prepareStatement("select portraitpath from user_info where user_id=?");
			pstmt.setInt(1, userId);
			rs = pstmt.executeQuery();
			// 判断结果
			if (rs.next()) {
				// 存在结果
				return rs.getString(1);
			}
			// 头像不存在
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
		// 默认不存在
		return null;
	}

	public static void main(String args[]) {
		// UserBean user = new UserBean();
		// user.setUserName("asd8707");
		// user.setPassword("asdzxyu");
		// user.setNickName("笑傲江湖");
		// user.setPortraitPath(null);
		// user.setGender(true);
		// int result = new UserDao().addUser(user);
		// System.out.println(result);

		System.out.println(new UserDao().findUserByUserName("asd8708"));
	}

	// TODO
	// /**
	// * 修改用户信息
	// *
	// * @param customer
	// * 要完善用户信息的用户对象
	// * @return 返回修改结果，1成功，0失败
	// */
	// public int updateUser(UserBean user) {
	// Connection conn = MySqlConnectPool.getConnection();
	// PreparedStatement pstmt = null;
	// ResultSet rs = null;
	// int result = 0;
	// try {
	// conn.setAutoCommit(false);
	// pstmt = conn
	// .prepareStatement("update user set custname=?,gender=?,address=? where email=?");
	// pstmt.setString(1, user.getCustname());
	// pstmt.setString(2, user.getGender());
	// pstmt.setString(3, user.getAddress());
	// pstmt.setString(4, user.getEmail());
	// // 执行修改
	// result = pstmt.executeUpdate();
	//
	// conn.commit();
	// } catch (SQLException se) {
	// try {
	// conn.rollback();
	// } catch (SQLException e) {
	// throw new RuntimeException(e);
	// }
	// throw new RuntimeException(se);
	// } finally {
	// try {
	// if (rs != null) {
	// rs.close();
	// }
	// if (pstmt != null) {
	// pstmt.close();
	// }
	// if (null != conn) {
	// conn.close();
	// }
	// } catch (SQLException e) {
	// throw new RuntimeException(e);
	// }
	// }
	// return result;
	// }

	public int updatePortraitPath(int userId, String filePath) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = 0;
		try {
			conn = MySqlConnectPool.getConnection();
			conn.setAutoCommit(false);
			// 添加用户
			pstmt = conn
					.prepareStatement("update user_info set portraitpath=? where user_id=?");
			pstmt.setString(1, filePath);
			pstmt.setInt(2, userId);

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

	public void updateIMAccount(String username, String im_Account) {

	}
}
