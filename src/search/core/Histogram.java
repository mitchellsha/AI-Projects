package search.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class Histogram<T> implements Iterable<T> {
	private HashMap<T,Integer> counts = new HashMap<>();

	public Histogram() {}
	
	public Histogram(Histogram<T> other) {
		this.counts.putAll(other.counts);
		if (this.counts.size() != other.counts.size()) {
			throw new IllegalStateException("Huh? " + this.counts.size() + ": " + other.counts.size());
		}
	}
	
	public void bump(T value) {
		bumpBy(value, 1);
	}
	
	public void bumpBy(T value, int numBumps) {
		counts.put(value, getCountFor(value) + numBumps);
	}
	
	public int getCountFor(T value) {
		return counts.getOrDefault(value, 0);
	}
	
	public int getTotalCounts() {
		int total = 0;
		for (Entry<T,Integer> entry: counts.entrySet()) {
			total += entry.getValue();
		}
		return total;
	}
	
	@Override
	public Iterator<T> iterator() {
		return counts.keySet().iterator();
	}
	
	public T getPluralityWinner() {
		Duple<T,Integer> best = new Duple<T,Integer>(null, 0);
		for (Entry<T,Integer> entry: counts.entrySet()) {
			best = (best == null || entry.getValue() > best.getSecond()) ? new Duple<T, Integer>(entry.getKey(), entry.getValue()) : best;
		}
		return best.getFirst();
	}
}
