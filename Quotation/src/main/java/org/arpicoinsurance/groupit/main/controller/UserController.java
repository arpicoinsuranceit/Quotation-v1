package org.arpicoinsurance.groupit.main.controller;

import java.util.Date;
import java.util.Map;

import org.arpicoinsurance.groupit.main.model.Logs;
import org.arpicoinsurance.groupit.main.service.LogService;
import org.arpicoinsurance.groupit.main.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

	@Autowired
	private LogService logService;

	@PostMapping("/uploadProf/{userCode}")
	public ResponseEntity<Object> saveUserProfilePicture(@RequestParam("image") MultipartFile image,
			@PathVariable String userCode, RedirectAttributes redirectAttributes) {
		// System.out.println("called");
		// System.out.println(userCode);
		String resp = "";
		if (image.isEmpty()) {

			return new ResponseEntity<Object>("noFile", HttpStatus.OK);
		} else {
			try {
				resp = userService.saveUserProfilePic(image, userCode);
				return new ResponseEntity<Object>(resp, HttpStatus.OK);
			} catch (Exception e) {
				Logs logs = new Logs();
				logs.setData("Error : " + e.getMessage() + ",\n Parameters : userCode : " + userCode
						+ " image  IsEmpty : " + image.isEmpty());
				logs.setDate(new Date());
				logs.setHeading("Error");
				logs.setOperation("saveUserProfilePicture : UserController");
				try {
					logService.saveLog(logs);
				} catch (Exception e1) {
					System.out.println("... Error Message for Operation ...");
					e.printStackTrace();
					System.out.println("... Error Message for save log ...");
					e1.printStackTrace();
				}
				return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

	}

	@RequestMapping(path = "/downloadProfPic/{id}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Object> getImage(@PathVariable String id) {

		Map<String, String> image = null;
		try {
			image = userService.getProfileImage(id);
			return new ResponseEntity<Object>(image, HttpStatus.OK);
		} catch (Exception e) {
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage() + ",\n Parameters  " + id);
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("getImage : UserController");
			try {
				logService.saveLog(logs);
			} catch (Exception e1) {
				System.out.println("... Error Message for Operation ...");
				e.printStackTrace();
				System.out.println("... Error Message for save log ...");
				e1.printStackTrace();
			}
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(path = "/getprofilePictures", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Object> getNotApprovedUsers() {

		try {
			return new ResponseEntity<Object>(userService.getNotApprovedUserProfilePic(), HttpStatus.OK);
		} catch (Exception e) {
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage());
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("getNotApprovedUsers : UserController");
			try {
				logService.saveLog(logs);
			} catch (Exception e1) {
				System.out.println("... Error Message for Operation ...");
				e.printStackTrace();
				System.out.println("... Error Message for save log ...");
				e1.printStackTrace();
			}
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// return null;
	}

	@RequestMapping(path = "/loadPendingProf/{id}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Object> getPendingImage(@PathVariable String id) {
		try {
			return new ResponseEntity<Object>(userService.getPendingImage(id), HttpStatus.OK);
		} catch (Exception e) {
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage() + ",\n id : " + id);
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("getPendingImage : UserController");
			try {
				logService.saveLog(logs);
			} catch (Exception e1) {
				System.out.println("... Error Message for Operation ...");
				e.printStackTrace();
				System.out.println("... Error Message for save log ...");
				e1.printStackTrace();
			}
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// return null;
	}

	@RequestMapping(value = "/approveUserProfile/{id}", method = RequestMethod.GET)
	public ResponseEntity<Object> approveImage(@PathVariable String id) {
		// System.out.println(id);
		try {
			return new ResponseEntity<Object>(userService.approveImage(id), HttpStatus.OK);
		} catch (Exception e) {
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage() + ",\n id : " + id);
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("approveImage : UserController");
			try {
				logService.saveLog(logs);
			} catch (Exception e1) {
				System.out.println("... Error Message for Operation ...");
				e.printStackTrace();
				System.out.println("... Error Message for save log ...");
				e1.printStackTrace();
			}
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// return null;
	}

	@RequestMapping(value = "/rejectUserProfile/{id}", method = RequestMethod.GET)
	public ResponseEntity<Object> rejectImage(@PathVariable String id) {
		// System.out.println(id);
		try {
			return new ResponseEntity<Object>(userService.rejectImage(id), HttpStatus.OK);
		} catch (Exception e) {
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage() + ",\n id : " + id);
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("rejectImage : UserController");
			try {
				logService.saveLog(logs);
			} catch (Exception e1) {
				System.out.println("... Error Message for Operation ...");
				e.printStackTrace();
				System.out.println("... Error Message for save log ...");
				e1.printStackTrace();
			}
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// return null;
	}

}
