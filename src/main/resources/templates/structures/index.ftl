<!DOCTYPE html>
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
<h1 class="page-header">
${Title[0]}
<a class="userGuide" href="${Title[2]}" target="_blank">
<img class="icon_userGuide clearfix" src="/img/icon_userGuide.png" height="48" width="24" alt="" />
<span class="text_userGuide"><img src="/img/text_userGuide.png" height="40" width="80" alt="" /></span>
</a>
</h1>
<!--Glycan List -->
<div id="glycan_list_app" data-notation="${imageNotation}" data-lang="${language}">
	<div class="glSearchWrapper">
		<div class="incSearch">
			<!--Search -->
			<span class="incSearch_label">${search}</span>
			<!--Enter a motif or monosaccharide name-->
			<input type="text" name="incSearchQuery" class="incSearch_input" size="50" placeholder="${enterAMotifOrMonosaccharideName}" />
			<!--Show full list of Motifs / Monosaccharides -->
			<span class="incSearch_list">${showFullListOf}&nbsp;
				<span class="incSearch_showList" data-category="motif">${motifs}</span>&nbsp;/&nbsp;
				<span class="incSearch_showList" data-category="monosaccharide">${monosaccharides}</span>
				<span class="incSearch_showList" data-category="database">Databases</span>
			</span>
			<!-- no suggestion -->
			<span class="incSearch_notfound">(${noneFound})</span>
		</div>

		<div class="listBox listBox--hide">
			<div class="listBox_title"><span class="listBox_close js_listBox_close">&times;</span></div>
			<div class="listBox_tab">
				<!-- Motifs -->
				<span class="listBox_tabBtn listBox_tabBtn--current" data-category="motif">${motifs}</span>
				<!-- Monosaccharides -->
				<span class="listBox_tabBtn" data-category="monosaccharide">${monosaccharides}</span>
				<!-- Databases -->
				<span class="listBox_tabBtn" data-category="database">Databases</span>
			</div>
			<div class="listBox_listArea">
				<ul class="listBox_ul listBox_ul-motif listBox_ul--show" data-category="motif"></ul>
				<ul class="listBox_ul listBox_ul-monosaccharide" data-category="monosaccharide"></ul>
				<ul class="listBox_ul listBox_ul-database" data-category="database"></ul>
			</div>
			</div>
		</div>

		<div class="searchSuggest searchSuggest--blank js_searchSuggest">
			<ul class="searchSuggest_ul"></ul>
		</div><!--/.searchSuggest-->
		<div class="adoptedSearch adoptedSearch--empty">
			<!--Motif and Monosaccharide-->
			<p class="adoptedSearch_title">${motifAndMonosaccharide}</p>
			<!-- No condition -->
			<p class="adoptedSearch_default">(${noCondition})</p>
			<div class="adoptedSearch_group adoptedSearch_group-motif adoptedSearch_group--empty">
				<!-- Motif -->
				<p class="adoptedSearch_label adoptedSearch_label--01" data-category="motif">${motif}</p>
			</div>
			<div class="adoptedSearch_group adoptedSearch_group-monosaccharide adoptedSearch_group--empty">
				<!-- Monosaccharide -->
				<p class="adoptedSearch_label adoptedSearch_label--02" data-category="monosaccharide">${monosaccharide}</p>
			</div>
		</div><!--/.adoptedSearch-->

		<div class="massRange">
			<!-- Range of the Mass -->
			<p class="massRange_title">${massRange}</p>
			<div class="massEnable">
				<input id="applyMassRange" class="massEnable_checkbox" type="checkbox" />
				<label class="massEnable_style" for="applyMassRange"><img src="/img/checkbox_check.png" width="30" height="30" alt="&radic;" /></label>
				<label class="massEnable_text" for="applyMassRange">Enable mass range filter</label>
			</div>
			<div class="massRange_input clearfix">
				<div class="massRange_slider ju_Range-massRange"></div>
				<input class="massRange_num massRange_num-min" disabled="disabled" type="text" size="6" value="" />
				&nbsp;〜&nbsp;
				<input class="massRange_num massRange_num-max" disabled="disabled" type="text" size="6" value="" />
			</div>
		</div><!--/.massRange-->

		<div class="linkedDb">
			<p class="linkedDb_title">Linked DB</p>
			<p class="linkedDb_default linkedDb_default--show">(No condition)</p>
			<div class="linkedDb_items"></div>
		</div><!--/.linkedDb-->
	</div><!--/.glSearchWrapper-->
	<div class="glResult glResult--listview clearfix">

		<div class="glResultTotal">
			<!-- Glycans -->
			<span class="glResultTotal_text">${numberOfGlycans}: </span>
			<!-- Count of glycans -->
			<span class="glResultTotal_num">0</span>
			<!-- Reset all conditions -->
			<span class="clearCondition_btn">&laquo; ${resetAllConditions}</span>
		</div><!--/.glResultTotal-->

		<div class="glCurrentStatus">
			<p class="glCurrentStatus_header">Current status</p>
			<div class="glCurrentStatus_category glCurrentStatus_category-motif">
				<p class="glCurrentStatus_title glCurrentStatus_title-01">Motif</p>
				<div class="glCurrentStatus_detail"></div>
			</div>
			<div class="glCurrentStatus_category glCurrentStatus_category-monosaccharide">
				<p class="glCurrentStatus_title glCurrentStatus_title-02">Monosaccharide</p>
				<div class="glCurrentStatus_detail"></div>
			</div>
			<div class="glCurrentStatus_category glCurrentStatus_category-mass">
				<p class="glCurrentStatus_title glCurrentStatus_title-03">Mass range</p>
				<div class="glCurrentStatus_detail"></div>
			</div>
			<div class="glCurrentStatus_category glCurrentStatus_category-database">
				<p class="glCurrentStatus_title glCurrentStatus_title-04">Linked DB</p>
				<div class="glCurrentStatus_detail"></div>
			</div>
		</div>

		<div class="glResultHeader clearfix">
			<div class="glResultSwitch js_resultSwitch">
				<!-- List -->
				<span class="glResultSwitch_text glResultSwitch_text--current" data-view="list">${list}</span>
				<span class="glResultSwitch_text" data-view="wurcs">WURCS</span>
				<span class="glResultSwitch_text" data-view="glycoct">GlycoCT</span>
			</div><!--/.glResultSwitch-->

			<div class="glResultSort">
				<!-- Sort -->
				<p class="glResultSort-text">${sort}</p>
				<select class="glResultSort_key">
					<!-- Date Entered -->
					<option class="glResultSort_val" selected="selected" value="ContributionTime">${dateEntered}</option>
					<!-- Mass -->
					<option class="glResultSort_val" value="Mass">${mass}</option>
					<!-- Contributor -->
					<option class="glResultSort_val" value="Contributor">${contributor}</option>
					<!-- Accession Number -->
					<option class="glResultSort_val" value="AccessionNumber">${accessionNumber}</option>
				</select>
				<select class="glResultSort_order">
					<!-- Up -->
					<option class="glResultSort_val" value="ASC">${up}</option>
					<!-- Down -->
					<option class="glResultSort_val" selected="selected" value="DESC">${down}</option>
				</select>
			</div><!--/.glResultSort-->

			<div class="glResultPager">
				<ul class="glResultPage-ul clearfix"></ul>
			</div><!--/.glResultPager-->
		</div>
		<div class="glResult glResultWrapper glResult--showing" data-mode="list">
			<span class="glSearchNothing">Nothing found in this condition.</span>
		</div>
		<table class="glResult glResultStructure" data-mode="wurcs">
			<thead class="glResultStructure_header">
				<tr>
					<!-- Accession Number -->
					<th class="glResultStructure_acc">${accessionNumber}</th>
					<th>WURCS</th>
				</tr>
			</thead>
			<tbody class="glResultWurcs_body"></tbody>
		</table><!--/.glResultStructure-->
		<table class="glResult glResultStructure" data-mode="glycoct">
			<thead class="glResultStructure_header">
				<tr>
					<!-- Accession Number -->
					<th class="glResultStructure_acc">${accessionNumber}</th>
					<th>GlycoCT</th>
				</tr>
			</thead>
			<tbody class="glResultStructure_body"></tbody>
		</table><!--/.glResultStructure-->
		<div class="glResultPager">
			<ul class="glResultPage-ul clearfix"></ul>
		</div><!--/.glResultPager-->
	</div><!--/#glResultArea-->
	<div class="loading_anim loading_anim--hide">
		<div class="loading_anim loading_anim--hide">
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
		</div>
	</div>
</div>
<!--/#glycan_list_app-->
</div>
<#include "../footer.html">
</div>
</body>
</html>
