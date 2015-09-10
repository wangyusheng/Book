package bean;

public class GiveBookBean {
	private int give_id;

	private int release_user;

	private int obtain_user;

	private int release_book;

	//private int obtain_book;

	private String release_msg;

	private String obtain_msg;

	private String generate_time;

	private String finish_time;

	public int getgive_id() {
		return give_id;
	}

	public void setgive_id(int giveId) {
		give_id = giveId;
	}

	public int getRelease_user() {
		return release_user;
	}

	public void setRelease_user(int releaseUser) {
		release_user = releaseUser;
	}

	public int getObtain_user() {
		return obtain_user;
	}

	public void setObtain_user(int obtainUser) {
		obtain_user = obtainUser;
	}

	public int getRelease_book() {
		return release_book;
	}

	public void setRelease_book(int releaseBook) {
		release_book = releaseBook;
	}

	

	public String getRelease_msg() {
		return release_msg;
	}

	public void setRelease_msg(String releaseMsg) {
		release_msg = releaseMsg;
	}

	public String getObtain_msg() {
		return obtain_msg;
	}

	public void setObtain_msg(String obtainMsg) {
		obtain_msg = obtainMsg;
	}

	public String getGenerate_time() {
		return generate_time;
	}

	public void setGenerate_time(String generateTime) {
		generate_time = generateTime;
	}

	public String getFinish_time() {
		return finish_time;
	}

	public void setFinish_time(String finishTime) {
		finish_time = finishTime;
	}


}
