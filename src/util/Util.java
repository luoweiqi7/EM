package util;


import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static jdk.nashorn.internal.objects.Global.print;

public class Util{
	public static String ll_sep = ";;;;;;";
	public static boolean strip_bracket = true;

//	private static Pattern resourcePattern =Pattern.compile(".*/resource/(.+)>$");
//	private static Pattern propertyPattern = Pattern.compile(".*/property/(.+)>$");
	//交换
	public static void swap(Object o1,Object o2) {
		Object tmp=o1;
		o1=o2;
		o2=tmp;
	}

	//按字典序比较集合大小关系
	public static <T extends Comparable<T>> int compareSet(Set<T> s1,Set<T> s2){
		Iterator<T> ite1=s1.iterator();
		Iterator<T> ite2=s2.iterator();
		while(ite1.hasNext()&&ite2.hasNext()) {
			int flag=ite1.next().compareTo(ite2.next());
			if(flag!=0) return flag;
		}
		return s1.size()-s2.size();
	}
	//a和b的最长公共前缀
	public static String LongestCommonPrefix(String a,String b) {
		String res = new String();
		for(int i=0;i<a.length()&&i<b.length();i++) {
			if(a.charAt(i)==b.charAt(i))
				res+=a.charAt(i);
			else return res;
		}
		return res;
	}

	// 当文件夹不存在时创建，仅当无法创建文件夹时，返回false
	public static boolean Makedirs(String dirname) {
		File dir = new File(dirname);
		if (dir.exists()) {
			System.out.println(dirname + " already exists.");
			return true;
		}

		if (dir.mkdirs()) {
			System.out.println(dirname + " create success!");
			return true;
		}
		else {
			System.out.println(dirname + " create failed!");
			return false;
		}
	}

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

//	//标签名
//	public static String getLabelname(String s){
//		Matcher m=resourcePattern.matcher(s);
//		if(!m.find()) return "";
//		String val=m.group(1);
//		return val;
//	}
//	//属性标签
//	public static String getPropertyLabelname(String s) {
//		Matcher m = propertyPattern.matcher(s);
//		if (!m.find()) return "";
//		String val=m.group(1);
//		return val;
//	}
//
//	//把一个utf-8编码串转成其真正表示的字符串
//	public static String unicodeStr2reprStr(String s) throws IOException {
//		StringReader sr = new StringReader(s);
//		UnicodeUnescapeReader uur = new UnicodeUnescapeReader(sr);
//
//		StringBuffer buf = new StringBuffer();
//		for(int c = uur.read(); c != -1; c = uur.read())
//		{
//			buf.append((char)c);
//		}
//		return buf.toString();
//	}

	public static void main(String[] args) {
		File f = new File("");
		System.out.println(f.exists());

		List<String> s = new ArrayList<>();
		System.out.println(s.size());

	}
}
