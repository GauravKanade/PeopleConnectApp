
import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.simple.*;

public class GetMyPublicQuestions extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection(Utility.connection, Utility.username, Utility.password);

            String using_id = request.getParameter("user_id");
            
            String user_state_id="0", user_city_id="0", user_area_id="0";
            
            String str1 = "SELECT * FROM PUBLIC_QUESTIONS WHERE POSTED_BY=? AND POSTED_BY_AUTH=? ORDER BY POSTED_ON DESC";
            PreparedStatement prep1 = con.prepareStatement(str1);
            prep1.setString(1, using_id);
            prep1.setBoolean(2, false);
            ResultSet rs1 = prep1.executeQuery();

            String id, question, option1, option2, option3, option4, option5, state_id, city_id, area_id, close_date, posted_by, posted_on;
            JSONObject obj = new JSONObject();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            String finaltime = "";
            finaltime += timeStamp.substring(0, 4) + "-" + timeStamp.substring(4, 6) + "-" + timeStamp.substring(6, 8) + " " + timeStamp.substring(9, 11) + ":" + timeStamp.substring(11, 13) + ":" + timeStamp.substring(13, 15);


            rs1.last();
            int n = rs1.getRow();
            rs1.beforeFirst();
            boolean voted;
            obj.put("n", "" + n);
            for (int i = 0; i < n; i++) {
                rs1.next();
                question = rs1.getString("QUESTION_BODY");
                close_date = rs1.getString("CLOSE_DATE");
                posted_on = rs1.getString("POSTED_ON");
                String posted_by_id = rs1.getString("POSTED_BY");
                id = rs1.getString("QUESTION_ID");
                state_id = rs1.getString("STATE_ID");
                city_id = rs1.getString("CITY_ID");
                area_id = rs1.getString("AREA_ID");
                String title = "";
                if(question.length()>15)
                    title =question.substring(0,14)+"...";
                else 
                    title=question;
                //voted or not
                voted = false;
                String str2 = "SELECT * FROM VOTES WHERE USER_ID=? AND QUESTION_ID=?";
                PreparedStatement prep2 = con.prepareStatement(str2);
                prep2.setString(1, using_id);
                prep2.setString(2, id);
                ResultSet rs2 = prep2.executeQuery();
                while (rs2.next()) {
                    voted = true;
                }
                //posted by name
                String str3 = "SELECT NAME FROM USERS WHERE USER_ID=?";
                PreparedStatement prep3 = con.prepareStatement(str3);
                prep3.setString(1, posted_by_id);
                ResultSet rs3 = prep3.executeQuery();
                while (rs3.next()) {
                    posted_by = rs3.getString("NAME");
                }


                //area
                String area = "";
                if (!area_id.equals("0")) {
                    String str4 = "SELECT AREA_NAME FROM AREA WHERE AREA_ID=?";
                    PreparedStatement prep4 = con.prepareStatement(str4);
                    prep4.setString(1, area_id);
                    ResultSet rs4 = prep4.executeQuery();
                    while (rs4.next()) {
                        area = rs4.getString("AREA_NAME");
                    }
                } else if (!city_id.equals("0")) {
                    String str4 = "SELECT CITY_NAME FROM CITY WHERE CITY_ID=?";
                    PreparedStatement prep4 = con.prepareStatement(str4);
                    prep4.setString(1, city_id);
                    ResultSet rs4 = prep4.executeQuery();
                    while (rs4.next()) {
                        area = rs4.getString("CITY_NAME");
                    }
                } else if (!state_id.equals("0")) {
                    String str4 = "SELECT STATE_NAME FROM STATES WHERE STATE_ID=?";
                    PreparedStatement prep4 = con.prepareStatement(str4);
                    prep4.setString(1, state_id);
                    ResultSet rs4 = prep4.executeQuery();
                    while (rs4.next()) {
                        area = rs4.getString("STATE_NAME");
                    }
                } else {
                    area = "India";
                }
                posted_on = Utility.getDateFormat(posted_on);
                close_date = Utility.getDateFormat(close_date);

                boolean closed = true;
                String str6 = "SELECT * FROM PUBLIC_QUESTIONS WHERE QUESTION_ID=? AND CLOSE_DATE>=?";
                PreparedStatement prep6 = con.prepareStatement(str6);
                prep6.setString(1, id);
                prep6.setString(2, finaltime);
                ResultSet rs6 = prep6.executeQuery();
                if (rs6.next()) {
                    closed = false;
                }
                obj.put(i + "closed", closed);
                obj.put(i + "title", title);
                obj.put(i + "question", question);
                obj.put(i + "date", posted_on);
                obj.put(i + "area", area);
                obj.put(i + "id", id);
                obj.put(i + "voted", voted);
                obj.put(i + "closingdate", close_date);
            }
            
            String str = "SELECT POINTS FROM USERS WHERE USER_ID=?";
            PreparedStatement prep = con.prepareStatement(str);
            prep.setString(1, using_id);
            ResultSet rs = prep.executeQuery();
            while(rs.next()){
                obj.put("points", rs.getInt("POINTS"));
            }
            String toSend = obj.toJSONString();

            out.print(toSend);
            System.out.println(toSend);
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
