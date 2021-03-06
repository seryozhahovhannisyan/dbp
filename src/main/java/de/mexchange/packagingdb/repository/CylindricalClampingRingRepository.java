package de.mexchange.packagingdb.repository;

import de.mexchange.packagingdb.entity.CylindricalClampingRingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CylindricalClampingRingRepository extends JpaRepository<CylindricalClampingRingEntity, Long> {

    List<Long> deleteByIdIn(List<Long> ids);
}
