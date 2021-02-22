package com.apzumi.task.dto;

import com.apzumi.task.postStatus.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import static com.apzumi.task.postStatus.PostStatus.NEW;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private Integer id;
    private String title;
    private String body;
    @Enumerated(EnumType.STRING)
    private PostStatus postStatus = NEW;
}
