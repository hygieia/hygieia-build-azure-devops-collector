package com.capitalone.dashboard.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.model.BuildStatus;

@Component
public class BuildUtil {

	private static final int FIRST_RUN_HISTORY_DEFAULT = 90;
	private static final String CONTINUATIONTOKEN_HEADER = "x-ms-continuationtoken";

	public BuildStatus getBuildStatus(String status) {

		BuildStatus buildStatus;

		switch (status) {
			case "succeeded":
				buildStatus = BuildStatus.Success;
				break;
			case "partiallySucceeded":
				buildStatus = BuildStatus.Unstable;
				break;
			case "failed":
				buildStatus = BuildStatus.Failure;
				break;
			case "canceled":
				buildStatus = BuildStatus.Aborted;
				break;
			default:
				buildStatus = BuildStatus.Unknown;
		}

		return buildStatus;
	}

	public long getTheLast90DaysInTime() {
		long time = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();
		LocalDateTime utcDate = Instant.ofEpochMilli(time).atZone(ZoneOffset.UTC).toLocalDateTime();
		LocalDateTime date = utcDate.minusDays(FIRST_RUN_HISTORY_DEFAULT);
		long history = date.toInstant(ZoneOffset.UTC).toEpochMilli();
		return history;
	}

	public <T> String extractedContinuationToken(ResponseEntity<T> response) {
		List<String> continuationTokenList = response.getHeaders().get(CONTINUATIONTOKEN_HEADER);
		String continuationToken = CollectionUtils.isNotEmpty(continuationTokenList) ? continuationTokenList.get(0)
				: StringUtils.EMPTY;
		return continuationToken;
	}

	public <T> List<T> getDifferentElementsBetweenList(List<T> main, List<T> sub) {
		return main.stream().filter(collectorItem -> !sub.contains(collectorItem)).collect(Collectors.toList());
	}
}
