package com.apzumi.task.repository;

import com.apzumi.task.entity.Post;
import com.apzumi.task.postStatus.PostStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface PostsRepository extends JpaRepository<Post, Integer> {

    Collection<Post> findByStatusIn(List<PostStatus> statuses);
    Collection<Post> findByTitleAndStatusIn(String title, List<PostStatus> statuses);
}
