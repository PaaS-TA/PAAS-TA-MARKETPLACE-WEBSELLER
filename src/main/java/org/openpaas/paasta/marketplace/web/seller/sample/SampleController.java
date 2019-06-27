package org.openpaas.paasta.marketplace.web.seller.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SampleController {

	@Autowired
	private SampleService sampleSvc;

	@GetMapping("/product/upload")
	public String writer() throws Exception {
		return "/product/upload";
	}

	@PostMapping("/db/upload")
	public String fileUpload(@ModelAttribute UploadFile upload) {
//		sampleSvc.fileUpload(upload);
		return "redirect:/product/upload";
	}

}
