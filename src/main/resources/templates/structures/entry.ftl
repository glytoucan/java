<!DOCTYPE html>
<#import "/spring.ftl" as spring />
<html lang="ja">
<head>
	<title>${description}</title>
	<#include "../header.html">
	<meta name="description" content="${description}" />
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
 -->	
</head>
<body>
<!--link for page top-->
<a name="top"></a>
<div id="contents">
 <#include "../nav.ftl">
 <#include "../errormessage.ftl">
 <div class="container">

	<article class="entryNew">
		<section class="entryNew_summary">
			<h1 class="entryNew_heading">${accNum}</h1>
			<togostanza-summary acc="${accNum}" notation="${imageNotation}"></togostanza-summary>
		</section>
		<div class="entryNew_content">
			<nav class="entryNav">
				<ol class="entryNav_ol">
					<!-- <li><a href="#structure">Structure (Image)</a></li> -->
					<li><a href="#grab">GRAB visualizer</a></li>
					<li><a href="#descriptors">Computed Descriptors</a>
						<ol class="entryNav_ol entryNav_ol-sub">
							<li><a href="#wurcs">WURCS</a></li>
							<li><a href="#glycoct">GlycoCT</a></li>
							<li><a href="#iupacCondensed">IUPAC Condensed</a></li>
							<li><a href="#iupacExtended">IUPAC Extended</a></li>
						</ol>
					</li>
					<li><a href="#motif">Glycan Motif</a></li>
					<li><a href="#species">Species</a></li>
					<li><a href="#literature">Literature</a></li>
					<li><a href="#external">External ID</a></li>
				</ol>
			</nav>
			<div class="entryNew_right">
<!-- 				<section id="structure" class="entryNew_section">
					<h1 class="entryNew_heading">Structure</h1>
					<togostanza-structure acc="${accNum}"></togostanza-structure>
				</section>
 -->				<section id="grab" class="entryNew_section">
					<h1 class="entryNew_heading">GRAB visualizer</h1>
					<togostanza-grabVisualizer acc="${accNum}"></togostanza-grabVisualizer>
					<!-- <a class="entryNew_heading-2nd btn btn-primary" href="/D3_dndTree/${accNum}" role="button">Viewer</a> -->
				</section>
				<section id="descriptors" class="entryNew_section">
					<h1 class="entryNew_heading">Computed Descriptors</h1>
					<h2 id="wurcs" class="entryNew_heading entryNew_heading-2nd">WURCS</h2>
					<togostanza-wurcs acc="${accNum}"></togostanza-wurcs>
					<h2 id="glycoct" class="entryNew_heading entryNew_heading-2nd">GlycoCT</h2>
					<togostanza-glycoct acc="${accNum}"></togostanza-glycoct>
					<h2 id="iupacCondensed" class="entryNew_heading entryNew_heading-2nd">IUPAC Condensed</h2>
					<togostanza-iupacCondensed acc="${accNum}"></togostanza-iupacCondensed>
					<h2 id="iupacExtended" class="entryNew_heading entryNew_heading-2nd">IUPAC Extended</h2>
					<togostanza-iupacExtended acc="${accNum}"></togostanza-iupacExtended>
				</section>
				<section id="motif" class="entryNew_section">
					<h1 class="entryNew_heading">Glycan Motif</h1>
					<togostanza-motif acc="${accNum}" notation="${imageNotation}"></togostanza-motif>
				</section>
				<section id="species" class="entryNew_section">
					<h1 class="entryNew_heading">Species</h1>
					<togostanza-species acc="${accNum}"></togostanza-species>
				</section>
				<section id="literature" class="entryNew_section">
					<h1 class="entryNew_heading">Literature</h1>
					<togostanza-literature acc="${accNum}"></togostanza-literature>
				</section>
				<section id="external" class="entryNew_section">
					<h1 class="entryNew_heading">External ID</h1>
					<togostanza-external acc="${accNum}"></togostanza-external>
				</section>
			</div>
		</div>
	</article><!--/.entryNew-->


 </div><!--end container-->
 <#include "../footer.html">


</div><!--end contents-->
	<script type="text/javascript" src="/assets/js/entry.js"></script>	
</body>
</html>
