package com.goldenplanet.license.authenticator.handler;

import org.springframework.stereotype.Component;

import com.goldenplanet.license.authenticator.config.SecretProperties;
import com.goldenplanet.license.authenticator.dto.EncryptPayloadData;
import com.goldenplanet.license.authenticator.exception.ErrorCode;
import com.goldenplanet.license.authenticator.exception.InvalidLicenseException;

import lombok.RequiredArgsConstructor;

/**
 * SolutionIdHandler 클래스
 *
 * 솔루션 ID를 검증하는 핸들러입니다.
 * 요청의 솔루션 ID가 설정 값과 일치하지 않으면 예외를 던집니다.
 */
@Component
@RequiredArgsConstructor
class SolutionIdHandler extends AbstractLicenseHandler {

	// 라이선스 설정 정보를 포함하는 구성 객체
	private final SecretProperties secretProperties;

	/**
	 * 솔루션 ID를 검증하는 메서드
	 *
	 * @param data EncryptPayloadData 객체 (검증할 데이터 포함)
	 * @throws InvalidLicenseException 솔루션 ID가 맞지 않을 경우 발생
	 */
	@Override
	public void handle(EncryptPayloadData data) {
		// 설정된 솔루션 ID와 요청의 솔루션 ID를 비교
		if (!secretProperties.getSolutionId().equals(data.solutionId())) {
			throw new InvalidLicenseException(ErrorCode.SOLUTION_CODE_MISMATCH.getErrorMessage());
		}

		// 검증 성공 시 다음 핸들러 호출
		super.next(data);
	}
}