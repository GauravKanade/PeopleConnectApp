import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ChangeEmailID extends HttpServlet {

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection(Utility.connection, Utility.username, Utility.password);
            
            String user_id = request.getParameter("user_id");
            String email_id = request.getParameter("email_id");
            
            System.out.println("User id ["+user_id+"] changed email id to "+email_id);
            
            boolean exists =false;
            String str1 = "SELECT USER_ID FROM USERS WHERE EMAIL_ID=?";
            PreparedStatement prep1 = con.prepareStatement(str1);
            prep1.setString(1, email_id);
            ResultSet rs1 = prep1.executeQuery();
            if(rs1.next())
                exists = true;
            if(exists)
            {
                out.print("error");
                return;
            } else {
                String str2 = "UPDATE USERS SET EMAIL_ID=? WHERE USER_ID=?";
                PreparedStatement prep2 = con.prepareStatement(str2);
                prep2.setString(1, email_id);
                prep2.setString(2, user_id);
                prep2.executeUpdate();
                out.print("success");
            }
            
        } catch(Exception e){
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
