package study.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.OnlyPk;
import study.querydsl.entity.QOnlyPk;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class QuerydslApplicationTests {

    @PersistenceContext
    private EntityManager em;

    @Test
    void contextLoads() {
        OnlyPk onlyPk = new OnlyPk();
        em.persist(onlyPk);

        JPAQueryFactory query = new JPAQueryFactory(em);

        QOnlyPk qOnlyPk = new QOnlyPk("onlyPk");
        OnlyPk result = query.selectFrom(qOnlyPk).fetchOne();

        assertThat(result.getId()).isEqualTo(onlyPk.getId());
    }

}
