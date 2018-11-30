package com.xczg.blockchain.yibaodapp.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("deprecation")
public class HttpUtil {  
	@SuppressWarnings("unused")
	private static RequestConfig requestConfig;
	private static final int MAX_TIMEOUT = 7000;

	static {
		RequestConfig.Builder configBuilder = RequestConfig.custom();
		// 设置连接超时
		configBuilder.setConnectTimeout(MAX_TIMEOUT);
		// 设置读取超时
		configBuilder.setSocketTimeout(MAX_TIMEOUT);
		// 设置从连接池获取连接实例的超时
		configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);
		// 在提交请求之前 测试连接是否可用
		configBuilder.setStaleConnectionCheckEnabled(true);
		requestConfig = configBuilder.build();
	}
	
	/**
	 * post发送json格式的数据
	 * 
	 * @param url
	 * @param json
	 * @return
	 */
	public static String doPostJson(String url, String json) throws Exception {
		HttpClient httpClient = new DefaultHttpClient();

		// httpClient = WebClientDevWrapper.wrapClient(httpClient);
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("Content-type", "application/json;charset=utf-8");
		httpPost.addHeader("Accept", "application/json");
		httpPost.setEntity(new StringEntity(json, Charset.forName("UTF-8")));
		HttpResponse response = httpClient.execute(httpPost);
		int status = response.getStatusLine().getStatusCode();
		if (status >= 200 && status < 300) {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String result = EntityUtils.toString(entity);
				return result;
			}
		} else {
			System.out.println("status=" + status+"  url="+url + " json="+json);
		}
		return null;
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			if(null !=param){
				url=url+"?"+JSONObject.parseObject(param, Map.class).toString().replace("{", "").replace("}", "").replaceAll(", ", "&");
				param="";
			}
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Content-Type", "image/jpeg");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	public static void main(String[] args) {
		// http://113.6.246.26:49967/ebaoAPI/api/prescription/siuserdetail.do?certNo=410205197204080024&realName=常爱梅
//		JSONObject req = new JSONObject();
//		req.put("certNo", "410205197204080024");
//		req.put("realName", "常爱梅");
//		System.out.println(req.toJSONString());
//		String res=HttpUtil.sendPost(
//				"http://113.6.246.26:49967/ebaoAPI/api/prescription/siuserdetail.do",
//				req.toJSONString());
//		System.out.println(res);

	}

};