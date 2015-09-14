<!DOCTYPE html>
<html lang="ja">
<head>
	<title>Glycan Repository</title>
<#include "../header.html">
<link rel="canonical" href="https://glytoucan.org/Structures/graphical" />
</head>
<body>
<a name="top"></a><!--link for page top-->
<div id="contents">
<#include "../nav.ftl">
<#include "../error.ftl">


<div class="container">
<h2 class="page-header">${Title[0]}
  <a class="userGuide" href="${Title[2]}" target="_blank">
    <img class="icon_userGuide clearfix" src="/img/icon_userGuide.png" height="48" width="24" alt="" />
    <span class="text_userGuide"><img src="/img/text_userGuide.png" height="40" width="80" alt="" /></span>
  </a>
<script type="text/javascript" src="/GlycanBuilder/VAADIN/vaadinBootstrap.js"></script>
</h2>

<div class="container-fluid">
  <div class="row-fluid">
    <div>

      <iframe id="__gwt_historyFrame" style="width:0;height:0;border:0;overflow:hidden" src="javascript:false"></iframe>

      <div style="height:450px;" id="fb" class="v-app">
        <!-- Optional placeholder for the loading indicator -->
        <div class=" v-app-loading"></div>

        <!-- Alternative fallback text -->
        <noscript>You have to enable javascript in your browser to use an application built with Vaadin.</noscript>
      </div>
      <script type="text/javascript">//<![CDATA[
      if (!window.vaadin)
        alert("Failed to load the bootstrap JavaScript: VAADIN/vaadinBootstrap.js");

      /* The UI Configuration */
      vaadin.initApplication("fb", {
      "browserDetailsUrl": "../GlycanBuilder/",
      "serviceUrl": "../GlycanBuilder/",
      "widgetset": "ac.uk.icl.dell.vaadin.glycanbuilder.widgetset.GlycanbuilderWidgetset",
      "theme": "ucdb_2011theme",
      "versionInfo": {"vaadinVersion": "7.0.0"},
      "vaadinDir": "/GlycanBuilder/VAADIN/",
      "heartbeatInterval": 300,
      "debug": false,
      "standalone": false,
      "authErrMsg": {
      "message": "Take note of any unsaved data, "+
      "and <u>click here<\/u> to continue.",
      "caption": "Authentication problem"
      },
      "comErrMsg": {
      "message": "Take note of any unsaved data, "+
      "and <u>click here<\/u> to continue.",
      "caption": "Communication problem"
      },
      "sessExpMsg": {
        "message": "Take note of any unsaved data, "+
          "and <u>click here<\/u> to continue.",
        "caption": "Session Expired"
      }
    });//]]>
      </script>

<script type="text/javascript">
var callBack=[];
callBack.run=function(response){
  document.write('Please wait... searching for:' + response);
  var r = response;
  // test.glytoucan.org/Structures/structure?input01=sub&select1=RES%0A1b:b-dglc-HEX-1:5%0A2s:n-acetyl%0A3b:b-dglc-HEX-1:5%0A4s:n-acetyl%0A5b:b-dman-HEX-1:5%0A6b:a-dman-HEX-1:5%0A7b:a-dman-HEX-1:5%0A8b:a-dman-HEX-1:5%0A9b:a-dman-HEX-1:5%0A10b:a-lgal-HEX-1:5|6:d%0ALIN%0A1:1d(2+1)2n%0A2:1o(4+1)3d%0A3:3d(2+1)4n%0A4:3o(4+1)5d%0A5:5o(3+1)6d%0A6:5o(6+1)7d%0A7:7o(3+1)8d%0A8:7o(6+1)9d%0A9:1o(6+1)10d%0A
  
  // "/Structures/structure"
  var url = "/Structures/structure?input01=sub&select1=glycoCT_condensed&sequence=";
  var x = url + encodeURIComponent(r);
  //alert(x);

  if (r.length < 1) {
    alert("Please enter a structure to search");
    window.location.assign("/Structures/graphical");
    return;
  }
  //var s = [ url, "" , r].join("");
  window.location.assign(x);
}
</script>

      <form id="frm1" name="frm1" action="ms" method="GET" class="form-search">
        <input type="button" name="Search" value="${search}" onclick='exportCanvas("glycoct_condensed","callBack");'/>
      </form>

      <div class="row-fluid">
        <div class="span6">
          <h3 class="">${BottomTitle[0]}</h3>
          <p class="builder">${Bottom[0]}</p>
        </div>
  
        <div class="span6">
          <h3 class="">${BottomTitle[1]}</h3>
          <p class="builder">${Bottom[1]} <a href="http://www.ncbi.nlm.nih.gov/pubmed/24234447" target="_blank">PubMed</a></p>
          <p class="builder">${Bottom[2]} <a href="http://www.ncbi.nlm.nih.gov/pubmed/23109548" target="_blank">PubMed</a></p>
        </div>
      </div>
    </div>
  </div>
</div>
</div>
<#include "../footer.html">
</div><!-- contents -->
</body>
</html>
