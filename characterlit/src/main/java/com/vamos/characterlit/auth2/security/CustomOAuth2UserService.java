package com.vamos.characterlit.auth2.security;

import com.vamos.characterlit.auth2.response.NaverResponse;
import com.vamos.characterlit.auth2.response.OAuth2Response;
import com.vamos.characterlit.users.domain.Users;
import com.vamos.characterlit.users.repository.UsersRepository;
import com.vamos.characterlit.users.response.UsersResponseDTO;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UsersRepository usersRepository;

    public CustomOAuth2UserService(UsersRepository usersRepository) {

        this.usersRepository = usersRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("naver")) {

            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("kakao")) {

        } else {

            return null;
        }
        String userId = oAuth2Response.getProvider() + "_" + oAuth2Response.getProviderId();
        Users existData = usersRepository.findByUserId(userId);

        if (existData == null) {

            Users user = new Users();
            user.setUserId(userId);
            user.setEmail(oAuth2Response.getEmail());
            user.setName(oAuth2Response.getName());
            user.setRole("USER");
            if (registrationId.equals("naver")) {
                user.setLoginServer(1);
            } else if (registrationId.equals("kakao")) {
                user.setLoginServer(2);
            }

            Timestamp now = new Timestamp(System.currentTimeMillis());
            user.setCreatedDate(now);

            usersRepository.save(user);

            UsersResponseDTO userDTO = new UsersResponseDTO();
            userDTO.setUserId(userId);
            userDTO.setName(oAuth2Response.getName());
            userDTO.setRole("USER");

            return new CustomOAuth2User(userDTO);
        } else {

            existData.setEmail(oAuth2Response.getEmail());
            existData.setName(oAuth2Response.getName());

            usersRepository.save(existData);

            UsersResponseDTO userDTO = new UsersResponseDTO();
            userDTO.setUserId(existData.getUserId());
            userDTO.setName(oAuth2Response.getName());
            userDTO.setRole(existData.getRole());

            return new CustomOAuth2User(userDTO);
        }
    }
}
