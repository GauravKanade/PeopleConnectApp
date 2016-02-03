
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class GetComments extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection(Utility.connection, Utility.username, Utility.password);

            String complaint_id = request.getParameter("complaint_id");
            String str1 = "SELECT * FROM COMMENTS WHERE COMPLAINT_ID=?";
            PreparedStatement prep1 = con.prepareStatement(str1);
            prep1.setString(1, complaint_id);
            ResultSet rs1 = prep1.executeQuery();

            String toSend = "";
            String seperator = "$$$$";
            while (rs1.next()) {
                String comment_id = rs1.getString("COMMENT_ID");
                String body = rs1.getString("COMMENT_BODY");
                String posted_on = rs1.getString("POSTED_ON");
                String posted_by_id = rs1.getString("POSTED_BY");

                String str2 = "SELECT NAME FROM USERS WHERE USER_ID=?";
                PreparedStatement prep2 = con.prepareStatement(str2);
                prep2.setString(1, posted_by_id);
                ResultSet rs2 = prep2.executeQuery();
                String posted_by = "";
                while (rs2.next()) {
                    posted_by = rs2.getString("NAME");
                }
                posted_on = Utility.getDateFormat(posted_on);

                toSend += comment_id + seperator + body + seperator + posted_on + seperator + posted_by_id + seperator + posted_by + seperator + "####";
            }
            System.out.println("Sent Comments of [" + complaint_id + "]: " + toSend);
            out.print(toSend);


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
