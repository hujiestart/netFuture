package com.idig8.api.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 
 * API网关这个方法需要往外爆露出去
 * 
 * @author idig8.com
 *
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface APIMapping {
    String value();
    boolean useLogin() default false;
}
