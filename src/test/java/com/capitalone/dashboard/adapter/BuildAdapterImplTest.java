package com.capitalone.dashboard.adapter;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.capitalone.dashboard.model.Build;
import com.capitalone.dashboard.model.BuildComparable;
import com.capitalone.dashboard.model.BuildlinkWeb;
import com.capitalone.dashboard.model.LinksBuild;
import com.capitalone.dashboard.model.RequestedBuild;
import com.capitalone.dashboard.model.SCM;
import com.capitalone.dashboard.model.VSTSBuild;
import com.capitalone.dashboard.model.VSTSBuildChanges;
import com.capitalone.dashboard.model.VSTSBuildDefinition;
import com.capitalone.dashboard.model.VSTSBuildJob;
import com.capitalone.dashboard.util.BuildUtil;

@RunWith(MockitoJUnitRunner.class)
public class BuildAdapterImplTest {

	@Mock
	private BuildUtil buildUtil;

	@InjectMocks
	private BuildAdapterImpl buildAdapterImpl;

	@Test
	public void testConvertVSTSBuildDefinitionToVSTSBuildJob() throws Exception {
		// Arrange
		VSTSBuildDefinition definition = new VSTSBuildDefinition();
		String expected = "Test";
		definition.setName(expected);
		// Act
		VSTSBuildJob job = buildAdapterImpl.convertVSTSBuildDefinitionToVSTSBuildJob(definition);
		// Assert
		assertEquals(expected, job.getJobName());
	}

	@Test
	public void testConvertVSTSBuildToBuild() throws Exception {
		// Arrange
		VSTSBuild vstsBuild = new VSTSBuild();
		LinksBuild links = new LinksBuild();
		BuildlinkWeb web = new BuildlinkWeb();
		String expected = "Test";
		web.setHref(expected);
		links.setWeb(web);
		vstsBuild.setLinks(links);
		vstsBuild.setFinishTime("2017-06-16T01:36:20.397Z");
		vstsBuild.setStartTime("2017-06-16T01:36:20.397Z");
		RequestedBuild request = new RequestedBuild();
		request.setDisplayName(expected);
		vstsBuild.setRequestedFor(request);
		VSTSBuildJob collectorItem = new VSTSBuildJob();
		// Act
		Build actual = buildAdapterImpl.convertVSTSBuildToBuild(collectorItem, vstsBuild);
		// Assert
		assertEquals(expected, actual.getStartedBy());
		assertEquals(expected, actual.getBuildUrl());
	}

	@Test
	public void testConvertVSTSBuildChangesToSCM() throws Exception {
		// Arrange
		VSTSBuildChanges change = new VSTSBuildChanges();
		change.setTimestamp("2021-03-10T21:45:38Z");
		RequestedBuild author = new RequestedBuild();
		String expected = "test";
		author.setDisplayName(expected);
		change.setAuthor(author);
		// Act
		SCM actual = buildAdapterImpl.convertVSTSBuildChangesToSCM(change);
		// Assert
		assertEquals(expected, actual.getScmAuthor());
	}

	@Test
	public void testConvertBuildListToComparableList() throws Exception {
		// Arrange

		List<Build> builds = new ArrayList<>();
		Build build = new Build();
		String expected = "123";
		build.setNumber(expected);
		builds.add(build);
		// Act
		List<BuildComparable> actual = buildAdapterImpl.convertBuildListToComparableList(builds);
		// Assert
		assertEquals(expected, actual.get(0).getNumber());
	}

}
