package com.apps.historyservice.repository;

import com.apps.historyservice.entity.QuantityMeasurementEntity;
import com.apps.historyservice.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuantityMeasurementRepository extends JpaRepository<QuantityMeasurementEntity, Long> {

	List<QuantityMeasurementEntity> findByUser(UserEntity user);

	List<QuantityMeasurementEntity> findByUserAndOperation(UserEntity user, String operation);

	long countByUserAndOperationAndErrorFalse(UserEntity user, String operation);

	List<QuantityMeasurementEntity> findByOperation(String operation);

	long countByOperationAndErrorFalse(String operation);

	List<QuantityMeasurementEntity> findByErrorTrue();
}
