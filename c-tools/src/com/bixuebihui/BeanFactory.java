package com.bixuebihui;

/**
 * 单例模式 spring bean 初始化和获取
 */
public class BeanFactory {

    private static SpringBeanZK springBeanZK = new SpringBeanZK();

    private BeanFactory(){
        throw new IllegalStateException("Utility class");
    }

    public static Object createObjectById(String objectId) {
        return springBeanZK.getBean(objectId);

    }

    public static void destroy() {
        springBeanZK.destroy();
    }


    public static boolean isSinglton(String beanId) {
        return springBeanZK.isSinglton(beanId);
    }


    public static String[] getBeanDefinitionNames() {
        return springBeanZK.getBeanDefinitionNames();
    }


}
