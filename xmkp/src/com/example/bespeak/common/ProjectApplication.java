package com.example.bespeak.common;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.baidu.frontia.FrontiaApplication;
import com.example.bespeak.model.t_HydaInfo;
import com.example.bespeak.model.t_SpdaInfo;
import com.example.bespeak.model.t_spflInfo;

import android.app.Application;

public class ProjectApplication extends FrontiaApplication {
	public ProjectApplication() {
		URL = "http://218.93.51.170:8002/Service1.asmx";
		//URL = "http://192.168.1.192/Bespeak/Service1.asmx";
		orderDetail = new ArrayList<t_SpdaInfo>();
		df = new DecimalFormat("#.00");
		peisongfei=0;
		peisongfeiFree=-1;
	}

	public String URL;

	/**
	 * ����
	 */
	public BadgeView badge;

	/**
	 * ��Ʒ��Ϣ
	 */
	public List<t_spflInfo> goodsCategoryList;

	/**
	 * ��Ա��Ϣ
	 */
	public t_HydaInfo hyda;

	/**
	 * ��ö�������
	 * 
	 * @return ��������
	 */
	public int GetOrderCount() {
		if (goodsCategoryList != null) {
			int count = 0;
			for (t_spflInfo spfl : goodsCategoryList) {
				for (t_SpdaInfo spda : spfl.goodsList) {
					count += spda.OrderAmcount;
				}
			}
			return count;
		} else {
			return 0;
		}
	}

	/**
	 * ������Ʒ��Ϣ
	 */
	public List<t_SpdaInfo> orderDetail;

	/**
	 * ���϶�����Ʒ
	 */
	public void GetOrderDetail() {
		if (goodsCategoryList != null) {
			orderDetail = new ArrayList<t_SpdaInfo>();
			for (t_spflInfo spfl : goodsCategoryList) {
				for (t_SpdaInfo spda : spfl.goodsList) {
					if (spda.OrderAmcount > 0) {
						orderDetail.add(spda);
					}
				}
			}
		}
	}
	
	/**
	 * ��ö������н��
	 * @return
	 */
	public double GetTotalMoney(){
		double totalMoney=0;
		for(t_SpdaInfo spda :orderDetail){
			totalMoney+=spda.c_Spsj*spda.OrderAmcount;
		}
		return m2(totalMoney);
	}

	/**
	 * ��ʽ����
	 */
	private DecimalFormat df;

	/**
	 * ����2ΪС��
	 * 
	 * @param f
	 */
	public double m2(double f) {
		return Double.parseDouble(df.format(f));
	}
	
	/**
	 * ���ͷ�
	 */
	public double peisongfei;
	
	/**
	 * �����������ͷ�
	 */
	public double peisongfeiFree;

}
