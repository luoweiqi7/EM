package algorithm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import concurrent.MatchSetReader;
import concurrent.TripleSetReader;
import model.collections.MatchSet;
import model.collections.TripleSet;
import model.element.Instance;
import model.element.Mapping;
import model.element.Match;
import model.element.Property;
import model.element.Triple;
import util.Util;

// 本实验的一些设定
// 本程序不进行数据的清洗，清洗工作由用户负责（包括种子，三元组，同义词等）
// 数据遵守以下标准，可参考文件夹data/
//     种子文件格式	<entity1>ll_sep<entity2>
//     三元组文件格式	<吴之章>ll_sep本名ll_sep<吴之章>
//     同义词文件格式	<Burned>ll_sep<夜之屋>
//     说明：分隔符 ll_sep定义在Util；
//     说明：是否如果包含括号，可将Util.strip_bracket设为1将括号剥离
// 配置文件参考函数 init


public class Main {
	public static File f1;
	public static File f2;
	public static String seedFilePath;
	public static List<String> synonymsFilePaths;
	public static String destDir;
	public static double threshold;
	public static double divergenceThreshold;
	public static int startIteration=1;
	public static int minSupport=100;
	public static boolean doMineSeeds = false;

	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
		init(args[0]);  // 命令行参数为配置文件路径
		// 读取同义词表，重定向文件
		for (String s: synonymsFilePaths) {
			File synFile = new File(s);
			Mapping.readSynonyms(synFile);
		}

		System.out.println("<main>Start mining...");
		long startTime = System.currentTimeMillis();
		MatchingRuleMiner arm=new MatchingRuleMiner(f1,f2,destDir,threshold,divergenceThreshold,minSupport,
				startIteration, seedFilePath, doMineSeeds);
		long initTime = System.currentTimeMillis();
		arm.mine();
		long endTime = System.currentTimeMillis();
		System.out.println("Loading time:"+ (initTime - startTime)/1000 + "s, Mine time:" +(endTime-startTime) / 1000 + " s");
		System.out.println("<main>Done.");
	}

	public static void init(String configFilePath) throws IOException {
		String funcTag = "<init>";
		List<String> lines = readlines(configFilePath);

		// 第一行，目标运行文件夹
		destDir = lines.get(0);
		Util.Makedirs(destDir);

		// 第二行，源0三元组，格式e.g. <s>;;;;;;p;;;;;;<o>
		String source0 = lines.get(1);
		f1 = new File(source0);
		System.out.println(funcTag + "source0: " + source0);

		// 第三行，源1三元组，格式e.g. <s>;;;;;;p;;;;;;"o"
		String source1 = lines.get(2);
		f2 = new File(source1);
		System.out.println(funcTag + "source1: " + source1);

		// 第四行，candidates阈值 0.98
		String s = lines.get(3);
		threshold = Double.parseDouble(s);
		System.out.println(funcTag + "candidates threshold: " + threshold);

		// 第五行，关联规则最小支持度 minsup 5
		s = lines.get(4);
		AssociationRuleMiner.MINSUP = Integer.parseInt(s);
		System.out.println(funcTag + "AssociationRuleMiner.MINSUP: " + AssociationRuleMiner.MINSUP);

		// 第六行，关联规则最小置信度 min conf 0.1
		s = lines.get(5);
		AssociationRuleMiner.MINCONF=Double.parseDouble(s);
		System.out.println(funcTag + "AssociationRuleMiner.MINCONF: " + AssociationRuleMiner.MINCONF);

		// 第七行，关联规则divergence Threshold divergenceThreshold=0.93
		s = lines.get(6);
		divergenceThreshold = Double.parseDouble(s);
		System.out.println(funcTag + "divergenceThreshold: " + divergenceThreshold);

		// 第八行，是否进行剥离括号的操作
		s = lines.get(7);
		Util.strip_bracket = s.equals("1");
		System.out.println(funcTag + "data strip_bracket: " + Util.strip_bracket + "(" + s + ")");

		// 第九行，是否执行种子挖掘程序
		s = lines.get(8);
		doMineSeeds = s.equals("1");
		System.out.println(funcTag + "do mine seeds: " + doMineSeeds + "(" + s + ")");

		// 第十行，种子文件，可以为空
		if (lines.size() <= 9 || lines.get(9).equals("")) {
			seedFilePath = "";
			System.out.println(funcTag + "no seeds file");
		}
		else {
			seedFilePath = lines.get(9);
			System.out.println(funcTag + "seeds path: " + seedFilePath);
		}

		// 第十一行，同义词文件，可以为空
		if (lines.size() <= 10 || lines.get(10).equals("")) {
			synonymsFilePaths = new ArrayList<>();
			System.out.println(funcTag + "no synonyms file");
		}
		else {
			String line = lines.get(10);
			synonymsFilePaths = new ArrayList<>();
			String[] names = line.split(" ");
			synonymsFilePaths.addAll(Arrays.asList(names));
			for (String ss: synonymsFilePaths) {
				System.out.println(funcTag + "synonyms: " + ss);
			}
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

}
