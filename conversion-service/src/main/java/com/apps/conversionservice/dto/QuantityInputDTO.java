package com.apps.conversionservice.dto;

public class QuantityInputDTO {

	private QuantityDTO thisQuantityDTO;
	private QuantityDTO thatQuantityDTO;

	public QuantityInputDTO() {
	}

	public QuantityInputDTO(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
		this.thisQuantityDTO = thisQuantityDTO;
		this.thatQuantityDTO = thatQuantityDTO;
	}

	public QuantityDTO getThisQuantityDTO() {
		return thisQuantityDTO;
	}

	public QuantityDTO getThatQuantityDTO() {
		return thatQuantityDTO;
	}

	public void setThisQuantityDTO(QuantityDTO thisQuantityDTO) {
		this.thisQuantityDTO = thisQuantityDTO;
	}

	public void setThatQuantityDTO(QuantityDTO thatQuantityDTO) {
		this.thatQuantityDTO = thatQuantityDTO;
	}
}
