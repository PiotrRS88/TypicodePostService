package com.apzumi.task.service;

import com.apzumi.task.dto.PostResponse;

import java.util.List;

public interface PostService {
    List<PostResponse> getAllWithFilters(String title);

    void updateTitleAndBodyById(Integer id, String title, String body);

    void markPostAsDeleted(Integer id);

    void upsertUnmodified();

    PostResponse getPostById(Integer id);
}