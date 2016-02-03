import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class GetCategories extends HttpServlet {

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection(Utility.connection, Utility.username, Utility.password);
            String str1 = "SELECT * FROM CATEGORIES";
            PreparedStatement prep1 = con.prepareStatement(str1);
            ResultSet rs1 = prep1.executeQuery();
            String sendList="";
            while(rs1.next()){
                String id = rs1.getString("CATEGORY_ID");
                String name = rs1.getString("CATEGORY_NAME");
                sendList+=id+","+name+"$";
            }
            System.out.println("Send LIst: "+sendList);
            out.print(sendList);
            
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
