package concurrent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.Callable;

import model.collections.MatchSet;
import model.element.Instance;
import model.element.Match;
import util.Util;

//用于读取匹配
public class MatchSetReader implements Callable<MatchSet> {
	private MatchSet matches;
	private File fmatch;
	private static String sep = Util.ll_sep;

	public MatchSetReader(File f) {
		matches=new MatchSet();
		fmatch=f;
	}
	@Override
	public MatchSet call() {
		// TODO Auto-generated method stub
		System.out.println("<MatchSetReader>Reading "+fmatch.getName()+"...");
		BufferedReader br=null;
		try {
			br = new BufferedReader(new FileReader(fmatch));
			while(true) {
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

				if (s1.length() == 0 || s2.length() == 0) continue;

				Instance e1=new Instance(s1,0),e2=new Instance(s2,1);//匹配的第一个实体视作源0，第二个视作源1
				matches.add(new Match(e1,e2));
			}
		} catch (FileNotFoundException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		} catch (IOException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}finally{
			try {
				if(br!=null)
					br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return matches;
	}
}
