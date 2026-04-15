package com.apps.historyservice.entity;

import com.apps.historyservice.entity.UserEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "quantity_measurements")
public class QuantityMeasurementEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Double thisValue;
	private String thisUnit;
	private String thisMeasurementType;

	private Double thatValue;
	private String thatUnit;
	private String thatMeasurementType;

	private String operation;

	private Double resultValue;
	private String resultUnit;
	private String resultString;

	private boolean error;
	private String errorMessage;

	private LocalDateTime createdAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private UserEntity user;

	public QuantityMeasurementEntity() {
	}

	@PrePersist
	public void prePersist() {
		createdAt = LocalDateTime.now();
	}

	// Explicit getters and setters
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public Double getThisValue() { return thisValue; }
	public void setThisValue(Double thisValue) { this.thisValue = thisValue; }

	public String getThisUnit() { return thisUnit; }
	public void setThisUnit(String thisUnit) { this.thisUnit = thisUnit; }

	public String getThisMeasurementType() { return thisMeasurementType; }
	public void setThisMeasurementType(String thisMeasurementType) { this.thisMeasurementType = thisMeasurementType; }

	public Double getThatValue() { return thatValue; }
	public void setThatValue(Double thatValue) { this.thatValue = thatValue; }

	public String getThatUnit() { return thatUnit; }
	public void setThatUnit(String thatUnit) { this.thatUnit = thatUnit; }

	public String getThatMeasurementType() { return thatMeasurementType; }
	public void setThatMeasurementType(String thatMeasurementType) { this.thatMeasurementType = thatMeasurementType; }

	public String getOperation() { return operation; }
	public void setOperation(String operation) { this.operation = operation; }

	public Double getResultValue() { return resultValue; }
	public void setResultValue(Double resultValue) { this.resultValue = resultValue; }

	public String getResultUnit() { return resultUnit; }
	public void setResultUnit(String resultUnit) { this.resultUnit = resultUnit; }

	public String getResultString() { return resultString; }
	public void setResultString(String resultString) { this.resultString = resultString; }

	public boolean isError() { return error; }
	public void setError(boolean error) { this.error = error; }

	public String getErrorMessage() { return errorMessage; }
	public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

	public LocalDateTime getCreatedAt() { return createdAt; }
	public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

	public UserEntity getUser() { return user; }
	public void setUser(UserEntity user) { this.user = user; }
}
