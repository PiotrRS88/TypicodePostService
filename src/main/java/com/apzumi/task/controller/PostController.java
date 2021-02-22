package com.apzumi.task.controller;

import com.apzumi.task.dto.PostResponse;
import com.apzumi.task.service.PostService;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PostController {

    private PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts")
    public List<PostResponse> getPosts(@RequestParam(required = false) String title) {
        return postService.getAllWithFilters(title);
    }

    @PostMapping("/fetchposts")
    public void fetchNewPosts() {
        postService.upsertUnmodified();
    }

    @PutMapping("/posts/{id}")
    public void updatePost(@PathVariable Integer id, @RequestBody UpdatePostRequest request) {
        postService.updateTitleAndBodyById(id, request.title, request.body);
    }

    @DeleteMapping("/posts/{id}")
    public void updatePost(@PathVariable Integer id) {
        postService.markPostAsDeleted(id);
    }

    @Value
    private static class UpdatePostRequest {
        String title;
        String body;
    }
}