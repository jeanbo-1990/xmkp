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
     /// 单据号
     /// </summary>
     public String c_Djh ;


     /// <summary>
     /// 节点编码
     /// </summary>
     public String c_Jdbm ;


     /// <summary>
     /// 单据类型[0手工录入|1网页|2App]
     /// </summary>
     public int c_Djlx ;


     /// <summary>
     /// 单据状态[0确认|1已发货|2已派送|3已完成|4作废]
     /// </summary>
     public int c_Djzt ;


     /// <summary>
     /// 会员编码
     /// </summary>
     public String c_Hybm ;


     /// <summary>
     /// 制单时间
     /// </summary>
     public String c_Zdrq ;


     /// <summary>
     /// 制单人编码
     /// </summary>
     public String c_Zdrbm ;


     /// <summary>
     /// 制单人名称
     /// </summary>
     public String c_Zdrmc ;


     /// <summary>
     /// 完成日期
     /// </summary>
     public String c_Wcrq ;


     /// <summary>
     /// 完成人编码
     /// </summary>
     public String c_Wcrbm ;


     /// <summary>
     /// 完成人名称
     /// </summary>
     public String c_Wcrmc ;


     /// <summary>
     /// 作废日期
     /// </summary>
     public String c_Zfrq ;


     /// <summary>
     /// 作废人编码
     /// </summary>
     public String c_Zfrbm ;


     /// <summary>
     /// 作废人名称
     /// </summary>
     public String c_Zfrmc ;


     /// <summary>
     /// 备注
     /// </summary>
     public String c_Bz ;


     /// <summary>
     /// 派送人名称
     /// </summary>
     public int c_Sfmd ;


     /// <summary>
     /// 订单总额
     /// </summary>
     public double c_Ddgz ;


     /// <summary>
     /// 使用积分
     /// </summary>
     public double c_Syjf ;


     /// <summary>
     /// 抵减金额
     /// </summary>
     public double c_Djje ;


     /// <summary>
     /// 省ID
     /// </summary>
     public String c_SID ;


     /// <summary>
     /// 市ID
     /// </summary>
     public String c_SSID ;


     /// <summary>
     /// 区(县)ID
     /// </summary>
     public String c_XID ;


     /// <summary>
     /// 小区ID号
     /// </summary>
     public String c_XQID ;


     /// <summary>
     /// 详细地址ID
     /// </summary>
     public int c_DzID ;


     /// <summary>
     /// 详细地址
     /// </summary>
     public String c_Xxdz ;


     /// <summary>
     /// 打印次数
     /// </summary>
     public int c_Dycs ;


     /// <summary>
     /// 制单时间
     /// </summary>
     public String c_Zdsj ;


     /// <summary>
     /// 预计到达时间
     /// </summary>
     public String c_Yjddsj ;


     /// <summary>
     /// 发货时间
     /// </summary>
     public String c_Fhsj ;


     /// <summary>
     /// 发货人编码
     /// </summary>
     public String c_Fhrbm ;


     /// <summary>
     /// 发货人名称
     /// </summary>
     public String c_Fhrmc ;


     /// <summary>
     /// 配送单号
     /// </summary>
     public String c_Psdh ;


     /// <summary>
     /// 派送时间
     /// </summary>
     public String c_Pssj ;


     /// <summary>
     /// 派送人编码
     /// </summary>
     public String c_Psrbm ;


     /// <summary>
     /// 派送人名称
     /// </summary>
     public String c_Psrmc ;


     /// <summary>
     /// 派送人号码
     /// </summary>
     public String c_Psrhm ;


     /// <summary>
     /// 完成时间
     /// </summary>
     public String c_Wcsj ;

     /// <summary>
     /// 订单明细
     /// </summary>
     public List<t_YYYYMM_DdmxInfo> detailList ;
     
     /**
      * 付款方式
      */
     public int c_Zfgs;
     
     /// <summary>
     /// 配送费
     /// </summary>
     public double peisongfei ;
     
     /**
      * 原始配送费
      */
     public double oldPeisongfei;
     
     /**
      * 满多少免配送费
      */
     public double peisongfeiFree;
}
