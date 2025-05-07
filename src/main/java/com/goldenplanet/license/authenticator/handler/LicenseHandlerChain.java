package com.goldenplanet.license.authenticator.handler;

import org.springframework.stereotype.Component;

import com.goldenplanet.license.authenticator.dto.EncryptPayloadData;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;

/**
 * LicenseHandlerChain 클래스
 *
 * 라이선스 검증 과정을 체인 형태로 관리하는 클래스입니다.
 * 여러 핸들러를 연결하여 순차적으로 검증을 수행합니다.
 */
@Component
@AllArgsConstructor
public class LicenseHandlerChain {

	// 핸들러들의 순차적인 연결
	private final ExpiryDateHandler expiryDateHandler;
	private final SolutionIdHandler solutionIdHandler;
	private final MacAddressHandler macAddressHandler;

	/**
	 * 체인의 초기화 메서드
	 *
	 * 각 핸들러들을 체인 형태로 연결합니다.
	 */
	@PostConstruct
	public void init() {
		expiryDateHandler.setNextHandler(solutionIdHandler);
		solutionIdHandler.setNextHandler(macAddressHandler);
	}

	/**
	 * 체인의 첫 번째 핸들러를 실행합니다.
	 *
	 * @param encryptPayloadData 검증할 데이터
	 */
	public void execute(EncryptPayloadData encryptPayloadData) {
		expiryDateHandler.handle(encryptPayloadData);
	}
}