package dev.erickson.blog_jdbc.model.service;

import dev.erickson.blog_jdbc.domain.Author;
import dev.erickson.blog_jdbc.model.AuthorEntity;
import dev.erickson.blog_jdbc.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

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
}
