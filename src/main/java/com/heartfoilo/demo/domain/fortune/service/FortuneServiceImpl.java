package com.heartfoilo.demo.domain.fortune.service;

import static com.heartfoilo.demo.domain.fortune.constant.ErrorMessage.DUPLICATE_DAILYFORTUNE_MSG;

import com.heartfoilo.demo.domain.fortune.entity.DailyFortune;
import com.heartfoilo.demo.domain.fortune.entity.Fortune;
import com.heartfoilo.demo.domain.fortune.exception.DuplicateRegistDailyFortune;
import com.heartfoilo.demo.domain.fortune.exception.NotRegisterDailyFortune;
import com.heartfoilo.demo.domain.fortune.repository.DailyFortuneRepository;
import com.heartfoilo.demo.domain.fortune.repository.FortuneRepository;
import com.heartfoilo.demo.domain.user.entity.User;
import com.heartfoilo.demo.domain.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FortuneServiceImpl implements FortuneService {

    private final FortuneRepository fortuneRepository;
    private final DailyFortuneRepository dailyFortuneRepository;
    private final UserRepository userRepository;
    private final Random random = new Random();

    @Override
    public String getDailyFortune(Long userId) throws NotRegisterDailyFortune {
        User user = userRepository.findById(userId).orElseThrow();
        Optional<DailyFortune> dailyFortune = dailyFortuneRepository.findByUser(user);
        if(dailyFortune.isEmpty() || !dailyFortune.get().getDate().equals(LocalDate.now())){
            throw new NotRegisterDailyFortune();
        }
        return dailyFortune.get().getFortune().getContent();

    }

    @Override
    public String registDailyFortune(Long userId) {
        if(isExistDailyFortune(userId)){
            throw new DuplicateRegistDailyFortune(DUPLICATE_DAILYFORTUNE_MSG);
        }
        int id = random.nextInt(191) + 1;
        Fortune fortune = fortuneRepository.findById((long) id).orElseThrow();
        dailyFortuneRepository.save(DailyFortune.builder().user(userRepository.findById(userId).get()).fortune(fortune).date(LocalDate.now()).build());
        return fortune.getContent();
    }

    private boolean isExistDailyFortune(Long userId){
        User user = userRepository.findById(userId).orElseThrow();
        Optional<DailyFortune> dailyFortune = dailyFortuneRepository.findByUser(user);
        return dailyFortune.isPresent() && dailyFortune.get().getDate().equals(LocalDate.now());
    }



}
