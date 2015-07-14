package com.example.bespeak.model;

import java.io.Serializable;

public class t_YYYYMM_PsmxInfo implements Serializable {

	public t_YYYYMM_PsmxInfo() {
		c_Djh = "";
		c_Jdbm = "";
		c_Dnxh = 0;
		c_Ddh = "";
		c_Bz = "";
		c_Psrbm2 = "";
		c_Psrmc2 = "";
		c_Dh = "";
	}

	// / <summary>
	// / 配送单号
	// / </summary>
	public String c_Djh;

	// / <summary>
	// / 节点编码
	// / </summary>
	public String c_Jdbm;

	// / <summary>
	// / 单内序号
	// / </summary>
	public int c_Dnxh;

	// / <summary>
	// / 订单号
	// / </summary>
	public String c_Ddh;

	// / <summary>
	// / 备注
	// / </summary>
	public String c_Bz;

	// / <summary>
	// / 二级配送人编码
	// / </summary>
	public String c_Psrbm2;

	// / <summary>
	// / 二级配送人名称
	// / </summary>
	public String c_Psrmc2;

	// / <summary>
	// / 二级配送人电话
	// / </summary>
	public String c_Dh;
}
