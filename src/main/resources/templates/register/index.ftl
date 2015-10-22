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
<div class="container-fluid">
	<div class="row-fluid">
		<div class="span6">

			<form class="well" action="/Registries/confirmation"
				id="confirmationForm" method="post" accept-charset="utf-8">
				<div style="display: none;">
					<input type="hidden" name="_method" value="POST" />
				</div>
				${Left[0]}
<@spring.formTextarea 'sequence.sequence' 'id="sequence" placeholder="Sequence" cols="20" rows="15" style="width: 370px; height: 600px;"' />
<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				<div class="submit">
					<input class="btn btn-primary" type="submit" value="${submit}" />
					<button type="reset" class="btn">${clear}</button>
				</div>
			</form>
		</div>

		<div class="span6">
			<div class="panel ">
				<div class="panel-heading">
					<label> ${RightTitle[0]}
					</label>
				</div>
				<div class="panel-body">
					${Right[0]?html?replace("\r\n", "<br>")}
				</div>
			</div>
		</div>
	</div>
</div>
</div>

<#include "../footer.html">
</div>
</body>
</html>