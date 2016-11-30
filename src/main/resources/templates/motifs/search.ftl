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
  <h1 class="page-header">${Title[0]}
  	<a class="userGuide" href="${Title[2]}" target="_blank">
		<img class="icon_userGuide clearfix" src="/img/icon_userGuide.png" height="48" width="24" alt="" />
		<span class="text_userGuide"><img src="/img/text_userGuide.png" height="40" width="80" alt="" /></span>
	</a>
  </h1>

<h4>${TopTitle[0]}</h4>

    <div id="motifSearchApp" data-notation="${imageNotation}">
	  <h3>${Top[0]}: <span class="motifSearch_count"></span></h3>

        <div class="container">
            <table class="table table-striped motifSearch_table">
                <thead><tr><th>${Name}</th><th>${sequence}</th><th>${Reducing}</th><th>${Frequency}</th></tr></thead>
                <tbody></tbody>
            </table>
        </div>
    </div><!--/#motifSearchApp-->
</div>
<#include "../footer.html">
</div><!-- contents -->
</body>
</html>
    