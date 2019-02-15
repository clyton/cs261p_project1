package main;

public interface HashTable<Key, Value> {
	public void put(Key key, Value val);

	public Value get(Key key);

	public void remove(Key key);
}
