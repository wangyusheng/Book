package bean;

public class ExchangeBookBean {

	private int exchange_id;

	private int release_user;

	private int obtain_user;

	private int release_book;

	private int obtain_book;

	private String release_msg;

	private String obtain_msg;

	private String generate_time;

	private String finish_time;

	public int getExchange_id() {
		return exchange_id;
	}

	public void setExchange_id(int exchangeId) {
		exchange_id = exchangeId;
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

	public int getObtain_book() {
		return obtain_book;
	}

	public void setObtain_book(int obtainBook) {
		obtain_book = obtainBook;
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
