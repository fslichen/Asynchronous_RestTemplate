package evolution.asynchronous.rest.template.invoke.all;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

@SuppressWarnings("rawtypes")
public class AsynchronousRestTemplate {
	private List<Callable<Response>> tasks;
	private Map<String, Response> responses;
	private RestTemplate restTemplate;
	private ExecutorService executorService;
	private Integer individualTaskTimeoutInSeconds;
	
	Logger logger = LoggerFactory.getLogger(AsynchronousRestTemplate.class);

	public AsynchronousRestTemplate(int individualTaskTimeoutInSeconds) {
		tasks = new LinkedList<>();
		responses = new HashMap<>();
		restTemplate = new RestTemplate();
		this.individualTaskTimeoutInSeconds = individualTaskTimeoutInSeconds;
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	}
	
	@SuppressWarnings("unchecked")
	public void addTask(String id, String url, Object request, Class responseType) {
		Callable<Response> task = new Callable<Response>() {
			@Override
			public Response call() {
				Response response = new Response();
				response.setId(id);
				ExecutorService executorService = Executors.newSingleThreadExecutor(); 
				Future future = executorService.submit(new Callable() {
					@Override
					public Object call() throws Exception {
						return restTemplate.postForObject(url, request, responseType);
					}
				});
				try {
					response.setData(future.get(individualTaskTimeoutInSeconds, TimeUnit.SECONDS));
				} catch (Exception e) {
					logger.error("Execution of task " + id + " has exceeded the time out limit " + individualTaskTimeoutInSeconds + " second(s).");
				}
				return response;
			}
		};
		tasks.add(task);
	}
	
	public void post() throws Exception {
		List<Future<Response>> futures = executorService.invokeAll(tasks);
		for (Future<Response> future : futures) {
			Response response = future.get();
			responses.put(response.getId(), response);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(String id) {
		return (T) responses.get(id).getData();
	}
}
