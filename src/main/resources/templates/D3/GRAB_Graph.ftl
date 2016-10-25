<!DOCTYPE>
<meta charset="utf-8">
<html lang="ja">
<head>
	<title>GRAB Graph visualization</title>

<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1, maximum-scale=1">

<script src="http://code.jquery.com/jquery-2.0.3.min.js"></script>
<script src="http://cytoscape.github.io/cytoscape.js/api/cytoscape.js-latest/cytoscape.min.js"></script>

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
				.selector('node')
					.css({
						"shape": "rectangle",
						"width": "50px",
						"height": "20px",
						'background-color': '#fff',
						// "content": function(ele){
						//   if (ele.id === "subsumedby"|| ele.id ==="subsumed"|| ele.id ==="superstructure"|| ele.id ==="substructure") {
						//     return "ele.id";
						//   }
						//   else return null;
						// },
						//"content":"data(id)",
						 "font-size":"12px",
						 "text-valign":"center",
						 "text-halign":"center",
						 //"text-outline-color":"#000",
						 //"text-outline-width":"2px",
						"color":"#000",
						"overlay-padding":"6px",
						//"z-index":"20",
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
								//console.log("https://glytoucan.org/glycans/" + ele.id() + "/image?style=extended&format=png&notation=cfg");
								return "/glycans/" + ele.id() + "/image?style=extended&format=png&notation=cfg";
							}
						},
						"backgroud-width": "50px",
						"backgroud-height": "20px",
						"background-fit": "contain",
						"background-clip" : "none",
				})
				.selector('edge')
					.css({
						'curve-style': 'bezier',
						'width': 3,
						'line-color': '#000',
						//'opacity': 0.5,
						"target-arrow-shape" : "triangle",
					//   'target-arrow-shape': function(ele){
					//     //console.log(ele.id());
					//     if (ele.id() == "center_to_subsumedby"||ele.id() == "center_to_subsumes"||ele.id() == "center_to_superstructure"||ele.id() == "center_to_substructure"){
					//       return 'none';
					//     }
					//     else return 'triangle';
					// },
						'target-arrow-color': '#000',
					})
				.selector(":parent")
					.css({
						'background-opacity': 0.333,
						'background-color': function(ele){
							if (ele.id() == "subsumedby_box"){
								return "#0000FF";
							} else if (ele.id() == "subsumes_box"){
								return "#FF0000";
							} else if (ele.id() == "superstructure_box"){
								return "#008000";
							} else if (ele.id() == "substructure_box"){
								return "#800080";
							} else {
								return '#fff';
							}
						}
					})
				// .selector('node.highlight') //This class is change all nodes
				//   .style({
				//       'border-color': '#FFF',
				//       'border-width': '2px'
				//   })
					// .selector('selected')
					//   .style({})
					.selector('node.addLabel') //This class is change the selected node
						.style({
								//'content':"data(id)",
								//'opacity': '0.5'
								"label" : function(ele){
									//console.log(ele.id());
									if (ele.id() == "subsumedby"||ele.id() == "subsumes"||ele.id() == "superstructure"||ele.id() == "substructure"||ele.id() == "subsumedby_box"||ele.id() == "subsumes_box"||ele.id() == "superstructure_box"||ele.id() == "substructure_box"){
										return "";
									}
									else return ele.id("id");
								},
								// Text need to fit which the glycan concerned
								// How the way separate?
								// x > 100 , y >100
								"text-halign":"center",
								"text-valign":"bottom",
								"text-margin-x":"33px",
								"text-margin-y":"0px",
								// "text-background-color": function(ele){
								//   console.log(ele.parent());
								//   console.log(ele.id());
								//   if (ele.parent() == "subsumedby_box"){
								//     return "#0000FF";
								//   } else if (ele.parent() == "subsumes_box"){
								//     return "#FF0000";
								//   } else if (ele.parent() == "superstructure_box"){
								//     return "#008000";
								//   } else if (ele.parent() == "substructure_box"){
								//     return "#800080";
								//   } else {
								//     return '#fff';
								//   }
								// },
								//"text-background-shape":"rectangle",
								// "text-background-opacity":"1.0",
								// "text-halign" : function(ele){
								//   console.log(ele.position('x'));
								//   if (ele.position('x') == 0 && ele.position('y') == 0){
								//     return "center";
								//   }
								//   else return "right";
								// },
								// "text-valign" : function(ele){
								//   if (ele.position('x') == 0 && ele.position('y') == 0){
								//     return "bottom";
								//   }
								//   else return "center";
								// },
								// "text-margin-x" : function(ele){
								//   if (ele.position('x') == 0 && ele.position('y') == 0){
								//     return "33px";
								//   }
								//   else return "5px";
								// },
								// "text-margin-y" : function(ele){
								//   if (ele.position('x') == 0 && ele.position('y') == 0){
								//     return "0px";
								//   }
								//   else return "10px";
								// },
								"z-index":"20"
					})
				.selector('node.addImage')
					.style({
						"width" : function(ele){
							//console.log(ele.id());
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
								//console.log("/glycans/" + ele.id("id") + "/image?style=extended&format=png&notation=cfg");
								return "/glycans/" + ele.id("id") + "/image?style=extended&format=png&notation=cfg";
							}
						}
					})
					
		});
		
		var isUserGrabbingN1 = cy.nodes().ungrabify();
		
		cy.on('mouseover', 'node', function(e){
				var sel = e.cyTarget;
				//cy.elements().difference(sel.outgoers()).not(sel).addClass('highlight');
				sel.addClass('addLabel').addClass('addLabel');
				sel.addClass('addImage').addClass('addImage');
		});
		cy.on('mouseout', 'node', function(e){
				var sel = e.cyTarget;
				//cy.elements().removeClass('highlight');
				sel.removeClass('addLabel').removeClass('addLabel');
				sel.removeClass('addImage').removeClass('addImage');
		});

		// var selectAllOfTheSameType = function(ele) {
		//     cy.elements().unselect();
		//     if(ele.isNode()) {
		//         cy.nodes().select();
		//     }
		//     else if(ele.isEdge()) {
		//         cy.edges().select();
		//     }
		// };

		var copyTextToClipboard = function(txt){
				var copyArea = $("<textarea/>");
				copyArea.text(txt);
				$("body").append(copyArea);
				copyArea.select();
				document.execCommand("copy");
				copyArea.remove();
		}

		// demo your core ext
		//TODO exception of name of glycan concerned
		cy.contextMenus({
				menuItems: [
						{
								id: 'Copy this glycan ID',
								title: 'Copy this glycan ID',
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
										location.href = "/GRAB_Graph/" + sel;
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
	          if( event.cyTarget != cy ) {
	            return;
	          }
	
	          cy.scratch('currentCyEvent', event);
	          adjustCxtMenu(event);
	          displayComponent($component);
	        });
	      }
	
	      if(selector) {
	        cy.on('cxttap', selector, cxtfcn = function(event) {
	          cy.scratch('currentCyEvent', event);
	          adjustCxtMenu(event);
	          displayComponent($component);
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
	      }
	
	      if(cxtCoreFcn) {
	        cy.off('cxttap', cxtCoreFcn);
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
	
</script>
</head>

<body>
<a name="top"></a><!--link for page top-->
<div id="contents">
	<header>
		<#include "../header.html">
	</header>
	<#include "../nav.ftl">
	<#include "../errormessage.ftl">
    <div id="cy"></div>
	<footer id ="footer">
		<#include "../footer.html">
	</footer>
</div>
</body>
</html>
