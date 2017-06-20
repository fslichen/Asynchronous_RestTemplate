package evolution;

import evolution.asynchronous.rest.template.invoke.all.AsynchronousRestTemplate;
import evolution.pojo.RequestPojo;
import evolution.pojo.ResponsePojo;

public class InvokeAllAsynchronousRestTemplateTest {
	// The size of the thread pool is 4. Due to the concurrency mechanism, 
	// you can assume that there are 50 / 4 = 12.5 blocking threads,
	// and 50 / 4 = 12.5 non-blocking threads in single thread context.
	// Each blocking thread takes 5 seconds to expire,
	// therefore the time spent on blocking threads is 5 * 12.5 = 62.5.
	// Each non-blocking thread takes 1 second to complete,
	// Therefore the time spent on non-blocking threads is 1 * 12.5 = 12.5.
	// The total amount of time is at least 62.5 + 12.5 = 75.
	// The actual time consumed is 77 due to JVM's task scheduling.
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
