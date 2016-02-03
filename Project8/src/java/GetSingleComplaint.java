
//import com.google.common.io.Files;
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.awt.Image.*;

public class GetSingleComplaint extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection(Utility.connection, Utility.username, Utility.password);
            String id = request.getParameter("complaint_id");

            String str1 = "SELECT * FROM COMPLAINTS WHERE COMPLAINT_ID=?";
            PreparedStatement prep1 = con.prepareStatement(str1);
            prep1.setString(1, id);
            ResultSet rs1 = prep1.executeQuery();

            String title = "", description = "", location = "", status = "", category = "", category_id = "", image = "";
            while (rs1.next()) {
                title = rs1.getString("TITLE");
                description = rs1.getString("COMPLAINT_BODY");
                location = rs1.getString("COMPLAINT_LOCATION");
                status = rs1.getString("STATUS");
                category_id = rs1.getString("CATEGORY");
                image = rs1.getString("IMAGE");

                String str2 = "SELECT CATEGORY_NAME FROM CATEGORIES WHERE CATEGORY_ID=?";
                PreparedStatement prep2 = con.prepareStatement(str2);
                prep2.setString(1, category_id);
                ResultSet rs2 = prep2.executeQuery();
                while (rs2.next()) {
                    category = rs2.getString("CATEGORY_NAME");
                }
                /*
                 * File file = new File(image); FileInputStream fis = new
                 * FileInputStream(file);
                 *
                 * ByteArrayOutputStream bos = new ByteArrayOutputStream();
                 * byte[] buf = new byte[1024]; try { for (int readNum; (readNum
                 * = fis.read(buf)) != -1;) { //Writes to this byte array output
                 * stream bos.write(buf, 0, readNum); System.out.println("read "
                 * + readNum + " bytes,"); } } catch (IOException ex) {
                 * System.out.println(ex.getMessage()); }
                 *
                 * byte[] bytes = bos.toByteArray();
                 *
                 * String imageString = new String(bytes);
                 * System.out.println("Image: " + imageString);
                 */
            }
            String seperator = "$%#$";
            String toSend = description + seperator + location + seperator + status + seperator + category + seperator + image + seperator;
            System.out.println("GetSingleComplaint[" + id + "]: " + toSend);
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
