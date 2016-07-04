<!DOCTYPE html>
<html lang="ja">
<head>
	<title>Glycan Repository</title>
<#include "header.html">
<link rel="canonical" href="https://glytoucan.org" />
<script type="application/ld+json">
{
  "@context": "http://schema.org",
  "@type": "WebSite",
  "url": "https://glytoucan.org/",
  "potentialAction": {
    "@type": "SearchAction",
    "target": "https://glytoucan.org/Structures/Glycans/{search_term_string}",
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
	
	<div class="topPageMain clearfix">
		<div class="topPageMain_content">
			<div class="topPageColumn topPageColumn-01">
				<p class="topPageColumn_title">${LeftTitle[0]}</p>
				<p class="topPageColumn_text">${Left[0]}</p>
			</div>
			<div class="topPageColumn topPageColumn-02">
				<p class="topPageColumn_title">${LeftTitle[1]}</p>
				<p class="topPageColumn_text">${Left[1]}</p>
			</div>
			<div class="topPageColumn topPageColumn-03">
				<p class="topPageColumn_title">${LeftTitle[2]}</p>
				<p class="topPageColumn_text">${Left[2]}<a href="http://www.glyspace.org" target="_blank">${Left[3]}</a>
				</p>
			</div>
		</div><!--/.topPageMain_content-->
	
		<div class="topPageMain_right">
		<!--	<nav class="topNav">-->
			<div class="topRight_image">
			<!--	<p class="topNav_label topNav_label-01">Search</p>
				<ul class="topNav_items">
					<li></li>
				</ul>-->
				<img src="/img/glytoucan_top.png" alt="glytoucan" width="240" height="250" class="image" />			</div>
			<div class="topRight_twitterTimeline">
			<!--	<p class="topNav_label">View All</p>
				<ul class="topNav_items">
					<li></li>
				</ul> -->
				<a class="twitter-timeline" width="240" height="250" href="https://twitter.com/glytoucan" data-widget-id="524091769575583744" lang="en">Tweets by @glytoucan</a>
            	<script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0],p=/^http:/.test(d.location)?'http':'https';if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src=p+"://platform.twitter.com/widgets.js";fjs.parentNode.insertBefore(js,fjs);}}(document,"script","twitter-wjs");
            	</script>
			</div>
		    <!--</nav>-->
		</div><!--/.topPageMain_right-->
	</div><!--/.topPageMain-->
	
	<#include "footer.html" parse="true">

</div> <!-- contents -->
</body>
</html>
