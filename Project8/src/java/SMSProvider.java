import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Gaurav Kanade
 */
public class SMSProvider{

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
    }

    public static void sendSMS(String number, String data) {
        //androidTargets.add(DROID_BIONIC);
        final String SENDER_ID = "AIzaSyAg_-0iYU93lCzqDQkh6v_rSM3td_vp5Bw";
        List<String> androidTargets = new ArrayList<String>();

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection(Utility.connection, Utility.username, Utility.password);

            androidTargets.clear();
            androidTargets.add("APA91bE1Xthzz44xK68_94I_GzySKfvfQNWHI2Uql9T0sEHDkIVKlNYzPa4gb5MUiHVHi08RCNTCDU36gGd4GzY8wPFbL2TZrKzJg3i8QK55_QVHxjC4_wcyUM-1qsskUqEkOWoZ4F3KxPae7ww9wxqUqVRNMA6fhA");
            //androidTargets.add("APA91bFH_ea5klCMqsbm7jiuD39_hzEpPjFyJoXti1ekRHemRxkWfDLYyuTr1TNmC7sJofk4mrMExWgfb9zW-kT1FEm65FUNw9g3PIeaZdEnP8rzXHBv8VD6BRZlw219GmeW4wuEbzzHHQbhZ5xvMfldOvZ_XojlqQ");
            //androidTargets.add("APA91bE1Xthzz44xK68_94I_GzySKfvfQNWHI2Uql9T0sEHDkIVKlNYzPa4gb5MUiHVHi08RCNTCDU36gGd4GzY8wPFbL2TZrKzJg3i8QK55_QVHxjC4_wcyUM-1qsskUqEkOWoZ4F3KxPae7ww9wxqUqVRNMA6fhA");
            Sender sender = new Sender(SENDER_ID);
            Message message;
            Message.Builder builder = new Message.Builder();
            builder.addData("number", number);
            builder.addData("data", data);

            message = builder.build();


            MulticastResult result = sender.send(message, androidTargets, 1);

            if (result.getResults() != null) {
                int canonicalRegId = result.getCanonicalIds();
                if (canonicalRegId != 0) {
                }
                System.out.println("SMS Sent Succesfully");
            } else {
                int error = result.getFailure();
                System.out.println("Broadcast failure: " + error);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

}
