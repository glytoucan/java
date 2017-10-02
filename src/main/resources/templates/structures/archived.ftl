<!DOCTYPE html>
<html lang="ja">
<head>
	<title>Glycan Repository</title>
<#include "../header.html">
<link rel="canonical" href="https://glytoucan.org/Structures/graphical" />
</head>
<body>
<a name="top"></a><!--link for page top-->
<div id="contents">
<#include "../nav.ftl">
<#include "../errormessage.ftl">

<div class="container">
<h1 class="page-header">Archived List</h1>

<#list archivedList>
  <div>
    <table class="table table-bordered table-striped table-hover">
    <tr>
      <td>#</td>
      <td>${accessionNumber}</td>
    </tr>


<#items as archivedItem>
    <tr>
      <td>
        ${archivedItem?counter}
      </td>
      <td>
        ${archivedItem}
      </td>
    </tr>
</#items>
    </table>
  </div>
  
</#list>


</div>
</div>
<#include "../footer.html">
</div><!-- contents -->
</body>
</html>