package com.talkka.server.review.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.talkka.server.bus.exception.BusRouteNotFoundException;
import com.talkka.server.bus.exception.BusStationNotFoundException;
import com.talkka.server.common.dto.ErrorRespDto;
import com.talkka.server.common.exception.InvalidTypeException;
import com.talkka.server.common.exception.enums.InvalidTimeSlotEnumException;
import com.talkka.server.oauth.domain.OAuth2UserInfo;
import com.talkka.server.review.dto.BusReviewDto;
import com.talkka.server.review.dto.BusReviewReqDto;
import com.talkka.server.review.dto.BusReviewRespDto;
import com.talkka.server.review.exception.BusReviewNotFoundException;
import com.talkka.server.review.exception.ContentAccessException;
import com.talkka.server.review.exception.UserNotFoundException;
import com.talkka.server.review.service.BusReviewService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bus-review")
@RequiredArgsConstructor
public class BusReviewController implements BusReviewApi {

	private final BusReviewService busReviewService;

	@Override
	@GetMapping("")
	public ResponseEntity<?> getBusReviewList(
		@RequestParam Long routeId,
		@RequestParam(required = false) Long busRouteStationId,
		@RequestParam(required = false) String timeSlot
	) {
		List<BusReviewRespDto> reviewData;

		try {
			if (busRouteStationId != null && timeSlot != null) {
				reviewData = busReviewService.getBusReviewList(routeId, busRouteStationId, timeSlot);
			} else if (busRouteStationId != null) {
				reviewData = busReviewService.getBusReviewList(routeId, busRouteStationId);
			} else {
				reviewData = busReviewService.getBusReviewList(routeId);
			}
		} catch (InvalidTimeSlotEnumException exception) {
			return ResponseEntity.badRequest().body(ErrorRespDto.of(exception));
		}
		return ResponseEntity.ok(reviewData);
	}

	@Override
	@PostMapping("")
	@Secured({"USER", "ADMIN"})
	public ResponseEntity<?> saveBusReview(
		@AuthenticationPrincipal OAuth2UserInfo oAuth2UserInfo,
		@RequestBody @Valid BusReviewReqDto busReviewReqDto
	) {
		ResponseEntity<?> response;
		try {
			BusReviewDto reviewDto = BusReviewDto.of(oAuth2UserInfo.getUserId(), busReviewReqDto);
			BusReviewRespDto createdReview = busReviewService.createBusReview(reviewDto);
			response = ResponseEntity.ok(createdReview);
		} catch (UserNotFoundException | BusStationNotFoundException
				 | BusRouteNotFoundException | InvalidTypeException exception) {
			response = ResponseEntity.badRequest().body(ErrorRespDto.of(exception));
		}
		return response;
	}

	@Override
	@PutMapping("{bus_review_id}")
	@Secured({"USER", "ADMIN"})
	public ResponseEntity<?> updateBusReview(
		@AuthenticationPrincipal OAuth2UserInfo oAuth2UserInfo,
		@PathVariable(name = "bus_review_id") Long busReviewId,
		@RequestBody @Valid BusReviewReqDto busReviewReqDto
	) {
		ResponseEntity<?> response;

		try {
			BusReviewDto reviewDto = BusReviewDto.of(busReviewId, oAuth2UserInfo.getUserId(), busReviewReqDto);
			BusReviewRespDto updatedReview = busReviewService.updateBusReview(reviewDto);
			response = ResponseEntity.ok(updatedReview);
		} catch (ContentAccessException exception) {
			response = ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(ErrorRespDto.of(exception));
		} catch (UserNotFoundException | BusReviewNotFoundException | InvalidTypeException exception) {
			response = ResponseEntity.badRequest().body(ErrorRespDto.of(exception));
		}
		return response;
	}

	@Override
	@DeleteMapping("{bus_review_id}")
	@Secured({"USER", "ADMIN"})
	public ResponseEntity<?> deleteBusReview(
		@AuthenticationPrincipal OAuth2UserInfo oAuth2UserInfo,
		@PathVariable(name = "bus_review_id") Long busReviewId
	) {
		ResponseEntity<?> response;

		try {
			busReviewService.deleteBusReview(oAuth2UserInfo.getUserId(), busReviewId);
			response = ResponseEntity.ok().build();
		} catch (ContentAccessException exception) {
			response = ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorRespDto.of(exception));
		} catch (UserNotFoundException | BusReviewNotFoundException exception) {
			response = ResponseEntity.badRequest().body(ErrorRespDto.of(exception));
		}
		return response;
	}
}
