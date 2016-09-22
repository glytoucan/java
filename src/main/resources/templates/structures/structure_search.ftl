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
<h1 class="page-header">
	${Title[0]}
	<a class="userGuide clearfix" href="${Title[2]}" target="_blank">
		<img class="icon_userGuide clearfix" src="/img/icon_userGuide.png" height="48" width="24" alt="" />
		<span class="text_userGuide"><img src="/img/text_userGuide.png" height="40" width="80" alt="" /></span>
	</a>
</h1>
<br>

    
<div class="container-fluid">
	<div class="row-fluid">
		<div class="span7">

			<form class="well" method="post" action="/Structures/structure" name="form1"> 
        <br>

<#-- <input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}" /> -->


				 <!-- 
				$doc{'LeftTitle'}[0]; Search for all structures that contain entered glycan substructure.
				$doc{'Left'}[2] = Search for exact same structure
				$doc{'Left'}[3] = Search for substructure
				value="exact"; exact search
				value="sub"; substructure search
				  -->
				
<@spring.formTextarea 'sequence.sequence' 'id="sequence" placeholder="Sequence" name="sequence" style="width: 465px; height: 300px;"' />
<input type="hidden" name="from" value="structure" />
<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				<#--
				
				 name="sequence" style="width: 465px; height: 300px;"
				<label class="control-label" for="select1"> ${Left[4]}</label>
				<select name="select1">
					<option value="glycoCT_condensed">${Left[5]}</option>
					<option value="carbbank">${Left[6]}</option>
					<option value="cfg">${Left[7]}</option>
					<option value="bcsdb">${Left[8]}</option>
					<option value="linucs">${Left[9]}</option>
					<option value="kcf">${Left[10]}</option>
				</select>-->
				<div class="submit">
					<button type="submit" class="btn btn-primary">${search}</button>
					<button type="reset" class="btn">${cancel}</button>
				</div>

			</form>

		</div>

<#-- 		<div class="span5">
			<br>
			<br>
			<br>
			<br>
			<br>
			<br>
			<ul>
			<li>${RightTitle[1]}
			<ul><li>${Right[1]}</ul>
			<br>
			<br>
			<li>${RightTitle[2]}
			<ul><li>${Right[2]}</ul>
			</ul>
		</div>
 -->		
		<div class="span12">
			<h id="tabs"></h>
			<ul class="nav nav-tabs">
				<!-- GlycoCT condenxed -->
				<li class="active"><a href="#A" data-toggle="tab">
					<#assign BottomTitle = BottomTitle>
					<#assign BottomTitleKeys = BottomTitle?keys>
					<#-- list BottomTitleKeys as key>${key} = ${BottomTitle[key]}; </#list -->
					${BottomTitle[BottomTitleKeys[0]]}
				</a></li>
				<!-- WURCS -->
				<li><a href="#I" data-toggle="tab">
					${BottomTitle[BottomTitleKeys[6]]}
				</a></li>
				<!-- LineraCode -->
				<!-- <li><a href="#E" data-toggle="tab"> -->
					<!-- ${BottomTitle[BottomTitleKeys[2]]} -->
				<!-- </a></li> -->
				<!-- KCF -->
				<!-- <li><a href="#H" data-toggle="tab"> -->
					<!-- ${BottomTitle[BottomTitleKeys[5]]} -->
				<!-- </a></li> -->
			</ul>
			<div class="tabbable">
				<div class="tab-content">
					<!-- GlycoCT description -->
					<div class="tab-pane active" id="A">
						<p>
							<#assign Bottom = Bottom>
							<#assign BottomKeys = Bottom?keys>
							<#-- list BottomKeys as key>${key} = ${Bottom[key]}; </#list -->
							${Bottom[BottomKeys[0]]}
							<a href="http://www.ncbi.nlm.nih.gov/pubmed/18436199">${Bottom[BottomKeys[6]]}</a>
							<br>
							<#assign BottomFigure = BottomFigure>
							<#assign BottomFigureKeys = BottomFigure?keys>
							${BottomFigure[BottomFigureKeys[0]]}
						</p>
					</div>
					<!-- IUPAC description -->
					<div class="tab-pane" id="D">
						<p>
							${Bottom[BottomKeys[1]]}
							<a href="http://books.google.co.jp/books?id=u_eA0voGL6UC&lpg=PA30&dq=glycome%20informatics%20Aoki%20CarbBank&hl=ja&pg=PA30#v=onepage&q&f=false">${Bottom[BottomKeys[6]]}</a>
							<br>
							${BottomFigure[BottomFigureKeys[1]]}
						</p>
					</div>
					<!-- Linear Code description -->
					<div class="tab-pane" id="E">
						<p>
							${Bottom[BottomKeys[2]]}
							<a href="https://books.google.co.jp/books?id=u_eA0voGL6UC&lpg=PP1&dq=glycome%20informatics&hl=ja&pg=PA37#v=onepage&q&f=false">${Bottom[BottomKeys[6]]}</a>
							<br>
							${BottomFigure[BottomFigureKeys[2]]}
						</p>
					</div>
					<!-- BCSDB format description -->
					<div class="tab-pane" id="F">
						<p>
							${Bottom[BottomKeys[3]]}
							<a href="http://csdb.glycoscience.ru/bacterial/index.html?help=rules">${Bottom[BottomKeys[6]]}</a>
							<br>
							${BottomFigure[BottomFigureKeys[3]]}
						</p>
					</div>
					<!-- LINUCS format description -->
					<div class="tab-pane" id="G">
						<p>
							${Bottom[BottomKeys[4]]}
							<a href="http://www.ncbi.nlm.nih.gov/pubmed/11675023?dopt=Abstract">${Bottom[BottomKeys[6]]}</a>
							<br>
							${BottomFigure[BottomFigureKeys[4]]}
						</p>
					</div>
					<!-- KCF format description -->
					<div class="tab-pane" id="H">
						<p>
							${Bottom[BottomKeys[5]]}
								<a href="http://books.google.co.jp/books?id=u_eA0voGL6UC&lpg=PA32&ots=3-YMFHBj8D&dq=glycome%20informatics%20Aoki%20KCF&hl=ja&pg=PA31#v=onepage&q&f=false">${Bottom[BottomKeys[6]]}</a>
							<br>
							${BottomFigure[BottomFigureKeys[5]]}
						</p>
					</div>
					<!-- WURCS description-->
					<div class="tab-pane" id="I">
						<p>
							${Bottom[BottomKeys[7]]}
								<a href="http://www.wurcs-wg.org/about.php">${Bottom[BottomKeys[6]]}</a>
							<br>
							${BottomFigure[BottomFigureKeys[6]]}
						</p>
					</div>
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