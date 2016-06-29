'use strict';
$(function () { // wrapper function

  var app = {

    init: function () {
      this.cacheElements();
      this.bindEvents();
    },

    cacheElements: function () {
      this.$app = $('#confirmationForm');
      this.$originalTextarea = $('#sequence');
    },

    bindEvents: function () {
      this.$app.on('click', '.registriesInput_addBtn', this._onAdd.bind(this));
      this.$app.on('click', '.registriesInput_remove', this._onRemove.bind(this));
    },

    _onAdd: function () {
      var nextNum = this.getNextNum();
      var $el = $('<div class="registriesInput_group"></div>');
      var $textarea = this.$originalTextarea.clone();
      $textarea.attr('id', 'sequence-' + nextNum);
      $textarea.attr('name', 'sequence-' + nextNum);
      $textarea.addClass('registriesInput_cloned');
      $textarea.attr('data-index', nextNum);
      $el.append($textarea);
      var $remove = $('<span class="registriesInput_remove btn" data-target="' + nextNum + '">Remove</span>');
      $el.append($remove);
      this.$app.find('.registriesInput_add').before($el);
    },

    _onRemove: function (e) {
      var $clicked = $(e.currentTarget);
      var $target = $clicked.parent('.registriesInput_group');
      $target.remove();
    },

    getNextNum:function () {
      var currentNum = 1, id, num;
      this.$app.find('textare').each(function () {
        id = $(this).attr('id');
        num = $(this).data('index');
        if (num && parseInt(num, 10) > currentNum) {
          currentNum = parseInt(num, 10);
        }
      });
      return currentNum + 1;
    }

  }; //app END.

  app.init();

});
