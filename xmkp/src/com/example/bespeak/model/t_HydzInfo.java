package com.example.bespeak.model;

/**
 * ��Ա��ַ
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
    /// ��Ա����
    /// </summary>
    public String c_Hybm ;


    /// <summary>
    /// ��ַID
    /// </summary>
    public int c_DzID ;


    /// <summary>
    /// ʡID
    /// </summary>
    public String c_SID ;


    /// <summary>
    /// ��ID
    /// </summary>
    public String c_SSID ;


    /// <summary>
    ///	��(��)ID
    /// </summary>
    public String c_XID ;


    /// <summary>
    /// С��ID��
    /// </summary>
    public String c_XQID ;
    
    /// <summary>
    /// С������
    /// </summary>
    public String c_Xq ;


    /// <summary>
    /// ��ϸ��ַ
    /// </summary>
    public String c_Xxdz ;
    
    /**
     * �Ƿ�ѡ��
     */
    public boolean IsSelect;
}
