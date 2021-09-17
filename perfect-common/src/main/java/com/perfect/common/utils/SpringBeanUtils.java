package com.perfect.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * Spring Context 工具类
 * 
 * @author
 *
 */
@Slf4j
@Component
public class SpringBeanUtils implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (SpringBeanUtils.applicationContext == null) {
			SpringBeanUtils.applicationContext = applicationContext;
		}
		log.debug("========ApplicationContext配置成功========");
		log.debug("========在普通类可以通过调用SpringUtils.getAppContext()获取applicationContext对象========");
		log.debug("========applicationContext=" + SpringBeanUtils.applicationContext + "========");
	}

	/**
	 * 获取applicationContext
	 *
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 通过name获取 Bean.
	 *
	 * @param beanName
	 * @return
	 */
	public static Object getBean(String beanName) throws ClassNotFoundException {
		Class<?> type = Class.forName(beanName);
		return getApplicationContext().getBean(type);
	}

	/**
	 * 通过class获取Bean.
	 *
	 * @param clazz
	 * @return
	 */
	public static <T> T getBean(Class<T> clazz) {
		return getApplicationContext().getBean(clazz);
	}

	/**
	 * 通过name,以及Clazz返回指定的Bean
	 *
	 * @param name
	 * @param clazz
	 * @return
	 */
	public static <T> T getBean(String name, Class<T> clazz) {
		return getApplicationContext().getBean(name, clazz);
	}


	public static Object invokeMethod(String beanName, String methodName) throws ClassNotFoundException {
		// 获取bean,注意这里用实现类的接口强转去获得目标bean的代理对象，才能成功执行下面的反射方法
		Object obj = SpringBeanUtils.getBean(beanName);
		// 获取方法，这个selectAll实际上是父类的方法
		Method method = ReflectionUtils.findMethod(obj.getClass(), methodName);
		// 反射执行方法
		Object objRe = ReflectionUtils.invokeMethod(method, obj);
		return objRe;
	}

	public static Object invokeMethod(String beanName, String methodName, Class<?>[] paramTypes, Object... args) throws ClassNotFoundException {
		// 获取bean,注意这里用实现类的接口强转去获得目标bean的代理对象，才能成功执行下面的反射方法
		Object obj = SpringBeanUtils.getBean(beanName);
		// 获取方法，这个selectAll实际上是父类的方法
		Method method = ReflectionUtils.findMethod(obj.getClass(), methodName, paramTypes);
		// 反射执行方法
		Object objRe = ReflectionUtils.invokeMethod(method, obj, args);
		return objRe;
	}
}