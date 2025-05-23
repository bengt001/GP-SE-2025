package de.techfak.gse.template.domain;

import org.springframework.data.repository.CrudRepository;

/**
 * Repository for cards.
 */
public interface CardRepository extends CrudRepository<Card, Long> {
}
