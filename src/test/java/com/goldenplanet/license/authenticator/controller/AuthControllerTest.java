package com.goldenplanet.license.authenticator.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goldenplanet.license.authenticator.dto.LicenseAuthenticationRequest;
import com.goldenplanet.license.authenticator.exception.ErrorCode;
import com.goldenplanet.license.authenticator.exception.InvalidLicenseException;
import com.goldenplanet.license.authenticator.service.AuthService;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private AuthService authService;

	@Test
	void shouldReturnOk_whenLicenseKeyIsValid() throws Exception {
		// given
		LicenseAuthenticationRequest licenseAuthenticationRequest = new LicenseAuthenticationRequest("licenseKey");

		when(authService.checkLicenseKey(any(LicenseAuthenticationRequest.class))).thenReturn(true);

		// when & then
		mockMvc.perform(post("/api/license/auth")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(
					licenseAuthenticationRequest))) // JSON representation of LicenseAuthenticationRequest
			.andExpect(status().isOk())
			.andExpect(content().string("true"));
	}

	@Test
	void shouldReturnFalse_whenLicenseKeyIsInvalid() throws Exception {
		// given
		LicenseAuthenticationRequest licenseAuthenticationRequest = new LicenseAuthenticationRequest("licenseKey");

		when(authService.checkLicenseKey(any(LicenseAuthenticationRequest.class))).thenThrow(
			new InvalidLicenseException(ErrorCode.INVALID.getErrorMessage()));

		// when & then
		mockMvc.perform(post("/api/license/auth")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(
					licenseAuthenticationRequest))) // JSON representation of LicenseAuthenticationRequest
			.andExpect(status().isBadRequest())
			.andExpect(content().string(ErrorCode.INVALID.getErrorMessage()));
	}
}