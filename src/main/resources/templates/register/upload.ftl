<!DOCTYPE html>
<#import "/spring.ftl" as spring />
<html lang="ja">
<head>
	<title>Glycan Repository</title>
<#include "../header.html">
<link rel="canonical" href="https://glytoucan.org/Structures" />
</head>
<body>
<a name="top"></a><!--link for page top-->
<div id="contents">
<#include "../nav.ftl">

<#include "../errormessage.ftl">
<div class="container">

<h1 class="page-header">${Title[0]}
	<a class="userGuide" href="${Title[2]}" target="_blank">
		<img class="icon_userGuide clearfix" src="/img/icon_userGuide.png" height="48" width="24" alt="" />
		<span class="text_userGuide"><img src="/img/text_userGuide.png" height="40" width="80" alt="" /></span>
	</a>
</h1>
<br>
<p>
${Top[0]}
<a href="/Registries/index">${Top[1]}</a>


	<form method="POST" enctype="multipart/form-data"
		action="/Registries/upload">
		File to upload: <input type="file" name="file"><br /> 
<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
 
		<input type="submit" value="${submit}">
	</form>
</p>

	</div>


</div>

<#include "../footer.html">
</div>
</body>
</html>