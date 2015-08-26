<!DOCTYPE html>
<html lang="ja">
<head>
	<title>Glycan Repository</title>
<#include "../header.html">
</head>
<body>
<a name="top"></a><!--link for page top-->
<div id="contents">
<#include "../nav.ftl">
<div class="container">
<#include "../error.ftl">

<#-- contents -->


<h1 class="page-header">${Title[0]}</h1>
<div>
<table class="table table-bordered table-striped table-hover">
<tr><td>#</td>
<td>Structure</td>
<td>Image</td>
<td>Status</td>
</tr>

<#list registeredList as newItem>
<tr>
<td>
${newItem?counter}
</td>
<td width="30%">

${origList[newItem?index]?html?replace("\\n", "<br>")}

</td>
<td width="60%">
<img src="${imageList[newItem?index]}" />
</td>
<td>
<#if resultList[newItem?index]?starts_with("ERROR")>
${resultList[newItem?index]}
<#else>
New ID:<a href="/Structures/glycans/${resultList[newItem?index]}">${resultList[newItem?index]}</a>
</#if>
</td>
</tr>

</#list>

</table>
</div>



<#-- end contents -->

</div>
<#include "../footer.html">
</div>
</body>
</html>