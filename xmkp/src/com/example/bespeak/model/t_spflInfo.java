package com.example.bespeak.model;

import java.util.List;

/**
 * 商品分类信息
 * @author jeanbo
 *
 */
public class t_spflInfo {
	public t_spflInfo() {
		c_flbm = "";
		c_flmc = "";
		c_fflbm = "";
		c_jb = 0;
		c_sfmj = 0;
	}

	/**
	 * 分类编码
	 */
	public String c_flbm;

	/**
	 * 分类名称
	 */
	public String c_flmc;

	/**
	 * 父分类编码
	 */
	public String c_fflbm;

	/**
	 * 级别
	 */
	public int c_jb;

	/**
	 * 是否末级
	 */
	public int c_sfmj;
	
	/**
	 * 分類下的商品
	 */
	public List<t_SpdaInfo> goodsList;
}