function toexportstep2() {
		var xhr = getXhr();
		xhr.open('post', 'toexportstep2', true);
		xhr.setRequestHeader('Content-Type',
				'application/x-www-form-urlencoded');
		xhr.onreadystatechange = function() {
			if (xhr.readyState == 4) {
				alert("请求已发送，请等待，或刷新页面");
			}
		};
		xhr.send(null);

	}

	function looklog(itself) {
		var jqiself=$(itself);
		var filename=jqiself.html();
		$post('logread?type=filelist&filename='+filename,{},function(data){
			jqiself.html("");
		});
	}

	function getXhr() {
		var xhr = null;
		if (window.XMLHttpRequest) {
			xhr = new XMLHttpRequest();
		} else {
			xhr = new ActiveXObject('Microsoft.XMLHttp');
		}
		return xhr;
	}

	function js$(id) {
		return document.getElementById(id);
	}

	function $F(id) {
		return document.getElementById(id).value;
	}
	
	function closeit() {
		js$('showlog').style.display = "none";
	}

function checkmission() {
    var go=confirm("确认提交么?");
    if(!go){
        return false;
    }
}


	