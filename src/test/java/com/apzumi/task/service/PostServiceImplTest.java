package com.apzumi.task.service;

import com.apzumi.task.dto.PostResponse;
import com.apzumi.task.exception.NotFoundException;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.apzumi.task.postStatus.PostStatus.DELETED;
import static com.apzumi.task.postStatus.PostStatus.MODIFIED;
import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureWireMock(port = 8081)
@ActiveProfiles("test")
class PostServiceImplTest {

    @Autowired
    private PostService postService;

    @Test
    @DisplayName("Should return empty when there's nothing on DB.")
    void shouldReturnEmptyRepository() {
        //when
        List<PostResponse> posts = postService.getAllWithFilters(null);
        //then
        Assert.assertTrue(posts.isEmpty());
    }

    @Test
    @DisplayName("Should give 100 posts when we fetch all from Typicode.")
    void shouldFetchPostsToRepository() {
        //when
        postService.upsertUnmodified();
        List<PostResponse> posts = postService.getAllWithFilters(null);
        //then
        Assert.assertEquals(100, posts.size());
    }

    @Test
    @DisplayName("Should update title and body by given id.")
    void shouldUpdateTitleAndBodyByGivenId() {
        //given
        final int postIdToModify = 1;
        //when
        postService.upsertUnmodified();
        //and
        postService.updateTitleAndBodyById(postIdToModify, "Updated title", "Updated body");

        PostResponse post = postService.getPostById(postIdToModify);

        Assert.assertEquals("Updated title", post.getTitle());
        Assert.assertEquals("Updated body", post.getBody());
        Assert.assertEquals(MODIFIED, post.getPostStatus());
    }

    @Test
    @DisplayName("Should throw exception after giving id which does not exist.")
    void shouldThrowExceptionAfterGivingIdWhichDoesNotExist() {
        //when
        Exception exception = assertThrows(
                NotFoundException.class,
                () -> postService.updateTitleAndBodyById(999, "Updated title", "Updated body"));
        //then
        assertTrue(exception.getMessage().contains("Post 999 has not been found."));
    }

    @ParameterizedTest
    @DisplayName("Should check if filtered posts are returned.")
    @CsvSource({
            "qui est esse, 1",
            "Here comes the Sun, 0"
    })
    void shouldCheckIfFilteredPostsAreReturned(String title, int size) {
        //when
        postService.upsertUnmodified();
        //and
        List<PostResponse> posts = postService.getAllWithFilters(title);

        //then
        assertEquals(posts.size(), size);
    }

    @Test
    @DisplayName("Should verify that posts marked as deleted have status DELETED.")
    void shouldAppearAsDeleted() {
        //given
        Integer postIdToDelete = 1;
        //when
        postService.upsertUnmodified();
        //and
        postService.markPostAsDeleted(postIdToDelete);
        final PostResponse foundPost = postService.getPostById(postIdToDelete);
        //then
        assertEquals(DELETED, foundPost.getPostStatus());
    }

    @Test
    @DisplayName("Should check if Modified posts statuses are MODIFIED.")
    void shouldCheckForModifiedStatus() {
        //given
        Integer postToModify = 1;

        //when
        postService.upsertUnmodified();
        //and
        postService.updateTitleAndBodyById(postToModify, "Get back", "Blackbird");
        final PostResponse foundPost = postService.getPostById(postToModify);

        //then
        assertEquals(MODIFIED, foundPost.getPostStatus());
    }

    @Test
    @DisplayName("Should verify that Modified posts stay as they were.")
    void shouldFetchUnmodifiedPosts() {
        //given
        Integer postToModify = 1;

        //when
        postService.upsertUnmodified();
        //and
        postService.updateTitleAndBodyById(postToModify, "Strawberry field", "Penny Lane");
        final PostResponse foundPost = postService.getPostById(postToModify);

        //then
        assertEquals(MODIFIED, foundPost.getPostStatus());

        //when
        postService.upsertUnmodified();
        final PostResponse modifiedPost = postService.getPostById(postToModify);

        //then
        assertEquals(MODIFIED, modifiedPost.getPostStatus());
    }
}