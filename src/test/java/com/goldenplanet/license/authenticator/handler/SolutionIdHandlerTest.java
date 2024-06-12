package com.goldenplanet.license.authenticator.handler;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.goldenplanet.license.authenticator.config.SecretProperties;
import com.goldenplanet.license.authenticator.dto.EncryptPayloadData;
import com.goldenplanet.license.authenticator.exception.InvalidLicenseException;

@ExtendWith(SpringExtension.class)
class SolutionIdHandlerTest {
	@InjectMocks
	private SolutionIdHandler solutionIdHandler;

	@Mock
	private SecretProperties secretProperties;

	@Test
	void shouldThrowInvalidLicenseExceptionWhenSolutionIdDoesNotMatch() {
		EncryptPayloadData data = mock(EncryptPayloadData.class);
		when(data.solutionId()).thenReturn("wrong-id");
		when(secretProperties.getSolutionId()).thenReturn("correct-id");

		assertThrows(InvalidLicenseException.class, () -> solutionIdHandler.handle(data));
	}

	@Test
	void shouldHandleCorrectlyWithValidSolutionId() {
		EncryptPayloadData data = mock(EncryptPayloadData.class);
		when(data.solutionId()).thenReturn("correct-id");
		when(secretProperties.getSolutionId()).thenReturn("correct-id");

		solutionIdHandler.handle(data);
	}
}
