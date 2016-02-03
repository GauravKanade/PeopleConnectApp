
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.simple.*;

public class AuthorityPassword extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Project_8", "root", "varuag");

            String auth_id = request.getParameter("authority_id");
            String str1 = "SELECT * FROM AUTHORITIES WHERE AUTHORITY_ID=?";
            PreparedStatement prep1 = con.prepareStatement(str1);
            prep1.setString(1, auth_id);
            ResultSet rs1 = prep1.executeQuery();
            String email = "", uname = "", phone = "";
            while (rs1.next()) {
                email = rs1.getString("EMAIL_ID");
                uname = rs1.getString("USER_NAME");
                phone = rs1.getString("PHONE_NUMBER");
            }
            String newPass = "Pc" + (int) (Math.random() * 953564 % 999999);
            String md5 = Utility.MD5(newPass);
            String str3 = "UPDATE AUTHORITIES SET PASSWORD=? WHERE AUTHORITY_ID=?";
            PreparedStatement prep3 = con.prepareStatement(str3);
            prep3.setString(1, md5);
            prep3.setString(2, auth_id);
            prep3.executeUpdate();
            String toSend = "Hi " + uname + ",<br/>Your People Connect Password is given below. Please note that this is a system generated password and not your permanent one. Please change your password on your first log in.<br/><br/>";
            toSend += "<table border=\"1\"><tr><td>User Name</td><td>" + uname + "</td></tr><tr><td>Password</td><td>" + newPass + "</td></tr><tr><td>Email ID</td><td>" + email + "</td></tr><tr><td>Phone Number</td><td>" + phone + "</td></tr></table>";
            toSend += "<br/><br/>Regards,<br/>People Connect";

            Utility.sendtosingle(email, "People Connect", toSend);
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
