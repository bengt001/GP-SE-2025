package de.techfak.gse.template.domain.repositories;
import de.techfak.gse.template.domain.entities.AbstractNotification;
import org.springframework.data.repository.CrudRepository;

/**
 * Interface for the Notification Repository.
 */
public interface NotificationRepository extends CrudRepository<AbstractNotification, Long> {
}
