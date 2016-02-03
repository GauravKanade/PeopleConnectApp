import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.simple.*;

public class LogoutFromApp extends HttpServlet {

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Project_8", "root", "varuag");
            
            String uid = request.getParameter("user_id");
            String str1 = "UPDATE USERS SET REGISTRATION_ID=? WHERE USER_ID=?";
            PreparedStatement prep1 = con.prepareStatement(str1);
            prep1.setString(1, "");
            prep1.setString(2, uid);
            prep1.executeUpdate();
            System.out.println("User id ["+uid+"], safely logged out");
            out.print("success");
            
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
