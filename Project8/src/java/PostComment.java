import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.servlet.*;
import javax.servlet.http.*;

public class PostComment extends HttpServlet {

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection(Utility.connection, Utility.username, Utility.password);
            String user_id = request.getParameter("user_id");
            String body = request.getParameter("body");
            String complaint_id = request.getParameter("complaint_id");
            
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            String finaltime = "";
            finaltime += timeStamp.substring(0, 4) + "-" + timeStamp.substring(4, 6) + "-" + timeStamp.substring(6, 8) + " " + timeStamp.substring(9, 11) + ":" + timeStamp.substring(11, 13) + ":" + timeStamp.substring(13, 15);

            String str1 = "INSERT INTO COMMENTS(COMPLAINT_ID, COMMENT_BODY, POSTED_ON, POSTED_BY) VALUES (?,?,?,?)";
            PreparedStatement prep1 = con.prepareStatement(str1);
            prep1.setString(1, complaint_id);
            prep1.setString(2, body);
            prep1.setString(3, finaltime);
            prep1.setString(4, user_id);
            prep1.executeUpdate();
            
            String str2 = "SELECT NAME FROM USERS WHERE USER_ID=?";
            PreparedStatement prep2 = con.prepareStatement(str2);
            prep2.setString(1, user_id);
            ResultSet rs2 = prep2.executeQuery();
            String user_name = "";
            while(rs2.next())
                user_name= rs2.getString("NAME");
            
            String str3 = "SELECT COMMENT_ID FROM COMMENTS WHERE COMMENT_BODY=? AND POSTED_ON=?";
            PreparedStatement prep3 = con.prepareStatement(str3);
            prep3.setString(1, body);
            prep3.setString(2, finaltime);
            ResultSet rs3 = prep3.executeQuery();
            int comment_id = 0;
            while(rs3.next())
                comment_id = rs3.getInt("COMMENT_ID");
            
            finaltime = Utility.getDateFormat(finaltime);
            String toSend = user_name+"##$$##"+finaltime+"##$$##"+body+"##$$##"+comment_id+"##$$##";
            System.out.println("Added COmment "+toSend);
            out.print(toSend);
            
            
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
