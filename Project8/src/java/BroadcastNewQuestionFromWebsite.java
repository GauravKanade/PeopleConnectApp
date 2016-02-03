/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

/**
 *
 * @author Gaurav Kanade
 */
public class BroadcastNewQuestionFromWebsite extends HttpServlet {

    private static final String SENDER_ID = "AIzaSyDRj3xsrU44zD4RUzk41cHQ-c4OdqEjISQ";
    private List<String> androidTargets = new ArrayList<String>();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection(Utility.connection, Utility.username, Utility.password);
            String question_id=request.getParameter("question_id");
            int city_id = -1, state_id = -1, area_id = -1;
            String title = "", time = "", close_date = "";
            //Step 1: get the broadcast area
            
            System.out.println("New Public Question from authority: id:["+question_id+"]");
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
            }
            String recipients = "";
            //Step 2: get the list of users and registration ids
            androidTargets.clear();
            if (area_id != 0) {
                String str2 = "SELECT REGISTRATION_ID FROM USERS WHERE AREA_ID=?";
                PreparedStatement prep2 = con.prepareStatement(str2);
                prep2.setInt(1, area_id);
                ResultSet rs2 = prep2.executeQuery();
                while (rs2.next()) {
                    String reg = rs2.getString("REGISTRATION_ID");
                    androidTargets.add(reg);
                }
                String str3 = "SELECT AREA_NAME FROM AREA WHERE AREA_ID=?";
                PreparedStatement prep3 = con.prepareStatement(str3);
                prep3.setInt(1, area_id);
                ResultSet rs3 = prep3.executeQuery();
                while (rs3.next()) {
                    recipients = rs3.getString("AREA_NAME");
                }

            } else if (city_id != 0) {
                String str2 = "SELECT REGISTRATION_ID FROM USERS WHERE CITY_ID=?";
                PreparedStatement prep2 = con.prepareStatement(str2);
                prep2.setInt(1, city_id);
                ResultSet rs2 = prep2.executeQuery();
                while (rs2.next()) {
                    String reg = rs2.getString("REGISTRATION_ID");
                    androidTargets.add(reg);
                }
                String str3 = "SELECT CITY_NAME FROM CITY WHERE CITY_ID=?";
                PreparedStatement prep3 = con.prepareStatement(str3);
                prep3.setInt(1, city_id);
                ResultSet rs3 = prep3.executeQuery();
                while (rs3.next()) {
                    recipients = rs3.getString("CITY_NAME");
                }
            } else if (state_id != 0) {
                String str2 = "SELECT REGISTRATION_ID FROM USERS WHERE STATE_ID=?";
                PreparedStatement prep2 = con.prepareStatement(str2);
                prep2.setInt(1, state_id);
                ResultSet rs2 = prep2.executeQuery();
                while (rs2.next()) {
                    String reg = rs2.getString("REGISTRATION_ID");
                    androidTargets.add(reg);
                }
                String str3 = "SELECT STATE_NAME FROM STATES WHERE STATE_ID=?";
                PreparedStatement prep3 = con.prepareStatement(str3);
                prep3.setInt(1, state_id);
                ResultSet rs3 = prep3.executeQuery();
                while (rs3.next()) {
                    recipients = rs3.getString("STATE_NAME");
                }
            } else {
                String str2 = "SELECT REGISTRATION_ID FROM USERS";
                PreparedStatement prep2 = con.prepareStatement(str2);
                ResultSet rs2 = prep2.executeQuery();
                while (rs2.next()) {
                    String reg = rs2.getString("REGISTRATION_ID");
                    androidTargets.add(reg);
                }
                recipients = "India";
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
            builder.addData("area", recipients);

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

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
