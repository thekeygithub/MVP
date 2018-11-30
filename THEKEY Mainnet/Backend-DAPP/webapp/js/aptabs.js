/*--------------------------------------------------------------------------*/
/*																			*/
/*  apTabs, version 1.1.0													*/
/*  (c) 2008 Armel Pingault													*/	
/*																			*/
/*  apTabs this site is licensed under a Creative Commons License.			*/
/*	For details, see http://creativecommons.org/licenses/by-sa/3.0/			*/
/*																			*/
/*--------------------------------------------------------------------------*/
 
var apTabs = {
	
	// Customization
	
	tagName: 'H2',		// Tag name for the tab, must be uppercase
	path: 'img/skin/ap/',	// Path to the images
	tabWidth: 130,		// Speed of the scroll
	contentPadding:10,	// Content padding
	scrollSpeed: 20,	// Speed of the scroll
	defaultWidth:600,	// Default width
	defaultHeight:400,	// Default height
	defaultTab:1,		// Default active tab: 1 = 1st tab, 2 = 2nd tab, ...
	
	// End of customization
	
	version: '1.1.0',
	requiredPrototype: '1.6.0',
	img: [],
	scrollTmp: 0,
	timeOut: 0,
	
	load: function() {
		
		apTabs.init();		

		$$('.apTabs').each(function(e) {
									
			e.style.width	= parseInt(e.style.width) - 2 || apTabs.defaultWidth;
			e.style.height	= parseInt(e.style.height) - 2 || apTabs.defaultHeight;
			
			var tabsWrapper = new Element('div', {
				'class': 'tabsWrapper',
				'style': 'background-image:url(' + apTabs.img['tabsBg'].src + ')'
			});
			e.insert(tabsWrapper);
	
			var imgPrev = new Element('img', {
				'class': 'scrollLeft',
				'src': apTabs.img['scrollLeftInactive'].src,
				'width': 18
			});
			imgPrev.onclick = function() {
				apTabs.scroll(e, 'left');
			};
			imgPrev.onmouseover = function() {
				if (imgPrev.src.include('scrollLeftActive')) {
					imgPrev.src = apTabs.img['scrollLeftOver'].src;
				}
			};
			imgPrev.onmouseout = function() {
				if (imgPrev.src.include('scrollLeftOver')) {
					imgPrev.src = apTabs.img['scrollLeftActive'].src;
				}
			};
			tabsWrapper.insert(imgPrev);
	
			var tabContentsOuter = new Element('div', {
				'class': 'tabContentsOuter',
				'style': 'width:' + (parseInt(e.style.width) - 36) + 'px;background-image:url(' + apTabs.img['line'].src + ')'
			});
			tabsWrapper.insert(tabContentsOuter);
	
			var tabContentsInner = new Element('div', {
				'class': 'tabContentsInner'
			});
			tabContentsOuter.insert(tabContentsInner);
	
			var imgNext = new Element('img', {
				'class': 'scrollRight',
				'src': apTabs.img['scrollRightInactive'].src,
				'width': 18
			});
			imgNext.onclick = function() {
				apTabs.scroll(e, 'right');
			};
			imgNext.onmouseover = function() {
				if (imgNext.src.include('scrollRightActive')) {
					imgNext.src = apTabs.img['scrollRightOver'].src;
				}
			};
			imgNext.onmouseout = function() {
				if (imgNext.src.include('scrollRightOver')) {
					imgNext.src = apTabs.img['scrollRightActive'].src;
				}
			};
			tabsWrapper.insert(imgNext);
	
			tabContentsInner.insert(new Element('ul', {
				'class': 'tabLabels'
			}));
			
			e.insert(new Element('div', {
				'class': 'tabsContent'
			}));
			
			e.childElements().each(function(elt) {
				if (elt.tagName == apTabs.tagName) {
					var content = elt.next().innerHTML;
					var contentType = 'html';
					if (elt.next().hasClassName('ajax')) contentType = 'ajax';
					else if (elt.next().hasClassName('iframe')) contentType = 'iframe';
					apTabs.add(e, {
						title: elt.innerHTML,
						type: contentType,
						content: content,
						close: (elt.className == 'noclose') ? false : true
					});
					elt.next().remove();
				}
			});	

			e.childElements().each(function(elt) {
				if (elt.tagName == apTabs.tagName) {
					elt.remove();
				}
			});
			
			apTabs.show(e, e.down('.tabLabels').childNodes[apTabs.defaultTab - 1]);
			apTabs.updateScrollButtons(e);			
		});
	},

	init: function() {
		var agt = navigator.userAgent.toLowerCase();
		apTabs.is_op  = (agt.indexOf("opera") != -1);
		apTabs.is_ie  = (agt.indexOf("msie") != -1) && document.all && !apTabs.is_op;
		apTabs.is_ie6 = (agt.indexOf("msie 6") != -1) && document.all && !apTabs.is_op;
		
		apTabs.checkRequiredPrototype(); // http://script.aculo.us/

		apTabs.preload();		
	},

	/* http://script.aculo.us/ */
	checkRequiredPrototype: function() {
		if ((typeof Prototype == 'undefined') || 
			(typeof Element == 'undefined') || 
			(typeof Element.Methods=='undefined') ||
			(apTabs.convertVersionString(Prototype.Version) <
			 apTabs.convertVersionString(apTabs.requiredPrototype)))
		throw("apTabs requires the Prototype JavaScript framework >= " + apTabs.requiredPrototype);
	},
	
	/* http://script.aculo.us/ */
	convertVersionString : function (versionString) {
		var r = versionString.split('.');
		return parseInt(r[0])*100000 + parseInt(r[1])*1000 + parseInt(r[2]);
	},
	
	preload: function() {		
		apTabs.img = new Array();		
		['tabsBg', 'line',
		 'scrollLeftActive','scrollLeftInactive','scrollLeftOver',
		 'scrollRightActive','scrollRightInactive','scrollRightOver',
		 'tabLeftActive','tabLeftInactive','tabLeftOver',
		 'tabRightActive','tabRightInactive','tabRightOver',
		 'closeActive','closeInactive','closeOver'].each(function(i) {
			apTabs.img[i] = new Image();
			apTabs.img[i].src = apTabs.path + i + '.gif';
		});
	},
	
	add: function(e, tab) {
		var newTab = false;
		if (typeof(e) == 'string') {
			e = $(e);
			newTab = true;			
		}

		var tabLabel = new Element('li', {
			'class': 'tabLabelActive',
			'style': 'width:' + apTabs.tabWidth + 'px'
		});
		tabLabel.onmouseover = function() {
			if (!tabLabel.firstChild.style.backgroundImage.include('tabLeftOver')) {
				tabLabel.firstChild.style.backgroundImage = 'url(' + apTabs.img['tabLeftActive'].src + ')';
				tabLabel.lastChild.style.backgroundImage = 'url(' + apTabs.img['tabRightActive'].src + ')';
			}
		};
		tabLabel.onmouseout = function() {
			if (!tabLabel.firstChild.style.backgroundImage.include('tabLeftOver')) {
				tabLabel.firstChild.style.backgroundImage = 'url(' + apTabs.img['tabLeftInactive'].src + ')';
				tabLabel.lastChild.style.backgroundImage = 'url(' + apTabs.img['tabRightInactive'].src + ')';
			}
		};
		
		var tabLabelA = new Element('div', {
			'class': 'tabLabelLeft'
		}).update(tab.title);
		tabLabelA.onclick = function() {
			apTabs.show(e, tabLabelA.parentNode);
		};		
		tabLabel.insert(tabLabelA);		

		var tabLabelClose = new Element('div', {
			'class': 'tabLabelRight',
			'style': 'background-image:url(' + apTabs.img['tabRightInactive'].src + ')'
		});
		tabLabel.insert(tabLabelClose);
		
		if (typeof(tab.close) == 'undefined' || tab.close == true) {
			var closeImg = new Element('img', {
				'src': apTabs.img['closeInactive'].src,
				'style': 'margin-top:6px'
			});
			closeImg.onclick = function() {
				apTabs.remove(e, closeImg.parentNode.parentNode);
			};
			closeImg.onmouseover = function() {
				closeImg.src = apTabs.img['closeOver'].src;
			};
			closeImg.onmouseout = function() {
				if (closeImg.parentNode.style.backgroundImage.include('tabRightOver')) {
					closeImg.src = apTabs.img['closeActive'].src;
				}
				else {
					closeImg.src = apTabs.img['closeInactive'].src;
				}
			};
			tabLabelClose.insert(closeImg);
		}

		e.down('.tabLabels').insert(tabLabel);
		
		tabLabelA.style.width = parseInt(tabLabel.getWidth() - 5 - 23) + 'px';

		apTabs.updateScrollButtons(e);

		var tabContent = new Element('div', {
			'class': 'tabContent',
			'style': 'height:' + (parseInt(e.style.height) - 29 - (apTabs.contentPadding * 2)) + 'px;padding:' + apTabs.contentPadding + 'px'
		});

		apTabs.hideAll(e);
		e.down('.tabsContent').appendChild(tabContent);
		
		if (tab.html) {
			tab.type = 'html';
			tab.content = tab.html;
		}
		else if (tab.ajax) {
			tab.type = 'ajax';
			tab.content = tab.ajax;
		}
		else if (tab.iframe) {
			tab.type = 'iframe';
			tab.content = tab.iframe;
		}
		
		if (tab.type == 'html') {
			tabContent.innerHTML = tab.content;
		} else if (tab.type == 'ajax') {
			new Ajax.Updater(tabContent, tab.content, {
				evalScripts: true,
				onLoading: function() {
					tabContent.innerHTML = 'Loading...';
				}
			});
		} else if (tab.type == 'iframe') {
			var iframeHeight = parseInt(e.style.height) - 35;
			var iframeWidth = (apTabs.is_ie6) ? parseInt(e.style.width) - 2 : (parseInt(e.style.width)-10);
			tabContent.setStyle({
			  padding: '0px',
			  height: eval(parseInt(tabContent.style.height) + apTabs.contentPadding * 2) + 'px'
			});
			tabContent.innerHTML = '<iframe width="' + iframeWidth + '" height="' + iframeHeight + '" margin="0" padding="0" frameborder="0" src="' + tab.content + '"></iframe>';
		}

		apTabs.setLabelInnerWidth(e);
		
		if (newTab) {
			apTabs.scroll(e, 'last');
			apTabs.show(e, apTabs.getNbTabs(e));
		}
		apTabs.updateScrollButtons(e);
	},
	
	remove: function(e, tab) {
		
		var id = tab.previousSiblings().length;

		// If closing current tab
		if (tab.className == 'tabLabelActive') {
			if (tab.next()) {
				apTabs.show(e, tab.next());
			} else if (tab.previous()) {
				apTabs.show(e, tab.previous());
			}
		}

		tab.remove();
		e.lastChild.childNodes[id].remove();
		
		apTabs.setLabelInnerWidth(e);
		apTabs.updateScrollButtons(e);
	},
	
	show: function(e, tab) {
		if (typeof(e) == 'string') e = $(e);
		if (e.className != 'apTabs') e = e.up('.apTabs');
		if (typeof(tab) != 'object') tab = e.down('.tabLabels').childElements()[tab - 1];
		apTabs.hideAll(e);
		tab.firstChild.style.backgroundImage = 'url(' + apTabs.img['tabLeftOver'].src + ')';
		tab.lastChild.style.backgroundImage = 'url(' + apTabs.img['tabRightOver'].src + ')';
		if (tab.lastChild.firstChild) {
			tab.lastChild.firstChild.src = apTabs.img['closeActive'].src;
		}
		e.lastChild.childNodes[tab.previousSiblings().length].show();
	},
	
	hideAll: function(e) {
		e.down('.tabLabels').childElements().each(function(tab) {
			tab.firstChild.style.backgroundImage = 'url(' + apTabs.img['tabLeftInactive'].src + ')';
			tab.lastChild.style.backgroundImage = 'url(' + apTabs.img['tabRightInactive'].src + ')';
		});

		e.down('.tabsContent').childElements().each(function(div) {
			div.hide();
		});
	},

	scroll: function(e, w) {
		var tabWidth = (w == 'last') ? apTabs.getNbTabs(e) * apTabs.getTabWidth(e) : apTabs.getTabWidth(e);
		if (apTabs.scrollTmp < tabWidth) {
			scrollSpeed = (tabWidth - apTabs.scrollTmp < apTabs.scrollSpeed) ? tabWidth - apTabs.scrollTmp : apTabs.scrollSpeed ;
			if (w == 'left') scrollSpeed = -scrollSpeed;
			e.down('.tabContentsOuter').scrollLeft = e.down('.tabContentsOuter').scrollLeft + scrollSpeed;
			apTabs.scrollTmp += apTabs.scrollSpeed;
			apTabs.timeOut = setTimeout(function(){apTabs.scroll(e, w)}, 10);
		} else {
			apTabs.scrollTmp = 0;
			clearTimeout(apTabs.timeOut);
			apTabs.updateScrollButtons(e);
		}
	},
	
	getTabWidth: function(e) {
		return (e.down('.tabLabels').firstChild) ? e.down('.tabLabels').firstChild.getWidth() : 0 ;
	},
	
	setLabelInnerWidth: function(e) {
		e.down('.tabContentsInner').style.width = (apTabs.getNbTabs(e) * apTabs.getTabWidth(e)) + 'px';
	},
	
	getNbTabs: function(e) {
		return e.down('.tabLabels').childNodes.length;
	},
	
	updateScrollButtons: function(e) {
		
		var leftScroll	= e.down('.scrollLeft');
		var rightScroll	= e.down('.scrollRight');

		if (e.down('.tabContentsOuter').style.width < e.down('.tabContentsInner').style.width) {
			if (e.down('.tabContentsOuter').scrollLeft == 0) {
				leftScroll.src = apTabs.img['scrollLeftInactive'].src;
				rightScroll.src = apTabs.img['scrollRightActive'].src;
			}
			else if (parseInt(e.down('.tabContentsOuter').style.width) + 
					 parseInt(e.down('.tabContentsOuter').scrollLeft) ==
					 parseInt(e.down('.tabContentsInner').style.width)) {
				if (!leftScroll.src.include('scrollLeftOver')) {
					leftScroll.src = apTabs.img['scrollLeftActive'].src;
				}
				rightScroll.src = apTabs.img['scrollRightInactive'].src;
			}
			else {
				if (!leftScroll.src.include('scrollLeftOver')) {
					leftScroll.src = apTabs.img['scrollLeftActive'].src;
				}
				if (!rightScroll.src.include('scrollRightOver')) {
					rightScroll.src = apTabs.img['scrollRightActive'].src;
				}
			}
		} else {
			leftScroll.src = apTabs.img['scrollLeftInactive'].src;
			rightScroll.src = apTabs.img['scrollRightInactive'].src;
		}
	}
};

Event.observe(window, 'load', function() {
    try{
	  adjustPosition();
	}catch(e){
	  alert(e.message);
	}
	apTabs.load();
});