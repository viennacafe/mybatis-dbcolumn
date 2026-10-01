package com.example.dbcolumn.mybatis.wrapper;

import com.example.dbcolumn.mybatis.mapping.DbColumnMetadataCache;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.wrapper.BeanWrapper;

/**
 * 컬럼명을 {@code @DbColumn} 매핑 정보로 먼저 조회하고,
 * 매핑이 없으면 MyBatis 기본 동작({@code useCamelCaseMapping} 포함)으로
 * 위임하는 {@link BeanWrapper}입니다.
 */
public class DbColumnBeanWrapper extends BeanWrapper {

    private final Class<?> targetType;

    public DbColumnBeanWrapper(MetaObject metaObject, Object object) {
        super(metaObject, object);
        this.targetType = object.getClass();
    }

    @Override
    public String findProperty(String name, boolean useCamelCaseMapping) {
        String property = DbColumnMetadataCache.findProperty(targetType, name);

        if (property != null) {
            return property;
        }

        return super.findProperty(name, useCamelCaseMapping);
    }
}
