package com.capitalone.dashboard.adapter;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.capitalone.dashboard.model.Fields;
import com.capitalone.dashboard.model.VSTSFields;
import com.capitalone.dashboard.model.VSTSWorkItem;
import com.capitalone.dashboard.model.WorkItem;

@RunWith(MockitoJUnitRunner.class)
public class WorkItemAdapterImplTest {

	@InjectMocks
	private WorkItemAdapterImpl workItemAdapterImpl;

	@Test
	public void testGetWorkItem() {
		// Arrange
		VSTSWorkItem expected = new VSTSWorkItem();
		expected.setId("test");
		VSTSFields fields = new VSTSFields();
		expected.setFields(fields);
		// Act
		WorkItem actual = workItemAdapterImpl.getWorkItem(expected);
		// Assert
		assertEquals(expected.getId(), actual.getIdWorkItem());
	}

	@Test
	public void testGetFields() {
		// Arrange
		VSTSFields expected = new VSTSFields();
		expected.setSystemAreaPath("test");
		// Act
		Fields actual = workItemAdapterImpl.getFields(expected);
		// Assert
		assertEquals(expected.getSystemAreaPath(), actual.getSystemAreaPath());
	}
}
