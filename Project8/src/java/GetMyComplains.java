
import java.io.*;
import java.util.StringTokenizer;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class GetMyComplains extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection(Utility.connection, Utility.username, Utility.password);
            String toSend = "";
            String user_id = request.getParameter("user_id");
            
            String str1 = "SELECT * FROM COMPLAINTS WHERE COMPLAINED_BY=? ORDER BY COMPLAINED_ON DESC";
            PreparedStatement prep1 = con.prepareStatement(str1);
            prep1.setString(1, user_id);
            ResultSet rs1 = prep1.executeQuery();

            while (rs1.next()) {
                int id = rs1.getInt("COMPLAINT_ID");
                String complained_by_id = rs1.getString("COMPLAINED_BY");
                String complained_on = rs1.getString("COMPLAINED_ON");
                String title = rs1.getString("TITLE");
                String city_id = rs1.getString("CITY_ID");

                String str2 = "SELECT NAME FROM USERS WHERE USER_ID=?";
                PreparedStatement prep2 = con.prepareStatement(str2);
                prep2.setString(1, complained_by_id);
                ResultSet rs2 = prep2.executeQuery();
                String user_name = "";
                while (rs2.next()) {
                    user_name = rs2.getString("NAME");
                }
                
                String loc="";
                String str3 = "SELECT CITY_NAME FROM CITY WHERE CITY_ID=?";
                PreparedStatement prep3 = con.prepareStatement(str3);
                prep3.setString(1, city_id);
                ResultSet rs3 = prep3.executeQuery();
                if(rs3.next()){
                    loc=rs3.getString("CITY_NAME");
                }
                String seperator = "####";
                toSend += id + seperator + title + seperator + user_name + seperator + complained_on + seperator + loc + seperator + "Sample" + "$$$$";

            }
            System.out.println(toSend);
            out.println(toSend);


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
