<!DOCTYPE html>
<#import "/spring.ftl" as spring />
<html lang="ja">
<head>
	<title>Glycan Repository</title>
	<#include "../header.html">
	<!-- <link rel="stylesheet" type="text/css" href="/assets/css/entry.css"> -->
	<script src="//code.jquery.com/jquery-1.10.2.min.js"></script>
	<script src="/assets/components/webcomponentsjs/webcomponents.min.js"></script>
	<link rel="import" href="/stanza/myStructure/">
</head>
<body>
<a name="top"></a><!--link for page top-->
<div id="contents">
  <#include "../nav.ftl">
  <#include "../errormessage.ftl">
  <div class="container" style="padding:20px 0">

	<h1 class="page-header">My Structure</h1>
	<togostanza-myStructure userId="${contributorUserId}" notation="${imageNotation}"></togostanza-myStructure>


  </div><!--end container-->
  <#include "../footer.html">

</div><!--end contents-->
</body>
</html>