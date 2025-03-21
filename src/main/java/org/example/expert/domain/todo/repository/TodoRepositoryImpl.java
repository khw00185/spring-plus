package org.example.expert.domain.todo.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.comment.entity.QComment;
import org.example.expert.domain.manager.entity.QManager;
import org.example.expert.domain.todo.dto.response.QTodoSearchResponseDto;
import org.example.expert.domain.todo.dto.response.TodoSearchResponseDto;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.example.expert.domain.todo.entity.QTodo.todo;
import static org.example.expert.domain.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class TodoRepositoryImpl implements TodoRepositoryCustom{
    private final EntityManager entityManager;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Todo> findAllWithFilterOrderByModifiedAtDesc(Pageable pageable, String weather, LocalDateTime startDate, LocalDateTime endDate){
        StringBuilder jpql = new StringBuilder("SELECT t FROM Todo t LEFT JOIN FETCH t.user u WHERE 1=1");

        List<Object> params = new ArrayList<>();

        if(weather != null) {
            jpql.append(" AND t.weather LIKE CONCAT('%', :weather, '%') ");
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
/*
    public Page<TodoSearchResponseDto> searchTodo(Pageable pageable, String title, String nickname, LocalDateTime startDate, LocalDateTime endDate) {
        QTodo todo = QTodo.todo;
        QManager manager = QManager.manager;
        QUser user = QUser.user;
        QComment comment = QComment.comment;

        List<TodoSearchResponseDto> test = jpaQueryFactory
                .select(
                        todo.id,
                        todo.title,
                        manager.countDistinct(),
                        comment.countDistinct()
                )
                .from(todo)
                .leftJoin(manager).on(manager.todo.eq(todo))
                .leftJoin(manager).on(manager.user.eq(user))
                .leftJoin(comment)
                .where(
                        (title != null) ? todo.title.containsIgnoreCase(title),
                        (nickname != null) ? user.nickname.containsIgnoreCase(nickname)
                )
    }
*/

    @Override
    public Page<TodoSearchResponseDto> searchTodo(Pageable pageable, String title, String nickname, LocalDateTime startDate, LocalDateTime endDate){
        QTodo todo = QTodo.todo;
        QManager manager = QManager.manager;
        QComment comment = QComment.comment;
        QUser user = QUser.user;

        List<TodoSearchResponseDto> searchTodos = jpaQueryFactory
                .select(
                        new QTodoSearchResponseDto(
                                todo.id,
                                todo.title,
                                manager.countDistinct(), // 담당자 수
                                comment.countDistinct() // 댓글 수
                        )
                )
                .from(todo)
                .leftJoin(manager).on(manager.todo.eq(todo))
                .leftJoin(manager.user, user) // Manager의 User로 조인
                .leftJoin(comment).on(comment.todo.eq(todo))
                .where(
                        (title != null) ? todo.title.containsIgnoreCase(title) : null,
                        (nickname != null) ? user.nickname.containsIgnoreCase(nickname) : null,
                        (startDate) != null ? todo.modifiedAt.goe(startDate) : null,
                        (endDate) != null ? todo.modifiedAt.loe(endDate) : null
                )
                .groupBy(todo.id)
                .orderBy(todo.modifiedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = Optional.ofNullable(jpaQueryFactory
                .select(todo.count())
                .from(todo)
                .where(
                        (title != null) ? todo.title.containsIgnoreCase(title) : null,
                        (nickname != null) ? user.nickname.containsIgnoreCase(nickname) : null,
                        (startDate) != null ? todo.modifiedAt.goe(startDate) : null,
                        (endDate) != null ? todo.modifiedAt.loe(endDate) : null
                )
                .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(searchTodos, pageable, total);
    }
}
