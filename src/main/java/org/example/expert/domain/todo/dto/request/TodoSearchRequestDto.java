package org.example.expert.domain.todo.dto.request;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class TodoSearchRequestDto {
    @Nullable
    private String title;
    @Nullable
    private LocalDate startDate;
    @Nullable
    private LocalDate endDate;
    @Nullable
    private String managerNickname;
}
