<%-- 
    Document   : Send
    Created on : 10 Apr, 2015, 11:56:07 PM
    Author     : Gaurav Kanade
--%>
<%@page import="java.sql.*;"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        
        <%System.out.println("Reached here");
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Project_8", "root", "abcd");
        %>
    </head>
    <body>
        <form name="toSend" action="Broadcaster" method="post">
            <h2>Send Data</h2>

            <b>Data 1</b>
            <input name="data1" type="text" />
            <br/><br/>
            <b>Data 2</b>

            <input name="data2" type="text"/>
            <br/><br/>
            <select name="user_id">
                <% String str3 = "SELECT * FROM USERS";
                    PreparedStatement prep3 = con.prepareStatement(str3);
                    ResultSet rs3 = prep3.executeQuery();
                    while (rs3.next()) {
                        String user_name = rs3.getString("USER_ID");
                        String name = rs3.getString("NAME");
                %>
                <option value="<%=user_name%>"><%=name%></option>
                <%
                    }
                %>
            </select>

            <input type="submit" value="Send"/>
        </form>
        <hr/>
        <form name="changeStatus" action="BroadcastChageStatus" method="post">
            <input type="hidden" name="data1" value="changeStatus"/>
            <h2>Change Status</h2>
            <table>
                <tr>
                    <td>
                        <select name="complaint_id">
                            <option value="0">Select Complaint</option>
                            <%  String str1 = "SELECT * FROM COMPLAINTS ORDER BY COMPLAINED_ON DESC";
                                PreparedStatement prep1 = con.prepareStatement(str1);
                                ResultSet rs1 = prep1.executeQuery();

                                while (rs1.next()) {
                                    int id = rs1.getInt("COMPLAINT_ID");
                                    String title = rs1.getString("TITLE");
                            %>
                            <option value="<%=id%>"><%=title%></option>
                            <%
                                }
                            %>
                        </select>
                    </td>

                    <td>
                        <select name="status">
                            <option value="Complaint Received" selected="true">Complaint Received</option>
                            <option value="Complaint Viewed">Complaint Viewed</option>
                            <option value="Under Process">Under Process</option>
                            <option value="Solved">Solved</option>
                            <option value="Rejected">Rejected</option>                                                        
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>
                        <select name="user_id">
                            <% String str4 = "SELECT * FROM USERS";
                                PreparedStatement prep4 = con.prepareStatement(str4);
                                ResultSet rs4 = prep4.executeQuery();
                                while (rs4.next()) {
                                    String user_name = rs4.getString("USER_ID");
                                    String name = rs4.getString("NAME");
                            %>
                            <option value="<%=user_name%>"><%=name%></option>
                            <%
                                }
                            %>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>
                        <input type="submit" value="Broadcast"/>
                    </td>
                </tr>
            </table>

        </form>
        <hr/>
        <form name ="SMSSender" action="Broadcaster">
            <h2>Send SMS</h2>

            <b>Number</b>
            <input name="number" type="text" />
            <br/><br/>
            <b>Data</b>
            <input name="data" type="text"/>
            <input type="submit" value="Send"/>
            <input type="hidden" name="data1" value="sendSMS"/>
            <input type="hidden" name="uid" value="0"/>
        </form>
        <hr/>
        <form name ="ActionSender" action="BroadcastNewAction">
            <h2>New Action Taken</h2>

            <b>Complaint</b>
            <table>
                <tr>
                    <td>
                        <select name="complaint_id">
                            <option value="0">Select Complaint</option>
                            <%  String str11 = "SELECT * FROM COMPLAINTS ORDER BY COMPLAINED_ON DESC";
                                PreparedStatement prep11 = con.prepareStatement(str11);
                                ResultSet rs11 = prep11.executeQuery();

                                while (rs11.next()) {
                                    int id = rs11.getInt("COMPLAINT_ID");
                                    String title = rs11.getString("TITLE");
                            %>
                            <option value="<%=id%>"><%=title%></option>
                            <%
                                }
                            %>
                        </select>
                    </td>
                    <td>
                        <input type="text" name="action_description"/>
                    </td>
                    <td>
                        <select name="authority_id">
                            <% String str7 = "SELECT * FROM AUTHORITIES ORDER BY USER_NAME";
                                PreparedStatement prep7 = con.prepareStatement(str7);
                                ResultSet rs7 = prep7.executeQuery();
                                while (rs7.next()) {
                                    String name = rs7.getString("USER_NAME");
                                    int auth_id = rs7.getInt("AUTHORITY_ID");
                            %>
                            <option value="<%=auth_id%>"><%=name%></option>
                            <%
                                }
                            %>
                        </select>
                </tr>
            </table>
            <input type="submit" value="Send"/>
            <input type="hidden" name="data1" value="newAction"/>
            <input type="hidden" name="uid" value="0"/>
        </form> 
        <hr/>
        <h2>Reset Password</h2>
        <form name="resetPassword" action="ForgotPasswordFromWebsite" method="post">
            <input name="email" type="text"/>
            <input type="submit" value="Reset"/>
        </form>
    </body>
</html>
