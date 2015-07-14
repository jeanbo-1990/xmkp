package com.example.bespeak.model;

import java.util.List;

/**
 * ��Ա��Ϣ
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
    /// �Ƿ��»�Ա
    /// </summary>
    public int IsNew;

	// / <summary>
	// / ��Ա����
	// / </summary>
	public String c_Hybm;

	// / <summary>
	// / ��Ա����
	// / </summary>
	public String c_Hymc;

	// / <summary>
	// / ��Ա����
	// / </summary>
	public String c_Hykh;

	// / <summary>
	// / �Ա�[0��|1Ů]
	// / </summary>
	public int c_Xb;

	// / <summary>
	// / ��������
	// / </summary>
	public String c_Csrq;

	// / <summary>
	// / ֤������
	// / </summary>
	public int c_Zjlx;

	// / <summary>
	// / ֤�����
	// / </summary>
	public String c_Zjbh;

	// / <summary>
	// / ��������
	// / </summary>
	public String c_Yzbm;

	// / <summary>
	// / ��Ա���
	// / </summary>
	public double c_Hyye;

	// / <summary>
	// / ����
	// / </summary>
	public double c_Jf;

	// / <summary>
	// / ��ʹ�û���
	// / </summary>
	public double c_Syjf;

	// / <summary>
	// / �����ܶ�
	// / </summary>
	public double c_Xfze;

	// / <summary>
	// / �ƶ��绰
	// / </summary>
	public String c_Yddh;

	// / <summary>
	// / �ʼ�
	// / </summary>
	public String c_Yj;

	// / <summary>
	// / QQ����
	// / </summary>
	public String c_QQ;

	  /// <summary>
    /// ʡID
    /// </summary>
    public String c_SID ;

    /// <summary>
    /// ʡ����
    /// </summary>
    public String c_SID_Name ;


    /// <summary>
    /// ��ID
    /// </summary>
    public String c_SSID ;

    /// <summary>
    /// ������
    /// </summary>
    public String c_SSID_Name ;


    /// <summary>
    /// ��(��)ID
    /// </summary>
    public String c_XID ;

    /// <summary>
    /// ��(��)����
    /// </summary>
    public String c_XID_Name ;


    /// <summary>
    /// С��ID��
    /// </summary>
    public String c_XQID ;

    /// <summary>
    /// С������
    /// </summary>
    public String c_XQID_Name ;

	// / <summary>
	// / ��ϸ��ַ
	// / </summary>
	public String c_Xxdz;

	// / <summary>
	// / ��ע
	// / </summary>
	public String c_Bz;

	// / <summary>
	// / ��������
	// / </summary>
	public String c_Jdrq;

	// / <summary>
	// / �����˱���
	// / </summary>
	public String c_Jdrbm;

	// / <summary>
	// / ����������
	// / </summary>
	public String c_Jdrmc;

	// / <summary>
	// / ����޸�����
	// / </summary>
	public String c_Zhxgrq;

	// / <summary>
	// / ����޸��˱���
	// / </summary>
	public String c_Zhxgbm;

	// / <summary>
	// / ����޸�������
	// / </summary>
	public String c_Zhxgmc;

	// / <summary>
	// / ������ѽ��
	// / </summary>
	public double c_Zhxfje;

	// / <summary>
	// / [ע��]
	// / </summary>
	public String c_Tjrbm;

	// / <summary>
	// / [ע��]
	// / </summary>
	public double c_Tjjf;

	// / <summary>
	// / [ע��]
	// / </summary>
	public String c_hymm;
	
    /// <summary>
    /// ��Ա��ַ����
    /// </summary>
    public List<t_HydzInfo> hydzList ;
    
    /**
     * �Ƿ��¼
     */
    public int isLogin;
    
    /**
     * ��ϸ��ַid
     */
    public int c_xxdzid;
}
