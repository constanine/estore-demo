var totalpage2,pagesize2,outstr2,tartable2,targettbody2,showpagesdiv2; 
//初始化 
outstr2 = ""; 
function pageinit2(pages,pagenum,targettable,targettbody,showdiv){
	totalpage2=pages;
	pagesize2=pagenum;
	tartable2=targettable;
	targettbody2=targettbody;
	showpagesdiv2=showdiv;
}



function gotopage2(target) 
{     
    cpage = target;        //把页面计数定位到第几页 
    setpage2(); 
    showthispage2(target,tartable2,targettbody2);
    //reloadpage(target);    //调用显示页面函数显示第几页,这个功能是用在页面内容用ajax载入的情况 
} 
function setpage2() 
{ 
    if(totalpage2 <= pagesize2){        //总页数小于十页 
        for (var count=1;count<=totalpage2;count++) 
        {    if(count!=cpage) 
            { 
                outstr2 = outstr2 + "<a href='javascript:void(0)' onclick='gotopage2("+count+")'>"+count+"</a>"; 
            }else{ 
                outstr2 = outstr2 + "<span class='current' >"+count+"</span>"; 
            } 
        } 
    } 
    if(totalpage2 > pagesize2){        //总页数大于十页 
        if(parseInt((cpage-1)/10) == 0) 
        {             
            for (var count=1;count<=10;count++) 
            {    if(count!=cpage) 
                { 
                    outstr2 = outstr2 + "<a href='javascript:void(0)' onclick='gotopage2("+count+")'>"+count+"</a>"; 
                }else{ 
                    outstr2 = outstr2 + "<span class='current'>"+count+"</span>"; 
                } 
            } 
            outstr2 = outstr2 + "<a href='javascript:void(0)' onclick='gotopage2("+count+")'> next </a>"; 
        } 
        else if(parseInt((cpage-1)/10) == parseInt(totalpage2/10)) 
        {     
            outstr2 = outstr2 + "<a href='javascript:void(0)' onclick='gotopage2("+(parseInt((cpage-1)/10)*10)+")'>previous</a>"; 
            for (count=parseInt(totalpage2/10)*10+1;count<=totalpage2;count++) 
            {    if(count!=cpage) 
                { 
                    outstr2 = outstr2 + "<a href='javascript:void(0)' onclick='gotopage2("+count+")'>"+count+"</a>"; 
                }else{ 
                    outstr2 = outstr2 + "<span class='current'>"+count+"</span>"; 
                } 
            } 
        } 
        else 
        {     
            outstr2 = outstr2 + "<a href='javascript:void(0)' onclick='gotopage2("+(parseInt((cpage-1)/10)*10)+")'>previous</a>"; 
            for (count=parseInt((cpage-1)/10)*10+1;count<=parseInt((cpage-1)/10)*10+10;count++) 
            {         
                if(count!=cpage) 
                { 
                    outstr2 = outstr2 + "<a href='javascript:void(0)' onclick='gotopage2("+count+")'>"+count+"</a>"; 
                }else{ 
                    outstr2 = outstr2 + "<span class='current'>"+count+"</span>"; 
                } 
            } 
            outstr2 = outstr2 + "<a href='javascript:void(0)' onclick='gotopage2("+count+")'> next </a>"; 
        } 
    }     
    $("#"+showpagesdiv2).html("<span id='info'>共"+totalpage2+"页|第"+cpage+"页<\/span>" + outstr2 + ""); 
    outstr2 = ""; 
} 
//调用分页 
function showthispage2(target,tartable){
	$("#"+tartable+" tbody").css({display:"none"});
	$("#"+targettbody2+target).css({display:"table-row-group"});
}