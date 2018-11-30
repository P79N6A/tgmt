/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbds.model.eo;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.tbds.util.StrUtil;
import java.util.List;

/**
 *
 * @author totan
 */
public class Mps extends Model<Mps> {
    public static final Mps dao = new Mps();
    public static final String TABLE_NAME = "tbds_mps";
    public static final String PRIMARY_KEY = "id";

    public Page<Mps> paginate(int pageNumber, int pageSize) {
        return paginate(pageNumber, pageSize, "select * ", "from " + TABLE_NAME + " order by " + PRIMARY_KEY + " asc");
    }
    
    public List<Mps> findAllMps() {
        return dao.find("select * from " + TABLE_NAME + " order by " + PRIMARY_KEY + " asc");
    }

    public List<Mps> findMps(String trainNum) {
        return dao.find("select * from " + TABLE_NAME + " where train_num=?", trainNum);
    }

    /**
     * 查找设备的信息 + 其对应的状态
     * */
    public Mps findMpsStatus(String trainNum, String abMarker) {
        if (StrUtil.isBlank(trainNum) && StrUtil.isBlank(abMarker)) {
            return null;
        }
        String sql =
            "select d.status from " + TABLE_NAME + " d " +
            " where d.train_num = ?  and ab_marker = ?";
        return dao.findFirst(sql, trainNum, abMarker);
    }

    public Mps findByTrainNumber(String trainNum) {
        return dao.findFirst("select * from " + TABLE_NAME + " where train_num=?", trainNum);
    }

    public int updateMpsStatus(String trainNum, String abMarker, int status) {
        return Db.update("update " + TABLE_NAME + " set status=? where train_num=? and ab_marker=?", status, trainNum, abMarker);
    }
}
