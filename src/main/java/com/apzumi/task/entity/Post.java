package com.apzumi.task.entity;

import com.apzumi.task.postStatus.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.apzumi.task.postStatus.PostStatus.*;

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
