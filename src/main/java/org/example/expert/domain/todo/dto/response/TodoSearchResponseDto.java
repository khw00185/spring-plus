package org.example.expert.domain.todo.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TodoSearchResponseDto {
    private Long id;
    private String title;
    private Long managerCount;
    private Long commentCount;

    @QueryProjection
    public TodoSearchResponseDto(Long id, String title, Long managerCount, Long commentCount){
        this.id = id;
        this.title = title;
        this.managerCount = managerCount;
        this.commentCount = commentCount;
    }
}
