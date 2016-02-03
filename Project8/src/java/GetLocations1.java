
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.simple.*;

public class GetLocations1 extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection(Utility.connection, Utility.username, Utility.password);
            String str1 = "SELECT * FROM STATES WHERE STATE_ID<>? ORDER BY STATE_NAME";
            PreparedStatement prep1 = con.prepareStatement(str1);
            prep1.setInt(1, 0);
            ResultSet rs1 = prep1.executeQuery();

            int n = 0;
            rs1.last();
            n = rs1.getRow();
            rs1.beforeFirst();

            JSONObject data = new JSONObject();
            data.put("number_of_states", n);
            int number_of_areas = 0;
            int number_of_cities = 0;
            for (int i = 0; i < n; i++) {
                rs1.next();
                String state = rs1.getString("STATE_NAME");
                String state_id = rs1.getString("STATE_ID");
                data.put(i + "state_name", state);

                String str2 = "SELECT * FROM CITY WHERE STATE_ID=? ORDER BY CITY_NAME";
                PreparedStatement prep2 = con.prepareStatement(str2);
                prep2.setString(1, state_id);
                ResultSet rs2 = prep2.executeQuery();

                rs2.last();
                int m = rs2.getRow();
                rs2.beforeFirst();
                data.put(i + "number_of_cities", m);

                for (int j = 0; j < m; j++) {
                    rs2.next();
                    String city_id = rs2.getString("CITY_ID");
                    String city = rs2.getString("CITY_NAME");
                    data.put(i + "," + j + "city_name", city);

                    String str3 = "SELECT * FROM AREA WHERE CITY_ID=? ORDER BY AREA_NAME";
                    PreparedStatement prep3 = con.prepareStatement(str3);
                    prep3.setString(1, city_id);
                    ResultSet rs3 = prep3.executeQuery();
                    rs3.last();
                    int o = rs3.getRow();
                    rs3.beforeFirst();

                    data.put(i + "," + j + "number_of_areas", o);
                    for (int k = 0; k < o; k++) {
                        number_of_areas++;
                        rs3.next();
                        String area = rs3.getString("AREA_NAME");
                        data.put(i + "," + j + "," + k + "area_name", area);

                    }
                }
            }
            data.put("total_number_of_areas", number_of_areas);
            data.put("total_no_of_cities", number_of_cities);
            String toSend = data.toJSONString();
            System.out.println("Locations: " + toSend);
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
