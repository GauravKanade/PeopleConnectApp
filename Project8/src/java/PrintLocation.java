
import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.servlet.*;
import javax.servlet.http.*;

public class PrintLocation extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            String finaltime = "";
            finaltime += timeStamp.substring(0, 4) + "-" + timeStamp.substring(4, 6) + "-" + timeStamp.substring(6, 8) + " " + timeStamp.substring(9, 11) + ":" + timeStamp.substring(11, 13) + ":" + timeStamp.substring(13, 15);

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection(Utility.connection, Utility.username, Utility.password);
            String latitude = request.getParameter("lat");
            String longitude = request.getParameter("long");
            String address = request.getParameter("address");

            System.out.println("time: " + finaltime + "\nlat: " + latitude + "\nlong: " + longitude + "\n address: " + address);

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
