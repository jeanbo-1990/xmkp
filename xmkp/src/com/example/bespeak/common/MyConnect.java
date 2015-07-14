package com.example.bespeak.common;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

public class MyConnect {

	/**
	 * 
	 */
	public SoapObject request;

	/**
	 * soap请求对象
	 */
	private final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
			SoapEnvelope.VER11);

	/**
	 * http访问对象
	 */
	private final HttpTransportSE transport = new HttpTransportSE(
			"http://192.168.10.102/Bespeak/Service1.asmx");

	/**
	 * webservice命名空间
	 */
	private final String webServiceNameSpace = "http://tempuri.org/";

	private static String SOAP_ACTION = "http://WebXml.com.cn/";

	public MyConnect() {

	}

	/**
	 * 从webservice 加载数据
	 * @param FunctionCode 功能代码
	 * @param Parameters 参数
	 * @return 数据json
	 */
	public String GetDataFromServer(String FunctionCode, String... Parameters) {
		try {
		request = new SoapObject(webServiceNameSpace, FunctionCode);
		for (String parameter : Parameters) {
			request.addProperty("", parameter);
		}
		envelope.bodyOut = request;
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		
			transport.call(SOAP_ACTION, envelope);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SoapObject sb = (SoapObject) envelope.bodyIn;

		return sb.toString();
	}
}
