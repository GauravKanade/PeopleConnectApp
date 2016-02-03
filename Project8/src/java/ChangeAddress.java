import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ChangeAddress extends HttpServlet {

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection(Utility.connection, Utility.username, Utility.password);
            
            String user_id = request.getParameter("user_id");
            String state = request.getParameter("state");
            String city = request.getParameter("city");
            String area = request.getParameter("area");
            String pincode = request.getParameter("pincode");
            
            int state_id = 0, city_id=0, area_id=0;
            
            //Get state id
            String str1 = "SELECT STATE_ID FROM STATES WHERE STATE_NAME=?";
            PreparedStatement prep1 = con.prepareStatement(str1);
            prep1.setString(1, state);
            ResultSet rs1 = prep1.executeQuery();
            while(rs1.next())
                state_id = rs1.getInt("STATE_ID");
            
            //Get City ID
            String str2 = "SELECT CITY_ID FROM CITY WHERE CITY_NAME=? AND STATE_ID=?";
            PreparedStatement prep2 = con.prepareStatement(str2);
            prep2.setString(1, city);
            prep2.setInt(2, state_id);
            ResultSet rs2 = prep2.executeQuery();
            while(rs2.next())
                city_id = rs2.getInt("CITY_ID");
            
            //Get Area ID
            String str3 = "SELECT AREA_ID FROM AREA WHERE AREA_NAME=? AND CITY_ID=?";
            PreparedStatement prep3 = con.prepareStatement(str3);
            prep3.setString(1, area);
            prep3.setInt(2, city_id);
            ResultSet rs3 = prep3.executeQuery();
            while(rs3.next())
                area_id = rs3.getInt("AREA_ID");
            
            String str4 = "UPDATE USERS SET STATE_ID=?, CITY_ID=?, AREA_ID=?, PINCODE=? WHERE USER_ID=?";
            PreparedStatement prep4 = con.prepareStatement(str4);
            prep4.setInt(1, state_id);
            prep4.setInt(2, city_id);
            prep4.setInt(3, area_id);
            prep4.setString(4, pincode);
            prep4.setString(5, user_id);
            prep4.executeUpdate();
            
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
