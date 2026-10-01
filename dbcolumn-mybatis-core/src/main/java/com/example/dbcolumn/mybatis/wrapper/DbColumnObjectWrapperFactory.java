package com.example.dbcolumn.mybatis.wrapper;

import com.example.dbcolumn.mybatis.mapping.DbColumnMetadataCache;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.wrapper.ObjectWrapper;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;

/**
 * {@code @DbColumn}이 하나라도 붙어 있는 클래스에 한해
 * {@link DbColumnBeanWrapper}를 적용하는 MyBatis {@link ObjectWrapperFactory}입니다.
 */
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
