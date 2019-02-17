package main;

import java.util.Random;

public class QuadraticProbingTest {
//	QuadraticProbing<Integer, Integer> ht = new QuadraticProbing<>();
//	LinearHashTable<Integer, Integer> ht = new LinearHashTable<>();
//	ChainedHashTable<Integer, Integer> ht = new ChainedHashTable<>();
	CuckooHashTable<Integer, Integer> ht = new CuckooHashTable<>();

	public void generateRandomKeyValue(int[] key, int[] value) {
		for (int i = 0; i < key.length; i++) {
			key[i] = i;
			value[i] = key.length - i;
		}
//		Random random = new Random(3483578L);
		Random random = new Random();
//		System.out.println(3483578L);
		for (int i = 1; i < key.length; i++) {
			int randomIndex = random.nextInt(i);
			int temp = key[randomIndex];
			key[randomIndex] = key[i];
			key[i] = temp;

			temp = value[randomIndex];
			value[randomIndex] = value[i];
			value[i] = temp;
		}

	}

	public void populate(int key[], int value[], int start, int end) {
		for (int i = 0; i < 16; i++) {
			System.out.println(key[i]);
		}

		for (int i = start; i < end; i++) {
			long startTime = System.nanoTime();
			ht.put(key[i], value[i]);
			System.out.printf(
					"Time passed = %d, Size = %d, Capacity = %d, LoadFactor = %f\n",
					(System.nanoTime() - startTime), ht.size(), ht.capacity(),
					ht.loadFactor());

		}
	}

	/**
	 * loads table till load factor reaches the level given or all keys are
	 * exhausted
	 * 
	 * @param key
	 * @param value
	 * @param start
	 * @param loadFactor
	 * @return if {@code index} is the last element inserted, return index+1
	 */
	public int loadTable(int key[], int value[], int start, double loadFactor) {
		int i = 0;
		for (i = start; ht.loadFactor() < loadFactor && i < key.length; i++) {
			ht.put(key[i], value[i]);
		}
		return i;
	}

	public void getAllKeys(int[] key, int[] value) {

		for (int i = 0; i < key.length; i++) {
			Integer r = ht.get(key[i]);
			if (r == null || r != value[i])
				System.out.print("Expected value but got " + r);
		}
	}

	public void removeKeys(int[] key, int start, int end, int step) {
		for (int i = start; i < end && i < key.length; i += step) {
			ht.remove(key[i]);
		}
	}

	public void getKeysShouldPass(int[] key, int[] value, int start, int end,
			int step) {
		for (int i = start; i < end; i += step) {
			Integer r = ht.get(key[i]);
			if (r == null || r != value[i])
				System.out.println("Expected value but got " + r);
		}
	}

	public void getKeysShouldReturnNull(int[] key, int start, int end,
			int step) {
		for (int i = start; i < end; i += step) {
			Integer r = ht.get(key[i]);
			if (r != null)
				System.out.println("Expected null but got value " + r);
		}
	}

	public void test1() {

		final int testSize = 10000;
		int key[] = new int[testSize];
		int value[] = new int[testSize];
		generateRandomKeyValue(key, value);
		populate(key, value, 0, key.length);
		System.out.println("Get keys at load factor = " + ht.loadFactor());
		getAllKeys(key, value);
		removeKeys(key, 0, key.length - 3, 1);
		getKeysShouldPass(key, value, key.length - 3, key.length, 1);
		getKeysShouldReturnNull(key, 0, key.length - 3, 1);
	}

	public void test2() {

		final int testSize = 10000;
		int key[] = new int[testSize];
		int value[] = new int[testSize];
		generateRandomKeyValue(key, value);
//		populate(key, value, 0, key.length);
		int end = loadTable(key, value, 0, 0.69);
		System.out.println("Get keys at load factor = " + ht.loadFactor());
//		getAllKeys(key, value);
		System.out
				.println("load factor = " + ht.loadFactor() + " end = " + end);
		getKeysShouldPass(key, value, 0, end, 1);
		removeKeys(key, 0, end - 3, 1);
		System.out.println("Get keys at load factor = " + ht.loadFactor());
		getKeysShouldPass(key, value, end - 3, end, 1);
		System.out.println("Get keys at load factor = " + ht.loadFactor());
		getKeysShouldReturnNull(key, 0, end - 3, 1);
	}

	public static void main(String[] args) {
		QuadraticProbingTest qt = new QuadraticProbingTest();

		qt.test1();

		System.out.println("Running test 2");
		qt.test2();
		System.out.println("All tests passed");
	}

}
