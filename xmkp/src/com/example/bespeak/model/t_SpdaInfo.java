package com.example.bespeak.model;

import android.graphics.Bitmap;

/**
 * 商品信息
 * @author jeanbo
 *
 */
public class t_SpdaInfo {
	public t_SpdaInfo() {
		c_Spbm = "";
		c_Spmc = "";
		c_Sphh = "";
		c_Spcd = "";
		c_Splx = 0;
		c_Ggxh = "";
		c_Spflbm = "";
		c_Jldw = "";
		c_Jxsl = 0;
		c_Xxsl = 0;
		c_Spjj = 0;
		c_Spsj = 0;
		c_Jlcz = 0;
		c_Zgsj = 0;
		c_Zjsj = 0;
		c_Sptm = "";
		c_Spms = "";
		c_Jdrq = "";
		c_Xgrq = "";
		c_Dtp = "";
		c_Xtp = "";
		c_Sfsj = 0;
		OrderAmcount=0;
	}

	/**
	 * 商品编码
	 */
	public String c_Spbm;

	/**
	 * 商品名称
	 */
	public String c_Spmc;

	/**
	 * 商品货号
	 */
	public String c_Sphh;

	/**
	 * 商品产地
	 */
	public String c_Spcd;

	/**
	 * 商品类型[0一般商品|1联营商品|2A进A出生鲜|3A进B出生鲜|4生鲜原料|5生鲜归集|6租赁]
	 */
	public int c_Splx;

	/**
	 * 规格型号
	 */
	public String c_Ggxh;

	/**
	 * 商品分类编码
	 */
	public String c_Spflbm;

	/**
	 * 计量单位
	 */
	public String c_Jldw;

	/**
	 * 进项税率
	 */
	public double c_Jxsl;

	/**
	 * 销项税率
	 */
	public double c_Xxsl;

	/**
	 * 商品进价
	 */
	public double c_Spjj;

	/**
	 * 商品售价
	 */
	public double c_Spsj;

	/**
	 * 计量称重[0标准|1计量|2称重]
	 */
	public int c_Jlcz;

	/**
	 * 最高售价
	 */
	public double c_Zgsj;

	/**
	 * 最低售价
	 */
	public double c_Zjsj;

	/**
	 * 商品条码
	 */
	public String c_Sptm;

	/**
	 * 商品描述
	 */
	public String c_Spms;

	/**
	 * 建档日期
	 */
	public String c_Jdrq;

	/**
	 * 修改日期
	 */
	public String c_Xgrq;

	/**
	 * 大图片
	 */
	public String c_Dtp;

	/**
	 * 小图片
	 */
	public String c_Xtp;

	/**
	 * 商品上架
	 */
	public int c_Sfsj;
	
	/**
	 * 订购数量
	 */
	public int OrderAmcount;
	
	public Bitmap bitmap;
}
