package com.tbds.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;
import com.tbds.model.eo.JobTemplate;
import com.tbds.util.FileUtil;
import com.tbds.util.StrUtil;

public class JobManagementService {
	/**
	 * Template文件储存路径
	 */
	public static String getTemplateFileStoredPath() {
		PropKit.use(com.tbds.util.Constants.CONFIG_FILE);
		return PropKit.get(com.tbds.util.Constants.SCHEDULER_JOB_TEMPLATE_PATH);
	}
	
	/**
	 * Job模板分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public static Page<JobTemplate> templatePaginate(int pageNumber, int pageSize) {
		return JobTemplate.dao.paginate(pageNumber, pageSize, "select *", " from tbds_job_template order by id asc");
	}
	
	public static Page<JobTemplate> templatePaginate(int pageNumber, int pageSize, 
			String templateType, String templateApplicantion, String keyword) 
	{
		return execSearchJobTemplate(pageNumber, pageSize, templateType, templateApplicantion, keyword);
		
	}
	
	private static Page<JobTemplate> execSearchJobTemplate(int pageNumber, int pageSize, 
			String templateType, String templateApplicantion, String keyword) {
		
		Page<JobTemplate> result = null;
		
		List<Object> paras = new  ArrayList<Object>();
		
		String whereSql = " where 1=1 ";
		
		if(StrUtil.notBlank(templateType) && !"all".equalsIgnoreCase(templateType)) {
			whereSql += " and template_type = ? ";
			paras.add(templateType);
			
		} 
		
		if(StrUtil.notBlank(templateApplicantion) && !"all".equalsIgnoreCase(templateApplicantion)) {
			whereSql += " and template_application = ? ";
			paras.add(templateApplicantion);
		}
		
		if(StrUtil.notBlank(keyword)) {
			whereSql += " and template_name like concat('%', ?, '%')";
			paras.add(keyword);
			
			whereSql += " and description like concat('%', ?, '%')";
			paras.add(keyword);
			
			whereSql += " and file_path like concat('%', ?, '%')";
			paras.add(keyword);
		}
		
		String from_whereSql = " from tbds_job_template " + whereSql + " order by id asc" ;
		
		if(null == paras || paras.isEmpty()) {
			result = JobTemplate.dao.paginate(pageNumber, pageSize, "select *", from_whereSql);
		} else {
			if(paras.size() == 1) {
				result = JobTemplate.dao.paginate(pageNumber, pageSize, "select *", from_whereSql, paras.get(0));
			} else if(paras.size() == 2) {
				result = JobTemplate.dao.paginate(pageNumber, pageSize, "select *", from_whereSql, paras.get(0), paras.get(1));
			} else if(paras.size() == 3) {
				result = JobTemplate.dao.paginate(pageNumber, pageSize, "select *", from_whereSql, paras.get(0), paras.get(1), paras.get(2));
			} else if(paras.size() == 4) {
				result = JobTemplate.dao.paginate(pageNumber, pageSize, "select *", from_whereSql, paras.get(0), paras.get(1), paras.get(2), paras.get(3));
			} else if(paras.size() == 5) {
				result = JobTemplate.dao.paginate(pageNumber, pageSize, "select *", from_whereSql, paras.get(0), paras.get(1), paras.get(2), paras.get(3), paras.get(4));
			}
		}
		
		return result;
	}
	
	/**
	 * 通过Template ID来查询Template信息
	 * @param templateId
	 * @return
	 */
	public static JobTemplate findTemplateById(long templateId) {
		return JobTemplate.dao.findById(templateId);
	}
	
	/**
	 * 保存模板 + 附件
	 * @param name
	 * @param type
	 * @param application
	 * @param description
	 * @param templateFile
	 * @return json object
	 */
	public static JSONObject saveTemplateWithAttachment(String name, String type, 
			String application, String description, UploadFile templateFile) {
		
		JSONObject resp = new JSONObject();
		
		String uploadFilePath = templateFile.getUploadPath();
		String uploadFileName = templateFile.getFileName();
		//String originalUploadFileName = templateFile.getOriginalFileName();
		
		String templateFolderPath = getTemplateFileStoredPath();
		
		File templateFolder = new File(templateFolderPath);
		if(!templateFolder.exists()) {
			templateFolder.mkdirs();
		}
		
		//检查上传源文件是否存在，不存在，则报错！
		File originalTemplateFile = new File(uploadFilePath + "/" + uploadFileName);
		if(!originalTemplateFile.exists()) {
			resp.put("code", -1);
			resp.put("msg", "模板xml文件找不到，请检查！");
			return resp;
		}
		
		//移动xml文件到指定目录
		String templateNewFileName = type + "_" +  name + "." + type + ".xml";
		String absoluteTemplateNewFilePath = templateFolderPath + "/" + templateNewFileName;
		boolean moveFile = originalTemplateFile.renameTo(new File(absoluteTemplateNewFilePath));
		
		if(!moveFile) {
			resp.put("code", 0);
			resp.put("msg", "生成模板正式xml文件失败，请检查！");
			return resp;
		}
		
		JobTemplate dao = new JobTemplate();
		dao.setTemplateName(name);
		dao.setTemplateType(type);
		dao.setTemplateApplication(application);
		dao.setDescription(description);
		dao.setFilePath(absoluteTemplateNewFilePath);
		
		boolean flag = dao.save();
		if(flag) {
			resp.put("id", dao.getId());
			resp.put("code", 1);
			resp.put("msg", "模板保存成功！");
		} else {
			resp.put("code", 0);
			resp.put("msg", "模板保存失败，请检查！");
		}
		
		return resp;
	}
	
	/**
	 * 仅保存模板基本信息（但需要重新对模板文件名称进行重命名）
	 * @param name
	 * @param type
	 * @param application
	 * @param description
	 * @return
	 */
	public static JSONObject saveOnlyTemplateInfo(long id, String name, String type, 
			String application, String description) {
		
		JSONObject resp = new JSONObject();
		
		if(id > 0) {
			
			JobTemplate jt = findTemplateById(id);
			
			if(jt == null) {
				resp.put("code", -1);
				resp.put("msg", "更新失败,找不到对应的模板！");
				return resp;
			}
			
			
			
			//判断是否存在原有文件
			String templateFilePath = null;
			if(StrUtil.notBlank(jt.getFilePath())) {
				templateFilePath = jt.getFilePath();
			}
			
			String templateFolderPath = getTemplateFileStoredPath();
			String templateNewFileName = type + "_" +  name + "." + type + ".xml";
			String absoluteTemplateNewFilePath = templateFolderPath + "/" + templateNewFileName;
			
			//重新命名xml文件
			File originalTemplateFile = new File(templateFilePath);
			if(originalTemplateFile.exists()) {
				boolean moveFile = originalTemplateFile.renameTo(new File(absoluteTemplateNewFilePath));
				if(!moveFile) {
					resp.put("code", 0);
					resp.put("msg", "重命名模板xml文件失败，请检查！");
					return resp;
				}
			}
			
			
			jt.setTemplateName(name);
			jt.setTemplateType(type);
			jt.setTemplateApplication(application);
			jt.setFilePath(absoluteTemplateNewFilePath);//更新为原有的模板名称
			jt.setDescription(description);
			
			boolean flag = jt.update();
			
			if(flag) {
				resp.put("code", 1);
				resp.put("msg", "更新成功");
			} else {
				resp.put("code", 0);
				resp.put("msg", "更新失败,请检查！");
			}
			
		} else {
			resp.put("code", 0);
			resp.put("msg", "更新失败,找不到对应的模板！");
		}
		
		
		return resp;
	}
	
	/**
	 * 通过上传文件的形式进行更新模板
	 * @param templateFile
	 * @return
	 */
	public static JSONObject saveOnlyTemplateAttachment(long id, UploadFile templateFile) {
		JSONObject resp = new JSONObject();
		
		JobTemplate jt = findTemplateById(id);
		
		if(jt == null) {
			resp.put("code", -1);
			resp.put("msg", "更新模板xml文件失败！");
			return resp;
		}
		
		String uploadFilePath = templateFile.getUploadPath();
		String uploadFileName = templateFile.getFileName();
		//String originalUploadFileName = templateFile.getOriginalFileName();
		
		String templateFolderPath = getTemplateFileStoredPath();
		
		File templateFolder = new File(templateFolderPath);
		if(!templateFolder.exists()) {
			templateFolder.mkdirs();
		}
		
		//检查上传源文件是否存在，不存在，则报错！
		File uploadTemplateFile = new File(uploadFilePath + "/" + uploadFileName);
		if(!uploadTemplateFile.exists()) {
			resp.put("code", -1);
			resp.put("msg", "模板xml文件找不到，请检查！");
			return resp;
		}
		
		String type = jt.getTemplateType();
		String name = jt.getTemplateName();
		
		//移动xml文件到指定目录
		String templateNewFileName = type + "_" +  name + "." + type + ".xml";
		String absoluteTemplateNewFilePath = templateFolderPath + "/" + templateNewFileName;
		
		//删除原有文件后再进行重新命名
		File existFile = new File(absoluteTemplateNewFilePath);
		if(existFile.exists()) {
			existFile.delete();
		}
		
		//将上传的文件重新移动到目标目录下
		boolean moveFile = uploadTemplateFile.renameTo(new File(absoluteTemplateNewFilePath));
		
		if(!moveFile) {
			resp.put("code", 0);
			resp.put("msg", "生成模板正式xml文件失败，请检查！");
			return resp;
		}
		
		//记得更新xml文件的路径字段
		jt.setFilePath(absoluteTemplateNewFilePath);
		
		//记得保存数据
		boolean flag = jt.update();
		
		if(flag == true) {
			resp.put("code", 1);
			resp.put("msg", "模板附件XML更新成功！");
		} else {
			resp.put("code", 0);
			resp.put("msg", "模板附件XML更新失败！");
		}
		
		return resp;
	}
	
	/**
	 * 通过模板ID进行加载XML文件
	 * @param templateId
	 * @return
	 */
	public static JSONObject loadingTemplateAttachment(long templateId) {
		JSONObject resp = new JSONObject();
		
		JobTemplate jt = findTemplateById(templateId);
		
		if(jt == null) {
			resp.put("code", -1);
			resp.put("msg", "找不到对应模板，加载xml失败！");
			return resp;
		}
		
		String absoluteFilePath = jt.getFilePath();
		File xmlFile = new File(absoluteFilePath);
		
		if(!xmlFile.exists()) {
			resp.put("code", -1);
			resp.put("msg", "找不到对应模板xml文件，加载xml失败！");
			return resp;
		}
		
		resp.put("code", 1);
		resp.put("xml", FileUtil.readFile2String(absoluteFilePath));
		resp.put("msg", "加载xml文件成功");
		
		return resp;
	}
	
	/**
	 * 根据模板ID获取模板xml文件路径
	 * @param templateId
	 * @return
	 */
	public static String getTemplateAttachmentFilePath(long templateId) {
		JobTemplate jt = findTemplateById(templateId);
		
		if(jt != null) {
			return jt.getFilePath();
		} else {
			return null;
		}
	}
	
	
	/**
	 * 通过在线编辑XML的形式进行保存模板文件
	 * @param templateId
	 * @return
	 */
	public static JSONObject saveTemplateAttachmentByText(long templateId) {
		JSONObject resp = new JSONObject();
		
		
		
		return resp;
	}
	
	/**
	 * 执行删除Job模板操作
	 * @param templateId
	 * @return
	 */
	public static JSONObject deleteTemplateById(long templateId) {
		JSONObject resp = new JSONObject();
		
		JobTemplate jt = findTemplateById(templateId);
		if(jt != null) {
			boolean flag = jt.delete();
			
			if(flag) {
				resp.put("code", 1);
				resp.put("msg", "模板删除成功！");
				return resp;
			}
			
		} else {
			resp.put("code", -1);
			resp.put("msg", "模板删除失败！");
		}
		
		return resp;
		
		
	}

	public static boolean generateMpsJobFromTemplateXml(String type) {
		boolean isGenerated = false;

		if (StrUtil.notBlank(type)) {// 生成指定类型的xml文件

			boolean all = false;

			if ("all".equalsIgnoreCase(type)) {
				all = true;
			}

			if ("job".equalsIgnoreCase(type) || all) {
				// generate job xml

			} else if ("chain".equalsIgnoreCase(type) || all) {
				// generate job chain xml

			} else if ("order".equalsIgnoreCase(type) || all) {
				// generate job chain order xml

			}

		} else {
			// do nothing

		}

		return isGenerated;
	}

}
