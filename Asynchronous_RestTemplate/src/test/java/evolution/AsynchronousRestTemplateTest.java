package evolution;

import evolution.pojo.RequestPojo;
import evolution.pojo.ResponsePojo;

public class AsynchronousRestTemplateTest {
	public static void main(String[] args) throws Exception {
		AsynchronousRestTemplate art = new AsynchronousRestTemplate(5);
		for (int i = 0; i < 50; i++) {
			RequestPojo request = new RequestPojo();
			request.setName("Anna");
			art.addTask(i + "", "http://localhost:8080/slow/post", request, ResponsePojo.class);
		}
		for (int i = 50; i < 100; i++) {
			RequestPojo request = new RequestPojo();
			request.setName("Elsa");
			art.addTask(i + "", "http://localhost:8080/post", request, ResponsePojo.class);
		}
		art.post();
		for (int i = 0; i < 100; i++) {
			ResponsePojo responsePojo0 = art.get(i + "");
			System.out.println(responsePojo0);
		}
	}
}
