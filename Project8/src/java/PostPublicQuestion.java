import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.servlet.*;
import javax.servlet.http.*;


public class PostPublicQuestion extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection(Utility.connection, Utility.username, Utility.password);
            
            int user_id = Integer.parseInt(request.getParameter("user_id"));
            String question = request.getParameter("question");
            int n = Integer.parseInt(request.getParameter("no_of_options"));
            String option1 =request.getParameter("option1");
            String option2 =request.getParameter("option2");
            String option3 =request.getParameter("option3");
            String option4 =request.getParameter("option4");
            String option5 =request.getParameter("option5");
            int recipient_type = Integer.parseInt(request.getParameter("recipient_type"));
            String closingDate = request.getParameter("closing_date");
            
            String options[] = new String[5];
            int i=0;
            if(option1.length()!=0)
                options[i++] = option1;
            if(option2.length()!=0)
                options[i++] = option2;
            if(option3.length()!=0)
                options[i++] = option3;
            if(option4.length()!=0)
                options[i++] = option4;
            if(option5.length()!=0)
                options[i++] = option5;
            
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            String finaltime = "";
            finaltime += timeStamp.substring(0, 4) + "-" + timeStamp.substring(4, 6) + "-" + timeStamp.substring(6, 8) + " " + timeStamp.substring(9, 11) + ":" + timeStamp.substring(11, 13) + ":" + timeStamp.substring(13, 15);

            int state=0, city=0, area=0;
            String recipients="";
            if(recipient_type==1){ // my area
                String str2 = "SELECT AREA_ID FROM USERS WHERE USER_ID=?";
                PreparedStatement prep2 = con.prepareStatement(str2);
                prep2.setInt(1, user_id);
                ResultSet rs2 = prep2.executeQuery();
                while(rs2.next()){
                    area  = rs2.getInt("AREA_ID");
                    String str3 = "SELECT AREA_NAME FROM AREA WHERE AREA_ID=?";
                    PreparedStatement prep3 = con.prepareStatement(str3);
                    prep3.setInt(1, area);
                    ResultSet rs3 = prep3.executeQuery();
                    while(rs3.next())
                        recipients = rs3.getString("AREA_NAME");
                            
                }
            } else if (recipient_type==2){ // my city
                String str2 = "SELECT CITY_ID FROM USERS WHERE USER_ID=?";
                PreparedStatement prep2 = con.prepareStatement(str2);
                prep2.setInt(1, user_id);
                ResultSet rs2 = prep2.executeQuery();
                while(rs2.next()){
                    city  = rs2.getInt("CITY_ID");
                    String str3 = "SELECT CITY_NAME FROM CITY WHERE CITY_ID=?";
                    PreparedStatement prep3 = con.prepareStatement(str3);
                    prep3.setInt(1, city);
                    ResultSet rs3 = prep3.executeQuery();
                    while(rs3.next())
                        recipients = rs3.getString("CITY_NAME");
                }
            }
            else if(recipient_type==3){//my state
                String str2 = "SELECT STATE_ID FROM USERS WHERE USER_ID=?";
                PreparedStatement prep2 = con.prepareStatement(str2);
                prep2.setInt(1, user_id);
                ResultSet rs2 = prep2.executeQuery();
                while(rs2.next()){
                    state  = rs2.getInt("STATE_ID");
                    
                    String str3 = "SELECT STATE_NAME FROM STATES WHERE STATE_ID=?";
                    PreparedStatement prep3 = con.prepareStatement(str3);
                    prep3.setInt(1, state);
                    ResultSet rs3 = prep3.executeQuery();
                    while(rs3.next())
                        recipients = rs3.getString("STATE_NAME");
                }
            } else {
                recipients = "India";
            }
            
            String str1 = "INSERT INTO PUBLIC_QUESTIONS(QUESTION_BODY,OPTION_1, ";
            str1+="OPTION_2, OPTION_3, OPTION_4, OPTION_5,STATE_ID, CITY_ID, AREA_ID, CLOSE_DATE,";
            str1+="POSTED_BY, POSTED_ON, POSTED_BY_AUTH) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement prep1 = con.prepareStatement(str1);
            prep1.setString(1, question);
            prep1.setString(2, options[0]);
            prep1.setString(3, options[1]);
            prep1.setString(4, options[2]);
            prep1.setString(5, options[3]);
            prep1.setString(6, options[4]);
            prep1.setInt(7, state);
            prep1.setInt(8, city);
            prep1.setInt(9, area);
            prep1.setString(10, closingDate);
            prep1.setInt(11, user_id);
            prep1.setString(12, finaltime);
            prep1.setBoolean(13, false);
            prep1.executeUpdate();
            
            String str2 = "SELECT QUESTION_ID FROM PUBLIC_QUESTIONS WHERE QUESTION_BODY=? AND POSTED_BY=?";
            PreparedStatement prep2 = con.prepareStatement(str2);
            prep2.setString(1, question);
            prep2.setInt(2, user_id);
            ResultSet rs2 = prep2.executeQuery();
            String question_id="0";
            while(rs2.next()){
                question_id = rs2.getString("QUESTION_ID");
            }
            
            String str3 = "INSERT INTO ARCHIVE_VOTES (QUESTION_ID, OPTION_1, OPTION_2, OPTION_3, OPTION_4, OPTION_5) VALUES (?,?,?,?,?,?)";
            PreparedStatement prep3 = con.prepareStatement(str3);
            prep3.setString(1, question_id);
            prep3.setInt(2, 0);
            prep3.setInt(3, 0);
            prep3.setInt(4, 0);
            prep3.setInt(5, 0);
            prep3.setInt(6, 0);
            prep3.executeUpdate();                    
            
            BroadcastNewQuestion.broadcastQuestion(question_id , recipients, user_id);
            out.print(recipients);
            System.out.println("Success!!");
            
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
