package com.capitalone.dashboard.bussiness.logic;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.collections4.map.HashedMap;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.LoggerFactory;

import com.capitalone.dashboard.model.CollectorItem;
import com.capitalone.dashboard.model.CollectorType;
import com.capitalone.dashboard.model.Component;
import com.capitalone.dashboard.model.VSTSBuildJob;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;

@RunWith(MockitoJUnitRunner.class)
public class CollectorLogicImplTest {

	@Mock
	private Component component;

	@Mock
	private Appender<ILoggingEvent> mockedAppender;

	@Captor
	private ArgumentCaptor<LoggingEvent> loggingEventCaptor;

	@InjectMocks
	private CollectorLogicImpl collectorLogicImpl;

	@Test
	public void testDisableUnconfiguredCollectorItems() throws Exception {
		// Arrange
		ObjectId collectorId = ObjectId.get();
		ObjectId collectorItemId = ObjectId.get();

		CollectorItem collectorItem = new CollectorItem();
		collectorItem.setCollectorId(collectorId);
		collectorItem.setId(collectorItemId);
		List<CollectorItem> collectorItems = new ArrayList<>();
		collectorItems.add(collectorItem);
		Map<CollectorType, List<CollectorItem>> collectorItemsInComponent = new HashedMap<>();
		collectorItemsInComponent.put(CollectorType.Build, collectorItems);

		when(component.getCollectorItems()).thenReturn(collectorItemsInComponent);
		List<Component> components = new ArrayList<>();
		components.add(component);

		List<VSTSBuildJob> collectorItemsinDB = new ArrayList<>();
		VSTSBuildJob collectorItemMisconfigured = new VSTSBuildJob();
		collectorItemMisconfigured.setId(collectorItemId);
		collectorItemsinDB.add(collectorItemMisconfigured);

		// Act
		List<VSTSBuildJob> actual = collectorLogicImpl.disableUnconfiguredCollectorItems(collectorId, components,
				collectorItemsinDB);

		// Assert
		assertTrue(actual.get(0).isEnabled());
	}

	@Test
	public void testUpdateOrCreateCollectorItems() throws Exception {
		// Arrange
		ObjectId collectorItemId = ObjectId.get();
		List<VSTSBuildJob> azure = new ArrayList<>();
		List<VSTSBuildJob> db = new ArrayList<>();
		VSTSBuildJob job1 = new VSTSBuildJob();
		job1.setJobId("1");
		VSTSBuildJob job2 = new VSTSBuildJob();
		job2.setJobId("2");
		azure.add(job1);
		azure.add(job2);
		db.add(job1);
		// Act
		List<VSTSBuildJob> actual = collectorLogicImpl.updateOrCreateCollectorItems(collectorItemId, azure, db);
		// Assert
		assertThat(actual, hasSize(2));
		assertTrue(actual.contains(job1));
		assertTrue(actual.contains(job2));
	}

	@Test
	public void testProgressing() throws Exception {
		// Arrange
		Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		root.addAppender(mockedAppender);
		root.setLevel(Level.INFO);
		AtomicInteger atomic = new AtomicInteger(49);
		// Act
		collectorLogicImpl.progressing(atomic, 100);
		// Assert
		verify(mockedAppender, times(1)).doAppend(loggingEventCaptor.capture());
		LoggingEvent loggingEvent = loggingEventCaptor.getAllValues().get(0);
		assertEquals(Level.INFO, loggingEvent.getLevel());
	}

	@Test
	public void testProgressingNotMultiple() throws Exception {
		// Arrange
		Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		root.addAppender(mockedAppender);
		root.setLevel(Level.INFO);
		AtomicInteger atomic = new AtomicInteger(51);
		// Act
		collectorLogicImpl.progressing(atomic, 100);
		// Assert
		verify(mockedAppender, never()).doAppend(loggingEventCaptor.capture());
	}

}
