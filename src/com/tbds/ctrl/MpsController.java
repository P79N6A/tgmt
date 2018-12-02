/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbds.ctrl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.log4j.Logger;

import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.kit.PropKit;
import com.tbds.ctrl.validator.MpsValidator;
import com.tbds.model.eo.Mps;
import com.tbds.service.MpsService;
import com.tbds.util.StrUtil;

/**
 *
 * @author totan
 */
public class MpsController extends Controller {

	private static final Logger log = Logger.getLogger(MpsController.class);

	MpsService service = new MpsService();

	/**
	 * index方法实现两个功能： （1）默认查询所有设备 （2）根据条件查询并分页处理
	 */
	public void index() {

		int currentPageIndex = getParaToInt(0, 1);
		String trainType = getPara(1);
		String keyword = getPara(2);

		if (StrUtil.isBlank(trainType)) {
			trainType = getPara("qTrainType");
		}

		if (StrUtil.isBlank(keyword)) {
			keyword = getPara("qKeyword");
		}

		if (StrUtil.notBlank(keyword) || StrUtil.notBlank(trainType)) {
			// 处理keyword传入：解码处理（中文keyword）
			if (StrUtil.notBlank(keyword)) {
				try {
					keyword = URLDecoder.decode(keyword, "UTF-8");
				} catch (UnsupportedEncodingException ex) {
					log.error(ex.getMessage());
				}
			}
			// 通过keyword进一步搜索查询
			setAttr("mpsPage", service.search(getParaToInt(0, 1), 10, trainType, keyword));
			setAttr("qTrainType", trainType);
			setAttr("qKeyword", keyword);

		} else {
			setAttr("mpsPage", service.paginate(getParaToInt(0, 1), 10));
		}
		render("index.html");
	}

	public void status() {
		setAttr("mpsPage", service.getMpsListStatus());
		render("status.html");

	}

	public void edit() {
		setAttr("mps", service.findById(getParaToInt()));
		render("edit.html");
	}

	public void add() {
		render("add.html");
	}
	
	@Before(MpsValidator.class)
	public void save() {
		Mps mps = getModel(Mps.class, "mps");

		Integer id = mps.getInt("id");
		String trainType = mps.get("train_type");
		String trainNum = mps.get("train_num");
		String abMarker = mps.get("ab_marker");
		String hostIP = mps.get("host_ip");
		String hostPort = mps.get("host_port");

		// TODO: 判断是否存在重复的IP地址与端口

		mps.set("fullname", trainType + "_" + trainNum);
		mps.set("desc", trainType + "_" + trainNum + abMarker);
		mps.set("status", 1);

		PropKit.use(com.tbds.util.Constants.CONFIG_FILE);
		String statusPath = PropKit.get(com.tbds.util.Constants.MPS_STATUS_LOG_PATH);
		mps.set("client_state_file", statusPath);
		mps.set("client_state_log", statusPath);

		boolean flag = mps.save();
		if (flag) {
			redirect("/mps");
		} else {
			System.err.println("save failed in MpsController");
			this.renderError(500);
		}
	}
	
	/**
	 * 更新操作
	 */
	@Before(MpsValidator.class)
	public void update() {
		Mps mps = getModel(Mps.class, "mps");

		Integer id = mps.getInt("id");
		String trainType = mps.get("train_type");
		String trainNum = mps.get("train_num");
		String abMarker = mps.get("ab_marker");
		String hostIP = mps.get("host_ip");
		String hostPort = mps.get("host_port");

		// TODO: 判断是否存在重复的IP地址与端口，方便排除问题
		mps.set("fullname", trainType + "_" + trainNum);
		mps.set("desc", trainType + "_" + trainNum + abMarker);
		mps.set("status", 1);

		boolean flag = mps.update();

		if (flag) {
			redirect("/mps");
		} else {
			System.err.println("update failed in MpsController");
			this.renderError(500);
		}

	}

	/**
	 * 删除操作
	 */
	public void delete() {
		// 直接删除掉
		Integer id = getParaToInt();
		System.out.println("id = " + id);
		boolean flag = service.deleteById(id);

		if (flag) {
			renderText("1");// del success
		} else {
			renderText("0");// del failed

		}
	}

	/*
	 * public void search() { String trainType = this.getPara("qTrainType"); String
	 * keyword = this.getPara("qKeyword");
	 * 
	 * System.out.println("trainType = " + trainType);
	 * System.out.println("keywords = " + keyword);
	 * 
	 * //查询并分页处理 setAttr("mpsPage", service.search(getParaToInt(0, 1), 10,
	 * trainType, keyword));
	 * 
	 * setAttr("qTrainType", trainType); setAttr("qKeyword", keyword);
	 * 
	 * render("search.html"); }
	 */

//	public void disable() {
//
//	}
//
//	public void enable() {
//
//	}

}
