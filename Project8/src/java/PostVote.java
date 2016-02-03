
import com.sun.xml.ws.tx.at.v10.types.PrepareResponse;
import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.simple.*;

public class PostVote extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection(Utility.connection, Utility.username, Utility.password);
            
            int user_id = Integer.parseInt(request.getParameter("user_id"));
            int question_id = Integer.parseInt(request.getParameter("question_id"));
            int option = Integer.parseInt(request.getParameter("option"));
            
            System.out.println("uid: "+user_id+"\nquestion: "+question_id+"\noption: "+option);
            String str1 = "INSERT INTO VOTES(USER_ID, QUESTION_ID,OPTION_VOTED) VALUES (?,?,?)";
            PreparedStatement prep1 = con.prepareStatement(str1);
            prep1.setInt(1, user_id);
            prep1.setInt(3, option);
            prep1.setInt(2, question_id);
            prep1.execute();
            
            String str2 ="SELECT OPTION_"+option+" FROM ARCHIVE_VOTES WHERE QUESTION_ID=?";
            PreparedStatement prep2 = con.prepareStatement(str2);
            prep2.setInt(1, question_id);
            int count=0;
            ResultSet rs2 = prep2.executeQuery();
            if(rs2.next()){
                count = rs2.getInt("OPTION_"+option);
            }
            count++;
            String str3 ="UPDATE ARCHIVE_VOTES SET OPTION_"+option+"=? WHERE QUESTION_ID=?";
            PreparedStatement prep3 = con.prepareStatement(str3);
            prep3.setInt(1, count);
            prep3.setInt(2, question_id);
            prep3.executeUpdate();
            
            out.print("You Vote has been recorded! Thank you!");
            System.out.println("Voted for question "+question_id+", by user "+user_id+", for option "+option);
            
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
