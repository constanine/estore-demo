 var cKey = 'fileexIntro_home';
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
                    intro: "<b>您好!FileExchange系统已更新，简单介绍一下！</b><p>回车和方向键控制步骤，ESC键退出。点击下面小圆点可直接跳至该步骤</p>"
                },			
			    {
                    element: '#header',
                    intro: "<b>页面头部信息</b><p>登出功能在右上角</p>"
                },
                {
                    element: document.querySelector('.tree'),
                    intro: "<b>这里是功能导航菜单</b>",
					position:"right"
                },
                {
                    element: document.querySelector('.tree').querySelector('.treeHead'),
                    intro: "<b>点击此菜单,跳转至首页</b>",
					position:"right"
                },				
                {
                    element: document.querySelector('.tree').querySelectorAll('li')[0],//找到匹配的第一项
                    intro: "<b>点击此菜单,可以查看内外网盘的IP配置</b>",
					position:"right"
                },
                {
                    element: document.querySelector('.tree').querySelectorAll('li')[1],//找到匹配的第一项
                    intro: "<b>点击此菜单,准备将外网的准备好的文件导入至内网</b>",
					position:"right"
                },
                {
                    element: document.querySelector('.tree').querySelectorAll('li')[2],//找到匹配的第一项
                    intro: "<b>点击此菜单,准备将内网的准备好的文件导出至外网</b>",
					position:"right"
                },
                {
                    element: document.querySelector('.tree').querySelectorAll('li')[4],//找到匹配的第一项
                    intro: "<b>点击此菜单,将内网trac等内部可允许查看的网页转成外网可以访问的url</b><p style='color:red'>注意:目前必须全部用IP,域名将无法解析</p>",
					position:"right"
                },
                {
                    element: '#messagecontent',
                    intro: '<dt><b>最近消息</b></dt><dd>这里将显示<em>自己和他人的最近动态</em></dd>',
                    position: 'right'
                },
                {
                	element:'#importcontent',
                    intro: '<p>这里显示最近3次导入任务的状态</p>'
                },
                {
                	element:'#exportcontent',
                    intro: '<p>这里显示最近3次导出任务的状态</p>'
                },
                {
                    element: document.querySelector('.refresh'),
                    intro: "<b>点击此按钮，刷新当前任务状态信息</b>",
					position:"left"
                },
                {
                    element: '#sacnbtn',
                    intro: "<b>当导出任务的任务完成，并且返回首页时，判断状态为‘待审批’，如果急于导出，那么请点击此按钮.<b><p>一般任务审批1分钟会自动执行一次</p>",
					position:"left"
                },
                {
                    element: '#export2',
                    intro: "<b>当导出任务的任务判断状态为自动导出时，即可导出的状态，如果急于导出，那么请点击此按钮</b><p>一般导出状态的任务，1分钟会自动执行一次任务导出</p>",
					position:"left"
                },
                
            ]
            }).start();  

}

