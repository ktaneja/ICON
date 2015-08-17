package edu.ncsu.csc.ase.dristi.datastructure;

public class IDFreqPair<T> implements Comparable<IDFreqPair<T>>{
	
	private T id;
	private long freq;
	
	public IDFreqPair(T id) {
		this.id = id;
		this.freq = new Long(0).longValue();
	}
	
	public IDFreqPair(T id, long freq) {
		this.id = id;
		this.freq = freq;
	}
	
	public T getId() {
		return id;
	}

	public void setId(T id) {
		this.id = id;
	}

	public long getFreq() {
		return freq;
	}

	public void setFreq(long freq) {
		this.freq = freq;
	}

	@Override
	public int compareTo(IDFreqPair<T> o) {
		if(this.freq<o.getFreq())
			return -1;
		else if(this.freq>o.getFreq())
			return 1;
		else
			return 0;
	}

}
