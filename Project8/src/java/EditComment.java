import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.servlet.*;
import javax.servlet.http.*;

public class EditComment extends HttpServlet {

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection(Utility.connection, Utility.username, Utility.password);
            String comment_id = request.getParameter("comment_id");
            String comment = request.getParameter("comment");
            
            String str1 = "UPDATE COMMENTS SET COMMENT_BODY=? WHERE COMMENT_ID=?";
            PreparedStatement prep1 = con.prepareStatement(str1);
            prep1.setString(1, comment);
            prep1.setString(2, comment_id);
            prep1.executeUpdate();
           
            out.print("Comment has been updated to : "+comment );
            System.out.print("Comment["+comment_id+"] has been updated to : "+comment );
            
            
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
