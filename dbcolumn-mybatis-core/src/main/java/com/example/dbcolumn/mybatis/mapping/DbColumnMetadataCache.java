package com.example.dbcolumn.mybatis.mapping;

import com.example.dbcolumn.mybatis.annotation.DbColumn;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 클래스별 {@code @DbColumn} 매핑 정보(컬럼명 → 필드명)를 리플렉션으로
 * 한 번만 계산해 캐싱합니다. 클래스가 처음 조회될 때만 필드를 스캔하고,
 * 이후에는 캐시된 불변 맵을 반환합니다.
 */
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

    public static String findProperty(Class<?> type, String columnName) {
        return getMapping(type).get(columnName);
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
