package com.example.bespeak.model;

import java.util.List;

/**
 * 会员信息
 * @author jeanbo
 *
 */
public class t_HydaInfo {
	public t_HydaInfo() {
		c_Hybm = "";
		c_Hymc = "";
		c_Hykh = "";
		c_Xb = 1;
		c_Csrq = "";
		c_Zjlx = 0;
		c_Zjbh = "";
		c_Yzbm = "";
		c_Hyye = 0;
		c_Jf = 0;
		c_Syjf = 0;
		c_Xfze = 0;
		c_Yddh = "";
		c_Yj = "";
		c_QQ = "";
		c_SID = "";
		c_SSID = "";
		c_XID = "";
		c_XQID = "";
		c_Xxdz = "";
		c_Bz = "";
		c_Jdrq = "";
		c_Jdrbm = "";
		c_Jdrmc = "";
		c_Zhxgrq = "";
		c_Zhxgbm = "";
		c_Zhxgmc = "";
		c_Zhxfje = 0;
		c_Tjrbm = "";
		c_Tjjf = 0;
		c_hymm = "";
		IsNew=0;
		isLogin=0;
		c_xxdzid=0;
	}
	
    /// <summary>
    /// 是否新会员
    /// </summary>
    public int IsNew;

	// / <summary>
	// / 会员编码
	// / </summary>
	public String c_Hybm;

	// / <summary>
	// / 会员名称
	// / </summary>
	public String c_Hymc;

	// / <summary>
	// / 会员卡号
	// / </summary>
	public String c_Hykh;

	// / <summary>
	// / 性别[0男|1女]
	// / </summary>
	public int c_Xb;

	// / <summary>
	// / 出生日期
	// / </summary>
	public String c_Csrq;

	// / <summary>
	// / 证件类型
	// / </summary>
	public int c_Zjlx;

	// / <summary>
	// / 证件编号
	// / </summary>
	public String c_Zjbh;

	// / <summary>
	// / 邮政编码
	// / </summary>
	public String c_Yzbm;

	// / <summary>
	// / 会员余额
	// / </summary>
	public double c_Hyye;

	// / <summary>
	// / 积分
	// / </summary>
	public double c_Jf;

	// / <summary>
	// / 已使用积分
	// / </summary>
	public double c_Syjf;

	// / <summary>
	// / 消费总额
	// / </summary>
	public double c_Xfze;

	// / <summary>
	// / 移动电话
	// / </summary>
	public String c_Yddh;

	// / <summary>
	// / 邮件
	// / </summary>
	public String c_Yj;

	// / <summary>
	// / QQ号码
	// / </summary>
	public String c_QQ;

	  /// <summary>
    /// 省ID
    /// </summary>
    public String c_SID ;

    /// <summary>
    /// 省名称
    /// </summary>
    public String c_SID_Name ;


    /// <summary>
    /// 市ID
    /// </summary>
    public String c_SSID ;

    /// <summary>
    /// 市名称
    /// </summary>
    public String c_SSID_Name ;


    /// <summary>
    /// 区(县)ID
    /// </summary>
    public String c_XID ;

    /// <summary>
    /// 区(县)名称
    /// </summary>
    public String c_XID_Name ;


    /// <summary>
    /// 小区ID号
    /// </summary>
    public String c_XQID ;

    /// <summary>
    /// 小区名称
    /// </summary>
    public String c_XQID_Name ;

	// / <summary>
	// / 详细地址
	// / </summary>
	public String c_Xxdz;

	// / <summary>
	// / 备注
	// / </summary>
	public String c_Bz;

	// / <summary>
	// / 建档日期
	// / </summary>
	public String c_Jdrq;

	// / <summary>
	// / 建档人编码
	// / </summary>
	public String c_Jdrbm;

	// / <summary>
	// / 建档人名称
	// / </summary>
	public String c_Jdrmc;

	// / <summary>
	// / 最后修改日期
	// / </summary>
	public String c_Zhxgrq;

	// / <summary>
	// / 最后修改人编码
	// / </summary>
	public String c_Zhxgbm;

	// / <summary>
	// / 最后修改人名称
	// / </summary>
	public String c_Zhxgmc;

	// / <summary>
	// / 最后消费金额
	// / </summary>
	public double c_Zhxfje;

	// / <summary>
	// / [注释]
	// / </summary>
	public String c_Tjrbm;

	// / <summary>
	// / [注释]
	// / </summary>
	public double c_Tjjf;

	// / <summary>
	// / [注释]
	// / </summary>
	public String c_hymm;
	
    /// <summary>
    /// 会员地址集合
    /// </summary>
    public List<t_HydzInfo> hydzList ;
    
    /**
     * 是否登录
     */
    public int isLogin;
    
    /**
     * 详细地址id
     */
    public int c_xxdzid;
}
