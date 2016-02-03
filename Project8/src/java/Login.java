import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.simple.*;

public class Login extends HttpServlet {

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection(Utility.connection, Utility.username, Utility.password);
            
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String reg_id = request.getParameter("reg_id");
            int uid=-1;
            
            JSONObject data = new JSONObject();
            
            String str1 = "SELECT USER_ID, NAME FROM USERS WHERE EMAIL_ID=? AND PASSWORD=?";
            PreparedStatement prep1 = con.prepareStatement(str1);
            prep1.setString(1, email);
            prep1.setString(2, password);
            ResultSet rs1 = prep1.executeQuery();
            if(rs1.next()){
                uid = rs1.getInt("USER_ID");
                String user_name = rs1.getString("NAME");
                data.put("user_name", user_name);
                String str2 = "UPDATE USERS SET REGISTRATION_ID=? WHERE USER_ID=?";
                PreparedStatement prep2 = con.prepareStatement(str2);
                prep2.setString(1, reg_id);
                prep2.setInt(2, uid);
                prep2.executeUpdate();
            }
            data.put("user_id", uid);
            String toSend = data.toJSONString();
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
