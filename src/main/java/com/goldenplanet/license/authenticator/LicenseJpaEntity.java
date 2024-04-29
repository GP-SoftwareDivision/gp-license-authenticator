package com.goldenplanet.license.authenticator;

import java.time.LocalDate;

import de.huxhorn.sulky.ulid.ULID;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "license")
@Builder
public class LicenseJpaEntity {
	@Id
	private String licenseId;

	@PrePersist
	public void generateULID() {
		this.licenseId = new ULID().nextULID();
	}

	private LocalDate licenseExpiredDt;
	private String macAddress;
	private String licenseKey;
	private String secretKey;
	private String licenseType;

}
