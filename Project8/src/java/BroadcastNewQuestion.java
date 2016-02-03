
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.servlet.*;
import javax.servlet.http.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;

public class BroadcastNewQuestion {

    public static void broadcastQuestion(String question_id, String area, int user_id) {
        final String SENDER_ID = "AIzaSyDRj3xsrU44zD4RUzk41cHQ-c4OdqEjISQ";
        List<String> androidTargets = new ArrayList<String>();
        //androidTargets.add(DROID_BIONIC);
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection(Utility.connection, Utility.username, Utility.password);

            int city_id = -1, state_id = -1, area_id = -1;
            String title = "", time = "", close_date = "";
            //Step 1: get the broadcast area
            String str1 = "SELECT * FROM PUBLIC_QUESTIONS WHERE QUESTION_ID=?";
            PreparedStatement prep1 = con.prepareStatement(str1);
            prep1.setString(1, question_id);
            ResultSet rs1 = prep1.executeQuery();
            while (rs1.next()) {
                title = rs1.getString("QUESTION_BODY");
                time = rs1.getString("POSTED_ON");
                close_date = rs1.getString("CLOSE_DATE");
                city_id = rs1.getInt("CITY_ID");
                area_id = rs1.getInt("AREA_ID");
                state_id = rs1.getInt("STATE_ID");
                
                if(title.length()>15)
                    title =title.substring(0,14);
                else 
                    title=title;
            }
            //Step 2: get the list of users and registration ids
            androidTargets.clear();
            if (area_id != 0) {
                String str2 = "SELECT REGISTRATION_ID FROM USERS WHERE AREA_ID=? AND USER_ID<>?";
                PreparedStatement prep2 = con.prepareStatement(str2);
                prep2.setInt(1, area_id);
                prep2.setInt(2, user_id);
                ResultSet rs2 = prep2.executeQuery();
                while (rs2.next()) {
                    String reg = rs2.getString("REGISTRATION_ID");
                    androidTargets.add(reg);
                }
            } else if (city_id != 0) {
                String str2 = "SELECT REGISTRATION_ID FROM USERS WHERE CITY_ID=? AND USER_ID<>?";
                PreparedStatement prep2 = con.prepareStatement(str2);
                prep2.setInt(1, city_id);
                prep2.setInt(2, user_id);
                ResultSet rs2 = prep2.executeQuery();
                while (rs2.next()) {
                    String reg = rs2.getString("REGISTRATION_ID");
                    androidTargets.add(reg);
                }
            } else if (state_id != 0) {
                String str2 = "SELECT REGISTRATION_ID FROM USERS WHERE STATE_ID=? AND USER_ID<>?";
                PreparedStatement prep2 = con.prepareStatement(str2);
                prep2.setInt(1, state_id);
                prep2.setInt(2, user_id);
                ResultSet rs2 = prep2.executeQuery();
                while (rs2.next()) {
                    String reg = rs2.getString("REGISTRATION_ID");
                    androidTargets.add(reg);
                }
            } else {
                String str2 = "SELECT REGISTRATION_ID FROM USERS WHERE USER_ID<>?";
                PreparedStatement prep2 = con.prepareStatement(str2);
                prep2.setInt(1, user_id);
                ResultSet rs2 = prep2.executeQuery();
                prep2.setInt(1, user_id);
                while (rs2.next()) {
                    String reg = rs2.getString("REGISTRATION_ID");
                    androidTargets.add(reg);
                }
            }

            //Step 3: create the message
            Sender sender = new Sender(SENDER_ID);
            Message.Builder builder = new Message.Builder();
            builder.addData("data1", "newQuestion");//type of message for the app

            //Step 5: get the complaint details
            builder.addData("title", title);
            builder.addData("time", time);
            builder.addData("closing_date", close_date);
            builder.addData("question_id", question_id);
            builder.addData("area", area);

            Message message = builder.build();
            MulticastResult result = sender.send(message, androidTargets, 1);

            if (result.getResults()
                    != null) {
                int canonicalRegId = result.getCanonicalIds();
                if (canonicalRegId != 0) {
                }
                System.out.println("New Question Broadcasted Succesfully");
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
