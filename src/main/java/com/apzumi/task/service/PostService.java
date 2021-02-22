package com.apzumi.task.service;

import com.apzumi.task.dto.PostResponse;
import com.apzumi.task.entity.Post;

import java.util.List;

public interface PostService {
 //   List<Post> getAllPosts();
    List<PostResponse> getAllActivePostsByTitle(String title);
    void updateTitleAndBodyById(Integer id, String title, String body);
    void markPostAsDeleted(int id);
    void upsertUnmodified();
}
