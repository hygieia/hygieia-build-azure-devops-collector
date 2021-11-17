package com.capitalone.dashboard.bussiness.workflow;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
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

import com.capitalone.dashboard.bussiness.logic.BuildLogic;
import com.capitalone.dashboard.model.Build;
import com.capitalone.dashboard.model.BuildComparable;
import com.capitalone.dashboard.model.VSTSBuildJob;
import com.capitalone.dashboard.repository.BuildCollectionRepository;
import com.capitalone.dashboard.service.BuildService;
import com.capitalone.dashboard.util.BuildUtil;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;

@RunWith(MockitoJUnitRunner.class)
public class BuildBWImplTest {

	@Mock
	private BuildLogic buildLogic;

	@Mock
	private BuildUtil buildUtil;

	@Mock
	private BuildService buildService;
	@Mock
	private BuildCollectionRepository buildCollectionRepository;

	@Mock
	private Appender<ILoggingEvent> mockedAppender;

	@Captor
	private ArgumentCaptor<LoggingEvent> loggingEventCaptor;

	@InjectMocks
	private BuildBWImpl buildBWImpl;

	@Test
	public void testWorkflow() throws Exception {
		// Arrange
		ObjectId id = ObjectId.get();
		VSTSBuildJob collectorItem = new VSTSBuildJob();
		collectorItem.setId(id);
		List<BuildComparable> builds = new ArrayList<>();
		BuildComparable build = new BuildComparable();
		builds.add(build);
		build.setStartedCommit("aaaa");
		when(buildUtil.getDifferentElementsBetweenList(anyListOf(BuildComparable.class),
				anyListOf(BuildComparable.class))).thenReturn(builds);
		when(buildService.addChangeSets(build)).thenReturn(build);
		when(buildService.addWorkItems(build)).thenReturn(build);
		when(buildLogic.buildPropagation(anyListOf(BuildComparable.class))).thenReturn(builds);
		// Act
		buildBWImpl.workflow(collectorItem);
		// Assert
		verify(buildService).saveBuild(any(BuildComparable.class));
		verify(buildLogic).buildPropagation(anyListOf(BuildComparable.class));
		verify(buildLogic).getBuildsPending(anyListOf(BuildComparable.class), anyListOf(BuildComparable.class));
		verify(buildUtil).getDifferentElementsBetweenList(anyListOf(Build.class), anyListOf(Build.class));
		verify(buildService).addChangeSets(build);
		verify(buildService).addWorkItems(build);
	}

	@Test
	public void testWorkflowException() throws Exception {
		// Arrange
		Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		root.addAppender(mockedAppender);
		root.setLevel(Level.DEBUG);
		ObjectId id = ObjectId.get();
		VSTSBuildJob collectorItem = new VSTSBuildJob();
		collectorItem.setId(id);
		doThrow(new NullPointerException()).when(buildService).getBuilds(collectorItem);
		// Act
		buildBWImpl.workflow(collectorItem);
		// Assert
		verify(mockedAppender, times(1)).doAppend(loggingEventCaptor.capture());
		LoggingEvent loggingEvent = loggingEventCaptor.getAllValues().get(0);
		assertEquals(Level.DEBUG, loggingEvent.getLevel());
	}

}
