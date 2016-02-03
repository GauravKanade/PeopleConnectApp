
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ChangeUserName extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection(Utility.connection, Utility.username, Utility.password);

            String user_id = request.getParameter("user_id");
            String user_name = request.getParameter("user_name");

            System.out.println("User id ["+user_id+"] changed user name to "+user_name);
            
            String str2 = "UPDATE USERS SET NAME=? WHERE USER_ID=?";
            PreparedStatement prep2 = con.prepareStatement(str2);
            prep2.setString(1, user_name);
            prep2.setString(2, user_id);
            prep2.executeUpdate();
            out.print("success");

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
