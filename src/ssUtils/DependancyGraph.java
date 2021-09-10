package ssUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

//@formatter:off
/**
 * (s1,t1) is an ordered pair of strings t1 depends on s1; s1 must be evaluated
 * before t1
 *
 * A DependencyGraph can be modeled as a set of ordered pairs of strings. Two
 * ordered pairs (s1,t1) and (s2,t2) are considered equal if and only if s1
 * equals s2 and t1 equals t2. Recall that sets never contain duplicates. If an
 * attempt is made to add an element to a set, and the element is already in the
 * set, the set remains unchanged.
 *
 * Given a DependencyGraph DG:
 *
 * (1) If s is a string, the set of all strings t such that (s,t) is in DG is
 * called dependents(s). (The set of things that depend on s)
 *
 * (2) If s is a string, the set of all strings t such that (t,s) is in DG is
 * called dependees(s). (The set of things that s depends on)
 *
 * For example, suppose DG = {("a", "b"), ("a", "c"), ("b", "d"), ("d", "d")}
 * dependents("a") = {"b", "c"} dependents("b") = {"d"} dependents("c") = {}
 * dependents("d") = {"d"} dependees("a") = {} dependees("b") = {"a"}
 * dependees("c") = {"a"} dependees("d") = {"b", "d"}
 *
 */
//@formatter:on
public class DependancyGraph
{

    // This graph represented as an adjacency list. If an element has no
    // dependents the key for that element will not be in this dictionary.
    private HashMap<String, HashSet<String>> list;

    // A reverse graph of dependents represented as an adjacency list.
    // If an element has no dependees they key will not be in the dictionary.
    private HashMap<String, HashSet<String>> dependees;

    // Holds the size of this DependencyGraph.
    private int size;

    /**
     * Creates an empty DependencyGraph.
     */
    public DependancyGraph()
    {
        list = new HashMap<String, HashSet<String>>();
        dependees = new HashMap<String, HashSet<String>>();
        size = 0;
    }

    /**
     * Returns the number of ordered pairs in this DependencyGraph
     */
    public int size()
    {
        return size;
    }

    /**
     * Returns the size of the dependees of s.
     */
    public int dependeeSize(String s)
    {
        if (dependees.containsKey(s))
        {
            return dependees.get(s).size();
        }
        else
        {
            return 0;
        }
    }

    /**
     * Returns true if s has dependents
     */
    public boolean hasDependents(String s)
    {
        return list.containsKey(s);
    }

    /**
     * Returns true if s has dependees
     */
    public boolean hasDependees(String s)
    {
        return dependees.containsKey(s);
    }

    /**
     * Returns the dependents of s
     */
    public Iterable<String> getDependents(String s)
    {
        if (hasDependents(s))
        {
            // want copy of set to avoid changing underlying structure
            return copyIterableToHashSet(list.get(s));
        }
        else
        {
            return new HashSet<String>();
        }
    }

    /**
     * Returns the dependees of s
     */
    public Iterable<String> getDependees(String s)
    {
        if (hasDependees(s))
        {
            // want copy of set to avoid changing underlying structure
            return copyIterableToHashSet(dependees.get(s));
        }
        else
        {
            return new HashSet<String>();
        }
    }

    /**
     * Adds the ordered pair (s, t) to this graph if it does not already exist.
     */
    public void addDependency(String s, String t)
    {
        boolean added;
        added = addKeyAndHashValue(list, s, t);
        added = addKeyAndHashValue(dependees, t, s) || added;

        if (added)
        {
            size++;
        }
    }

    /**
     * Removes the ordered pair (s, t) if it is contained by graph
     */
    public void removeDependency(String s, String t)
    {
        boolean removed;
        removed = removeKeyAndHashValue(list, s, t);
        removed = removeKeyAndHashValue(dependees, t, s) || removed;

        if (removed)
        {
            size--;
        }
    }

    /**
     * Removes all existing ordered pairs of the form (s,r). Then, for each t in
     * newDependents, adds the ordered pair (s,t).
     */
    public void replaceDependents(String s, Iterable<String> newDependents)
    {
        // getDependents(s) returns a copy making this safe
        Iterable<String> oldDependents = getDependents(s);
        for (String r : oldDependents)
        {
            removeDependency(s, r);
        }

        for (String t : newDependents)
        {
            addDependency(s, t);
        }
    }

    /**
     * Removes all existing ordered pairs of the form (r,s). Then, for each t in
     * newDependees, adds the ordered pair t,s
     */
    public void replaceDependees(String s, Iterable<String> newDependees)
    {

        // getDependents(s) returns a copy making this safe
        Iterable<String> oldDependees = getDependees(s);
        for (String r : oldDependees)
        {
            removeDependency(r, s);
        }

        for (String t : newDependees)
        {
            addDependency(t, s);
        }
    }

    /* Some helper methods below */
    /**
     * Returns a new HashSet<String> containing all Strings provided by the
     * original Iterable
     */
    private static HashSet<String> copyIterableToHashSet(Iterable<String> original)
    {
        if (original == null)
        {
            return new HashSet<String>();
        }

        HashSet<String> copy = new HashSet<String>();

        for (String s : original)
        {
            copy.add(s);
        }

        return copy;
    }

    /**
     * Takes two strings, a key and a value. If the key exists, in dict, adds
     * the corresponding value to the HashSet. If the key does not exist, adds
     * it to dict, and adds value to its new corresponding HashSet.
     *
     * Returns true if dict or its underlying values were changed.
     */
    private static boolean addKeyAndHashValue(HashMap<String, HashSet<String>> dict, String key, String value)
    {
        if (dict.containsKey(key))
        {
            return dict.get(key).add(value);
        }
        else
        {
            HashSet<String> set = new HashSet<String>();
            set.add(value);
            dict.put(key, set);
            return true;
        }
    }

    /**
     * Takes two strings, a key and a value. If the key exists, in dict, removes
     * the corresponding value from the HashSet. If the underlying HashSet
     * becomes empty, removes the key from dict.
     *
     * Returns true if dict or its underlying values were changed.
     */
    private static boolean removeKeyAndHashValue(HashMap<String, HashSet<String>> dict, String key, String value)
    {

        // We found the key, lets try to remove the edge leading to the value
        if (dict.containsKey(key) && dict.get(key).remove(value))
        {
            // because we removed a value, we must follow the invariant, if there are no
            // more edges from the key, we must remove the underlying HashSet
            if (dict.get(key).size() < 1)
            {
                dict.remove(key);
            }

            return true; // we removed something
        }

        return false; // we didn't remove anything
    }

    
}
