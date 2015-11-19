
class Weibo {
	String id;
	Date createDate;
	String content;
	//String fullContent;
	@Override
	public String toString(){
		"ID: ${id}\nDate: ${createDate}\nContent: ${content}";
	}
	@Override
	public int hashCode(){
		return id.hashCode()+createDate.hashCode()+content.hashCode();
	}
}
