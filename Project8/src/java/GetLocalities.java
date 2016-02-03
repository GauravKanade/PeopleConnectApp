import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.simple.*;

public class GetLocalities extends HttpServlet {

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection(Utility.connection, Utility.username, Utility.password);
            
            String user_id = request.getParameter("user_id");
            String state="", city="", area="";
            
            String str1 = "SELECT C.CITY_NAME, A.AREA_NAME, S.STATE_NAME FROM STATES S, CITY C, AREA A, USERS U";
            str1+=" WHERE U.USER_ID=? AND U.STATE_ID=S.STATE_ID AND U.AREA_ID=A.AREA_ID AND U.CITY_ID=C.CITY_ID";
            PreparedStatement prep1 = con.prepareStatement(str1);
            prep1.setString(1, user_id);
            ResultSet rs1 = prep1.executeQuery();
            while(rs1.next()){
                state=rs1.getString("STATE_NAME");
                city = rs1.getString("CITY_NAME");
                area=rs1.getString("AREA_NAME");
            }
            JSONObject data = new JSONObject();
            data.put("state", state);
            data.put("city", city);
            data.put("area", area);
            
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
