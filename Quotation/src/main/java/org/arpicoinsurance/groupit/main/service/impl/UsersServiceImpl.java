package org.arpicoinsurance.groupit.main.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.arpicoinsurance.groupit.main.dao.UserProfilePictureDao;
import org.arpicoinsurance.groupit.main.dao.UsersDao;
import org.arpicoinsurance.groupit.main.model.UserProfilePicture;
import org.arpicoinsurance.groupit.main.model.Users;
import org.arpicoinsurance.groupit.main.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class UsersServiceImpl implements UsersService{
	
	private static String UPLOADED_FOLDER = "F://temp//";
	
	@Autowired
	private UsersDao usersDao;
	
	@Autowired
	private UserProfilePictureDao userProfilePictureDao;

	@Override
	public boolean saveUser(Users users) throws Exception {
		if(usersDao.save(users)!=null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean updateUser(Users users) throws Exception {
		if(usersDao.save(users)!=null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteUser(Integer id) throws Exception {
		if(usersDao.deleteOne(id)!= null) {
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

			String fileName = UPLOADED_FOLDER + "pending_"+ userCode;
			
			File saveImage = new File(fileName);
			
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(saveImage));
			
			stream.write(bytes);

			UserProfilePicture userProfilePicture = new UserProfilePicture();
			
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
	public ArrayList<UserProfilePicture> approveUserProfilePic(Integer id, String userCode) throws Exception {
		

		return null;
	}

}
