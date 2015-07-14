package com.example.bespeak.model;

/**
 * 会员地址
 * @author jeanbo
 *
 */
public class t_HydzInfo {
	public t_HydzInfo()
    {
        c_Hybm = "";
        c_DzID = 0;
        c_SID = "0001";
        c_SSID = "0001";
        c_XID = "0001";
        c_XQID = "";
        c_Xxdz = "";
        c_Xq="";
        IsSelect=false;
    }

      
    /// <summary>
    /// 会员编码
    /// </summary>
    public String c_Hybm ;


    /// <summary>
    /// 地址ID
    /// </summary>
    public int c_DzID ;


    /// <summary>
    /// 省ID
    /// </summary>
    public String c_SID ;


    /// <summary>
    /// 市ID
    /// </summary>
    public String c_SSID ;


    /// <summary>
    ///	区(县)ID
    /// </summary>
    public String c_XID ;


    /// <summary>
    /// 小区ID号
    /// </summary>
    public String c_XQID ;
    
    /// <summary>
    /// 小区名称
    /// </summary>
    public String c_Xq ;


    /// <summary>
    /// 详细地址
    /// </summary>
    public String c_Xxdz ;
    
    /**
     * 是否选中
     */
    public boolean IsSelect;
}
