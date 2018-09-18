package org.arpicoinsurance.groupit.main.dao;

import java.util.ArrayList;

import org.arpicoinsurance.groupit.main.model.UserProfilePicture;
import org.arpicoinsurance.groupit.main.model.Users;
import org.springframework.data.repository.CrudRepository;

public interface UserProfilePictureDao extends CrudRepository<UserProfilePicture, Integer>{

	UserProfilePicture findByUsersAndApprove(Users users, boolean approve) throws Exception;
	
	ArrayList<UserProfilePicture> findAllByApproveAndStatus(Boolean approve, String status) throws Exception;
	
	ArrayList<UserProfilePicture> findAllByUsersAndStatus(Users users , String status) throws Exception;
}
