package com.capitalone.dashboard.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.LoggerFactory;

import com.capitalone.dashboard.adapter.BuildAdapter;
import com.capitalone.dashboard.adapter.WorkItemAdapter;
import com.capitalone.dashboard.client.AzureDevOpsClient;
import com.capitalone.dashboard.exception.AzureDevOpsApiException;
import com.capitalone.dashboard.model.Build;
import com.capitalone.dashboard.model.BuildComparable;
import com.capitalone.dashboard.model.SCM;
import com.capitalone.dashboard.model.VSTSBuild;
import com.capitalone.dashboard.model.VSTSBuildChanges;
import com.capitalone.dashboard.model.VSTSBuildJob;
import com.capitalone.dashboard.model.VSTSBuildWorkItems;
import com.capitalone.dashboard.model.VSTSFields;
import com.capitalone.dashboard.model.VSTSWorkItem;
import com.capitalone.dashboard.model.WorkItem;
import com.capitalone.dashboard.repository.BuildCollectionRepository;
import com.capitalone.dashboard.util.BuildUtil;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;

@RunWith(MockitoJUnitRunner.class)
public class BuildServiceImplTest {

	@Mock
	private AzureDevOpsClient azureDevOpsClient;
	@Mock
	private BuildAdapter buildAdapter;
	@Mock
	private WorkItemAdapter workItemAdapter;
	@Mock
	private BuildCollectionRepository buildCollectionRepository;
	@Mock
	private BuildUtil buildUtil;
	@Mock
	private Appender<ILoggingEvent> mockedAppender;

	@Captor
	private ArgumentCaptor<LoggingEvent> loggingEventCaptor;

	@InjectMocks
	private BuildServiceImpl buildServiceImpl;

	@Test
	public void testCleanBuilds() throws Exception {
		// Arrange
		ObjectId id = ObjectId.get();
		VSTSBuildJob collectorItem = new VSTSBuildJob();
		collectorItem.setId(id);

		List<BuildComparable> buildsComparable = new ArrayList<>();

		BuildComparable buildComparable = new BuildComparable();
		buildComparable.setNumber("123");
		buildsComparable.add(buildComparable);

		List<Build> builds = new ArrayList<>();
		Build build = new Build();
		build.setNumber("123");
		builds.add(build);
		List<String> buildIds = new ArrayList<>();
		buildIds.add("123");
		when(buildCollectionRepository.findBuildsOlderByCollectorItemId(collectorItem.getId(), buildIds))
				.thenReturn(builds);
		// Act
		buildServiceImpl.cleanBuilds(collectorItem, buildsComparable);
		// Assert
		verify(buildCollectionRepository).findBuildsOlderByCollectorItemId(collectorItem.getId(), buildIds);
		verify(buildCollectionRepository).delete(builds);
	}

	@Test
	public void testAddChangeSets() throws Exception {
		// Arrange
		String number = "123";
		BuildComparable expected = new BuildComparable();
		expected.setNumber(number);
		List<VSTSBuildChanges> changes = new ArrayList<>();
		VSTSBuildChanges change = new VSTSBuildChanges();
		changes.add(change);
		when(azureDevOpsClient.getBuildChanges(number)).thenReturn(changes);
		SCM scm = new SCM();
		when(buildAdapter.convertVSTSBuildChangesToSCM(change)).thenReturn(scm);
		// Act
		BuildComparable actual = buildServiceImpl.addChangeSets(expected);
		// Assert
		assertEquals(expected, actual);
		assertTrue(actual.getSourceChangeSet().contains(scm));
		verify(azureDevOpsClient).getBuildChanges(number);
		verify(buildAdapter).convertVSTSBuildChangesToSCM(change);
	}

	@Test(expected = RuntimeException.class)
	public void testAddChangeSetsException() throws Exception {
		// Arrange
		String number = "123";
		BuildComparable expected = new BuildComparable();
		expected.setNumber(number);
		doThrow(new AzureDevOpsApiException("fail")).when(azureDevOpsClient).getBuildChanges(number);
		// Act
		buildServiceImpl.addChangeSets(expected);
	}

	@Test
	public void testGetBuilds() throws Exception {
		// Arrange
		String jobId = "123";
		VSTSBuildJob collectorItem = new VSTSBuildJob();
		collectorItem.setJobId(jobId);
		List<VSTSBuild> builds = new ArrayList<>();
		VSTSBuild vstsbuild = new VSTSBuild();
		vstsbuild.setSourceVersion("123");
		builds.add(vstsbuild);
		when(azureDevOpsClient.getBuilds(jobId)).thenReturn(builds);
		BuildComparable build = new BuildComparable();
		when(buildAdapter.convertVSTSBuildToBuild(collectorItem, vstsbuild)).thenReturn(build);
		// Act

		List<BuildComparable> actual = buildServiceImpl.getBuilds(collectorItem);
		// Assert
		assertTrue(actual.contains(build));
		verify(azureDevOpsClient).getBuilds(jobId);
		verify(buildAdapter).convertVSTSBuildToBuild(collectorItem, vstsbuild);
	}

	@Test(expected = RuntimeException.class)
	public void testGetBuildsException() throws Exception {
		// Arrange
		String jobId = "123";
		VSTSBuildJob collectorItem = new VSTSBuildJob();
		collectorItem.setJobId(jobId);
		doThrow(new AzureDevOpsApiException("Fail")).when(azureDevOpsClient).getBuilds(jobId);
		// Act
		buildServiceImpl.getBuilds(collectorItem);
	}

	@Test
	public void testGetBuildComparable() throws Exception {
		// Arrange
		List<Build> builds = new ArrayList<>();
		List<BuildComparable> expected = new ArrayList<BuildComparable>();
		when(buildAdapter.convertBuildListToComparableList(builds)).thenReturn(expected);
		// Act
		List<BuildComparable> actual = buildServiceImpl.getBuildComparable(builds);
		// Assert
		assertEquals(expected, actual);
		verify(buildAdapter).convertBuildListToComparableList(builds);
	}

	@Test
	public void testSaveBuild() throws Exception {
		// Arrange

		Build build = new Build();
		// Act
		buildServiceImpl.saveBuild(build);
		// Assert
		verify(buildCollectionRepository).save(build);
	}

	@Test
	public void testSaveBuildException() throws Exception {
		// Arrange
		Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		root.addAppender(mockedAppender);
		root.setLevel(Level.ERROR);
		Build build = new Build();
		doThrow(new RuntimeException()).when(buildCollectionRepository).save(build);
		// Act
		buildServiceImpl.saveBuild(build);
		// Assert
		verify(mockedAppender, times(1)).doAppend(loggingEventCaptor.capture());
		LoggingEvent loggingEvent = loggingEventCaptor.getAllValues().get(0);
		assertEquals(Level.ERROR, loggingEvent.getLevel());

	}

	@Test(expected = RuntimeException.class)
	public void testAddWorkItemsException() throws Exception {
		// Arrange
		String hash = "123";
		BuildComparable build = new BuildComparable();
		build.setStartedCommit(hash);
		SCM commit = new SCM();
		commit.setScmRevisionNumber(hash);
		build.addSourceChangeSet(commit);
		doThrow(new AzureDevOpsApiException("Fail")).when(azureDevOpsClient).getBuildWorkItemRef(any(), any());
		// Act
		buildServiceImpl.addWorkItems(build);
	}

	@Test
	public void testAddWorkItemsEmpty() throws Exception {
		// Arrange
		BuildComparable build = new BuildComparable();
		build.setStartedCommit("123");
		List<VSTSBuildWorkItems> refs = new ArrayList<VSTSBuildWorkItems>();
		when(azureDevOpsClient.getBuildWorkItemRef(any(), any())).thenReturn(refs);
		// Act
		BuildComparable actual = buildServiceImpl.addWorkItems(build);
		assertTrue(actual.getWorkItems().isEmpty());
	}

	@Test
	public void testAddWorkItems() throws Exception {
		// Arrange
		String hash = "123";
		BuildComparable build = new BuildComparable();
		build.setStartedCommit(hash);
		SCM commit = new SCM();
		commit.setScmUrl("/repositories/test/commits/");
		commit.setScmRevisionNumber(hash);
		build.addSourceChangeSet(commit);
		VSTSBuildWorkItems ref = new VSTSBuildWorkItems();
		ref.setId("1234");
		List<VSTSBuildWorkItems> refs = new ArrayList<VSTSBuildWorkItems>();
		refs.add(ref);
		when(azureDevOpsClient.getBuildWorkItemRef(anyString(), anyString())).thenReturn(refs);
		VSTSWorkItem workItem1 = new VSTSWorkItem();

		VSTSWorkItem workItem2 = new VSTSWorkItem();
		VSTSFields fields = new VSTSFields();
		fields.setSystemWorkItemType("User Story");
		workItem2.setFields(fields);

		VSTSWorkItem workItem3 = new VSTSWorkItem();
		VSTSFields fieldsBad = new VSTSFields();
		fieldsBad.setSystemWorkItemType("test");
		workItem3.setFields(fieldsBad);

		List<VSTSWorkItem> workItems = new ArrayList<>();
		workItems.add(workItem1);
		workItems.add(workItem2);
		workItems.add(workItem3);
		when(azureDevOpsClient.getBuildWorkItem(anyListOf(String.class))).thenReturn(workItems);
		WorkItem expected = new WorkItem();
		expected.setIdWorkItem("test");
		when(workItemAdapter.getWorkItem(any())).thenReturn(expected = new WorkItem());
		// Act
		BuildComparable actual = buildServiceImpl.addWorkItems(build);
		// Assert
		assertEquals(actual.getWorkItems().get(0).getIdWorkItem(), workItem2.getId());
		assertFalse(actual.getWorkItems().isEmpty());
		assertEquals(actual.getWorkItems().size(), 1);
	}

}
