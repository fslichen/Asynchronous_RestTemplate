package evolution.asynchronous.rest.template.count.down;

@SuppressWarnings("rawtypes")
public class Task {
	private String id;
	private String url;
	private Object request;
	private Class responseType;
	public Task(String id, String url, Object request, Class responseType) {
		this.id = id;
		this.url = url;
		this.request = request;
		this.responseType = responseType;
	}
	public String getId() {
		return id;
	}
	public Object getRequest() {
		return request;
	}
	public Class getResponseType() {
		return responseType;
	}
	public String getUrl() {
		return url;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setRequest(Object request) {
		this.request = request;
	}
	public void setResponseType(Class responseType) {
		this.responseType = responseType;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
