package com.veisite.vegecom.service.security;

import java.util.List;

import com.veisite.vegecom.model.security.User;
import com.veisite.vegecom.model.security.UserRole;

public interface LoginUserService {

	public User save(User loginUser);
	
	public User getById(String id);

	public List<User> getList();
	
	public List<UserRole> getRolesNames(String userId);

	public void addRole(String userId, String role);

	public void removeRole(String userId, String role);

}
