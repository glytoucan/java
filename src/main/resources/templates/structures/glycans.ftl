<!DOCTYPE html>
<#import "/spring.ftl" as spring />
<html lang="ja">
<head>
	<title>Glycan Repository</title>
<#include "../header.html">
</head>
<body>
<a name="top"></a><!--link for page top-->
<div id="contents">
<#include "../nav.ftl">

<#include "../errormessage.ftl">
<div class="container">

<div id="glycan_entry_app" data-notation="${imageNotation}" data-lang="${language}">
	<h1 class="glycan_entry_acc page-header"></h1>
	<!-- Overview -->
	<h2 class="entry_subtitle">${overview}</h2>
	<div class="entryOverview">
		<div class="entryInfo">
			<p class="glycan_entry_notFound">No data found.</p>
		</div><!--/.entryInfo-->
		<div class="entry_loading entry_loading--hide js_loading_anim_entry">
			<div id="floatingCirclesG">
				<div class="f_circleG" id="frotateG_01"></div>
				<div class="f_circleG" id="frotateG_02"></div>
				<div class="f_circleG" id="frotateG_03"></div>
				<div class="f_circleG" id="frotateG_04"></div>
				<div class="f_circleG" id="frotateG_05"></div>
				<div class="f_circleG" id="frotateG_06"></div>
				<div class="f_circleG" id="frotateG_07"></div>
				<div class="f_circleG" id="frotateG_08"></div>
			</div>
		</div><!--/.entry_loading-->
	</div>
	<hr class="entry_hr" />
	<!-- Relational data -->
	<h2 class="entry_subtitle">${relatedData}</h2>
	<div class="entryMain clearfix" data-init="motif">
		<div class="entryMain_menu">
			<ul>
				<li class="entryMain_menuList entryMain_menuList--current">
					<!-- Motif -->
					<span class="entryMain_menuText" data-stanza="relation_list" data-category="Motif">${motif}</span>
				</li>
				<li class="entryMain_menuList">
					<!-- Mono-<br />saccharide -->
					<span class="entryMain_menuText" data-stanza="relation_list" data-category="Monosaccharide">${monosaccharideComposition}</span>
				</li>
			</ul>
		</div><!--/.entryMain_menu-->
		<div class="entryMain_left">
			<div class="entryMain_leftBox">
				<div class="entryMain_content">
					<div class="entryMain_header">
						<!-- Motif -->
						<h3 class="entryMain_title">${motif}</h3>
						<div class="entryMain_stat">${numberFound}:<span class="entryMain_count">0</span></div>
					</div>
					<div class="cardView"></div>
				</div>
				<div class="entry_loading entry_loading--hide js_loading_anim_list">
					<div id="floatingCirclesG-1">
						<div class="f_circleG" id="frotateG_11"></div>
						<div class="f_circleG" id="frotateG_12"></div>
						<div class="f_circleG" id="frotateG_13"></div>
						<div class="f_circleG" id="frotateG_14"></div>
						<div class="f_circleG" id="frotateG_15"></div>
						<div class="f_circleG" id="frotateG_16"></div>
						<div class="f_circleG" id="frotateG_17"></div>
						<div class="f_circleG" id="frotateG_18"></div>
					</div>
				</div><!--/.entry_loading-->
			</div>
		</div><!--/.entryMain_left-->
	</div><!--/.entryMain-->
	<!-- Linked DB -->
	<h2 class="entry_subtitle">${linkedDb}</h2>
	<div class="entryMain clearfix" data-init="bcsdb">
		<div class="entryMain_menu entryMain_menu-linkedDB">
			<ul>
				<li class="entryMain_menuList">
					<span class="entryMain_menuText" data-stanza="relation_bcsdb" data-category="BCSDB">${bcsdb}</span>
				</li>
				<li class="entryMain_menuList">
					<span class="entryMain_menuText" data-stanza="relation_glycomedb" data-category="GlycomeDB">GlycomeDB</span>
				</li>
				<li class="entryMain_menuList">
					<span class="entryMain_menuText" data-stanza="relation_glycoepitope" data-category="GlycoEpitope">GlycoEpitope</span>
				</li>
			</ul>
		</div><!--/.entryMain_menu-->
		<div class="entryMain_left">
			<div class="entryMain_leftBox">
				<div class="entryMain_content">
					<div class="entryMain_header">
						<h3 class="entryMain_title">${bcsdb}</h3>
					</div>
					<div class="cardView"></div>
				</div>
				<div class="entry_loading entry_loading--hide js_loading_anim_list">
					<div id="floatingCirclesG-2">
						<div class="f_circleG" id="frotateG_21"></div>
						<div class="f_circleG" id="frotateG_22"></div>
						<div class="f_circleG" id="frotateG_23"></div>
						<div class="f_circleG" id="frotateG_24"></div>
						<div class="f_circleG" id="frotateG_25"></div>
						<div class="f_circleG" id="frotateG_26"></div>
						<div class="f_circleG" id="frotateG_27"></div>
						<div class="f_circleG" id="frotateG_28"></div>
					</div>
				</div><!--/.entry_loading-->
			</div>
		</div><!--/.entryMain_left-->
	</div><!--/.entryMain-->
</div><!--/#glycan_view_app-->

</div>
<#include "../footer.html">

</div>
</body>
</html>