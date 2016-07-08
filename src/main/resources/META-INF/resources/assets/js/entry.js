'use strict';
!(function () {

  var app = {
    init: function () {
      this.cacheElements();
      this.bindEvents();
    },

    cacheElements: function () {
      this.window = window;
      this.entryContent = document.querySelector('.entryNew_content');
      this.entryNav = document.querySelector('.entryNav');
    },

    bindEvents: function () {
      var _this = this;
      this.window.addEventListener('scroll', this.onScroll.bind(this), false);
      this.window.addEventListener('resize', this.onResize.bind(this), false);
      this.entryNav.addEventListener('click', this.onClickNav.bind(this), false);
    },

    onScroll: function () {
      var scroll = document.documentElement.scrollTop || document.body.scrollTop;
      var navOffset = this.calcOffset(this.entryContent);
      if ((navOffset.top - 50 - scroll) < 0 && !this.entryNav.classList.contains('entryNav--stuck')) {
        this.entryNav.classList.add('entryNav--stuck');
        this.entryNav.style.left = navOffset.left + 'px';
        return;
      }
      if ((navOffset.top - 50 - scroll) >= 0 && this.entryNav.classList.contains('entryNav--stuck')) {
        this.entryNav.classList.remove('entryNav--stuck');
        this.entryNav.style.left = '0';
      }
    },

    onResize: function () {
      var navOffset = this.calcOffset(this.entryContent);
      if (this.entryNav.classList.contains('entryNav--stuck')) {
        this.entryNav.style.left = navOffset.left + 'px';
      }
    },

    calcOffset: function (el) {
      var top = 0, left = 0;
      var element = el;
      while (element) {
        top += element.offsetTop || 0;
        left += element.offsetLeft || 0;
        element = element.offsetParent;
      }
      return {top: top, left: left};
    },

    onClickNav: function (e) {
      var target = e.target.getAttribute('href');
      if (target === null || target.slice(0, 1) !== '#') {
        return;
      }
      e.preventDefault();
      var el = this.entryContent.querySelector(target);
      if (el === null || el === undefined) {
        return;
      }
      var offset = parseInt($(el).offset().top, 10);
      $('html, body').animate({scrollTop: offset});
    }

  }; //appEND.

  app.init();

})();
