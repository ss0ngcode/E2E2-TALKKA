package com.talkka.server.common.validator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.talkka.server.oauth.enums.AuthRole;
import com.talkka.server.review.exception.ContentAccessException;

class ContentAccessValidatorTest {

	@Test
	@DisplayName("관리자 권한의 유저인 경우 true를 반환한다.")
	void testValidateOwnerContentAccessAdmin() {
		// given
		Long userId = 1L;
		Long contentUserId = 2L;
		AuthRole authRole = AuthRole.ADMIN;
		ContentAccessValidator validator = new ContentAccessValidator();

		// when
		boolean result = validator.validateOwnerContentAccess(userId, authRole, contentUserId);

		// then
		assertTrue(result);
	}

	@Test
	@DisplayName("관리자 권한이 아닌 유저이고, userId와 contentUserId가 같은 경우 true를 반환한다.")
	void testValidateOwnerContentAccessSameUserId() {
		// given
		Long userId = 1L;
		Long contentUserId = 1L;
		AuthRole authRole = AuthRole.USER;
		ContentAccessValidator validator = new ContentAccessValidator();

		// when
		boolean result = validator.validateOwnerContentAccess(userId, authRole, contentUserId);

		// then
		assertTrue(result);
	}

	@Test
	@DisplayName("관리자 권한이 아닌 유저이고, userId와 contentUserId가 다른 경우 ContentAccessException을 발생시킨다.")
	void testValidateOwnerContentAccessDifferentUserId() {
		// given
		Long userId = 1L;
		Long contentUserId = 2L;
		AuthRole authRole = AuthRole.USER;
		ContentAccessValidator validator = new ContentAccessValidator();

		// when, then
		assertThrows(ContentAccessException.class,
			() -> validator.validateOwnerContentAccess(userId, authRole, contentUserId));
	}
}