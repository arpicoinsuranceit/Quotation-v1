package org.arpicoinsurance.groupit.main.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.arpicoinsurance.groupit.main.dao.UserProfilePictureDao;
import org.arpicoinsurance.groupit.main.dao.UsersDao;
import org.arpicoinsurance.groupit.main.model.Branch;
import org.arpicoinsurance.groupit.main.model.UserProfilePicture;
import org.arpicoinsurance.groupit.main.model.Users;
import org.arpicoinsurance.groupit.main.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class UsersServiceImpl implements UsersService {

	private static String UPLOADED_FOLDER = "F://temp//";

	@Autowired
	private UsersDao usersDao;

	@Autowired
	private UserProfilePictureDao userProfilePictureDao;

	@Override
	public boolean saveUser(Users users) throws Exception {
		if (usersDao.save(users) != null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean updateUser(Users users) throws Exception {
		if (usersDao.save(users) != null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteUser(Integer id) throws Exception {
		if (usersDao.deleteOne(id) != null) {
			return true;
		}
		return false;
	}

	@Override
	public Users getUser(Integer id) throws Exception {
		return usersDao.findOne(id);
	}

	@Override
	public List<Users> getAllUsers() throws Exception {
		List<Users> users = new ArrayList<>();
		usersDao.findAll().forEach(users::add);
		return users;
	}

	@Override
	public Users getUserByLoginId(Integer id) throws Exception {
		return usersDao.findByLoginId(id);
	}

	@Override
	public String saveUserProfilePic(MultipartFile image, String userCode) throws Exception {
		try {

			Users user = usersDao.findOneByUserCode(userCode);
			// Get the file and save it somewhere
			byte[] bytes = image.getBytes();

			String fileName = UPLOADED_FOLDER + "pending_" + userCode;

			File saveImage = new File(fileName);

			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(saveImage));

			stream.write(bytes);

			UserProfilePicture userProfilePicture = userProfilePictureDao.findByUsersAndApprove(user, false);

			if (userProfilePicture == null) {
				userProfilePicture = new UserProfilePicture();
				userProfilePicture.setUsers(user);
				userProfilePicture.setApprove(false);
			}

			userProfilePicture.setUrl(fileName);
			userProfilePicture.setUploadDate(new Date());
			userProfilePicture.setStatus("Pending");

			userProfilePictureDao.save(userProfilePicture);

			stream.close();

			return "Success";

		} catch (IOException e) {
			e.printStackTrace();
		}

		return "Fail";
	}

	@Override
	public Map<String, String> getPendingImage(String userCode) throws Exception {

		File file = new File(userProfilePictureDao.findOne(Integer.parseInt(userCode)).getUrl());

		Map<String, String> jsonMap = new HashMap<>();

		if (!file.exists()) {
			jsonMap.put("content", "Error");
		}

		String encodeImage = Base64.getEncoder().withoutPadding().encodeToString(Files.readAllBytes(file.toPath()));

		jsonMap.put("content", encodeImage);

		return jsonMap;
	}

	@Override
	public ArrayList<UserProfilePicture> getNotApprovedUserProfilePic() throws Exception {

		ArrayList<UserProfilePicture> profilePictureLsit = userProfilePictureDao.findAllByApprove(false);
		for (UserProfilePicture userProfilePicture : profilePictureLsit) {
			Branch branch = userProfilePicture.getUsers().getBranch();
			userProfilePicture.getUsers().setBranch(branch);
		}
		return profilePictureLsit;
	}

	@Override
	public ArrayList<UserProfilePicture> approveImage(String id) throws Exception {

		ArrayList<UserProfilePicture> profilePictureLsit = null;

		UserProfilePicture userProfilePicture = userProfilePictureDao.findOne(Integer.parseInt(id));
		if (userProfilePicture != null) {
			UserProfilePicture userProfilePictureCurrent = userProfilePictureDao
					.findByUsersAndApprove(userProfilePicture.getUsers(), true);
			if (userProfilePictureCurrent != null) {
				File currentFile = new File(UPLOADED_FOLDER + userProfilePictureCurrent.getUsers().getUserCode());
				if (currentFile.exists()) {
					File dest = new File(UPLOADED_FOLDER + "old_" + userProfilePicture.getUsers().getUserCode());
					currentFile.renameTo(dest);
					userProfilePictureCurrent.setApprove(false);
					userProfilePictureCurrent.setStatus("Old");
				}
			}
			File file = new File(userProfilePicture.getUrl());
			if (file.exists()) {
				File dest = new File(UPLOADED_FOLDER + userProfilePicture.getUsers().getUserCode());
				file.renameTo(dest);
				userProfilePicture.setApprove(true);
				userProfilePicture.setApprovedBy(usersDao.findOneByUserCode("kavinda"));
				userProfilePicture.setApprovedDate(new Date());
				userProfilePicture.setStatus("Approved");

			}

			profilePictureLsit = getNotApprovedUserProfilePic();

		} else {
			return profilePictureLsit;
		}

		if (userProfilePictureDao.save(userProfilePicture) != null) {
			return profilePictureLsit;
		}
		return profilePictureLsit;
	}

}
