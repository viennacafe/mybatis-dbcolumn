package com.example.demo.mybatis.wrapper;

import com.example.demo.mybatis.mapping.DbColumnMetadataCache;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.wrapper.ObjectWrapper;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;

public class DbColumnObjectWrapperFactory implements ObjectWrapperFactory {

    @Override
    public boolean hasWrapperFor(Object object) {
        return object != null
                && DbColumnMetadataCache.hasDbColumn(object.getClass());
    }

    @Override
    public ObjectWrapper getWrapperFor(MetaObject metaObject, Object object) {
        return new DbColumnBeanWrapper(metaObject, object);
    }
}
