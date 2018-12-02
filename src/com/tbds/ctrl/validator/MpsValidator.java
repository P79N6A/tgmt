package com.tbds.ctrl.validator;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import com.tbds.model.eo.Mps;

public class MpsValidator extends Validator {

	@Override
	protected void handleError(Controller arg0) {
		controller.keepModel(Mps.class);
		
		String actionKey = getActionKey();
		if (actionKey.equals("/mps/save"))
			controller.render("add.html");
		else if (actionKey.equals("/mps/update"))
			controller.render("edit.html");
	}

	@Override
	protected void validate(Controller arg0) {
		validateRequiredString("mps.host_ip", "hostIpMsg", "请输入主机IP地址!");
		validateRequiredString("mps.host_port", "hostPortMsg", "请输入端口!");
		validateRequiredString("mps.train_type", "trainTypeMsg", "请选择设备类型!");
		validateRequiredString("mps.train_num", "trainNumMsg", "请输入列车号!");
		validateRequiredString("mps.ab_marker", "abMarkerMsg", "请选择列车设备端!");
	}

}
