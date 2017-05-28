package com.github.TheDwoon.robots.gui;

import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.FutureTask;

/**
 * Created by sigmar on 28.05.17.
 */
public class FxThreadAdapter implements InvocationHandler {

    private static final Logger log = LogManager.getLogger();

    public static Object create(Object instance, Class<?>... interfaces) {
        return Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), interfaces,
                new FxThreadAdapter(instance));
    }

    @SuppressWarnings("unchecked")
    public static <T> T create(Object instance, Class<T> iface) {
        return (T) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[] { iface },
                new FxThreadAdapter(instance));
    }

    private final Object instance;

    public FxThreadAdapter(Object instance) {
        this.instance = instance;
    }

    @Override
    public Object invoke(final Object proxy, Method method, final Object[] args) throws Throwable {
        if (!method.getDeclaringClass().isInterface()) {
            return method.invoke(instance, args);
        }
        if (method.getReturnType().equals(Void.TYPE)) {
            Platform.runLater(() -> {
                try {
                    method.invoke(instance, args);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    log.catching(e);
                }
            });
            return null;
        } else {
            FutureTask<Object> task = new FutureTask<>(() -> method.invoke(instance, args));
            Platform.runLater(task);
            return task.get();
        }
    }
}
