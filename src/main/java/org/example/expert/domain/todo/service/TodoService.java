package org.example.expert.domain.todo.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.client.WeatherClient;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.request.TodoSearchRequestDto;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.dto.response.TodoSearchResponseDto;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final WeatherClient weatherClient;

    @Transactional
    public TodoSaveResponse saveTodo(AuthUser authUser, TodoSaveRequest todoSaveRequest) {
        User user = User.fromAuthUser(authUser);

        String weather = weatherClient.getTodayWeather();

        Todo newTodo = new Todo(
                todoSaveRequest.getTitle(),
                todoSaveRequest.getContents(),
                weather,
                user
        );
        Todo savedTodo = todoRepository.save(newTodo);

        return new TodoSaveResponse(
                savedTodo.getId(),
                savedTodo.getTitle(),
                savedTodo.getContents(),
                weather,
                new UserResponse(user.getId(), user.getEmail(), user.getNickname())
        );
    }
    @Transactional(readOnly = true)
    public Page<TodoResponse> getTodos(int page, int size, String weather, LocalDate startDate, LocalDate endDate) {
        Pageable pageable = PageRequest.of(page - 1, size);

        LocalDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = (endDate != null) ? endDate.atTime(LocalTime.MAX) : null;

        Page<Todo> todos = todoRepository.findAllWithFilterOrderByModifiedAtDesc(pageable, weather, startDateTime, endDateTime);

        return todos.map(todo -> new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getContents(),
                todo.getWeather(),
                new UserResponse(todo.getUser().getId(), todo.getUser().getEmail(), todo.getUser().getNickname()),
                todo.getCreatedAt(),
                todo.getModifiedAt()
        ));
    }
    @Transactional(readOnly = true)
    public TodoResponse getTodo(long todoId) {
        Todo todo = Optional.ofNullable(todoRepository.findByIdWithUser(todoId))
                .orElseThrow(()-> new InvalidRequestException("Todo not found"));
        User user = todo.getUser();

        return new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getContents(),
                todo.getWeather(),
                new UserResponse(user.getId(), user.getEmail(), user.getNickname()),
                todo.getCreatedAt(),
                todo.getModifiedAt()
        );
    }

    @Transactional(readOnly = true)
    public Page<TodoSearchResponseDto> searchTodos(int page, int size, TodoSearchRequestDto requestDto){
        Pageable pageable = PageRequest.of(page - 1, size);

        LocalDateTime startDateTime = (requestDto.getStartDate() != null) ? requestDto.getStartDate().atStartOfDay() : null;
        LocalDateTime endDateTime = (requestDto.getEndDate() != null) ? requestDto.getEndDate().atTime(LocalTime.MAX) : null;

        Page<TodoSearchResponseDto> todos = todoRepository.searchTodo(
                pageable,
                requestDto.getTitle(),
                requestDto.getManagerNickname(),
                startDateTime, endDateTime
        );

        return todos;


    }
}
