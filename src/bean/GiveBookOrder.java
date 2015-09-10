package bean;

public class GiveBookOrder {
	private int give_id;

	private int release_user;

	private int obtain_user;

	private int release_book;

	//private int obtain_book;

	private String release_msg;

	private String obtain_msg;

	private String generate_time;

	private String rfinish_time;

	private String ofinish_time;

	public GiveBookOrder() {

	}

	public int getgive_id() {
		return give_id;
	}

	public void setgive_id(int give_id) {
		this.give_id = give_id;
	}

	public int getRelease_user() {
		return release_user;
	}

	public void setRelease_user(int release_user) {
		this.release_user = release_user;
	}

	public int getObtain_user() {
		return obtain_user;
	}

	public void setObtain_user(int obtain_user) {
		this.obtain_user = obtain_user;
	}

	public int getRelease_book() {
		return release_book;
	}

	public void setRelease_book(int release_book) {
		this.release_book = release_book;
	}

	
	public String getRelease_msg() {
		return release_msg;
	}

	public void setRelease_msg(String release_msg) {
		this.release_msg = release_msg;
	}

	public String getObtain_msg() {
		return obtain_msg;
	}

	public void setObtain_msg(String obtain_msg) {
		this.obtain_msg = obtain_msg;
	}

	public String getGenerate_time() {
		return generate_time;
	}

	public void setGenerate_time(String generate_time) {
		this.generate_time = generate_time;
	}

	public String getRfinish_time() {
		return rfinish_time;
	}

	public void setRfinish_time(String rfinish_time) {
		if (rfinish_time == null) {
			rfinish_time = " ";
		}
		this.rfinish_time = rfinish_time;
	}

	public String getOfinish_time() {
		return ofinish_time;
	}

	public void setOfinish_time(String ofinish_time) {
		if (ofinish_time == null) {
			ofinish_time = " ";
		}
		this.ofinish_time = ofinish_time;

	}

}
