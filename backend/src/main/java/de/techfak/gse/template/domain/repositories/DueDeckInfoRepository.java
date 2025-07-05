package de.techfak.gse.template.domain.repositories;

import de.techfak.gse.template.domain.entities.DueDeckInfo;
import de.techfak.gse.template.domain.entities.Notification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Interface for the DueDeckInfo Repository.
 */
public interface DueDeckInfoRepository extends CrudRepository<DueDeckInfo, Integer> {
    @Query("SELECT d FROM DueDeckInfo d WHERE d.notification = :notification")
    List<DueDeckInfo> findAllDueDecksByNote(Notification notification);
}
