/*
* @description generate menu use json data, the json data format please check the example data in the function
* @parameter appName: application base path
*  			 container:menu container,
* 			 json: menu json data
* @author dtx
* @date 2014/09/11
* 
*/

jQuery.Menu = function(appName, container, json){
	if(json==null || json=='') {
	  return false;
        }
	var obj = eval(json);
	//var str = ""; //
    var str = '<li><a href="'+appName+'/home"><span class="am-icon-home am-icon-fw"></span> 首页</a></li>';
	$(obj).each(function(idx){
		var modu = obj[idx];
		
		str += '<li class="admin-parent">\n';
		str += '<a class="am-cf" data-am-collapse="{target: \'#md'+modu.moduleId+'\'}"><span class="'+modu.moduleIcon+' am-icon-fw"></span> ';
		str += modu.moduleName;
		str += '<span class="am-icon-angle-right am-fr am-margin-right"></span></a>';
		str += '<ul class="am-list am-collapse admin-sidebar-sub" id="md'+modu.moduleId+'">';
		if(typeof (modu.menus) == 'object'){
			$(modu.menus).each(function(index){
				var menu = modu.menus[index];
				var sybol = "";
				var position = menu.url.indexOf("?");
				if(position>-1 && position<(menu.url.length-1)){
					sybol = "&";
				}else{
					sybol = "?";
				}
				str += '\t<li><a href="'+appName+menu.url+sybol+'m='+menu.menuId+'" class="am-cf"><span class="'+menu.icon+'"></span> '+menu.menuName+'</a></li>';
			});
		}
		str += "</ul></li>";
	});
	
	$(container).append(str);

};
