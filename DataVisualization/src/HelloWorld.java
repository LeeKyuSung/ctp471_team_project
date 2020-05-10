
public class HelloWorld {

	public static void main(String[] args) {
		String[] tmp = "a|b|c".split("\\|");
		for (int i=0; i<tmp.length; i++)
			System.out.println(tmp[i]);
		
	}
}
