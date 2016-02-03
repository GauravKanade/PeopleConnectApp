
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.simple.JSONObject;

public class GetActions extends HttpServlet {
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection(Utility.connection, Utility.username, Utility.password);
            
            String complaint_id = request.getParameter("complaint_id");
            System.out.println("COmplaint "+complaint_id);
            String str1 = "SELECT * FROM ACTIONS WHERE COMPLAINT_ID=? ORDER BY ACTION_PERFORMED_AT DESC";
            PreparedStatement prep1 = con.prepareStatement(str1);
            prep1.setString(1, complaint_id);
            ResultSet rs1 = prep1.executeQuery();
            
            JSONObject obj = new JSONObject();
            int n = 0;
            while (rs1.next()) {
                
                String authority_id = rs1.getString("AUTHORITY_ID");
                String time = rs1.getString("ACTION_PERFORMED_AT");
                String action = rs1.getString("ACTION_TAKEN");
                
                String str2 = "SELECT USER_NAME FROM AUTHORITIES WHERE AUTHORITY_ID=?";
                PreparedStatement prep2 = con.prepareStatement(str2);
                prep2.setString(1, authority_id);
                ResultSet rs2 = prep2.executeQuery();
                String authority = "";
                while (rs2.next()) {
                    authority = rs2.getString("USER_NAME");
                }
                time = Utility.getDateFormat(time);
                obj.put(n + "time", time);
                obj.put(n + "action", action);
                obj.put(n + "authority", authority);
                n++;
            }
            obj.put("n", n);
            out.print(obj.toJSONString());
            System.out.println("Complaint[" + complaint_id + "]: " + obj.toJSONString());
            
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
