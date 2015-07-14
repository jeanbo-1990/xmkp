package com.example.bespeak.model;

import java.util.ArrayList;
import java.util.List;

public class t_YYYYMM_DddjInfo {

	 public t_YYYYMM_DddjInfo()
     {
         c_Djh = "";
         c_Jdbm = "";
         c_Djlx = 0;
         c_Djzt = 0;
         c_Hybm = "";
         c_Zdrq = "";
         c_Zdrbm = "";
         c_Zdrmc = "";
         c_Wcrq = "";
         c_Wcrbm = "";
         c_Wcrmc = "";
         c_Zfrq = "";
         c_Zfrbm = "";
         c_Zfrmc = "";
         c_Bz = "";
         c_Sfmd = 0;
         c_Ddgz = 0;
         c_Syjf = 0;
         c_Djje = 0;
         c_SID = "";
         c_SSID = "";
         c_XID = "";
         c_XQID = "";
         c_DzID = 0;
         c_Xxdz = "";
         c_Dycs = 0;
         c_Zdsj = "";
         c_Yjddsj = "";
         c_Fhsj = "";
         c_Fhrbm = "";
         c_Fhrmc = "";
         c_Psdh = "";
         c_Pssj = "";
         c_Psrbm = "";
         c_Psrmc = "";
         c_Psrhm = "";
         c_Wcsj = "";
         detailList = new ArrayList<t_YYYYMM_DdmxInfo>();
         c_Zfgs = 0;
         peisongfei=0;
     }

       
     /// <summary>
     /// ���ݺ�
     /// </summary>
     public String c_Djh ;


     /// <summary>
     /// �ڵ����
     /// </summary>
     public String c_Jdbm ;


     /// <summary>
     /// ��������[0�ֹ�¼��|1��ҳ|2App]
     /// </summary>
     public int c_Djlx ;


     /// <summary>
     /// ����״̬[0ȷ��|1�ѷ���|2������|3�����|4����]
     /// </summary>
     public int c_Djzt ;


     /// <summary>
     /// ��Ա����
     /// </summary>
     public String c_Hybm ;


     /// <summary>
     /// �Ƶ�ʱ��
     /// </summary>
     public String c_Zdrq ;


     /// <summary>
     /// �Ƶ��˱���
     /// </summary>
     public String c_Zdrbm ;


     /// <summary>
     /// �Ƶ�������
     /// </summary>
     public String c_Zdrmc ;


     /// <summary>
     /// �������
     /// </summary>
     public String c_Wcrq ;


     /// <summary>
     /// ����˱���
     /// </summary>
     public String c_Wcrbm ;


     /// <summary>
     /// ���������
     /// </summary>
     public String c_Wcrmc ;


     /// <summary>
     /// ��������
     /// </summary>
     public String c_Zfrq ;


     /// <summary>
     /// �����˱���
     /// </summary>
     public String c_Zfrbm ;


     /// <summary>
     /// ����������
     /// </summary>
     public String c_Zfrmc ;


     /// <summary>
     /// ��ע
     /// </summary>
     public String c_Bz ;


     /// <summary>
     /// ����������
     /// </summary>
     public int c_Sfmd ;


     /// <summary>
     /// �����ܶ�
     /// </summary>
     public double c_Ddgz ;


     /// <summary>
     /// ʹ�û���
     /// </summary>
     public double c_Syjf ;


     /// <summary>
     /// �ּ����
     /// </summary>
     public double c_Djje ;


     /// <summary>
     /// ʡID
     /// </summary>
     public String c_SID ;


     /// <summary>
     /// ��ID
     /// </summary>
     public String c_SSID ;


     /// <summary>
     /// ��(��)ID
     /// </summary>
     public String c_XID ;


     /// <summary>
     /// С��ID��
     /// </summary>
     public String c_XQID ;


     /// <summary>
     /// ��ϸ��ַID
     /// </summary>
     public int c_DzID ;


     /// <summary>
     /// ��ϸ��ַ
     /// </summary>
     public String c_Xxdz ;


     /// <summary>
     /// ��ӡ����
     /// </summary>
     public int c_Dycs ;


     /// <summary>
     /// �Ƶ�ʱ��
     /// </summary>
     public String c_Zdsj ;


     /// <summary>
     /// Ԥ�Ƶ���ʱ��
     /// </summary>
     public String c_Yjddsj ;


     /// <summary>
     /// ����ʱ��
     /// </summary>
     public String c_Fhsj ;


     /// <summary>
     /// �����˱���
     /// </summary>
     public String c_Fhrbm ;


     /// <summary>
     /// ����������
     /// </summary>
     public String c_Fhrmc ;


     /// <summary>
     /// ���͵���
     /// </summary>
     public String c_Psdh ;


     /// <summary>
     /// ����ʱ��
     /// </summary>
     public String c_Pssj ;


     /// <summary>
     /// �����˱���
     /// </summary>
     public String c_Psrbm ;


     /// <summary>
     /// ����������
     /// </summary>
     public String c_Psrmc ;


     /// <summary>
     /// �����˺���
     /// </summary>
     public String c_Psrhm ;


     /// <summary>
     /// ���ʱ��
     /// </summary>
     public String c_Wcsj ;

     /// <summary>
     /// ������ϸ
     /// </summary>
     public List<t_YYYYMM_DdmxInfo> detailList ;
     
     /**
      * ���ʽ
      */
     public int c_Zfgs;
     
     /// <summary>
     /// ���ͷ�
     /// </summary>
     public double peisongfei ;
     
     /**
      * ԭʼ���ͷ�
      */
     public double oldPeisongfei;
     
     /**
      * �����������ͷ�
      */
     public double peisongfeiFree;
}
