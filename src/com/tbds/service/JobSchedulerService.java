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

    /*
     * Search File Transfer History
     */
    public static Page<TransferedFile> searchHistory(String startDate,String endDate,String dataType,int pageNumber, int pageSize)
    {
        //String selectSql = "from YADE_FILES where modified > ? and modified < ?  and SOURCE_PATH LIKE ? order by ID desc";
        
        String sql = "from YADE_FILES  " ;
        String startStr = ( startDate == null || startDate.trim().isEmpty() ) ?  "" : " modified > ? ";
        String endStr = ( endDate == null || endDate.trim().isEmpty() ) ?  "" : " modified < ? ";
        String typeStr = ( dataType == null || dataType.trim().isEmpty() || dataType.equalsIgnoreCase("All") ) ?  "" : " SOURCE_PATH LIKE ?  ";
        
        if ( !startStr.isEmpty() || !endStr.isEmpty() || !typeStr.isEmpty() )
        {
            sql = sql + " where ";
        }
        
        String joinStr = ( startStr.isEmpty() ? "": startStr  + " and ") +  ( endStr.isEmpty() ? "":  endStr + " and " ) + ( typeStr.isEmpty() ? "": typeStr );
        
        if ( joinStr.trim().endsWith("and") )
        {
            joinStr = joinStr.substring(0,joinStr.trim().lastIndexOf("and") );
        }
        
        //add file transfer destination filter
        String receiveDirFlag = "receive";
        if ( sql.indexOf("where") >= 0 )
        {
            joinStr = joinStr + " and TARGET_PATH like '%" + receiveDirFlag + "%'"; 
        }
        else
        {
            joinStr = joinStr + " where TARGET_PATH like '%" + receiveDirFlag + "%'"; 
        }
        
        sql = sql + joinStr + " order by ID desc";
        
        String filterVar = "";
        
        /*
        String sql2 = "abcdef?DD";
        String[] s1 = sql2.split("\\?");
        System.out.println("********** len is" + s1.length + "***********");
        */
        String[] arr = sql.split("\\?");
        
        Page<TransferedFile> ret = null;
        
        if ( arr.length == 1 ) // 0 filter
        {
             ret = TransferedFile.dao.paginate(pageNumber, pageSize, "select * ", sql);
        }
        else if ( arr.length  == 2) // 1 filter
        {
            filterVar = ( startStr.isEmpty() ? "": startDate ) +  ( endStr.isEmpty() ? "": "" + endDate ) + ( typeStr.isEmpty() ? "": "%"+dataType+"%" );
            ret = TransferedFile.dao.paginate(pageNumber, pageSize, "select * ", sql, filterVar);
        }
        else if ( arr.length  == 3) //2 filters
        {
            if ( !startStr.isEmpty() && !endStr.isEmpty() )
            {
                ret = TransferedFile.dao.paginate(pageNumber, pageSize, "select * ", sql, startDate,endDate);
            }
            else if ( !startStr.isEmpty() && !typeStr.isEmpty() )
            {
                 ret = TransferedFile.dao.paginate(pageNumber, pageSize, "select * ", sql, startDate,"%"+dataType+"%");
            }
            else if ( !endStr.isEmpty() && !typeStr.isEmpty() )
            {
                 ret = TransferedFile.dao.paginate(pageNumber, pageSize, "select * ", sql, endDate,"%"+dataType+"%");
            }            
        }
        else if ( arr.length  == 4) //3 filters
        {
           ret = TransferedFile.dao.paginate(pageNumber, pageSize, "select * ", sql,startDate,endDate,"%"+dataType+"%");
        }
        
        return ret;
    }
    
    /**
     * File Inventory Search
     * @param startDate
     * @param endDate
     * @param dataType
     * @param trainNo
     * @param pageNumber
     * @param pageSize
     * @return
     */
     public static Page<TransferedFile> searchFiles(String startDate,String endDate,String dataType,String trainNo,int pageNumber, int pageSize)
    {
        //String selectSql = "SELECT MAX(ID) AS ID,TARGET_PATH  FROM ztjs.YADE_FILES WHERE"
    	//			+ " TARGET_PATH LIKE '%receive%' and (TARGET_PATH LIKE '%CDV%') "
    	//			+ " and (TARGET_PATH LIKE '%0223%') and (TARGET_PATH LIKE '%20171215%' ||TARGET_PATH LIKE '%20171216%') " 
    	//			+ " group by TARGET_PATH ORDER BY ID";
        
    	//assuming "receive" is the receiving folder flag 
        String sql = "from YADE_FILES WHERE TARGET_PATH LIKE '%receive%' and " ;
        String typeStr = ( dataType == null || dataType.trim().isEmpty() || dataType.equalsIgnoreCase("All") ) ?  "" : " TARGET_PATH LIKE ?  ";
        String trainStr = ( trainNo == null || trainNo.trim().isEmpty() ) ?  "" : " TARGET_PATH LIKE ?  ";
        
        String joinStr = ( typeStr.isEmpty() ? "": typeStr  + " and ") +  ( trainStr.isEmpty() ? "":  trainStr + " and " );
        sql = sql + " " + joinStr;
        
        String dateString = "";
        String sfDate ="";
        String efDate ="";
        if ( startDate != null )
        {
        	sfDate = startDate.replaceAll("-","");
        }
        
        if ( endDate != null )
        {
        	efDate = endDate.replaceAll("-","");
        }
        
        if ( ( startDate != null && !startDate.isEmpty() ) && ( endDate != null && !endDate.isEmpty() )  )
        {
            dateString = " ( substring(TARGET_PATH,-26,8) >= '" + sfDate +  "' and substring(TARGET_PATH,-26,8) <= '" +  efDate + "') ";
        }
        else if ( ( startDate != null && !startDate.isEmpty() ) && ( endDate == null || endDate.isEmpty() )  )
        {
        	dateString = " ( substring(TARGET_PATH,-26,8) >= '" + sfDate + "')";
        }
        else if ( ( endDate != null && !endDate.isEmpty() ) && ( startDate == null || startDate.isEmpty() )  )
        {
        	dateString = " ( substring(TARGET_PATH,-26,8) <= '" + efDate + "')";
        }
        
        sql = sql + dateString;
        
        if ( sql.trim().endsWith("and") )
        {
        	sql = sql.substring(0,sql.trim().lastIndexOf("and") );
        }
        
        
        sql = sql + " group by TARGET_PATH ORDER BY ID ";
        
        
        String selectSql = "SELECT MAX(ID) AS ID,TARGET_PATH ";
        
        String filterVar = "";
        
        /*
        String sql2 = "abcdef?DD";
        String[] s1 = sql2.split("\\?");
        System.out.println("********** len is" + s1.length + "***********");
        */
        String[] arr = sql.split("\\?");
        
        Page<TransferedFile> ret = null;
        
        if ( arr.length == 1 ) // 0 filter
        {
             ret = TransferedFile.dao.paginate(pageNumber, pageSize, selectSql, sql);
        }
        else if ( arr.length  == 2) // 1 filter
        {
            filterVar = ( typeStr.isEmpty() ? "": "%"+ dataType + "%" ) +  ( trainStr.isEmpty() ? "": "%_" + trainNo + "_%" ) ;
            ret = TransferedFile.dao.paginate(pageNumber, pageSize, selectSql, sql, filterVar);
        }
        else if ( arr.length  == 3) //2 filters
        {
           ret = TransferedFile.dao.paginate(pageNumber, pageSize, selectSql, sql,"%"+dataType+"%","%_"+trainNo+"_%");
        }
        
        return ret;
    }
     
    
}
