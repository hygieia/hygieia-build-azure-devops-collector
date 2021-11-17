package com.capitalone.dashboard.adapter;

import org.springframework.stereotype.Component;

import com.capitalone.dashboard.model.Fields;
import com.capitalone.dashboard.model.VSTSFields;
import com.capitalone.dashboard.model.VSTSWorkItem;
import com.capitalone.dashboard.model.WorkItem;

@Component
public class WorkItemAdapterImpl implements WorkItemAdapter {

	@Override
	public WorkItem getWorkItem(VSTSWorkItem vstsWorkItem) {
		WorkItem workItem = new WorkItem();
        workItem.setIdWorkItem(vstsWorkItem.getId());
		workItem.setUrl(vstsWorkItem.getUrl());
		workItem.setFields(getFields(vstsWorkItem.getFields()));
		return workItem;
	}
	
	@Override
	public Fields getFields(VSTSFields vstsFields) {
		Fields fields = new Fields();
		fields.setSystemAreaPath(vstsFields.getSystemAreaPath());
		fields.setSystemCreatedDate(vstsFields.getSystemCreatedDate());
		fields.setSystemIterationPath(vstsFields.getSystemIterationPath());
		fields.setSystemState(vstsFields.getSystemState());
		fields.setSystemTeamProject(vstsFields.getSystemTeamProject());
		fields.setSystemWorkItemType(vstsFields.getSystemWorkItemType());
		return fields;
	}
}
