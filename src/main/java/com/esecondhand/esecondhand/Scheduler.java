package com.esecondhand.esecondhand;

import com.esecondhand.esecondhand.domain.entity.VerificationToken;
import com.esecondhand.esecondhand.domain.repository.UserRepository;
import com.esecondhand.esecondhand.domain.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class Scheduler {
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;

    @Transactional
    @Scheduled(cron = "0 0 12 ? * SUN")
    public void scheduleDeleteNotEnabledUsers() {
        List<VerificationToken> tokens = verificationTokenRepository.findUnconfirmedUsers(LocalDateTime.now());
        tokens.forEach((token) -> {
            userRepository.deleteById(token.getUser().getId());
            verificationTokenRepository.deleteById(token.getId());
        });
    }
}
