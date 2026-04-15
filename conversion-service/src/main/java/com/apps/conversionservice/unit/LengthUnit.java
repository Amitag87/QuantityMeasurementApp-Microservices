package com.apps.conversionservice.unit;

public enum LengthUnit implements IMeasurable {
	FEET(12.0), INCHES(1.0), YARDS(36.0), CENTIMETERS(0.393701);

	private final double conversionFactor;

	LengthUnit(double conversionFactor) {
		this.conversionFactor = conversionFactor;
	}

	@Override
	public double getConversionFactor() {
		return conversionFactor;
	}

	@Override
	public double convertToBaseUnit(double value) {
		return value * conversionFactor;
	}

	@Override
	public double convertFromBaseUnit(double baseValue) {
		return baseValue / conversionFactor;
	}

	@Override
	public String getUnitName() {
		return name();
	}

	@Override
	public boolean supportsArithmetic() {
		return true;
	}
}
