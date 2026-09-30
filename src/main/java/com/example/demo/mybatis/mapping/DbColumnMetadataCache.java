package com.example.demo.mybatis.mapping;

import com.example.demo.mybatis.annotation.DbColumn;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class DbColumnMetadataCache {

    private static final Map<Class<?>, Map<String, String>> CACHE =
            new ConcurrentHashMap<>();

    private DbColumnMetadataCache() {
    }

    public static Map<String, String> getMapping(Class<?> type) {
        return CACHE.computeIfAbsent(type, DbColumnMetadataCache::createMapping);
    }

    public static boolean hasDbColumn(Class<?> type) {
        return !getMapping(type).isEmpty();
    }

    private static Map<String, String> createMapping(Class<?> type) {
        Map<String, String> mapping = new LinkedHashMap<>();

        Class<?> current = type;

        while (current != null && current != Object.class) {
            for (Field field : current.getDeclaredFields()) {
                DbColumn annotation = field.getAnnotation(DbColumn.class);

                if (annotation == null) {
                    continue;
                }

                String columnName = annotation.value();
                String propertyName = field.getName();

                String previous = mapping.put(columnName, propertyName);

                if (previous != null) {
                    throw new IllegalStateException(
                            "Duplicate @DbColumn mapping found: "
                                    + type.getName()
                                    + " / column=" + columnName
                                    + " / properties=" + previous + "," + propertyName
                    );
                }
            }

            current = current.getSuperclass();
        }

        return Collections.unmodifiableMap(mapping);
    }
}
