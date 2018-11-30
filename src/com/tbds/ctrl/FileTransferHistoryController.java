/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbds.ctrl;

import com.jfinal.core.Controller;
import com.tbds.service.JobSchedulerService;

/**
 *
 * @author jq
 */
public class FileTransferHistoryController extends Controller  {
        public void index() {
        setAttr("files", JobSchedulerService.paginate(getParaToInt(0, 1), 5));
        render("index.html");
    }
}
