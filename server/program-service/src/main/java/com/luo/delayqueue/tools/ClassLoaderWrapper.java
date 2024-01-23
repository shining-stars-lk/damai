package com.luo.delayqueue.tools;

/***
 * 类加载器包装， 专门用于 加载类
 *
 * @author HadLuo
 * @since JDK1.7
 * @history 2018年8月15日 新建
 */
public class ClassLoaderWrapper {
	ClassLoader defaultClassLoader;
	ClassLoader systemClassLoader;

	private ClassLoaderWrapper() {
		try {
			systemClassLoader = ClassLoader.getSystemClassLoader();
		} catch (SecurityException ignored) {
		}
	}

	public static ClassLoaderWrapper getWrapper() {
		return new ClassLoaderWrapper();
	}

	public Class<?> classForName(String name) throws ClassNotFoundException {
		return classForName(name, getClassLoaders(null));
	}

	public Class<?> classForName(String name, ClassLoader classLoader) throws ClassNotFoundException {
		return classForName(name, getClassLoaders(classLoader));
	}
	
	/***
	 * 加载类
	 *
	 * @param name
	 * @param classLoader
	 * @return
	 * @throws ClassNotFoundException
	 * @author HadLuo 2018年8月15日 新建
	 */
	private Class<?> classForName(String name, ClassLoader[] classLoader) throws ClassNotFoundException {
		for (ClassLoader cl : classLoader) {
			if (null != cl) {
				try {
					Class<?> c = Class.forName(name, true, cl);
					if (null != c) {
						return c;
					}
				} catch (ClassNotFoundException e) {
				}
			}
		}
		throw new ClassNotFoundException("�Ҳ����� : " + name);
	}
	
	/***
	 * 获取 加载器， 注意 类加载器的顺序
	 *
	 * @param classLoader
	 * @return
	 * @author HadLuo 2018年8月15日 新建
	 */
	private ClassLoader[] getClassLoaders(ClassLoader classLoader) {
		return new ClassLoader[] { classLoader, defaultClassLoader, Thread.currentThread().getContextClassLoader(),
				getClass().getClassLoader(), systemClassLoader };
	}
}