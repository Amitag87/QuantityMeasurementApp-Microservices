package com.apps.conversionservice.unit;

public interface IMeasurable {
	double getConversionFactor();

	double convertToBaseUnit(double value);

	double convertFromBaseUnit(double baseValue);

	String getUnitName();

	boolean supportsArithmetic();
}
