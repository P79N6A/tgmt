/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbds.ctrl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.tbds.ctrl.validator.MpsValidator;
import com.tbds.model.eo.Mps;
import com.tbds.service.MpsService;
import com.tbds.util.StrUtil;

/**
 *
 * @author totan
 */
public class MpsController extends TbdsBaseController {

	private static final Logger log = Logger.getLogger(MpsController.class);

	/**
	 * index方法实现两个功能： （1）默认查询所有设备 （2）根据条件查询并分页处理
	 */
	public void index() {

		int currentPageIndex = getParaToInt(0, 1);
//		String trainType = getPara(1);
//		String keyword = getPara(2);
//
//		if (StrUtil.isBlank(trainType)) {
//			trainType = getPara("qTrainType");
//		}
//		if (StrUtil.isBlank(keyword)) {
//			keyword = getPara("qKeyword");
//		}
		
		String trainType = "all";//getPara("qTrainType");
		String keyword = getPara("qKeyword");


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
			setAttr("mpsPage", MpsService.search(currentPageIndex, 10, null, keyword));
			//setAttr("qTrainType", trainType);
			setAttr("qKeyword", keyword);

		} else {
			setAttr("mpsPage", MpsService.paginate(currentPageIndex, 10));
		}
		
		//用于判断是否需要重新生成
		setAttr("isNeededRegeneratMpsDataFile", MpsService.isNeededRegeneratMpsDataFile);
		
		render("index.html");
	}

	public void status() {
		setAttr("mpsPage", MpsService.getMpsListStatus());
		render("status.html");

	}

	public void edit() {
		setAttr("mps", MpsService.findById(getParaToInt()));
		render("edit.html");
	}

	public void add() {
		render("add.html");
	}
	
	@Before(MpsValidator.class)
	public void save() {
		JSONObject resp = new JSONObject();
		int code = -1;
		
		Mps mps = getModel(Mps.class, "mps");
		
		//默认Train Type设置为T
		String trainType = "T";//mps.get("train_type");
		
		String trainNum = mps.get("train_num");
		String abMarker = mps.get("ab_marker");
		String hostIP = mps.get("host_ip");
		String hostPort = mps.get("host_port");
		
		if(StrKit.isBlank(trainNum) || StrKit.isBlank(abMarker) || StrKit.isBlank(hostIP) || StrKit.isBlank(hostPort))  {
			resp.put("msg", "保存MPS实例失败，请检查!");
			resp.put("code", code);
			renderJson(resp);
			return;
		}
		
		String desc = "";
		
		//如果设置列车号不以0开头，则自动添加0
		if(!trainNum.startsWith("0")) {
			desc = trainType + "0" + trainNum;
		} else {
			desc = trainType + "" + trainNum;
		}
		
		//设置车载单元完整的名字
		String fullname = desc + abMarker;
		
		//检查是否已存在相同的车载单元名称
		boolean exist = MpsService.isMpsExist(fullname);
		//若已存在MPS，则返回失败提示
		if(exist) {
			resp.put("code", 0);
			resp.put("msg", "添加失败，已存在相同车载单元名称的MPS实例!");
			renderJson(resp);
			return;
		}
		
		
		mps.set("train_type", trainType);//默认设定上面的类型
		mps.set("fullname", fullname);
		mps.set("desc", desc);
		mps.set("status", 1);
		

		PropKit.use(com.tbds.util.Constants.CONFIG_FILE);
		String statusPath = PropKit.get(com.tbds.util.Constants.MPS_STATUS_LOG_PATH);
		mps.set("client_state_file", statusPath);
		mps.set("client_state_log", statusPath);

		boolean flag = mps.save();
		
		if (flag) {
			Integer id = mps.getInt("id");
			
			MpsService.isNeededRegeneratMpsDataFile = true;

			code = 1;
			resp.put("msg", "成功添加MPS服务器实例!");
			resp.put("id", id);
			
		} else {
			code = 0;
			resp.put("msg", "添加MPS服务器实例失败!");
			
			System.err.println("save failed in MpsController");
		}
		
		resp.put("code", code);
		renderJson(resp);
	}
	
	/**
	 * 更新操作
	 */
	@Before(MpsValidator.class)
	public void update() {
		JSONObject resp = new JSONObject();
		int code = -1;
		
		Mps mps = getModel(Mps.class, "mps");
		
		Integer currentMpsId = mps.getInt("id");

		//默认Train Type设置为T
		String trainType = "T";//mps.get("train_type");
		
		String trainNum = mps.get("train_num");
		String abMarker = mps.get("ab_marker");
		String hostIP = mps.get("host_ip");
		String hostPort = mps.get("host_port");
		
		if(StrKit.isBlank(trainNum) || StrKit.isBlank(abMarker) || StrKit.isBlank(hostIP) || StrKit.isBlank(hostPort))  {
			resp.put("msg", "保存MPS实例失败，请检查!");
			resp.put("code", code);
			renderJson(resp);
			return;
		}
		
		String desc = "";
		
		//如果设置列车号不以0开头，则自动添加0
		if(!trainNum.startsWith("0")) {
			desc = trainType + "0" + trainNum;
		} else {
			desc = trainType + "" + trainNum;
		}
		
		//设置车载单元完整的名字
		String fullname = desc + abMarker;
		
		//检查是否已存在相同的车载单元名称
		boolean exist = MpsService.isMpsExist(fullname, currentMpsId);
		//若已存在MPS，则返回失败提示
		if(exist) {
			resp.put("code", 0);
			resp.put("msg", "保存失败，更新信息与已存在的其他车载单元名称相同!");
			renderJson(resp);
			return;
		}
		
		
		mps.set("train_type", trainType);//默认设定上面的类型
		mps.set("fullname", fullname);
		mps.set("desc", desc);
		mps.set("status", 1);

		boolean flag = mps.update();

		if (flag) {
			
			MpsService.isNeededRegeneratMpsDataFile = true;
			code = 1;
			resp.put("msg", "成功保存MPS服务器实例!");
			resp.put("id", currentMpsId);
			
		} else {
			code = 0;
			resp.put("msg", "保存MPS服务器实例失败，请检查!");
			
			System.err.println("update failed in MpsController");
			
		}
		
		resp.put("code", code);
		renderJson(resp);

	}

	/**
	 * 删除操作
	 */
	public void delete() {
		JSONObject resp = new JSONObject();
		
		Mps mps = getModel(Mps.class, "mps");
		
		int code = -1;
		
		if(mps == null) {
			resp.put("msg", "删除MPS失败，请检查!");
			resp.put("code", code);
			renderJson(resp);
			return;
		}
		
		boolean flag = mps.delete();

		if (flag) {
			MpsService.isNeededRegeneratMpsDataFile = true;
			resp.put("msg", "成功删除MPS!");
			code = 1;
		} else {
			resp.put("msg", "删除MPS失败，请检查!");
			code = 0;
		}
		resp.put("code", code);
		renderJson(resp);
	}
	
	/**
	 * 用于生成MPS数据文件，用于ping检测MPS server实例的状态
	 */
	public void regenerate() {
		JSONObject resp = new JSONObject();
		int code = 0;
		
		boolean result = MpsService.regenerateMpsListDataFile();
		
		
		if(result) {//文件成功产生
			MpsService.isNeededRegeneratMpsDataFile = false;
			resp.put("code", 1);
			resp.put("msg", "文件成功产生！");
		} else {
			resp.put("code", code);
			resp.put("msg", "生成MPS数据列表文件失败，请检查！");
		}
		
		renderJson(resp);
		
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
