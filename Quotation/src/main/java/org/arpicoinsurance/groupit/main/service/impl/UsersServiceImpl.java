package org.arpicoinsurance.groupit.main.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
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

	private static String UPLOADED_FOLDER = "D:/APINProfilePic/";

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

			// UserProfilePicture userProfilePicture =
			// userProfilePictureDao.findByUsersAndApprove(user, false);

			UserProfilePicture userProfilePicture = new UserProfilePicture();
			/*
			 * if (userProfilePicture == null) { userProfilePicture = new
			 * UserProfilePicture(); userProfilePicture.setUsers(user);
			 * userProfilePicture.setApprove(false); }
			 */

			userProfilePicture = new UserProfilePicture();
			userProfilePicture.setUsers(user);
			userProfilePicture.setApprove(false);
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

		ArrayList<UserProfilePicture> profilePictureLsit = userProfilePictureDao.findAllByApproveAndStatus(false,
				"Pending");
		for (UserProfilePicture userProfilePicture : profilePictureLsit) {
			Branch branch = userProfilePicture.getUsers().getBranch();
			userProfilePicture.getUsers().setBranch(branch);
		}
		return profilePictureLsit;
	}

	@Override
	public ArrayList<UserProfilePicture> rejectImage(String id) throws Exception {

		ArrayList<UserProfilePicture> profilePictureLsit = null;

		UserProfilePicture userProfilePicturePending = userProfilePictureDao.findOne(Integer.parseInt(id));
		UserProfilePicture userProfilePictureCurrent = null;

		if (userProfilePicturePending != null) {
			userProfilePictureCurrent = userProfilePictureDao
					.findByUsersAndApprove(userProfilePicturePending.getUsers(), true);
			
			File file = new File(userProfilePicturePending.getUrl());
			if (file.exists()) {
				ArrayList<UserProfilePicture> rejectList = userProfilePictureDao.findAllByUsersAndStatus(userProfilePicturePending.getUsers(), "Reject");
				File dest = new File(UPLOADED_FOLDER + "Reject_"+ userProfilePicturePending.getUsers().getUserCode()+"_"+(rejectList.size()+1));
				file.renameTo(dest);
				userProfilePicturePending.setApprove(false);
				userProfilePicturePending.setUrl(UPLOADED_FOLDER + "Reject_"+ userProfilePicturePending.getUsers().getUserCode()+"_"+(rejectList.size()+1));
				userProfilePicturePending.setApprovedBy(usersDao.findOneByUserCode("kavinda"));
				userProfilePicturePending.setApprovedDate(new Date());
				userProfilePicturePending.setStatus("Reject");

			}

			profilePictureLsit = getNotApprovedUserProfilePic();

		} else {
			return profilePictureLsit;
		}

		if (userProfilePictureDao.save(userProfilePicturePending) != null) {
			if (userProfilePictureCurrent != null) {
				if (userProfilePictureDao.save(userProfilePictureCurrent) != null) {
					return profilePictureLsit;
				}
			}

		}
		return profilePictureLsit;
	}
	
	
	@Override
	public ArrayList<UserProfilePicture> approveImage(String id) throws Exception {

		ArrayList<UserProfilePicture> profilePictureLsit = null;

		UserProfilePicture userProfilePicturePending = userProfilePictureDao.findOne(Integer.parseInt(id));
		UserProfilePicture userProfilePictureCurrent = null;

		if (userProfilePicturePending != null) {
			userProfilePictureCurrent = userProfilePictureDao
					.findByUsersAndApprove(userProfilePicturePending.getUsers(), true);
			if (userProfilePictureCurrent != null) {
				ArrayList<UserProfilePicture> oldList = userProfilePictureDao.findAllByUsersAndStatus(userProfilePicturePending.getUsers(), "Old");
				File currentFile = new File(userProfilePictureCurrent.getUrl());
				if (currentFile.exists()) {
					
//					System.out.println("Called");
					File dest = new File(UPLOADED_FOLDER + "old_" + userProfilePicturePending.getUsers().getUserCode()+"_"+(oldList.size()+1));
					if (dest.exists()) {
						dest.delete();
					}
					currentFile.renameTo(dest);
				}
				userProfilePictureCurrent
						.setUrl(UPLOADED_FOLDER + "old_" + userProfilePicturePending.getUsers().getUserCode()+"_"+(oldList.size()+1));
				userProfilePictureCurrent.setApprove(false);
				userProfilePictureCurrent.setStatus("Old");
			}
			File file = new File(userProfilePicturePending.getUrl());
			if (file.exists()) {
				File dest = new File(UPLOADED_FOLDER + userProfilePicturePending.getUsers().getUserCode());
				if (dest.exists()) {
					dest.delete();
				}
				file.renameTo(dest);
				userProfilePicturePending.setApprove(true);
				userProfilePicturePending.setUrl(UPLOADED_FOLDER + userProfilePicturePending.getUsers().getUserCode());
				userProfilePicturePending.setApprovedBy(usersDao.findOneByUserCode("kavinda"));
				userProfilePicturePending.setApprovedDate(new Date());
				userProfilePicturePending.setStatus("Approved");

			}

			profilePictureLsit = getNotApprovedUserProfilePic();

		} else {
			return profilePictureLsit;
		}

		if (userProfilePictureDao.save(userProfilePicturePending) != null) {
			if (userProfilePictureCurrent != null) {
				if (userProfilePictureDao.save(userProfilePictureCurrent) != null) {
					return profilePictureLsit;
				}
			}

		}
		return profilePictureLsit;
	}

	@Override
	public Map<String, String> getProfileImage(String id) throws Exception {
		File file = new File("/home/rpcadmin/APINProfilePic/" + id);

		if (!file.exists()) {
			file = new File("/home/rpcadmin/APINProfilePic/dummy");
		}

		String encodeImage = Base64.getEncoder().withoutPadding().encodeToString(Files.readAllBytes(file.toPath()));

		Map<String, String> jsonMap = new HashMap<>();

		jsonMap.put("content", encodeImage);

		return jsonMap;
	}

	@Override
	public Users getUserByUserCode(String userCode) throws Exception {
		return usersDao.findOneByUserCode(userCode);
	}

}
