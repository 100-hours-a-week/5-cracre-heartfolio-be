package com.heartfoilo.demo.domain.user.service;

import com.heartfoilo.demo.domain.donation.repository.DonationRepository;
import com.heartfoilo.demo.domain.portfolio.entity.Account;
import com.heartfoilo.demo.domain.donation.entity.Donation;
import com.heartfoilo.demo.domain.portfolio.repository.PortfolioRepository;
import com.heartfoilo.demo.domain.user.entity.User;
import com.heartfoilo.demo.domain.user.repository.UserRepository;
import com.heartfoilo.demo.global.exception.UserNotFoundException;
import com.heartfoilo.demo.login.Entity.RefreshToken;
import com.heartfoilo.demo.login.Repository.RefreshTokenRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;
    private final DonationRepository donationRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    @Override
    public ResponseEntity<?> getMypageInfo(long userId){

        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("유저 정보를 찾을 수 없습니다."));
        Map<String,Object> info = new HashMap<>();
        info.put("profile_image_url",user.getProfileImageUrl());
        info.put("name",user.getName());
        info.put("nickname",user.getNickname());

        Account account = portfolioRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Account not found"));
        if(account == null){
            throw new EntityNotFoundException("Account not exist");
        }
        info.put("cash",account.getCash());
        info.put("donation",account.getDonationPayment());


        return ResponseEntity.ok(info);
    }

    @Override
    public ResponseEntity<?> fixNickname(long userId,String nickname){
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("유저 정보를 찾을 수 없습니다."));
        boolean DuplicateNickname = userRepository.existsByNickname(nickname);
        if (DuplicateNickname == true){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("중복된 닉네임입니다.");
        } // status code 409 반환
        user.fixInfo(userId,nickname);
        userRepository.save(user);

        return ResponseEntity.ok(user);

    }
    @Override
    public ResponseEntity<?> removeRefreshToken(long userId){
        refreshTokenRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Token not exists"));

        refreshTokenRepository.deleteByUserId(userId);

        return ResponseEntity.ok("Refresh token successfully removed.");

    }



}
