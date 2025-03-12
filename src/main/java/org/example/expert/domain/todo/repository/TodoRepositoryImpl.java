package org.example.expert.domain.todo.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.example.expert.domain.todo.entity.QTodo.todo;
import static org.example.expert.domain.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class TodoRepositoryImpl implements TodoRepositoryCustom{
    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Todo> findAllWithFilterOrderByModifiedAtDesc(Pageable pageable, String weather, LocalDate startDate, LocalDate endDate){
        StringBuilder jpql = new StringBuilder("SELECT t FROM Todo t LEFT JOIN FETCH t.user u WHERE 1=1");

        List<Object> params = new ArrayList<>();

        if(weather != null) {
            jpql.append(" AND t.weather = :weather ");
            params.add(weather);
        }
        if(startDate != null) {
            jpql.append(" AND t.modifiedAt >= :startDate ");
            params.add(startDate);
        }
        if(endDate != null) {
            jpql.append(" AND t.modifiedAt <= :endDate ");
            params.add(endDate);
        }
        jpql.append(" ORDER BY t.modifiedAt DESC ");

        TypedQuery<Todo> query = entityManager.createQuery(jpql.toString(), Todo.class);
        //쿼리 파라미터 바인딩
        if(weather != null) {
            query.setParameter("weather", weather);
        }
        if(startDate != null) {
            query.setParameter("startDate", startDate);
        }
        if(endDate != null) {
            query.setParameter("endDate", endDate);
        }

        //페이징 처리
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Todo> todos = query.getResultList();

        StringBuilder countJpql = new StringBuilder("SELECT COUNT(t) FROM Todo t WHERE 1=1");
        params.clear();

        if(weather != null) {
            countJpql.append(" AND t.weather = :weather ");
            params.add(weather);
        }
        if(startDate != null) {
            countJpql.append(" AND t.modifiedAt >= :startDate ");
            params.add(startDate);
        }
        if(endDate != null) {
            countJpql.append(" AND t.modifiedAt <= :endDate ");
            params.add(endDate);
        }
        TypedQuery<Long> countQuery = entityManager.createQuery(countJpql.toString(), Long.class);
        if(weather != null) {
            countQuery.setParameter("weather", weather);
        }
        if(startDate != null) {
            countQuery.setParameter("startDate", startDate);
        }
        if(endDate != null) {
            countQuery.setParameter("endDate", endDate);
        }

        long total = countQuery.getSingleResult();

        return new PageImpl<>(todos, pageable, total);
    }

    @Override
    public Todo findByIdWithUser(Long todoId){
        return jpaQueryFactory.selectFrom(todo)
                .leftJoin(todo.user, user).fetchJoin()
                .where(todo.id.eq(todoId))
                .fetchOne();
    }
}
