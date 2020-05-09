package tmp_test;

public class SimpleTest {

	public static void main(String[] args) {
		
		String a = "asdf\"asdf";
		System.out.println(a);
		System.out.println(a.replace("\"", "\\\""));
	}
}
