
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.simple.*;

public class GetUserInfo extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection(Utility.connection, Utility.username, Utility.password);
            String user_id = request.getParameter("user_id");

            String str1 = "SELECT * FROM USERS WHERE USER_ID=?";
            PreparedStatement prep1 = con.prepareStatement(str1);
            prep1.setString(1, user_id);
            ResultSet rs1 = prep1.executeQuery();

            JSONObject data = new JSONObject();
            String user_name = "", phone = "", email = "", city_id = "", state_id = "", area_id = "", pincode = "";
            while (rs1.next()) {
                user_name = rs1.getString("NAME");
                phone = rs1.getString("PHONE_NUMBER");
                email = rs1.getString("EMAIL_ID");
                city_id = rs1.getString("CITY_ID");
                state_id = rs1.getString("STATE_ID");
                area_id = rs1.getString("AREA_ID");
                pincode = rs1.getString("PINCODE");
            }

            String str2 = "SELECT AREA_NAME FROM AREA WHERE AREA_ID=?";
            PreparedStatement prep2 = con.prepareStatement(str2);
            prep2.setString(1, area_id);
            ResultSet rs2 = prep2.executeQuery();
            String area_name = "";
            while (rs2.next()) {
                area_name = rs2.getString("AREA_NAME");
            }

            String str3 = "SELECT CITY_NAME FROM CITY WHERE CITY_ID=?";
            PreparedStatement prep3 = con.prepareStatement(str3);
            prep3.setString(1, city_id);
            ResultSet rs3 = prep3.executeQuery();
            String city_name = "";
            while (rs3.next()) {
                city_name = rs3.getString("CITY_NAME");
            }

            String str4 = "SELECT STATE_NAME FROM STATES WHERE STATE_ID=?";
            PreparedStatement prep4 = con.prepareStatement(str4);
            prep4.setString(1, state_id);
            ResultSet rs4 = prep4.executeQuery();
            String state_name = "";
            while (rs4.next()) {
                state_name = rs4.getString("STATE_NAME");
            }

            String history = "";
            String str5 = "SELECT * FROM COMPLAINTS WHERE COMPLAINED_BY=? ORDER BY COMPLAINED_ON DESC";
            PreparedStatement prep5 = con.prepareStatement(str5);
            prep5.setString(1, user_id);
            ResultSet rs5 = prep5.executeQuery();
            while (rs5.next()) {
                String complaint_id = rs5.getString("COMPLAINT_ID");
                String complaint = rs5.getString("COMPLAINT_BODY");
                String title = rs5.getString("TITLE");
                String posted_on = Utility.getDateFormat(rs5.getString("COMPLAINED_ON"));
                history += "Complained ID#" + complaint_id + "\n" + title + "\n[" + posted_on + "]\n\n\n";
            }

            String str6 = "SELECT PQ.QUESTION_ID,PQ.QUESTION_BODY, PQ.OPTION_1, PQ.OPTION_2, PQ.OPTION_3, PQ.OPTION_4, PQ.OPTION_5,V.OPTION_VOTED ";
            str6 += "FROM PUBLIC_QUESTIONS PQ, VOTES V ";
            str6 += "WHERE PQ.QUESTION_ID=V.QUESTION_ID AND V.USER_ID=? ";
            str6 += "ORDER BY PQ.POSTED_ON DESC";
            PreparedStatement prep6 = con.prepareStatement(str6);
            prep6.setString(1, user_id);
            ResultSet rs6 = prep6.executeQuery();
            while (rs6.next()) {
                String question_id = rs6.getString("QUESTION_ID");
                String question = rs6.getString("QUESTION_BODY");
                String option1 = rs6.getString("OPTION_1");
                String option2 = rs6.getString("OPTION_2");
                String option3 = rs6.getString("OPTION_3");
                String option4 = rs6.getString("OPTION_4");
                String option5 = rs6.getString("OPTION_5");
                history += "Question #" + question_id + "\n" + question + "\nVote:[";
                int option = rs6.getInt("OPTION_VOTED");
                switch (option) {
                    case 1:
                        history += option1;
                        break;
                    case 2:
                        history += option2;
                        break;
                    case 3:
                        history += option3;
                        break;
                    case 4:
                        history += option4;
                        break;
                    case 5:
                        history += option5;
                        break;
                }
                history += "]\n\n\n";
            }
            String address = area_name + "," + city_name + "," + state_name + " " + pincode;
            data.put("user_name", user_name);
            data.put("phone", phone);
            data.put("email", email);
            data.put("address", address);
            data.put("history", history);
            
            String toSend = data.toJSONString();
            System.out.println("GetUserInfo ["+user_id+"] "+toSend);
            out.print(toSend);

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
