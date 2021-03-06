
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

public class BroadcastNewAction extends HttpServlet {

    private static final String SENDER_ID = "AIzaSyDRj3xsrU44zD4RUzk41cHQ-c4OdqEjISQ";
    private List<String> androidTargets = new ArrayList<String>();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        //androidTargets.add(DROID_BIONIC);
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection(Utility.connection, Utility.username, Utility.password);
            String complaint_id = request.getParameter("complaint_id");
            String action = request.getParameter("action_description");
            String city_id = "-1", city_name = "City";
            
            //Step 1: get the broadcast city name
            String str1 = "SELECT CITY_ID FROM COMPLAINTS WHERE COMPLAINT_ID=?";
            PreparedStatement prep1 = con.prepareStatement(str1);
            prep1.setString(1, complaint_id);
            ResultSet rs1 = prep1.executeQuery();
            while (rs1.next()) {
                city_id = rs1.getString("CITY_ID");
            }

            System.out.println("The city id: " + city_id);
            //Step 3: get the list of people of the corresponding city
            String str3 = "SELECT REGISTRATION_ID FROM USERS WHERE CITY_ID=?";
            PreparedStatement prep3 = con.prepareStatement(str3);
            prep3.setString(1, city_id);
            ResultSet rs3 = prep3.executeQuery();
            androidTargets.clear();
            while (rs3.next()) {
                String reg_id = rs3.getString("REGISTRATION_ID");
                androidTargets.add(reg_id);
            }

            //Step 4: create the message
            Sender sender = new Sender(SENDER_ID);
            Message.Builder builder = new Message.Builder();
            builder.addData("data1", "newAction");//type of message for the app

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
                String complained_on = Utility.getDateFormat(rs4.getString("COMPLAINED_ON"));

                String str22 = "SELECT NAME FROM USERS WHERE USER_ID=?";
                PreparedStatement prep22 = con.prepareStatement(str22);
                prep22.setString(1, complained_by_id);
                ResultSet rs22 = prep22.executeQuery();
                String user_name = "";
                while (rs22.next()) {
                    user_name = rs22.getString("NAME");
                }
                if (title.length() > 15) {
                    title = title.substring(0, 15);
                }
                builder.addData("title", title);
                builder.addData("complaint_id", complaint_id);
                builder.addData("action", action);
                builder.addData("sender", user_name);
                builder.addData("time", complained_on);
                builder.addData("area", location);
            }

            Message message = builder.build();
            if (!androidTargets.isEmpty()) {
                MulticastResult result = sender.send(message, androidTargets, 1);
            
            if (result.getResults()
                    != null) {
                int canonicalRegId = result.getCanonicalIds();
                if (canonicalRegId != 0) {
                }
                System.out.println("Sent Succesfully");
            } else {
                int error = result.getFailure();
                System.out.println("Broadcast failure: " + error);
            }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}