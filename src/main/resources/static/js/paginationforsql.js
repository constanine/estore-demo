var totalpage,pagesize,cpage,outstr,showpagesdiv,show; 
//初始化 
cpage = 1; 
outstr = ""; 
function pageinit(startpage,pages,pagenum,showdiv,showmethod){
	totalpage=pages;
	pagesize=pagenum;
	showpagesdiv=showdiv;
	show=showmethod;
	cpage=startpage;
}




function gotopage(target) 
{     
    cpage = target;        //把页面计数定位到第几页 
    setpage(); 
    //reloadpage(target);    //调用显示页面函数显示第几页,这个功能是用在页面内容用ajax载入的情况 
} 
function setpage() 
{ 
    if(totalpage <= pagesize){        //总页数小于十页 
        for (var count=1;count<=totalpage;count++) 
        {    if(count!=cpage) 
            { 
                outstr = outstr + "<a href='showmissiondeal?show="+show+"&pagesize="+pagesize+"&startnum="+((count-1)*pagesize)+"'>"+count+"</a>"; 
            }else{ 
                outstr = outstr + "<span class='current' >"+count+"</span>"; 
            } 
        } 
    } 
    if(totalpage > pagesize){        //总页数大于十页 
        if(parseInt((cpage-1)/10) == 0) 
        {             
            for (var count=1;count<=10;count++) 
            {    if(count!=cpage) 
                { 
                    outstr = outstr + "<a href='showmissiondeal?show="+show+"&pagesize="+pagesize+"&startnum="+((count-1)*pagesize)+"'>"+count+"</a>"; 
                }else{ 
                    outstr = outstr + "<span class='current'>"+count+"</span>"; 
                } 
            } 
            outstr = outstr + "<a href='javascript:void(0)' onclick='gotopage("+count+")'> next </a>"; 
        } 
        else if(parseInt((cpage-1)/10) == parseInt(totalpage/10)) 
        {     
            outstr = outstr + "<a href='javascript:void(0)' onclick='gotopage("+(parseInt((cpage-1)/10)*10)+")'>previous</a>"; 
            for (count=parseInt(totalpage/10)*10+1;count<=totalpage;count++) 
            {    if(count!=cpage) 
                { 
                    outstr = outstr +"<a href='showmissiondeal?show="+show+"&pagesize="+pagesize+"&startnum="+((count-1)*pagesize)+"'>"+count+"</a>"; 
                }else{ 
                    outstr = outstr + "<span class='current'>"+count+"</span>"; 
                } 
            } 
        } 
        else 
        {     
            outstr = outstr + "<a href='javascript:void(0)' onclick='gotopage("+(parseInt((cpage-1)/10)*10)+")'>previous</a>"; 
            for (var count=parseInt((cpage-1)/10)*10+1;count<=parseInt((cpage-1)/10)*10+10;count++) 
            {         
                if(count!=cpage) 
                { 
                    outstr = outstr + "<a href='showmissiondeal?show="+show+"&pagesize="+pagesize+"&startnum="+((count-1)*pagesize)+"'>"+count+"</a>"; 
                }else{ 
                    outstr = outstr + "<span class='current'>"+count+"</span>"; 
                } 
            } 
            outstr = outstr + "<a href='javascript:void(0)' onclick='gotopage("+count+")'> next </a>"; 
        } 
    }     
    $("#"+showpagesdiv).html("<span id='info'>共"+totalpage+"页|第"+cpage+"页<\/span>" + outstr + ""); 
    outstr = ""; 
} 


