 var cKey = 'fileexIntro_issueurl';
//每次页面加载时调用即可
function initintro(){   
    var start = document.cookie.indexOf(cKey);
    var date = new Date();
    date.setTime(date.getTime() + 30*24*60*60*1000);
    if(start<0)
        document.cookie = cKey + '='+date.getTime()+';expires='+ date.toGMTString();
    else{
    	var value =0;
    	if(document.cookie.split(";").length>1){
    		var end = document.cookie.indexOf(';',start);
    		if(end<0){
    			end=document.cookie.length;
    		}
    		value = document.cookie.substring(start,end);
    	}else{
    		value = document.cookie;
    	}
        var visitTime = value.split('=')[1];
        if(visitTime>0 && visitTime>new Date().getTime()){
            document.cookie = cKey + '=' + (Number(date.getTime())+1)+";expires="+ date.toGMTString();
            return;
        }
        else
            document.cookie = cKey + '=' + (Number(date.getTime())+1)+";expires="+ date.toGMTString();
    }  
	

    showintro();

}

$(function(){
	initintro();
});
		
//每次页面加载时调用即可
function showintro(){   
    
            introJs().setOptions({
            nextLabel : '下一步',
            prevLabel : '上一步',
            skipLabel : '退出引导',
            doneLabel : '结束',
            exitOnOverlayClick : false,			

                //对应的数组，顺序出现每一步引导提示
			steps: [			  
			    {
                    element: '#intraneturl',
                    intro: "<b>这里输入内网开源的访问页面</b>"
                    		+"<p style='color:red'><b>注意事项:</b></p>"
                    		+"<p style='color:red'>1.url的输入规则,请求协议必须输入如:http,ftp等</p>"
                    		+"<p style='color:red'>2.非开源项目将无法显示其内容</p>"
                    		
                },
                {
                    element: '#checkpdfcreate',
                    intro: "<b>点击此按钮可以预览开源网页转成pdf后的访问页面</b>"
                },
                {
                    element: '#showinfodiv',
                    intro: "<b>当输入的url符合要求时，给出一个外网可以访问的url,否则就报出错误信息</b>"
                }                
            ]
            }).start();  

}

