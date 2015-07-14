package com.example.bespeak.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ddzb implements Serializable {
    public ddzb()
    {
        c_Hybm = "";
        c_Hymc = "";
        c_Djh = "";
        c_Zdsj = "";
        c_Ddgz = 0;
        c_Ss = "";
        c_Qs = "";
        c_Xq = "";
        c_Xxdz = "";
        detailList = new ArrayList<ddmx>();
        c_Djzt=0;
    }
	
	 /// <summary>
    /// 会员编码
    /// </summary>
    public String c_Hybm ;

    /// <summary>
    /// 会员名称
    /// </summary>
    public String c_Hymc ;
    
    /// <summary>
    /// 单据状态
    /// </summary>
    public int c_Djzt ;

    /// <summary>
    /// 单据号
    /// </summary>
    public String c_Djh ;

    /// <summary>
    /// 制单时间
    /// </summary>
    public String c_Zdsj ;

    /// <summary>
    /// 订单总额
    /// </summary>
    public double c_Ddgz ;

    /// <summary>
    /// 市
    /// </summary>
    public String c_Ss ;

    /// <summary>
    /// 县
    /// </summary>
    public String c_Qs ;

    /// <summary>
    /// 小区
    /// </summary>
    public String c_Xq ;

    /// <summary>
    /// 详细地址
    /// </summary>
    public String c_Xxdz ;

    /// <summary>
    /// 明细集合
    /// </summary>
    public List<ddmx> detailList ;
    
    /// <summary>
    /// 配送信息
    /// </summary>
    public t_YYYYMM_PsmxInfo psmx ;
    
    /// <summary>
    /// 配送时间
    /// </summary>
    public String c_Pssj ;
}
