package bean;

/**
 * 用户类
 * 
 * @author Administrator
 * 
 */
public class UserBean {
	private int user_id;
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 昵称
	 */
	private String email;
	/**
	 * 头像地址
	 */
	private String nickname;

	private String age;

	private String sex;

	private String vocation;

	private String interest;

	private String potraitpath;

	private String generation_time;

	private String im_account;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPotraitpath() {
		return potraitpath;
	}

	public void setPotraitpath(String potraitpath) {
		this.potraitpath = potraitpath;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int userId) {
		user_id = userId;
	}

	public String getAge() {
		return age;
	}

	public String getVocation() {
		return vocation;
	}

	public String getInterest() {
		return interest;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public void setVocation(String vocation) {
		this.vocation = vocation;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}

	public String getGeneration_time() {
		return generation_time;
	}

	public void setGeneration_time(String generation_time) {
		this.generation_time = generation_time;
	}

	public String getIm_account() {
		return im_account;
	}

	public void setIm_account(String im_account) {
		this.im_account = im_account;
	}
 
	
}
