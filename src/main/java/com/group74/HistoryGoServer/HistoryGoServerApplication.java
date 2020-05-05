package com.group74.HistoryGoServer;

import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//d
@SpringBootApplication
@RestController
public class HistoryGoServerApplication extends SpringBootServletInitializer {
	
	static JSONObject[] JSONArr;
	static final Requester reqTEST = new Requester();

	
	public static void main(String[] args) {
		JSONObject obj = new JSONObject();
		reqTEST.getPlaces(59.328, 18.096);
		obj.put("JSON", "OBJ");
		JSONArr = new JSONObject[] {obj};
		SpringApplication.run(HistoryGoServerApplication.class, args);
	}

	@GetMapping("/hello")
	public JSONObject hello(@RequestParam(value ="name", defaultValue ="World")String name) {
		return JSONArr[0];

	}
	
	//URL FORMAT = http://localhost:8080/getPlace?lats=56&lats=46
	@RequestMapping(value="/getPlace", method = RequestMethod.GET)
	public String handleFileUpload(Model model, @RequestParam(value = "lats", required = true)List<String> lats) throws NumberFormatException{
		try {
			return reqTEST.getPlaces(Double.parseDouble(lats.get(0)), Double.parseDouble(lats.get(1)));
		}catch(NumberFormatException e) {}
		return null;
	}
}


/*
	//LADDA UPP
 * @PostMapping("/")
	public String handleFileUpload(@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) {

		storageService.store(file);
		redirectAttributes.addFlashAttribute("message",
				"You successfully uploaded " + file.getOriginalFilename() + "!");

		return "redirect:/";
	}
	
	//LADDA NER
	@GetMapping("/")
	public String listUploadedFiles(Model model) throws IOException {

		model.addAttribute("files", storageService.loadAll().map(
				path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
						"serveFile", path.getFileName().toString()).build().toUri().toString())
				.collect(Collectors.toList()));

		return "uploadForm";
	}
*/