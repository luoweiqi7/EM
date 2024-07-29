//package util;
//
//import model.element.Instance;
//
//import java.io.IOException;
//import java.util.HashSet;
//import java.util.Set;
//
//import static util.Util.getLabelname;
//import static util.Util.unicodeStr2reprStr;
//
//public class Similarity {
////    public static String clean(String s) {
////        String res = s.replaceAll("\\s*", "");
////        return res.replaceAll("\\p{P}", "");
////    }
//
//    private static int compare(String str, String target) {
//        int d[][]; // 矩阵
//        int n = str.length();
//        int m = target.length();
//        int i; // 遍历str的
//        int j; // 遍历target的
//        char ch1; // str的
//        char ch2; // target的
//        int temp; // 记录相同字符,在某个矩阵位置值的增量,不是0就是1
//
//        if (n == 0) {
//            return m;
//        }
//
//        if (m == 0) {
//            return n;
//        }
//
//        d = new int[n + 1][m + 1];
//
//        for (i = 0; i <= n; i++) { // 初始化第一列
//            d[i][0] = i;
//        }
//
//        for (j = 0; j <= m; j++) { // 初始化第一行
//            d[0][j] = j;
//        }
//
//        for (i = 1; i <= n; i++) { // 遍历str
//            ch1 = str.charAt(i - 1);
//            // 去匹配target
//            for (j = 1; j <= m; j++) {
//                ch2 = target.charAt(j - 1);
//                if (ch1 == ch2) {
//                    temp = 0;
//                } else {
//                    temp = 1;
//                }
//
//                // 左边+1,上边+1, 左上角+temp取最小
//                d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + temp);
//            }
//        }
//
//        return d[n][m];
//    }
//
//    private static int min(int one, int two, int three) {
//        return (one = one < two ? one : two) < three ? one : three;
//    }
//
//    public static float levenshteinSimilarity(String str, String target) {
//        return 1 - (float) compare(str, target) / Math.max(str.length(), target.length());
//
//    }
//
//    // 如果是一个实体，
//    // <http://zhishi.me/baidubaike/resource/%E6%94%BF%E7%AD%96>
//    // URL解码
//    // <http://zhishi.me/baidubaike/resource/政策>
//    // 返回其字面值
//    // 政策
//    //
//    // 如果是一个字面值，
//    // 68u82F1u5C3A
//    // 恢复正常编码
//    // 68\u82F1\u5C3A
//    // 返回其解码值
//    // 68英尺
//    public static String literal(Instance entity) {
//        String s = entity.getValue();
//        String res = "";
//        if (s.length() == 0) {
//            // null statement
//        }
//        else if (s.charAt(0) == '<') {
//            try {
//                res = java.net.URLDecoder.decode(getLabelname(s), "UTF-8");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        else {
//            try {
//                res = unicodeStr2reprStr(objectLiteralToUTF8(s));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return res;
//    }
//
//    private static String objectLiteralToUTF8(String s) {
//        String utf8code = "0123456789abcdefABCEDF";
//        StringBuffer stringBuffer = new StringBuffer();
//        for (int i = 0; i < s.length(); i++) {
//            if (s.charAt(i) == 'u') {
//                if (i+4 >= s.length()) {
//                    stringBuffer.append('u');
//                    continue;
//                }
//                else {
//                    boolean isutf8 = true;
//                    for (int j = 1; j <= 4 ; j++) {
//                        if (utf8code.indexOf(s.charAt(i+j)) != -1) {
//                            // null-statement
//                        }
//                        else {
//                            isutf8 = false;
//                        }
//                    }
//                    if (isutf8) {
//                        stringBuffer.append("\\u");
//                        stringBuffer.append(s.substring(i+1, i+5));
//                        i += 4;
//                        continue;
//                    }
//                }
//            }
//            stringBuffer.append(s.charAt(i));
//        }
//        return stringBuffer.toString();
//    }
//
//    public static double jaccardSimilarity(Set<Character> words1, Set<Character> words2) {
//        Set<Character> intersectionSet = new HashSet<>();
//        Set<Character> unionSet = new HashSet<>();
//        intersectionSet.addAll(words1);
//        intersectionSet.retainAll(words2);
//        unionSet.addAll(words1);
//        unionSet.addAll(words2);
//        return 1.0 * intersectionSet.size() / unionSet.size();
//    }
//
//    public static Set<Character> stringToSet(String s) {
//        Set<Character> set = new HashSet<>();
//        for (char ch : s.toCharArray()) {
//            set.add(ch);
//        }
//        return set;
//    }
//
//    public static void main(String[] args) {
//        String str = "日本国东??？？       京  (【) 都";
////        str = clean(str);
//        String target = "日本东京都";
//        System.out.println(str);
//        for (char ch: target.toCharArray()) {
//            System.out.println(ch);
//        }
////        System.out.println(stringToSet(target));
//        System.out.println("similarityRatio=" + levenshteinSimilarity(str, target));
//        System.out.println("jaccard=" + jaccardSimilarity(stringToSet(str), stringToSet(target)));
//
//    }
//
//}
