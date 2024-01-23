package com.luo.delayqueue.tools;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.StringUtils;
import org.springframework.util.SystemPropertyUtils;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/***
 * 扫描 指定包下的所有类
 *
 * @author HadLuo
 * @since JDK1.7
 * @history 2018年8月15日 新建
 */
public class PackageScans {
	/***
	 * 黑名单包(包名只要包含关键字的就不扫描) ，不扫描到
	 */
	private static final String[] BLACK_KEY_NAMES = {};
	
	private final static ClassLoaderWrapper classloader = ClassLoaderWrapper.getWrapper();
	
	// 扫描 scanPackages 下的文件的匹配符
	protected static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
	
	/***
	 * 根据扫描包的,查询下面的所有类 ,多个包以逗号分隔
	 *
	 * @param scanPackages
	 * @return
	 * @author HadLuo 2018年8月15日 新建
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Set<Class<?>> findPackageClass(String scanPackages, ClassLoader classLoader,
												 Class<? extends Annotation> annotationClass) throws IOException, ClassNotFoundException {
		if (StringUtils.isEmpty(scanPackages)) {
			return Collections.emptySet();
		}
		// 验证及排重包路径,避免父子路径多次扫描
		Set<String> packages = checkPackage(scanPackages);
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
		Set<Class<?>> clazzSet = new HashSet<Class<?>>();
		for (String basePackage : packages) {
			if (StringUtils.isEmpty(basePackage)) {
				continue;
			}
			String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
					+ org.springframework.util.ClassUtils.convertClassNameToResourcePath(
					SystemPropertyUtils.resolvePlaceholders(basePackage))
					+ "/" + DEFAULT_RESOURCE_PATTERN;
			Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
			for (Resource resource : resources) {
				Class<?> clazz = null;
				try {
					String name = loadClassName(metadataReaderFactory, resource);
					boolean inBlack = false;
					// 判断包名 是否包含黑名单 关键字
					for (String keys : BLACK_KEY_NAMES) {
						if (name.contains(keys)) {
							inBlack = true;
							break;
						}
					}
					if (inBlack) {
						// 在黑名单， 不扫描
						continue;
					}
					if (classLoader != null) {
						clazz = classloader.classForName(name, classLoader);
					} else {
						clazz = classloader.classForName(name);
					}
				} catch (Throwable e) {
					continue;
				}
				if (annotationClass != null) {
					// 配置了指定注解,才加入
					if (clazz.isAnnotationPresent(annotationClass)) {
						clazzSet.add(clazz);
					} else {
						continue;
					}
				} else {
					// 全量加入
					clazzSet.add(clazz);
				}
			}
		}
		return clazzSet;
	}
	
	public static Set<Class<?>> findPackageClass(String scanPackages) throws IOException, ClassNotFoundException {
		return findPackageClass(scanPackages, null, null);
	}
	
	public static Set<Class<?>> findPackageClass(String scanPackages, Class<? extends Annotation> annotationClass)
			throws IOException, ClassNotFoundException {
		return findPackageClass(scanPackages, null, annotationClass);
	}
	
	/***
	 * 排重、检测package父子关系，避免多次扫描
	 *
	 * @param scanPackages
	 * @return
	 * @author HadLuo 2018年8月15日 新建
	 */
	private static Set<String> checkPackage(String scanPackages) {
		if (StringUtils.isEmpty(scanPackages)) {
			return Collections.emptySet();
		}
		Set<String> packages = new HashSet<String>();
		// 排重路径
		Collections.addAll(packages, scanPackages.split(","));
		for (String pInArr : packages.toArray(new String[packages.size()])) {
			if (StringUtils.isEmpty(pInArr) || pInArr.equals(".") || pInArr.startsWith(".")) {
				continue;
			}
			if (pInArr.endsWith(".")) {
				pInArr = pInArr.substring(0, pInArr.length() - 1);
			}
			Iterator<String> packageIte = packages.iterator();
			boolean needAdd = true;
			while (packageIte.hasNext()) {
				String pack = packageIte.next();
				if (pInArr.startsWith(pack + ".")) {
					// 如果待加入的路径是已经加入的pack的子集，不加入
					needAdd = false;
				} else if (pack.startsWith(pInArr + ".")) {
					// 如果待加入的路径是已经加入的pack的父集，删除已加入的pack
					packageIte.remove();
				}
			}
			if (needAdd) {
				packages.add(pInArr);
			}
		}
		return packages;
	}
	
	/***
	 * 加载资源，根据resource获取className
	 *
	 * @param metadataReaderFactory
	 * @param resource
	 * @return
	 * @throws IOException
	 * @author HadLuo 2018年8月15日 新建
	 */
	private static String loadClassName(MetadataReaderFactory metadataReaderFactory, Resource resource)
			throws IOException {
		if (resource.isReadable()) {
			MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
			if (metadataReader != null) {
				return metadataReader.getClassMetadata().getClassName();
			}
		}
		return null;
	}
}