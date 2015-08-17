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

<#include "../error.ftl">
<div class="container">
  <!-- -<a href="${Title[2]}" target="_blank">${Title[1]}</a> --> 
  <#--	// $submit_wurcs = 'WURCS%3D2.0/2,2,1/%5Bx2122h-1x_1-5_2*NCC/3=O%5D%5Bx1221m-1x_1-5%5D/1-2/a?-b1';
	// $submit_wurcs = 'WURCS%3D2.0%2F4%2C7%2C6%2F%5Bu2122h_2*NCC%2F3%3DO%5D%5B12122h-1b_1-5_2*NCC%2F3%3DO%5D%5B11122h-1b_1-5%5D%5B21122h-1a_1-5%5D%2F1-2-3-4-2-4-2%2Fa4-b1_b4-c1_c3-d1_c6-f1_e1-d2%7Cd4_g1-f2%7Cf4';
	if (isset($_POST['sequence'])) {
		$submit_wurcs = htmlspecialchars($_POST['sequence']);
	} else if (isset($_GET['sequence'])) {
		$submit_wurcs = htmlspecialchars($_GET['sequence']);
	}
	$accNum = "A registered accession number" or "unregistered" 	
	$img = Image of a query
	$submit_wurcs = WURCS sequence of a query
-->
  <div id="substructure_list_app" data-notation="${(image.notation)!""}" data-lang="${language}" data-wurcs="${sequence.resultSequence}" >
	<h1 class="page-header">Substructure search results</h1> 
	<!-- Query accession number and Image -->
	<div class="page-header">
		<div class="subInputQuery">
		<span class="subInputQuery_text">Input Query</span>
		<table class="subInputQueryTable">
			<thead>
				<tr>
					<th> 
						${accessionNumber}
					</th>
					<th>Image</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>${sequence.id}</td>
					<td width="60%"><img src="http://${hostname}${sequence.image}"><p></td>
				</tr>
			</tbody>
		</table>
		</div>
	</div>

	<!-- substructureResult -->
	<div class="substructureResult clearfix">

		<div class="glResultTotal">
			<span class="glResultTotal_num js_resultTotal_num">0</span>
			<!-- Glycans -->
			<span class="glResultTotal_text">${numberOfGlycans}</span>
		</div><!--/.glResultTotal-->

		<div class="glResultHeader clearfix">
			<div class="glResultPager">
				<ul class="glResultPage-ul js_mainPager clearfix"></ul>
			</div><!--/.glResultPager-->
		</div>

		<table class="subResultTable">
			<thead class="subResultTable_header">
				<tr>
					<th class="subResultTable_acc">
						<?php echo $common_doc['accessionNumber'] ?>
						<span class="subReultTable_sort subReultTable_sort--current" data-sort="ASC">▲</span>
						<span class="subReultTable_sort" data-sort="DESC">▼</span>
					</th>
					<th>Image</th>
				</tr>
			</thead>
			<tbody class="subResultTable_body"></tbody>
		</table><!--/.glResultStructure-->

		<p class="glSearchNothing">Nothing found in this condition.</p>

		<div class="glResultPager">
			<ul class="glResultPage-ul js_mainPager clearfix"></ul>
		</div><!--/.glResultPager-->

	</div><!--/.substructureResult-->

	<div class="loading_anim loading_anim--hide js_loading_anim">
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
	</div><!--/.loading_anim-->

  </div><!--/#substructure_list_app-->


</div>
<#include "../footer.html">

</div>
</body>
</html>