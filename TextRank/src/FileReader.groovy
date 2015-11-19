import java.text.SimpleDateFormat


class FileReader {
	public static void readFile(File file){
		String name=file.getName();
		println name;
		Date d = new SimpleDateFormat("yyyyMMddhh").parse(name);
		ArrayList<String> items=file.readLines();
		
	}
	public static void main(String[] args){
		readFile(new File("C:\\Users\\user\\sorted\\2012010816"));
	}
}
