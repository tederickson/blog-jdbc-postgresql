package dev.erickson.blog_jdbc.model.service;

import dev.erickson.blog_jdbc.domain.Comment;
import dev.erickson.blog_jdbc.domain.Post;
import dev.erickson.blog_jdbc.model.PostEntity;
import dev.erickson.blog_jdbc.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

        for (var postEntity : postRepository.findAll()) {
            Post post = PostMapper.toRest(postEntity);

            post.setAuthor(authorService.findById(postEntity.getAuthorId()).orElseThrow());
            post.setComments(commentService.findByPost(postEntity));

            posts.add(post);
        }

        return posts;
    }

    public Post create(final Post post) throws SQLException {
        Assert.isNull(post.getId(), "The id must be null");

        PostEntity postEntity = PostMapper.toEntity(post);
        Long postId = postRepository.save(postEntity);

        if (post.getComments() != null) {
            for (var comment : post.getComments()) {commentService.create(comment, postId);}
        }

        return findById(postId).orElseThrow();
    }

    public Post update(final Post post) throws SQLException {
        Assert.notNull(post.getId(), "The id must not be null");

        PostEntity postEntity = PostMapper.toEntity(post);
        postRepository.update(postEntity);

        for (Comment databaseComment : commentService.findByPost(postEntity)) {
            Optional<Comment> existingComment = post.getComments().stream()
                    .filter(comment -> Objects.equals(comment.id(), databaseComment.id()))
                    .findFirst();
            if (existingComment.isEmpty()) {
                commentService.deleteById(databaseComment.id());
            }
            else {
                commentService.update(existingComment.get());
            }
        }

        List<Comment> newComments = post.getComments().stream()
                .filter(comment -> comment.id() == null).toList();
        for (Comment comment : newComments) {
            commentService.create(comment, post.getId());
        }

        return findById(post.getId()).orElseThrow();
    }

    public Optional<Post> findById(Long id) {
        var optionalPostEntity = postRepository.findById(id);

        if (optionalPostEntity.isEmpty()) {return Optional.empty();}

        PostEntity postEntity = optionalPostEntity.get();
        Post post = PostMapper.toRest(postEntity);

        post.setAuthor(authorService.findById(postEntity.getAuthorId()).orElseThrow());
        post.setComments(commentService.findByPost(postEntity));

        return Optional.of(post);
    }

    public int deleteById(Long id) {
        var possiblePost = postRepository.findById(id);
        if (possiblePost.isEmpty()) {return 0;}

        commentService.findByPost(possiblePost.get())
                .forEach(comment -> commentService.deleteById(comment.id()));

        return postRepository.deleteById(id);
    }
}
