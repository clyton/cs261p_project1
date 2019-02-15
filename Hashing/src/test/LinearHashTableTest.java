package test;

import main.HashTable;
import main.LinearHashTable;

public class LinearHashTableTest {
	static HashTable<Integer, Integer> ht = new LinearHashTable<>();

	public static void main(String args[]) {

		int key[] = { 18, 41, 22, 44, 59, 32, 31, 73, 25, 13 };
		int value[] = { 18, 41, 22, 44, 59, 32, 31, 73, 25, 13 };

		for (int i = 0; i < key.length; i++) {
			ht.put(key[i], value[i]);
		}

		System.out.println(ht);

		ht.remove(18);
		System.out.println(ht);
//		ht.remove(2000);
//
//		System.out.println(ht);
//
//		ht.remove(20);
//
//		System.out.println(ht);
////
//		ht.put(200, 5);
//		System.out.println(ht);
//		ht.put(50, 6);
//		System.out.println(ht);
//		System.out.println(ht.get(20000));
//
//		ht.remove(20000);
//		System.out.println(ht);
//
//		System.out.println(ht.get(50));

	}
}
