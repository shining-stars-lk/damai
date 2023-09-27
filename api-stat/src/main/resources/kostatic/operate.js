function checkLogin() {
    $.get(contextPath+'/koTime/isLogin?kotoken=' + globalToken+"&project="+globalProject, function (data) {
        globalIsLogin = data['isLogin'] == 1 ? true : false;
    });
    if (globalNeedLogin == true && globalIsLogin == false) {
        UIkit.modal(document.getElementById("modal-login")).show();
        return;
    }
    ;
}

function loadConfig(){
    $.get(contextPath+'/koTime/getConfig?kotoken='+globalToken+"&project="+globalProject, function (data) {
        // let exceptionEnable = data['exceptionEnable'];
        //
        // let exceptionEnableDom = document.getElementById('exceptionEnable');
        // exceptionEnableDom.checked = exceptionEnable;
        //

        let kotimeEnable = data['enable'];
        // let kotimeEnableDom = document.getElementById('kotimeEnable');
        // kotimeEnableDom.checked = kotimeEnable;

        let apiTip = document.getElementById('apiTip');
        apiTip.innerHTML = kotimeEnable==true?"接口根据调用情况统计，未调用的接口无法被统计到，请先调用接口":"方法调用监测已关闭，数据将不会更新，需要开启请到配置面板";

        let threshold = data['threshold'];
        let timeThresholdDom = document.getElementById('timeThreshold');
        timeThresholdDom.value = threshold;

        // let logEnable = data['logEnable'];
        // let logEnableDom = document.getElementById('logEnable');
        // logEnableDom.checked = logEnable;

        let language = data['language'];
        $("#languageSwitch").val(language)
    });
}

function addConfigEvent(){


    document.getElementById('languageSwitch').onchange = function(){
        let selectedObj = document.getElementById('languageSwitch');
        $.ajax({type:'POST',url:contextPath+'/koTime/updateConfig?kotoken='+globalToken+"&project="+globalProject,data:JSON.stringify({language:selectedObj.options[selectedObj.selectedIndex].value}),dataType:'json', headers: {'Content-Type': 'application/json' }});
    };

    document.getElementById("timeThresholdYes").onclick = function(){
        $.ajax({type:'POST',url:contextPath+'/koTime/updateConfig?kotoken='+globalToken+"&project="+globalProject,data:JSON.stringify({threshold:document.getElementById('timeThreshold').value}),dataType:'json', headers: {'Content-Type': 'application/json' }});
    };
}

function loadStatistic(){
    $.get(contextPath+'/koTime/getStatistic?kotoken='+globalToken+"&project="+globalProject, function (data) {
        let totalNum = data['totalNum'];
        let systemTotalNum = document.getElementById("systemTotalNum");
        systemTotalNum.innerHTML=totalNum;

        let normalNum = data['normalNum'];
        let systemNormalNum = document.getElementById("systemNormalNum");
        systemNormalNum.innerHTML=normalNum;

        let delayNum = data['delayNum'];
        let systemDelayNum = document.getElementById("systemDelayNum");
        systemDelayNum.innerHTML=delayNum;
        if (delayNum>0) {
            document.getElementById("systemDelayNum-div").className+=' uk-label-danger';
        }else {
            document.getElementById("systemDelayNum-div").className+=' uk-label-success';
        };

        let avgRunTime = data['avgRunTime'];
        let systemAvgRunTime = document.getElementById("systemAvgRunTime");
        systemAvgRunTime.innerHTML=avgRunTime;
        if (avgRunTime>globalThreshold) {
            document.getElementById("systemAvgRunTime-div").className+=' uk-label-danger';
        }else {
            document.getElementById("systemAvgRunTime-div").className+=' uk-label-success';
        };

        let maxRunTime = data['maxRunTime'];
        let systemMaxRunTime = document.getElementById("systemMaxRunTime");
        systemMaxRunTime.innerHTML=maxRunTime;
        if (maxRunTime>globalThreshold) {
            document.getElementById("systemMaxRunTime-div").className+=' uk-label-danger';
        }else {
            document.getElementById("systemMaxRunTime-div").className+=' uk-label-success';
        };


        let minRunTime = data['minRunTime'];
        let systemMinRunTime = document.getElementById("systemMinRunTime");
        systemMinRunTime.innerHTML=minRunTime;
        if (minRunTime>globalThreshold) {
            document.getElementById("systemMinRunTime-div").className+=' uk-label-danger';
        }else {
            document.getElementById("systemMinRunTime-div").className+=' uk-label-success';
        };

    });
}

function loadApis(){
    let searchText = $("#searchText").val();
    $.get(contextPath+'/koTime/getApis?question='+searchText+'&kotoken='+globalToken+"&project="+globalProject, function (data) {
        let element = document.getElementById('apiList');
        html = '';
        for (let i = 0; i < data.length; i++) {
            let id = data[i]['id'];
            let className = data[i]['className'];
            let methodName = data[i]['methodName'];
            let avgRunTime = data[i]['avgRunTime'];
            let methodType = data[i]['methodType'];
            let routeName = data[i]['routeName'];

            let apiId = className+"."+methodName;
            let  color = avgRunTime>globalThreshold?'danger':'success';
            if (methodType=='Controller' && routeName!=null && routeName!='') {
                html += "<li onclick=\"showMethods('"+id+"')\" style='color: #333;font-weight: 400;font-size: 14px;' id=\""+apiId+"-list\">"+ className+"#<span style='font-size: 16px;font-weight: 500;'>"+methodName+"</span>&nbsp(<span style='font-size: 14px;font-weight: 430;color:#032b11'>"+routeName+"</span>)&nbsp &nbsp<span style='font-size: 10px;' class=\"uk-label uk-label-"+color+"\">平均响应 "+avgRunTime+" 毫秒</span></li>";
            }else{
                html += "<li onclick=\"showMethods('"+id+"')\" style='color: #333;font-weight: 400;font-size: 14px;' id=\""+apiId+"-list\">"+ className+"#<span style='font-size: 16px;font-weight: 500;'>"+methodName+"</span>&nbsp &nbsp<span style='font-size: 10px;' class=\"uk-label uk-label-"+color+"\">平均响应 "+avgRunTime+" 毫秒</span></li>";
            }
        };
        element.innerHTML = html;
    });
}

function loadExceptions(){
    $.get(contextPath+'/koTime/getExceptions?kotoken='+globalToken+"&project="+globalProject, function (data) {
        let element = document.getElementById('exceptionList');
        html = '';
        for (let i = 0; i < data.length; i++) {
            let id = data[i]['id'];
            let className = data[i]['className'];
            let message = data[i]['message'];
            html += "<li onclick=\"showExceptions('"+id+"','"+message+"')\" style='' id=\""+id+"\"><span style='font-size: 16px;font-weight: 500;'>"+className+"</span>&nbsp &nbsp<span style='font-size: 10px;' class=\"uk-label uk-label-danger\">"+message+"</span></li>";
        };
        element.innerHTML = html;
    });
}

let methodParamMap  = new Map();
function paramGraph(e) {
    let  clickNode = e.currentTarget;
    if (clickNode==null){
        return
    };
    let clickNodeId = clickNode.id;
    if (methodParamMap.has(clickNodeId+"ana")) {
        graph.removeNode(clickNodeId+"ana")
        methodParamMap.delete(clickNodeId+"ana")
    }else {
        $.get(contextPath+'/koTime/getParamGraph?kotoken='+globalToken+"&methodId="+clickNodeId.replace('node','')+"&project="+globalProject, function (data) {
            let datas = []
            for(let key in data) {
                datas.push( {"name":key+"：平均 "+data[key]['avgRunTime']+" ms"})
            };
            let paramGraphData = {
                "id":clickNodeId+"ana",
                "from":clickNodeId,
                "title":{'name':"入参组合分析"},
                "data":datas,
                'style':{
                    'title-color':'#427b72',
                    'border-color':'#427b72',
                    'data-font-size':'10px',
                    'title-font-size':'12px'
                }
            }
            let clickNodeX =  Number(clickNode.getAttribute("x"));
            let clickNodeY =  Number(clickNode.getAttribute("y"));
            graph.createNode(paramGraphData,clickNodeX,clickNodeY-100);
            // graph.createNode(paramGraphData,e.x+150,e.y-30);
            methodParamMap.set(clickNodeId+"ana","")
        });
    }
}
function formData(data) {
    if (data['avgRunTime']>globalThreshold) {
        data['style'] = {
            'title-color':'#c71335',
            'border-color':'#c71335',
            'data-font-size':'10px',
            'title-font-size':'12px'
        }
    }
    else{
        data['style'] = {
            'title-color':'#375d46',
            'border-color':'#375d46',
            'data-font-size':'10px',
            'title-font-size':'12px'
        }
    };
    data['title'] = {'name':data['name']};
    data['data'] = [
        {'name':'<span style="color:gray">类型：</span>'+data['methodType']},
        {'name':'<span style="color:gray">平均耗时：</span>'+data['avgRunTime']+' ms'},
        {'name':'<span style="color:gray">最大耗时：</span>'+data['maxRunTime']+' ms'},
        {'name':'<span style="color:gray">最小耗时：</span>'+data['minRunTime']+' ms'}
    ];
    if (data['exceptionNum']!=null && data['exceptionNum']>0) {
        data['data'].push({'name':'<span style="color:gray">异常数目：</span>'+data['exceptionNum']+' 个'});
    }
    data["dblclick"]="paramGraph";
    return data;
};


function showMethods(name) {
    let exceptionDetailDom = document.getElementById('layerDemo');
    exceptionDetailDom.innerHTML = "";
    let options = {
        'link-start-offsetx':8,
        'link-start-offsety':0,
        'link-end-offsetx':+10,
        'link-end-offsety':0,
        'link-width-offset':0
    };
    graph = new MetricFlow("layerDemo",options);
    graph.threshold = globalThreshold;
    UIkit.notification.closeAll();
    UIkit.modal(document.getElementById("modal-method")).show();

    $.get(contextPath+'/koTime/getTree?methodName=' + name+'&kotoken='+globalToken+"&project="+globalProject, function (data) {
        let rootX = 100;
        let rootY = $(window).get(0).innerHeight / 2-50;
        data['x'] = rootX;
        data['y'] = rootY;
        graph.createNodes(data,formData);

    });

};

function showExceptions(id,message) {
    $.get(contextPath+'/koTime/getMethodsByExceptionId?exceptionId=' + id+'&message='+message+'&kotoken='+globalToken+"&project="+globalProject, function (data) {
        let html = '';
        for (let i = 0; i < data.length; i++) {
            html +=
                "<ul class=\"uk-list uk-list-divider\">\n" +
                "\t<li id=\"exception-class\">异常类："+data[i].occurClassName+"</li>\n" +
                "\t<li id=\"exception-method\">异常方法："+data[i].methodName+"</li>\n" +
                "\t<li id=\"exception-line\">异常行：<span class=\"uk-label uk-label-success\">"+data[i].location+"</span></li>\n" +
                "\t<li id=\"exception-message\">异常消息：<span class=\"uk-label uk-label-danger\">"+data[i].message+"</span></li>\n" +
                "</ul>"
            if (data.length-1>i) {
                html +='<hr class="uk-divider-icon">'
            }
        };

        let exceptionDetailDom = document.getElementById('exception-detail');
        exceptionDetailDom.innerHTML = html;
        UIkit.notification.closeAll();
        UIkit.modal(document.getElementById("modal-exception")).show();
    });
};

function login() {
    let userId = $("#userName").val()
    let password = $("#userPassword").val()
    if (userId==undefined || userId.length<1 || password==undefined || password.length<1 ) {
        UIkit.notification.closeAll()
        UIkit.notification("<font color='red'>请正确输入用户名和密码</font>",{});
        return
    }
    $.ajax({
        type:'POST',
        url:contextPath+"/koTime/login",
        data:JSON.stringify({'userName':userId,'password':password}),
        dataType:'json',
        headers: {'Content-Type': 'application/json' },
        success:function (re) {
            if (re['state']==1) {
                UIkit.notification("<font color='green'>登录成功</font>",{});
                UIkit.notification.closeAll()
                sessionStorage.setItem("kotimeToken", re["token"]);
                location.reload();
            }else {
                UIkit.notification("<font color='red'>用户名或密码错误</font>",{});
            }
        },
        error:function (re) {
            console.log(re)
        }
    });
}
function searchTip(e){
    let question =  $('#searchText').val()
    $.get(contextPath+'/koTime/getApiTips?question='+question+'&kotoken='+globalToken+"&project="+globalProject, function (data) {
        $("#condidates").html("")
        for (let i = 0; i < data.length; i++) {
            let name = data[i];
            $("#condidates").append('<option value="'+name+'"/>');
        };
    });
}

function loadProjects(){
    $.get(contextPath+'/koTime/getProjects?kotoken='+globalToken+"&project=all", function (data) {
        $("#projects").html("")
        $("#projects").append('<option selected hidden disabled value="">选择项目</option>');
        for (let i = 0; i < data.length; i++) {
            let name = data[i];
            $("#projects").append(`<option value="${name}">${name}</option>`);
        };
    });
}

function searchApis(e) {
    if (e.keyCode == 13) {
        let question =  $('#searchText').val()
        $.get(contextPath+'/koTime/getApis?question='+question+'&kotoken='+globalToken+"&project="+globalProject, function (data) {
            let element = document.getElementById('apiList');
            html = '';
            for (let i = 0; i < data.length; i++) {
                let id = data[i]['id'];
                let className = data[i]['className'];
                let methodName = data[i]['methodName'];
                let avgRunTime = data[i]['avgRunTime'];
                let methodType = data[i]['methodType'];
                let routeName = data[i]['routeName'];
                let apiId = className+"."+methodName;
                let  color = avgRunTime>globalThreshold?'danger':'success';

                if (methodType=='Controller' && routeName!=null && routeName!='') {
                    html += "<li onclick=\"showMethods('"+id+"')\" style='color: #333;font-weight: 400;font-size: 14px;' id=\""+apiId+"-list\">"+ className+"#<span style='font-size: 16px;font-weight: 500;'>"+methodName+"</span>&nbsp(<span style='font-size: 14px;font-weight: 430;color:#032b11'>"+routeName+"</span>)&nbsp &nbsp<span style='font-size: 10px;' class=\"uk-label uk-label-"+color+"\">平均响应 "+avgRunTime+" 毫秒</span></li>";
                }else{
                    html += "<li onclick=\"showMethods('"+id+"')\" style='color: #333;font-weight: 400;font-size: 14px;' id=\""+apiId+"-list\">"+ className+"#<span style='font-size: 16px;font-weight: 500;'>"+methodName+"</span>&nbsp &nbsp<span style='font-size: 10px;' class=\"uk-label uk-label-"+color+"\">平均响应 "+avgRunTime+" 毫秒</span></li>";
                }
            };
            element.innerHTML = html;
        });
        $('#searchText').val('');
    }
}

function getProjectName(){
    globalProject = document.querySelector("#projects").value;
    loadStatistic();
    loadApis();
    loadExceptions();
}