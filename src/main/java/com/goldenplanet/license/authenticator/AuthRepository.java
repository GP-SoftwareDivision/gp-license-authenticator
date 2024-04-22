package com.goldenplanet.license.authenticator;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<LicenseJpaEntity, String> {
}
