package com.apzumi.task.service;

import com.apzumi.task.adapter.PostApi;
import com.apzumi.task.dto.PostResponse;
import com.apzumi.task.entity.Post;
import com.apzumi.task.exception.NotFoundException;
import com.apzumi.task.feignClient.PostFeignClient.TypicodePost;
import com.apzumi.task.postStatus.PostStatus;
import com.apzumi.task.repository.PostsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Service
public class PostServiceImpl implements PostService {

    private PostsRepository postsRepository;
    private PostApi postApi;
    private static final Logger log = LoggerFactory.getLogger(PostServiceImpl.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public PostServiceImpl(PostsRepository postsRepository, PostApi postApi) {
        this.postsRepository = postsRepository;
        this.postApi = postApi;
    }

    @Override
    public List<PostResponse> getAllWithFilters(String title) {
        final Collection<Post> foundPosts;
        final List<PostStatus> statusesToFind = Arrays.asList(PostStatus.NEW, PostStatus.MODIFIED);
        if (title != null) {
            foundPosts = postsRepository.findByTitleAndStatusIn(title, statusesToFind);
        } else {
            foundPosts = postsRepository.findByStatusIn(statusesToFind);
        }
        return foundPosts
                .stream()
                .map(this::toPostResponse)
                .collect(toList());
    }

    @Override
    public void updateTitleAndBodyById(Integer id, String title, String body) {
        final Post post = postsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(format("Post %d has not been found.", id)));

        ofNullable(title).ifPresent(post::setTitle);
        ofNullable(body).ifPresent(post::setBody);
        post.setStatus(PostStatus.MODIFIED);
        postsRepository.save(post);
    }

    @Override
    public void markPostAsDeleted(Integer id) {
        final Post post = postsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(format("Post %d has not been found.", id)));
        post.setStatus(PostStatus.DELETED);
        postsRepository.save(post);
    }

    private List<Post> getPostsFromTypicode() {
        return postApi.getPosts().stream()
                .map(this::toPost)
                .collect(Collectors.toList());
    }

    @Override
    @Scheduled(cron = "* 6 * * * ?")
    public void upsertUnmodified() {
        log.info("Posts fetched at: {}", dateFormat.format(new Date()));
        final List<Post> posts = getPostsFromTypicode();
        Collection<Post> foundPostsOtherThanNew = postsRepository.findByStatusIn(Arrays.asList(PostStatus.MODIFIED, PostStatus.DELETED));
        final Set<Integer> postToOmit = foundPostsOtherThanNew.stream().map(Post::getId).collect(toSet());

        final Collection<Post> postsToCreate = posts.stream()
                .filter(post -> !postToOmit.contains(post.getId()))
                .collect(Collectors.toList());

        postsRepository.saveAll(postsToCreate);
    }

    @Override
    public PostResponse getPostById(Integer id) {
        final Post foundPost = postsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(format("Post %d has not been found.", id)));
        return toPostResponse(foundPost);
    }

    private Post toPost(TypicodePost typicodePost) {
        return Post.builder()
                .userId(typicodePost.getUserId())
                .id(typicodePost.getId())
                .title(typicodePost.getTitle())
                .body(typicodePost.getBody())
                .status(PostStatus.NEW)
                .build();
    }

    private PostResponse toPostResponse(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .body(post.getBody())
                .postStatus(post.getStatus())
                .build();
    }
}