package com.unicom.requestresponse;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ����json��Ϣ����
 * 
 * @author sdcuike
 *
 *         Created At 2016��10��26�� ����8:54:08
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResponseEncryptBody {

}

