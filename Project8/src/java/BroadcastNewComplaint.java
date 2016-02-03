
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;

public class BroadcastNewComplaint {

    public static void broadcast(String complaint_id, String user_id) {
        final String SENDER_ID = "AIzaSyDRj3xsrU44zD4RUzk41cHQ-c4OdqEjISQ";
        List<String> androidTargets = new ArrayList<String>();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection(Utility.connection, Utility.username, Utility.password);
            //String complaint_id = request.getAttribute("complaint_id").toString();
            String city_id = "-1", state_id = "0";

            //Step 1: get the broadcast city name
            String str1 = "SELECT CITY_ID, STATE_ID FROM COMPLAINTS WHERE COMPLAINT_ID=?";
            PreparedStatement prep1 = con.prepareStatement(str1);
            prep1.setString(1, complaint_id);
            ResultSet rs1 = prep1.executeQuery();
            while (rs1.next()) {
                city_id = rs1.getString("CITY_ID");
                state_id = rs1.getString("STATE_ID");
            }

            //Step 2: get the list of people of the corresponding city
            String str3 = "SELECT REGISTRATION_ID,USER_ID FROM USERS WHERE CITY_ID=? AND STATE_ID=?";
            PreparedStatement prep3 = con.prepareStatement(str3);
            prep3.setString(1, city_id);
            prep3.setString(2, state_id);
            ResultSet rs3 = prep3.executeQuery();
            androidTargets.clear();
            while (rs3.next()) {
                String user_id_1 = rs3.getString("USER_ID");
                if (!user_id.equals(user_id_1)) {
                    String reg_id = rs3.getString("REGISTRATION_ID");
                    androidTargets.add(reg_id);
                }
            }

            //Step 4: create the message
            Sender sender = new Sender(SENDER_ID);
            Message.Builder builder = new Message.Builder();
            builder.addData("data1", "newComplaint");//type of message for the app

            //Step 5: get the complaint details
            String str4 = "SELECT * FROM COMPLAINTS WHERE COMPLAINT_ID=?";
            PreparedStatement prep4 = con.prepareStatement(str4);
            prep4.setString(1, complaint_id);
            ResultSet rs4 = prep4.executeQuery();
            String title = "", location = "";
            while (rs4.next()) {
                title = rs4.getString("TITLE");
                location = rs4.getString("COMPLAINT_LOCATION");
                String complained_by_id = rs4.getString("COMPLAINED_BY");
                String complained_on = rs4.getString("COMPLAINED_ON");

                String str22 = "SELECT NAME FROM USERS WHERE USER_ID=?";
                PreparedStatement prep22 = con.prepareStatement(str22);
                prep22.setString(1, complained_by_id);
                ResultSet rs22 = prep22.executeQuery();
                String user_name = "";
                while (rs22.next()) {
                    user_name = rs22.getString("NAME");
                }
                builder.addData("title", title);
                builder.addData("complaint_id", complaint_id);
                builder.addData("sender", user_name);
                builder.addData("time", complained_on);
                builder.addData("area", location);
            }

            Message message = builder.build();
            MulticastResult result = sender.send(message, androidTargets, 1);

            if (result.getResults()
                    != null) {
                int canonicalRegId = result.getCanonicalIds();
                if (canonicalRegId != 0) {
                }
                System.out.println("Broadcasted Succesfully");
            } else {
                int error = result.getFailure();
                System.out.println("Broadcast failure: " + error);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return;
        }
    }
}
