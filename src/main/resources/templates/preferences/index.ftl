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
<#include "../error.ftl">

<div class="container">
<h1 class="page-header">${Title[0]}</h1>
<!--Start Error Message-->
<!--END Error Message-->
<!-- selected message 
<?php  $image = $this->Session->check('imageNotation');
	  if ($image) {
	  	echo "Image notation : ", $this->Session->read('image.nLabel');
	  }else{
	  	echo $this->Session->read('image.message');
	  }
?>
-->
<fieldset>
	<legend>${TopTitle[0]}</legend>
	<input type="button" onclick="location.href='./image/cfg'"value="CFG">
	<input type="button" onclick="location.href='./image/cfgbw'"value="CFG greyscale">
	<input type="button" onclick="location.href='./image/uoxf'"value="Oxford">
	<input type="button" onclick="location.href='./image/uoxf-color'"value="Oxford colorscale">
	<input type="button" onclick="location.href='./image/cfg-uoxf'"value="CFG and Oxford">
	<input type="button" onclick="location.href='./image/iupac'"value="IUPAC">
<img src="/glycans/G00026MO/image?format=png&notation=cfg&style=extended">
</fieldset>
<fieldset>
	<legend>${TopTitle[1]}</legend>
	<input type="button" onclick="location.href='/Preferences/en'"value="English">
	<input type="button" onclick="location.href='/Preferences/ja'"value="日本語">
	<input type="button" onclick="location.href='/Preferences/ch1'"value="中文(简体)">
	<input type="button" onclick="location.href='/Preferences/ch2'"value="中文(繁體)">
	<input type="button" onclick="location.href='/Preferences/fr'"value="Français">
	<input type="button" onclick="location.href='/Preferences/de'"value="Deutsch">
	<input type="button" onclick="location.href='/Preferences/ru'"value="русский">
</fieldset>
</div>

<#include "../footer.html">
</div>
</body>
</html>