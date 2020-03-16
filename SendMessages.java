import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import org.apache.hadoop.conf.Configuration; 
import org.apache.hadoop.hbase.HBaseConfiguration; 
import org.apache.hadoop.hbase.client.HTable; 
import org.apache.hadoop.hbase.client.Put; 
import org.apache.hadoop.hbase.util.Bytes; 

public class SendMessages {

	public static void main(String sEmail, String rEmail, String mTitle, String mBody) throws IOException
	{
	    Configuration config = HBaseConfiguration.create();

	    HTable messagesTable = new HTable(config, "messages");
	    
	    String senderEmail, receiverEmail, messageTitle, messageBody;
	    String[] dateTime;
	    LocalDateTime date;
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	    Put p;
	    
	    senderEmail = sEmail;
	    receiverEmail = rEmail;
	    messageTitle = mTitle;
	    messageBody = mBody;
	    
	    date = LocalDateTime.now();
	    dateTime = date.format(formatter).split(" "); // array with the date [0] and time [1]
	    
	    // row key = recipient email + date localdatetime object
	    p = new Put(Bytes.toBytes(receiverEmail + " " + date));
		p.add(Bytes.toBytes("message"), Bytes.toBytes("from"), Bytes.toBytes(senderEmail));
		p.add(Bytes.toBytes("message"), Bytes.toBytes("to"), Bytes.toBytes(receiverEmail));
		p.add(Bytes.toBytes("message"), Bytes.toBytes("date"), Bytes.toBytes(dateTime[0]));
		p.add(Bytes.toBytes("message"), Bytes.toBytes("time"), Bytes.toBytes(dateTime[1]));
		p.add(Bytes.toBytes("message"), Bytes.toBytes("messageTitle"), Bytes.toBytes(messageTitle));
		p.add(Bytes.toBytes("message"), Bytes.toBytes("messageBody"), Bytes.toBytes(messageBody));
		messagesTable.put(p);
		
	}

}
