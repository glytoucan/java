<!DOCTYPE>
<meta charset="utf-8">
<html lang="ja">
<head>
	<title>GRAB Graph visualization</title>

<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1, maximum-scale=1">

<script src="//code.jquery.com/jquery-2.0.3.min.js"></script>
<script src="//cytoscape.github.io/cytoscape.js/api/cytoscape.js-latest/cytoscape.min.js"></script>
<script src="https://raw.githubusercontent.com/cytoscape/cytoscape.js/v2.7.13/dist/cytoscape.min.js"></script>
<link data-require="fontawesome@4.1.0" data-semver="4.2.0" rel="stylesheet" href="//netdna.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css" />

<!-- ID: ${ID} -->
<style>
	body {
		font-family: helvetica;
		font-size: 14px;
	}

	#cy {
		width: 100%;
		height: auto;
		height: 90%; 
		position: relative;
		left: 0;
		top: 0;
		bottom: 0;
		right: 0;
		z-index: 999; 
		background: #f2f2f2;
	}

	h1 {
		opacity: 0.5;
		font-size: 1em;
		font-weight: bold;
	}
	
	#footer{
		width:100%;
		position:fixed;
		bottom:0;
		z-index: 1000;
	}
	
/* cytoscape-context-menu styles */
	#cy-context-menus-cxt-menu {
			display:none;
			z-index:1000;
			position:absolute;
			border:1px solid #A0A0A0;
			padding: 0;
			margin: 0;
	}

	.cy-context-menus-cxt-menuitem{
			display:block;
			z-index:1000;
			min-width: 200px;
			padding: 3px 20px;
			position:relative;
			margin:0;
			background-color:#F2F2F2;
			font-weight:normal;
			font-size: 14px;
			white-space:nowrap;
			border: 0;
			text-align: left;
	}

	.cy-context-menus-cxt-menuitem:enabled {
			color: #000000;
	}

	.cy-context-menus-ctx-operation:focus{
		outline: none;
	}

	.cy-context-menus-cxt-menuitem:hover{
			color: #ffffff;
			text-decoration: none;
			background-color: #0B9BCD;
			background-image: none;
			cursor: pointer;
	}

	.cy-context-menus-cxt-menuitem[title]:before{
			content:attr(title);
	}

	.cy-context-menus-cxt-menuitem:not([title]):before{
			content:"\2630";
	}

	.cy-context-menus-divider {
		border-bottom:1px solid #A0A0A0;
	}
	
	/* panzoom */
      .cy-panzoom {
      	position: absolute;
      	font-size: 12px;
      	color: #fff;
      	font-family: arial, helvetica, sans-serif;
      	line-height: 1;
      	color: #666;
      	font-size: 11px;
      	z-index: 99999;
      	box-sizing: content-box;
      }

      .cy-panzoom-zoom-button {
      	cursor: pointer;
      	padding: 3px;
      	text-align: center;
      	position: absolute;
      	border-radius: 3px;
      	width: 10px;
      	height: 10px;
      	left: 16px;
      	background: #fff;
      	border: 1px solid #999;
      	margin-left: -1px;
      	margin-top: -1px;
      	z-index: 1;
      	box-sizing: content-box;
      }

      .cy-panzoom-zoom-button:active,
      .cy-panzoom-slider-handle:active,
      .cy-panzoom-slider-handle.active {
      	background: #ddd;
      	box-sizing: content-box;
      }

      .cy-panzoom-pan-button {
      	position: absolute;
      	z-index: 1;
      	height: 16px;
      	width: 16px;
      	box-sizing: content-box;
      }

      .cy-panzoom-reset {
      	top: 55px;
      	box-sizing: content-box;
      }

      .cy-panzoom-zoom-in {
      	top: 80px;
      	box-sizing: content-box;
      }

      .cy-panzoom-zoom-out {
      	top: 197px;
      	box-sizing: content-box;
      }

      .cy-panzoom-pan-up {
      	top: 0;
      	left: 50%;
      	margin-left: -5px;
      	width: 0;
      	height: 0;
      	border-left: 5px solid transparent;
      	border-right: 5px solid transparent;
      	border-bottom: 5px solid #666;
      	box-sizing: content-box;
      }

      .cy-panzoom-pan-down {
      	bottom: 0;
      	left: 50%;
      	margin-left: -5px;
      	width: 0;
      	height: 0;
      	border-left: 5px solid transparent;
      	border-right: 5px solid transparent;
      	border-top: 5px solid #666;
      	box-sizing: content-box;
      }

      .cy-panzoom-pan-left {
      	top: 50%;
      	left: 0;
      	margin-top: -5px;
      	width: 0;
      	height: 0;
      	border-top: 5px solid transparent;
      	border-bottom: 5px solid transparent;
      	border-right: 5px solid #666;
      	box-sizing: content-box;
      }

      .cy-panzoom-pan-right {
      	top: 50%;
      	right: 0;
      	margin-top: -5px;
      	width: 0;
      	height: 0;
      	border-top: 5px solid transparent;
      	border-bottom: 5px solid transparent;
      	border-left: 5px solid #666;
      	box-sizing: content-box;
      }

      .cy-panzoom-pan-indicator {
      	position: absolute;
      	left: 0;
      	top: 0;
      	width: 8px;
      	height: 8px;
      	border-radius: 8px;
      	background: #000;
      	border-radius: 8px;
      	margin-left: -5px;
      	margin-top: -5px;
      	display: none;
      	z-index: 999;
      	opacity: 0.6;
      	box-sizing: content-box;
      }

      .cy-panzoom-slider {
      	position: absolute;
      	top: 97px;
      	left: 17px;
      	height: 100px;
      	width: 15px;
      	box-sizing: content-box;
      }

      .cy-panzoom-slider-background {
      	position: absolute;
      	top: 0;
      	width: 2px;
      	height: 100px;
      	left: 5px;
      	background: #fff;
      	border-left: 1px solid #999;
      	border-right: 1px solid #999;
      	box-sizing: content-box;
      }

      .cy-panzoom-slider-handle {
      	position: absolute;
      	width: 16px;
      	height: 8px;
      	background: #fff;
      	border: 1px solid #999;
      	border-radius: 2px;
      	margin-left: -2px;
      	z-index: 999;
      	line-height: 8px;
      	cursor: default;
      	box-sizing: content-box;
      }

      .cy-panzoom-slider-handle .icon {
      	margin: 0 4px;
      	line-height: 10px;
      	box-sizing: content-box;
      }

      .cy-panzoom-no-zoom-tick {
      	position: absolute;
      	background: #666;
      	border: 1px solid #fff;
      	border-radius: 2px;
      	margin-left: -1px;
      	width: 8px;
      	height: 2px;
      	left: 3px;
      	z-index: 1;
      	margin-top: 3px;
      	box-sizing: content-box;
      }

      .cy-panzoom-panner {
      	position: absolute;
      	left: 5px;
      	top: 5px;
      	height: 40px;
      	width: 40px;
      	background: #fff;
      	border: 1px solid #999;
      	border-radius: 40px;
      	margin-left: -1px;
      	box-sizing: content-box;
      }

      .cy-panzoom-panner-handle {
      	position: absolute;
      	left: 0;
      	top: 0;
      	outline: none;
      	height: 40px;
      	width: 40px;
      	position: absolute;
      	z-index: 999;
      	box-sizing: content-box;
      }

      .cy-panzoom-zoom-only .cy-panzoom-slider,
      .cy-panzoom-zoom-only .cy-panzoom-panner {
      	display: none;
      }

      .cy-panzoom-zoom-only .cy-panzoom-reset {
      	top: 20px;
      }

      .cy-panzoom-zoom-only .cy-panzoom-zoom-in {
      	top: 45px;
      }

      .cy-panzoom-zoom-only .cy-panzoom-zoom-out {
      	top: 70px;
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
		}
		div.menu li{
			display: inline;
			padding: 0;
			margin: 0;
		}
		div.menu li a{
			display: block;
			width: 150px;
			padding: 2px;
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
		
		/* 画面の枠設定	*/
			div.menu{
			   float: left;
			   width: 17%;
			}
			div.blockb{
			 float: right;
			 width: 83%;
			}

</style>

<script>
	$.getJSON(("/Graph/Graphretrieve?primaryId=${ID}"),function(GRABElements){
		var cy = window.cy = cytoscape({
			container: document.getElementById('cy'),
			elements: GRABElements,
			layout: {
				name: 'preset',

				positions: undefined, // map of (node id) => (position obj); or function(node){ return somPos; }
				zoom: undefined, // the zoom level to set (prob want fit = false if set)
				pan: undefined, // the pan level to set (prob want fit = false if set)
				fit: true, // whether to fit to viewport
				padding: 30, // padding on fit
				animate: false, // whether to transition the node positions
				animationDuration: 500, // duration of animation in ms if enabled
				animationEasing: undefined, // easing of animation if enabled
				ready: undefined, // callback on layoutready
				stop: undefined // callback on layoutstop
			},


			style: cytoscape.stylesheet()
				.selector("node")
					.css({
						"shape": "rectangle",
						"width": "50px",
						"height": "20px",
						"background-color": "#fff",
						"font-size":"12px",
						"text-valign":"center",
						"text-halign":"center",
						"color":"#000",
						"overlay-padding":"6px",
						"background-image" : function(ele){
							if (ele.id() == "subsumedby"){
								return "/img/Subsumedby_picture.png";
							} else if (ele.id() == "subsumes"){
								return "/img/Subsumes_picture.png";
							} else if (ele.id() == "superstructure"){
								return "/img/Superstructure_picture.png";
							} else if (ele.id() == "substructure"){
								return "/img/Substructure_picture.png";
							} else {
								return "/glycans/" + ele.id() + "/image?style=extended&format=png&notation=cfg";
							}
						},
						"backgroud-width": "50px",
						"backgroud-height": "20px",
						"background-fit": "contain",
						"background-clip" : "none",
				})
				.selector("edge")
					.css({
						"curve-style": "bezier",
						"width": 3,
						"line-color": "#000",
						"target-arrow-shape" : "triangle",
						"target-arrow-color": "#000",
					})
				.selector(":parent")
					.css({
						"background-opacity": 0.333,
						"background-color": function(ele){
							if (ele.id() == "subsumedby_box"){
								return "#0000FF";
							} else if (ele.id() == "subsumes_box"){
								return "#FF0000";
							} else if (ele.id() == "superstructure_box"){
								return "#008000";
							} else if (ele.id() == "substructure_box"){
								return "#800080";
							} else {
								return "#fff";
							}
						}
					})
					.selector("node.addLabel") //This class is change the selected node
						.style({
								"label" : function(ele){
									if (ele.id() == "subsumedby"||ele.id() == "subsumes"||ele.id() == "superstructure"||ele.id() == "substructure"||ele.id() == "subsumedby_box"||ele.id() == "subsumes_box"||ele.id() == "superstructure_box"||ele.id() == "substructure_box"){
										return "";
									}
									else return ele.id("id");
								},
								"text-halign":"center",
								"text-valign":"bottom",
								"text-margin-x":"33px",
								"text-margin-y":"0px",
								"z-index":"20"
					})
				.selector("node.addImage")
					.style({
						"width" : function(ele){
							if (ele.id() == "subsumedby"||ele.id() == "subsumes"||ele.id() == "superstructure"||ele.id() == "substructure"||ele.id() == "subsumedby_box"||ele.id() == "subsumes_box"||ele.id() == "superstructure_box"||ele.id() == "substructure_box"){
								return "50px";
							}
							else return "100px";
						},
						"height" : function(ele){
							if (ele.id() == "subsumedby"||ele.id() == "subsumes"||ele.id() == "superstructure"||ele.id() == "substructure"||ele.id() == "subsumedby_box"||ele.id() == "subsumes_box"||ele.id() == "superstructure_box"||ele.id() == "substructure_box"){
								return "20px";
							}
							else return "40px";
						},
						"background-image" : function(ele){
							if (ele.id() == "subsumedby"){
								return "/img/Subsumedby_picture.png";
							} else if (ele.id() == "subsumes"){
								return "/img/Subsumes_picture.png";
							} else if (ele.id() == "superstructure"){
								return "/img/Superstructure_picture.png";
							} else if (ele.id() == "substructure"){
								return "/img/Substructure_picture.png";
							} else {
								return "/glycans/" + ele.id("id") + "/image?style=extended&format=png&notation=cfg";
							}
						}
					})
					
		});
		
		var isUserGrabbingN1 = cy.nodes().ungrabify();
		
		var _ua = (function(u){
		  return {
		    Tablet:(u.indexOf("windows") != -1 && u.indexOf("touch") != -1 && u.indexOf("tablet pc") == -1)
		      || u.indexOf("ipad") != -1
		      || (u.indexOf("android") != -1 && u.indexOf("mobile") == -1)
		      || (u.indexOf("firefox") != -1 && u.indexOf("tablet") != -1)
		      || u.indexOf("kindle") != -1
		      || u.indexOf("silk") != -1
		      || u.indexOf("playbook") != -1,
		    Mobile:(u.indexOf("windows") != -1 && u.indexOf("phone") != -1)
		      || u.indexOf("iphone") != -1
		      || u.indexOf("ipod") != -1
		      || (u.indexOf("android") != -1 && u.indexOf("mobile") != -1)
		      || (u.indexOf("firefox") != -1 && u.indexOf("mobile") != -1)
		      || u.indexOf("blackberry") != -1
		  }
		})(window.navigator.userAgent.toLowerCase());

		if(_ua.Mobile || _ua.Tablet){
			cy.on('touchstart', 'node', function(e){
				var sel = e.cyTarget;
				sel.addClass('addLabel').addClass('addLabel');
				sel.addClass('addImage').addClass('addImage');
			});
			cy.on('touchend', 'node', function(e){
					var sel = e.cyTarget;
					sel.removeClass('addLabel').removeClass('addLabel');
					sel.removeClass('addImage').removeClass('addImage');
			});
		}else{
			cy.on('mouseover', 'node', function(e){
	        var sel = e.cyTarget;
	        sel.addClass('addLabel').addClass('addLabel');
	        sel.addClass('addImage').addClass('addImage');  
	        });
	        cy.on('mouseout', 'node', function(e){
	            var sel = e.cyTarget;
	            sel.removeClass('addLabel').removeClass('addLabel');
	            sel.removeClass('addImage').removeClass('addImage');
	        });
	    };
		
		cy.panzoom({
			// options here...
		});

		var copyTextToClipboard = function(txt){
				var copyArea = $("<textarea/>");
				copyArea.text(txt);
				$("body").append(copyArea);
				copyArea.select();
				document.execCommand("copy");
				copyArea.remove();
		};
		
		// contextMenus
		cy.contextMenus({
	        menuItems: [
	            {
	                id: 'Copy this accession number',
	                title: 'Copy this accession number',
	                selector: 'node',
	                onClickFunction: function (event) {
	                  var sel = event.cyTarget.id();
	                  copyTextToClipboard(sel);
	                },
	              },
	              {
	                id: 'Center on this glycan',
	                title: 'Center on this glycan',
	                selector: 'node',
	                onClickFunction: function (event) {
	                  var sel = event.cyTarget.id();
	                  if ( sel != null) {
	                    location.href = "/GRABGraph/" + sel;
	            			}
	                },
	              },
	              {
	                id: 'Open this glycan\'s entry page',
	                title: 'Open this glycan\'s entry page',
	                selector: 'node',
	                onClickFunction: function (event) {
	                  var sel = event.cyTarget.id();
	              			if (sel != null) {
	                      window.open('/Structures/Glycans/'+ sel);
	              			}
	                }
	              },
	              {
	                id: 'Show this glycan image',
	                title: 'Show this glycan image',
	                selector: 'node',
	                onClickFunction: function (event) {
	                  var sel = event.cyTarget.id();
	              			if (sel != null) {
	                      		window.open('/glycans/' +sel+ '/image?style=extended&format=png&notation=cfg');
	              			}
	                }
	              }
	           ]
	       });
		
	});
	
	// cytoscape-context-menus.js
	;(function(){ 'use strict';

	  var $ = typeof jQuery === typeof undefined ? null : jQuery;
	
	  var register = function( cytoscape, $ ){
	
	    if( !cytoscape ){ return; } // can't register if cytoscape unspecified
	    var cy;
	
	    var defaults = {
	      // List of initial menu items
	      menuItems: [
	        /*
	        {
	          id: 'remove',
	          title: 'remove',
	          selector: 'node, edge',
	          onClickFunction: function () {
	            console.log('remove element');
	          },
	          hasTrailingDivider: true
	        },
	        {
	          id: 'hide',
	          title: 'hide',
	          selector: 'node, edge',
	          onClickFunction: function () {
	            console.log('hide element');
	          },
	          disabled: true
	        }*/
	      ],
	      // css classes that menu items will have
	      menuItemClasses: [
	        // add class names to this list
	      ],
	      // css classes that context menu will have
	      contextMenuClasses: [
	        // add class names to this list
	      ]
	    };
	
	    var options;
	    var $cxtMenu;
	    var menuItemCSSClass = 'cy-context-menus-cxt-menuitem';
	    var dividerCSSClass = 'cy-context-menus-divider';
	    var eventCyTapStart;
	    var active = false;
	
	    // Merge default options with the ones coming from parameter
	    function extend(defaults, options) {
	      var obj = {};
	
	      for (var i in defaults) {
	        obj[i] = defaults[i];
	      }
	
	      for (var i in options) {
	        obj[i] = options[i];
	      }
	
	      return obj;
	    };
	
	    function preventDefaultContextTap() {
	      $("#cy-context-menus-cxt-menu").contextmenu( function() {
	          return false;
	      });
	    }
	
	    // Get string representation of css classes
	    function getMenuItemClassStr(classes, hasTrailingDivider) {
	      var str = getClassStr(classes);
	
	      str += ' ' + menuItemCSSClass;
	
	      if(hasTrailingDivider) {
	        str += ' ' + dividerCSSClass;
	      }
	
	      return str;
	    }
	
	    // Get string representation of css classes
	    function getClassStr(classes) {
	      var str = '';
	
	      for( var i = 0; i < classes.length; i++ ) {
	        var className = classes[i];
	        str += className;
	        if(i !== classes.length - 1) {
	          str += ' ';
	        }
	      }
	
	      return str;
	    }
	
	    function displayComponent($component) {
	      $component.css('display', 'block');
	    }
	
	    function hideComponent($component) {
	      $component.css('display', 'none');
	    }
	
	    function hideMenuItemComponents() {
	      $cxtMenu.children().css('display', 'none');
	    }
	
	    function bindOnClickFunction($component, onClickFcn) {
	      var callOnClickFcn;
	
	      $component.on('click', callOnClickFcn = function() {
	        onClickFcn(cy.scratch('currentCyEvent'));
	      });
	
	      $component.data('call-on-click-function', callOnClickFcn);
	    }
	
	    function bindCyCxttap($component, selector, coreAsWell) {
	      var cxtfcn;
	      var cxtCoreFcn;
	
	      if(coreAsWell) {
	        cy.on('cxttap', cxtCoreFcn = function(event) {
	          if (sel.id() != "subsumedby"　&&　sel.id() != "subsumes"　&&　sel.id() != "superstructure"　&&　sel.id() != "substructure"　&&　sel.id() != "subsumedby_box"　&&　sel.id() != "subsumes_box"　&&　sel.id() != "superstructure_box"　&&　sel.id() != "substructure_box"){
		          
		          if( event.cyTarget != cy ) {
		            return;
		          }
		
		          cy.scratch('currentCyEvent', event);
		          adjustCxtMenu(event);
		          displayComponent($component);
		       }
		    });
	        cy.on('taphold', cxtCoreFcn = function(event) {
	          if (sel.id() != "subsumedby"　&&　sel.id() != "subsumes"　&&　sel.id() != "superstructure"　&&　sel.id() != "substructure"　&&　sel.id() != "subsumedby_box"　&&　sel.id() != "subsumes_box"　&&　sel.id() != "superstructure_box"　&&　sel.id() != "substructure_box"){
		          if( event.cyTarget != cy ) {
		            return;
		          }
		
		          cy.scratch('currentCyEvent', event);
		          adjustCxtMenu(event);
		          displayComponent($component);
		       }
	        });
	      }
	
	      if(selector) {
	        cy.on('cxttap', selector, cxtfcn = function(event) {
	        var sel = event.cyTarget;
	          if (sel.id() != "subsumedby"　&&　sel.id() != "subsumes"　&&　sel.id() != "superstructure"　&&　sel.id() != "substructure"　&&　sel.id() != "subsumedby_box"　&&　sel.id() != "subsumes_box"　&&　sel.id() != "superstructure_box"　&&　sel.id() != "substructure_box"){
		          cy.scratch('currentCyEvent', event);
		          adjustCxtMenu(event);
		          displayComponent($component);
		      }
	        });
	        cy.on('taphold', selector, cxtfcn = function(event) {
	          if (sel.id() != "subsumedby"　&&　sel.id() != "subsumes"　&&　sel.id() != "superstructure"　&&　sel.id() != "substructure"　&&　sel.id() != "subsumedby_box"　&&　sel.id() != "subsumes_box"　&&　sel.id() != "superstructure_box"　&&　sel.id() != "substructure_box"){  
		          cy.scratch('currentCyEvent', event);
		          adjustCxtMenu(event);
		          displayComponent($component);
		      }
	        });
	      }
	
	      // Bind the event to menu item to be able to remove it back
	      $component.data('cy-context-menus-cxtfcn', cxtfcn);
	      $component.data('cy-context-menus-cxtcorefcn', cxtCoreFcn);
	    }
	
	    function bindCyEvents() {
	      cy.on('tapstart', eventCyTapStart = function(){
	        hideComponent($cxtMenu);
	        cy.removeScratch('cxtMenuPosition');
	        cy.removeScratch('currentCyEvent');
	      });
	    }
	
	    function performBindings($component, onClickFcn, selector, coreAsWell) {
	      bindOnClickFunction($component, onClickFcn);
	      bindCyCxttap($component, selector, coreAsWell);
	    }
	
	    // Adjusts context menu if necessary
	    function adjustCxtMenu(event) {
	      var currentCxtMenuPosition = cy.scratch('cxtMenuPosition');
	
	      if( currentCxtMenuPosition != event.cyPosition ) {
	        hideMenuItemComponents();
	        cy.scratch('cxtMenuPosition', event.cyPosition);
	
	        var containerPos = $(cy.container()).position();
	
	        var left = containerPos.left + event.cyRenderedPosition.x;
	        var top = containerPos.top + event.cyRenderedPosition.y;
	
	        displayComponent($cxtMenu);
	        $cxtMenu.css('left', left);
	        $cxtMenu.css('top', top);
	      }
	    }
	
	    function createAndAppendMenuItemComponents(menuItems) {
	      for (var i = 0; i < menuItems.length; i++) {
	        createAndAppendMenuItemComponent(menuItems[i]);
	      }
	    }
	
	    function createAndAppendMenuItemComponent(menuItem) {
	      // Create and append menu item
	      var $menuItemComponent = createMenuItemComponent(menuItem);
	      appendComponentToCxtMenu($menuItemComponent);
	
	      performBindings($menuItemComponent, menuItem.onClickFunction, menuItem.selector, menuItem.coreAsWell);
	    }//insertComponentBeforeExistingItem(component, existingItemID)
	
	    function createAndInsertMenuItemComponentBeforeExistingComponent(menuItem, existingComponentID) {
	      // Create and insert menu item
	      var $menuItemComponent = createMenuItemComponent(menuItem);
	      insertComponentBeforeExistingItem($menuItemComponent, existingComponentID);
	
	      performBindings($menuItemComponent, menuItem.onClickFunction, menuItem.selector, menuItem.coreAsWell);
	    }
	
	    // create cxtMenu and append it to body
	    function createAndAppendCxtMenuComponent() {
	      var classes = getClassStr(options.contextMenuClasses);
	      $cxtMenu = $('<div id="cy-context-menus-cxt-menu" class=' + classes + '></div>');
	      $('body').append($cxtMenu);
	
	      return $cxtMenu;
	    }
	
	    // Creates a menu item as an html component
	    function createMenuItemComponent(item) {
	      var classStr = getMenuItemClassStr(options.menuItemClasses, item.hasTrailingDivider);
	      var itemStr = '<button id="' + item.id + '" title="' + item.title + '" class="' + classStr + '"';
	
	      if(item.disabled) {
	        itemStr += ' disabled';
	      }
	
	      itemStr += '></button>';
	      var $menuItemComponent = $(itemStr);
	
	      $menuItemComponent.data('selector', item.selector);
	      $menuItemComponent.data('on-click-function', item.onClickFunction);
	
	      return $menuItemComponent;
	    }
	
	    // Appends the given component to cxtMenu
	    function appendComponentToCxtMenu(component) {
	      $cxtMenu.append(component);
	      bindMenuItemClickFunction(component);
	    }
	
	    // Insert the given component to cxtMenu just before the existing item with given ID
	    function insertComponentBeforeExistingItem(component, existingItemID) {
	      var $existingItem = $('#' + existingItemID);
	      component.insertBefore($existingItem);
	    }
	
	    function destroyCxtMenu() {
	      if(!active) {
	        return;
	      }
	
	      removeAndUnbindMenuItems();
	
	      cy.off('tapstart', eventCyTapStart);
	
	      $cxtMenu.remove();
	      $cxtMenu = undefined;
	      active = false;
	    }
	
	    function removeAndUnbindMenuItems() {
	      var children = $cxtMenu.children();
	
	      $(children).each(function() {
	        removeAndUnbindMenuItem($(this));
	      });
	    }
	
	    function removeAndUnbindMenuItem(itemID) {
	      var $component = typeof itemID === 'string' ? $('#' + itemID) : itemID;
	      var cxtfcn = $component.data('cy-context-menus-cxtfcn');
	      var selector = $component.data('selector');
	      var callOnClickFcn = $component.data('call-on-click-function');
	      var cxtCoreFcn = $component.data('cy-context-menus-cxtcorefcn');
	
	      if(cxtfcn) {
	        cy.off('cxttap', selector, cxtfcn);
	        cy.off('taphold', selector, cxtfcn);
	      }
	
	      if(cxtCoreFcn) {
	        cy.off('cxttap', cxtCoreFcn);
	        cy.off('taphold', cxtCoreFcn);
	      }
	
	      if(callOnClickFcn) {
	        $component.off('click', callOnClickFcn);
	      }
	
	      $component.remove();
	    }
	
	    function moveBeforeOtherMenuItemComponent(componentID, existingComponentID) {
	      if( componentID === existingComponentID ) {
	        return;
	      }
	
	      var $component = $('#' + componentID).detach();
	      var $existingComponent = $('#' + existingComponentID);
	
	      $component.insertBefore($existingComponent);
	    }
	
	    function bindMenuItemClickFunction(component) {
	      component.click( function() {
	          hideComponent($cxtMenu);
	          cy.removeScratch('cxtMenuPosition');
	      });
	    }
	
	    function disableComponent(componentID) {
	      $('#' + componentID).attr('disabled', true);
	    }
	
	    function enableComponent(componentID) {
	      $('#' + componentID).attr('disabled', false);
	    }
	
	    function setTrailingDivider(componentID, status) {
	      var $component = $('#' + componentID);
	      if(status) {
	        $component.addClass(dividerCSSClass);
	      }
	      else {
	        $component.removeClass(dividerCSSClass);
	      }
	    }
	
	    // Get an extension instance to enable users to access extension methods
	    function getInstance(cy) {
	      var instance = {
	        // Returns whether the extension is active
	       isActive: function() {
	         return active;
	       },
	       // Appends given menu item to the menu items list.
	       appendMenuItem: function(item) {
	         createAndAppendMenuItemComponent(item);
	         return cy;
	       },
	       // Appends menu items in the given list to the menu items list.
	       appendMenuItems: function(items) {
	         createAndAppendMenuItemComponents(items);
	         return cy;
	       },
	       // Removes the menu item with given ID.
	       removeMenuItem: function(itemID) {
	         removeAndUnbindMenuItem(itemID);
	         return cy;
	       },
	       // Sets whether the menuItem with given ID will have a following divider.
	       setTrailingDivider: function(itemID, status) {
	         setTrailingDivider(itemID, status);
	         return cy;
	       },
	       // Inserts given item before the existingitem.
	       insertBeforeMenuItem: function(item, existingItemID) {
	         createAndInsertMenuItemComponentBeforeExistingComponent(item, existingItemID);
	         return cy;
	       },
	       // Moves the item with given ID before the existingitem.
	       moveBeforeOtherMenuItem: function(itemID, existingItemID) {
	         moveBeforeOtherMenuItemComponent(itemID, existingItemID);
	         return cy;
	       },
	       // Disables the menu item with given ID.
	       disableMenuItem: function(itemID) {
	         disableComponent(itemID);
	         return cy;
	       },
	       // Enables the menu item with given ID.
	       enableMenuItem: function(itemID) {
	         enableComponent(itemID);
	         return cy;
	       },
	       // Destroys the extension instance
	       destroy: function() {
	         destroyCxtMenu();
	         return cy;
	       }
	      };
	
	      return instance;
	    }
	
	    // To initialize with options.
	    cytoscape('core', 'contextMenus', function (opts) {
	      cy = this;
	
	      if ( opts !== 'get' ) {
	        // merge the options with default ones
	        options = extend(defaults, opts);
	
	        // Clear old context menu if needed
	        if(active) {
	          destroyCxtMenu();
	        }
	
	        active = true;
	
	        $cxtMenu = createAndAppendCxtMenuComponent();
	
	        var menuItems = options.menuItems;
	        createAndAppendMenuItemComponents(menuItems);
	
	        bindCyEvents();
	        preventDefaultContextTap();
	      }
	
	      return getInstance(this);
	    });
	  };
	
	  if( typeof module !== 'undefined' && module.exports ){ // expose as a commonjs module
	    module.exports = register;
	  }
	
	  if( typeof define !== 'undefined' && define.amd ){ // expose as an amd/requirejs module
	    define('cytoscape-context-menus', function(){
	      return register;
	    });
	  }
	
	  if( typeof cytoscape !== 'undefined' && $ ){ // expose to global cytoscape (i.e. window.cytoscape)
	    register( cytoscape, $ );
	  }
	
	})();

	//panzoom 
      ;(function(){ 'use strict';

        // registers the extension on a cytoscape lib ref
        var register = function( cytoscape, $ ){
          if( !cytoscape ){ return; } // can't register if cytoscape unspecified

          $.fn.cyPanzoom = $.fn.cytoscapePanzoom = function( options ){
            panzoom.apply( this, [ options, $ ] );

            return this; // chainability
          };

          // if you want a core extension
          cytoscape('core', 'panzoom', function( options ){ // could use options object, but args are up to you
            var cy = this;

            panzoom.apply( cy.container(), [ options, $ ] );

            return this; // chainability
          });

        };

        var defaults = {
          zoomFactor: 0.05, // zoom factor per zoom tick
          zoomDelay: 45, // how many ms between zoom ticks
          minZoom: 0.1, // min zoom level
          maxZoom: 10, // max zoom level
          fitPadding: 50, // padding when fitting
          panSpeed: 10, // how many ms in between pan ticks
          panDistance: 10, // max pan distance per tick
          panDragAreaSize: 75, // the length of the pan drag box in which the vector for panning is calculated (bigger = finer control of pan speed and direction)
          panMinPercentSpeed: 0.25, // the slowest speed we can pan by (as a percent of panSpeed)
          panInactiveArea: 8, // radius of inactive area in pan drag box
          panIndicatorMinOpacity: 0.5, // min opacity of pan indicator (the draggable nib); scales from this to 1.0
          zoomOnly: false, // a minimal version of the ui only with zooming (useful on systems with bad mousewheel resolution)
          fitSelector: undefined, // selector of elements to fit
          animateOnFit: function(){ // whether to animate on fit
            return false;
          },
          fitAnimationDuration: 1000, // duration of animation on fit

          // icon class names
          sliderHandleIcon: 'fa fa-minus',
          zoomInIcon: 'fa fa-plus',
          zoomOutIcon: 'fa fa-minus',
          resetIcon: 'fa fa-expand'
        };

        var panzoom = function( params, $ ){
          var options = $.extend(true, {}, defaults, params);
          var fn = params;

          var functions = {
            destroy: function(){
              var $this = $(this);
              var $pz = $this.find(".cy-panzoom");

              $pz.data('winbdgs').forEach(function( l ){
                $(window).unbind( l.evt, l.fn );
              });

              $pz.data('cybdgs').forEach(function( l ){
                $this.cytoscape('get').off( l.evt, l.fn );
              });

              $pz.remove();
            },

            init: function(){
              var browserIsMobile = 'ontouchstart' in window;

              return $(this).each(function(){
                var $container = $(this);

                var winbdgs = [];
                var $win = $(window);

                var windowBind = function( evt, fn ){
                  winbdgs.push({ evt: evt, fn: fn });

                  $win.bind( evt, fn );
                };

                var windowUnbind = function( evt, fn ){
                  for( var i = 0; i < winbdgs.length; i++ ){
                    var l = winbdgs[i];

                    if( l.evt === evt && l.fn === fn ){
                      winbdgs.splice( i, 1 );
                      break;
                    }
                  }

                  $win.unbind( evt, fn );
                };

                var cybdgs = [];
                var cy = $container.cytoscape('get');

                var cyOn = function( evt, fn ){
                  cybdgs.push({ evt: evt, fn: fn });

                  cy.on( evt, fn );
                };

                var cyOff = function( evt, fn ){
                  for( var i = 0; i < cybdgs.length; i++ ){
                    var l = cybdgs[i];

                    if( l.evt === evt && l.fn === fn ){
                      cybdgs.splice( i, 1 );
                      break;
                    }
                  }

                  cy.off( evt, fn );
                };

                var $panzoom = $('<div class="cy-panzoom"></div>');
                $container.prepend( $panzoom );

                $panzoom.css('position', 'absolute'); // must be absolute regardless of stylesheet

                $panzoom.data('winbdgs', winbdgs);
                $panzoom.data('cybdgs', cybdgs);

                if( options.zoomOnly ){
                  $panzoom.addClass("cy-panzoom-zoom-only");
                }

                // add base html elements
                /////////////////////////

                var $zoomIn = $('<div class="cy-panzoom-zoom-in cy-panzoom-zoom-button"><span class="icon '+ options.zoomInIcon +'"></span></div>');
                $panzoom.append( $zoomIn );

                var $zoomOut = $('<div class="cy-panzoom-zoom-out cy-panzoom-zoom-button"><span class="icon ' + options.zoomOutIcon + '"></span></div>');
                $panzoom.append( $zoomOut );

                var $reset = $('<div class="cy-panzoom-reset cy-panzoom-zoom-button"><span class="icon ' + options.resetIcon + '"></span></div>');
                $panzoom.append( $reset );

                var $slider = $('<div class="cy-panzoom-slider"></div>');
                $panzoom.append( $slider );

                $slider.append('<div class="cy-panzoom-slider-background"></div>');

                var $sliderHandle = $('<div class="cy-panzoom-slider-handle"><span class="icon ' + options.sliderHandleIcon + '"></span></div>');
                $slider.append( $sliderHandle );

                var $noZoomTick = $('<div class="cy-panzoom-no-zoom-tick"></div>');
                $slider.append( $noZoomTick );

                var $panner = $('<div class="cy-panzoom-panner"></div>');
                $panzoom.append( $panner );

                var $pHandle = $('<div class="cy-panzoom-panner-handle"></div>');
                $panner.append( $pHandle );

                var $pUp = $('<div class="cy-panzoom-pan-up cy-panzoom-pan-button"></div>');
                var $pDown = $('<div class="cy-panzoom-pan-down cy-panzoom-pan-button"></div>');
                var $pLeft = $('<div class="cy-panzoom-pan-left cy-panzoom-pan-button"></div>');
                var $pRight = $('<div class="cy-panzoom-pan-right cy-panzoom-pan-button"></div>');
                $panner.append( $pUp ).append( $pDown ).append( $pLeft ).append( $pRight );

                var $pIndicator = $('<div class="cy-panzoom-pan-indicator"></div>');
                $panner.append( $pIndicator );

                // functions for calculating panning
                ////////////////////////////////////

                function handle2pan(e){
                  var v = {
                    x: e.originalEvent.pageX - $panner.offset().left - $panner.width()/2,
                    y: e.originalEvent.pageY - $panner.offset().top - $panner.height()/2
                  }

                  var r = options.panDragAreaSize;
                  var d = Math.sqrt( v.x*v.x + v.y*v.y );
                  var percent = Math.min( d/r, 1 );

                  if( d < options.panInactiveArea ){
                    return {
                      x: NaN,
                      y: NaN
                    };
                  }

                  v = {
                    x: v.x/d,
                    y: v.y/d
                  };

                  percent = Math.max( options.panMinPercentSpeed, percent );

                  var vnorm = {
                    x: -1 * v.x * (percent * options.panDistance),
                    y: -1 * v.y * (percent * options.panDistance)
                  };

                  return vnorm;
                }

                function donePanning(){
                  clearInterval(panInterval);
                  windowUnbind("mousemove", handler);

                  $pIndicator.hide();
                }

                function positionIndicator(pan){
                  var v = pan;
                  var d = Math.sqrt( v.x*v.x + v.y*v.y );
                  var vnorm = {
                    x: -1 * v.x/d,
                    y: -1 * v.y/d
                  };

                  var w = $panner.width();
                  var h = $panner.height();
                  var percent = d/options.panDistance;
                  var opacity = Math.max( options.panIndicatorMinOpacity, percent );
                  var color = 255 - Math.round( opacity * 255 );

                  $pIndicator.show().css({
                    left: w/2 * vnorm.x + w/2,
                    top: h/2 * vnorm.y + h/2,
                    background: "rgb(" + color + ", " + color + ", " + color + ")"
                  });
                }

                function calculateZoomCenterPoint(){
                  var cy = $container.cytoscape("get");
                  var pan = cy.pan();
                  var zoom = cy.zoom();

                  zx = $container.width()/2;
                  zy = $container.height()/2;
                }

                var zooming = false;
                function startZooming(){
                  zooming = true;

                  calculateZoomCenterPoint();
                }


                function endZooming(){
                  zooming = false;
                }

                var zx, zy;
                function zoomTo(level){
                  var cy = $container.cytoscape("get");

                  if( !zooming ){ // for non-continuous zooming (e.g. click slider at pt)
                    calculateZoomCenterPoint();
                  }

                  cy.zoom({
                    level: level,
                    renderedPosition: { x: zx, y: zy }
                  });
                }

                var panInterval;

                var handler = function(e){
                  e.stopPropagation(); // don't trigger dragging of panzoom
                  e.preventDefault(); // don't cause text selection
                  clearInterval(panInterval);

                  var pan = handle2pan(e);

                  if( isNaN(pan.x) || isNaN(pan.y) ){
                    $pIndicator.hide();
                    return;
                  }

                  positionIndicator(pan);
                  panInterval = setInterval(function(){
                    $container.cytoscape("get").panBy(pan);
                  }, options.panSpeed);
                };

                $pHandle.bind("mousedown", function(e){
                  // handle click of icon
                  handler(e);

                  // update on mousemove
                  windowBind("mousemove", handler);
                });

                $pHandle.bind("mouseup", function(){
                  donePanning();
                });

                windowBind("mouseup blur", function(){
                  donePanning();
                });



                // set up slider behaviour
                //////////////////////////

                $slider.bind('mousedown', function(){
                  return false; // so we don't pan close to the slider handle
                });

                var sliderVal;
                var sliding = false;
                var sliderPadding = 2;

                function setSliderFromMouse(evt, handleOffset){
                  if( handleOffset === undefined ){
                    handleOffset = 0;
                  }

                  var padding = sliderPadding;
                  var min = 0 + padding;
                  var max = $slider.height() - $sliderHandle.height() - 2*padding;
                  var top = evt.pageY - $slider.offset().top - handleOffset;

                  // constrain to slider bounds
                  if( top < min ){ top = min }
                  if( top > max ){ top = max }

                  var percent = 1 - (top - min) / ( max - min );

                  // move the handle
                  $sliderHandle.css('top', top);

                  var zmin = options.minZoom;
                  var zmax = options.maxZoom;

                  // assume (zoom = zmax ^ p) where p ranges on (x, 1) with x negative
                  var x = Math.log(zmin) / Math.log(zmax);
                  var p = (1 - x)*percent + x;

                  // change the zoom level
                  var z = Math.pow( zmax, p );

                  // bound the zoom value in case of floating pt rounding error
                  if( z < zmin ){
                    z = zmin;
                  } else if( z > zmax ){
                    z = zmax;
                  }

                  zoomTo( z );
                }

                var sliderMdownHandler, sliderMmoveHandler;
                $sliderHandle.bind('mousedown', sliderMdownHandler = function( mdEvt ){
                  var handleOffset = mdEvt.target === $sliderHandle[0] ? mdEvt.offsetY : 0;
                  sliding = true;

                  startZooming();
                  $sliderHandle.addClass("active");

                  var lastMove = 0;
                  windowBind('mousemove', sliderMmoveHandler = function( mmEvt ){
                    var now = +new Date;

                    // throttle the zooms every 10 ms so we don't call zoom too often and cause lag
                    if( now > lastMove + 10 ){
                      lastMove = now;
                    } else {
                      return false;
                    }

                    setSliderFromMouse(mmEvt, handleOffset);

                    return false;
                  });

                  // unbind when
                  windowBind('mouseup', function(){
                    windowUnbind('mousemove', sliderMmoveHandler);
                    sliding = false;

                    $sliderHandle.removeClass("active");
                    endZooming();
                  });

                  return false;
                });

                $slider.bind('mousedown', function(e){
                  if( e.target !== $sliderHandle[0] ){
                    sliderMdownHandler(e);
                    setSliderFromMouse(e);
                  }
                });

                function positionSliderFromZoom(){
                  var cy = $container.cytoscape("get");
                  var z = cy.zoom();
                  var zmin = options.minZoom;
                  var zmax = options.maxZoom;

                  // assume (zoom = zmax ^ p) where p ranges on (x, 1) with x negative
                  var x = Math.log(zmin) / Math.log(zmax);
                  var p = Math.log(z) / Math.log(zmax);
                  var percent = 1 - (p - x) / (1 - x); // the 1- bit at the front b/c up is in the -ve y direction

                  var min = sliderPadding;
                  var max = $slider.height() - $sliderHandle.height() - 2*sliderPadding;
                  var top = percent * ( max - min );

                  // constrain to slider bounds
                  if( top < min ){ top = min }
                  if( top > max ){ top = max }

                  // move the handle
                  $sliderHandle.css('top', top);
                }

                positionSliderFromZoom();

                cyOn('zoom', function(){
                  if( !sliding ){
                    positionSliderFromZoom();
                  }
                });

                // set the position of the zoom=1 tick
                (function(){
                  var z = 1;
                  var zmin = options.minZoom;
                  var zmax = options.maxZoom;

                  // assume (zoom = zmax ^ p) where p ranges on (x, 1) with x negative
                  var x = Math.log(zmin) / Math.log(zmax);
                  var p = Math.log(z) / Math.log(zmax);
                  var percent = 1 - (p - x) / (1 - x); // the 1- bit at the front b/c up is in the -ve y direction

                  if( percent > 1 || percent < 0 ){
                    $noZoomTick.hide();
                    return;
                  }

                  var min = sliderPadding;
                  var max = $slider.height() - $sliderHandle.height() - 2*sliderPadding;
                  var top = percent * ( max - min );

                  // constrain to slider bounds
                  if( top < min ){ top = min }
                  if( top > max ){ top = max }

                  $noZoomTick.css('top', top);
                })();

                // set up zoom in/out buttons
                /////////////////////////////

                function bindButton($button, factor){
                  var zoomInterval;

                  $button.bind("mousedown", function(e){
                    e.preventDefault();
                    e.stopPropagation();

                    if( e.button != 0 ){
                      return;
                    }

                    var cy = $container.cytoscape("get");
                    var doZoom = function(){
                      var zoom = cy.zoom();
                      var lvl = cy.zoom() * factor;

                      if( lvl < options.minZoom ){
                        lvl = options.minZoom;
                      }

                      if( lvl > options.maxZoom ){
                        lvl = options.maxZoom;
                      }

                      if( (lvl == options.maxZoom && zoom == options.maxZoom) ||
                        (lvl == options.minZoom && zoom == options.minZoom)
                      ){
                        return;
                      }

                      zoomTo(lvl);
                    };

                    startZooming();
                    doZoom();
                    zoomInterval = setInterval(doZoom, options.zoomDelay);

                    return false;
                  });

                  windowBind("mouseup blur", function(){
                    clearInterval(zoomInterval);
                    endZooming();
                  });
                }

                bindButton( $zoomIn, (1 + options.zoomFactor) );
                bindButton( $zoomOut, (1 - options.zoomFactor) );

                $reset.bind("mousedown", function(e){
                  if( e.button != 0 ){
                    return;
                  }

                  var cy = $container.cytoscape("get");
                  var elesToFit = options.fitSelector?cy.elements(options.fitSelector):cy.elements();

                  if( elesToFit.size() === 0 ){
                    cy.reset();
                  } else {
                    var animateOnFit = typeof options.animateOnFit === 'function' ? options.animateOnFit.call() : options.animateOnFit;
                    if(animateOnFit){
                      cy.animate({
                        fit: {
                          eles: elesToFit,
                          padding: options.fitPadding
                        }
                      }, {
                        duration: options.fitAnimationDuration
                      });
                    }
                    else{
                      cy.fit( elesToFit, options.fitPadding );
                    }

                  }

                  return false;
                });



              });
            }
          };

          if( functions[fn] ){
            return functions[fn].apply(this, Array.prototype.slice.call( arguments, 1 ));
          } else if( typeof fn == 'object' || !fn ) {
            return functions.init.apply( this, arguments );
          } else {
            $.error("No such function `"+ fn +"` for jquery.cytoscapePanzoom");
          }

          return $(this);
        };


        if( typeof module !== 'undefined' && module.exports ){ // expose as a commonjs module
          module.exports = register;
        }

        if( typeof define !== 'undefined' && define.amd ){ // expose as an amd/requirejs module
          define('cytoscape-panzoom', function(){
            return register;
          });
        }

        if( typeof cytoscape !== 'undefined' && typeof jQuery !== 'undefined' ){ // expose to global cytoscape (i.e. window.cytoscape)
          register( cytoscape, jQuery );
        }

      })();
	
</script>
</head>

<body>
<a name="top"></a><!--link for page top-->
<div id="contents">
	<header>
		<#include "../header.html">
		<#include "../nav.ftl">
		<#include "../errormessage.ftl">
	</header>
	<!-- side menu -->
	<div style="float:left;" class="menu">
	 	 <ul>
	 	 <br><font size="6" color="black"><center><U>GRAB Graph</U></center></font>
	 	 <span style="line-height:100%"></span><br>
		 <font size="5" color="black"><center>${ID}</center></font>
	 	 <li><a href="/GRABTree/${ID}">GRAB Tree</a></li>
	 	 <li><a href="/Structures/Glycans/${ID}">Return</a></li>
	 	 <span style="line-height:100%"><font size="1" color="black"><B>GRAB</B> allows users to easily understand the relationships of glycan structures in an informational graphic.</font></span><br>
	 	 <span style="line-height:100%"><font size="1" color="black"><B>GRAB Tree</B> displays multiple relationships in tree format.  Relationships such as motifs, superstructures, substructures, and topology will be displayed as they become available.</font></span><br>
	 	 <span style="line-height:100%"><font size="1" color="black"><B>GRAB Graph</B> displays the relationships in four directions: superstructures (Above), substructures (Below), subsumes (Left), and subsumed by (Right) with the selected glycan at the center.</font></span><br>
	 	 <span style="line-height:100%"><font size="1" color="black"><B>Mouse over function</B>: User can easily check the selected glycan’s structure and accession number.</font></span><br>
	 	 <span style="line-height:100%"><font size="1" color="black"><B>Right-click function</B>: A menu will display where Users can select to display the glycan’s entry page, to show a new GRAB page with the selected glycan, copy the glycan accession number, etc.</font></span><br>
	 	 </ul>
	</div>
	<div style="float:right;" class="blockb">
    <div id="cy"></div>
    </div>
	<footer id ="footer">
		<#include "../footer.html">
	</footer>
</div>
</body>
</html>
