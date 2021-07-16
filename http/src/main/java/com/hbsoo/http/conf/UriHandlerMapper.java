package com.hbsoo.http.conf;

import com.hbsoo.commons.utils.PackageUtil;
import com.hbsoo.http.controller.HttpController;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Stream;

/**
 * Created by zun.wei on 2021/7/16.
 */
@Slf4j
public final class UriHandlerMapper {

    public static final Map<String, HttpController> router = new HashMap<>();


    /**
     * 注册请求处理器
     */
    public static void init() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        log.info("\n\n==== 开始扫描注册请求处理器 ====");

        // 获取包名称
        final String packageName = HttpController.class.getPackage().getName();
        // 获取 HttpController 所有的实现类
        Set<Class<?>> clazzSet = PackageUtil.listSubClazz(packageName, true, HttpController.class);

        for (Class<?> handlerClazz : clazzSet) {
            if (null == handlerClazz ||
                    0 != (handlerClazz.getModifiers() & Modifier.ABSTRACT)) {
                continue;
            }

            // 获取方法数组
            Method[] methodArray = handlerClazz.getDeclaredMethods();
            Optional<Method> regUriMethodOpt = Stream.of(methodArray)
                    .filter(m -> m.getName().equals("regUri") && m.getParameters().length == 0)
                    .findFirst();
            boolean presentUri = regUriMethodOpt.isPresent();
            if (!presentUri) {
                continue;
            }
            Method regUriMethod = regUriMethodOpt.get();
            Object instance = handlerClazz.newInstance();
            String[] regUris = (String[]) regUriMethod.invoke(instance);
            for (String uri : regUris) {
                HttpController controller = router.get(uri);
                if (Objects.nonNull(controller)) {
                    throw new RuntimeException("http handler uri ["
                            + uri + "] is exist, handler name --::"
                            + controller.getClass().getName()
                            + ", but expect register to ---::" + handlerClazz.getName());
                }
                log.info("{}  <<==========>>  {}", uri, instance.getClass().getName());
                router.put(uri, (HttpController) instance);
            }

            log.info("==== 结束扫描注册请求处理器 ====\n\n");
        }
    }

}
