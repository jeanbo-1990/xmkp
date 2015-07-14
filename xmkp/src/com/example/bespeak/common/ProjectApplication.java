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
	 * 数字
	 */
	public BadgeView badge;

	/**
	 * 商品信息
	 */
	public List<t_spflInfo> goodsCategoryList;

	/**
	 * 会员信息
	 */
	public t_HydaInfo hyda;

	/**
	 * 获得订购数量
	 * 
	 * @return 订购数量
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
	 * 订购商品信息
	 */
	public List<t_SpdaInfo> orderDetail;

	/**
	 * 整合订购商品
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
	 * 获得订购所有金额
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
	 * 格式化类
	 */
	private DecimalFormat df;

	/**
	 * 保留2为小数
	 * 
	 * @param f
	 */
	public double m2(double f) {
		return Double.parseDouble(df.format(f));
	}
	
	/**
	 * 配送费
	 */
	public double peisongfei;
	
	/**
	 * 满多少免配送费
	 */
	public double peisongfeiFree;

}
