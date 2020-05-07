	function showadd() {
		var showblock=js$("addUrlBlock").style.display="block";
	}
	
	function modify(itself){
	var tno=itself.id.substr(2);
	var tno=itself.id.substr(2);
	var tdurl=js$("ulrtd_"+tno);
	var tdname=js$("nametd_"+tno);
	exurl=js$("ulrtdval_"+tno).value;
	exname=js$("nametdval_"+tno).value;
	tdurl.innerHTML="<input type=\"text\" value=\""+exurl+"\" id=\"ulrtdval_"+tno+"\" name=\"url\" />"
					+"<input type=\"hidden\" value=\""+exurl+"\" id=\"ulrtdval2_"+tno+"\" />";
	tdname.innerHTML="<input type=\"text\" value=\""+exname+"\" id=\"ametdval_"+tno+"\" name=\"pdfname\" />"
	+"<input type=\"hidden\" value=\""+exname+"\" id=\"nametdval2_"+tno+"\" />";	
	itself.style.display="none";
	js$("r_"+tno).style.display="inline";
	}
	
	function deleterow(itself){
		var tno=itself.id.substr(2);
		var tdurl=js$("ulrtd_"+tno);
		var tdname=js$("nametd_"+tno);
		exurl=js$("ulrtdval2_"+tno).value;
		exname=js$("nametdval2_"+tno).value;
		tdurl.innerHTML="<input type=\"hidden\" value=\""+exurl+"\" id=\"ulrtdval2_"+tno+"\" />";
		tdname.innerHTML="<input type=\"hidden\" value=\""+exname+"\" id=\"nametdval2_"+tno+"\" />";
		itself.style.display="none";
		js$("r_"+tno).style.display="inline";
	}
	
	function canceldo(itself){
		var tno=itself.id.substr(2);
		var tdurl=js$("ulrtd_"+tno);
		var tdname=js$("nametd_"+tno);
		exurl=js$("ulrtdval2_"+tno).value;
		exname=js$("nametdval2_"+tno).value;
		tdurl.innerHTML=exurl+"<input type=\"hidden\" value=\""+exurl+"\" id=\"ulrtdval_"+tno+"\" name=\"pdfname\" />"
							+"<input type=\"hidden\" value=\""+exurl+"\" id=\"ulrtdval2_"+tno+"\" />";
		tdname.innerHTML=exname+"<input type=\"hidden\" value=\""+exname+"\" id=\"nametdval_"+tno+"\" name=\"url\" />"
							+"<input type=\"hidden\" value=\""+exname+"\" id=\"nametdval2_"+tno+"\" />";
		itself.style.display="none";
		if(js$("d_"+tno).style.display=="none"){
			js$("d_"+tno).style.display="inline";
		}
		if(js$("m_"+tno).style.display=="none"){
			js$("m_"+tno).style.display="inline";
		}
	}