package test;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.collections.TripleSet;
import model.element.Instance;
import model.element.Property;
import model.element.Triple;

public class Test {
	public static void main(String[] args) {
		Pattern r=Pattern.compile(".*/resource/(.*)$");
		Matcher m=r.matcher("<http://zhishi.me/hudongbaike/resource/%E8%90%A8%E5%8D%A1%E4%BB%80%E7%BB%B4%E5%88%A9>");
		System.out.println(m.find()+m.group(1));

		List<Integer> li = new LinkedList<>();
		li.add(1);
		li.add(2);
        System.out.println(li);

		String s = "b----ll+++a";
		String[] spl = s.split("---ll\\+\\+\\+");
		for (int i = 0; i < spl.length; i++) {
			System.out.println(spl[i]);
		}

	}
}
