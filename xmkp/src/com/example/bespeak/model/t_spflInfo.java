package com.example.bespeak.model;

import java.util.List;

/**
 * ��Ʒ������Ϣ
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
	 * �������
	 */
	public String c_flbm;

	/**
	 * ��������
	 */
	public String c_flmc;

	/**
	 * ���������
	 */
	public String c_fflbm;

	/**
	 * ����
	 */
	public int c_jb;

	/**
	 * �Ƿ�ĩ��
	 */
	public int c_sfmj;
	
	/**
	 * ����µ���Ʒ
	 */
	public List<t_SpdaInfo> goodsList;
}