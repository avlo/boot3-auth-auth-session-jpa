package com.prosilion.scdecisionmatrix.service;

import com.prosilion.scdecisionmatrix.model.entity.AppUser;
import com.prosilion.scdecisionmatrix.repository.AppUserRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppUserService {
	private final AppUserRepository appUserRepository;

	@Autowired
	public AppUserService(AppUserRepository appUserRepository) {
		this.appUserRepository = appUserRepository;
	}

	public AppUser findById(Integer id) {
		return appUserRepository.findById(id).get();
	}

	public AppUser findByAppUser(@NonNull AppUser appUser) {
		if (appUser.getId() != null) {
			return findById(appUser.getId());
		}
		return new AppUser();
	}

	@Transactional
	public AppUser save(@NonNull AppUser appUser) {
		AppUser appUserToSave = findByAppUser(appUser);
		return appUserToSave.getId() != null ? appUserToSave
				: appUserRepository.save(appUserToSave);
	}

//	@Transactional
//	public AppUser createUser(@NonNull AppUser appUser) {
//		return appUserRepository.save(authUserDetails.authgetAppUser());
//	}
}
