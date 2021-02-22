package com.apzumi.task.service;

import com.apzumi.task.dto.PostResponse;
import com.apzumi.task.exception.NotFoundException;
import com.apzumi.task.repository.PostsRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

import static com.apzumi.task.postStatus.PostStatus.*;
import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 8081)
class PostServiceImplTest {

    @Resource
    private PostsRepository postsRepository;

    @Autowired
    private PostService postService;

    @Test
    void shouldReturnEmptyRepository() {
        List<PostResponse> posts = postService.getAllActivePostsByTitle(null);
        Assert.assertTrue(posts.isEmpty());
    }

    @Test
    @Order(1)
    void shouldFetchPostsToRepository() {
        postService.upsertUnmodified();
        List<PostResponse> posts = postService.getAllActivePostsByTitle(null);
        Assert.assertEquals(100, posts.size());

        PostResponse postToUpdate = posts.get(0);
        Assert.assertEquals(NEW, postToUpdate.getPostStatus());
    }

    @Test
    @Order(2)
    void shouldUpdateTitleAndBodyByGivenId() {
        postService.updateTitleAndBodyById(1, "Updated title", "Updated body");
        List<PostResponse> posts = postService.getAllActivePostsByTitle(null);
        PostResponse postToUpdate = posts.get(0);

        Assert.assertEquals("Updated title", postToUpdate.getTitle());
        Assert.assertEquals("Updated body", postToUpdate.getBody());
        Assert.assertEquals(MODIFIED, postToUpdate.getPostStatus());
    }

    @Test
    @Order(3)
    void shouldThrowExceptionAfterGivingIdWhichDoesNotExist() {
        Exception exception = assertThrows(
                NotFoundException.class,
                () -> postService.updateTitleAndBodyById(999, "Updated title", "Updated body"));

        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    @Order(4)
    void shouldMarkPostAsDeleted() {
        List<PostResponse> posts = postService.getAllActivePostsByTitle(null);
        PostResponse postToMarkAsDeleted = posts.get(1);
        postService.markPostAsDeleted(postToMarkAsDeleted.getId());

        Assert.assertEquals(DELETED, postToMarkAsDeleted.getPostStatus());
    }

    @Test
    @Order(5)
    void shouldFetchUnmodifiedPosts() {
        postService.upsertUnmodified();
        List<PostResponse> posts = postService.getAllActivePostsByTitle(null);

        Assert.assertEquals(MODIFIED, posts.get(0).getPostStatus());
        Assert.assertEquals(DELETED, posts.get(1).getPostStatus());
        Assert.assertEquals(NEW, posts.get(2).getPostStatus());
    }
}