package com.tbds.service;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;
import com.tbds.model.eo.JobTemplate;
import com.tbds.model.eo.Server;
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
			whereSql += " and ( ";
			whereSql += " or template_name like concat('%', ?, '%')";
			paras.add(keyword);
			
			whereSql += " or description like concat('%', ?, '%')";
			paras.add(keyword);
			
			whereSql += " or file_path like concat('%', ?, '%')";
			paras.add(keyword);
			whereSql += " ) ";
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
	 * 出现错误时，需要删除已上传的临时文件
	 * @param templateFile
	 * @return
	 */
	public static boolean removeTempUploadFile(UploadFile templateFile) {
		boolean isRemoved = false;
		String uploadFilePath = templateFile.getUploadPath();
		String uploadFileName = templateFile.getFileName();
		//String originalUploadFileName = templateFile.getOriginalFileName();
		
		File originalTemplateFile = new File(uploadFilePath + "/" + uploadFileName);
		if(!originalTemplateFile.exists()) {
			isRemoved = true;
		} else {
			isRemoved = originalTemplateFile.delete();
		}
		return isRemoved;
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
			//业务异常，同时需要删除已上传的文件
			removeTempUploadFile(templateFile);
			
			resp.put("code", -1);
			resp.put("msg", "模板xml文件找不到，请检查！");
			return resp;
		}
		
		//移动xml文件到指定目录
		
		String templateNewFileName = ("config".equals(type)?"job_chain":type) + "_" +  name + "." + type + ".xml";
		String absoluteTemplateNewFilePath = templateFolderPath + "/" + templateNewFileName;
		boolean moveFile = originalTemplateFile.renameTo(new File(absoluteTemplateNewFilePath));
		
		if(!moveFile) {
			//业务异常，同时需要删除已上传的文件
			removeTempUploadFile(templateFile);
			
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
			//业务异常，同时需要删除已上传的文件
			removeTempUploadFile(templateFile);
			
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
			String templateNewFileName = ("config".equals(type)?"job_chain":type) + "_" +  name + "." + type + ".xml";
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
			//业务异常，同时需要删除已上传的文件
			removeTempUploadFile(templateFile);
			
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
	public static JSONObject saveTemplateAttachmentByText(long templateId, String xmlContent) {
		JSONObject resp = new JSONObject();
		
		JobTemplate jt = findTemplateById(templateId);
		
		if(jt == null) {
			resp.put("code", -1);
			resp.put("msg", "更新模板xml内容失败，请检查！");
			return resp;
		}
		
		String absoluteFilePath = jt.getFilePath();
		File xmlFile = new File(absoluteFilePath);
		
		//将字符串写入到xml文件中
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        DocumentBuilder builder;  
        try  
        {  
            builder = factory.newDocumentBuilder();
            //将xml转换成document
            Document xmlDocument = builder.parse(new InputSource( new StringReader(xmlContent))); 
            
            //TransformerFactory实例
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            //修改xml编码
            //transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
            DOMSource source = new DOMSource(xmlDocument);
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(source, result);//写入xml文件
            resp.put("code", 1);
            resp.put("msg", "成功保存模板xml文件！");
            
        } catch (Exception e) {  
            e.printStackTrace();  
            resp.put("code", 0);
            resp.put("msg", "保存模板xml文件出现异常，请检查！");
        }
		
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
			
			String filePath = jt.getFilePath();
			
			File templateFile = new File(filePath);
			
			//记得删除记录的同时，需要删除对应的模板文件
			boolean flag = Db.tx(() -> {
				
				if(templateFile.exists()) {
					templateFile.delete();
				}
				
				jt.delete();
				
				return true;
			});
			
			
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
	
	
	private static Page<Server> execSearchServer(int pageNumber, int pageSize, 
			String serverType, String keyword) {
		
		Page<Server> result = null;
		
		List<Object> paras = new  ArrayList<Object>();
		
		String whereSql = " where 1=1 ";
		
		if(StrUtil.notBlank(serverType) && !"all".equalsIgnoreCase(serverType)) {
			whereSql += " and catalog = ? ";
			paras.add(serverType);
		} 
		
		if(StrUtil.notBlank(keyword)) {
			whereSql += " and ( ";
			whereSql += " name like concat('%', ?, '%')";
			paras.add(keyword);
			
			whereSql += " or host like concat('%', ?, '%')";
			paras.add(keyword);
			
			whereSql += " or port like concat('%', ?, '%')";
			paras.add(keyword);
			
			whereSql += " or description like concat('%', ?, '%')";
			paras.add(keyword);
			whereSql += " ) ";
			
		}
		
		String from_whereSql = " from tbds_server " + whereSql + " order by id asc" ;
		
		if(null == paras || paras.isEmpty()) {
			result = Server.dao.paginate(pageNumber, pageSize, "select *", from_whereSql);
		} else {
			if(paras.size() == 1) {
				result = Server.dao.paginate(pageNumber, pageSize, "select *", from_whereSql, paras.get(0));
			} else if(paras.size() == 2) {
				result = Server.dao.paginate(pageNumber, pageSize, "select *", from_whereSql, paras.get(0), paras.get(1));
			} else if(paras.size() == 3) {
				result = Server.dao.paginate(pageNumber, pageSize, "select *", from_whereSql, paras.get(0), paras.get(1), paras.get(2));
			} else if(paras.size() == 4) {
				result = Server.dao.paginate(pageNumber, pageSize, "select *", from_whereSql, paras.get(0), paras.get(1), paras.get(2), paras.get(3));
			} else if(paras.size() == 5) {
				result = Server.dao.paginate(pageNumber, pageSize, "select *", from_whereSql, paras.get(0), paras.get(1), paras.get(2), paras.get(3), paras.get(4));
			}
		}
		
		return result;
	}
	
	/**
	 * 查找Server By ID
	 * @param serverId
	 * @return
	 */
	public static Server findServerById(long serverId) {
		return Server.dao.findById(serverId);
	}
	
	/**
	 * 查询Server
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public static Page<Server> serverPaginate(int pageNumber, int pageSize, String serverType, String keyword) {
		return execSearchServer(pageNumber, pageSize, serverType, keyword);
	}
	
	public static JSONObject saveJobServer(String name, String host, int port, String catalog, String description) {
		JSONObject resp = new JSONObject();
		
		Server dao = new Server();
		dao.setName(name);
		dao.setHost(host);
		dao.setPort(port);
		dao.setCatalog(catalog);
		dao.setDescription(description);
		
		boolean flag = dao.save();
		if(flag) {
			resp.put("id", dao.getId());
			resp.put("code", 1);
			resp.put("msg", "添加Server成功！");
		} else {
			resp.put("code", 0);
			resp.put("msg", "添加Server失败，请检查！");
		}
		return resp;
		
	}
	
	public static JSONObject updateJobServer(Long id, String name, String host, int port, String catalog, String description) {
		JSONObject resp = new JSONObject();
		
		if(id > 0) {
			
			Server jobSerer = findServerById(id);
			
			if(jobSerer == null) {
				resp.put("code", -1);
				resp.put("msg", "更新失败,找不到对应的Server服务！");
				return resp;
			}
			
			jobSerer.setName(name);
			jobSerer.setHost(host);
			jobSerer.setPort(port);
			jobSerer.setCatalog(catalog);
			jobSerer.setDescription(description);
			
			boolean flag = jobSerer.update();
			
			if(flag) {
				resp.put("code", 1);
				resp.put("msg", "更新Server成功");
			} else {
				resp.put("code", 0);
				resp.put("msg", "更新Server失败,请检查！");
			}
			
		} else {
			resp.put("code", 0);
			resp.put("msg", "更新失败,找不到对应的Server服务！");
		}
		
		return resp;
		
	}
	
	public static JSONObject deleteServerById(long serverId) {
		JSONObject resp = new JSONObject();
		
		Server jobServer = findServerById(serverId);
		if(jobServer != null) {
			boolean flag = jobServer.delete();
			
			if(flag) {
				resp.put("code", 1);
				resp.put("msg", "删除Server服务成功！");
				return resp;
			}
			
		} else {
			resp.put("code", -1);
			resp.put("msg", "删除Server服务失败！");
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
