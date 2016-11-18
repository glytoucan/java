<!DOCTYPE html>
<html lang="ja">
<head>
	<title>Glycan Repository</title>
<#include "header.html">
<link rel="canonical" href="http://glytoucan.org" />
<script type="application/ld+json">
{
  "@context": "http://schema.org",
  "@type": "WebSite",
  "url": "http://glytoucan.org/",
  "potentialAction": {
    "@type": "SearchAction",
    "target": "http://glytoucan.org/Structures/Glycans/{search_term_string}",
    "query-input": "required name=search_term_string"
  }
}
</script>
</head>
<body>
<a name="top"></a><!--link for page top-->
<div id="contents">

	<#include "nav.ftl">
	<#include "errormessage.ftl">
		
	<div class="topMainVis">
		<div class="topMainVis_inner">
			<div class="topMainVis_title">
				<h1>GlyTouCan<br /><span class="topMainVis_subTitle">THE GLYCAN REPOSITORY</span></h1>
			</div><!--Chenged height from 333-->
			<img class="topMainVis_img" src="/img/top_structure_img.png" height="300" width="382" alt="" />
		</div>
		<div id="top_status_app" class="topMainVis_bottom">
			<div class="topMainVisStat">
				<a class="topMainVisStat_col" href="/Structures">
					<p class="topMainVisStat_value topMainVisStat_value-num"><span class="statusTotalCount"></span></p>
					<p class="topMainVisStat_label">${glycans}</p>
				</a>
				<a class="topMainVisStat_col" href="/Motifs/listAll">
				    <p class="topMainVisStat_value topMainVisStat_value-num"><span class="statusMotifCount"></span></p>
					<p class="topMainVisStat_label">${motifs}</p>
				</a>
			<!--	<a class="topMainVisStat_col" href="/Monosaccharides"> -->
				<div class="topMainVisStat_col" href="/Monosaccharides">
					<p class="topMainVisStat_value topMainVisStat_value-num"><span class="statusMonosaccharideCount"></span></p>
					<p class="topMainVisStat_label">${monosaccharides}</p>
				</div>
			</div><!--/.topMainVis_stat-->
		</div>
	</div><!--/.topMainVis-->
	
	<#include "footer.html">

</div> <!-- contents -->
</body>
</html>
