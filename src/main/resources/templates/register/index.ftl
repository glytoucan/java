<!DOCTYPE html>
<#import "/spring.ftl" as spring />
<html lang="ja">
<head>
	<title>Glycan Repository</title>
<#include "../header.html">
<link rel="canonical" href="${hostname}/Registries/index" />
<link rel="stylesheet" type="text/css" href="/css/add_input.css" />
<script type="text/javascript" src="/js/registries_input.js"></script>
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
			<!-- Input text form -->
			<div class="span6">
				<form class="well" action="/Registries/confirmation"
					id="confirmationForm" method="post" accept-charset="utf-8">
					<div style="display: none;">
						<input type="hidden" name="_method" value="POST" />
					</div>
					${Left[0]}
					<@spring.formTextarea 'sequence.sequence' 'id="sequence" placeholder="Sequence" cols="20" rows="15" style="width: 370px; height: 400px;"' />
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
	                <div class="registriesInput_add">
	                    <span class="registriesInput_addBtn btn">${add!"Add"} ${another!"Another"}</span>
	                </div>
					<div class="submit">
						<input class="btn btn-primary" type="submit" value="${submit}" />
						<button type="reset" class="btn">${clear}</button>
					</div>
				</form>
			</div>

			<!-- Explanation of sequence format -->
			<!--
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
			-->
			<div class="span6">
				<h id="tabs"></h>
				<ul class="nav nav-tabs">
					<!-- GlycoCT condensed -->
					<li><a href="#A" data-toggle="tab">
						${RightTitle[1]}
					</a></li>
					<!-- WURCS -->
					<li class="active"><a href="#B" data-toggle="tab">
						${RightTitle[2]}
					</a></li>
				</ul>
				<div class="tabbable">
					<div class="tab-content">
						<!-- GlycoCT condensed description -->
						<div class="tab-pane" id="A">
							<p>
								${Right[1]}
								<a href="http://www.ncbi.nlm.nih.gov/pubmed/18436199">More details</a><br>
								${RightFigure[1]}
							</p>
						</div>
						<!-- WURCS -->
						<div class="tab-pane active" id="B">
							<p>
								${Right[2]}
								<a href="http://www.wurcs-wg.org/about.php">More details</a><br>
								<!-- ${RightFigure[2]} -->
								<img src="/glycans/G22768VO/image?style=extended&format=png&notation=cfg"><br>
								WURCS=2.0/3,5,4/[a2122h-1b_1-5_2*NCC/3=O][a1122h-1b_1-5][a1122h-1a_1-5]/1-1-2-3-3/a4-b1_b4-c1_c3-d1_c6-e1
							</p>
						</div>
					</div>
				</div>
			</div><!-- <div class="span6"> -->
		</div><!-- <div class="row-fluid"> -->
	</div><!-- <div class="container-fluid"> -->
</div><!-- <div class="container"> -->

<#include "../footer.html">
</div><!-- <div id="contents"> -->
</body>
</html>