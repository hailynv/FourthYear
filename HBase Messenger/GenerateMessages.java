import java.io.IOException; 
import java.lang.Math;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.hadoop.conf.Configuration; 
import org.apache.hadoop.hbase.HBaseConfiguration; 
import org.apache.hadoop.hbase.client.HTable; 
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes; 

public class GenerateMessages 
{

	public static String generateText()
	{
		String words = new String();
		
		char letters[] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
                'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
                'w', 'x', 'y', 'z'}; // character library
		
		int index, wordLength, randCharIdx;
		
		// generating 50 random words and adding them to a string
		for(index = 0; index < 50; index++)
		{
			// each word will be between 1 and 10 characters
			wordLength = (int) (Math.random() * 10) + 1;
			
			// generating a random word
			for(; wordLength > 0; wordLength--)
			{
				// picking a random index for the letters array
				randCharIdx = (int) ((Math.random() * 26));
				// adding a random letter to the current word
				words += letters[randCharIdx];
			}
			
			// adding a space between words
			words += " ";
		}
		
		return words;
	}
	
	public static void main(String[] args) throws IOException
	{

	    Configuration config = HBaseConfiguration.create();

	    HTable messagesTable = new HTable(config, "messages");
	    
	    int index, messageIndex, randomReceiver;
	    String senderEmail, receiverEmail, messageTitle, messageBody;
	    String[] dateTime;
	    LocalDateTime date;
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	    Put p;
	    
	    // 500 users, each sends 10 messages
	    for(index = 0; index < 500; index++)
	    {
			senderEmail = "user" + Integer.toString(index + 1) + "@email.com";
			
			for(messageIndex = 0; messageIndex < 10; messageIndex++)
			{
				// creating the date
				date = LocalDateTime.now();
				
				 // randomizing the date
				 date = date.plusDays((int) ((Math.random() * 30) + 1));
				 date = date.plusSeconds((int) ((Math.random() * 60) + 1));
				 date = date.plusMinutes((int) ((Math.random() * 60) + 1));
				
				// splitting the date and time up
				dateTime = date.format(formatter).split(" "); // array with the date [0] and time [1]
				
				// generating random message body
				messageBody = generateText();
				
				messageTitle = "Message Subject" + Integer.toString(index + 1);
				randomReceiver = (int) (Math.random() * 500) + 1;
				receiverEmail = "user" + Integer.toString(randomReceiver) + "@email.com";
				
				// row key = recipient email + date localdatetime object with randomization
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
	    
	    messagesTable.close();
	}

}
