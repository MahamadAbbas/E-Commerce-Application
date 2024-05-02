package com.retail.e_com.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.retail.e_com.model.AccessToken;
import com.retail.e_com.model.User;

public interface AccessTokenRepo extends JpaRepository<AccessToken, Integer> {

	boolean existsByTokenAndIsBlocked(String at, boolean b);

	Optional<AccessToken> findByToken(String accessToken);

	List<AccessToken> findAllByExpirationLessThan(LocalDateTime date);

	boolean existsByToken(String accessToken);
}
