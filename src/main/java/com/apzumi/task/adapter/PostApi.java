package com.apzumi.task.adapter;

import com.apzumi.task.feignClient.PostFeignClient;
import com.apzumi.task.feignClient.PostFeignClient.TypicodePost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostApi {
    private final PostFeignClient client;

    @Autowired
    public PostApi(PostFeignClient client) {
        this.client = client;
    }

    public List<TypicodePost> getPosts() {
        return client.getPosts();
    }
}
