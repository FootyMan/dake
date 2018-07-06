package com.unicom.utils;


import java.io.OutputStream;
import java.io.PrintWriter;

import java.security.MessageDigest;
import java.text.DecimalFormat;

import java.util.Iterator;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
public class CommonMethod {

	private static ObjectMapper mapper = new ObjectMapper();

	/**
	 * �Ѷ���װ����JSON
	 * 
	 * @param obj
	 * @return
	 * @throws JsonProcessingException
	 */
	public static String ConvertObjToJson(Object obj) throws JsonProcessingException {
		// ��NULLȥ��
		mapper.setSerializationInclusion(Include.NON_NULL);
		String json = mapper.writeValueAsString(obj);
		return json;
	}

	public static void response(String data, HttpServletResponse response) {
		PrintWriter pw = null;
		try {
			response.setHeader("content-type", "text/html;charset=UTF-8");
			pw = response.getWriter();
			pw.write(data);
			pw.flush();
		} catch (Exception e) {
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
	}

	/**
	 * ��JSON ת���ɶ���
	 * 
	 * @param objJSON
	 * @param obj
	 * @return
	 * @throws IOException
	 */
	public static <T> T ConvertJsonToObj(String objJSON, Class<T> obj) throws IOException {
		T t = mapper.readValue(objJSON, obj);
		/**
		 * ��ǩ ��ʱ���� ����Щ�������ǽӿڡ�
		 */
		/*
		 * boolean isSign = Boolean.parseBoolean(GetConfigValue("isSign")); if
		 * (isSign) {
		 * 
		 * @SuppressWarnings("unchecked") BaseRequestModel<Object> baseObj =
		 * (BaseRequestModel<Object>) t; if (baseObj != null) { String sign =
		 * "20160323" + baseObj.getTimeStamp() + baseObj.getRandomCode(); //
		 * byte[] data = sign.getBytes(); String md5sign =
		 * bytesToLowerCaseString(md5(sign)); if
		 * (!baseObj.getSign().equals(md5sign)) { return null; } } }
		 */

		return t;
	}

	/**
	 * ���JSON���ݵ��ͻ���
	 * 
	 * @param response
	 * @param jsonContent
	 * @throws Exception
	 */
	public static void ResponseJsonToClient(HttpServletResponse response, String jsonContent) throws Exception {
		OutputStream outputStream = response.getOutputStream();// ��ȡOutputStream�����
		response.setHeader("content-type", "text/html;charset=UTF-8");// ͨ��������Ӧͷ�����������UTF-8�ı�����ʾ���ݣ����������仰����ô�������ʾ�Ľ�������
		/**
		 * data.getBytes()��һ�����ַ�ת�����ֽ�����Ĺ��̣����������һ����ȥ�����
		 * ��������ĵĲ���ϵͳ������Ĭ�Ͼ��ǲ��Ҳ�GB2312����� ���ַ�ת�����ֽ�����Ĺ��̾��ǽ������ַ�ת����GB2312������϶�Ӧ������
		 * ���磺 "��"��GB2312������϶�Ӧ��������98 "��"��GB2312������϶�Ӧ��������99
		 */
		/**
		 * getBytes()�������������������ô�ͻ���ݲ���ϵͳ�����Ի�����ѡ��ת�������������Ĳ���ϵͳ����ô��ʹ��GB2312�����
		 */
		byte[] dataByteArr = jsonContent.getBytes("UTF-8");// ���ַ�ת�����ֽ����飬ָ����UTF-8�������ת��
		outputStream.write(dataByteArr);// ʹ��OutputStream����ͻ�������ֽ�����
	}

	/**
	 * ��ȡ�������������������
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static String ReadInputStreamFromClientRequest(HttpServletRequest request) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = in.readLine()) != null) {
			sb.append(line);
		}
		return sb.toString();
	}

	public static String WriteLog(HttpServletRequest request, String key) {
		String value = request.getServletContext().getInitParameter(key);
		return value;
	}

	/**
	 * ��ȡweb.xml ���� context-param ֵ
	 * 
	 * @param request
	 * @param key
	 * @return
	 */
	public static String ReadWebConfigValue(HttpServletRequest request, String key) {
		String value = request.getServletContext().getInitParameter(key);
		return value;
	}

	/**
	 * ��ȡXML�ļ���ֵ
	 * 
	 * @param key
	 * @return
	 */
	public static String GetConfigValue(String key) {

		Properties props = new Properties();
		String value = "";
		// xml�����·��
		String xmlpath = "key-value.xml"; // ��XML�ŵ����ļ����£�������·��

		ClassLoader classLoader = CommonMethod.class.getClassLoader(); // ����XmlReadLearn�ǵ�ǰ��
		InputStream in = classLoader.getResourceAsStream(xmlpath);

		try {
			props.loadFromXML(in);
			value = props.getProperty(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * MD5摘要
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] md5(String strOrgin) {
		try {
			byte[] data = strOrgin.getBytes();
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(data);
			return md5.digest();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ת��д
	 * 
	 * @param data
	 * @param charCase
	 * @return
	 */
	public static String bytesToUpperCaseString(byte[] data) {
		StringBuilder result = new StringBuilder("");
		if (data == null || data.length <= 0) {
			return null;
		}
		for (int i = 0; i < data.length; i++) {
			int v = data[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				result.append(0);
			}
			result.append(hv);
		}
		return result.toString().toUpperCase();

	}

	/**
	 * Сд
	 * 
	 * @param data
	 * @return
	 */
	public static String bytesToLowerCaseString(byte[] data) {
		StringBuilder result = new StringBuilder("");
		if (data == null || data.length <= 0) {
			return null;
		}
		for (int i = 0; i < data.length; i++) {
			int v = data[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				result.append(0);
			}
			result.append(hv);
		}
		return result.toString().toLowerCase();

	}

//	public static void SendShortMessage(String phoneNumber, String msgContent) {
//
//		try {
//			PhoneServiceLocator service = new PhoneServiceLocator();
//			java.net.URL url = new java.net.URL("http://service.luxuriesclub.com/PhoneService.asmx?WSDL");
//			PhoneServiceSoapStub stub = new PhoneServiceSoapStub(url, service);
//			String x2 = stub.XSCreateBlueMessage(phoneNumber, msgContent);
//			System.out.println(x2);
//
//		} catch (RemoteException e) {
//			// TODO Auto-generated catch block20
//			e.printStackTrace();
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block23
//			e.printStackTrace();
//		}
//	}

	/**
	 * ʹ��ImageReader��ȡͼƬ��߱�
	 * 
	 * @param src
	 *            ԴͼƬ·��
	 */
	public static String getImageSizeByImageReader(String src) {

		String dd = "0.00";
		File file = new File(src);
		try {
			Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("jpg");
			ImageReader reader = (ImageReader) readers.next();
			ImageInputStream iis = ImageIO.createImageInputStream(file);
			reader.setInput(iis, true);
			System.out.println("width:" + reader.getWidth(0));
			System.out.println("height:" + reader.getHeight(0));

			float scale = reader.getWidth(0) / reader.getHeight(0);
			DecimalFormat fnum = new DecimalFormat("##0.00");
			dd = fnum.format(scale);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return dd;
	}
	/**
	 * 获取IP
	 * @param request
	 * @return
	 */
	public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if(StringUtils.isNotEmptyString(ip) && !"unKnown".equalsIgnoreCase(ip)){
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if(index != -1){
                return ip.substring(0,index);
            }else{
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if(StringUtils.isNotEmptyString(ip) && !"unKnown".equalsIgnoreCase(ip)){
            return ip;
        }
        return request.getRemoteAddr();
    }
}
