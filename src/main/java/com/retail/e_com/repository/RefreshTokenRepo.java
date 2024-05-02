package com.retail.e_com.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.retail.e_com.model.AccessToken;
import com.retail.e_com.model.RefreshToken;
import com.retail.e_com.model.User;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Integer> {

	boolean existsByTokenAndIsBlocked(String rt, boolean b);

	Optional<RefreshToken> findByToken(String refreshToken);

	List<RefreshToken> findAllByExpirationLessThan(LocalDateTime date);

//	List<RefreshToken> findAllByTokenParseJwtClaimsIssuedAtLessThan(Date date);

}
