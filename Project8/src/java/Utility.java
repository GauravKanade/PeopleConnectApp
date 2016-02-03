
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author Gaurav Kanade
 */
public class Utility {

    public static String connection = "jdbc:mysql://localhost:3306/Project_8";
    public static String username = "root";
    public static String password = "abcd";

    static String getDateFormat(String timeStamp) {
        String finaltime = "";
        String year = timeStamp.substring(0, 4);
        int m = Integer.parseInt(timeStamp.substring(5, 7));
        int d = Integer.parseInt(timeStamp.substring(8, 10));
        int hour = Integer.parseInt(timeStamp.substring(11, 13));
        String min = timeStamp.substring(14, 16);
        String time = "";
        if (hour == 12) {
            time = "12" + ":" + min + "pm";
        } else if (hour == 0) {
            time = "12" + ":" + min + "am";
        } else if (hour > 12) {
            time = (hour - 12) + ":" + min + "pm";
        } else {
            time = hour + ":" + min + "am";
        }
        String month = "";
        switch (m) {
            case 1:
                month = "Jan";
                break;
            case 2:
                month = "Feb";
                break;
            case 3:
                month = "Mar";
                break;
            case 4:
                month = "Apr";
                break;
            case 5:
                month = "May";
                break;
            case 6:
                month = "Jun";
                break;
            case 7:
                month = "Jul";
                break;
            case 8:
                month = "Aug";
                break;
            case 9:
                month = "Sep";
                break;
            case 10:
                month = "Oct";
                break;
            case 11:
                month = "Nov";
                break;
            case 12:
                month = "Dec";
                break;
        }
        finaltime = d + " " + month + "," + year + " " + time;
        return finaltime;
    }

    public static void sendtosingle(
            String to,
            String subject,
            String body) {
        try {

            body = "<font face='Calibri' size='5' color='#666676'><b>People Connect</b><br><br><font size='3' color='#444454'>" + body;
            File f = new File("C:\\PeopleConnectConf\\conf.txt");
            BufferedReader br = new BufferedReader(new FileReader(f));
            String host = br.readLine();
            int port = Integer.parseInt(br.readLine());
            String password = br.readLine();
            String username = br.readLine();
            
            br.close();
            System.out.println("GMAIL "+username+"\nPassword: "+password);
            Session session = Session.getInstance(System.getProperties(), null);
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(username));
            Address[] toAddresses = InternetAddress.parse(to);
            msg.addRecipients(Message.RecipientType.TO, toAddresses);
            msg.setSubject(subject);
            msg.setContent(body, "text/html");
            Transport transport = session.getTransport("smtps");
            transport.connect(host, port, username, password);
            transport.sendMessage(msg, toAddresses);
            transport.close();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    public static String MD5(String pass) {
        return pass;
    }
}
