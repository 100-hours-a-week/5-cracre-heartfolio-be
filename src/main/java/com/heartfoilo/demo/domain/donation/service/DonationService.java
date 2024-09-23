package com.heartfoilo.demo.domain.donation.service;

import com.heartfoilo.demo.domain.donation.entity.Donation;
import com.heartfoilo.demo.domain.user.entity.User;


public interface DonationService {
    Donation makeDontaion(User user, Long cash);
}
