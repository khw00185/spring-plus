package org.example.expert.domain.log.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime requestTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id", nullable = false)
    private Todo todo;

    private Long managerUserId;

    @Nullable
    private String errorMessage;

    public Log(User user, Todo todo, Long managerUserId, String errorMessage){
        requestTime = LocalDateTime.now();
        this.user = user;
        this.todo = todo;
        this.managerUserId = managerUserId;
        this.errorMessage = errorMessage;
    }
}
