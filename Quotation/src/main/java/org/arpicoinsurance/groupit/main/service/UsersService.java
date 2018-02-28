package org.arpicoinsurance.groupit.main.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.arpicoinsurance.groupit.main.model.UserProfilePicture;
import org.arpicoinsurance.groupit.main.model.Users;
import org.springframework.web.multipart.MultipartFile;


public interface UsersService {

	boolean saveUser(Users users) throws Exception;
	
	boolean updateUser(Users users) throws Exception;
	
	boolean deleteUser(Integer id) throws Exception;
	
	Users getUser(Integer id) throws Exception;
	
	List <Users> getAllUsers() throws Exception;
	
	Users getUserByLoginId(Integer id) throws Exception;
	
	String saveUserProfilePic(MultipartFile image, String userCode) throws Exception;

	ArrayList<UserProfilePicture> getNotApprovedUserProfilePic() throws Exception;
	 
	Map<String, String> getPendingImage(String userCode) throws Exception;

	ArrayList<UserProfilePicture> approveImage(String id) throws Exception;
	
}
