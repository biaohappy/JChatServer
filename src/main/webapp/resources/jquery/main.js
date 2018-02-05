/**
 * Created by zhoup on 2017/1/14.
 */
$(function () {
    initTableCss();
    bindSelectAll();
    //该方法为限制金额的小数位
    $(".numLimit").keyup(function(){
        this.value = this.value.replace(/[^\d.]/g,""); //清除"数字"和"."以外的字符
        this.value = this.value.replace(/^\./g,""); //验证第一个字符是数字
        this.value = this.value.replace(/\.{2,}/g,"."); //只保留第一个, 清除多余的
        this.value = this.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
        this.value = this.value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3'); //只能输入两个小数
    });
});

function initTableCss() {
    // $("tr").hasClass("odd").onclick(function () {
    //     //如果没有某个样式则加上，否则去除
    //     $(this).children().toggleClass("table-bgColor");
    // });
    // $('tr:odd:not(0)').css('background', '#dfffb0');
    // $('tr:even').css('background', '#fff');
};

function bindSelectAll() {
    $(".selectAll").unbind("click");
    $(".selectAll").bind("click", function () {
        $("input[name=" + $(".selectAll").attr("name") + "]").each(function (i) {
            if ($(this).parent().css("display") != "none") {
                $(this).attr("checked", $(".selectAll").attr("checked"));
            }
        });
    });
    $("checkBox").not(".selectAll").unbind("click");
    $("checkBox").not(".selectAll").bind("click", function () {
        var v = $("input:checked").not(".selectAll").length;
        var v1 = $("checkBox").not(".selectAll").length;
        if (v == v1) {
            $(".selectAll").attr("checked", true);
        } else {
            $(".selectAll").attr("checked", false);
        }
    });
}

function getIds() {
    var ids = '';
    $("input[name=" + $(".selectAll").attr("name") + "]").each(function () {
        if ($(this).attr("checked") == true && !$(this).hasClass("selectAll")) {
            if ($(this).val() != '') {
                if (ids == '') {
                    ids = $(this).val();
                } else {
                    ids += "," + $(this).val();
                }
            }
        }
    });
    return ids;
}

function wopen(url,tittle) {
    tittle = tittle == null ? '明细' : tittle;
    art.dialog.open(url, {
        title : tittle,
        lock : true, //锁屏
        background : "", // 背景色
        width: "75%",
        height : "80%",
        close: function () {
            location.reload();
        },
    }, true);
}

function undo(url) {
    title='撤销';
    art.dialog.open(url, {
        title : title,
        lock : true, //锁屏
        background : "", // 背景色
        width: "432px",
        height : "480px",
        close: function () {
            location.reload();
        },
    }, true);
}

function commonAutoComplete(obj, url, maxShowSize, keyFlag, valueFlag, replaceFlag,formatFunction,callback) {
    $(obj).autocomplete(url, {
        matchContains: true,
        width: 500,
        minChars: 0,
        max: maxShowSize,
        mustMatch: false,
        async: false,
        matchSubset: false,
        cacheLength: 100,
        dataType: "json",
        extraParams: {
            "resourceLength": maxShowSize,
            "keyFlag": keyFlag,
            "valueFlag": valueFlag
        },
        parse: function (data) {
            return $.map(data, function (row) {
                return {
                    data: row,
                    value: row.key,
                    result: replaceFlag && (row.value != "") && (row.key != "") ? (row.value + "---[" + row.key + "]") : (row.key + "---[" + row.value + "]")
                }
            });
        },
        formatItem: function (item) {
            return formatFunction(item);
        }
    }).result(function (e,item) {
        callback(e, item);
    });
}


function autoComplete(obj, url, maxShowSize, keyFlag, valueFlag, replaceFlag, keyDisplayFlag, cacheLength, type) {
    var completeWidth = 300;
    if(type == 'autoName' || type == 'namecard'){
        completeWidth = 500;
    }
    $(obj).autocomplete(url, {
        matchContains: true,
        width: completeWidth,
        minChars: 0,
        max: maxShowSize,
        mustMatch: false,
        async: false,
        matchSubset: false,
        cacheLength: cacheLength,
        dataType: "json",
        extraParams: {
            "resourceLength": maxShowSize,
            "keyFlag": keyFlag,
            "valueFlag": valueFlag
        },
        parse: function (data) {
            return $.map(data, function (row) {
                return {
                    data: row,
                    value: row.key,
                    result: replaceFlag && (row.value != "") && (row.key != "") ? (row.value + "---[" + row.key + "]") : (row.key + "---[" + row.value + "]")
                }
            });
        },
        formatItem: function (item) {
            if(type == "goods" || type == "goodsAll"){
                var arr = item.value.split("|");
                var displayVal = arr[3] + "(" + arr[4] + ")";
                return keyDisplayFlag ? ("[" + displayVal + "]") : (item.key + "---[" + displayVal + "]");
            }else if(type == "namecard" || type == "autoName"){
                return keyDisplayFlag ? ("[" + item.value.substring(7) + "]") : (item.key.substring(7) + "---[" + item.value.substring(7) + "]");
            }

        }
    }).result(function (e, item, formatted) {
        /**
         * 加载数据
         */
        if(type == "goods"){
            var arr = item.value.split("|");
            intoGoodsForType(type,this,arr,item);
        }else if(type == "namecard"){
            $(this).val(item.key.substring(7));
        }else if(type == "goodsAll"){
            var arr = item.value.split("|");
            intoGoodsALLForType(type,this,arr,item);
        }else if(type == "autoName"){
            console.log(item);
            $(this).val(item.key.substring(7));
            $("."+type).val(item.value.substring(7).substring(0,item.value.substring(7).indexOf("(")));
        }
    });
}

function intoGoodsForType(type, object, arr, item){
    $(object).val(arr[3]);//品种名称
    var tr = $(object).parent().parent();
    var goodsid = item.key.toString().substring(7);
    $(tr).find(".goodsId").val(goodsid);//品种id
    $(tr).find(".zone").val(arr[0]);//品种区域、大类
    //$(tr).find(".price").val(arr[2]);//单价
}

function intoGoodsALLForType(type, object, arr, item){
    $(object).val(arr[3]);//品种名称
    $("#goodsId").val(item.key.toString().substring(7)+"|"+arr[3]);
    //getZtreeIdByTitleName(arr[3]);
}

function getZtreeIdByTitleName(goodsName){
    var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
    treeObj.expandAll(false);
    $("#treeDemo").find("a[title="+goodsName+"]").each(function(){
        var node = treeObj.getNodeByTId($(this).parent()[0].id).getParentNode();
        treeObj.expandNode(node, true, true, true, false);
        treeObj.checkNode(treeObj.getNodeByTId($(this).parent()[0].id), true, true);
    })
}

/**
 * 将form表单数据转换为json对象
 * @returns {{}}
 */
$.fn.serializeObject = function() {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [ o[this.name] ];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};
//Confirm  Dialog  弹窗
$(function () {
    window.Modal = function () {
        var reg = new RegExp("\\[([^\\[\\]]*?)\\]", 'igm');
        var alr = $("#ycf-alert");
        var ahtml = alr.html();

        //关闭时恢复 modal html 原样，供下次调用时 replace 用
        //var _init = function () {
        //	alr.on("hidden.bs.modal", function (e) {
        //		$(this).html(ahtml);
        //	});
        //}();

        /* html 复原不在 _init() 里面做了，重复调用时会有问题，直接在 _alert/_confirm 里面做 */


        var _alert = function (options) {
            alr.html(ahtml);	// 复原
            alr.find('.ok').removeClass('btn-success').addClass('btn-primary');
            alr.find('.cancel').hide();
            _dialog(options);

            return {
                on: function (callback) {
                    if (callback && callback instanceof Function) {
                        alr.find('.ok').click(function () { callback(true) });
                    }
                }
            };
        };

        var _confirm = function (options) {
            alr.html(ahtml); // 复原
            alr.find('.ok').removeClass('btn-primary').addClass('btn-success');
            alr.find('.cancel').show();
            _dialog(options);

            return {
                on: function (callback) {
                    if (callback && callback instanceof Function) {
                        alr.find('.ok').click(function () { callback(true) });
                        alr.find('.cancel').click(function () { callback(false) });
                    }
                }
            };
        };

        var _dialog = function (options) {
            var ops = {
                msg: "提示内容",
                title: "操作提示",
                btnok: "确定",
                btncl: "取消"
            };

            $.extend(ops, options);

            console.log(alr);

            var html = alr.html().replace(reg, function (node, key) {
                return {
                    Title: ops.title,
                    Message: ops.msg,
                    BtnOk: ops.btnok,
                    BtnCancel: ops.btncl
                }[key];
            });

            alr.html(html);
            alr.modal({
                width: 500,
                backdrop: 'static'
            });
        }

        return {
            alert: _alert,
            confirm: _confirm
        }

    }();
});