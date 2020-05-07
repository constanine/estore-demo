  $(function() {
		//////////////////////////////////  加载原来的onload事件  //////////////////////////////////
	  var oldonload=window.onload;
	  oldonload();
	  
	  /////////////////////////////////填写信息显示 js ////////////////////////////////
	  $("#showurlmission").click(function() {	
		  $("#showurlcontent").show();
		  $("#showfilecontent").hide();
		  $(this).addClass("hover");
		  $(this).siblings("li").removeClass("hover");/*两个将集成  此处未改完 2014.2.28*/
	  });
	  
	  $("#showfilemission").click(function() {	
		  $("#showurlcontent").hide();
		  $("#showfilecontent").show();
		  $(this).addClass("hover");
		  $(this).siblings("li").removeClass("hover");	  
	  });
	  
		//////////////////////////////////  url 的使用js  //////////////////////////////////
	$("#u_line_all").click(function() {		
		 var checksarray=$(".url_check");
		if($("#u_line_all").prop("checked")){
			var urlinfo;
			var thisvar;
			checksarray.prop("checked", "true");
			for(var i=0;i<checksarray.length;i++){
			  thisvar=$(checksarray[i]);
			  urlinfo=$("<input class=\"btn\" type=\"hidden\"  name=\"filename\" value=\""+$("#uname"+thisvar.val()).html()+"\" />"
						+ "<input type=\"hidden\" name=\"filetype\" value=\"url\" />"
						+ "<input type=\"hidden\" name=\"filesize\" value=\"0\" />"
						+ "<input class=\"btn\" type=\"hidden\"  name=\"uri\" value=\""+$("#url"+thisvar.val()).html()+"\" />");
			  thisvar.parent().append(urlinfo);
			}
			js$('uemsub').disabled = "";
		}else{
			for(var i=0;i<checksarray.length;i++){
				  thisvar=$(checksarray[i]);
				  var newcheck=$("<input type=\"checkbox\" class=\"url_check\" value=\""+thisvar.val()+"\" />");
				  thisvar.parent().html(newcheck);
				  newcheck.click(function(){
					  grapthischeck(this);
				  });
				}
			checksarray.prop("checked", null);			
			
			
			urliscannotconfrim();
		}	
	});
	
	$(".url_check").click(
			function(){
			grapthischeck(this);
			}
	);
	
	function grapthischeck(itself) {
		var $this=$(itself);
		if($this.prop("checked")){
			var urlinfo=$("<input class=\"btn\" type=\"hidden\"  name=\"filename\" value=\""+$("#uname"+$this.val()).html()+"\" />"
					+ "<input type=\"hidden\" name=\"filetype\" value=\"url\" />"
					+ "<input type=\"hidden\" name=\"filesize\" value=\"0\" />"
					+ "<input class=\"btn\" type=\"hidden\"  name=\"uri\" value=\""+$("#url"+$this.val()).html()+"\" />");
			$this.parent().append(urlinfo);
			js$('uemsub').disabled = "";
		}else{
			var newcheck=$("<input type=\"checkbox\" class=\"url_check\" value=\""+$this.val()+"\" />");			
			$this.parent().html(newcheck);
			newcheck.click(function(){
			grapthischeck(this);
			});
			urliscannotconfrim();
		}
		
	}
	
	function urliscannotconfrim(){
		var missionnum=0;
	    var checksarray=$(".url_check");
		for(var i=0;i<checksarray.length;i++){
			  if($(checksarray[i]).prop("checked")){
				  missionnum++;
			  }
		}
		if(missionnum<1){
			js$('uemsub').disabled = "true";
		}
	}
	
	//////////////////////////////////  file 的使用js  //////////////////////////////////
	
	$("#f_line_all").click(function() {		
		 var fchecksarray=$(".file_check");
		if($("#f_line_all").prop("checked")){		
			var fileinfo;
			var thisfilevar;
			fchecksarray.prop("checked", "true");
			for(var i=0;i<fchecksarray.length;i++){
				thisfilevar=$(fchecksarray[i]);
			  fileinfo=$("<input class=\"btn\" type=\"hidden\"  name=\"filename\" value=\""+$("#fname"+thisfilevar.val()).html()+"\" />"
						+ "<input type=\"hidden\" name=\"filetype\" value=\"file\" />"
						+ "<input type=\"hidden\" name=\"filesize\" value=\""+$("#fisze"+thisfilevar.val()).html()+"\" />"
						+ "<input class=\"btn\" type=\"hidden\"  name=\"uri\" value=\""+$("#path"+thisfilevar.val()).html()+"\" />");
			  thisfilevar.parent().append(fileinfo);
				  if(parseFloat($("#fisze"+thisfilevar.val()).html())){
					  $("#file_sumfilesize").val( parseInt($("#file_sumfilesize").val())+parseInt($("#fisze"+thisfilevar.val()).html()));
						
				  }			  
			}
			js$('femsub').disabled = "";
		}else{
			for(var i=0;i<fchecksarray.length;i++){
				thisfilevar=$(fchecksarray[i]);
				  var filenewcheck=$("<input type=\"checkbox\" class=\"file_check\" value=\""+thisfilevar.val()+"\" />");
				  thisfilevar.parent().html(filenewcheck);
				  filenewcheck.click(function(){
					  grapfilethischeck(this);
				  });
				  if(parseFloat($("#fisze"+thisfilevar.val()).html())){
					  $("#file_sumfilesize").val(parseInt( $("#file_sumfilesize").val())-parseInt($("#fisze"+thisfilevar.val()).html()));	
				  }	
			}
			fchecksarray.prop("checked", null);			
			fireiscannotconfrim();
		}	
	});
	
	$(".file_check").click(
			function(){
				grapfilethischeck(this);
			}
	);
	
	function grapfilethischeck(itself) {
		var thisfilevar=$(itself);
		if(thisfilevar.prop("checked")){
			var fileinfo=$("<input class=\"btn\" type=\"hidden\"  name=\"filename\" value=\""+$("#fname"+thisfilevar.val()).html()+"\" />"
					+ "<input type=\"hidden\" name=\"filetype\" value=\"file\" />"
					+ "<input type=\"hidden\" name=\"filesize\" value=\""+$("#fisze"+thisfilevar.val()).val()+"\" />"
					+ "<input class=\"btn\" type=\"hidden\"  name=\"uri\" value=\""+$("#path"+thisfilevar.val()).html()+"\" />");
			thisfilevar.parent().append(fileinfo);
			js$('femsub').disabled = "";
		}else{
			var filenewcheck=$("<input type=\"checkbox\" class=\"file_check\" value=\""+thisfilevar.val()+"\" />");			
			thisfilevar.parent().html(filenewcheck);
			filenewcheck.click(function(){
			grapfilethischeck(this);
			});
			fireiscannotconfrim();
		}
		
	}
	
	function fireiscannotconfrim(){
		var filemissionnum=0;
	    var fchecksarray=$(".file_check");
		for(var i=0;i<fchecksarray.length;i++){
			  if($(fchecksarray[i]).prop("checked")){
				  filemissionnum++;
			  }
		}
		if(filemissionnum<1){
			js$('femsub').disabled = "true";
		}
	}
	 
  });
  
  function grapfilethischeck(itself) {
		var thisfilevar=$(itself);
		if(thisfilevar.prop("checked")){
			var fileinfo=$("<input class=\"btn\" type=\"hidden\"  name=\"filename\" value=\""+$("#fname"+thisfilevar.val()).html()+"\" />"
					+ "<input type=\"hidden\" name=\"filetype\" value=\"file\" />"
					+ "<input type=\"hidden\" name=\"filesize\" value=\""+$("#fisze"+thisfilevar.val()).val()+"\" />"
					+ "<input class=\"btn\" type=\"hidden\"  name=\"uri\" value=\""+$("#path"+thisfilevar.val()).html()+"\" />");
			thisfilevar.parent().append(fileinfo);
			js$('femsub').disabled = "";
		}else{
			var filenewcheck=$("<input type=\"checkbox\" class=\"file_check\" value=\""+thisfilevar.val()+"\" />");			
			thisfilevar.parent().html(filenewcheck);
			filenewcheck.click(function(){
			grapfilethischeck(this);
			});
			fireiscannotconfrim();
		}
		
	}
	
	function fireiscannotconfrim(){
		var filemissionnum=0;
	    var fchecksarray=$(".file_check");
		for(var i=0;i<fchecksarray.length;i++){
			  if($(fchecksarray[i]).prop("checked")){
				  filemissionnum++;
			  }
		}
		if(filemissionnum<1){
			js$('femsub').disabled = "true";
		}
	}
  
  function scanfileagain(){

		var xhr = getXhr();
		xhr.open('post', 'testajajx', true);
		xhr.setRequestHeader('Content-Type',
				'application/x-www-form-urlencoded');
		xhr.onreadystatechange = function() {
		if(xhr.reayState==3){
		
			js$('showinfo').style.display = "inline";
			js$('showinfo').innerHTML="清稍等......";
		}else if (xhr.readyState == 4) {

				if (xhr.status == 200) {
					txt = xhr.responseText;
					var filelist=eval(txt);
					fulltablefromscanfile(filelist);
				} else {
					js$('showinfo').innerHTML = "目前有错误，请联系";
					js$('showinfo').style.display = "inline";
				}
			}
		};
		xhr.send('towhich=Export&doajax=scanfile');
	}
	
	function fulltablefromscanfile(filelist){
		var showfiletable=$("#showfiletable");
		showfiletable.html("<thead class=\"table-head\"><tr  >"
				+"<th width=\"60px\"><label for=\"f_line_all\">全选</label>&nbsp;<input type=\"checkbox\" id=\"f_line_all\" /></th>"
				+"<th width=\"30px\">编号</th>"
				+"<th width=\"30px\">类型</th>"
				+"<th width=\"80px\">名字</th>"
				+"<th width=\"100px\">Path</th>"				
				+"</tr></thead>");
		
		
		for(var i=0;i<filelist.length;i++){
			
		var rowno=parseInt(i+1);

		var tr=$('<tr></tr>');	
		var tadd=$("<td></td>");		
		var ajaxnewcheck=$("<input type=\"checkbox\" class=\"file_check\" value=\""+i+"\" />");			
			ajaxnewcheck.click(function(){
				grapfilethischeck(this);
			});
		tadd.append(ajaxnewcheck);	
		tr.append(tadd);
		var trow=$("<td>"+rowno+"</td>");
		tr.append(trow);
		var ttype=$("<td>File<input id=\"fisze"+i+"\" type=\"hidden\" value=\"+filelist[0].size+\"></td>");
		tr.append(ttype);
		var tname=$("<td id=\"fname"+i+"\">"+filelist[i].filename+"</td>");
		tr.append(tname);
		var tpath=$("<td id=\"path"+i+"\">"+filelist[i].path+"</td>");
		tr.append(tpath);
		
		showfiletable.append(tr);
		}
		if(filelist.length<1){
			var trempty=$('<tr></tr>');	
			var tdempty=$('<td colspan="5">当前文件列表为空</td>');
			trempty.append(tdempty);
			showfiletable.append(trempty);			
		}
		
		
		$("#f_line_all").click(function() {		
			fchecksarray=$(".file_check");
			if($("#f_line_all").prop("checked")){		
				var fileinfo;
				var thisfilevar;
				fchecksarray.prop("checked", "true");
				for(var i=0;i<fchecksarray.length;i++){
					thisfilevar=$(fchecksarray[i]);
				  fileinfo=$("<input class=\"btn\" type=\"hidden\"  name=\"filename\" value=\""+$("#fname"+thisfilevar.val()).html()+"\" />"
							+ "<input type=\"hidden\" name=\"filetype\" value=\"file\" />"
							+ "<input type=\"hidden\" name=\"filesize\" value=\""+$("#fisze"+thisfilevar.val()).html()+"\" />"
							+ "<input class=\"btn\" type=\"hidden\"  name=\"uri\" value=\""+$("#path"+thisfilevar.val()).html()+"\" />");
				  thisfilevar.parent().append(fileinfo);	
				  $("#file_sumfilesize").val( parseInt($("#file_sumfilesize").val())+parseInt($("#fisze"+thisfilevar.val()).html()));
				}
				js$('femsub').disabled = "";
			}else{
				for(var i=0;i<fchecksarray.length;i++){
					thisfilevar=$(fchecksarray[i]);
					  var filenewcheck=$("<input type=\"checkbox\" class=\"file_check\" value=\""+thisfilevar.val()+"\" />");
					  thisfilevar.parent().html(filenewcheck);
					  filenewcheck.click(function(){
						  grapfilethischeck(this);
					  });
					  $("#file_sumfilesize").val(parseInt( $("#file_sumfilesize").val())-parseInt($("#fisze"+thisfilevar.val()).html()));
					}
				fchecksarray.prop("checked", null);			
				fireiscannotconfrim();
			}	
		});
		
	}