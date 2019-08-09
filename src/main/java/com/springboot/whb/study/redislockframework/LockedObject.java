package com.springboot.whb.study.redislockframework;

import java.lang.annotation.*;

/**
 * 
 * @author liushao
 *
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LockedObject {
	

}
