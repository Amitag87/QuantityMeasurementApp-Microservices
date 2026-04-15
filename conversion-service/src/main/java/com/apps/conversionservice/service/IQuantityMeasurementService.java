package com.apps.conversionservice.service;

import com.apps.conversionservice.dto.QuantityInputDTO;
import com.apps.conversionservice.entity.QuantityMeasurementEntity;

import java.util.List;

public interface IQuantityMeasurementService {
	QuantityMeasurementEntity compare(QuantityInputDTO input);

	QuantityMeasurementEntity convert(QuantityInputDTO input);

	QuantityMeasurementEntity add(QuantityInputDTO input);

	QuantityMeasurementEntity subtract(QuantityInputDTO input);

	QuantityMeasurementEntity divide(QuantityInputDTO input);

}
