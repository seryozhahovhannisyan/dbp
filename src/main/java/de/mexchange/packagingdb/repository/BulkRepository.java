package de.mexchange.packagingdb.repository;

import de.mexchange.packagingdb.entity.BulkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BulkRepository extends JpaRepository<BulkEntity, Long> {

    List<Long> deleteByIdIn(List<Long> ids);
}
