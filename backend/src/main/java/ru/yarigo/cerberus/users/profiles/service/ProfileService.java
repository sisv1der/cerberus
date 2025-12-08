package ru.yarigo.cerberus.users.profiles.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yarigo.cerberus.users.profiles.model.Profile;
import ru.yarigo.cerberus.users.profiles.model.ProfileRepository;
import ru.yarigo.cerberus.users.user.model.User;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;

    @Transactional
    public Profile createProfileForUser(User user, String fullName) {
        Profile profile = new Profile();
        profile.setFullName(fullName);

        profile.setUser(user);
        return profileRepository.save(profile);
    }
}
