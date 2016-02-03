
import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class ReceiveComplaint extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection(Utility.connection, Utility.username, Utility.password);
            String user_id = "", title = "", description = "", image = "", category = "";
            user_id = request.getParameter("user_id");
            description = request.getParameter("description");
            String imgEncodedStr = request.getParameter("image");
            String fileName = request.getParameter("filename");
            category = request.getParameter("category");
            title = request.getParameter("title");
            String lat = request.getParameter("latitude");
            String lon = request.getParameter("longitude");
            String address = request.getParameter("address");
            String state = request.getParameter("state");
            String city = request.getParameter("city");
            String country = request.getParameter("country");

            System.out.println("Data received:\nUser_id: " + user_id + "\nTitle: " + title + "\nDescription: " + description + "\ncategory: " + category + "\nImage: " + image + "\nPosition: " + lat + ", " + lon + "\nAddress: " + address + "\nState: " + state + "\nCountry: " + country);
            //PUT TO DATABASE
            if (country.equalsIgnoreCase("India")) {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
                String finaltime = "";
                finaltime += timeStamp.substring(0, 4) + "-" + timeStamp.substring(4, 6) + "-" + timeStamp.substring(6, 8) + " " + timeStamp.substring(9, 11) + ":" + timeStamp.substring(11, 13) + ":" + timeStamp.substring(13, 15);

                String state_id = "0";
                String str6 = "SELECT * FROM STATES WHERE STATE_NAME=?";
                PreparedStatement prep6 = con.prepareStatement(str6);
                prep6.setString(1, state);
                ResultSet rs6 = prep6.executeQuery();
                if (rs6.next()) {
                    state_id = rs6.getString("STATE_ID");
                }

                String city_id = "0";
                String str7 = "SELECT * FROM CITY WHERE CITY_NAME=? AND STATE_ID=?";
                PreparedStatement prep7 = con.prepareStatement(str7);
                prep7.setString(1, city);
                prep7.setString(2, state_id);
                ResultSet rs7 = prep7.executeQuery();
                if (rs7.next()) {
                    city_id = rs7.getString("CITY_ID");
                }

                String str1 = "INSERT INTO COMPLAINTS(COMPLAINT_BODY, COMPLAINED_BY, STATUS, COMPLAINED_ON, IMAGE, TITLE, CATEGORY,COMPLAINT_LOCATION, STATE_ID, CITY_ID) VALUES (?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement prep1 = con.prepareStatement(str1);
                prep1.setString(1, description);
                prep1.setString(2, user_id);
                prep1.setString(3, "Complaint Received");
                prep1.setString(4, finaltime);
                prep1.setString(5, "/images/" + fileName + ".jpg");
                prep1.setString(6, title);
                prep1.setString(7, category);
                prep1.setString(8, address);
                prep1.setString(9, state_id);
                prep1.setString(10, city_id);
                prep1.execute();

                //get the ID from the Database and return tht to the user
                String str2 = "SELECT COMPLAINT_ID FROM COMPLAINTS WHERE COMPLAINED_BY=? AND COMPLAINED_ON=?";
                PreparedStatement prep2 = con.prepareStatement(str2);
                prep2.setString(1, user_id);
                prep2.setString(2, finaltime);

                ResultSet rs2 = prep2.executeQuery();
                int complaint_id = 0;
                while (rs2.next()) {
                    complaint_id = rs2.getInt("COMPLAINT_ID");
                }

                // CODE TO BEGIN BROADCAST
                BroadcastNewComplaint.broadcast("" + complaint_id, user_id);
                System.out.println("Filename: " + fileName);
                if (imgEncodedStr != null) {
                    ManipulateImage.convertStringtoImage(imgEncodedStr, fileName);
                    out.println(complaint_id + "$Complaint Registed Successfully");
                } else {
                    out.print("Complaint registry failed!! Try Again");
                }
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
