
import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.simple.*;

public class GetPublicQuestionSingle extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection(Utility.connection, Utility.username, Utility.password);

            String using_id = request.getParameter("user_id");
            String question_id = request.getParameter("question_id");
            String str1 = "SELECT * FROM PUBLIC_QUESTIONS WHERE QUESTION_ID=?";
            PreparedStatement prep1 = con.prepareStatement(str1);
            prep1.setString(1, question_id);
            ResultSet rs1 = prep1.executeQuery();

            System.out.println("Public Questions: User _id "+using_id);
            String id, question, option1, option2, option3, option4, option5, state_id, city_id, area_id, close_date, posted_by="", posted_on;
            JSONObject obj = new JSONObject();
            String posted_by_id="";
            while (rs1.next()) {

                System.out.println("Reached here0");
                option1 = rs1.getString("OPTION_1");
                option2 = rs1.getString("OPTION_2");
                option3 = rs1.getString("OPTION_3");
                option4 = rs1.getString("OPTION_4");
                option5 = rs1.getString("OPTION_5");
                posted_by_id = rs1.getString("POSTED_BY");
                boolean auth = rs1.getBoolean("POSTED_BY_AUTH");
                int count = 0;
                if (option1 != null) {
                    obj.put(count + "option", option1);
                    count++;
                }
                if (option2 != null) {
                    obj.put(count + "option", option2);
                    count++;
                }
                if (option3 != null) {
                    obj.put(count + "option", option3);
                    count++;
                }
                if (option4 != null) {
                    obj.put(count + "option", option4);
                    count++;
                }
                if (option5 != null) {
                    obj.put(count + "option", option5);
                    count++;
                }

                obj.put("number_of_options", count);
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
                String finaltime = "";
                finaltime += timeStamp.substring(0, 4) + "-" + timeStamp.substring(4, 6) + "-" + timeStamp.substring(6, 8) + " " + timeStamp.substring(9, 11) + ":" + timeStamp.substring(11, 13) + ":" + timeStamp.substring(13, 15);
                
                boolean closed = true;
                String str2 = "SELECT * FROM PUBLIC_QUESTIONS WHERE QUESTION_ID=? AND CLOSE_DATE>=?";
                PreparedStatement prep2 = con.prepareStatement(str2);
                prep2.setString(1, question_id);
                prep2.setString(2, finaltime);
                ResultSet rs2 = prep2.executeQuery();
                if (rs2.next()) {
                    closed = false;
                }
                obj.put("closed", closed);

                int option = -1;
                
                //Get statistics
                int votes[] = new int[count];
                for(int i=0;i<count;i++)
                    votes[i]=0;
                String str4 = "SELECT * FROM VOTES WHERE QUESTION_ID=?";
                PreparedStatement prep4 = con.prepareStatement(str4);
                prep4.setString(1, question_id);
                ResultSet rs4 = prep4.executeQuery();
                int total = 0;
                while(rs4.next()){
                    total++;
                    int option_no = rs4.getInt("OPTION_VOTED");
                    votes[option_no-1]++;
                    int user_id_voted = rs4.getInt("USER_ID");
                    if(Integer.parseInt(using_id)==user_id_voted)
                        option = option_no;
                }
                obj.put("option", option);
                if(option==-1)
                    obj.put("voted", false);
                else
                    obj.put("voted", true);
                for(int i=0;i<count;i++){
                    obj.put(i+"result",votes[i]);
                }
                obj.put("total",total);
                //posted by name
                if (auth) {
                    String str3 = "SELECT USER_NAME FROM AUTHORITIES WHERE AUTHORITY_ID=?";
                    PreparedStatement prep3 = con.prepareStatement(str3);
                    prep3.setString(1, posted_by_id);
                    ResultSet rs3 = prep3.executeQuery();
                    while (rs3.next()) {
                        posted_by = rs3.getString("USER_NAME")+"(Authority)";
                    }
                } else {
                    String str3 = "SELECT NAME FROM USERS WHERE USER_ID=?";
                    PreparedStatement prep3 = con.prepareStatement(str3);
                    prep3.setString(1, posted_by_id);
                    ResultSet rs3 = prep3.executeQuery();
                    while (rs3.next()) {
                        posted_by = rs3.getString("NAME");
                    }
                }
            }
            
            obj.put("posted_by", posted_by);
            
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
