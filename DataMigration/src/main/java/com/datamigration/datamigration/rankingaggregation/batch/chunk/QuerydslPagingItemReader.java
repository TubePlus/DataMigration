package com.datamigration.datamigration.rankingaggregation.batch.chunk;


import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

// Chunk 설정
public class QuerydslPagingItemReader<T> extends AbstractPagingItemReader<T> {

    protected final Map<String, Object> jpaPropertyMap = new HashMap<>();
    protected EntityManagerFactory entityManagerFactory;
    protected EntityManager entityManager;
    protected Function<JPAQueryFactory, JPAQuery<T>> queryFunction;
    protected Boolean transacted = true; // default value

    @Override
    protected void doOpen() throws Exception {

        super.doOpen();

        entityManager = entityManagerFactory.createEntityManager(jpaPropertyMap);
        if(entityManager == null) {
            throw new DataAccessResourceFailureException("Unable to obtain an EntityManager");
        }
    }

    @Override
    protected void doReadPage() {

        clearIfTransacted();
        JPAQuery<T> query = createQuery().offset((long) getPage() * getPageSize()).limit(getPageSize());
        initResults();
        fetchQuery(query);
    }

    @Override
    protected void jumpToItem(int itemIndex) {
    }

    @Override
    protected void doClose() throws Exception {
        entityManager.close();
        super.doClose();
    }

    protected void clearIfTransacted() {
        if(transacted) {
            entityManager.clear();
        }
    }

    protected JPAQuery<T> createQuery() {

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        return queryFunction.apply(queryFactory);
    }

    protected void initResults() {

        if(CollectionUtils.isEmpty(results)) {
            results = new CopyOnWriteArrayList<>();
        } else {
            results.clear();
        }
    }

    protected void fetchQuery(JPAQuery<T> query) {
        if (!transacted) {
            List<T> queryResult = query.fetch();
            for (T entity : queryResult) {
                entityManager.detach(entity);
                results.add(entity);
            }
        } else {
            results.addAll(query.fetch());
        }
    }

    public QuerydslPagingItemReader(EntityManagerFactory emf, Integer pageSize,
                                    Function<JPAQueryFactory, JPAQuery<T>> queryFunction) {

        this.entityManagerFactory = emf;
        this.queryFunction = queryFunction;
        setPageSize(pageSize);
    }

    protected QuerydslPagingItemReader() {

        setName(ClassUtils.getShortName(QuerydslPagingItemReader.class));
    }

    public void setTransacted(Boolean transacted) {
        this.transacted = transacted;
    }
}

