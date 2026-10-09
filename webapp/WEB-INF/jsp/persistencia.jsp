<% String pageTitle = "Persistencia"; %>
<%@ include file="header.jsp" %>
<%@ page import="com.ebay.cmp.ItemVO" %>
<%
    String cmpError = (String) request.getAttribute("cmpError");
%>

<FONT SIZE="4"><B>HashMap vs. entity beans</B></FONT><BR>
<FONT SIZE="2" COLOR="#666666">The same catalogue held two ways. Measured on
this request.</FONT><BR><BR>

<% if (cmpError != null) { %>
  <TABLE WIDTH="100%" CELLPADDING="6" CELLSPACING="0" BORDER="0" CLASS="err">
  <TR><TD><FONT SIZE="2"><B>CMP error:</B> <ebay:esc value="<%= cmpError %>"/></FONT></TD></TR>
  </TABLE><BR>
<% } else { %>

<TABLE WIDTH="100%" CELLPADDING="4" CELLSPACING="1" BORDER="0" BGCOLOR="#CCCCCC">
<TR CLASS="hdr"><TD COLSPAN="2">1 &#149; The same query, both tiers</TD></TR>
<TR BGCOLOR="#FFFFFF"><TD COLSPAN="2"><FONT SIZE="1">
  <%= request.getAttribute("rowCount") %> listings copied into the database
  (<%= request.getAttribute("seeded") %> created on this request).
  Question asked of both: <I>which listings are in category 11?</I>
</FONT></TD></TR>
<TR BGCOLOR="#FFFFFF" VALIGN="TOP">
  <TD WIDTH="50%"><FONT SIZE="2"><B>HashMap</B><BR>
    <FONT SIZE="1">A loop you write, over state you own.</FONT><BR><BR>
    <TT><FONT SIZE="1">for (item : house.allItems())<BR>
    &nbsp;&nbsp;if (item.getCategoryId() == 11) ...</FONT></TT><BR><BR>
    <FONT SIZE="2"><B><%= request.getAttribute("memoryHits") %></B> hits in
    <B><%= request.getAttribute("memoryMicros") %></B> &#181;s</FONT>
  </TD>
  <TD WIDTH="50%"><FONT SIZE="2"><B>CMP entity bean</B><BR>
    <FONT SIZE="1">A finder the container implements from EJB-QL.</FONT><BR><BR>
    <TT><FONT SIZE="1">SELECT OBJECT(i) FROM ItemSchema i<BR>
    &nbsp;&nbsp;WHERE i.categoryId = ?1</FONT></TT><BR><BR>
    <FONT SIZE="2"><B><%= request.getAttribute("cmpHits") %></B> hits in
    <B><%= request.getAttribute("cmpMicros") %></B> &#181;s</FONT>
    <BR><FONT SIZE="1">(includes the round trip and building value objects)</FONT>
  </TD>
</TR>
</TABLE>
<BR>

<TABLE WIDTH="100%" CELLPADDING="4" CELLSPACING="1" BORDER="0" BGCOLOR="#CCCCCC">
<TR CLASS="hdr"><TD COLSPAN="2">2 &#149; Other finders, all declared in XML</TD></TR>
<TR BGCOLOR="#FFFFFF" VALIGN="TOP">
  <TD WIDTH="50%"><FONT SIZE="1"><TT>WHERE i.currentCents &gt; ?1</TT> (100.00)<BR><BR>
  <%
    List dearer = (List) request.getAttribute("dearer");
    for (int i = 0; dearer != null && i < dearer.size() && i < 6; i++) {
      ItemVO v = (ItemVO) dearer.get(i);
  %>
    <ebay:esc value="<%= v.getTitle() %>"/> &#151; <ebay:money cents="<%= v.getCurrentCents().longValue() %>"/><BR>
  <% } %>
  </FONT></TD>
  <TD WIDTH="50%"><FONT SIZE="1"><TT>WHERE i.title LIKE ?1</TT> ('%Beanie%')<BR><BR>
  <%
    List tl = (List) request.getAttribute("titleLike");
    for (int i = 0; tl != null && i < tl.size() && i < 6; i++) {
      ItemVO v = (ItemVO) tl.get(i);
  %>
    <ebay:esc value="<%= v.getTitle() %>"/><BR>
  <% } %>
  </FONT></TD>
</TR>
</TABLE>
<BR>

<TABLE WIDTH="100%" CELLPADDING="4" CELLSPACING="1" BORDER="0" BGCOLOR="#CCCCCC">
<TR CLASS="hdr"><TD>3 &#149; Changing a field without saving it</TD></TR>
<TR BGCOLOR="#FFFFFF"><TD><FONT SIZE="2">
  <TT><FONT SIZE="1">row.setCurrentCents(before + 100);</FONT></TT><BR>
  <FONT SIZE="1">There is no save, no flush and no update statement anywhere in
  this project. The container wrote the row back when the facade's transaction
  committed. Reload and watch it climb by a peso each time.</FONT><BR><BR>
  <TT><FONT SIZE="2"><%= request.getAttribute("dirty") %></FONT></TT>
</TD></TR>
</TABLE>
<BR>

<TABLE WIDTH="100%" CELLPADDING="4" CELLSPACING="1" BORDER="0" BGCOLOR="#CCCCCC">
<TR CLASS="hdr"><TD>4 &#149; Walking the relationship: the n+1 problem</TD></TR>
<TR BGCOLOR="#FFFFFF"><TD><FONT SIZE="2">
  One finder, then one visit per row to fetch its bids through the
  container-managed relationship:<BR><BR>
  <FONT SIZE="3"><B><%= request.getAttribute("walkItems") %></B> listings,
  <B><%= request.getAttribute("walkBids") %></B> bids,
  <FONT COLOR="#CC0000"><B><%= request.getAttribute("walkTrips") %></B> round
  trips</FONT></FONT>
  <BR><BR><FONT SIZE="1">
  n listings cost n+1 visits, and nothing in EJB-QL lets you ask for the
  listings and their bids together. This shape, repeated across a real domain
  model, is the single most cited reason the industry left CMP for Hibernate
  from 2001 onward and replaced entity beans outright with JPA in 2006.
  </FONT>
</TD></TR>
</TABLE>
<BR>

<TABLE WIDTH="100%" CELLPADDING="4" CELLSPACING="1" BORDER="0" BGCOLOR="#CCCCCC">
<TR CLASS="hdr"><TD>5 &#149; What the container generated</TD></TR>
<TR BGCOLOR="#FFFFFF"><TD><FONT SIZE="1"><TT>
  <%= request.getAttribute("generated") %>
</TT><BR><BR>
  You wrote an abstract class with abstract accessors. The concrete subclass,
  the table, the columns, the foreign key and every query were produced at
  deployment time from the descriptor.
</FONT></TD></TR>
</TABLE>

<% } %>

<%@ include file="footer.jsp" %>
