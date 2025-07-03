package de.techfak.gse.template.domain.repositories;
import de.techfak.gse.template.domain.entities.Notification;
import org.springframework.data.repository.CrudRepository;

/**
 * Interface for the Notification Repository.
 */
public interface NotificationRepository extends CrudRepository<Notification, Long> {
}
