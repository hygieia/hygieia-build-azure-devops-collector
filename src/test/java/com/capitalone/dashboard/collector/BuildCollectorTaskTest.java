package com.capitalone.dashboard.collector;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.LoggerFactory;

import com.capitalone.dashboard.bussiness.workflow.CollectorBW;
import com.capitalone.dashboard.model.CollectorType;
import com.capitalone.dashboard.model.Configuration;
import com.capitalone.dashboard.model.VSTSBuildCollector;
import com.capitalone.dashboard.model.VSTSBuildJob;
import com.capitalone.dashboard.repository.BaseCollectorRepository;
import com.capitalone.dashboard.repository.BuildCollectorRepository;
import com.capitalone.dashboard.repository.ConfigurationRepository;
import com.capitalone.dashboard.repository.CustomConfigurationRepository;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;

@RunWith(MockitoJUnitRunner.class)
public class BuildCollectorTaskTest {

	@Mock
	private ConfigurationRepository configurationRepository;

	@Mock
	private CustomConfigurationRepository customConfigurationRepository;

	@Mock
	private BuildSettings buildSettings;

	@Mock
	private BuildCollectorRepository buildCollectorRepository;

	@Mock
	private CollectorBW collectorBW;

	@Mock
	private Appender<ILoggingEvent> mockedAppender;

	@Captor
	private ArgumentCaptor<LoggingEvent> loggingEventCaptor;

	private BuildCollectorTask buildCollectorTask;

	@Before
	public void init() {

		buildCollectorTask = new BuildCollectorTask(null, buildCollectorRepository, buildSettings,
				configurationRepository, collectorBW, customConfigurationRepository);
	}

	@Test
	public void testGetCollector() throws Exception {
		// Arrange
		Configuration config = new Configuration();
		when(customConfigurationRepository.findByName(anyString())).thenReturn(config);
		// Act
		VSTSBuildCollector actual = buildCollectorTask.getCollector();
		// Assert
		assertEquals(CollectorType.Build, actual.getCollectorType());
		verify(customConfigurationRepository).findByName(anyString());
	}

	@Test
	public void testGetCollectorWithoutConfig() throws Exception {
		// Arrange
		when(customConfigurationRepository.findByName(anyString())).thenReturn(null);
		// Act
		buildCollectorTask.getCollector();
		// Assert
		verify(buildSettings, never()).setApiTokens(anyListOf(String.class));
		verify(customConfigurationRepository).findByName(anyString());
	}

	@Test
	public void testGetCollectorRepository() throws Exception {
		// Arrange
		// Act
		BaseCollectorRepository<VSTSBuildCollector> actual = buildCollectorTask.getCollectorRepository();
		// Assert
		assertEquals(buildCollectorRepository, actual);
	}

	@Test
	public void testGetCron() throws Exception {
		// Arrange
		String expected = "* * *";
		when(buildSettings.getCron()).thenReturn(expected);
		// Act
		String actual = buildCollectorTask.getCron();
		// Assert
		assertEquals(expected, actual);
	}

	@Test
	public void testCollect() throws Exception {
		// Arrange
		ObjectId id = ObjectId.get();
		VSTSBuildCollector collector = new VSTSBuildCollector();
		collector.setId(id);
		List<VSTSBuildJob> buildJobs = new ArrayList<>();
		when(collectorBW.getAzureCollectorItem()).thenReturn(buildJobs);
		// Act
		buildCollectorTask.collect(collector);
		// Assert
		verify(collectorBW).getAzureCollectorItem();
		verify(collectorBW).cleanAndUpdateCollectorItems(id, buildJobs);
		verify(collectorBW).collectInfoFromEnableCollectorItems(id);
	}

	@Test
	public void testCollectException() throws Exception {
		// Arrange
		Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		root.addAppender(mockedAppender);
		root.setLevel(Level.ERROR);
		VSTSBuildCollector collector = new VSTSBuildCollector();
		doThrow(new RuntimeException()).when(collectorBW).getAzureCollectorItem();
		// Act
		buildCollectorTask.collect(collector);
		// Assert
		verify(collectorBW).getAzureCollectorItem();
		verify(mockedAppender, times(1)).doAppend(loggingEventCaptor.capture());
		LoggingEvent loggingEvent = loggingEventCaptor.getAllValues().get(0);
		assertEquals(Level.ERROR, loggingEvent.getLevel());
	}

}
