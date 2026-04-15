package com.apps.conversionservice.service;

import com.apps.conversionservice.dto.QuantityDTO;
import com.apps.conversionservice.dto.QuantityInputDTO;
import com.apps.conversionservice.entity.QuantityMeasurementEntity;
import com.apps.conversionservice.entity.UserEntity;
import com.apps.conversionservice.exception.QuantityMeasurementException;


import com.apps.conversionservice.unit.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;

@Service
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

	private UserEntity getCurrentUser() {
        if(SecurityContextHolder.getContext().getAuthentication() == null) return null;
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if(email == null || email.equals("anonymousUser")) return null;
        UserEntity user = new UserEntity();
        user.setEmail(email);
        return user;
	}

    private QuantityMeasurementEntity saveToHistoryService(QuantityMeasurementEntity entity) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null && attributes.getRequest().getHeader("Authorization") != null) {
                headers.set("Authorization", attributes.getRequest().getHeader("Authorization"));
            }
            
            HttpEntity<QuantityMeasurementEntity> request = new HttpEntity<>(entity, headers);
            return restTemplate.postForObject("http://localhost:8083/api/v1/quantities/internal/save", request, QuantityMeasurementEntity.class);
        } catch (Exception e) {
            e.printStackTrace();
            return entity; // return computed result even if history save fails
        }
    }

	private IMeasurable getUnit(String measurementType, String unit) {
		return switch (measurementType.toUpperCase()) {
		case "LENGTH" -> LengthUnit.valueOf(unit.toUpperCase());
		case "WEIGHT" -> WeightUnit.valueOf(unit.toUpperCase());
		case "VOLUME" -> VolumeUnit.valueOf(unit.toUpperCase());
		case "TEMPERATURE" -> TemperatureUnit.valueOf(unit.toUpperCase());
		default -> throw new QuantityMeasurementException("Invalid measurement type: " + measurementType);
		};
	}

	private void setCommonFields(QuantityMeasurementEntity entity, QuantityInputDTO input) {
		QuantityDTO q1 = input.getThisQuantityDTO();
		QuantityDTO q2 = input.getThatQuantityDTO();

		entity.setThisValue(q1.getValue());
		entity.setThisUnit(q1.getUnit());
		entity.setThisMeasurementType(q1.getMeasurementType());

		if (q2 != null) {
			entity.setThatValue(q2.getValue());
			entity.setThatUnit(q2.getUnit());
			entity.setThatMeasurementType(q2.getMeasurementType());
		}
		entity.setCreatedAt(LocalDateTime.now());
		entity.setUser(getCurrentUser());
	}

	@Override
	public QuantityMeasurementEntity compare(QuantityInputDTO input) {
		QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
		try {
			QuantityDTO q1 = input.getThisQuantityDTO();
			QuantityDTO q2 = input.getThatQuantityDTO();

			IMeasurable unit1 = getUnit(q1.getMeasurementType(), q1.getUnit());
			IMeasurable unit2 = getUnit(q2.getMeasurementType(), q2.getUnit());

			double base1 = unit1.convertToBaseUnit(q1.getValue());
			double base2 = unit2.convertToBaseUnit(q2.getValue());

			setCommonFields(entity, input);
			entity.setOperation("COMPARE");
			entity.setResultString(String.valueOf(base1 == base2));
		} catch (Exception e) {
			entity.setError(true);
			entity.setErrorMessage(e.getMessage());
			entity.setOperation("COMPARE");
			entity.setCreatedAt(LocalDateTime.now());
			entity.setUser(getCurrentUser());
		}
		return saveToHistoryService(entity);
	}

	@Override
	public QuantityMeasurementEntity convert(QuantityInputDTO input) {
		QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
		try {
			QuantityDTO q = input.getThisQuantityDTO();
			QuantityDTO target = input.getThatQuantityDTO();

			IMeasurable fromUnit = getUnit(q.getMeasurementType(), q.getUnit());
			IMeasurable toUnit = getUnit(q.getMeasurementType(), target.getUnit());

			double base = fromUnit.convertToBaseUnit(q.getValue());
			double result = toUnit.convertFromBaseUnit(base);

			entity.setThisValue(q.getValue());
			entity.setThisUnit(q.getUnit());
			entity.setThisMeasurementType(q.getMeasurementType());
			entity.setOperation("CONVERT");
			entity.setResultValue(result);
			entity.setResultUnit(target.getUnit());
			entity.setCreatedAt(LocalDateTime.now());
			entity.setUser(getCurrentUser());
		} catch (Exception e) {
			entity.setError(true);
			entity.setErrorMessage(e.getMessage());
			entity.setOperation("CONVERT");
			entity.setCreatedAt(LocalDateTime.now());
			entity.setUser(getCurrentUser());
		}
		return saveToHistoryService(entity);
	}

	@Override
	public QuantityMeasurementEntity add(QuantityInputDTO input) {
		QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
		try {
			QuantityDTO q1 = input.getThisQuantityDTO();
			QuantityDTO q2 = input.getThatQuantityDTO();

			IMeasurable unit1 = getUnit(q1.getMeasurementType(), q1.getUnit());
			IMeasurable unit2 = getUnit(q2.getMeasurementType(), q2.getUnit());

			if (!unit1.supportsArithmetic() || !unit2.supportsArithmetic()) {
				throw new QuantityMeasurementException("Arithmetic not supported for this unit");
			}

			double base1 = unit1.convertToBaseUnit(q1.getValue());
			double base2 = unit2.convertToBaseUnit(q2.getValue());
			double result = unit1.convertFromBaseUnit(base1 + base2);

			setCommonFields(entity, input);
			entity.setOperation("ADD");
			entity.setResultValue(result);
			entity.setResultUnit(q1.getUnit());
		} catch (Exception e) {
			entity.setError(true);
			entity.setErrorMessage(e.getMessage());
			entity.setOperation("ADD");
			entity.setCreatedAt(LocalDateTime.now());
			entity.setUser(getCurrentUser());
		}
		return saveToHistoryService(entity);
	}

	@Override
	public QuantityMeasurementEntity subtract(QuantityInputDTO input) {
		QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
		try {
			QuantityDTO q1 = input.getThisQuantityDTO();
			QuantityDTO q2 = input.getThatQuantityDTO();

			IMeasurable unit1 = getUnit(q1.getMeasurementType(), q1.getUnit());
			IMeasurable unit2 = getUnit(q2.getMeasurementType(), q2.getUnit());

			if (!unit1.supportsArithmetic() || !unit2.supportsArithmetic()) {
				throw new QuantityMeasurementException("Arithmetic not supported for this unit");
			}

			double base1 = unit1.convertToBaseUnit(q1.getValue());
			double base2 = unit2.convertToBaseUnit(q2.getValue());
			double result = unit1.convertFromBaseUnit(base1 - base2);

			setCommonFields(entity, input);
			entity.setOperation("SUBTRACT");
			entity.setResultValue(result);
			entity.setResultUnit(q1.getUnit());
		} catch (Exception e) {
			entity.setError(true);
			entity.setErrorMessage(e.getMessage());
			entity.setOperation("SUBTRACT");
			entity.setCreatedAt(LocalDateTime.now());
			entity.setUser(getCurrentUser());
		}
		return saveToHistoryService(entity);
	}

	@Override
	public QuantityMeasurementEntity divide(QuantityInputDTO input) {
		QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
		try {
			QuantityDTO q1 = input.getThisQuantityDTO();
			QuantityDTO q2 = input.getThatQuantityDTO();

			IMeasurable unit1 = getUnit(q1.getMeasurementType(), q1.getUnit());
			IMeasurable unit2 = getUnit(q2.getMeasurementType(), q2.getUnit());

			if (!unit1.supportsArithmetic() || !unit2.supportsArithmetic()) {
				throw new QuantityMeasurementException("Arithmetic not supported for this unit");
			}

			double base1 = unit1.convertToBaseUnit(q1.getValue());
			double base2 = unit2.convertToBaseUnit(q2.getValue());

			if (base2 == 0) {
				throw new QuantityMeasurementException("Cannot divide by zero");
			}

			setCommonFields(entity, input);
			entity.setOperation("DIVIDE");
			entity.setResultValue(base1 / base2);
			entity.setResultUnit("RATIO");
		} catch (Exception e) {
			entity.setError(true);
			entity.setErrorMessage(e.getMessage());
			entity.setOperation("DIVIDE");
			entity.setCreatedAt(LocalDateTime.now());
			entity.setUser(getCurrentUser());
		}
		return saveToHistoryService(entity);
	}
}
