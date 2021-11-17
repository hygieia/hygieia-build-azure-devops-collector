package com.capitalone.dashboard.bussiness.logic;

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

import com.capitalone.dashboard.model.BuildComparable;
import com.capitalone.dashboard.model.BuildStatus;
import com.capitalone.dashboard.model.SCM;

@RunWith(MockitoJUnitRunner.class)
public class BuildLogicImplTest {

	@InjectMocks
	private BuildLogicImpl buildLogicImpl;

	@Test
	public void testGetBuildsPending() throws Exception {
		// Arrange
		List<BuildComparable> current = new ArrayList<>();
		BuildComparable build1 = getBuild();
		build1.setNumber("1");
		BuildComparable build2 = getBuild();
		build2.setNumber("2");
		BuildComparable build3 = getBuild();
		build3.setNumber("3");
		build3.setSourceChangeSet(new ArrayList<>());
		BuildComparable build4 = getBuild();
		build4.setNumber("4");
		current.add(build1);
		current.add(build2);
		current.add(build3);
		List<BuildComparable> azure = new ArrayList<>();
		BuildComparable build11 = getBuild();
		build11.setBuildStatus(BuildStatus.Failure);
		build11.setNumber("1");
		azure.add(build11);
		BuildComparable build22 = getBuild();
		build22.setNumber("2");
		build22.setStartTime(120000000L);
		azure.add(build22);
		BuildComparable build33 = getBuild();
		build33.setNumber("3");
		azure.add(build33);
		BuildComparable build44 = getBuild();
		azure.add(build44);
		// Act
		List<BuildComparable> actual = buildLogicImpl.getBuildsPending(current, azure);
		// Assert
		assertFalse(actual.isEmpty());
		assertEquals(actual.size(), 3);
		assertTrue(actual.contains(build1));
		assertTrue(actual.contains(build2));
		assertTrue(actual.contains(build3));
		assertFalse(actual.contains(build4));
	}

	@Test
	public void testBuildPropagation() throws Exception {
		// Arrange
		List<BuildComparable> current = new ArrayList<>();
		BuildComparable build1 = new BuildComparable();
		BuildComparable build2 = new BuildComparable();
		List<SCM> commits = new ArrayList<>();
		SCM scm = new SCM();
		scm.setScmRevisionNumber("123");
		commits.add(scm);
		build1.setSourceChangeSet(commits);
		current.add(build1);
		current.add(build2);
		// Act
		List<BuildComparable> actual = buildLogicImpl.buildPropagation(current);
		// Assert
		assertThat(actual, hasSize(2));
		assertTrue(actual.contains(build1));
		assertTrue(actual.get(actual.lastIndexOf(build2)).getSourceChangeSet().contains(scm));
	}

	@Test
	public void testBuildPropagationSingleItem() throws Exception {
		// Arrange
		List<BuildComparable> current = new ArrayList<>();
		BuildComparable build1 = new BuildComparable();
		current.add(build1);
		// Act
		List<BuildComparable> actual = buildLogicImpl.buildPropagation(current);
		// Assert
		assertThat(actual, hasSize(1));
		assertTrue(actual.contains(build1));
	}

	private BuildComparable getBuild() {
		BuildComparable build = new BuildComparable();
		build.setBuildStatus(BuildStatus.Success);
		List<SCM> commits = new ArrayList<>();
		SCM scm = new SCM();
		commits.add(scm);
		build.setSourceChangeSet(commits);
		build.setNumber("4");
		build.setStartedCommit("4");
		build.setStartTime(40000000L);
		return build;
	}

}
