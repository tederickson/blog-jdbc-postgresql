package dev.erickson.blog_jdbc.model.service;

import dev.erickson.blog_jdbc.domain.Post;
import dev.erickson.blog_jdbc.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final AuthorService authorService;
    private final CommentService commentService;

    public Integer count() {
        return postRepository.count();
    }

    public List<Post> findAll() {
        List<Post> posts = new ArrayList<>();

        for (var postEntity : postRepository.findAll().stream().toList()) {
            Post post = PostMapper.toRest(postEntity);
            post.setAuthor(authorService.findById(postEntity.getAuthorId()).orElseThrow());
            post.setComments(commentService.findByPost(postEntity));
        }

        return posts;
    }
}
