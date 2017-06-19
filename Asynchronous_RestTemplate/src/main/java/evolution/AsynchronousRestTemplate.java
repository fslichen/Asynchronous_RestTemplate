package evolution;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SuppressWarnings("rawtypes")
public class AsynchronousRestTemplate {
	private List<Task> tasks;
	private CountDownLatch latch;
	private RestTemplate restTemplate;
	private Map<String, Future> futures;
	private ExecutorService executorService;
	
	public AsynchronousRestTemplate() {
		tasks = new LinkedList<>();
		futures = new HashMap<>();
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		restTemplate = new RestTemplate();
	}
	
	public AsynchronousRestTemplate(int timeoutInSeconds) {
		this();
		HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectTimeout(timeoutInSeconds);
        restTemplate.setRequestFactory(httpRequestFactory);
	}
	
	public void addTask(String id, String url, Object request, Class response) {
		tasks.add(new Task(id, url, request, response));
	}
	
	@SuppressWarnings("unchecked")
	public void post() throws InterruptedException {
		latch = new CountDownLatch(tasks.size());
		for (Task task : tasks) {
			Future future = executorService.submit(new Callable() {
				@Override
				public Object call() throws Exception {
					Object response = restTemplate.postForObject(task.getUrl(), task.getRequest(), task.getResponseType());
					latch.countDown();
					return response;
				}
			});
			futures.put(task.getId(), future);
		}
		latch.await();
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(String id) {
		try {
			return (T) futures.get(id).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			return null;
		}
	}
}
