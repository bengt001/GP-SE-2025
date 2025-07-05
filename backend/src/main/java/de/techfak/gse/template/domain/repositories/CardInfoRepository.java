package de.techfak.gse.template.domain.repositories;

import de.techfak.gse.template.domain.entities.CardInfo;
import org.springframework.data.repository.CrudRepository;

/**
 * The interface Card info repository.
 */
public interface CardInfoRepository extends CrudRepository<CardInfo, Long> {

}
