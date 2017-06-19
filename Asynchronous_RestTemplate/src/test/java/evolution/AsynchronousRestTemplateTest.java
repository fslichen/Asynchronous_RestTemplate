package evolution;

import org.junit.Test;

import evolution.pojo.RequestPojo;
import evolution.pojo.ResponsePojo;

public class AsynchronousRestTemplateTest {
	@Test
	public void test() throws InterruptedException {
		AsynchronousRestTemplate art = new AsynchronousRestTemplate(10000);
		for (int i = 0; i < 10; i++) {
			RequestPojo request = new RequestPojo();
			request.setName("Chen");
			art.addTask(i + "", "http://localhost:8080/post", request, ResponsePojo.class);
		}
		art.post();
		ResponsePojo responsePojo0 = art.get("0");
		System.out.println(responsePojo0);
		ResponsePojo responsePojo9 = art.get("9");
		System.out.println(responsePojo9);
	}
}
