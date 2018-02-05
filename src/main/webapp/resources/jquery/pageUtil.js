/**
 * Created by liuchen on 2017/3/31.
 */
$(function(){
    $("#selectPageSize").val($("#pageSize").val());
})

function toPage(formId,curPage){
    $("#currentPage").val(curPage);
    formId.submit();
}

function toPageNext(formId,page,totPage){
    var curpage = page + 1;
    if(eval(curpage) > eval(totPage)){
         alert("已经是最后一页了");
         return false;
    }
    toPage(formId,curpage);
}

function toPagePrev(formId,page){
    var curpage = page - 1;
    if(eval(curpage) < 1){
        alert("已经是第一页了");
        return false;
    }
    toPage(formId,curpage);
}

function toPageGo(formId,totPage,obj){
    var curpage = $(obj).prev("div").find(":text").val();
    if(eval(curpage) > eval(totPage)){
        alert("不可大于最大页数");
        return false;
    }
    if(eval(curpage) < eval(1)){
        alert("不可小于最小页数");
        return false;
    }
    toPage(formId,curpage);
}

function selectPage(obj,formId){
    var pageSize = $(obj).val();
    console.log(pageSize);
    $("#pageSize").val(pageSize);
    $("#currentPage").val(1);
    formId.submit();
}