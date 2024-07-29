package concurrent;


import model.collections.TripleSet;
import model.element.Instance;
import model.element.Property;
import model.element.Triple;
import util.Util;

import javax.security.auth.Subject;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

//import static util.Similarity.clean;

public class TripleSetReader implements Callable<Map<Instance,TripleSet>> {
    private int source;
    private File f = null;
    private Map<Instance, TripleSet> triples;
    private static String sep = Util.ll_sep;  // 统一分隔符

    public TripleSetReader(File f, int s) {
        this.f = f;
        source = s;
        triples = new HashMap<>();
    }

    @Override
    public Map<Instance, TripleSet> call() {
        BufferedReader br = null;
        ExecutorService es = Executors.newFixedThreadPool(41);
        try {
            System.out.println("<TripleSetReader "+source+">Reading file "+f.getName()+"...");
            br = new BufferedReader(new FileReader(f));
            while (true) {
                String line = br.readLine();
                if (line == null) break;
                es.execute(new Runnable() {
                    private String str = line;
                    @Override
                    public void run() {
                        String[] elements = line.split(sep);
                        if (elements.length != 3 ||
                                elements[0].length() <= 2 ||
                                elements[1].length() == 0 ||
                                elements[2].length() <= 2) {
                            // do nothing
                        }
                        else {
                            // 格式：<吴之章>---ll+++本名---ll+++<吴之章>
                            String s, o;
                            String p = elements[1];

                            if (Util.strip_bracket) {
                                s = elements[0].substring(1, elements[0].length()-1);
                                o = elements[2].substring(1, elements[2].length()-1);
                            }
                            else {
                                s = elements[0];
                                o = elements[2];
                            }

                            Instance sub = new Instance(s, source);
                            Property pro = new Property(p, source);
                            Instance obj = new Instance(o, source);
                            synchronized (triples) {
                                if (!triples.containsKey(sub)) {
                                    triples.put(sub, new TripleSet());
                                }
                                triples.get(sub).add(new Triple(sub, pro, obj));
                            }
                        }
                    }
                });
            }
            es.shutdown();
            while (!es.awaitTermination(5, TimeUnit.SECONDS));
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (br != null) br.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return triples;
    }

    public static void main(String[] args) {
        String s = "<wohaahha>---ll+++name---ll+++\"中国\"";
        String[] parts = s.split(sep);
        System.out.println(parts.length);
        for (String part : parts) {
            System.out.println(part + " " + part.length());
        }

    }
}
