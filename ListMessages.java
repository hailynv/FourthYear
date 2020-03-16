import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration; 
import org.apache.hadoop.hbase.HBaseConfiguration; 
import org.apache.hadoop.hbase.client.HTable; 
import org.apache.hadoop.hbase.client.Result; 
import org.apache.hadoop.hbase.client.ResultScanner; 
import org.apache.hadoop.hbase.client.Scan; 
import org.apache.hadoop.hbase.util.Bytes;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

public class ListMessages {

	public static ArrayList<Message> main(String userEmail) throws IOException
	{
	    Configuration config = HBaseConfiguration.create();

	    HTable messagesTable = new HTable(config, "messages");
	    
	    int stopUser;
	    String email = userEmail;
	    String from, to, date, time, messageBody, messageTitle;
	    ArrayList<Message> messages = new ArrayList<Message>();
	    byte[] fromBytes, toBytes, dateBytes, timeBytes, messageBodyBytes, messageTitleBytes;
	    Scan s = new Scan();
	    ResultScanner scanner = messagesTable.getScanner(s);
	    
	    s.addFamily(Bytes.toBytes("message"));
	    s.setStartRow(Bytes.toBytes(email));
	    // stop scanning at the email after the test email
	    stopUser = Integer.parseInt(email.replaceAll("[^\\d]", "")) + 1;
	    s.setStopRow(Bytes.toBytes("user" + stopUser + "@email.com"));
	   
	    try 
	    {
	    	for(Result r : scanner)
	    	{
	    		// getting the bytes from each column in the message family
	    		fromBytes = r.getValue(Bytes.toBytes("message"), Bytes.toBytes("from"));
	    		toBytes = r.getValue(Bytes.toBytes("message"), Bytes.toBytes("to"));
	    		dateBytes = r.getValue(Bytes.toBytes("message"), Bytes.toBytes("date"));
	    		timeBytes = r.getValue(Bytes.toBytes("message"), Bytes.toBytes("time"));
	    		messageTitleBytes = r.getValue(Bytes.toBytes("message"), Bytes.toBytes("messageTitle"));
	    		messageBodyBytes = r.getValue(Bytes.toBytes("message"), Bytes.toBytes("messageBody"));
	    		
	    		// converting the bytes to strings
	    		from = Bytes.toString(fromBytes);
	    		to = Bytes.toString(toBytes);
	    		date = Bytes.toString(dateBytes);
	    		time = Bytes.toString(timeBytes);
	    		messageTitle = Bytes.toString(messageTitleBytes);
	    		messageBody = Bytes.toString(messageBodyBytes);
	    		
	    		// it's supposed to be a partial scan as set up above
	    		// but it doesn't work :-/
	    		// so we do a whole scan and let this logic add the appropriate messages
	    		if(to.contentEquals(userEmail))
	    		{
		    		Message usrMessage = new Message(from, to, date, time, messageTitle, messageBody);
			    	messages.add(usrMessage);
	    		}
	    		
	    	}
	    }
	    finally 
	    {
	    	scanner.close(); // closing resources
	    }
	    
	    messagesTable.close();
	    return messages;

	}

}
