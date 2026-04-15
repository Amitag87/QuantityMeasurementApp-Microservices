package com.apps.conversionservice.unit;

public enum TemperatureUnit implements IMeasurable {
	CELSIUS {
		@Override
		public double convertToBaseUnit(double value) {
			return value;
		}

		@Override
		public double convertFromBaseUnit(double base) {
			return base;
		}
	},
	FAHRENHEIT {
		@Override
		public double convertToBaseUnit(double value) {
			return (value - 32) * 5.0 / 9.0;
		}

		@Override
		public double convertFromBaseUnit(double base) {
			return base * 9.0 / 5.0 + 32;
		}
	};

	@Override
	public double getConversionFactor() {
		return 1.0;
	}

	@Override
	public String getUnitName() {
		return name();
	}

	@Override
	public boolean supportsArithmetic() {
		return false;
	}
}
