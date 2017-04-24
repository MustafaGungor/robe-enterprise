package com.mebitech.robe.persistence.jpa.dao;

import com.mebitech.robe.persistence.api.criteria.Criteria;
import com.mebitech.robe.persistence.api.criteria.Result;
import com.mebitech.robe.persistence.api.criteria.Transformer;
import com.mebitech.robe.persistence.api.dao.BaseDao;
import com.mebitech.robe.persistence.api.query.Query;
import com.mebitech.robe.persistence.api.query.search.SearchModel;
import com.mebitech.robe.persistence.jpa.criteria.TransformerImpl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

/**
 * Basic Dao Class which limits {@link BaseDao} to take
 *
 * @param <T> Type of the entity parameter.
 */
public class BaseDaoImpl<T, ID extends Serializable> implements BaseDao<T, ID> {

    @PersistenceContext
    protected EntityManager entityManager;

    protected Class<T> entityClass;

    public BaseDaoImpl() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
    }

    @Override
    public Criteria<T> queryAllStrict(SearchModel search) {
        Query<T> query = new Query<>(new TransformerImpl<T>(this.entityManager));
        return query.createCriteria(this.entityClass, search);
    }

    @Override
    public Criteria<Map<String, Object>> queryAll(SearchModel search) {
        Transformer<Map<String, Object>> transformer = new TransformerImpl<>(this.entityManager, Criteria.MAP_CLASS);
        Query<Map<String, Object>> query = new Query<>(transformer);
        return query.createCriteria(this.entityClass, search);
    }

    @Override
    public <E> Criteria<E> queryAll(SearchModel search, Class<E> transformClass) {
        Query<E> query = new Query<>(new TransformerImpl<>(this.entityManager, transformClass));
        return query.createCriteria(this.entityClass, search);
    }

    @Override
    public Result<T> findAllStrict(SearchModel search) {
        return queryAllStrict(search).pairList();
    }

    @Override
    public Result<Map<String, Object>> findAll(SearchModel search) {
        return queryAll(search).pairList();
    }

    @Override
    public <E> Result<E> findAll(SearchModel search, Class<E> transformClass) {
        return queryAll(search, transformClass).pairList();
    }

    @Override
    public List<T> findAll() {
        javax.persistence.Query query = entityManager.createQuery("SELECT e FROM " + this.entityClass.getName() + " e");
        return query.getResultList();
    }

    @Override
    public T findById(Serializable oid) {
        return this.entityManager.find(this.entityClass, oid);
    }

    @Override
    public <E extends Serializable> E findById(Class<E> clazz, Serializable oid) {
        return this.entityManager.find(clazz, oid);
    }

    @Override
    public T create(T entity) {
        this.entityManager.persist(entity);
        return entity;
    }

    @Override
    public T update(T entity) {
        return this.entityManager.merge(entity);
    }

    @Override
    public T delete(ID id) {
        T entity = findById(id);
        this.entityManager.remove(entity);
        return entity;
    }

    @Override
    public T delete(T entity) {
        entity = this.entityManager.merge(entity);
        this.entityManager.remove(entity);
        return entity;
    }

    @Override
    public void flush() {
        this.entityManager.flush();
    }

    @Override
    public T merge(T entity) {
        return this.entityManager.merge(entity);
    }

    @Override
    public T detach(T entity) {
        this.entityManager.detach(entity);
        return entity;
    }
}