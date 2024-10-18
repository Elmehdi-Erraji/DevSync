package repository;

import domain.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepositoryInterface {
    Optional<List<Tag>> findAll();

    Optional<Tag> findById(Long id);

    Tag save(Tag tag);

    boolean delete(Long id);
}
