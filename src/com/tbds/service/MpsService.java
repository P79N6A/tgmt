/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbds.service;

import com.jfinal.plugin.activerecord.Page;
import com.tbds.model.eo.Mps;
import com.tbds.model.eo.MpsExt;
import com.tbds.util.FileUtil;
import com.tbds.util.StrUtil;
import java.io.File;
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
    private Mps dao = new Mps().dao();

    public Page<Mps> paginate(int pageNumber, int pageSize) {
        return dao.paginate(pageNumber, pageSize);
    }

    public Mps findById(int id) {
        return dao.findById(id);
    }

    public void deleteById(int id) {
        dao.deleteById(id);
    }

    public List<MpsExt> getMpsListStatus() {
        List<Mps> listMps = dao.findAllMps();
        List<MpsExt> mpsList = new ArrayList<MpsExt>();
        Map<String, MpsExt> cluster = new HashMap<String, MpsExt>();
        ArrayList<String> orderKeys = new ArrayList<String>();
        for (Mps mps : listMps) {
            MpsExt currentMpsExt = null;
            String trainType = mps.get("train_type");
            String trainNum = mps.get("train_num");

            String trainName = trainType + "_" + trainNum;

            if (cluster.get(trainName) == null) {
                orderKeys.add(trainName);
                currentMpsExt = new MpsExt();
                currentMpsExt.setTrainName(trainName);
                cluster.put(trainName, currentMpsExt);
            } else {
                currentMpsExt = cluster.get(trainName);
            }

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

}
