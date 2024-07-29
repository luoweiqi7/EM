package model.element;

public class Pair<T1,T2> {
	public final T1 first;
	public final T2 second;
	public Pair(T1 f,T2 s) {
		first=f;
		second=s;
	}
	public Pair() {
		// TODO Auto-generated constructor stub
		first=null;
		second=null;
	}
}
