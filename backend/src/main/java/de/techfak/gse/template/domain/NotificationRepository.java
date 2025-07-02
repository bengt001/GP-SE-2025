package de.techfak.gse.template.domain;
import org.springframework.data.repository.CrudRepository;

/**
 * Interface for the Notification Repository.
 */
public interface NotificationRepository extends CrudRepository<Notification, Long> {
}
