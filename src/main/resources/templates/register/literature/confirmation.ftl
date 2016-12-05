<!DOCTYPE html>
<#import "/spring.ftl" as spring />
<html lang="ja">
<head>
	<title>Confirmation</title>
	<#include "/header.html">
	<meta name="description" content="Glycan Structure Literature Publication pubmed id registration linking" />
	<link rel="stylesheet" type="text/css" href="/assets/css/entry.css">
	<script src="//code.jquery.com/jquery-1.10.2.min.js"></script>
	<script src="/assets/components/webcomponentsjs/webcomponents.min.js"></script>
	<link rel="import" href="/stanza/summary/">
	<link rel="import" href="/stanza/literature/">
</head>
<body>
<!--link for page top-->
<a name="top"></a>
<div id="contents">
 <#include "/nav.ftl">
 <#include "/errormessage.ftl">
 <div class="container">

	<article class="entryNew">
		<section class="entryNew_summary">
			<h1 class="entryNew_heading">${accNum}</h1>
			<togostanza-summary acc="${accNum}" notation="${imageNotation}"></togostanza-summary>
		</section>
		<div class="entryNew_content">
			<nav class="entryNav">
				<ol class="entryNav_ol">
					<li><a href="#literature">${literature}</a></li>
				</ol>
			</nav>
			<div class="entryNew_right">
				<section id="literature" class="entryNew_section">
					<h1 class="entryNew_heading">${confirmationTitlePublicationId}: ${literature}</h1>
					<form action="/Registries/supplement/${accNum}/complete" method="post" accept-charset="utf-8">
					
					<table>
					<tbody>
					<tr><th></th><td>${literatureTitle}</td></tr>
					</tbody></table>
					<h1 class="entryNew_heading">${submitCharactersImage}:</h1>
						<div>
							<img src="/Captcha/image">
						</div>
						<div>
							<input type="text" name="captcha" id="captcha" value="${captcha!""}" />
						</div>
						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" /> 
						<input type="hidden" name="accessionNumber" value="${accNum}" />
						<input type="hidden" name="literatureId" value="${literatureId}" />
						<input type="hidden" name="literatureTitle" value="${literatureTitle}" />

						<div class="submit">
							<input class="btn btn-primary" type="submit" value="${submit}" />
						</div>
					</form>
					</div>
				</section>
			</div>
		</div>
	</article><!--/.entryNew-->


 </div><!--end container-->
 <#include "/footer.html">


</div><!--end contents-->
	<script type="text/javascript" src="/assets/js/entry.js"></script>	
</body>
</html>