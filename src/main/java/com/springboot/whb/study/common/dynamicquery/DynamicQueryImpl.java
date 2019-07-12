package com.springboot.whb.study.common.dynamicquery;

import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * @author: whb
 * @date: 2019/7/12 18:30
 * @description: 动态查询接口实现
 */
public class DynamicQueryImpl implements DynamicQuery {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Object o) {
        entityManager.persist(o);
    }

    @Override
    public void update(Object o) {
        entityManager.merge(o);
    }

    @Override
    public Object queryObject(String nativeSql, Object... params) {
        return createNativeQuery(nativeSql, params).getSingleResult();
    }

    @Override
    public Object[] queryArray(String nativeSql, Object... params) {
        return (Object[]) createNativeQuery(nativeSql, params).getSingleResult();
    }

    @Override
    public int executeUpdate(String nativeSql, Object... params) {
        return createNativeQuery(nativeSql, params).executeUpdate();
    }

    @Override
    public List queryListMap(String nativeSql, Object... params) {
        Query query = createNativeQuery(nativeSql, params);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    @Override
    public List queryList(String nativeSql, Object... params) {
        Query query = createNativeQuery(nativeSql, params);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.TO_LIST);
        return query.getResultList();
    }

    @Override
    public void delte(Class clazz, Object[] ids) {
        for (Object id : ids) {
            entityManager.remove(entityManager.getReference(clazz, id));
        }
    }

    @Override
    public void delete(Class clazz, Object id) {
        delete(clazz, new Object[]{id});
    }

    /**
     * 封装Queyr对象
     *
     * @param sql    动态Sql
     * @param params 参数
     * @return
     */
    private Query createNativeQuery(String sql, Object... params) {
        Query query = entityManager.createNativeQuery(sql);
        if (null != params && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                //jpa的query从位置1开始
                query.setParameter(i + 1, params[i]);
            }
        }
        return query;
    }
}
