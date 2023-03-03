package study.querydsl.support;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.function.Function;
import java.util.function.LongSupplier;

@Repository
public abstract class Querydsl4RepositorySupport {

    private final Class<?> domainClass;
    private EntityManager entityManager;
    private Querydsl querydsl;
    private JPAQueryFactory queryFactory;

    public Querydsl4RepositorySupport(Class<?> domainClass) {
        Assert.notNull(domainClass, "DomainClass must not be null.");
        this.domainClass = domainClass;
    }

    @Autowired
    public void setEntityManager(EntityManager entityManager) {
        Assert.notNull(entityManager, "EntityManager must not be null.");

        JpaEntityInformation<?, ?> entityInformation =
            JpaEntityInformationSupport.getEntityInformation(domainClass, entityManager);
        EntityPath<?> path = SimpleEntityPathResolver.INSTANCE.createPath(entityInformation.getJavaType());

        this.entityManager = entityManager;
        this.querydsl = new Querydsl(entityManager, new PathBuilder<>(path.getType(), path.getMetadata()));
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @PostConstruct
    public void validate() {
        Assert.notNull(entityManager, "EntityManager must not be null.");
        Assert.notNull(querydsl, "Querydsl must not be null.");
        Assert.notNull(queryFactory, "QueryFactory must not be null.");
    }

    protected EntityManager getEntityManager() {
        return entityManager;
    }

    protected Querydsl getQuerydsl() {
        return querydsl;
    }

    protected JPAQueryFactory getQueryFactory() {
        return queryFactory;
    }

    protected <T> JPAQuery<T> select(Expression<T> expr) {
        return getQueryFactory().select(expr);
    }

    protected <T> JPAQuery<T> selectFrom(EntityPath<T> entityPath) {
        return getQueryFactory().selectFrom(entityPath);
    }

    protected <T> Page<T> applyPagination(Pageable pageable,
                                          Function<JPAQueryFactory, JPAQuery<T>> contentQuery,
                                          Function<JPAQueryFactory, LongSupplier> countQuery) {
        JPAQuery<T> query = contentQuery.apply(getQueryFactory());
        LongSupplier countSupplier = countQuery.apply(getQueryFactory());
        List<T> fetch = getQuerydsl().applyPagination(pageable, query).fetch();
        return PageableExecutionUtils.getPage(fetch, pageable, countSupplier);
    }

    protected <T> Page<T> applyPagination(Pageable pageable, JPAQuery<T> contentQuery, JPAQuery<Long> countQuery) {
        List<T> content = getQuerydsl().applyPagination(pageable, contentQuery).fetch();
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
