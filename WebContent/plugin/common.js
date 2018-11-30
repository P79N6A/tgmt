
//检查是否是数字
function isNumeric(a){
	var reg = /^(-|+)?d+(.d+)?$/;
	return(reg.test(a));
}

//检查是否为正数
function isUnsignedNumeric(a){
	var reg = /^d+(.d+)?$/;
	return reg.test(a);
}

//检查是否为整数
function isInteger(a){
	var reg = /^(-|+)?d+$/;
	return reg.test(a);
}

//检查是否为正整数
function isUnsignedInteger(a){
	var reg = /^d+$/;
	return reg.test(a);
}
