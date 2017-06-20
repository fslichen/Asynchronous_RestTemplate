package evolution.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import evolution.pojo.RequestPojo;
import evolution.pojo.ResponsePojo;

@RestController
public class AnyController {
	@PostMapping("/post")
	public ResponsePojo post(@RequestBody RequestPojo request) throws InterruptedException {
		Thread.sleep(1000);
		ResponsePojo responsePojo = new ResponsePojo();
		responsePojo.setMessage(request.getName());
		return responsePojo;
	}
	
	@PostMapping("/slow/post")
	public ResponsePojo slowPost(@RequestBody RequestPojo request) throws InterruptedException {
		Thread.sleep(Integer.MAX_VALUE);
		ResponsePojo responsePojo = new ResponsePojo();
		responsePojo.setMessage(request.getName());
		return responsePojo;
	}
}
