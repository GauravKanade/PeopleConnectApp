
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.simple.*;

public class CheckEmailPhone extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection(Utility.connection, Utility.username, Utility.password);
            String phone = request.getParameter("phone");
            String email = request.getParameter("email");
            boolean error = false;
            String str1 = "SELECT * FROM USERS WHERE EMAIL_ID=?";
            PreparedStatement prep1 = con.prepareStatement(str1);
            prep1.setString(1, email);
            ResultSet rs1 = prep1.executeQuery();
            if (rs1.next()) {
                error = true;
                out.print("Email ID already in use!!");
            }
            String str2 = "SELECT * FROM USERS WHERE PHONE_NUMBER=?";
            PreparedStatement prep2 = con.prepareStatement(str2);
            prep2.setString(1, phone);
            ResultSet rs2 = prep2.executeQuery();
            if (rs2.next()) {
                error = true;
                out.print("Phone Number already linked to another user!!");
            }
            String str3 = "SELECT * FROM AUTHORITIES WHERE EMAIL_ID=?";
            PreparedStatement prep3 = con.prepareStatement(str3);
            prep3.setString(1, email);
            ResultSet rs3 = prep3.executeQuery();
            if (rs3.next()) {
                error = true;
                out.print("Email ID already in use!!");
            }
            String str4 = "SELECT * FROM AUTHORITIES WHERE PHONE_NUMBER=?";
            PreparedStatement prep4 = con.prepareStatement(str4);
            prep4.setString(1, phone);
            ResultSet rs4 = prep4.executeQuery();
            if (rs4.next()) {
                error = true;
                out.print("Phone Number already linked to another user!!");
            }
            if (!error) {
                out.print("Allowed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
