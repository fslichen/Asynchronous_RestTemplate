package evolution.asynchronous.rest.template.invoke.all;

public class Response {
	private String id;
	private Object data;
	public Object getData() {
		return data;
	}
	public String getId() {
		return id;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public void setId(String id) {
		this.id = id;
	}
}
