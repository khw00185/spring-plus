package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.dto.response.TodoSearchResponseDto;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface TodoRepositoryCustom {
    Page<Todo> findAllWithFilterOrderByModifiedAtDesc(Pageable pageable, String weather, LocalDateTime startDate, LocalDateTime endDate);

    Todo findByIdWithUser (Long todoId);

    Page<TodoSearchResponseDto> searchTodo(Pageable pageable, String title, String nickname, LocalDateTime startDate, LocalDateTime endDate);


}
