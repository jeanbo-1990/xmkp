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
    /// ��Ʒ����
    /// </summary>
    public String c_Spbm ;

    /// <summary>
    /// ��Ʒ����
    /// </summary>
    public String c_Spmc ;

    /// <summary>
    /// ��������
    /// </summary>
    public double c_Ddsl ;

    /// <summary>
    /// �������
    /// </summary>
    public double c_Ddje ;
}
