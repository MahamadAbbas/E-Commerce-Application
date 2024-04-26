package com.retail.e_com.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.retail.e_com.model.RefreshToken;
import com.retail.e_com.model.User;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Integer> {

	boolean existsByTokenAndIsBlocked(String rt, boolean b);

	Optional<RefreshToken> findByToken(String refreshtoken);

}
