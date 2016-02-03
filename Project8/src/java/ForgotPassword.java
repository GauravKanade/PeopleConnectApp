
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.simple.*;

public class ForgotPassword extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Project_8", "root", "varuag");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");

            String str1 = "SELECT * FROM USERS WHERE EMAIL_ID=? AND PHONE_NUMBER=?";
            PreparedStatement prep1 = con.prepareStatement(str1);
            prep1.setString(1, email);
            prep1.setString(2, phone);
            ResultSet rs1 = prep1.executeQuery();

            if (rs1.next()) {
                int uid = rs1.getInt("USER_ID");
                String uname = rs1.getString("NAME");

                String newPass = "Pc" + (int) (Math.random() * 953564 % 999999);
                System.out.println("User with email " + email + ", forgot password. Reset to " + newPass);
                String md5 = Utility.MD5(newPass);
                
                String str3 = "UPDATE USERS SET PASSWORD=? WHERE USER_ID=?";
                PreparedStatement prep3 = con.prepareStatement(str3);
                prep3.setString(1, md5);
                prep3.setInt(2, uid);
                prep3.executeUpdate();
                
                //Sending mail
                String toSend = "Hi "+uname+",<br/>Your People Connect Password has been reset by the server.<br/><br/>";
                toSend+="<table border=\"1\"><tr><td>User Name</td><td>"+uname+"</td></tr><tr><td>Phone Number</td><td>"+phone+"</td></tr><tr><td>Password</td><td>"+newPass+"</td></tr></table>";
                toSend+="<br/><br/>Regards,<br/>People Connect";
                
                Utility.sendtosingle(email, "People Connect",toSend );
                out.print("success");
                
            } else {
                out.print("failure");
            }

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
