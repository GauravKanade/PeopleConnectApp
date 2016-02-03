
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

public class Broadcaster extends HttpServlet {

    private static final String SENDER_ID = "AIzaSyDRj3xsrU44zD4RUzk41cHQ-c4OdqEjISQ";
    private List<String> androidTargets = new ArrayList<String>();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        //androidTargets.add(DROID_BIONIC);
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
           Connection con = DriverManager.getConnection(Utility.connection, Utility.username, Utility.password);
            String data1 = request.getParameter("data1");
            String uid = "1";
            uid = request.getParameter("user_id");

            androidTargets.clear();
            String str2 = "SELECT REGISTRATION_ID FROM USERS WHERE USER_ID=?";
            PreparedStatement prep2 = con.prepareStatement(str2);
            prep2.setString(1, uid);
            ResultSet rs2 = prep2.executeQuery();

            while (rs2.next()) {
                String reg_id = rs2.getString("REGISTRATION_ID");
                androidTargets.add(reg_id);
            }

System.out.println("DAta 1:" +data1);
            Sender sender = new Sender(SENDER_ID);
            Message message;
            Message.Builder builder = new Message.Builder();
            builder.addData("data1", data1);
            if (data1.equals("sendSMS")) {
                System.out.println("SMS SENDING CALL");
                String number = request.getParameter("number");
                String data = request.getParameter("data");
                SMSProvider.sendSMS(number, data);
            } else {
                String data2 = request.getParameter("data2");
                builder.addData("data2", data2);


                message = builder.build();


                MulticastResult result = sender.send(message, androidTargets, 1);

                if (result.getResults() != null) {
                    int canonicalRegId = result.getCanonicalIds();
                    if (canonicalRegId != 0) {
                    }
                    System.out.println("Sent Succesfully");
                } else {
                    int error = result.getFailure();
                    System.out.println("Broadcast failure: " + error);
                }
            }
            request.getRequestDispatcher("Send.jsp").forward(request, response);

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
