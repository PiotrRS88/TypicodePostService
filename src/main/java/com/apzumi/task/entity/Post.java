package com.apzumi.task.entity;

import com.apzumi.task.postStatus.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import static com.apzumi.task.postStatus.PostStatus.NEW;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Post {
    @Id
    private Integer id;
    private Integer userId;
    private String title;
    private String body;
    @Enumerated(EnumType.STRING)
    private PostStatus status = NEW;
}
