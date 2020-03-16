
public class Message 
{
	String from, to, date, time, mTitle, mBody;
	
	public Message(String from, String to, String date, String time, String mTitle, String mBody)
	{
		this.from = from;
		this.date = date;
		this.time = time;
		this.mTitle = mTitle;
		this.mBody = mBody;
		this.to = to;
	}

	public String getFrom() {
		return from;
	}
	
	public String getTo()
	{
		return to;
	}

	public String getDate() {
		return date;
	}


	public String getTime() {
		return time;
	}

	public String getmTitle() {
		return mTitle;
	}

	public String getmBody() {
		return mBody;
	}
	
	public String toString()
	{
		return from + "  " + date + "  " + time + "  " + mTitle;
	}

	
	

}
