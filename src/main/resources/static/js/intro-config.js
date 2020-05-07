 var cKey = 'fileexIntro_conifg';
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
                    element: '#innerurl',
                    intro: "<b>这是内网上传盘的IP地址</b>"
                },
                {
                    element: '#outerurl',
                    intro: "<b>这是外网上传盘的IP地址</b>"
                }                
            ]
            }).start();  

}

