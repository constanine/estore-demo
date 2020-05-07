 var cKey = 'fileexIntro_import';
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
                    element: '#importfilelist',
                    intro: "<b>这里显示在外网盘上准备导入的文件或者文件夹的列表</b>"
                },
                {
                    element: '#excute_div',
                    intro: 	"<p>1.当前有导入任务在执行,不会出现<b style='color:#0066FF'>[确认]</b>按钮,但回出现查看当前进行任务的状态链接</p>"
                    		+"<p>1.当前外网盘中没有准备导入的文件，<b style='color:#0066FF'>[确认]</b>按钮将处于不能点击的状态</p>"
                    		+"<p>3.当前外网盘中有准备导入文件，且当无前导入任务执行中，<b style='color:#0066FF'>[确认]</b>按钮将处于可点击的状态，确认后将执行导入操作</p>",
                    position:"top"
                }                
            ]
            }).start();  

}

