var globalRgbArray;
var globalImgHeight;
var globalImgWidth;

//记住原来的html代码
//var originalRightDivinnerHTML = document.getElementById("rightDiv").innerHTML;

var globalShowImgWidth = 120;
var globalShowImgHeight = 40;

//对接收到的图片做一些校验判断和限制 true检验文件为正常图片 false不符合的文件 20231127
function validFileBeforeUpload(file) {
    // 判断是文本还是图片文件
    //文本的代码先不要
    //if (item.kind == 'string') {
    // // 获取文本内容
    // var text = event.clipboardData.getData('Text');
    // // 设置到content中
    // content.innerHTML = text;
    //} else
    //文件选择按钮获取的文件没有这个属性，而且一想下面大家都有的判断就够了。 zhou 20231127
    //if (item.kind != 'file') return;
    if (file.type.indexOf('image/') === -1 || file.type === 'image/gif') {
        alert("不是图片或者不是静态图片");
        return false;
    }
    if (file.size > 300000) {
        alert("图片大于300k,不收");
        return false;
    }
    return true;
}

//在上传图片到服务器前对图片做一定的处理 20231127
function dealImgBeforeUpload(file) {
// file 就是我们输入框选择文件所获取的文件内容
    // 图片插入div中
    var reader = new FileReader();
    reader.readAsDataURL(file);
    // 名称取当前时间戳
    const type = file.type.split('/');
    const renameFile = new File([file], new Date().getTime() + '.' + type[1], {
        type: file.type
    });

    reader.onload = function (event) {
        var img = document.createElement("img");
        // event.target.result 输出 图片base64
        img.src = event.target.result;

        img.onload = function () {
            // 创建Canvas元素
            var canvas = document.createElement("canvas");
            var ctx = canvas.getContext("2d");

            // 设置Canvas元素的大小与图像一致
            canvas.width = img.width;
            canvas.height = img.height;
            globalImgHeight = img.height;
            globalImgWidth = img.width;
            // 在Canvas上绘制图像
            ctx.drawImage(img, 0, 0);

            // 获取ImageData对象
            var imageData = ctx.getImageData(0, 0, canvas.width, canvas.height);
            var data = imageData.data;

            // 创建RGB三维数组
            var result = new Array(canvas.height);
            for (var i = 0; i < canvas.height; i++) {
                result[i] = new Array(canvas.width);
                for (var j = 0; j < canvas.width; j++) {
                    var index = (i * canvas.width + j) * 4;
                    var r = data[index];
                    var g = data[index + 1];
                    var b = data[index + 2];
                    result[i][j] = [r, g, b];
                }
            }
            //dealresult
            globalRgbArray = result;
            uploadImage2(event.target.result.substring(reader.result.indexOf(",") + 1));
        };

        content.innerHTML = "";
        content.appendChild(img);
        //后台需要以纯字符串的形式上传（即去掉data:image/png;base64，截取字符串即可）
        //uploadImage2(event.target.result);
    }
}

var content = document.getElementById("content");
// 添加监听事件paste 黏贴
content.addEventListener('paste', function (e) {
    // 黏贴版没有数据,则直接结束
    if (!(e.clipboardData && e.clipboardData.items)) return;
    // 黏贴版数据项,是个数组
    var data = e.clipboardData.items;
    if (!data || !data.length) return;
    var item = data[0];
    var file = item.getAsFile();
    if (!validFileBeforeUpload(file)) return;
    // 获取文件
    dealImgBeforeUpload(file);
});

//选择图片按钮的逻辑
function handleFiles(files) {
    var file = files[0];
    if (!validFileBeforeUpload(file)) return;
    dealImgBeforeUpload(file);
}

function checkStatus(response) {
    if (response.status >= 200 && response.status < 300) return response;
    var error = new Error(response.statusText);
    error.response = response;
    throw error;
}

function parseJSON(response) {
    return response.json();
}

function get3DArrayByteLength(arr) {
    const str = JSON.stringify(arr);
    const encoder = new TextEncoder();
    const bytes = encoder.encode(str);
    return bytes.length;
}

//校验图片分辨率，太大会触发nginx1M限制，1M会干穿小水管带宽的,干脆禁止这么大
function validPic() {
    if (globalImgHeight != null && globalImgHeight > 0 && globalImgWidth != null && globalImgWidth > 0)
        if (70000 < (globalImgWidth * globalImgHeight)) {
            alert("图片尺寸过大，分辨率长x宽需要小于70000");
            return false;
        }
    return true;
}

function uploadImage2(img) {
    if (validPic()) {
        let formData = new FormData();
        formData.append("imgBase64Str", img);
        formData.append("imageInfo.rgbArray", globalRgbArray);
        formData.append("imageInfo.height", globalImgHeight);
        formData.append("imageInfo.width", globalImgWidth);
        fetch(("/dealImg/dealImg"), {
            method: "POST",
            body: formData
        })
            .then(checkStatus)
            .then(
                response => {
                    console.log('联系服务器成功了');
                    return response.json();
                }
            ).then(
            response => {
                console.log('获取数据成功了', response);
                var resultCode = response.resultCode;
                if (resultCode == "0000") {
                    var nameList = response.resultData;
                    for (var i = 0; i < 12; i++) {
                        var textboxId = "textBox-" + i;
                        var textBox = document.getElementById(textboxId);
                        textBox.value = "";
                        if (nameList[i] != null && nameList[i] != "") {
                            textBox.value = nameList[i];
                        }
                    }
                    cleanLines();
                } else {
                    alert("图片识别失败:" + response.resultMessage);
                }
            }
        ).catch((error) => {
            console.log('请求出错', error);
        })
    }
}

function cleanImg() {
    content.innerHTML = "";
    document.getElementById("uploadFile").value = "";
}

function cleanTextBox() {
    for (var i = 0; i <= 11; i++) {
        var textboxId = "textBox-" + i;
        document.getElementById(textboxId).value = "";
    }
    cleanLines();
}

//三个查询按钮统一逻辑
function commitSearch(searchType) {
    if (!validParams()) return;

    disableButton();
    cleanLines();

    var selectArea = document.getElementById("areaSelect").value;
    //var object = new Object();
    //object.bigArea = selectArea;
    //document.cookie = object;
    //setCookie("selectArea", selectArea, 30);
    setServerCookie(selectArea);
    var nameList = new Array(12);
    for (var i = 0; i <= 11; i++) {
        var textboxId = "textBox-" + i;
        var textValue = document.getElementById(textboxId).value.trim();
        nameList[i] = textValue;
    }

    let formData = new FormData();
    formData.append("nameList", nameList);
    formData.append("searchType", searchType);
    formData.append("selectArea", selectArea);
    fetch(("/roleinfo/list"), {
        method: "POST",
        body: formData
    })
        .then(checkStatus)
        .then(
            response => {
                console.log('联系服务器成功了');
                return response.json();
            }
        ).then(
        response => {
            console.log('获取数据成功了', response);
            var resultCode = response.resultCode;
            if (resultCode == "0000") {
                var resultData = response.resultData;
                if (resultData != null) {
                    var picUrl = resultData.picUrl;
                    var roleInfoList = resultData.matchRoleVOList;
                    globalDataForPage = roleInfoList;
                    globalNowPage = 1;
                    globalTotalPage = roleInfoList.length % globalPageNum == 0 ? parseInt(roleInfoList.length / globalPageNum) : (parseInt(roleInfoList.length / globalPageNum) + 1);
                    document.getElementById("pageLable").innerHTML = globalNowPage + "/" + globalTotalPage;

                    if (roleInfoList != null && roleInfoList.length > 0) {
                        //setTimeout(function () {
                        //fillTable(roleInfoList, picUrl);
                        var endIndex = roleInfoList.length < globalPageNum ? roleInfoList.length : globalPageNum;

                        beforeFillTableByPage(0, endIndex, picUrl);
                        //updateDataByPage(1,globalTotalPage);
                        //document.getElementById("pageLable").innerHTML=1+"/"+totalPage;
                        //}, 1000);
                    } else {
                        tableBody.innerHTML = "没查到匹配信息！";
                    }
                }
            } else {
                alert(resultCode + "获取角色信息失败。" + response.resultMessage);
            }

        }).catch((error) => {
        console.log('请求出错', error);
    })
}

//校验名字参数
function validParams() {
    var checkFlag = true;
    var checkNullFlag = true;
    for (var i = 0; i <= 11; i++) {
        var textboxId = "textBox-" + i;
        var textValue = document.getElementById(textboxId).value;
        if (textValue != null && textValue.trim() != "") {
            checkNullFlag = false;
            if (textValue != null && textValue.length > 18) {
                alert("第" + (i + 1) + "栏角色名超长!");
                checkFlag = false;
                break;
            }
        }
    }
    if (checkNullFlag) alert("输入参数为空");
    return (checkFlag && !checkNullFlag);
}

function disableButton() {
    document.getElementById('searchroleNameBtn').disabled = true;
    document.getElementById('searchParentNameBtn').disabled = true;
    document.getElementById('searchAllBtn').disabled = true;
    setTimeout(function () {
        document.getElementById('searchroleNameBtn').disabled = false;
        document.getElementById('searchParentNameBtn').disabled = false;
        document.getElementById('searchAllBtn').disabled = false;
    }, 1000);
}

function disablePageButton() {
    document.getElementById('prePageBtn').disabled = true;
    document.getElementById('nextPageBtn').disabled = true;
    setTimeout(function () {
        document.getElementById('prePageBtn').disabled = false;
        document.getElementById('nextPageBtn').disabled = false;
    }, 1000);
}

var globalHost;
window.onload = onloadFunction;

function onloadFunction() {
    var selectArea = getCookie("selectArea");
    if (selectArea != null) document.getElementById("areaSelect").value = selectArea;
    var portNumber = window.location.port;
    var hostname = window.location.hostname;
    globalHost = hostname;
    if (portNumber != '') globalHost += (":" + portNumber);
    var areaDivHeight = document.getElementById("areaSelect").offsetHeight;
    document.getElementById("content").style.paddingTop = (areaDivHeight + 6) + "px";
}

//做一个加载图片判断，如果是404，那就加载自己的图片
function beforeFillTableByPage(beginIndex, endIndex, picUrl) {
    var loadOwnPicFlag = false;
    var img = new Image();
    img.src = picUrl;
    img.onload = function () {
        // 图像成功加载
        console.log("图像成功加载");
        fillTableByPage(beginIndex, endIndex, picUrl, loadOwnPicFlag);
    };

    img.onerror = function () {
        // 图像加载失败
        console.error('Failed to load image');
        loadOwnPicFlag = true;
        // 你可以在此处采取适当的措施，例如显示占位图像或显示错误消息。
        fillTableByPage(beginIndex, endIndex, picUrl, loadOwnPicFlag);
    };
}

function fillTableByPage(beginIndex, endIndex, picUrl, loadOwnPicFlag) {
    var tableBody = document.getElementById('tableBody');
    tableBody.innerHTML = "";

    for (var i = beginIndex; i < endIndex; i++) {
        var roleInfo = globalDataForPage[i];
        // 创建新行
        var newRow = tableBody.insertRow();

        // 创建单元格并设置内容
        var cell0 = newRow.insertCell();
        var spanElement = document.createElement('span');
        spanElement.id = 'right' + i + roleInfo.matchRow;
        spanElement.innerHTML = roleInfo.bigArea;
        cell0.appendChild(spanElement);
        drawLine('left' + roleInfo.matchRow, spanElement.id);

        var cell1 = newRow.insertCell();
        cell1.textContent = roleInfo.littleArea;

        var cell2 = newRow.insertCell();
        cell2.textContent = roleInfo.roleType;

        var cell3 = newRow.insertCell();
        var div = document.createElement('div');
        if (loadOwnPicFlag) {
            div.style.background = "url(" + roleInfo.bigPicUrl + ")";
            div.style.height = globalShowImgHeight + "px";
            div.style.width = globalShowImgWidth + "px";
        } else {
            div.style.background = "url(" + picUrl + ")";
            div.style.backgroundPosition = "0px " + (-1 * globalShowImgHeight * i) + "px";
            div.style.height = globalShowImgHeight + "px";
            div.style.width = globalShowImgWidth + "px";
        }
        cell3.appendChild(div);

        var matchType = roleInfo.matchType == 0 ? "角色名称" : "冒险团名称";
        var matchDetail = matchType + "与" + roleInfo.matchRow + "栏信息匹配度" + (roleInfo.matchRate * 100) + "%";
        var cell4 = newRow.insertCell();
        cell4.textContent = matchDetail;

        var cell5 = newRow.insertCell();
        cell5.textContent = roleInfo.reason;

        var cell6 = newRow.insertCell();
        var aEle = document.createElement('a');
        aEle.href = '#'; // 设置链接地址
        aEle.textContent = '证据';
        aEle.onclick = (function (roleId) {
            return function () {
                window.open(('/reasonDetail.html?roleId=' + roleId));
                return false;
            };
        })(roleInfo.roleId);
        cell6.appendChild(aEle);

    }
}

var globalDataForPage;
var globalNowPage;
var globalTotalPage;
var globalPageNum = 10;

function prePage() {
    disablePageButton();
    if (globalDataForPage != null && globalNowPage > 1) {
        updateDataByPage(globalNowPage - 1, globalTotalPage);
    }
}

function nextPage() {
    disablePageButton();
    if (globalDataForPage != null && globalNowPage < globalTotalPage) {
        updateDataByPage(globalNowPage + 1, globalTotalPage);
    }
}

function updateDataByPage(nowPage, totalPage) {
    var beginIndex = (nowPage - 1) * globalPageNum;
    var endIndex;
    if (nowPage == totalPage) endIndex = globalDataForPage.length;
    else endIndex = nowPage * globalPageNum;
    var picIndexArray = new Array((endIndex - beginIndex));
    for (var i = beginIndex, j = 0; i < endIndex; i++, j++) {
        picIndexArray[j] = globalDataForPage[i].roleId;
    }
    var picUrl = findPicUrlByPicIndexArray2(picIndexArray);
    if (picUrl != null) {
        //setTimeout(function () {
        beforeFillTableByPage(beginIndex, endIndex, picUrl);
        document.getElementById("pageLable").innerHTML = nowPage + "/" + totalPage;
        globalNowPage = nowPage;
        //}, 1000);
    }
}

function findPicUrlByPicIndexArray2(picIndexArray) {
    var picUrl;
    var requestData = {
        roleIdList: picIndexArray
    };

    var xhr = new XMLHttpRequest();
    var url = '/roleinfo/findPicUrlByPicIndexArray';
    //var params = 'param1=value1&param2=value2'; // 参数字符串，按需修改

    //xhr.open('GET', url + '?' + params, true); // GET 请求，参数拼接在 URL 上
    xhr.open('POST', url, false); // POST 请求，参数作为请求体发送
    xhr.setRequestHeader('Content-Type', 'application/json');

    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                // 请求成功
                //console.log(xhr.responseText);
                var response = JSON.parse(xhr.responseText);
                var resultCode = response.resultCode;
                if (resultCode == "0000" && response.resultData != null) {
                    picUrl = response.resultData;
                } else {
                    alert(resultCode + "获取图片信息失败");
                }
            } else {
                // 请求失败
                console.error('请求失败: ' + xhr.status);
            }
        }
    };

    // 如果是 POST 请求，需要将参数作为 send 方法的参数传入
    xhr.send(JSON.stringify(requestData)); // 发送参数作为请求体

    // 如果是 GET 请求，不需要传入参数
    //xhr.send(); // 发送请求
    return picUrl;
}

// function findPicUrlByPicIndexArray(picIndexArray) {
//     var picUrl;
//     var returnFlag = false;
//     setTimeout(function () {
//         returnFlag = true;
//     }, 2000);
//     if (picIndexArray != null) {
//         var formData = new FormData();
//         formData.append("roleIdList", picIndexArray);
//         fetch( "/roleinfo/findPicUrlByPicIndexArray", {
//             method: "POST",
//             body: formData
//         })
//             .then(checkStatus)
//             .then(
//                 response => {
//                     return response.json();
//                 }
//             ).then(
//             response => {
//                 var resultCode = response.resultCode;
//                 if (resultCode == "0000" && response.resultData != null) {
//                     picUrl = response.resultData;
//                     returnFlag = true;
//                 } else {
//                     alert(resultCode + "获取图片信息失败");
//                 }
//
//             }).catch((error) => {
//             console.log('请求出错', error);
//         })
//     }
//     while (returnFlag) return picUrl;
// }

//获取cookie
function getCookie(name) {
    var cookies = document.cookie.split("; ");
    for (var i = 0; i < cookies.length; i++) {
        var cookie = cookies[i].split("=");
        if (cookie[0] === name) {
            return decodeURIComponent(cookie[1]);
        }
    }
    return null;
}

function setServerCookie(value) {
    if (getCookie("selectArea") == value) return;
    var formData = new FormData();
    formData.append("selectArea", value);
    fetch("/base/setCookie", {
        method: "POST",
        body: formData
    })
        .then(checkStatus)
        .then(
            response => {
                return response.json();
            }
        ).then(
        response => {
            var resultCode = response.resultCode;
            if (resultCode == "0000" && response.resultData != null) {

            } else {
                console.log(resultCode + "保存cookie失败");
            }

        }).catch((error) => {
        console.log('请求出错', error);
    })
}

function createLineElement(x, y, length, angle) {
    var line = document.createElement("div");
    var styles = 'border: 1px solid red; '
        + 'width: ' + length + 'px; '
        + 'height: 0px; '
        + '-moz-transform: rotate(' + angle + 'rad); '
        + '-webkit-transform: rotate(' + angle + 'rad); '
        + '-o-transform: rotate(' + angle + 'rad); '
        + '-ms-transform: rotate(' + angle + 'rad); '
        + 'position: absolute; '
        + 'top: ' + y + 'px; '
        + 'left: ' + x + 'px; ';
    line.setAttribute('style', styles);
    line.className = "lineClass";
    return line;
}

//画线代码
function createLine(x1, y1, x2, y2) {
    var a = x1 - x2,
        b = y1 - y2,
        c = Math.sqrt(a * a + b * b);

    var sx = (x1 + x2) / 2,
        sy = (y1 + y2) / 2;

    var x = sx - c / 2,
        y = sy;

    var alpha = Math.PI - Math.atan2(-b, a);

    return createLineElement(x, y, c, alpha);
}

// 获取元素坐标
function getElementOffset(element) {
    let offset = {left: 0, top: 0};
    let current = element.offsetParent;

    offset.left += element.offsetLeft;
    offset.top += element.offsetTop;

    while (current !== null) {
        offset.left += current.offsetLeft;
        offset.top += current.offsetTop;
        current = current.offsetParent;
    }
    return offset;
}


function drawLine(id1, id2) {
    xy1 = getElementOffset(document.getElementById(id1));
    xy2 = getElementOffset(document.getElementById(id2));
    height1 = document.getElementById(id1).offsetHeight;
    height2 = document.getElementById(id2).offsetHeight;
    w1 = document.getElementById(id1).offsetWidth;
    w2 = document.getElementById(id2).offsetWidth;
    document.body.appendChild(createLine(xy1.left + (w1 / 2), xy1.top + (height1 / 2), xy2.left, xy2.top + height2));
}

function begindraw() {
    drawLine('right1', 'left12');
}

function cleanLines() {
    var elements = document.getElementsByClassName("lineClass");
    for (var i = elements.length - 1; i >= 0; i--) {
        elements[i].remove();
    }
}