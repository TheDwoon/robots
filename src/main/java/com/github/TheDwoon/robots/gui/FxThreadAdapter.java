package com.github.TheDwoon.robots.gui;

import com.github.TheDwoon.robots.game.entity.Entity;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
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
        return (T) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{iface},
                new FxThreadAdapter(instance));
    }

    private final Object instance;

    public FxThreadAdapter(Object instance) {
        this.instance = instance;
    }

    @Override
    public Object invoke(final Object proxy, Method method, final Object[] args) throws Throwable {
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(instance, args);
        }

        // TODO (sigmarw, 01. Jun 2017): debug, remove me
        // log.info("calling {}, arguments: {}", method.getName(), debugToString(args));

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

    // TODO (sigmarw, 01. Jun 2017): debug, remove me
    private static String debugToString(Object[] a) {
        if (a == null)
            return "null";

        int iMax = a.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            b.append(debugToString(a[i]));
            if (i == iMax)
                return b.append(']').toString();
            b.append(", ");
        }
    }

    // TODO (sigmarw, 01. Jun 2017): debug, remove me
    private static String debugToString(Object o) {
        if (o instanceof Entity) {
            Entity e = (Entity) o;
            return o.getClass().getName() + ':' + e.getUUID() + '[' + e.getX() + '|' + e.getY() + ']';
        } else {
            return String.valueOf(o);
        }
    }
}
