package org.arpicoinsurance.groupit.main.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.arpicoinsurance.groupit.main.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@CrossOrigin(origins = "*")
public class UserController {

	@Autowired
	private UsersService userService;
	
	@PostMapping("/uploadProf/{userCode}")
	public String saveUserProfilePicture(@RequestParam("image") MultipartFile image, @PathVariable String userCode, RedirectAttributes redirectAttributes) {
		System.out.println("called");
		System.out.println(userCode);
		String resp = "";
		if (image.isEmpty()) {
			return "noFile";
		}else {
			try {
				resp=userService.saveUserProfilePic(image, userCode);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return resp;
	}
	
	@RequestMapping(path = "/downloadProfPic/{id}", method = RequestMethod.GET)
	public @ResponseBody Map<String, String> getImage(@PathVariable String id) throws IOException {

		File file = new File("F://temp//"+id);
		
		if(!file.exists()) {
			file = new File("F://temp//dummy");
		}

		
		
		String encodeImage = Base64.getEncoder().withoutPadding().encodeToString(Files.readAllBytes(file.toPath()));

		Map<String, String> jsonMap = new HashMap<>();

		jsonMap.put("content", encodeImage);

		return jsonMap;
	}
	
}
