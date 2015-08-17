<%@page import="java.io.*" %>
<%@page import="java.net.*" %>
<%
   String recv = "";
   String recvbuff = "";
   URL datapage = new URL(request.getParameter("url"));
   System.out.println(request.getParameter("url"));
   URLConnection urlcon = datapage.openConnection();
   BufferedReader buffread = new BufferedReader(new InputStreamReader(urlcon.getInputStream()));

   while ((recv = buffread.readLine()) != null) {
    recvbuff += recv;
   }
   buffread.close();
%>
<%= recvbuff %>