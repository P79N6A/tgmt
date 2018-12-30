package com.tbds.ctrl;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.upload.UploadFile;
import com.tbds.service.JobManagementService;
import com.tbds.service.MpsService;
import com.tbds.util.FileUtil;
import com.tbds.util.StrUtil;

public class JobController extends TbdsBaseController {
	
	public void index() {
		render("index.html");
	}
	
	public void template() {
		int currentPageIndex = getParaToInt(0, 1);
		
		String qTemplateType = getPara("qTemplateType");
		String qTemplateApplication = getPara("qTemplateApplication");
		String keyword = getPara("qKeyword");
		
		if (StrUtil.notBlank(keyword) || StrUtil.notBlank(qTemplateType) || StrUtil.notBlank(qTemplateApplication)) {
			// 处理keyword传入：解码处理（中文keyword）
			if (StrUtil.notBlank(keyword)) {
				try {
					keyword = URLDecoder.decode(keyword, "UTF-8");
				} catch (UnsupportedEncodingException ex) {
					System.err.println(ex.getMessage());
				}
			}
			// 通过keyword进一步搜索查询
			setAttr("jobTemplatePage", JobManagementService.templatePaginate(currentPageIndex, 10, qTemplateType, qTemplateApplication, keyword));
			
			setAttr("qTemplateType", qTemplateType);
			setAttr("qTemplateApplication", qTemplateApplication);
			setAttr("qKeyword", keyword);

		} else {
			setAttr("jobTemplatePage", JobManagementService.templatePaginate(currentPageIndex, 10));
		}
		render("template/index.html");
	}
	
	public void addtemplate() {
		
		render("template/add.html");
		
	}
	
	public void edittemplate() {
		Long id = getParaToLong();
		if(id != null && id > 0) {
			setAttr("jobTemplate", JobManagementService.findTemplateById(id));
		}
		render("template/edit.html");
	}
	
	public void savejobtemplate() {
		JSONObject resp = new JSONObject();		
		
		UploadFile ufile = this.getFile("templateFile");
		if(null == ufile || StrUtil.isBlank(ufile.getOriginalFileName())) {
			resp.put("code", -1);
			resp.put("msg", "缺少模板文件，请上传模板xml文件！");
			renderJson(resp);
			return;
		} else {
			String originalFileName = ufile.getOriginalFileName();
			if(!FileUtil.isXmlByFileExtension(originalFileName)) {
				resp.put("code", -1);
				resp.put("msg", "模板文件必需为xml文件！");
				renderJson(resp);
				return;
			}
		}
		
		String templateName = getPara("templateName");
		String templateType = getPara("templateType");
		String templateApplication = getPara("templateApplication");
		String templateDescription = getPara("templateDescription");
		
		//执行保存操作
		resp = JobManagementService.saveTemplateWithAttachment(templateName, templateType, templateApplication, templateDescription, ufile);		
		
		renderJson(resp);
	}
	
	public void downloadTemplateAttachment() {
		Long templateId = getParaToLong();
		if(templateId != null && templateId > 0) {
			String downloadFileName = JobManagementService.getTemplateAttachmentFilePath(templateId);
			if(StrUtil.notBlank(downloadFileName)) {
				//System.out.println(">>>>>>>>>>>>>>>>>" + downloadFileName);
				
				File downloadFile = new File(downloadFileName);
				
				if(downloadFile.exists()) {//渲染文件
					this.renderFile(downloadFile, templateId + ".xml");;
				} else {
					this.renderText("文件不存在，请检查！");
				}
				
			} 
		}
		
	}
	
	/**
	 * 覆盖模板附件操作
	 */
	public void overrideTemplateAttachment() {
		JSONObject resp = new JSONObject();
		
		Long templateId = this.getParaToLong("templateId");
		
		if(templateId == null || templateId == 0) {
			resp.put("code", -1);
			resp.put("msg", "缺少模板信息，更新模板xml文件失败！");
			renderJson(resp);
			return;
		}
		
		UploadFile templateFile = this.getFile("templateFile");
		if(null == templateFile || StrUtil.isBlank(templateFile.getOriginalFileName())) {
			resp.put("code", -1);
			resp.put("msg", "缺少模板文件，请上传模板xml文件！");
			renderJson(resp);
			return;
		} else {
			String originalFileName = templateFile.getOriginalFileName();
			if(!FileUtil.isXmlByFileExtension(originalFileName)) {
				resp.put("code", -1);
				resp.put("msg", "模板文件必需为xml文件！");
				renderJson(resp);
				return;
			}
			
			//执行模板文件更新替换操作
			resp = JobManagementService.saveOnlyTemplateAttachment(templateId, templateFile);
			
			System.out.println(resp.toJSONString());
			
			renderJson(resp);
		}
	}
	
	/**
	 * 保存模板信息（only）操作
	 */
	public void savejobtemplateinfo() {
		JSONObject resp = new JSONObject();
		
		Long id = getParaToLong("templateId");
		String name = getPara("templateName");
		String type = getPara("templateType");
		String application = getPara("templateApplication");
		String description = getPara("templateDescription");
		
		if(id == 0 || id == null || StrUtil.isBlank(name) || StrUtil.isBlank(type) || StrUtil.isBlank(application)) {
			resp.put("code", -1);
			resp.put("msg", "缺少必填项，请检查！");
			renderJson(resp);
			return;
		}
		
		//更新操作结果渲染返回
		resp = JobManagementService.saveOnlyTemplateInfo(id, name, type, application, description);
		renderJson(resp);
		
	}
	
	/**
	 * 删除模板请求操作
	 */
	public void deleltetemplate() {
		JSONObject resp = new JSONObject();
		Long id = getParaToLong("templateId");
		
		if(id == null || id == 0) {
			resp.put("code", -1);
			resp.put("msg", "操作失败，请检查！");
			renderJson(resp);
			return;
		}
		
		//执行查询并删除操作
		resp = JobManagementService.deleteTemplateById(id);
		renderJson(resp);
	}
	
	/**
	 * 加载模板XML到前端进行在线编辑
	 */
	public void loadTemplateXml() {
		JSONObject resp = new JSONObject();
		
		Long id = getParaToLong("templateId");
		
		if(id == null || id == 0) {
			resp.put("code", -1);
			resp.put("msg", "加载模板失败，请检查！");
			renderJson(resp);
			return;
		}
		
		resp = JobManagementService.loadingTemplateAttachment(id);
		renderJson(resp);
		
	}
	
	/**
	 * 处理页面提交上来的xml内容信息
	 */
	public void saveTemplateAttachment() {
		//String xml= HttpKit.readData(getRequest());
		JSONObject resp = new JSONObject();
		Long id = getParaToLong("templateId");
		String xml = getPara("xmlContent");
		
		if(id == null || id == 0) {
			resp.put("code", -1);
			resp.put("msg", "保存模板xml失败，请检查！");
			renderJson(resp);
			return;
		}
		
		resp = JobManagementService.saveTemplateAttachmentByText(id, xml);
		
		renderJson(resp);
		
	}
	
}
