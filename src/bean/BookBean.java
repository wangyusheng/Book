package bean;

public class BookBean {

	private int book_Id;

	private int user_Id;

	private String book_Name;

	private String author_name;

	private String abstract_content;

	private String time_Release;

	private String transcation;

	private String new_Old;

	private String picture_Path;

	private int priority;

	private int rating;

	private String generation_time;

	private String interest;

	private int state;

	public BookBean() {

	}

	public int getBook_Id() {
		return book_Id;
	}

	public void setBook_Id(int bookId) {
		book_Id = bookId;
	}

	public int getUser_Id() {
		return user_Id;
	}

	public void setUser_Id(int userId) {
		user_Id = userId;
	}

	public String getBook_Name() {
		return book_Name;
	}

	public void setBook_Name(String bookName) {
		book_Name = bookName;
	}

	public String getAbstract_content() {
		return abstract_content;
	}

	public void setAbstract_content(String abstractContent) {
		abstract_content = abstractContent;
	}

	public String getTime_Release() {
		return time_Release;
	}

	public void setTime_Release(String timeRelease) {
		time_Release = timeRelease;
	}

	public String getTranscation() {
		return transcation;
	}

	public void setTranscation(String transcation) {
		this.transcation = transcation;
	}

	public String getNew_Old() {
		return new_Old;
	}

	public void setNew_Old(String newOld) {
		new_Old = newOld;
	}

	public String getPicture_Path() {
		return picture_Path;
	}

	public void setPicture_Path(String picturePath) {
		picture_Path = picturePath;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getGeneration_time() {
		return generation_time;
	}

	public void setGeneration_time(String generationTime) {
		generation_time = generationTime;
	}

	public String getAuthor_name() {
		return author_name;
	}

	public void setAuthor_name(String authorName) {
		author_name = authorName;
	}

	public String getInterest() {
		return interest;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	
	
}
