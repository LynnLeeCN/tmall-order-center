package com.github.lynnleecn.tmall.config;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.github.lynnleecn.tmall.config.constant.Constants;

/**
 * Invoke TaoBao API by HTTP Request(Don't need TaoBao SDK)
 * 
 * @author LynnLee
 *
 */
public class ConnectionConfig {

	/*
	 * App key
	 */
	private static final String APP_KEY = "app_key";
	/*
	 * json or xml
	 */
	private static final String FORMAT = "format";
	/*
	 * API method name
	 */
	private static final String METHOD = "method";
	/*
	 * Request timestamp
	 */
	private static final String TIMESTAMP = "timestamp";
	/*
	 * Version
	 */
	private static final String VERSION = "v";
	/*
	 * Signature
	 */
	private static final String SIGN = "sign";
	/*
	 * Signature method:md5(defalut)
	 */
	private static final String SIGN_METHOD = "sign_method";
	/*
	 * TODO
	 */
	private static final String PARTNER_ID = "partner_id";
	/*
	 * Session key(or token)
	 */
	private static final String SESSION = "session";
	/*
	 * TODO
	 */
	private static final String SIMPLIFY = "simplify";

	/*
	 * configure properties file path
	 */
	private static final String PATH = "connection.properties";

	private static final String serverUrl;

	private static final String appKey;

	private static final String appSecret;

	// TODO callback function
	private static String token;

	static {

		Properties properties = new Properties();

		try {
			properties.load(ConnectionConfig.class.getClassLoader().getResourceAsStream(PATH));
		} catch (IOException e) {
			// file not found
			throw new RuntimeException(e.getMessage());
		}

		serverUrl = properties.getProperty("serverUrl");
		appKey = properties.getProperty("appKey");
		appSecret = properties.getProperty("appSecret");

	}

	public static String getResponse(String apiName, TreeMap<String, String> params) {
		return getResponse(apiName, params, Constants.FORMAT_JSON);
	}

	/**
	 * Acquire API result
	 * 
	 * @param apiName
	 *            Taobao API name(eg. taobao.trades.sold.get)
	 * @param params
	 *            contains key&value
	 * @param format
	 *            json/xml
	 * @return API result format by json/xml
	 */
	public static String getResponse(String apiName, TreeMap<String, String> params, String format) {

		// Basic parameters
		params.put(SIGN_METHOD, Constants.SIGN_METHOD_MD5);
		params.put(VERSION, "2.0");
		// Configure parameters
		params.put(METHOD, apiName);
		params.put(FORMAT, format);
		params.put(TIMESTAMP, DateFormatUtils.format(new Date(), Constants.DATE_TIME_FORMAT));
		// Authentication
		params.put(APP_KEY, appKey);
		params.put(SESSION, ConnectionUtil.getToken());
		params.put(SIGN, ConnectionUtil.md5Signature(params, appSecret));
		// construct HTTP request parameter
		StringBuffer httpParamBuff = new StringBuffer();
		for (Iterator<Map.Entry<String, String>> it = params.entrySet().iterator(); it.hasNext();) {
			Map.Entry<String, String> e = it.next();
			httpParamBuff.append("&").append(e.getKey()).append("=").append(e.getValue());
		}
		String httpParam = httpParamBuff.toString().substring(1);

		String response = ConnectionUtil.getResult(serverUrl, httpParam);

		return response;
	}
}
