package test;

public class Test {
	public static void main(String[] args) {
		try {
			byte [] a = new byte[]{-58, 105, 111, 122, -69, 34, -94};
			String b = new String(a,"UTF-8");
			System.out.println( b);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
}
