package bean;

public class PosterBean {
	private int poster_id;

	private int user_id;

	private String topic;

	private String person;

	private String time;

	private String address;
	
	private int concern_num;

	private int accusation_num;

	private String generate_time;

	public int getposter_id() {
		return poster_id;
	}

	public void setposter_id(int poster_id) {
		this.poster_id = poster_id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getperson() {
		return person;
	}

	public void setperson(String person) {
		this.person = person;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getConcern_num() {
		return concern_num;
	}

	public void setConcern_num(int concern_num) {
		this.concern_num = concern_num;
	}

	public int getAccusation_num() {
		return accusation_num;
	}

	public void setAccusation_num(int accusation_num) {
		this.accusation_num = accusation_num;
	}

	public String getGenerate_time() {
		return generate_time;
	}

	public void setGenerate_time(String generate_time) {
		this.generate_time = generate_time;
	}

}
