<!DOCTYPE html>
<meta charset="utf-8">
<html lang="ja">
<head>
	<title>Glycan D3 dndTree</title>
<#include "../header.html">

<script data-require="d3@3.5.3" data-semver="3.5.3" src="https://cdnjs.cloudflare.com/ajax/libs/d3/3.5.3/d3.js"></script>

<style type="text/css">

/* d3-context-menu styles */
.d3-context-menu {
	position: absolute;
	display: none;
	background-color: #f2f2f2;
	border-radius: 4px;

	font-family: Arial, sans-serif;
	font-size: 14px;
	min-width: 150px;
	border: 1px solid #d4d4d4;

	z-index:1200;
}

.d3-context-menu ul {
	list-style-type: none;
	margin: 4px 0px;
	padding: 0px;
	cursor: default;
}

.d3-context-menu ul li {
	padding: 4px 16px;
}

.d3-context-menu ul li:hover {
	background-color: #4677f8;
	color: #fefefe;
}

/* dndTree styles */
	.node {
    cursor: pointer;
  }

  .overlay{
      background-color:#EEE;
  }

  .node circle {
    stroke-width: 1.5px;
  }

  .node image {
	position: absolute;
	z-index: -1;
  }

  .front {
    position: absolute;
    z-index: 2;
  }

  .back {
	position: absolute;
	z-index: -1;
  }

  .node text {
    font-size:10px;
    font-family:sans-serif;
  }

  .link {
    fill: none;
    stroke: #ccc;
    stroke-width: 1.5px;
  }

  .templink {
    fill: none;
    stroke: red;
    stroke-width: 3px;
  }

  .ghostCircle.show{
      display:block;
  }

  .ghostCircle, .activeDrag .ghostCircle{
       display: none;
  }

/* 画面の枠設定	*/
	div.menu{
	   float: left;
	   width: 17%;
	}
	div.blockb{
	 float: right;
	 width: 83%;
	}


/* Side menu styles */
	div.menu a {
		font-size:30px;
	    font-family:sans-serif;
	    color: black;
	}
	
	div.menu ul{
		margin: 0;
		padding: 0;
		list-style-type: none;
		-webkit-box-align: center; /* safari, Chrome */
		line-height: 100%;
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
		margin-left: auto;
		margin-right: auto;
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
<#include "../errormessage.ftl">
<!-- ID: ${ID} -->
<script src="https://code.jquery.com/jquery-1.10.2.min.js"></script>
<script src="https://d3js.org/d3.v3.min.js"></script>

<!-- side menu -->
	<div style="float:left;" class="menu">
	 	 <ul>
	 	 <br><br><font size="6" color="black"><center><U>GRAB Tree</U></center></font><br>
		 <font size="5" color="black"><center>${ID}</center></font>
	 	 <li><a href="/GRABGraph/${ID}">GRAB Graph</a></li>
	 	 <li><a href="/Structures/Glycans/${ID}">Return</a></li>
	 	 <font size="1" color="black"><B>GRAB</B> allows users to easily understand the relationships of glycan structures in an informational graphic.</font><br>
	 	 <font size="1" color="black"><B>GRAB Tree</B> displays multiple relationships in tree format.  Relationships such as motifs, superstructures, substructures, and topology will be displayed as they become available.</font><br>
	 	 <font size="1" color="black"><B>GRAB Graph</B> displays the relationships in four directions: superstructures (Above), substructures (Below), subsumes (Left), and subsumed by (Right) with the selected glycan at the center.</font><br>
	 	 <font size="1" color="black"><B>Mouse over function</B>: User can easily check the selected glycan’s structure and accession number.</font><br>
	 	 <font size="1" color="black"><B>Right-click function</B>: A menu will display where Users can select to display the glycan’s entry page, to show a new GRAB page with the selected glycan, copy the glycan accession number, etc.</font><br>
	 	 </ul>
	</div>
	<div style="float:right;" class="blockb">
	
<script>
d3.contextMenu = function (menu, openCallback) {

	// create the div element that will hold the context menu
	d3.selectAll('.d3-context-menu').data([1])
		.enter()
		.append('div')
		.attr('class', 'd3-context-menu');

	// close menu
	d3.select('body').on('click.d3-context-menu', function() {
		d3.select('.d3-context-menu').style('display', 'none');
	});

	// this gets executed when a contextmenu event occurs
	return function(data, index) {
		var elm = this;	
		d3.selectAll('.d3-context-menu').html('');
		var list = d3.selectAll('.d3-context-menu').append('ul');
		list.selectAll('li').data(menu).enter()
			.append('li')
			.html(function(d) {
				return (typeof d.title === 'string') ? d.title : d.title(data);
			})
			.on('click', function(d, i) {
				d.action(elm, data, index);
				d3.select('.d3-context-menu').style('display', 'none');
			});

		// the openCallback allows an action to fire before the menu is displayed
		// an example usage would be closing a tooltip
		if (openCallback) {
			if (openCallback(data, index) === false) {
				return;
			}
		}

		// display context menu
		d3.select('.d3-context-menu')
			.style('left', (d3.event.pageX - 2) + 'px')
			.style('top', (d3.event.pageY - 2) + 'px')
			.style('display', 'block');

		d3.event.preventDefault();
		d3.event.stopPropagation();
	};
};

// Function of copy
var copyTextToClipboard = function(txt){
    var copyArea = $("<textarea/>");
    copyArea.text(txt);
    $("body").append(copyArea);
    copyArea.select();
    document.execCommand("copy");
    copyArea.remove();
}

// context menu

var menu = [
	{
		title: 'Copy this accession number',
		action: function(elm, d, i) {
			// Copy this accession number
			var cTxt = d.name;
			copyTextToClipboard(cTxt);
			// alert("Copy this accession number \"" + d.name + "\" in Clipboard");
		}
	},
	{
		title: 'Center on this glycan',
		action: function(elm, d, i) {		
			// Center on this glycan
			if (d.name != null) {
				// link to this glycan's relation
				location.href = "/GRABTree/" + d.name;
			}
		}
	},
	{
		title: 'Open this glycan\'s entry page',
		action: function(elm, d, i) {		
			// Open this glycan\'s entry page
			if (d.name != null) {
				// New Tab: link to this glycan's entry page
				window.open('/Structures/Glycans/'+ d.name);
				// New Window
				//window.open('/Structures/Glycans/'+ d.name, '_blank', 'width=800,height=600');
			}
		}
	},
	{
		title: 'Show this glycan image',
		action: function(elm, d, i) {
			// Show of this glycan image
			if (d.name != null) {
				// New Tab: link to this glycan's relation
				window.open('/glycans/'+ d.name + '/image?style=extended&format=png&notation=cfg');
				// New Window
				// window.open('/glycans/'+ d.name + '/image?style=extended&format=png&notation=cfg', '_blank', 'width=800,height=600');
			}
		}
	}
];

d3.json("/Tree/D3retrieve?primaryId=${ID}", function(error, treeData) {

	    // Calculate total nodes, max label length
	    var totalNodes = 0;
	    var maxLabelLength = 0;
	    // variables for drag/drop
	    var selectedNode = null;
	    var draggingNode = null;
	    // panning variables
	    var panSpeed = 200;
	    var panBoundary = 20; // Within 20px from edges will pan when dragging.
	    // Misc. variables
	    var i = 0;
	    var duration = 750;
	    var root;

	    // size of the diagram
	    var viewerWidth = $(document).width()*0.85;
	    var viewerHeight = $(document).height();

	    var tree = d3.layout.tree()
	        .size([viewerHeight, viewerWidth]);

	    // define a d3 diagonal projection for use by the node paths later on.
	    var diagonal = d3.svg.diagonal()
	        .projection(function(d) {
	            return [d.y, d.x];
	        });

	    // A recursive helper function for performing some setup by walking through all nodes

	    function visit(parent, visitFn, childrenFn) {
	        if (!parent) return;

	        visitFn(parent);

	        var children = childrenFn(parent);
	        if (children) {
	            var count = children.length;
	            for (var i = 0; i < count; i++) {
	                visit(children[i], visitFn, childrenFn);
	            }
	        }
	    }

	    // Call visit function to establish maxLabelLength
	    visit(treeData, function(d) {
	        totalNodes++;
	        maxLabelLength = Math.max(d.name.length, maxLabelLength);

	    }, function(d) {
	        return d.children && d.children.length > 0 ? d.children : null;
	    });


	    // sort the tree according to the node names

	    function sortTree() {
	        tree.sort(function(a, b) {
	            return b.name.toLowerCase() < a.name.toLowerCase() ? 1 : -1;
	        });
	    }
	    // Sort the tree initially incase the JSON isn't in a sorted order.
	    sortTree();

	    // TODO: Pan function, can be better implemented.

	    function pan(domNode, direction) {
	        var speed = panSpeed;
	        if (panTimer) {
	            clearTimeout(panTimer);
	            translateCoords = d3.transform(svgGroup.attr("transform"));
	            if (direction == 'left' || direction == 'right') {
	                translateX = direction == 'left' ? translateCoords.translate[0] + speed : translateCoords.translate[0] - speed;
	                translateY = translateCoords.translate[1];
	            } else if (direction == 'up' || direction == 'down') {
	                translateX = translateCoords.translate[0];
	                translateY = direction == 'up' ? translateCoords.translate[1] + speed : translateCoords.translate[1] - speed;
	            }
	            scaleX = translateCoords.scale[0];
	            scaleY = translateCoords.scale[1];
	            scale = zoomListener.scale();
	            svgGroup.transition().attr("transform", "translate(" + translateX + "," + translateY + ")scale(" + scale + ")");
	            d3.select(domNode).select('g.node').attr("transform", "translate(" + translateX + "," + translateY + ")");
	            zoomListener.scale(zoomListener.scale());
	            zoomListener.translate([translateX, translateY]);
	            panTimer = setTimeout(function() {
	                pan(domNode, speed, direction);
	            }, 50);
	        }
	    }

	    // Define the zoom function for the zoomable tree

	    function zoom() {
	        svgGroup.attr("transform", "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")");
	    }


	    // define the zoomListener which calls the zoom function on the "zoom" event constrained within the scaleExtents
	    var zoomListener = d3.behavior.zoom().scaleExtent([0.1, 3]).on("zoom", zoom);

	    function initiateDrag(d, domNode) {
	        draggingNode = d;
	        d3.select(domNode).select('.ghostCircle').attr('pointer-events', 'none');
	        d3.selectAll('.ghostCircle').attr('class', 'ghostCircle show');
	        d3.select(domNode).attr('class', 'node activeDrag');

	        svgGroup.selectAll("g.node").sort(function(a, b) { // select the parent and sort the path's
	            if (a.id != draggingNode.id) return 1; // a is not the hovered element, send "a" to the back
	            else return -1; // a is the hovered element, bring "a" to the front
	        });
	        // if nodes has children, remove the links and nodes
	        if (nodes.length > 1) {
	            // remove link paths
	            links = tree.links(nodes);
	            nodePaths = svgGroup.selectAll("path.link")
	                .data(links, function(d) {
	                    return d.target.id;
	                }).remove();
	            // remove child nodes
	            nodesExit = svgGroup.selectAll("g.node")
	                .data(nodes, function(d) {
	                    return d.id;
	                }).filter(function(d, i) {
	                    if (d.id == draggingNode.id) {
	                        return false;
	                    }
	                    return true;
	                }).remove();
	        }

	        // remove parent link
	        parentLink = tree.links(tree.nodes(draggingNode.parent));
	        svgGroup.selectAll('path.link').filter(function(d, i) {
	            if (d.target.id == draggingNode.id) {
	                return true;
	            }
	            return false;
	        }).remove();

	        dragStarted = null;
	    }

	    // define the baseSvg, attaching a class for styling and the zoomListener
	    var baseSvg = d3.select("#tree-container").append("svg")
	        .attr("width", viewerWidth)
	        .attr("height", viewerHeight)
	        .attr("class", "overlay")
	        .call(zoomListener);


	    // Define the drag listeners for drag/drop behaviour of nodes.
    dragListener = d3.behavior.drag()
	        // .on("dragstart", function(d) {
        //     if (d == root) {
        //         return;
        //     }
        //     dragStarted = true;
        //     nodes = tree.nodes(d);
        //     d3.event.sourceEvent.stopPropagation();
        //     // it's important that we suppress the mouseover event on the node being dragged. Otherwise it will absorb the mouseover event and the underlying node will not detect it d3.select(this).attr('pointer-events', 'none');
        // })
	        .on("drag", function(d) {
	            if (d == root) {
	                return;
	            }
	            // if (dragStarted) {
            //     domNode = this;
            //     initiateDrag(d, domNode);
            // }

	            // get coords of mouseEvent relative to svg container to allow for panning
	            relCoords = d3.mouse($('svg').get(0));
	            if (relCoords[0] < panBoundary) {
	                panTimer = true;
	                pan(this, 'left');
	            } else if (relCoords[0] > ($('svg').width() - panBoundary)) {

	                panTimer = true;
	                pan(this, 'right');
	            } else if (relCoords[1] < panBoundary) {
	                panTimer = true;
	                pan(this, 'up');
	            } else if (relCoords[1] > ($('svg').height() - panBoundary)) {
	                panTimer = true;
	                pan(this, 'down');
	            } else {
	                try {
	                    clearTimeout(panTimer);
	                } catch (e) {

	                }
	            }

	            d.x0 += d3.event.dy;
	            d.y0 += d3.event.dx;
	            var node = d3.select(this);
	            node.attr("transform", "translate(" + d.y0 + "," + d.x0 + ")");
	            updateTempConnector();
	        }).on("dragend", function(d) {
	            if (d == root) {
	                return;
	            }
	            domNode = this;
	            if (selectedNode) {
	                // now remove the element from the parent, and insert it into the new elements children
	                var index = draggingNode.parent.children.indexOf(draggingNode);
	                if (index > -1) {
	                    draggingNode.parent.children.splice(index, 1);
	                }
	                if (typeof selectedNode.children !== 'undefined' || typeof selectedNode._children !== 'undefined') {
	                    if (typeof selectedNode.children !== 'undefined') {
	                        selectedNode.children.push(draggingNode);
	                    } else {
	                        selectedNode._children.push(draggingNode);
	                    }
	                } else {
	                    selectedNode.children = [];
	                    selectedNode.children.push(draggingNode);
	                }
	                // Make sure that the node being added to is expanded so user can see added node is correctly moved
	                expand(selectedNode);
	                sortTree();
	                endDrag();
	            } else {
	                endDrag();
	            }
	        });

	    function endDrag() {
	        selectedNode = null;
	        d3.selectAll('.ghostCircle').attr('class', 'ghostCircle');
	        d3.select(domNode).attr('class', 'node');
	        // now restore the mouseover event or we won't be able to drag a 2nd time
	        d3.select(domNode).select('.ghostCircle').attr('pointer-events', '');
	        updateTempConnector();
	        if (draggingNode !== null) {
	            update(root);
	            centerNode(draggingNode);
	            draggingNode = null;
	        }
	    }

	    // Helper functions for collapsing and expanding nodes.

	    function collapse(d) {
	        if (d.children) {
	            d._children = d.children;
	            d._children.forEach(collapse);
	            d.children = null;
	        }
	    }

	    function expand(d) {
	        if (d._children) {
	            d.children = d._children;
	            d.children.forEach(expand);
	            d._children = null;
	        }
	    }

	    var overCircle = function(d) {
	        selectedNode = d;
	        updateTempConnector();
	    };
	    var outCircle = function(d) {
	        selectedNode = null;
	        updateTempConnector();
	    };

	    // Function to update the temporary connector indicating dragging affiliation
	    var updateTempConnector = function() {
	        var data = [];
	        if (draggingNode !== null && selectedNode !== null) {
	            // have to flip the source coordinates since we did this for the existing connectors on the original tree
	            data = [{
	                source: {
	                    x: selectedNode.y0,
	                    y: selectedNode.x0
	                },
	                target: {
	                    x: draggingNode.y0,
	                    y: draggingNode.x0
	                }
	            }];
	        }
	        var link = svgGroup.selectAll(".templink").data(data);

	        link.enter().append("path")
	            .attr("class", "templink")
	            .attr("d", d3.svg.diagonal())
	            .attr('pointer-events', 'none');

	        link.attr("d", d3.svg.diagonal());

	        link.exit().remove();
	    };

	    // Function to center node when clicked/dropped so node doesn't get lost when collapsing/moving with large amount of children.

	    function centerNode(source) {
	        scale = zoomListener.scale();
	        x = -source.y0;
	        y = -source.x0;
	        x = x * scale + viewerWidth / 3.5;
	        y = y * scale + viewerHeight / 2;
	        d3.select('g').transition()
	            .duration(duration)
	            .attr("transform", "translate(" + x + "," + y + ")scale(" + scale + ")");
	        zoomListener.scale(scale);
	        zoomListener.translate([x, y]);
	    }

	    // Toggle children function

	    function toggleChildren(d) {
	        if (d.children) {
	            d._children = d.children;
	            d.children = null;
	        } else if (d._children) {
	            d.children = d._children;
	            d._children = null;
	        }
	        return d;
	    }

	    // Toggle children on click.

	    function click(d) {
	        if (d3.event.defaultPrevented) return; // click suppressed
	        d = toggleChildren(d);
	        update(d);
	        centerNode(d);
	    }

	    function update(source) {
	        // Compute the new height, function counts total children of root node and sets tree height accordingly.
	        // This prevents the layout looking squashed when new nodes are made visible or looking sparse when nodes are removed
	        // This makes the layout more consistent.
	        var levelWidth = [1];
	        var childCount = function(level, n) {

	            if (n.children && n.children.length > 0) {
	                if (levelWidth.length <= level + 1) levelWidth.push(0);

	                levelWidth[level + 1] += n.children.length;
	                n.children.forEach(function(d) {
	                    childCount(level + 1, d);
	                });
	            }
	        };
	        childCount(0, root);
	        var newHeight = d3.max(levelWidth) * 25; // 25 pixels per line
	        tree = tree.size([newHeight, viewerWidth]);

	        // Compute the new tree layout.
	        var nodes = tree.nodes(root).reverse(),
	            links = tree.links(nodes);

	        // Set widths between levels based on maxLabelLength.
	        nodes.forEach(function(d) {
	            d.y = (d.depth * (maxLabelLength * 10)); //maxLabelLength * 10px
	            // alternatively to keep a fixed scale one can set a fixed depth per level
	            // Normalize for fixed-depth by commenting out below line
	            // d.y = (d.depth * 500); //500px per level.
	        });

	        // Update the nodes…
	        node = svgGroup.selectAll("g.node")
	            .data(nodes, function(d) {
	                return d.id || (d.id = ++i);
	            });

	        // ☆Enter any new nodes at the parent's previous position.
	        var nodeEnter = node.enter().append("g")
	            .call(dragListener)
	            .attr("class", "node")
	            .attr("transform", function(d) {
	                return "translate(" + source.y0+"," + source.x0 + ")";
	            })
	            .on('click', click);

	        nodeEnter.append("circle")
	            .attr('class', 'nodeCircle')
	            .attr("r", 0)
	            .style("fill", function(d) {
	                return d._children ? "lightsteelblue" : "#fff";
	            });

	        nodeEnter.append("text")
	            .attr("x", function(d) {
	                return d.children || d._children ? -10 : 10;
	            })
	            .attr("dy", ".35em")
	            .attr('class', 'nodeText')
	            .attr("text-anchor", function(d) {
	                return d.children || d._children ? "end" : "start";
	            })
	            .text(function(d) {
	                return d.name;
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
	                      if (d.name == "has_topology")
	                        return "skyblue";
	                      if (d.name == "topology_contained_by")
	                        return "purple";
	                      if (d.name == "subsumes")
	                        return "skyblue";
	                      if (d.name == "subsumed_by")
	                        return "purple";
	                      if (d.name == "None")
	                      	return "black";
	                      else  return "black";
	                })
	            .style("fill-opacity", 0);

		var text3;
	    var text_name3 = null;
	            nodeEnter.append("image")
	            .attr("xlink:href", function(d){
	             if(d.name == "has_motif"|| d.name =="has_linkage_isomer"|| d.name =="has_topology"|| d.name =="topology_contained_by"|| d.name =="subsumes"|| d.name =="subsumed_by"|| d.name =="has_superstructure"|| d.name =="has_substructure" || d.name == "None")
	               return null;
	             else  return "/glycans/"+ d.name + "/image?style=extended&format=png&notation=cfg";
	         })
	            .attr("width", function(d){
	                if (d.size == null)
	                   return 150;
	                else return 50;
	                })
	            .attr("height", function(d){
	                if (d.size == null)
	                   return 50;
	                else return 20;
	                })
	            .attr("transform", function(d){
	                if (d.size == null)
	                   return "translate(-120,-30)";
	                else return "translate(10,-10)";
	              })
	              .on("mouseover", function (d) {
	                  d3.select(this).attr("width", 200)
	                                 .attr("height", 100)
	                                 .attr("xlink:href", function(d){
	                                 if(d.name == "has_motif"|| d.name =="has_linkage_isomer"|| d.name =="has_topology"|| d.name =="topology_contained_by"|| d.name =="subsumes"|| d.name =="subsumed_by"|| d.name =="has_superstructure"|| d.name =="has_substructure"|| d.name == "None")
	                                   return null;
	                                 else  return "/glycans/"+ d.name + "/image?style=extended&format=png&notation=cfg";
	                               })
	                               .attr("transform", function(d){
	                                 if(d.size == null)
	                                  return "translate(-120,-30)";
	                                else return "translate(100,-10)";
	                              });
	                        if(d.name == "has_motif"|| d.name =="has_linkage_isomer"|| d.name =="has_topology"|| d.name =="topology_contained_by"|| d.name =="subsumes"|| d.name =="subsumed_by"|| d.name =="has_superstructure"|| d.name =="has_substructure"|| d.name == "None"){text_name3 = null;}
	                        else if (d.size == null){text_name3 = null;}
	                        else{text_name3 = d.name;};

	                          text3 = nodeEnter.append("text")
	                          .attr("x", function(d) {
	                              return d.children || d._children ? -10 : 10;
	                          })
	                          .attr("dy", ".35em")
	                          .attr('class', 'nodeText')
	                          .attr("text-anchor", function(d) {
	                              return d.children || d._children ? "end" : "start";
	                          })
	                          .text(function(d) {
					                 if (d.name == text_name3){
						                 if(d.size == null){
						                 	return null;
						                 }
						                  return d.name;
									}
			                        else return null;
	                            })
	                          .attr("transform", function(d){
	                            if(d.size == null)
	                             return "translate(-30,100)";
	                           else return "translate(100,100)";
	                         });
	              })
	              .on("mouseout", function (d) {
	                  d3.select(this).attr("width", function(d){
	                      if (d.size == null)
	                         return 150;
	                      else return 50;
	                      })
	                  .attr("height", function(d){
	                      if (d.size == null)
	                         return 50;
	                      else return 20;
	                      })
	                      .attr("xlink:href", function(d){
	                      if(d.name == "has_motif"|| d.name =="has_linkage_isomer"|| d.name =="has_topology"|| d.name =="topology_contained_by"|| d.name =="subsumes"|| d.name =="subsumed_by"|| d.name =="has_superstructure"|| d.name =="has_substructure"|| d.name == "None")
	                        return null;
	                      else  return "/glycans/"+ d.name + "/image?style=extended&format=png&notation=cfg";
	                    })
	                    .attr("transform", function(d){
	                      if(d.size == null)
	                       return "translate(-120,-30)";
	                     else return "translate(10,-10)";
	                   });
	                   text3.remove();
	                    //.attr("class",orderlist[0]);
	              })
	              .on('contextmenu', d3.contextMenu(menu));


	        // phantom node to give us mouseover in a radius around it
	        nodeEnter.append("circle")
	            .attr('class', 'ghostCircle')
	            .attr("r", 30)
	            .attr("opacity", 0.2) // change this to zero to hide the target area
	        	.style("fill", "red")
	            .attr('pointer-events', 'mouseover')
	            .on("mouseover", function(node) {
	                overCircle(node);
	            })
	            .on("mouseout", function(node) {
	                outCircle(node);
	            });

	        // Update the text to reflect whether node has children or not.
	        node.select('text')
	            .attr("x", function(d) {
	                return d.children || d._children ? -10 : 10;
	            })
	            .attr("text-anchor", function(d) {
	                return d.children || d._children ? "end" : "start";
	            })
	            .text(function(d) {
	                if(d.name == "has_motif"|| d.name =="has_linkage_isomer"|| d.name =="has_topology"|| d.name =="topology_contained_by"|| d.name =="subsumes"|| d.name =="subsumed_by"|| d.name =="has_superstructure"|| d.name =="has_substructure"|| d.name == "None")
	                  return d.name;
	                else return null;
	              });

	        // Change the circle fill depending on whether it has children and is collapsed
	        node.select("circle.nodeCircle")
	            //.attr("r", 4.5)
	            .attr("r", function(d){
	          	  if (d.name == "has_motif"||d.name == "has_linkage_isomer"||d.name == "has_superstructure"||d.name == "has_substructure"||d.name == "has_topology"||d.name == "topology_contained_by"|| d.name =="subsumes"|| d.name =="subsumed_by"|| d.name == "None")
	          	  		return null;
	          	  else return 4.5;
	            })
	            .style("fill",function(d){
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
	                return d._children ? "lightsteelblue" : "#fff";
	            })
	            .attr("stroke","black");

					var text_name = null;
			        var text2;
			            node.append("image")
			                  // .on("click",function(d){
			                  //   alert(d.name +"<br>"+ d.x +"<br>" + d.y);
			                  // })
			                  .attr("xlink:href", function(d){
			            	     if(d.name == "has_motif"|| d.name =="has_linkage_isomer"|| d.name =="has_topology"|| d.name =="topology_contained_by"|| d.name =="subsumes"|| d.name =="subsumed_by"|| d.name =="has_superstructure"|| d.name =="has_substructure"|| d.name == "None")
			            	       return null;
			            	     else  return "/glycans/"+ d.name + "/image?style=extended&format=png&notation=cfg";
			             	 })
			                  .attr("width", function(d){
			                    	if (d.size == null)
			                   		   return 150;
			                   	  else return 50;
			                      })
			                  .attr("height", function(d){
			                    	if (d.size == null)
			                   		   return 50;
			                   	  else return 20;
			                      })
			                  .attr("transform", function(d){
			                    	if (d.size == null)
			                   		   return "translate(-120,-30)";
			                   	  else return "translate(10,-10)";
			                    })
			                    // .attr("class",orderlist[0])
			                    //.attr('pointer-events', 'mouseover')
			                    .on("mouseover", function (d) {
			                        d3.select(this).attr("width", 200)
			                                       .attr("height", 100)
			                                       .attr("xlink:href", function(d){
			                                 	     if(d.name == "has_motif"|| d.name =="has_linkage_isomer"|| d.name =="has_topology"|| d.name =="topology_contained_by"|| d.name =="subsumes"|| d.name =="subsumed_by"|| d.name =="has_superstructure"|| d.name =="has_substructure"|| d.name == "None")
			                                 	       return null;
			                                 	     else  return "/glycans/"+ d.name + "/image?style=extended&format=png&notation=cfg";
			                                  	 })
			                                     .attr("transform", function(d){
			                                       if(d.size == null)
			                                        return "translate(-120,-30)";
			                                  	  else return "translate(100,-10)";
			                                    });
			                          if(d.name == "has_motif"|| d.name =="has_linkage_isomer"|| d.name =="has_topology"|| d.name =="topology_contained_by"|| d.name =="subsumes"|| d.name =="subsumed_by"|| d.name =="has_superstructure"|| d.name =="has_substructure"|| d.name == "None"){text_name = null;}
			                          else if (d.size == null){text_name = null;}
			                          else{text_name = d.name;};

			                            text2 = node.append("text")
			                            .attr("x", function(d) {
			                                return d.children || d._children ? -10 : 10;
			                            })
			                            .attr("dy", ".35em")
			                            .attr('class', 'nodeText')
			                            .attr("text-anchor", function(d) {
			                                return d.children || d._children ? "end" : "start";
			                            })
			                            .text(function(d) {
							                 if (d.name == text_name){
								                 if(d.size == null){
								                 	return null;
								                 }
								                  return d.name;
											}
					                        else return null;
			                            })
			                            .attr("transform", function(d){
			                              if(d.size == null)
			                               return "translate(-30,100)";
			                             else return "translate(100,100)";
			                           });
			                                     //iamge2.attr("class",orderlist[1]);
			                          //alert(d.name);
			                    })
			                    .on("mouseout", function (d) {
			                        d3.select(this).attr("width", function(d){
			                          	if (d.size == null)
			                         		   return 150;
			                         	  else return 50;
			                            })
			                        .attr("height", function(d){
			                          	if (d.size == null)
			                         		   return 50;
			                         	  else return 20;
			                            })
			                            .attr("xlink:href", function(d){
			                            if(d.name == "has_motif"|| d.name =="has_linkage_isomer"|| d.name =="has_topology"|| d.name =="topology_contained_by"|| d.name =="subsumes"|| d.name =="subsumed_by"|| d.name =="has_superstructure"|| d.name =="has_substructure"|| d.name == "None")
			                              return null;
			                            else  return "/glycans/"+ d.name + "/image?style=extended&format=png&notation=cfg";
			                          })
			                          .attr("transform", function(d){
			                            if(d.size == null)
			                             return "translate(-120,-30)";
			                           else return "translate(10,-10)";
			                         });
			                         text2.remove();

			                          //.attr("class",orderlist[0]);
			                    })
			                    .on('contextmenu', d3.contextMenu(menu));


	        // Transition nodes to their new position.
	        var nodeUpdate = node.transition()
	            .duration(duration)
	            .attr("transform", function(d) {
	                return "translate(" + d.y + "," + d.x + ")";
	            });

	        // Fade the text in
	        nodeUpdate.select("text")
	            .style("fill-opacity", 1);

	        // Transition exiting nodes to the parent's new position.
	        var nodeExit = node.exit().transition()
	            .duration(duration)
	            .attr("transform", function(d) {
	                return "translate(" + source.y + "," + source.x + ")";
	            })
	            .remove();

	        nodeExit.select("circle")
	            .attr("r", 0);

	        nodeExit.select("text")
	            .style("fill-opacity", 0);

	        // Update the links…
	        var link = svgGroup.selectAll("path.link")
	            .data(links, function(d) {
	                return d.target.id;
	            });

	        // Enter any new links at the parent's previous position.
	        link.enter().insert("path", "g")
	            .attr("class", "link")
	            .attr("d", function(d) {
	                var o = {
	                    x: source.x0,
	                    y: source.y0
	                };
	                return diagonal({
	                    source: o,
	                    target: o
	                });
	            });

	        // Transition links to their new position.
	        link.transition()
	            .duration(duration)
	            .attr("d", diagonal);

	        // Transition exiting nodes to the parent's new position.
	        link.exit().transition()
	            .duration(duration)
	            .attr("d", function(d) {
	                var o = {
	                    x: source.x,
	                    y: source.y
	                };
	                return diagonal({
	                    source: o,
	                    target: o
	                });
	            })
	            .remove();

	        // Stash the old positions for transition.
	        nodes.forEach(function(d) {
	            d.x0 = d.x;
	            d.y0 = d.y;
	        });
	    }

	    // Append a group which holds all nodes and which the zoom Listener can act upon.
	    var svgGroup = baseSvg.append("g");

	    // Define the root
	    root = treeData;
	    root.x0 = viewerHeight / 2;
	    root.y0 = 0;

	    // Layout the tree initially and center on the root node.
	    update(root);
	    centerNode(root);
	});


</script>
    <div id="tree-container"></div>
</div>
<#include "../footer.html">
</div>

</body>
</html>
