package model.element;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import util.Util;

//import static util.Similarity.clean;

//用于索引字符串
public class Mapping {
	private static Map<String,Integer> smp=new TreeMap<String,Integer>();	//(字符串->id)
	private static ArrayList<String> simp=new ArrayList<String>();	//(id->字符串)
	private static ArrayList<Integer> father=new ArrayList<Integer>();	//并查集父结点，属于同一个集合的字符串同义
//	private static Map<Integer, String> ori = new TreeMap<>(); // smp 中存储的都是clean之后的String，这里保留了原始字符串

	static {
		simp.add("");
		smp.put("", 0);
		father.add(0);
	}
	//字符串s的索引
	public synchronized static Integer getIndex(String s) {
		if(smp.containsKey(s))
			return smp.get(s);
		else {
			simp.add(s);
			smp.put(s, simp.size()-1);
			father.add(simp.size()-1);
			return simp.size()-1;
		}
	}

	//字符串s的索引，同时记录clean和原始两个字符串
//	public synchronized static Integer  getIndexWithOri(String s_clean, String s) {
//		if (smp.containsKey(s_clean))
//			return smp.get(s_clean);
//		else {
//			simp.add(s_clean);
//			smp.put(s_clean, simp.size()-1);
//			father.add(simp.size()-1);
//			ori.put(simp.size()-1, s);
//			return simp.size() - 1;
//		}
//	}

	//索引i的字符串
	public static String getString(int i) {
		return simp.get(i);
	}
	//索引i的原始字符串
//	public static String getOriginString(int i) { return ori.get(i); }
	//并查集-求u所属集合
	public static synchronized int findSet(int u){
		if(father.get(u)==u) return u;
		father.set(u, findSet(father.get(u)));
		return father.get(u);
	}
	//并查集-合并u，v集合
	public static synchronized void unionSet(int u,int v){
		u=findSet(u);
		v=findSet(v);
		father.set(u, v);
	}
	//读取同义词表
	public static void readSynonyms(File f) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader br=new BufferedReader(new FileReader(f));
		String sep = Util.ll_sep;

		while(true){
			String s=br.readLine();
			if(s==null) break;
			String[] e=s.split(sep);

			if (e.length != 2) continue;

			String s1, s2;

			if (Util.strip_bracket) {
				s1 = e[0].substring(1, e[0].length()-1);
				s2 = e[1].substring(1, e[1].length()-1);
			}
			else {
				s1 = e[0];
				s2 = e[1];
			}
			unionSet(getIndex(s1), getIndex(s2));

//			unionSet(getIndex(clean(e[0])), getIndex(clean(e[1])));
			//System.out.println(e[0]);
			//抽取标签名
//			String v1=Util.getLabelname(e[0]),v2=Util.getLabelname(e[1]);
//			if(v1==null||v1.equals(""))continue;
//			if(v2==null||v2.equals(""))continue;
			//v1、v2同义，合并三个源中v1、v2的集合

			//去除链接前缀
			// before
//			unionSet(getIndex("<http://zhishi.me/zhwiki/resource/"+v1+">"),getIndex("<http://zhishi.me/zhwiki/resource/"+v2+">"));
//			unionSet(getIndex("<http://zhishi.me/hudongbaike/resource/"+v1+">"),getIndex("<http://zhishi.me/hudongbaike/resource/"+v2+">"));
//			unionSet(getIndex("<http://zhishi.me/baidubaike/resource/"+v1+">"),getIndex("<http://zhishi.me/baidubaike/resource/"+v2+">"));
//			unionSet();
		}
		br.close();
	}
}
