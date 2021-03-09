package ssUtils;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

class DependancyGraphTest
{

	@Test
	void testDependancyGraph()
	{
		/**
		 * @formatter:off
		 * DG = {("a", "b"), ("a", "c"), ("b", "d"), ("d", "d")} 
		 * 	dependents("a") = {"b","c"} 
		 * 	dependents("b") = {"d"} 
		 * 	dependents("c") = {} 
		 * 	dependents("d") = {"d"}
		 * 	dependees("a") = {} 
		 * 	dependees("b") = {"a"} 
		 * 	dependees("c") = {"a"}
		 * 	dependees("d") = {"b", "d"}
		 * @formatter:on
		 */

		// new DependencyGraph shouldn't throw any exceptions when created
		DependancyGraph dg = new DependancyGraph();

		// size of new DependencyGraph should be 0
		assertEquals(dg.size(), 0);

		// getting dependents of any vertex should return empty Iterator
		assertEquals(countElementsInIterable(dg.getDependents("a")), 0);
		assertEquals(countElementsInIterable(dg.getDependents("foo")), 0);
		assertEquals(countElementsInIterable(dg.getDependents("bar")), 0);

		assertEquals(countElementsInIterable(dg.getDependees("a")), 0);
		assertEquals(countElementsInIterable(dg.getDependees("foo")), 0);
		assertEquals(countElementsInIterable(dg.getDependees("bar")), 0);
	}

	/**
	 * Returns the number of items in iterable
	 */
	private int countElementsInIterable(Iterable<String> iterable)
	{
		int items = 0;
		for (String s : iterable)
			items++;

		return items;
	}

	@Test
	void testSize()
	{

		DependancyGraph dg = new DependancyGraph();

		// size of new DependencyGraph should be 0
		assertEquals(dg.size(), 0);

		// add four ordered pairs to the dg
		dg.addDependency("a", "b");
		dg.addDependency("a", "c");
		dg.addDependency("b", "d");
		dg.addDependency("d", "d");

		assertEquals(dg.size(), 4);

		// remove two ordered pair
		dg.removeDependency("a", "c");
		dg.removeDependency("d", "d");

		assertEquals(dg.size(), 2);

		dg.addDependency("a", "c");
		dg.addDependency("d", "d");

		assertEquals(dg.size(), 4);

		dg.removeDependency("a", "b");

		assertEquals(dg.size(), 3);
	}

	@Test
	void testDependeeSize()
	{

		DependancyGraph dg = new DependancyGraph();

		// add four ordered pairs to the dg
		dg.addDependency("a", "b");
		dg.addDependency("a", "c");
		dg.addDependency("b", "d");
		dg.addDependency("d", "d");

		assertEquals(0, dg.dependeeSize("a"));
		assertEquals(1, dg.dependeeSize("b"));
		assertEquals(1, dg.dependeeSize("c"));
		assertEquals(2, dg.dependeeSize("d"));

	}

	@Test
	void testHasDependents()
	{
		DependancyGraph dg = new DependancyGraph();

		dg.addDependency("a", "b");
		dg.addDependency("a", "c");
		dg.addDependency("b", "d");
		dg.addDependency("d", "d");

		assertTrue(dg.hasDependents("a"));
		assertTrue(dg.hasDependents("b"));
		assertTrue(dg.hasDependents("d"));

		assertFalse(dg.hasDependents("c"));
	}

	@Test
	void testHasDependees()
	{
		DependancyGraph dg = new DependancyGraph();

		dg.addDependency("a", "b");
		dg.addDependency("a", "c");
		dg.addDependency("b", "d");
		dg.addDependency("d", "d");

		assertFalse(dg.hasDependees("a"));

		assertTrue(dg.hasDependees("b"));
		assertTrue(dg.hasDependees("c"));
		assertTrue(dg.hasDependees("d"));
	}

	@Test
	void testGetDependents()
	{
		/**
		 * @formatter:off
		 * DG = {("a", "b"), ("a", "c"), ("b", "d"), ("d", "d")} 
		 * 	dependents("a") = {"b","c"} 
		 * 	dependents("b") = {"d"} 
		 * 	dependents("c") = {} 
		 * 	dependents("d") = {"d"}
		 * 	dependees("a") = {} 
		 * 	dependees("b") = {"a"} 
		 * 	dependees("c") = {"a"}
		 * 	dependees("d") = {"b", "d"}
		 * @formatter:on
		 */

		DependancyGraph dg = new DependancyGraph();

		dg.addDependency("a", "b");
		dg.addDependency("a", "c");
		dg.addDependency("b", "d");
		dg.addDependency("d", "d");
		
		String[] expectedDependentsOfA = {"b","c"};
		String[] expectedDependentsOfB = {"d"};
		String[] expectedDependentsOfC = {};
		String[] expectedDependentsOfD = {"d"};
		
		assertTrue(compareIterables(dg.getDependents("a"), turnStringArrayIntoIterable(expectedDependentsOfA)));
		assertTrue(compareIterables(dg.getDependents("b"), turnStringArrayIntoIterable(expectedDependentsOfB)));
		assertTrue(compareIterables(dg.getDependents("c"), turnStringArrayIntoIterable(expectedDependentsOfC)));
		assertTrue(compareIterables(dg.getDependents("d"), turnStringArrayIntoIterable(expectedDependentsOfD)));
	}

	/**
	 * Returns true if the number of items in each Iterable are the same and all
	 * elements in each Iterable are the same
	 */
	private boolean compareIterables(Iterable<String> iterable1, Iterable<String> iterable2)
	{
		// check that both are the same size
		if (countElementsInIterable(iterable1) != countElementsInIterable(iterable2))
		{
			return false;
		}
		
		// turn iterable1 into a hashset to check its elements
		HashSet<String> setOfIterable1 = new HashSet();
		for (String s : iterable1)
		{
			setOfIterable1.add(s);
		}
		
		// make sure all elements in iterable2 are in iterable1
		for (String s : iterable2)
		{
			if (!setOfIterable1.contains(s))
				return false;
		}
		
		return true;
	}
	
	/**
	 * Turns a string array into an iterable
	 */
	private Iterable<String> turnStringArrayIntoIterable(String[] strings)
	{
		ArrayList<String> list = new ArrayList(strings.length);
		for (String s : strings)
		{
			list.add(s);
		}
		
		return list;
	}

	@Test
	void testGetDependees()
	{
		/**
		 * @formatter:off
		 * DG = {("a", "b"), ("a", "c"), ("b", "d"), ("d", "d")} 
		 * 	dependents("a") = {"b","c"} 
		 * 	dependents("b") = {"d"} 
		 * 	dependents("c") = {} 
		 * 	dependents("d") = {"d"}
		 * 	dependees("a") = {} 
		 * 	dependees("b") = {"a"} 
		 * 	dependees("c") = {"a"}
		 * 	dependees("d") = {"b", "d"}
		 * @formatter:on
		 */

		DependancyGraph dg = new DependancyGraph();

		dg.addDependency("a", "b");
		dg.addDependency("a", "c");
		dg.addDependency("b", "d");
		dg.addDependency("d", "d");
		
		String[] expectedDependeesOfA = {};
		String[] expectedDependeesOfB = {"a"};
		String[] expectedDependeesOfC = {"a"};
		String[] expectedDependeesOfD = {"b", "d"};
		
		assertTrue(compareIterables(dg.getDependees("a"), turnStringArrayIntoIterable(expectedDependeesOfA)));
		assertTrue(compareIterables(dg.getDependees("b"), turnStringArrayIntoIterable(expectedDependeesOfB)));
		assertTrue(compareIterables(dg.getDependees("c"), turnStringArrayIntoIterable(expectedDependeesOfC)));
		assertTrue(compareIterables(dg.getDependees("d"), turnStringArrayIntoIterable(expectedDependeesOfD)));

	}

	@Test
	void testAddDependency()
	{
		DependancyGraph dg = new DependancyGraph();

		// size of new DependencyGraph should be 0
		assertEquals(dg.size(), 0);

		// add four ordered pairs to the dg
		dg.addDependency("a", "b");
		dg.addDependency("a", "c");
		dg.addDependency("b", "d");
		dg.addDependency("d", "d");

		assertEquals(dg.size(), 4);

		// remove two ordered pair
		dg.removeDependency("a", "c");
		dg.removeDependency("d", "d");

		assertEquals(dg.size(), 2);

		dg.addDependency("a", "c");
		dg.addDependency("d", "d");

		assertEquals(dg.size(), 4);

		dg.removeDependency("a", "b");

		assertEquals(dg.size(), 3);
	}

	@Test
	void testRemoveDependency()
	{
		DependancyGraph dg = new DependancyGraph();

		// size of new DependencyGraph should be 0
		assertEquals(dg.size(), 0);

		// add four ordered pairs to the dg
		dg.addDependency("a", "b");
		dg.addDependency("a", "c");
		dg.addDependency("b", "d");
		dg.addDependency("d", "d");

		assertEquals(dg.size(), 4);

		// remove two ordered pair
		dg.removeDependency("a", "c");
		dg.removeDependency("d", "d");

		assertEquals(dg.size(), 2);

		dg.addDependency("a", "c");
		dg.addDependency("d", "d");

		assertEquals(dg.size(), 4);

		dg.removeDependency("a", "b");

		assertEquals(dg.size(), 3);
	}

	@Test
	void testReplaceDependents()
	{
		DependancyGraph graph = new DependancyGraph();
		graph.addDependency("a", "b");
		graph.addDependency("b", "c");
		graph.addDependency("b", "d");

		HashSet<String> newDependents = new HashSet<String>();
		newDependents.add("e");
		newDependents.add("f");
		newDependents.add("g");

		graph.replaceDependents("b", newDependents);

		// b should have dependents of e f and g and no others
		Iterable<String> dependentsOfB = graph.getDependents("b");
		HashSet<String> dependentsOfBSet = new HashSet<>();
		for (String s : dependentsOfB)
		{
			dependentsOfBSet.add(s);
			assertTrue(newDependents.contains(s)); // each returned item should be in newDependents
		}

		assertFalse(dependentsOfBSet.contains("c")); // should not contain old dependents
		assertFalse(dependentsOfBSet.contains("d")); // should not contain old dependents

		assertEquals(dependentsOfBSet.size(), newDependents.size()); // no additional item should be in newDependents.

	}

	@Test
	void testReplaceDependees()
	{
		DependancyGraph graph = new DependancyGraph();
		graph.addDependency("a", "b");
		graph.addDependency("b", "c");
		graph.addDependency("b", "d");

		HashSet<String> newDependees = new HashSet<String>();
		newDependees.add("e");
		newDependees.add("f");
		newDependees.add("g");

		graph.replaceDependees("b", newDependees);

		// b should have dependees of e f and g and no others
		Iterable<String> dependendeesOfB = graph.getDependees("b");
		HashSet<String> dependeesOfBSet = new HashSet<>();
		for (String s : dependendeesOfB)
		{
			dependeesOfBSet.add(s);
			assertTrue(newDependees.contains(s)); // each returned item should be in newDependents
		}

		assertFalse(dependeesOfBSet.contains("a")); // should not contain old dependees

		assertEquals(dependeesOfBSet.size(), newDependees.size()); // no additional item should be in newDependents.
	}

}
