/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbds.service;

import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.tbds.model.eo.Mps;
import com.tbds.model.eo.MpsExt;
import com.tbds.util.FileUtil;
import com.tbds.util.StrUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author totan
 */
public class MpsService {

    /**
     * 所有的 dao 对象也放在 Service 中，并且声明为 private，避免 sql 满天飞 sql 只放在业务层，或者放在外部 sql
     * 模板，用模板引擎管理： http://www.jfinal.com/doc/5-13
     */
    private static Mps dao = new Mps().dao();
    
    /**
     * 用于控制页面是否需要显示“生成MPS文件列表”
     */
    public static boolean isNeededRegeneratMpsDataFile = false;
    
    public static String getMpsStatusLogPath() {
    	PropKit.use(com.tbds.util.Constants.CONFIG_FILE);
    	String statusPath = PropKit.get(com.tbds.util.Constants.MPS_STATUS_LOG_PATH);
    	return statusPath;
    }
    
    /**
     * 获取MPS设备信息，后台分页查询
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public static Page<Mps> paginate(int pageNumber, int pageSize) {
        return dao.paginate(pageNumber, pageSize);
    }
    
    
    /**
     * 获取MPS设备信息，后台分页查询(根据关键字进行查询)
     * @param pageNumber
     * @param pageSize
     * @param trainType
     * @param keyword
     * @return
     */
    public static Page<Mps> search(int pageNumber, int pageSize, String trainType, String keyword) {
    	return dao.search(pageNumber, pageSize, trainType, keyword);
    }
    
    
    /**
     * 通过id来获取设备信息
     * @param id
     * @return
     */
    public static Mps findById(int id) {
        return dao.findById(id);
    }
    
    /**
     * 通过fullname判断是否已存在，若存在，则返回true
     * @param fullname
     */
    public static boolean isMpsExist(String fullname) {
    	boolean alreadyExist = false;
    	Mps mps = dao.findFirst("select * from tbds_mps where fullname = ?", fullname);
    	if(mps != null) {
    		alreadyExist = true;
    	}
    	
        return alreadyExist;
    }
    
    /**
     * 防止在更新时，更新到成重复的MPS实例
     * @param fullname
     * @param existId
     * @return
     */
    public static boolean isMpsExist(String fullname, int existId) {
    	boolean alreadyExist = false;
    	Mps mps = dao.findFirst("select * from tbds_mps where fullname = ? and id <> ? ", fullname, existId);
    	if(mps != null) {
    		alreadyExist = true;
    	}
    	
        return alreadyExist;
    }
    
    /**
     * 通过id来删除设备
     * @param id
     * @return boolean
     */
    public static boolean deleteById(int id) {
        return dao.deleteById(id);
    }
    
    /**
     * 获取MPS设备的状态信息，先查询数据库，然后再读取文件（状态信息）
     *  
     **/
    public static List<MpsExt> getMpsListStatus() {
        List<Mps> listMps = dao.findAllMps();
        List<MpsExt> mpsList = new ArrayList<MpsExt>();
        Map<String, MpsExt> cluster = new HashMap<String, MpsExt>();
        ArrayList<String> orderKeys = new ArrayList<String>();
        for (Mps mps : listMps) {
            MpsExt currentMpsExt = null;
            
            String trainName = mps.get("desc");
            

            if (cluster.get(trainName) == null) {
                orderKeys.add(trainName);
                currentMpsExt = new MpsExt();
                currentMpsExt.setTrainName(trainName);
                cluster.put(trainName, currentMpsExt);
            } else {
                currentMpsExt = cluster.get(trainName);
            }
            
            String trainNum = mps.get("train_num");
            currentMpsExt.setTrainNum(trainNum);

            String trainPoint = mps.get("ab_marker");
            String ip = mps.get("host_ip");
            String port = mps.get("host_port");

            if (StrUtil.notBlank(trainPoint)) {
                String trainFullName = trainName + trainPoint;

                String stateFilePath = mps.get("client_state_file");

                String stateFileName = stateFilePath + "/" + trainFullName + ".state";
                //String stateLogFileName = stateFilePath + "/" + trainFullName + ".history";
                
                //读取状态文件
                String stateLine = FileUtil.readFileLastLine(stateFileName);
                
                if ("A".equals(trainPoint)) {
                    currentMpsExt.setAPointIP(ip);
                    currentMpsExt.setAPointPort(port);
                    
                    if (StrUtil.notBlank(stateLine)) {
                        String[] detail = stateLine.split("=");
                        if (detail.length >= 3) {
                            currentMpsExt.setAPointCheckTime(detail[0]);
                            currentMpsExt.setAPointStatus(Integer.valueOf(detail[1]).intValue());
                            currentMpsExt.setAPointDuration(Long.valueOf(detail[2]).longValue());
                        }
                    }
                    
                } else if ("B".equals(trainPoint)) {
                    currentMpsExt.setBPointIP(ip);
                    currentMpsExt.setBPointPort(port);
                    
                    if (StrUtil.notBlank(stateLine)) {
                        String[] detail = stateLine.split("=");
                        if (detail.length >= 3) {
                            currentMpsExt.setBPointCheckTime(detail[0]);
                            currentMpsExt.setBPointStatus(Integer.valueOf(detail[1]).intValue());
                            currentMpsExt.setBPointDuration(Long.valueOf(detail[2]).longValue());
                        }
                    }
                }//end B
            }//end trainPoint
            
        }//end for
        
        //for order
        for(String trainName : orderKeys) {
            mpsList.add(cluster.get(trainName));
        }
        
        return mpsList;
    }
    
    
    /**
     * 统计MPS在线离线的数量
     * @return
     */
    public static Map<String, Integer> statisticMpsStatus() {
    	Map<String, Integer> result = new HashMap<String, Integer>();
    	
    	List<MpsExt> mpsList = getMpsListStatus();
    	
    	Integer onlineMps = 0;
    	Integer offlineMps = 0;
    	
    	if(mpsList != null) {
	    	for(MpsExt me : mpsList) {
	    		
	    		if(me.getAPointStatus() == 1) {
	    			onlineMps++;
	    		} else {
	    			offlineMps++;
	    		}

	    		if(me.getBPointStatus() == 1) {
	    			onlineMps++;
	    		} else {
	    			offlineMps++;
	    		}
	    		
	    	}
    	}
    	
    	result.put("online", onlineMps);
    	result.put("offline", offlineMps);
    	
    	return result;
    }
    
    /**
     * 自动生成MPS需要所有数据
     * 
     */
    public static boolean regenerateMpsListDataFile() {
    	boolean result = false;

    	BufferedWriter buffWriteHandler = null;
    	PropKit.use(com.tbds.util.Constants.CONFIG_FILE);
		String mpsPath = PropKit.get(com.tbds.util.Constants.MPS_STATUS_LOG_PATH);
		String mpsDataFile = mpsPath +"/"+ com.tbds.util.Constants.MPS_SERVER_LIST_FILE_NAME;
    	
    	List<Mps> listMps = dao.findAllMps();
    	
    	if(listMps != null) {
    		
    		if(listMps.size() > 0) {
    			
	    		try {
	    			File mpsFile = new File(mpsDataFile);
	    			if(mpsFile.exists()) {
	    				mpsFile.deleteOnExit();
	    			}
	    			
	    			buffWriteHandler = new BufferedWriter(new FileWriter(mpsFile));
	    			
	    			for(Mps mps:listMps) {
	    				String mpsUniqueName = mps.get("fullname");
	    				String hostIp = mps.get("host_ip");
	    				buffWriteHandler.write(mpsUniqueName + ":" + hostIp);
	    				buffWriteHandler.newLine();
	    			}
	    			
	    			result = true;
	    			
	    		} catch(IOException exe) {
	    			System.err.println(exe);
	    		} finally {
	    			try {
	    				if(null != buffWriteHandler) {
	    					buffWriteHandler.close();
	    				}
	    			}catch(Exception e) {
	    				//ignore
	    			}
	    		}
    		
    		}
    	}
    	return result;
    }
    
    /**
     * 根据trainNum查询MPS详细列表
     */
    public static List<Mps> findMpsByTrainNum(String trainNum) {
    	List<Mps> result = dao.find("select * from tbds_mps where train_num = ?", trainNum);
    	
    	if(null != result && result.size() > 0) {
	    	for(Mps mps : result) {
	    		
	    		String trainFullName = mps.get("fullname");
	    		String stateFilePath = mps.get("client_state_file");
				
	            String stateFileName = stateFilePath + "/" + trainFullName + ".state";
	            //String stateLogFileName = stateFilePath + "/" + trainFullName + ".history";
	            
	            //读取状态文件
	            String stateLine = FileUtil.readFileLastLine(stateFileName);
	    		
	            if (StrUtil.notBlank(stateLine)) {
	                String[] detail = stateLine.split("=");
	                if (detail.length >= 3) {
	                    mps.setCheckTime(detail[0]);
	                    mps.setCheckStatus(Integer.valueOf(detail[1]).intValue());
	                    mps.setDuration(Long.valueOf(detail[2]).longValue());
	                }
	            }
	    	}
    	
    	}
    	
    	
    	return result;
    	
    }
    

}
