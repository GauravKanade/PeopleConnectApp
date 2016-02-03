
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.simple.*;

public class SupportMailFromWebsite extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Project_8", "root", "varuag");

            //inputs
            String user_id = request.getParameter("user_id");
            String AuthOrNot = request.getParameter("AuthOrNot");
            String type = request.getParameter("type");
            String subject = request.getParameter("subject");
            String body = request.getParameter("supportbody");
            System.out.println("Support from website: uid:" + user_id + "\nAuthOrNot: " + AuthOrNot + "\nType: " + type + "\nSubject:" + subject + "\nBody:" + body);
            String userInfo = "";
            if (AuthOrNot.equals("1")) {
                String str1 = "SELECT * FROM AUTHORITIES WHERE AUTHORITY_ID=?";
                PreparedStatement prep1 = con.prepareStatement(str1);
                prep1.setString(1, user_id);
                ResultSet rs1 = prep1.executeQuery();
                while (rs1.next()) {
                    String name = rs1.getString("USER_NAME");
                    String email = rs1.getString("EMAIL_ID");
                    String phone = rs1.getString("PHONE_NUMBER");
                    String city_id = rs1.getString("CITY_ID");
                    String state_id = rs1.getString("STATE_ID");

                    String str2 = "SELECT STATE_NAME FROM STATES WHERE STATE_ID=?";
                    PreparedStatement prep2 = con.prepareStatement(str2);
                    prep2.setString(1, state_id);
                    ResultSet rs2 = prep2.executeQuery();
                    String state_name = "";
                    while (rs2.next()) {
                        state_name = rs2.getString("STATE_NAME");
                    }
                    String str3 = "SELECT CITY_NAME FROM CITY WHERE CITY_ID=?";
                    PreparedStatement prep3 = con.prepareStatement(str3);
                    prep3.setString(1, city_id);
                    ResultSet rs3 = prep3.executeQuery();
                    String city_name = "";
                    while (rs3.next()) {
                        city_name = rs3.getString("CITY_NAME");
                    }
                    userInfo = "<b>" + name + "</b>(Authority)<br/>" + email + "<br/>Phone:+91" + phone + "<br/>" + city_name + ", " + state_name + ", India";

                }

            } else {
                String str1 = "SELECT * FROM USERS WHERE USER_ID=?";
                PreparedStatement prep1 = con.prepareStatement(str1);
                prep1.setString(1, user_id);
                ResultSet rs1 = prep1.executeQuery();
                while (rs1.next()) {
                    String name = rs1.getString("NAME");
                    String email = rs1.getString("EMAIL_ID");
                    String phone = rs1.getString("PHONE_NUMBER");
                    String city_id = rs1.getString("CITY_ID");
                    String state_id = rs1.getString("STATE_ID");

                    String str2 = "SELECT STATE_NAME FROM STATES WHERE STATE_ID=?";
                    PreparedStatement prep2 = con.prepareStatement(str2);
                    prep2.setString(1, state_id);
                    ResultSet rs2 = prep2.executeQuery();
                    String state_name = "";
                    while (rs2.next()) {
                        state_name = rs2.getString("STATE_NAME");
                    }
                    String str3 = "SELECT CITY_NAME FROM CITY WHERE CITY_ID=?";
                    PreparedStatement prep3 = con.prepareStatement(str3);
                    prep3.setString(1, city_id);
                    ResultSet rs3 = prep3.executeQuery();
                    String city_name = "";
                    while (rs3.next()) {
                        city_name = rs3.getString("CITY_NAME");
                    }
                    userInfo = "<b>" + name + "</b>(User)<br/>" + email + "<br/>Phone:+91" + phone + "<br/>" + city_name + ", " + state_name + ", India";

                }
            }

            String toSend = "";
            toSend = "Hi admin,<br/>This is a " + type + " on the domain called '" + subject + "' of People Connect Website.<br/>My Concern is:<br/>" + body;
            toSend += "<br/><br/>Sent By,<br/>" + userInfo;
            Utility.sendtosingle("gpkanade2@gmail.com", "Support Mail", toSend);
            System.out.print("Sent Support MAil");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.close();
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
