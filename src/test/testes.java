package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class testes {
	static Map<Integer,Set<Integer>> m=new TreeMap<Integer,Set<Integer>>();

	public static List<String> readlines(String filepath) throws IOException {
		File file = new File(filepath);
		BufferedReader br = new BufferedReader(new FileReader(file));
		List<String> lines = new ArrayList<>();

		String line;
		while ((line = br.readLine()) != null) {
			lines.add(line);
		}

		return lines;
	}

	public static void main(String[] args) throws IOException {
		m.put(1,new TreeSet<Integer>());
		m.get(1).add(2);
		for(Integer i:m.get(1)) {
			System.out.println(i);
		}

		List<String> lines = readlines("configs/hudongbaike_baidubaike_config.txt");
		for (String line : lines) {
			if (line.equals(""))
				System.out.println("empty");

			System.out.print(line + "\n");
		}

		System.out.println("size " + lines.size());

	}
}
