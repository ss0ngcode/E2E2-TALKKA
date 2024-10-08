package com.talkka.server.subway.exception;

public class StationAlreadyExistsException extends RuntimeException {
	private static final String MESSAGE = "이미 존재하는 지하철 역입니다. StationCode: ";

	public StationAlreadyExistsException(String staionCode) {
		super(MESSAGE + staionCode);
	}
}
