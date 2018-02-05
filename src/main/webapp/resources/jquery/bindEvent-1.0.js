/***********************************密码键盘事件开始********************************************/

//定义密码键盘通用参数
var pwd_confirm_auto = 1;
var pwd_length = 6;
var master_key_no = 0;
var work_key_no = 0;
var voiceValue_input_pwd_again = 1;
var voiceValue_input_pwd = 0;
var randomNum = "";
var locked = false;

function openComPort() {
    var portArray = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9];
    var baud = 9600;
    var openFlag = false;
    var pwdPadReturnVal;
    for (var port in portArray) {
        try {
            pwdPadReturnVal = HJ_OCX.Keypad_SetCom(port, baud);
            if (pwdPadReturnVal == 0) {
                console.log("端口打开成功！当前使用端口：COM" + port);
                try {
                    var seqNo = getDeviceSeq();
                    if (seqNo == "0") {
                        openFlag = true;
                        break;
                    } else {
                        console.log("错误端口！");

                    }
                } catch (err) {
                    console.log(err);

                }

            }
        } catch (err) {
            console.log(err);
        }
    }
    if (!openFlag) {
        alert("硬件设备初始化失败！请刷新页面。当前硬件设备只支持IE浏览器，并将浏览器的Internet 选项->安全->该区域的安全级别 设置为低！");

    }
}
/**
 * 卡号的一部分作为加密因子，所以输入密码之前必须先选择卡号，避免解密失败。
 * @returns {boolean}
 */
function cardCodeIsNull() {
    var cardCode = $("input[name='cardcode']").val();
    if (cardCode == null || cardCode == '') {
        alert("请先选择卡！");
        return true;
    } else {
        //random：加密方法的参数之一，一般一个页面只有一个卡号，所以这里取值后直接放入全局变量
        randomNum = cardCode.substring(0, 12);
        return false;
    }
}

function readPin(index) {
    openComPort();
    //index：1 原始密码，不做重复密码校验 2:第一次输入新密码，需要做重复密码校验
    var pwdPadReturnVal = HJ_OCX.Keypad_DESReadPin(master_key_no, work_key_no,
        voiceValue_input_pwd, pwd_confirm_auto, pwd_length, 20, randomNum);
    if (pwdPadReturnVal == 0) {
        var isSimplePwd = HJ_OCX.PinRule == 1;
        if (index == 0) {
            //输入原密码
            $(".oldPassword").val(HJ_OCX.PinData);
            $(".newPassWord").focus().click();
        } else {
            if (isSimplePwd) {
                //第一次输入新密码，需要做复杂度校验
                alert("密码过于简单，请重新输入！");

            } else {
                $(".newPassWord").val(HJ_OCX.PinData);
                $(".confirmNewPassWord").focus().click();
            }
        }
    } else {
        alert("读取密码失败，请重试！\n" + "错误码:" + pwdPadReturnVal + "错误信息：");
        cancelInput();
    }
}

function readPinAgain() {
    if (cardCodeIsNull()) {
        return;
    }
    openComPort();
    var pwdPadReturnVal = HJ_OCX.Keypad_DESReadPin(master_key_no, work_key_no,
        voiceValue_input_pwd_again, pwd_confirm_auto, pwd_length, 20, randomNum);
    if (pwdPadReturnVal == 0) {
        var isSimplePwd = HJ_OCX.PinRule == 1;
        if (isSimplePwd) {
            alert("密码过于简单，请重新输入！");
        } else {
            if ($(".newPassWord").val() != HJ_OCX.PinData) {
                $(".confirmNewPassWord").val("");
                alert("两次输入的密码不一致，请重新输入密码！");
                $(".newPassWord").val("").focus().click();
            } else {
                $("input[name='confirmNewPwd']").val(HJ_OCX.PinData);
            }
        }
    } else {
        alert("读取密码失败，请重新输入！\n" + "错误码:" + pwdPadReturnVal);
        cancelInput();
    }
}

function getDeviceSeq() {
    var seq = HJ_OCX.Keypad_GetSeqNo();
    console.log(seq);
    return seq;
}

function cancelInput() {
    console.log("停止上次输入事件");
    if(hasCefKeyPad()){
        cefKeyPad.cancelReadPasswd();
    }else {
        HJ_OCX.Keypad_CancelOpt();
    }
}

/**
 * 密码键盘输入密码（调用c#客户端cefsharp框架的CefKeyPad类）
 * @param index：1 原始密码，不做重复密码校验 2:第一次输入新密码，需要做重复密码校验
 */
function readPinByCef(index) {

    if (cardCodeIsNull()) {
        console.log("正在读取密码！");
        return;
    }
    if(locked) return;
    locked = true;

    if($("#oldPasswordHidden").val() == "1" && index == 0){
        return;
    }

    if($("#secondPasswordHidden").val() == "1" && index == 2){
        return;
    }

    if(index == 0){
        $("#oldPasswordHidden").val("1");
    }

    if(index == 2){
        $("#secondPasswordHidden").val("1");
    }

    if (!hasCefKeyPad()) {
        alert("请使用客户端调用此密码输入方法");
        return;
    }
    //voice 语音，0-请输入密码，1-请再输入一次密码
    //timeout 超时时间
    cefKeyPad.readPasswd5(0, 20, randomNum, 0);
    if (cefKeyPad.success) {
        //index：1 原始密码，不做重复密码校验 2:第一次输入新密码，需要做重复密码校验
        if (index == 0) {
            //输入原密码
            $(".oldPassword").val(cefKeyPad.encryptPasswd);
            locked = false;
            $(".newPassWord").click();
        }else if (index == 2) {
            //输入原密码
            $(".secondPassword").val(cefKeyPad.encryptPasswd);
        } else {
            $(".newPassWord").val(cefKeyPad.encryptPasswd);
            locked = false;
            $(".confirmNewPassWord").click();
        }
    } else {
        //密码键盘错误提示
        alert(cefKeyPad.error);
    }
    setTimeout(function () {
        locked = false;
        $("#oldPasswordHidden").val("0");
        $("#secondPasswordHidden").val("0");
    }, 500);
}

function readPinAgainByCef() {
    if (locked) {
        console.log("有未完成的密码输入事件！");
        return;
    }
    locked = true;
    //voice 语音，0-请输入密码，1-请再输入一次密码
    //timeout 超时时间
    cefKeyPad.readPasswd2(1, 20, randomNum, 0);
    if (cefKeyPad.success) {
        if ($(".newPassWord").val() != cefKeyPad.encryptPasswd) {
            $(".confirmNewPassWord").val("");
            alert("两次输入的密码不一致，请重新输入密码！");
            locked = false;
            $(".newPassWord").val("").focus().click();
        } else {
            $(".confirmNewPassWord").val(cefKeyPad.encryptPasswd);
        }
    } else {
        //密码键盘错误提示
        alert(cefKeyPad.error);
    }
    setTimeout(function () {
        locked = false;
    }, 500);
}

/**
 * CEF密码键盘自定义参数设置
 * @param MKeyArea int 主密钥区号，默认0
 * @param WKeyArea int 工作密钥区号，默认0
 * @param autoType int 确认密码方式,0-自动，1-手动
 * @param pinMaxLen int 密码输入最大长度
 * @param userAccount string 帐号（12位）
 * @param timeoutSec int 输入超时，单位秒
 */
function cefKeyPadSetParam(MKeyArea, WKeyArea, autoType, pinMaxLen, userAccount, timeoutSec) {
    cefKeyPad.setParam(MKeyArea, WKeyArea, autoType, pinMaxLen, userAccount, timeoutSec);
}

/**
 * 是否存在客户端CefKeyPad
 * @returns {boolean}
 */
function hasCefKeyPad() {
    try {
        if (cefKeyPad) {
            return true;
        }
    } catch (ex) {
        return false;
    }
    return false;
}


/***********************************密码键盘事件结束********************************************/


/***********************************读卡器事件开始********************************************/
function openCardReaderPort() {
    var version = MWRFATL.openReader("100", 9600);
    var result = false;
    if (version != null && version != "" && version.indexOf("读取硬件版本号失败") < 0) {
        console.log("读卡器连接成功！" + "\n 读卡器版本：" + version);
        result = true;
    } else {
        alert("读卡器未正确连接，请检查读卡器线路！");
        result = false;
    }
    return result;
}

//断开设备
function closeCardReader() {
    var iRet = MWRFATL.CloseReader();
    if (iRet) {
        console.log("断开设备失败！请重新插入读卡器！")
    } else {
        console.log("断开设备成功！");
    }
}

//关闭卡片
function closeCard() {
    var iRet = MWRFATL.CloseCard();
    if (iRet) {
        console.log("关闭卡片失败！")
    }
    else {
        console.log("关闭卡片成功！")
    }
}

//寻卡
function Scard() {
    var returnVal = MWRFATL.openCard(1);
    if (returnVal.indexOf("寻卡失败") > -1) {
        alert("寻卡失败，请重新放置卡片！");
        return false;
    }
    return true;
}
//验证密码
function authenticationKey() {
    var iRet = MWRFATL.mifare_authentication(0, 1);
    if (iRet) {
        alert("卡片校验失败,请重试！");
        return false;
    } else {
        console.log("验证密码成功！");
        return true;
    }
}

//以16进制读数据
function readDataHex() {
    var databuff = MWRFATL.mifare_readHex(4);
    databuff = databuff.substring(1, 20);
    $(".autoCardCodeByName").val(databuff);
    MWRFATL.ReaderBeep(10);
}
function autoCompleteCardCode() {
    //打开端口
    if (openCardReaderPort()) {
        if (Scard()) {
            if (authenticationKey()) {
                readDataHex();
            }
        }
        //关闭卡片
        closeCard();
        //关闭连接
        closeCardReader();
    }
}

/**
 * 读卡器读卡（调用c#客户端cefsharp框架的CefCardReader类）
 * @param elementType 赋值元素类型（1 id  |  2 name  |  3 class  |  其他 id）
 * @param elementName 赋值元素名称
 * @param isBeep 成功读卡后是否有提示声
 */
function readCardByCef(elementType, elementName, isBeep) {
    if (!hasCefCardReader()) {
        alert("请使用客户端调用此读卡方法");
        return;
    }
    cefCardReader.readCard(isBeep);
    if (cefCardReader.success) {
        if (elementType == 1) {
            //elementType 1 按id来赋值
            $("#" + elementName).val(cefCardReader.result);
        } else if (elementType == 2) {
            //elementType 2 按name来赋值
            $("[name=" + elementName + "]").val(cefCardReader.result);
        } else if (elementType == 3) {
            //elementType 3 按class来赋值
            $("." + elementName).val(cefCardReader.result);
        } else {
            //elementType 其他 按id来赋值
            $("#" + elementName).val(cefCardReader.result);
        }
    } else {
        //读卡器错误提示
        alert(cefCardReader.error);
    }
}
/**
 * 是否存在客户端CefCardReader
 * @returns {boolean}
 */

function hasCefCardReader() {
    try {
        if (cefCardReader) {
            return true;
        }
    } catch (ex) {
        return false;
    }
}


/***********************************读卡器事件结束********************************************/

/***********************************事件绑定开始********************************************/
$(document).ready(
    function () {
        //oldPasswordHidden 防止多次点击触发密码键盘
        $(".oldPassword").parent().append("<input type='hidden' id='oldPasswordHidden' value='0' />");
        $(".oldPassword").click(function () {
            if (hasCefCardReader()) {
                readPinByCef(0);
            } else {
                readPin(0);
            }
        });

        $(".secondPassword").parent().append("<input type='hidden' id='secondPasswordHidden' value='0' />");
        $(".secondPassword").click(function () {
            if (hasCefCardReader()) {
                readPinByCef(2);
            }
        });

        $(".newPassWord").click(function () {
            if (hasCefKeyPad()) {
                readPinByCef(1);
            } else {
                readPin(1);
            }
        });

        $(".confirmNewPassWord").click(function () {
            if (hasCefKeyPad()) {
                readPinAgainByCef();
            } else {
                readPinAgain();
            }
        });

        $(".readCard").click(function () {
            if (hasCefCardReader()) {
                readCardByCef(3, "autoCardCodeByName", true);
                $("#userform").submit();
            } else {
                autoCompleteCardCode();
            }
        });
        $(".readCardNoCommit").click(function () {
            if (hasCefCardReader()) {
                readCardByCef(3, "autoCardCodeByName", true);
            } else {
                autoCompleteCardCode();
            }
        });
        $("#readCardById").click(function () {
            if (hasCefCardReader()) {
                readCardByCef(1, "autoCardCodeById", true);
                checkChild("#autoCardCodeById",null,1);
            } else {
                autoCompleteCardCode();
            }
        });
    }
);
/***********************************事件绑定结束******************************************/

/***********************************COOKIE赋值开始******************************************/
//将密码键盘端口放入cookie，过期时间为30天，作用域为所有页面
function setCookieValues(cookieName, cookieValue, expires, scope) {
    $.cookie(cookieName, cookieValue, {expires: expires, path: scope});
}
/***********************************COOKIE赋值结束******************************************/





