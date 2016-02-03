import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ChangePassword extends HttpServlet {

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection(Utility.connection, Utility.username, Utility.password);
            
            String user_id = request.getParameter("user_id");
            String exist = request.getParameter("existing");
            String pass1 = request.getParameter("pass1");
            
            String current = "";
            String str1 = "SELECT PASSWORD FROM USERS WHERE USER_ID=?";
            PreparedStatement prep1 = con.prepareStatement(str1);
            prep1.setString(1, user_id);
            ResultSet rs1 = prep1.executeQuery();
            if(rs1.next()){
                current = rs1.getString("PASSWORD");
            }
            if(exist.equals(current)){
                String str2 = "UPDATE USERS SET PASSWORD=? WHERE USER_ID=?";
                PreparedStatement prep2 = con.prepareStatement(str2);
                prep2.setString(1, pass1);
                prep2.setString(2, user_id);
                prep2.executeUpdate();
                out.print("success");
            } else{
                out.print("mismatch");
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
