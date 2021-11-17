package com.capitalone.dashboard.util;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.capitalone.dashboard.model.Build;
import com.capitalone.dashboard.model.BuildStatus;
import com.capitalone.dashboard.model.VSTSBuildJob;
import com.capitalone.dashboard.model.VSTSBuildResponse;

@RunWith(MockitoJUnitRunner.class)
public class BuildUtilTest {

	@InjectMocks
	private BuildUtil buildUtil;

	@Test
	public void testGetBuildStatusSuccess() throws Exception {
		// Act
		BuildStatus actual = buildUtil.getBuildStatus("succeeded");
		// Assert
		assertEquals(BuildStatus.Success.toString(), actual.toString());
	}

	@Test
	public void testGetBuildStatusUnstable() throws Exception {
		// Act
		BuildStatus actual = buildUtil.getBuildStatus("partiallySucceeded");
		// Assert
		assertEquals(BuildStatus.Unstable.toString(), actual.toString());
	}

	@Test
	public void testGetBuildStatusFailure() throws Exception {
		// Act
		BuildStatus actual = buildUtil.getBuildStatus("failed");
		// Assert
		assertEquals(BuildStatus.Failure.toString(), actual.toString());
	}

	@Test
	public void testGetBuildStatusAborted() throws Exception {
		// Act
		BuildStatus actual = buildUtil.getBuildStatus("canceled");
		// Assert
		assertEquals(BuildStatus.Aborted.toString(), actual.toString());
	}

	@Test
	public void testGetBuildStatusUnknown() throws Exception {
		// Act
		BuildStatus actual = buildUtil.getBuildStatus("test");
		// Assert
		assertEquals(BuildStatus.Unknown.toString(), actual.toString());
	}

	@Test
	public void testGetTheLast90DaysInTime() throws Exception {
		// Arrange
		long expected = System.currentTimeMillis();
		// Act
		long actual = buildUtil.getTheLast90DaysInTime();
		// Assert
		assertTrue(expected > actual);
	}

	@Test
	public void testExtractedContinuationToken() throws Exception {
		// Arrange
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		List<String> values = new ArrayList<String>();
		String expected = "12";
		values.add(expected);
		headers.put("x-ms-continuationtoken", values);
		ResponseEntity<VSTSBuildResponse> response = new ResponseEntity<VSTSBuildResponse>(new VSTSBuildResponse(),
				headers, HttpStatus.ACCEPTED);
		// Act
		String actual = buildUtil.extractedContinuationToken(response);
		// Assert
		assertEquals(expected, actual);
	}

	@Test
	public void testExtractedContinuationTokenEmpty() throws Exception {
		// Arrange
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();

		ResponseEntity<VSTSBuildResponse> response = new ResponseEntity<VSTSBuildResponse>(new VSTSBuildResponse(),
				headers, HttpStatus.ACCEPTED);
		// Act
		String actual = buildUtil.extractedContinuationToken(response);
		// Assert
		assertTrue(actual.isEmpty());
	}

	@Test
	public void testGetDifferentElementsBetweenList() throws Exception {
		// Arrange
		List<Build> azureJob = new ArrayList<>();
		Build build1 = new Build();
		Build build2 = new Build();
		azureJob.add(build1);
		azureJob.add(build2);
		List<Build> current = new ArrayList<>();
		current.add(build1);
		// Act
		List<Build> actual = buildUtil.getDifferentElementsBetweenList(azureJob, current);
		// Assert
		assertFalse(actual.isEmpty());
		assertEquals(actual.size(), 1);
		assertEquals(actual.get(0), build2);
	}

	@Test
	public void testGetDifferentElements() throws Exception {
		// Arrange
		List<VSTSBuildJob> main = new ArrayList<>();
		List<VSTSBuildJob> sub = new ArrayList<>();
		VSTSBuildJob job1 = new VSTSBuildJob();
		job1.setJobId("1");
		VSTSBuildJob job2 = new VSTSBuildJob();
		job2.setJobId("2");
		VSTSBuildJob job3 = new VSTSBuildJob();
		job3.setJobId("3");
		main.add(job1);
		main.add(job2);
		main.add(job3);
		sub.add(job1);
		sub.add(job2);
		// Act
		List<VSTSBuildJob> actual = buildUtil.getDifferentElementsBetweenList(main, sub);
		// Assert
		assertThat(actual, hasSize(1));
		assertTrue(actual.contains(job3));
	}
}
