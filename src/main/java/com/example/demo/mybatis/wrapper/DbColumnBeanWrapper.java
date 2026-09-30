package com.example.demo.mybatis.wrapper;

import com.example.demo.mybatis.mapping.DbColumnMetadataCache;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.wrapper.BeanWrapper;

import java.util.Map;

public class DbColumnBeanWrapper extends BeanWrapper {

    private final Map<String, String> columnToProperty;

    public DbColumnBeanWrapper(MetaObject metaObject, Object object) {
        super(metaObject, object);
        this.columnToProperty = DbColumnMetadataCache.getMapping(object.getClass());
    }

    @Override
    public String findProperty(String name, boolean useCamelCaseMapping) {
        String property = columnToProperty.get(name);

        if (property != null) {
            return property;
        }

        return super.findProperty(name, useCamelCaseMapping);
    }
}
