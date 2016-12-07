<!DOCTYPE html>
<#import "/spring.ftl" as spring />
<html lang="ja">
<head>
	<title>Register a publication to Glycan Structure ${accNum}</title>
	<#include "/header.html">
	<meta name="description" content="Register a publication to Glycan Structure ${accNum}" />
	<link rel="stylesheet" type="text/css" href="/assets/css/entry.css">
	<script src="//code.jquery.com/jquery-1.10.2.min.js"></script>
	<script src="/assets/components/webcomponentsjs/webcomponents.min.js"></script>
	<link rel="import" href="/stanza/summary/">
	<!-- <link rel="import" href="/stanza/structure/"> -->
	<link rel="import" href="/stanza/grabVisualizer/">
	<link rel="import" href="/stanza/wurcs/">
	<link rel="import" href="/stanza/glycoct/">
	<link rel="import" href="/stanza/iupacCondensed/">
	<link rel="import" href="/stanza/iupacExtended/">
	<link rel="import" href="/stanza/motif/">
	<link rel="import" href="/stanza/species/">
	<link rel="import" href="/stanza/literature/">
	<link rel="import" href="/stanza/external/">
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
					<h1 class="entryNew_heading">${currentlyRegisteredLiterature}</h1>
					<togostanza-literature acc="${accNum}"></togostanza-literature>
					<div>
					${submitPublicationId}:
					<form action="/Registries/supplement/${accNum}/confirmation" id="supplementConfirmationForm" method="post" accept-charset="utf-8">
						<div style="display: none;">
							<input type="hidden" name="accessionNumber" value="${accNum}" />

						</div>
						<input type="text" name="literatureId" id="literatureId" placeholder="pubmed id" value="${literatureId!""}" />
						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
						<div class="submit">
							<input class="btn btn-primary" type="submit" value="${submit!"Submit"}" /> &nbsp; <button type="reset" class="btn">${reset!"Reset"}</button>
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