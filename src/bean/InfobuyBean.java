package bean;

public class InfobuyBean {
	private int info_id;

	private int user_id;

	private String bookname;

	private String price;

	//private String time;

	private String address;
	
	private int concern_num;

	private int accusation_num;

	private String generate_time;

	public int getinfo_id() {
		return info_id;
	}

	public void setinfo_id(int info_id) {
		this.info_id = info_id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getbookname() {
		return bookname;
	}

	public void setbookname(String bookname) {
		this.bookname = bookname;
	}

	public String getprice() {
		return price;
	}

	public void setprice(String price) {
		this.price = price;
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
