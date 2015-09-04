<%@page import="java.io.*" %>
<%@page import="java.net.*" %>
<%@page import="java.net.URLEncoder" %>

<%
   String recv = "";
   StringBuffer recvbuff = new StringBuffer();
   String url = request.getParameter("url");
   url = url.replaceAll("\\s+","+");
   
   URL datapage = new URL(url);
   
   System.out.println(request.getParameter("url"));
   URLConnection urlcon = datapage.openConnection();
   BufferedReader buffread = new BufferedReader(new InputStreamReader(urlcon.getInputStream()));

   while ((recv = buffread.readLine()) != null) {
    recvbuff.append(recv).append("\n");
   }
   buffread.close();
   
//   System.out.println("recvbuff:" + recvbuff + "<");
   
%><%= recvbuff %>