package com.example.bespeak.model;

import java.io.Serializable;

public class ddmx implements Serializable {
    public ddmx()
    {
        c_Spbm = "";
        c_Spmc = "";
        c_Ddsl = 0;
        c_Ddje = 0;
    }
	
	 /// <summary>
    /// 商品编码
    /// </summary>
    public String c_Spbm ;

    /// <summary>
    /// 商品名称
    /// </summary>
    public String c_Spmc ;

    /// <summary>
    /// 订单数量
    /// </summary>
    public double c_Ddsl ;

    /// <summary>
    /// 订单金额
    /// </summary>
    public double c_Ddje ;
}
