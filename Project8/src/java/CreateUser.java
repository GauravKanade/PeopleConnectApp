import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class CreateUser extends HttpServlet {

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection(Utility.connection, Utility.username, Utility.password);
            String name = request.getParameter("name");
            String phone = request.getParameter("phone");
            String email = request.getParameter("email");
            String state = request.getParameter("state");
            String city = request.getParameter("city");
            String area = request.getParameter("area");
            String password = request.getParameter("password");
            String reg_id = request.getParameter("reg_id");
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
            
            String str4 = "INSERT INTO USERS(NAME, STATE_ID, CITY_ID, AREA_ID, PHONE_NUMBER, EMAIL_ID, PASSWORD, POINTS, REGISTRATION_ID, PINCODE) VALUES (?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement prep4 = con.prepareStatement(str4);
            prep4.setString(1, name);
            prep4.setInt(2, state_id);
            prep4.setInt(3, city_id);
            prep4.setInt(4, area_id);
            prep4.setString(5, phone);
            prep4.setString(6, email);
            prep4.setString(7, password);
            prep4.setInt(8, 0);
            prep4.setString(9, reg_id);
            prep4.setString(10, pincode);
            prep4.executeUpdate();
            
            int user_id = 0;
            
            String str5 = "SELECT USER_ID FROM USERS WHERE PHONE_NUMBER=? AND EMAIL_ID=? AND REGISTRATION_ID=?";
            PreparedStatement prep5 = con.prepareStatement(str5);
            prep5.setString(1, phone);
            prep5.setString(2, email);
            prep5.setString(3, reg_id);
            ResultSet rs5 = prep5.executeQuery();
            while(rs5.next()){
                user_id = rs5.getInt("USER_ID");
            }
            System.out.println("User_id new: "+user_id);
            out.print(""+user_id);
                    
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
