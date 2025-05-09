package de.techfak.gse.template.domain;

import org.springframework.data.repository.CrudRepository;
/**
 * Interface für das UserRepository extends die Klasse CrudRepository.
 */
public interface UserRepository extends CrudRepository<Usr, String> {
}
