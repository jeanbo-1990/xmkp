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
    /// ��Ա����
    /// </summary>
    public String c_Hybm ;

    /// <summary>
    /// ��Ա����
    /// </summary>
    public String c_Hymc ;
    
    /// <summary>
    /// ����״̬
    /// </summary>
    public int c_Djzt ;

    /// <summary>
    /// ���ݺ�
    /// </summary>
    public String c_Djh ;

    /// <summary>
    /// �Ƶ�ʱ��
    /// </summary>
    public String c_Zdsj ;

    /// <summary>
    /// �����ܶ�
    /// </summary>
    public double c_Ddgz ;

    /// <summary>
    /// ��
    /// </summary>
    public String c_Ss ;

    /// <summary>
    /// ��
    /// </summary>
    public String c_Qs ;

    /// <summary>
    /// С��
    /// </summary>
    public String c_Xq ;

    /// <summary>
    /// ��ϸ��ַ
    /// </summary>
    public String c_Xxdz ;

    /// <summary>
    /// ��ϸ����
    /// </summary>
    public List<ddmx> detailList ;
    
    /// <summary>
    /// ������Ϣ
    /// </summary>
    public t_YYYYMM_PsmxInfo psmx ;
    
    /// <summary>
    /// ����ʱ��
    /// </summary>
    public String c_Pssj ;
}
