package com.goldenplanet.license.authenticator.dto;

/**
 * EncryptPayloadData 클래스
 *
 * 이 클래스는 복호화된 라이선스 데이터의 정보를 보관하는 DTO(데이터 전송 객체)입니다.
 * 라이선스 검증 과정에서 필요한 정보를 포함합니다.
 *
 * @param solutionId   라이선스에 해당하는 솔루션 ID
 * @param expiredDate  라이선스의 만료 날짜 (YYYY-MM-DD 형식)
 * @param macAddress   해당 라이선스가 적용되는 MAC 주소
 * @param licenseType  라이선스 유형 (예: DEV, PROD 등)
 */
public record EncryptPayloadData(String solutionId, String expiredDate, String macAddress, String licenseType) {
}