package com.retail.e_com.utility;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.retail.e_com.model.AccessToken;
import com.retail.e_com.model.RefreshToken;
import com.retail.e_com.repository.AccessTokenRepo;
import com.retail.e_com.repository.RefreshTokenRepo;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
@EnableScheduling
public class Scheduler {

	private AccessTokenRepo accessTokenRepo;
	private RefreshTokenRepo refreshTokenRepo;

	@Scheduled(fixedDelay = 60 * 60 * 1000l)
	public void removeAccessTokenScheduling() {

		List<AccessToken> list = accessTokenRepo.findAllByExpirationLessThan(LocalDateTime.now().minusHours(1)).stream()
				.map(accessToken -> {
					return accessToken;
				}).collect(Collectors.toList());
		if (!list.isEmpty()) {
			accessTokenRepo.deleteAll(list);
		}

	}

	@Scheduled(fixedDelay = 60 * 60 * 1000l)
	public void removeRefreshTokenScheduling() {

		List<RefreshToken> refreshTokenList = refreshTokenRepo
				.findAllByExpirationLessThan(LocalDateTime.now().minusDays(15)).stream().map(refreshToken -> {
					return refreshToken;
				}).collect(Collectors.toList());
		if (!refreshTokenList.isEmpty()) {
			refreshTokenRepo.deleteAll(refreshTokenList);
		}
	}

}
