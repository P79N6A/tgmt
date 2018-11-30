/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbds.service;

import com.jfinal.plugin.activerecord.Page;
import com.tbds.model.eo.TransferedFile;

/**
 *
 * @author JQIAO
 */
public class JobSchedulerService {
    
     public static Page<TransferedFile> paginate(int pageNumber, int pageSize) {
        
        return TransferedFile.dao.paginate(pageNumber, pageSize, "select * ", "from YADE_FILES order by ID desc");
    }
}
