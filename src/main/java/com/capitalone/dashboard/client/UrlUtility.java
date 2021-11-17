package com.capitalone.dashboard.client;

import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.capitalone.dashboard.collector.BuildSettings;
import com.capitalone.dashboard.util.BuildUtil;

@Component
public class UrlUtility {

	private static final String DEFAULT_PROTOCOL = "https";
	private static final String SEGMENT_API = "_apis";
	private static final String API_VERSION = "api-version";
	private static final String PUBLIC_VSTS_HOST_NAME = "dev.azure.com";
	private static final String SEGMENT_DEFINITIONS = "definitions";
	private static final String SEGMENT_BUILD = "build";
	private static final String SEGMENT_BUILDS = "builds";
	private static final String SEGMENT_CHANGES = "changes";
	private static final String SEGMENT_WORKITEMS = "workitems";
	private static final String SEGMENT_ARTIFACTURIQUERY = "artifacturiquery";
	private static final String SEGMENT_FIELDS = "fields";
	private static final String SEGMENT_WIT = "wit";
	private static final String API_VERSION_5_0 = "5.0";
	private static final String API_VERSION_5_1_PREVIEW = "5.0-preview.1";
	private static final int DEFINITIONS_SIZE = 500;
	private static final String DATE_FORMAT_QUERY_VSTS = "MM/dd/yyyy'%20'HH:mm:ss";
	private static final String TOP = "$top";
	private static final String IDS = "ids";
	private static final String CONTINUATION_TOKEN = "continuationToken";

	private final BuildSettings collectorSettings;
	private final BuildUtil buildUtil;

	@Autowired
	public UrlUtility(BuildSettings collectorSettings, BuildUtil buildUtil) {
		this.collectorSettings = collectorSettings;
		this.buildUtil = buildUtil;
	}

	public String getBaseHost() {
		String apiHost = StringUtils.isBlank(getHost()) ? PUBLIC_VSTS_HOST_NAME : getHost();
		StringBuilder repoHost = new StringBuilder();
		repoHost.append(apiHost);
		return repoHost.toString();
	}

	private String getHost() {
		return collectorSettings.getHost();
	}

	private String getProtocol() {
		return StringUtils.isBlank(collectorSettings.getProtocol()) ? DEFAULT_PROTOCOL
				: collectorSettings.getProtocol();
	}

	public String getAccountName() {
		return collectorSettings.getAccount();
	}

	private String getProject() {
		return collectorSettings.getProjectId();
	}

	private String getPort() {
		return collectorSettings.getPort();
	}

	private UriComponentsBuilder apiBase() {
		String protocol = getProtocol();
		String host = getBaseHost();
		String project = getProject();
		String accountName = getAccountName();

		UriComponentsBuilder builderBase = UriComponentsBuilder.newInstance();

		if (StringUtils.isNotBlank(getPort())) {
			builderBase.port(getPort());
		}

		builderBase.scheme(protocol).host(host).pathSegment(accountName).pathSegment(project).pathSegment(SEGMENT_API);

		return builderBase;
	}

	public URI getApiBuildDefinitions(String continuationToken) {
		String apiVersion = API_VERSION_5_0;
		UriComponentsBuilder builderBase = apiBase();
		continuationToken = continuationToken.replace(" ", "%20");
		URI uri = builderBase.pathSegment().pathSegment(SEGMENT_BUILD).pathSegment(SEGMENT_DEFINITIONS)
				.queryParam(TOP, DEFINITIONS_SIZE).queryParam("queryOrder", "lastModifiedAscending")
				.queryParam(CONTINUATION_TOKEN, continuationToken).queryParam(API_VERSION, apiVersion).build(true)
				.toUri();
		return uri;
	}

	public URI getApiBuildsbyBuildDefinition(String buildDefinitionId, String continuationToken) {
		String apiVersion = API_VERSION_5_0;
		UriComponentsBuilder builderBase = apiBase();

		URI uri = builderBase.pathSegment().pathSegment(SEGMENT_BUILD).pathSegment(SEGMENT_BUILDS)
				.queryParam(SEGMENT_DEFINITIONS, buildDefinitionId).queryParam(TOP, DEFINITIONS_SIZE)
				.queryParam("queryOrder", "finishTimeAscending").queryParam(CONTINUATION_TOKEN, continuationToken)
				.queryParam("minTime", getDateForTime()).queryParam("statusFilter", "completed")
				.queryParam(API_VERSION, apiVersion).build(true).toUri();
		return uri;
	}

	public URI getApiBuild(String buildId) {
		String apiVersion = API_VERSION_5_0;
		UriComponentsBuilder builderBase = apiBase();

		URI uri = builderBase.pathSegment().pathSegment(SEGMENT_BUILD).pathSegment(SEGMENT_BUILDS).pathSegment(buildId)
				.queryParam(API_VERSION, apiVersion).build(true).toUri();
		return uri;
	}

	public URI getApiBuildChangesbyBuildId(String buildId, String continuationToken) {
		String apiVersion = API_VERSION_5_0;
		UriComponentsBuilder builderBase = apiBase();

		URI uri = builderBase.pathSegment().pathSegment(SEGMENT_BUILD).pathSegment(SEGMENT_BUILDS).pathSegment(buildId)
				.pathSegment(SEGMENT_CHANGES).queryParam(TOP, DEFINITIONS_SIZE)
				.queryParam(CONTINUATION_TOKEN, continuationToken).queryParam(API_VERSION, apiVersion).build(true)
				.toUri();
		return uri;
	}
	
	public URI getArtifactUriQuery() {
		String apiVersion = API_VERSION_5_1_PREVIEW;
		UriComponentsBuilder builderBase = apiBase();

		URI uri = builderBase.pathSegment().pathSegment(SEGMENT_WIT).pathSegment(SEGMENT_ARTIFACTURIQUERY)
				.queryParam(API_VERSION, apiVersion).build(true).toUri();
		return uri;
	}
	
	public URI getApiWorkItems(List<String> workItemsIds) {
		String apiVersion = API_VERSION_5_0;
		UriComponentsBuilder builderBase = apiBase();

		URI uri = builderBase.pathSegment().pathSegment(SEGMENT_WIT).pathSegment(SEGMENT_WORKITEMS)
				.queryParam(IDS, String.join(",", workItemsIds))
				.queryParam(SEGMENT_FIELDS,
						"System.AreaPath,System.TeamProject,System.IterationPath,System.WorkItemType,System.State,System.CreatedDate")
				.queryParam(API_VERSION, apiVersion).build(true).toUri();
		return uri;
	}
	
	public String getCommitUrl(String repository, String commit) {
		String base = String.join(StringUtils.EMPTY, "vstfs:///Git/Commit/", collectorSettings.getProjectId());
		String args = String.join("%2F", repository, commit);
		return String.join("%2F", base, args);
	}

	private String getDateForTime() {
		long time = buildUtil.getTheLast90DaysInTime();
		LocalDateTime date = Instant.ofEpochMilli(time).atZone(ZoneOffset.UTC).toLocalDateTime();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_QUERY_VSTS);
		return date.format(formatter);
	}
}
