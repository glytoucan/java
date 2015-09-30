<!DOCTYPE html>
<meta charset="utf-8">
<html lang="ja">

<head>
	<title>Glycan D3</title>
<#include "../header.html">

<style>

.node circle {
  stroke-width: 1.5px;
}

.node {
  font: 20px sans-serif;
}

.link {
  fill: none;
  stroke: #ccc;
  stroke-width: 1.5px;
}
	div.menu{
   float: left;
   width: 15%;
}
div.blockb{
 float: right;
 width: 85%;
}
div.menu ul{
margin: 0;
padding: 0;
list-style-type: none;
}
div.menu li{
display: inline;
padding: 0;
margin: 0;
}
div.menu li a{
display: block;
width: 150px;
padding: 3px;
margin: 10px 0px 10px 0px;
text-decoration: none;
border: outset 3px #C2CBBD;
background-color: #C2CBBD;
text-align: center;
color: #000000;
font-size: 14px;
}
div.menu li a:hover{
border: inset 3px #95A38D;
background-color: #95A38D;
}

</style>
</head>

<body>
<a name="top"></a><!--link for page top-->
<div id="contents">
<#include "../nav.ftl">
<#include "../error.ftl">

<!-- ID: ${ID} -->
<script src="http://d3js.org/d3.v3.min.js"></script>
	<div class="menu">
 	 <ul>
 	 <li><a href="/D3_dndTree/${ID}">All data</a></li>
 	 <li><a href="/D3_motif_isomer/${ID}">Motif and Isomer</a></li>
 	 <li><a href="/D3_structure/${ID}">Superstructure and Substructure</a></li>
 	 <li><a href="/D3_subsumed/${ID}">Subsumes and Subsumed_by</a></li>
 	 <li><a href="/Structures/Glycans/${ID}">RETURN</a></li>
  </div>

<div class="blockb">
<script>
var viewerWidth = $(document).width();
var viewerHeight = $(document).height();
var diameter = viewerWidth*1.5;

var tree = d3.layout.tree()
    .size([360,360])
    .separation(function(a, b) { return (a.parent == b.parent ? 1 : 2) / a.depth; });

var diagonal = d3.svg.diagonal.radial()
    .projection(function(d) { return [d.y, d.x / 180 * Math.PI]; });

var svg = d3.select("body").append("svg")
    .attr("width", diameter*0.7)
    .attr("height", diameter - 150)
  .append("g")
    .attr("transform", "translate(" + diameter / 3 + "," + diameter / 4 + ")");
	
    d3.json("/Tree/D3retrieve?primaryId=${ID}", function (error, root) {

  var nodes = tree.nodes(root),
      links = tree.links(nodes);

  var link = svg.selectAll(".link")
      .data(links)
    .enter().append("path")
      .attr("class", "link")
      .attr("d", diagonal);

  var node = svg.selectAll(".node")
      .data(nodes)
    .enter().append("g")
      .attr("class", "node")
      .attr("transform", function(d) { return "rotate(" + (d.x - 90) + ")translate(" + d.y + ")"; });

      node.append("circle")
      .attr("r", function(d){
    	  if (d.name == "has_motif"||d.name == "has_linkage_isomer"||d.name == "has_superstructure"||d.name == "has_substructure"||d.name == "subsumes"||d.name == "subsumed_by")
    	  		return null;
    	  else return 10;
      })
      .attr("fill",function(d){
    	  if (d.size == 1)
                return "red";
          if (d.size == 2)
                return "blue";
          if (d.size == 3)
                return "green";
          if (d.size == 4)
                return "orange";
       	  if (d.size == 5)
                return "skyblue";
       	  if (d.size == 6)
                return "purple";
       	  else  return "black";
      })
      .attr("stroke","black");

  var text = node.append("text")
      .attr("dy", ".31em")
      .attr("text-anchor", function(d) { return d.x < 180 ? "start" : "end"; })
      .text(function(d) {
        if(d.name == "has_motif"|| d.name =="has_linkage_isomer"|| d.name =="subsumes"|| d.name =="subsumed_by"|| d.name =="has_superstructure"|| d.name =="has_substructure")
          return d.name;
        else return null;
      })
      .attr("fill",function(d){
    	  if (d.name == "has_motif")
              return "red";
            if (d.name == "has_linkage_isomer")
              return "blue";
            if (d.name == "has_superstructure")
              return "green";
            if (d.name == "has_substructure")
              return "orange";
            if (d.name == "subsumes")
              return "skyblue";
            if (d.name == "subsumed_by")
              return "purple";
            else  return "black";
      })
      .attr("transform", function(d){
    	    if (d.name == "has_motif")
    		  return "rotate(270)translate(60,10)";
            if (d.name == "has_linkage_isomer")
            	return "rotate(270)translate(60,10)";
            if (d.name == "has_superstructure")
            	return "rotate(270)translate(60,10)";
            if (d.name == "has_substructure")
            	return "rotate(270)translate(60,10)";
            if (d.name == "subsumes")
            	return "rotate(270)translate(60,10)";
            if (d.name == "subsumed_by")
            	return "rotate(270)translate(60,10)";
            else  return "rotate(270)translate(60,10)";	
    	 /*  if (d.size == null)
         		return "rotate(270)translate(60,10)";
         	else if (d.x < 180)
         		return "rotate(270)translate(60,10)";
         	else if (d.x == 0)
         		return "rotate(270)translate(60,10)";
         	//else return "rotate(180)translate(-165,-40)";
    	    else return "rotate(270)translate(60,10)"; */
      });


  node.append("image")
      .on("click",function(d){
        alert(d.name +"<br>"+ d.x +"<br>" + d.y);
        
      })
      .attr("xlink:href", function(d){
	     if(d.name == "has_motif"|| d.name =="has_linkage_isomer"|| d.name =="subsumes"|| d.name =="subsumed_by"|| d.name =="has_superstructure"|| d.name =="has_substructure")
	       return null;
	     else  return "http://glytoucan.org/glyspace/service/glycans/"+ d.name + "/image?style=extended&format=png&notation=cfg";
 	 })
      .attr("width", 150)
      .attr("height", 70)
      .attr("transform", function(d){
    	  //alert(d.x);
      	if (d.size == null)
     		return "rotate(270)translate(-70,-90)";
     	else if (d.x < 180)
     		return "translate(15,-35)";
     	else if (d.x == 0)
     		return "rotate(270)translate(15,-35)";
     	else return "rotate(180)translate(-165,-40)";
      });

});

d3.select(self.frameElement).style("height", diameter - 150 + "px");


</script>
</div>
<#include "../footer.html">

</body>
</html>

