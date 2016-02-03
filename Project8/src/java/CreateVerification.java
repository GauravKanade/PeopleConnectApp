
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class CreateVerification extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection(Utility.connection, Utility.username, Utility.password);
            String number = request.getParameter("phone");

            boolean exists = false;
            String str1 = "SELECT USER_ID FROM USERS WHERE PHONE_NUMBER=?";
            PreparedStatement prep1 = con.prepareStatement(str1);
            prep1.setString(1, number);
            ResultSet rs1 = prep1.executeQuery();
            if (rs1.next()) {
                exists = true;
            }
            if (exists) {
                out.print("000000");
            } else {
                int verification = (int) (Math.random() * 953564 % 999999);
                System.out.println("Number " + number + "\nVerification: " + verification);
                SMSProvider.sendSMS(number, "Your One Time Verification Code for PeopleConnect Is " + verification);
                out.print(verification);
            }

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
