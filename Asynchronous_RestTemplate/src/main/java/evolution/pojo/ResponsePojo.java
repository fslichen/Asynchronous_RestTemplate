package evolution.pojo;

public class ResponsePojo {
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ResponsePojo [message=" + message + "]";
	}
}
