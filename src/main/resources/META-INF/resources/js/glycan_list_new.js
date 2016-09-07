'use strict';
$(function () { // wrapper function

  var ITEM_PER_PAGE = 20;

  var KEY_DOWN = 40;
  var KEY_UP = 38;
  var KEY_ENTER = 13;

  Number.isNaN = Number.isNaN || function (any) {
    return typeof any === 'number' && isNaN(any);
  }; //IE10 hack.

////////////////////////////////////////

//helper functions
  var util = {

    get_params: function () {
      var vars = {};
      var param = location.search.substring(1).split('&');
      var i, keySearch, key, val;
      for (i = 0; i < param.length; i += 1) {
        keySearch = param[i].search(/\=/);
        key = '';
        if (keySearch !== -1) {
          key = param[i].slice(0, keySearch);
        }
        val = param[i].slice(param[i].indexOf('=', 0) + 1);
        if (key !== '') {
          vars[key] = decodeURI(val);
        }
      }
      return vars;
    },

    get_id: function () {
      var params = location.href.split('/');
      return params.pop();
    },

    ajax_get: function (stanza_name, obj) {
      var defer = new $.Deferred();
      var stanza_url = STANZA_URL;
      var params = '';
      var key = '';
      for (key in obj) {
        params += ('&' + key + '=' + obj[key]);
      }
      $.ajax({
        type: 'GET',
        //url: stanza_url + stanza_name + "?" + params,
        url: '/connect',
        data: {
          url: stanza_url + stanza_name + "?" + params
        },
        cache: false,
        timeout: 60000,
        datatype: 'text'
      }).then(function (data) {
        defer.resolve(data);
      }, function () {
        defer.reject('Ajax request failed.');
      });
      return defer.promise();
    },

    set_pager: function (page, total) {
      if (total === 0) {
        return '';
      }
      var pager_tags = '';
      var per_page = ITEM_PER_PAGE;
      var last_page = parseInt(((total - 1) / per_page), 10) + 1;
      if (page > 1) {
        pager_tags += ('<li class="glResultPager_prev" data-gopage="' + (page - 1) + '">&lt;</li>');
      }
      if (page > 2) {
        pager_tags += ('<li class="glResultPager_num glResultPager_num" data-gopage="1">1</li>');
      }
      if (page > 3) {
        pager_tags += ('<li class="glResultPager_ellipsis">&hellip;</li>');
      }
      if (page > 1) {
        pager_tags += ('<li class="glResultPager_num glResultPager_num" data-gopage="' + (page - 1) + '">' + (page - 1) + '</li>');
      }
      pager_tags += ('<li class="glResultPager_num glResultPager_num--current">' + page + '</li>');
      if ((last_page - page) > 0) {
        pager_tags += ('<li class="glResultPager_num glResultPager_num" data-gopage="' + (page + 1) + '">' + (page + 1) + '</li>');
      }
      if ((last_page - page) > 2) {
        pager_tags += ('<li class="glResultPager_ellipsis">&hellip;</li>');
      }
      if ((last_page - page) > 1) {
        pager_tags += ('<li class="glResultPager_num glResultPager_num" data-gopage="' + last_page + '">' + last_page + '</li>');
      }
      if ((last_page - page) > 0) {
        pager_tags += ('<li class="glResultPager_next" data-gopage="' + (page + 1) + '">&gt;</li>');
      }
      return pager_tags;
    },

    modifyTimeStamp: function (time) {
      var time_nums = time.split(/\-|\:|\s|\./);
      var date = new Date(time_nums[0], parseInt(time_nums[1], 10) - 1, time_nums[2], time_nums[3], time_nums[4], time_nums[5]);
      return date.toUTCString();
    },

    getImg: function ($el, notation) {
      var $originalImg = $el.children('.js_zoomImg');
      var src = IMG_PATH + $originalImg.data('acc') + '/image?style=extended&format=png&notation=' + notation;
      $originalImg.attr('src', src);
      var img = $('<img>');
      img.attr('src', src);
      var $_this = $el;
      img.bind('error', function () {
        $_this.html('<span>no picture</span>').addClass('js_noImg_box');
      });
    },

    zoomImg: function (e) {
      var $target = $(e.currentTarget);
      if ($target.hasClass('js_noImg_box')) {
        return;
      }
      var box_size = $target.height();
      var img_size = $target.children('.js_zoomImg').width();
      var zoom_size = $target.children('.js_zoomImg').get(0).naturalWidth;
      if (parseInt(zoom_size, 10) > parseInt(img_size, 10)) {
        $target.children('.js_zoomImg').addClass('zoomImg--zoom');
        $target.height(box_size);
      }
    },

    zoomOutImg: function (e) {
      var $target = $(e.currentTarget);
      if ($target.hasClass('js_noImg_box')) {
        return;
      }
      $target.children('.js_zoomImg').removeClass('zoomImg--zoom');
    }

  }; //util END.

////////////////////////////////////////

//glycan list page
  var listApp = {

    init: function () {
      this.cacheElements();
      this.bindEvents();

      this.notation = this.$app.data('notation') || 'cfg';
      this.language = this.$app.data('lang') || '1';
      this.page = 1;
      this.count = 0;

      this.labels = {};
      this.labels.motif = this.$adoptedList.find('.adoptedSearch_label[data-category="motif"]').text();
      this.labels.monosaccharide = this.$adoptedList.find('.adoptedSearch_label[data-category="monosaccharide"]').text();

      this.motifList = {};
      this.monosaccharideList = {};
      this.databaseList = {};
      this.current_items = {motif: {}, monosaccharide: {}, database: {}};
      this.$listBox.draggable({handle: '.listBox_title, .listBox_tab'});

      this.mass_enable = false;
      this.mass_initValue = {min: -1, max: -1};
      this.mass_range = {min: -1, max: -1};
      this.$massRange.find('.massRange_slider').slider({disabled: true});

      this.prevWord = '';
      this.showSuggest = false;

      this.setFromParam();
    },

    cacheElements: function () {
      this.$app = $('#glycan_list_app');

      this.$searchInput = $('.incSearch');
      this.$suggestList = this.$app.find('.searchSuggest');
      this.$adoptedList = this.$app.find('.adoptedSearch');

      this.$showList = this.$app.find('.incSearch_showList');
      this.$listBox = this.$app.find('.listBox');

      this.$massRange = this.$app.find('.massRange');
      this.$linkedDb = this.$app.find('.linkedDb');

      this.$totalNum = this.$app.find('.glResultTotal_num');
      this.$viewSwitch = this.$app.find('.glResultSwitch');
      this.$sortKey = this.$app.find('.glResultSort_key');
      this.$sortDirec = this.$app.find('.glResultSort_order');
      this.$pager = this.$app.find('.glResultPage-ul');
      this.$clearAll = this.$app.find('.clearCondition_btn');

      this.$resultNothing = this.$app.find('.glSearchNothing');
      this.$resultList = this.$app.find('.glResult[data-mode="list"]');
      this.$resultWurcs = this.$app.find('.glResult[data-mode="wurcs"]');
      this.$resultGlycoct = this.$app.find('.glResult[data-mode="glycoct"]');

      this.$loading = this.$app.find('.loading_anim');

      this.$currentStatus = this.$app.find('.glCurrentStatus');
    },

    bindEvents: function () {
      this.$searchInput.on('keyup', '.incSearch_input', this.incSearch.bind(this));
      this.$searchInput.on('blur', '.incSearch_input', this.endSearch.bind(this));

      this.$suggestList.on('click', 'li.searchSuggest_list', this.selectCondition.bind(this));
      this.$suggestList.on('mouseenter', 'li.searchSuggest_list', this.hoverCondition.bind(this));
      this.$app.on('keydown', this.suggestKeypress.bind(this));
      this.$adoptedList.on('click', '.adoptedSearch_remove', this.removeCondition.bind(this));
      this.$adoptedList.on('change', '.msRange_num', this.inputMono.bind(this));

      this.$showList.on('click', this.showList.bind(this));
      this.$listBox.on('click', '.listBox_close', this.hideList.bind(this));
      this.$listBox.on('click', '.listBox_tabBtn', this.switchList.bind(this));
      this.$listBox.on('click', '.listBox_ul > li', this.selectFromList.bind(this));

      this.$massRange.on('change', '.massEnable_checkbox', this.toggleMass.bind(this));
      this.$massRange.on('change', '.massRange_num', this.inputMass.bind(this));

      this.$linkedDb.on('click', '.linkedDb_item_remove', this.removeDatabase.bind(this));

      this.$sortKey.on('change', this.doSort.bind(this));
      this.$sortDirec.on('change', this.doSort.bind(this));
      this.$viewSwitch.on('click', '.glResultSwitch_text', this.mainSwitch.bind(this));
      this.$pager.on('click', '.glResultPager_prev, .glResultPager_num, .glResultPager_next', this.goPage.bind(this));
      this.$clearAll.on('click', this.clearConditions.bind(this));

      this.$app.on('mouseenter', '.js_zoomImg_box', util.zoomImg.bind());
      this.$app.on('mouseleave', '.js_zoomImg_box', util.zoomOutImg.bind());
    },

    setFromParam: function () {
      var params = util.get_params();
      if (params.motif === undefined || params.motif === '') {
        this.getMain();
        return;
      }
      this.addCondition('motif', params.motif.replace(/\+/g, ' '));
    },

    getMain: function () {
      var _this = this;
      var offset = (this.page - 1) * 20;
      var monosaccharide_list = [], i, item, keys;
      keys = Object.keys(this.current_items.monosaccharide);
      for (i = 0; i < keys.length; i += 1) {
        item = this.current_items.monosaccharide[keys[i]];
        monosaccharide_list.push(item.name + '_Min_' + item.min + '_Max_' + item.max);
      }
      var obj = {
        massmin: this.mass_range.min,
        massmax: this.mass_range.max,
        motif: Object.keys(this.current_items.motif).join('__'),
        monosaccharide: monosaccharide_list.join('__'),
        database: Object.keys(this.current_items.database).join('__'),
        order: this.$sortDirec.val() || 'DESC',
        orderkey: this.$sortKey.val() || 'ContributionTime',
        offset: offset,
        lang: this.language
      };
      this.$loading.removeClass('loading_anim--hide');
      util.ajax_get('main_list', obj).then(function (data) {
        if (data === undefined || data === '') {
          _this.renderMain('', '', '', '');
          return;
        }
        var data_set = data.split('<hr />');
        _this.renderMain(data_set[3], data_set[0], data_set[1], data_set[2]);
        var range = data_set[4].split('--');
        var mass_res_min = parseInt(range[0], 10);
        var mass_res_max = parseInt((parseFloat(range[1], 10) + 1), 10);
        if (Number.isNaN(mass_res_min)) {
          mass_res_min = 0;
        }
        if (Number.isNaN(mass_res_max)) {
          mass_res_max = 0;
        }
        if (mass_res_min < 0) {
          mass_res_min = 0;
        }
        if (_this.mass_initValue.min === -1 && _this.mass_initValue.max === -1) {
          _this.mass_initValue.min = mass_res_min;
          _this.mass_initValue.max = mass_res_max;
        }
        _this.mass_range.current_min = mass_res_min;
        _this.mass_range.current_max = mass_res_max;
        _this.updateList(data_set[5], data_set[6], data_set[7]);
        _this.updateCurrent();
      }, function (err) {
        console.log(err);
      }).always(function () {
        _this.$loading.addClass('loading_anim--hide');
      });
    },

    renderMain: function (count_data, list_data, wurcs_data, structure_data) {
      var _this = this;
      var total = count_data === undefined || Number.isNaN(count_data) ? 0 : parseInt(count_data, 10);
      this.$totalNum.text(total);
      this.count = total;
      this.$pager.html(util.set_pager(parseInt(this.page, 10), total));
      this.$resultNothing.removeClass('glSearchNothing--show');
      if (list_data === '') {
        this.$resultNothing.addClass('glSearchNothing--show');
        this.$resultList.html('').append(_this.$resultNothing);
        this.$pager.html('');
      } else {
        var $list = $('<ol class="glResultList">' + list_data + '</ol>');
        $list.find('.js-listTime').each(function () {
          $(this).text(util.modifyTimeStamp($(this).text()));
        });
        var massVal;
        $list.find('.js-massValue').each(function () {
          massVal = $(this).text();
          if (massVal === 'NAN' || parseInt(massVal, 10) < 0) {
            $(this).text('');
          }
        });
        this.$resultList.html($list);
      }
      if (structure_data === '') {
        structure_data = '<tr><td colspan="2">' + _this.$resultNothing.text() + '</td></tr>';
      }
      this.$resultWurcs.children('tbody').html(wurcs_data);
      // this.$resultWurcs.find('code').each(function () {
      //   var wurcs_decoded = decodeURIComponent($(this).text());
      //   $(this).text(wurcs_decoded);
      // });
      if (wurcs_data === '') {
        wurcs_data = '<tr><td colspan="2">' + _this.$resultNothing.text() + '</td></tr>';
      }
      this.$resultGlycoct.children('tbody').html(structure_data);
      this.$app.find('.js_zoomImg_box').each(function () {
        util.getImg($(this), _this.notation);
      });

      if (this.mass_enable || Object.keys(this.current_items.motif).length > 0 || Object.keys(this.current_items.monosaccharide).length > 0 || Object.keys(this.current_items.database).length > 0) {
        this.$currentStatus.addClass('glCurrentStatus--show');
      } else {
        this.$currentStatus.removeClass('glCurrentStatus--show');
      }
    },

    updateList: function (motif_data, monosaccharide_data, db_data) {
      var _this = this;

      var name, num, max, min, current;
      var keys, i, item;
      this.$listBox.find('.listBox_listArea').find('ul').html('');

      //Motif
      keys = Object.keys(this.motifList);
      for (i = 0; i < keys.length; i += 1) {
        item = this.motifList[keys[i]];
        item.exist = false;
      }
      var $motif_list = $(motif_data);
      $motif_list.children('li').each(function () {
        name = $(this).children('.listBox_itemName').text();
        num = parseInt($(this).children('.listBox_itemNum').text().replace(/\(|\)/g, ''), 10);
        current = _this.current_items.motif[name] === undefined ? false : true;
        _this.motifList[name] = {name: name, num: num, current: current, exist: true};
        if (current) {
          $(this).addClass('listBox_name--disabled');
        }
      });
      this.$listBox.find('.listBox_ul-motif').html($motif_list.html());

      //Monosaccharide
      keys = Object.keys(this.monosaccharideList);
      for (i = 0; i < keys.length; i += 1) {
        item = this.monosaccharideList[keys[i]];
        item.exist = false;
      }
      var $mono_list = $(monosaccharide_data).addClass('listBox_ul-monosaccharide');
      $mono_list.children('li').each(function () {
        name = $(this).children('.listBox_itemName').text();
        num = parseInt($(this).children('.listBox_itemNum').text().replace(/\(|\)/g, ''), 10);
        current = _this.current_items.monosaccharide[name] === undefined ? false : true;
        max = parseInt($(this).data('max'), 10);
        min = parseInt($(this).data('min'), 10);
        if (_this.monosaccharideList[name] === undefined) {
          _this.monosaccharideList[name] = {name: name, num: num, max: max, min: min, current: current, exist: true};
        }
        _this.monosaccharideList[name].current = current;
        _this.monosaccharideList[name].num = num;
        _this.monosaccharideList[name].exist = true;
        if (current) {
          $(this).addClass('listBox_name--disabled');
          _this.current_items.monosaccharide[name].current_min = min;
          _this.current_items.monosaccharide[name].current_max = max;
        }
        $(this).data('min', min);
        $(this).data('max', max);
      });
      this.$listBox.find('.listBox_ul-monosaccharide').html($mono_list.html());

      //Database
      keys = Object.keys(this.databaseList);
      for (i = 0; i < keys.length; i += 1) {
        item = this.databaseList[keys[i]];
        item.exist = false;
      }
      var $database_list = $(db_data);
      $database_list.children('li').each(function () {
        name = $(this).children('.listBox_itemName').text();
        num = parseInt($(this).children('.listBox_itemNum').text().replace(/\(|\)/g, ''), 10);
        current = _this.current_items.database[name] === undefined ? false : true;
        _this.databaseList[name] = {name: name, num: num, current: current, exist: true};
        if (current) {
          $(this).addClass('listBox_name--disabled');
        }
      });
      this.$listBox.find('.listBox_ul-database').html($database_list.html());
    },

    mainSwitch: function (e) {
      var $clicked = $(e.target);
      if ($clicked.hasClass('glResultSwitch_text--current')) {
        return;
      }
      var target = $clicked.data('view');
      this.$resultList.removeClass('glResult--showing');
      this.$resultWurcs.removeClass('glResult--showing');
      this.$resultGlycoct.removeClass('glResult--showing');
      if (target === 'list') {
        this.$resultList.addClass('glResult--showing');
      } else if (target === 'wurcs') {
        this.$resultWurcs.addClass('glResult--showing');
      } else {
        this.$resultGlycoct.addClass('glResult--showing');
      }
      this.$viewSwitch.find('.glResultSwitch_text').removeClass('glResultSwitch_text--current');
      $clicked.addClass('glResultSwitch_text--current');
    },

    doSort: function () {
      this.page = 1;
      this.getMain();
    },

    goPage: function (e) {
      var $clicked = $(e.target);
      if ($clicked.hasClass('glResultPager_num--current')) {
        return;
      }
      this.page = parseInt($clicked.data('gopage'), 10);
      this.getMain();
    },

    clearConditions: function () {
      this.mass_range = {min: -1, max: -1};
      this.$massRange.find('.massEnable_checkbox').prop({'checked': false});
      this.mass_enable = false;
      this.toggleMass();
      this.current_items.motif = {};
      this.current_items.monosaccharide = {};
      this.current_items.database = {};
      this.$adoptedList.find('ul').remove();
      this.$adoptedList.find('.adoptedSearch_group').addClass('adoptedSearch_group--empty');
      this.page = 1;
      this.getMain();
    },

    showList: function (e) {
      var $clicked = $(e.currentTarget);
      var category = $clicked.data('category');
      this.$listBox.removeClass('listBox--hide');
      this.$listBox.find('.listBox_ul').removeClass('listBox_ul--show');
      this.$listBox.find('.listBox_ul-' + category).addClass('listBox_ul--show');
      this.$listBox.find('.listBox_tabBtn').each(function () {
        $(this).removeClass('listBox_tabBtn--current');
        if ($(this).data('category') === category) {
          $(this).addClass('listBox_tabBtn--current');
        }
      });
    },

    hideList: function () {
      this.$listBox.addClass('listBox--hide');
      this.$listBox.find('.listBox_ul').addClass('listBox_ul--show');
    },

    switchList: function (e) {
      var $clicked = $(e.currentTarget);
      if ($clicked.hasClass('listBox_tabBtn--current')) {
        return;
      }
      var category = $clicked.data('category');
      this.$listBox.find('.listBox_tabBtn').removeClass('listBox_tabBtn--current');
      this.$listBox.find('.listBox_ul').removeClass('listBox_ul--show');
      this.$listBox.find('.listBox_ul-' + category).addClass('listBox_ul--show');
      $clicked.addClass('listBox_tabBtn--current');
    },

    selectFromList: function (e) {
      var $clicked = $(e.currentTarget);
      if ($clicked.hasClass('listBox_name--disabled')) {
        return;
      }
      var category = $clicked.parent('ul.listBox_ul').data('category');
      var name = $clicked.children('.listBox_itemName').text();
      this.addCondition(category, name);
    },

    flushSuggest: function () {
      this.$suggestList.addClass('searchSuggest--blank').children('ul').html('');
      this.prev_word = '';
      this.showSuggest = false;
    },

    incSearchNotFound: function () {
      var $notFound = this.$searchInput.find('.incSearch_notfound');
      $notFound.addClass('incSearch_notfound--active');
      setTimeout(function () {
        $notFound.removeClass('incSearch_notfound--active');
      }, 3000);
      this.showSuggest = false;
    },

    incSearch: function () {
      var input = this.$searchInput.find('.incSearch_input').val();
      if (input === this.prevWord) {
        return;
      }
      if (input.length < 3) {
        this.flushSuggest();
        return;
      }
      this.prevWord = input;
      var data = [], keys, i, item, disabled;
      keys = Object.keys(this.motifList);
      for (i = 0; i < keys.length; i += 1) {
        item = this.motifList[keys[i]];
        if (item.exist && item.name.toLowerCase().indexOf(input.toLowerCase()) !== -1) {
          disabled = item.current ? '  searchSuggest_list--disabled' : '';
          data.push('<li class="searchSuggest_list' + disabled + '"><span class="searchSuggest_label searchSuggest_label--motif" data-category="motif">' + this.labels.motif + '</span>');
          data.push('<span class="searchSuggest_value">' + item.name + '</span>&nbsp;<span class="searchSuggest_num">' + item.num + '</span></li>');
        }
      }
      keys = Object.keys(this.monosaccharideList);
      for (i = 0; i < keys.length; i += 1) {
        item = this.monosaccharideList[keys[i]];
        if (item.exist && item.name.toLowerCase().indexOf(input.toLowerCase()) !== -1) {
          disabled = item.current ? ' searchSuggest_list--disabled' : '';
          data.push('<li class="searchSuggest_list' + disabled + '"><span class="searchSuggest_label searchSuggest_label--monosaccharide" data-category="monosaccharide">' + this.labels.monosaccharide + '</span>');
          data.push('<span class="searchSuggest_value" data-min="' + item.min + '" data-max="' + item.max + '">' + item.name + '</span>&nbsp;<span class="searchSuggest_num">' + item.num + '</span></li>');
        }
      }
      if (data.length > 0) {
        this.$suggestList.children('ul').html(data.join("\n"));
        this.$suggestList.children('ul').children('li:first-child').addClass('searchSuggest_list--current');
        this.$suggestList.removeClass('searchSuggest--blank');
        this.showSuggest = true;
      } else {
        this.$suggestList.children('ul').html('');
        this.$suggestList.addClass('searchSuggest--blank');
        this.incSearchNotFound();
      }
    },

    endSearch: function () {
      var _this = this;
      this.$searchInput.val('');
      setTimeout(function () {
        _this.flushSuggest();
      }, 500);
      this.showSuggest = false;
    },

    suggestKeypress: function (e) {
      if (!this.showSuggest) {
        return;
      }
      var $current, $target;
      $current = this.$suggestList.children('ul').children('li.searchSuggest_list--current');
      if (e.keyCode === KEY_ENTER) {
        e.preventDefault();
        $current.trigger('click');
      } else if (e.keyCode === KEY_UP) {
        e.preventDefault();
        $target = $current.prev('li.searchSuggest_list');
        if ($target.length > 0) {
          $current.removeClass('searchSuggest_list--current');
          $target.addClass('searchSuggest_list--current');
        }
      } else if (e.keyCode === KEY_DOWN) {
        e.preventDefault();
        $target = $current.next('li.searchSuggest_list');
        if ($target.length > 0) {
          $current.removeClass('searchSuggest_list--current');
          $target.addClass('searchSuggest_list--current');
        }
      }
    },

    hoverCondition: function (e) {
      var $hovered = $(e.currentTarget);
      this.$suggestList.children('ul').children('li.searchSuggest_list--current').removeClass('searchSuggest_list--current');
      $hovered.addClass('searchSuggest_list--current');
    },

    selectCondition: function (e) {
      var $clicked = $(e.currentTarget);
      if ($clicked.hasClass('searchSuggest_list--disabled')) {
        return;
      }
      var category = $clicked.children('span.searchSuggest_label').data('category');
      var name = $clicked.children('.searchSuggest_value').text();
      this.$searchInput.val('');
      this.addCondition(category, name);
    },

    addCondition: function (category, item_name) {
      var item_min = category === 'monosaccharide' ? this.monosaccharideList[item_name].min : '';
      var item_max = category === 'monosaccharide' ? this.monosaccharideList[item_name].max : '';
      this.current_items[category][item_name] = {name: item_name, min: item_min, max: item_max};
      this.flushSuggest();
      this.page = 1;
      if (category === 'motif') {
        this.addMotifView(item_name);
      } else if (category === 'monosaccharide') {
        this.addMonoView(item_name);
      } else if (category === 'database') {
        this.addDatabaseView(item_name);
      }
      this.getMain();
    },

    removeCondition: function (e) {
      var $clicked = $(e.target);
      var $target = $clicked.parent('li.adoptedSearch_item');
      var target_name = $target.children('span.adoptedSearch_itemName').text();
      if (this.current_items.motif[target_name] !== undefined) {
        delete this.current_items.motif[target_name];
        this.removeAdoptedItem(target_name, 'motif');
      } else if (this.current_items.monosaccharide[target_name] !== undefined) {
        delete this.current_items.monosaccharide[target_name];
        this.removeAdoptedItem(target_name, 'monosaccharide');
      }
      this.page = 1;
      this.getMain();
    },

    removeDatabase: function (e) {
      var $clicked = $(e.target);
      var $target = $clicked.parent('.linkedDb_item');
      var target_name = $target.children('span.linkedDb_item_name').text();
      if (this.current_items.database[target_name] === undefined) {
        return;
      }
      delete this.current_items.database[target_name];
      $target.remove();
      if (Object.keys(this.current_items.database).length === 0) {
        this.$linkedDb.find('.linkedDb_default').addClass('linkedDb_default--show');
      }
      this.page = 1;
      this.getMain();
    },

    addMotifView: function (name) {
      this.$adoptedList.removeClass('adoptedSearch--empty');
      var $target = this.$adoptedList.find('.adoptedSearch_group-motif');
      var item = this.current_items.motif[name];
      var $li = $('<li class="adoptedSearch_item" data-name="' + name + '"><span class="adoptedSearch_itemName">' + item.name + '</span><span class="adoptedSearch_range"></span><span class="adoptedSearch_remove">Remove</span></li>');
      if ($target.children('ul.adoptedSearch_ul').length > 0) {
        $target.children('ul.adoptedSearch_ul').append($li);
      } else {
        var $el = $('<ul class="adoptedSearch_ul"></ul>');
        $el.append($li);
        $target.append($el);
        $target.removeClass('adoptedSearch_group--empty');
      }
    },

    addMonoView: function (name) {
      var _this = this;
      this.$adoptedList.removeClass('adoptedSearch--empty');
      var $target = this.$adoptedList.find('.adoptedSearch_group-monosaccharide');
      var item = this.current_items.monosaccharide[name];
      var $li = $('<li class="adoptedSearch_item" data-name="' + name + '"><span class="adoptedSearch_itemName">' + item.name + '</span><span class="adoptedSearch_range"></span><span class="adoptedSearch_remove">Remove</span></li>');
      var $slider = $('<span class="msRange_slider"></span>');
      var $input_min = $('<input class="msRange_num range_min" type="text" size="5" value="' + item.min + '" />');
      var $input_max = $('<input class="msRange_num range_max" type="text" size="5" value="' + item.max + '" />');
      $slider.slider({
        range: true, min: item.min, max: item.max, values: [item.min, item.max],
        slide: function (event, ui) {
          $(this).parent('span.adoptedSearch_range').children('.range_min').val(ui.values[0]);
          $(this).parent('span.adoptedSearch_range').children('.range_max').val(ui.values[1]);
        },
        change: function (event, ui) {
          if (event.originalEvent) {
            var current_name = $(this).parent('span.adoptedSearch_range').prev('.adoptedSearch_itemName').text();
            _this.current_items.monosaccharide[current_name].min = ui.values[0];
            _this.current_items.monosaccharide[current_name].max = ui.values[1];
            _this.page = 1;
            _this.getMain();
          }
        }
      });
      $li.find('.adoptedSearch_range').append($slider).append($input_min).append('&nbsp;〜&nbsp;').append($input_max);
      if ($target.children('ul.adoptedSearch_ul').length > 0) {
        $target.children('ul.adoptedSearch_ul').append($li);
      } else {
        var $el = $('<ul class="adoptedSearch_ul"></ul>');
        $el.append($li);
        $target.append($el);
        $target.removeClass('adoptedSearch_group--empty');
      }
    },

    addDatabaseView: function (name) {
      var $target = this.$linkedDb.find('.linkedDb_items');
      var item = this.current_items.database[name];
      var $el = $('<div class="linkedDb_item" data-name="' + name + '"><span class="linkedDb_item_name">' + item.name + '</span><span class="linkedDb_item_remove">Remove</span>');
      if ($target.append($el));
      if ($target.find('.linkedDb_item').length > 0) {
        this.$linkedDb.find('.linkedDb_default').removeClass('linkedDb_default--show');
      } else {
        this.$linkedDb.find('.linkedDb_default').addClass('linkedDb_default--show');
      }
    },

    removeAdoptedItem: function (name, category) {
      var $target = this.$adoptedList.find('.adoptedSearch_group-' + category);
      $target.children('ul.adoptedSearch_ul').children('li[data-name="' + name + '"]').remove();
      if ($target.children('ul.adoptedSearch_ul').children('li').length < 1) {
        $target.children('ul.adoptedSearch_ul').remove();
        $target.addClass('adoptedSearch_group--empty');
      }
      if (Object.keys(this.current_items.motif).length === 0 && Object.keys(this.current_items.monosaccharide).length === 0) {
        this.$adoptedList.addClass('adoptedSearch--empty');
      }
    },

    inputMono: function (e) {
      var $item = $(e.currentTarget).parents('li.adoptedSearch_item');
      var $min = $item.find('.range_min');
      var $max = $item.find('.range_max');
      var item_name = $item.children('.adoptedSearch_itemName').text();
      var val = this.validateMinMax($min.val(), $max.val(), this.monosaccharideList[item_name]);
      this.current_items.monosaccharide[item_name].min = val.min;
      this.current_items.monosaccharide[item_name].max = val.max;
      $item.find('.msRange_slider').slider({values: [val.min, val.max]});
      $min.val(val.min);
      $max.val(val.max);
      this.page = 1;
      this.getMain();
    },

    toggleMass: function () {
      var _this = this;
      this.mass_enable = this.$massRange.find('.massEnable_checkbox').prop('checked');
      if (this.mass_enable) {
        this.$massRange.find('.massRange_num-min').prop({'disabled': false}).val(this.mass_initValue.min);
        this.$massRange.find('.massRange_num-max').prop({'disabled': false}).val(this.mass_initValue.max);
        this.$massRange.find('.massRange_slider').slider({
          range: true, min: _this.mass_initValue.min, max: _this.mass_initValue.max, values: [_this.mass_initValue.min, _this.mass_initValue.max], disabled: false,
          slide: function (event, ui) {
            _this.$massRange.find('.massRange_num-min').val(ui.values[0]);
            _this.$massRange.find('.massRange_num-max').val(ui.values[1]);
          },
          change: function (event, ui) {
            if (event.originalEvent) {
              _this.mass_range = {min: ui.values[0], max: ui.values[1]};
              _this.page = 1;
              _this.getMain();
            }
          }
        });
        this.mass_range = {min: this.mass_initValue.min, max: this.mass_initValue.max};
      } else {
        this.mass_range = {min: -1, max: -1};
        this.$massRange.find('.massRange_slider').slider({
          disabled: true, values: [_this.mass_initValue.min, _this.mass_initValue.max]
        });
        this.$massRange.find('.massRange_num').prop({'disabled': true}).val('');
      }
      this.page = 1;
      this.getMain();
    },

    inputMass: function () {
      var max = parseInt(this.$massRange.find('.massRange_num-max').val(), 10);
      var min = parseInt(this.$massRange.find('.massRange_num-min').val(), 10);
      var val = this.validateMinMax(min, max, this.mass_initValue);
      this.$massRange.find('.massRange_slider').slider({values: [val.min, val.max]});
      this.$massRange.find('.massRange_num-max').val(val.max);
      this.$massRange.find('.massRange_num-min').val(val.min);
      this.mass_range.min = val.min;
      this.mass_range.max = val.max;
      this.page = 1;
      this.getMain();
    },

    validateMinMax: function (min, max, limits) {
      min = parseInt(min, 10);
      max = parseInt(max, 10);
      if (max < min) {
        var tmp = min;
        min = max;
        max = tmp;
      }
      if (max > limits.max) {
        max = limits.max;
      }
      if (min < limits.min) {
        min = limits.min;
      }
      var val = {min: min, max: max};
      return val;
    },

    updateCurrent: function () {
      this.$currentStatus.find('.glCurrentStatus_detail').html('');
      var i, item, motif_names = [], mono_names = [], db_names = [];

      var keys = Object.keys(this.current_items.motif);
      for (i = 0; i < keys.length; i += 1) {
        item = this.current_items.motif[keys[i]];
        motif_names.push(item.name);
      }
      this.$currentStatus.find('.glCurrentStatus_category-motif').children('.glCurrentStatus_detail').text(motif_names.join(', '));

      keys = Object.keys(this.current_items.monosaccharide);
      for (i = 0; i < keys.length; i += 1) {
        item = this.current_items.monosaccharide[keys[i]];
        mono_names.push(item.name + '(' + item.current_min + '〜' + item.current_max + ')');
      }
      this.$currentStatus.find('.glCurrentStatus_category-monosaccharide').children('.glCurrentStatus_detail').text(mono_names.join(', '));

      var keys = Object.keys(this.current_items.database);
      for (i = 0; i < keys.length; i += 1) {
        item = this.current_items.database[keys[i]];
        db_names.push(item.name);
      }
      this.$currentStatus.find('.glCurrentStatus_category-database').children('.glCurrentStatus_detail').text(db_names.join(', '));

      var mass_text;
      if (this.mass_enable && this.count > 0) {
        mass_text = this.mass_range.current_min + '〜' + this.mass_range.current_max;
      } else {
        mass_text = '';
      }
      this.$currentStatus.find('.glCurrentStatus_category-mass').children('.glCurrentStatus_detail').text(mass_text);
    }

  }; //listApp END.

////////////////////////////////////////

//glycan entry page
  var entryApp = {

    init: function () {
      this.cacheElements();
      this.bindEvents();
      this.accNum = util.get_id();
      this.$title.text(this.accNum);
      this.notation = this.$app.data('notation') || 'cfg';
      this.language = this.$app.data('lang') || '1';
      this.getEntry();
      this.initData();
    },

    cacheElements: function () {
      this.$app = $('#glycan_entry_app');
      this.$title = this.$app.find('.glycan_entry_acc');
      this.$entryInfo = this.$app.find('.entryInfo');
      this.$infoNotfound = this.$app.find('.glycan_entry_notFound');
      this.$entryData = this.$app.find('.entryMain');
      this.$listView = this.$app.find('.cardView');
      this.$listStatus = this.$app.find('.entryMain_stat');
      this.$listNav = this.$app.find('.entryMain_menu');
      this.$listTitle = this.$app.find('.entryMain_title');
      this.$loadingEntry = this.$app.find('.js_loading_anim_entry');
      this.$loadingList = this.$app.find('.js_loading_anim_list');
    },

    bindEvents: function () {
      this.$app.on('click', '.js_list_structure_more', this.showListStructure.bind(this));
      this.$app.on('click', '.js_list_structure_hide', this.hideListStructure.bind(this));
      this.$app.on('click', '.infoStructure_switch_btn', this.switchStructure.bind(this));
      this.$app.on('mouseenter', '.js_list_img', util.zoomImg.bind());
      this.$app.on('mouseleave', '.js_list_img', util.zoomOutImg.bind());
      this.$listNav.on('click', 'li', this.changeList.bind(this));
    },

    getEntry: function () {
      var _this = this;
      var obj = {acc: this.accNum, lang: this.language};
      this.$loadingEntry.removeClass('entry_loading--hide');
      util.ajax_get('glycan_entry', obj).then(function (data) {
        if (data === '') {
          _this.$entryInfo.html('').append(_this.$infoNotfound);
          return;
        }
        var data_arr = data.split('<hr />');
        _this.$entryInfo.html(data_arr[0]);
        _this.renderEntry();
      }, function (err) {
        console.log(err);
      }).always(function () {
        _this.$loadingEntry.addClass('entry_loading--hide');
      });
    },

    renderEntry: function () {
      this.$entryInfo.find('.infoBox_accNum').text(this.accNum);
      var src = IMG_PATH + this.accNum + '/image?style=extended&format=png&notation=' + this.notation;
      this.$entryInfo.find('.entryInfo_img').attr('src', src);
      this.$entryInfo.find('.infoBox_time').text(util.modifyTimeStamp(this.$entryInfo.find('.infoBox_time').text()));
      var wurcs_code = this.$entryInfo.find('.infoStructure_code-wurcs').find('code').text();
      var wurcs_decoded = decodeURIComponent(wurcs_code);
      this.$entryInfo.find('.infoStructure_code-wurcs').find('code').text(wurcs_decoded);
      this.structureHeight = this.$app.find('.infoBox_3cols').innerHeight() - 50;
    },

    switchStructure: function (e) {
      var $clicked = $(e.target);
      if ($clicked.hasClass('.infoStructure_switch_btn--active')) {
        return;
      }
      var target_name = $clicked.data('format');
      var $target = $('.infoStructure_code-' + target_name);
      $('.infoStructure_code').addClass('infoStructure_code--hide');
      $target.removeClass('infoStructure_code--hide');
      $('.infoStructure_switch_btn').removeClass('infoStructure_switch_btn--active');
      $clicked.addClass('infoStructure_switch_btn--active');
    },

    showListStructure: function (e) {
      var $clicked = $(e.target);
      var $target = $clicked.parent('.flexCodeWrapper');
      $target.addClass('flexCodeWrapper--open');
    },

    hideListStructure: function (e) {
      var $clicked = $(e.target);
      var $target = $clicked.parent('.flexCodeWrapper');
      $target.removeClass('flexCodeWrapper--open');
    },

    initData: function () {
      var _this = this;
      this.$entryData.each(function () {
        _this.getData($(this));
      });
    },

    getData: function ($target) {
      var _this = this;
      var list = [];
      $target.find('.entryMain_menuText').each(function () {
        var category = $(this).data('category');
        var stanza = $(this).data('stanza');
        if (stanza !== undefined && category !== undefined) {
          list.push({category: category, acc: _this.accNum, lang: _this.language, stanza: stanza, $target: $target});
        }
      });
      $target.find(this.$loadingList).removeClass('entry_loading--hide');
      var i = 0;
      var prevValue = new $.Deferred().resolve(0);
      for (i = 0; i < list.length; i += 1) {
        prevValue = prevValue.then(function (next_i) {
          return _this.getHtml(list, next_i);
        });
      }
    },

    getHtml: function (list, i) {
      var _this = this;
      var dfd = new $.Deferred();
      var param = list[i];
      var obj = {category: param.category.toLowerCase(), acc: param.acc, lang: param.lang};
      var $tag;
      util.ajax_get(param.stanza, obj).then(function (data) {
        $tag = $('<div class="cardView_category" data-category="' + param.category + '">' + data + '</div>');
        param.$target.find('.cardView').append($tag);
        if (i === (list.length - 1)) {
          param.$target.find(_this.$loadingList).addClass('entry_loading--hide');
          _this.setCurrent(param.$target);
        }
        dfd.resolve(i + 1);
      });
      return dfd.promise();
    },

    setCurrent: function ($target) {
      var _this = this;
      var exist = false;
      var category, $category, count;
      $target.find('.cardView_category').each(function () {
        $category = $(this);
        category = $category.data('category');
        count = $category.find('li, td').length;
        if (count > 0) {
          if (!exist) {
            _this.renderSelected($target, category);
            _this.renderList($target);
            exist = true;
          }
        } else {
          $target.find('span.entryMain_menuText[data-category="' + category + '"]').parent('li').addClass('entryMain_menuList--disabled');
        }
      });
      if (!exist) {
        $target.find('.entryMain_header').addClass('entryMain_header--noData');
        $target.find('.cardView').append('<span class="cardView_noData">No data found.</span>');
      }
    },

    renderSelected: function ($target, category) {
      $target.find('span.entryMain_menuText[data-category="' + category + '"]').parent('li').addClass('entryMain_menuList--current');
      var $category = $target.find('.cardView_category[data-category="' + category + '"]');
      var count = $category.find('li, td').length;
      $target.find('.cardView_category').removeClass('cardView_category--show');
      $category.addClass('cardView_category--show');
      $target.find('.entryMain_title').text(category);
      if ($target.find('.entryMain_count').length > 0) {
        $target.find('.entryMain_count').text(count);
      }
    },

    renderList: function ($target) {
      var _this = this;
      $target.find('.js_zoomImg').each(function () {
        var src = IMG_PATH + $(this).data('acc') + '/image?style=extended&format=png&notation=' + _this.notation;
        $(this).attr('src', src);
      });
      $target.find('.cardView_disName').each(function () {
        var text = $(this).text();
        $(this).text(text.replace('http://www.ncbi.nlm.nih.gov/mesh?term=', ''));
      });
    },

    changeList: function (e) {
      var $clicked = $(e.currentTarget);
      if ($clicked.hasClass('entryMain_menuList--disabled') || $clicked.hasClass('entryMain_menuList--current')) {
        return;
      }
      if ($clicked.children('a').length > 0) {
        var path = $clicked.children('a').attr('href');
        var current = window.location.href;
        var host = current.replace(/\/Structures.*$/, '');
        window.location.href = host + path;
        return;
      }
      var category = $clicked.children('.entryMain_menuText').data('category');
      if (category === null || category === undefined) {
        return;
      }
      var $target = $clicked.parents('.entryMain');
      $target.find('.entryMain_menuList').removeClass('entryMain_menuList--current');
      $clicked.addClass('entryMain_menuList--current');
      this.renderSelected($target, category);
    }

  }; //entryApp END.

  ////////////////////////////////////////

  //substrucure list page
  var substructureApp = {

    init: function () {
      this.cacheElements();
      this.bindEvents();
      this.language = this.$app.data('lang') || '1';
      this.notation = this.$app.data('notation') || 'cfg';
      this.wurcs = this.$app.data('wurcs');
      this.sortDirec = 'ASC';
      this.getMain();
    },

    cacheElements: function () {
      this.$app = $('#substructure_list_app');
      this.$resultTable = this.$app.find('.subResultTable');
      this.$count = this.$app.find('.js_resultTotal_num');
      this.$pager = this.$app.find('.glResultPager');
      this.$sorter = this.$app.find('.subReultTable_sort');
      this.$resultNothing = this.$app.find('.glSearchNothing');
      this.$loading = this.$app.find('.js_loading_anim');
    },

    bindEvents: function () {
      this.$pager.on('click', '.glResultPager_prev, .glResultPager_num, .glResultPager_next', this.goPage.bind(this));
      this.$app.on('mouseenter', '.js_zoomImg_box', util.zoomImg.bind());
      this.$app.on('mouseleave', '.js_zoomImg_box', util.zoomOutImg.bind());
      this.$sorter.on('click', this.doSort.bind(this));
    },

    getMain: function (gopage) {
      var _this = this;
      var page = (typeof gopage === 'number') ? gopage : 1;
      var offset = (page - 1) * 20;
      var obj = {
        wurcs: this.wurcs,
        order: this.sortDirec,
        orderkey: 'AccessionNumber',
        offset: offset,
        lang: this.language
      };
      this.$loading.removeClass('loading_anim--hide');
      util.ajax_get('substructure_search', obj).then(function (data) {
        var data_arr = data.split('<hr />');
        if (data_arr[0].replace(/\n/g, '') === '') {
          _this.$resultNothing.addClass('glSearchNothing--show');
          _this.$resultTable.children('tbody').html('');
          _this.$resultTable.addClass('subResultTable--hide');
          _this.$pager.html('');
          return;
        }
        _this.$resultTable.children('tbody').html(data_arr[0]);
        var total = data_arr[1].replace(/^\s+/, '').split(/\s+/)[0];
        _this.$count.text(total);
        _this.$pager.html(util.set_pager(parseInt(page, 10), total));
        _this.setZoomImg();
      }, function (err) {
        console.log(err);
      }).always(function () {
        _this.$loading.addClass('loading_anim--hide');
      });
    },

    setZoomImg: function () {
      var _this = this;
      this.$app.find('.js_zoomImg_box').each(function () {
        var $originalImg = $(this).children('.js_zoomImg');
        var src = IMG_PATH + $originalImg.data('acc') + '/image?style=extended&format=png&notation=' + _this.notation;
        $originalImg.attr('src', src);
        var img = $('<img>');
        img.attr('src', src);
        var $_this = $(this);
        img.bind('error', function () {
          $_this.html('<span>no picture</span>').addClass('js_noImg_box');
        });
      });
    },

    doSort: function (e) {
      var $clicked = $(e.currentTarget);
      if ($clicked.hasClass('subReultTable_sort--current')) {
        return;
      }
      this.sortDirec = $clicked.data('sort');
      this.$sorter.removeClass('subReultTable_sort--current');
      $clicked.addClass('subReultTable_sort--current');
      this.getMain(1);
    },

    goPage: function (e) {
      var $clicked = $(e.target);
      if ($clicked.hasClass('glResultPager_num--current')) {
        return;
      }
      var gopage = parseInt($clicked.data('gopage'), 10);
      this.getMain(gopage);
    }

  }; //substructureApp END

  ////////////////////////////////////////

  //top page status
  var statusApp = {
    init: function () {
      this.cacheElements();
      this.getStatus();
    },

    cacheElements: function () {
      this.$app = $('#top_status_app');
      this.$total = this.$app.find('.statusTotalCount');
      this.$motif = this.$app.find('.statusMotifCount');
      this.$monosaccharide = this.$app.find('.statusMonosaccharideCount');
    },

    getStatus: function () {
      var _this = this;
      var obj = {};
      util.ajax_get('top_status', obj).then(function (data) {
        var values = data.split('<hr />');
        _this.$total.text(values[0]);
        _this.$motif.text(values[1]);
        _this.$monosaccharide.text(values[2]);
      }, function (err) {
        console.log(err);
      });
    }

  }; //statusApp END.


  var motifListApp = {
    init: function () {
      this.cacheElements();
      this.getList();
    },

    cacheElements: function () {
      this.$app = $('#motifListApp');
      this.$total = this.$app.find('.motifList_count');
      this.$table = this.$app.find('.motifList_table');
    },

    getList: function () {
      var _this = this;
      var obj = {notation: this.$app.data('notation') || 'cfg', page: 'motif_list'};
      util.ajax_get('motif_list', obj).then(function (data) {
        _this.$table.children('tbody').html(data);
        var count = _this.$table.children('tbody').find('tr').length;
        _this.$total.text(count);
      }, function (err) {
        console.log(err);
      });
    }
  }; //motifListApp END.

  var motifSearchApp = {
    init: function () {
      this.cacheElements();
      this.getList();
    },

    cacheElements: function () {
      this.$app = $('#motifSearchApp');
      this.$total = this.$app.find('.motifSearch_count');
      this.$table = this.$app.find('.motifSearch_table');
    },

    getList: function () {
      var _this = this;
      var obj = {notation: this.$app.data('notation') || 'cfg', page: 'motif_search'};
      util.ajax_get('motif_list', obj).then(function (data) {
        _this.$table.children('tbody').html(data);
        var count = _this.$table.children('tbody').find('tr').length;
        _this.$total.text(count);
      }, function (err) {
        console.log(err);
      });
    }
  }; //motifSearchApp END.

////////////////////////////////////////

//routing
  if ($('#glycan_list_app').length > 0) {
    listApp.init();
  } else if ($('#glycan_entry_app').length > 0) {
    entryApp.init();
  } else if ($('#top_status_app').length > 0) {
    statusApp.init();
  } else if ($('#substructure_list_app').length > 0 ) {
    substructureApp.init();
  } else if ($('#motifListApp').length > 0) {
    motifListApp.init();
  } else if ($('#motifSearchApp').length > 0) {
    motifSearchApp.init();
  }

});
