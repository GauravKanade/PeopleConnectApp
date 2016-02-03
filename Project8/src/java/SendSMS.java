
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.simple.*;

public class SendSMS extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection(Utility.connection, Utility.username, Utility.password);
            
            String email = request.getParameter("email_id");
            
            
            String number = "";
            boolean exists = false;
            String user_name = "";
            int user_id = -1;
            String str1 = "SELECT USER_ID,NAME,PHONE_NUMBER FROM USERS WHERE EMAIL_ID=?";
            PreparedStatement prep1 = con.prepareStatement(str1);
            prep1.setString(1, email);
            ResultSet rs1 = prep1.executeQuery();
            if (rs1.next()) {
                exists = true;
                user_id = rs1.getInt("USER_ID");
                user_name = rs1.getString("NAME");
                number = rs1.getString("PHONE_NUMBER");
            }
            int verification = 0;
            JSONObject data = new JSONObject();
            if (exists) {
                verification = (int) (Math.random() * 9535641 % 999999);
                System.out.println("Number " + number + "\nVerification: " + verification);
                SMSProvider.sendSMS(number, "Your One Time Verification Code for PeopleConnect Is " + verification);
            }

            data.put("user_name", user_name);
            data.put("user_id", user_id);
            data.put("verification_code", "" + verification);
            data.put("phone_number", number);
            
            String toSend = data.toJSONString();
            out.print(toSend);
            System.out.println(toSend);


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
