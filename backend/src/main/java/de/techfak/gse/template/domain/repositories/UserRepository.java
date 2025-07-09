package de.techfak.gse.template.domain.repositories;

import de.techfak.gse.template.domain.entities.Usr;
import org.springframework.data.repository.CrudRepository;

/**
 * Interface für das UserRepository extends die Klasse CrudRepository.
 */
public interface UserRepository extends CrudRepository<Usr, String> {
}
