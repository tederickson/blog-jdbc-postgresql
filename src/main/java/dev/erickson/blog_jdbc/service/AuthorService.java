package dev.erickson.blog_jdbc.service;

import dev.erickson.blog_jdbc.domain.Author;
import dev.erickson.blog_jdbc.model.AuthorEntity;
import dev.erickson.blog_jdbc.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;

    public Integer count() {
        return authorRepository.count();
    }

    public Author create(final Author author) {
        Assert.isNull(author.id(), "The id must be null");

        AuthorEntity authorEntity = AuthorMapper.toEntity(author);
        int count = authorRepository.save(authorEntity);

        if (count != 1) {
            throw new DataIntegrityViolationException("unable to save Author");
        }

        return AuthorMapper.toRest(authorRepository.findByEmail(author.email())
                                           .orElseThrow(() -> new DataRetrievalFailureException(author.email())));
    }

    public List<Author> findAll() {
        return authorRepository.findAll().stream().map(AuthorMapper::toRest).toList();
    }

    public Optional<Author> findById(Long id) {
        return authorRepository.findById(id).map(AuthorMapper::toRest);
    }

    public int deleteById(Long id) {
        return authorRepository.deleteById(id);
    }

    public Author update(final Author author) {
        Assert.notNull(author.id(), "The id must not be null");
        authorRepository.findById(author.id()).orElseThrow();
        authorRepository.update(AuthorMapper.toEntity(author));

        return authorRepository.findById(author.id()).map(AuthorMapper::toRest).orElseThrow();
    }

}
