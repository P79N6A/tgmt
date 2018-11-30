/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbds.model.eo;

import com.jfinal.plugin.activerecord.Model;

/**
 *
 * @author JQIAO
 */
public class TransferedFile extends Model<TransferedFile>{
    public static final TransferedFile dao = new TransferedFile().dao();
    
     public Transfer getTransfer() {
       return Transfer.dao.findById( get("TRANSFER_ID").toString() );
    }
}
