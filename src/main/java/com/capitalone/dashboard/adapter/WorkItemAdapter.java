package com.capitalone.dashboard.adapter;

import com.capitalone.dashboard.model.Fields;
import com.capitalone.dashboard.model.VSTSFields;
import com.capitalone.dashboard.model.VSTSWorkItem;
import com.capitalone.dashboard.model.WorkItem;

public interface WorkItemAdapter {

	public WorkItem getWorkItem(VSTSWorkItem vstsWorkItem);
	
	public Fields getFields(VSTSFields vstsFields);
}
