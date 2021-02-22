package com.apzumi.task.feignClient;

import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(url = "${externalapi.typicodeUrl}", name = "TypiCodePosts")
public interface PostFeignClient {

    @GetMapping("/posts")
    List<TypicodePost> getPosts();

    @Data
    class TypicodePost {
        private Integer id;
        private String title;
        private String body;
        private Integer userId;
    }
}