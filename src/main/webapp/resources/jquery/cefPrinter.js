/**
 * Cef打印
 * Created by Leon on 2016/7/28.
 */
/**
 * 是否存在客户端cefPrint
 * @returns {boolean}
 */
function hasCefPrint(){
    try {
        if (cefPrint) {
            return true;
        }
    } catch (ex){
        return false;
    }
}

/**
 * 打印指定页面
 * @param url 指定页面URL，绝对路径
 * @param width 窗体初始化时的宽度
 * @param height 窗体初始化时的高度
 * @param isAutoPrint 是否加载后自动打印并关闭
 * @param isAutoClose 是否打印完成后自动关闭
 */
function printPage(url, width, height, isAutoPrint, isAutoClose) {
    cefPrint.printPage(url, width, height, isAutoPrint, isAutoClose);
}


function bodyOnLoad(PageHeight){
    if (hasCefPrint()) {
        $("#ieControl").hide();
        return;
    }
    prn1_print(PageHeight);
}
function bodyOnLoadView(PageHeight){
    if (hasCefPrint()) {
        $("#ieControl").hide();
        return;
    }
    prn1_preview(PageHeight);
}

var LODOP; //声明为全局变量
function prn1_preview(PageHeight) {
    LODOP=getLodop();
    LODOP.PRINT_INIT("");
    CreateOneFormPage(PageHeight);
    LODOP.PREVIEW();
}
function prn1_print(PageHeight) {
    LODOP=getLodop();
    LODOP.PRINT_INIT("");
    CreateOneFormPage(PageHeight);
    LODOP.PRINT();
}
function prn1_printA(PageHeight) {
    LODOP=getLodop();
    LODOP.PRINT_INIT("");
    CreateOneFormPage(PageHeight);
    LODOP.PRINTA();
}
function CreateOneFormPage(PageHeight){
    for(var i=1;i<500;i++){
        if(document.getElementById('page'+i)!=undefined){
            var page = document.getElementById('page'+i).innerHTML;
            var strBodyStyle="<style>"+document.getElementById("style1").innerHTML+"</style>";
            var strFormHtml=strBodyStyle+"<body>"+page+"</body>";
            LODOP.NewPage();
            LODOP.SET_PRINT_PAGESIZE(3,2200,PageHeight,"");
            LODOP.ADD_PRINT_HTM(0,0,800,PageHeight+100,strFormHtml);
        }else{
            break;
        }
    }
}
/**
 * 供客户端调用，用来负责打印页码
 * @param pageNumber
 */
function setPageNumber1(pageNumber,totalPage){
    var pageStr = document.getElementById("pageNumberHidden").value;
    var pageNumberStr = pageStr.replace(/x/g, pageNumber);
    pageNumberStr = pageNumberStr.replace(/y/g, totalPage);
    if(totalPage=='1'||totalPage==1){
        document.getElementById("pageNumber").style.display="none";
    }else{
        document.getElementById("pageNumber").style.display="block";
        document.getElementById("pageNumber").innerHTML = pageNumberStr;
    }
}

function setPageNumber2(){
    var pageStr = document.getElementById("pageNumber").value;
    var pageNumberStr = pageStr.replace(/x/g, cefPrint.CurrentPage);
    document.getElementById("pageNumber").innerHTML = pageNumberStr;
}

