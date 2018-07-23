package com.unicom.requestresponse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

//import com.api.utils.DES;

/**
 * @author sdcuike
 *
 *         Created At 2016��10��26�� ����11:59:07
 */
public abstract class RequestDecryptResponseEncryptBodyProcessor {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	public final String decryptRequestBody(HttpInputMessage inputMessage, Charset charset) throws IOException {
		InputStream inputStream = inputMessage.getBody();
		String input = IOUtils.toString(inputStream, charset);
		HttpHeaders httpHeaders = inputMessage.getHeaders();
		return doDecryptRequestBody(input, httpHeaders, charset);
	}

	public final String encryptResponseBody(String input, HttpHeaders httpHeaders, Charset charset) {
		return doEncryptResponseBody(input, httpHeaders, charset);
	}

	protected String doDecryptRequestBody(String input, HttpHeaders httpHeaders, Charset charset) {
		return input;
	}

	protected String doEncryptResponseBody(String input, HttpHeaders httpHeaders, Charset charset) {
		String jiami = input;
		try {
//			jiami = DES.encrypt(input);
			// System.out.println("���ܺ�����" + jiami);

			// String jimi = DES.decrypt(jiami);
			// System.out.println("���ܺ�����" + jimi);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return jiami;
	}

}
