package com.heartfoilo.demo.domain.donation.api;

import com.heartfoilo.demo.domain.donation.dto.requestDto.PaymentCallbackRequestDto;
import com.heartfoilo.demo.domain.donation.dto.requestDto.RequestPayDto;
import com.heartfoilo.demo.domain.donation.entity.Donation;
import com.heartfoilo.demo.domain.donation.service.DonationService;
import com.heartfoilo.demo.domain.donation.service.PaymentService;
import com.heartfoilo.demo.domain.user.entity.User;
import com.heartfoilo.demo.domain.user.repository.UserRepository;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Controller
@CrossOrigin("*")
@RequestMapping("/api/donation")
public class LocalHomeController {
    private final UserRepository userRepository;
    private final DonationService donationService;
    @GetMapping
    public String home() {
        return "home";
    }

    @GetMapping("/order")
    public String order(@RequestParam(name = "message", required = false) String message,
                        @RequestParam(name = "orderUid", required = false) String id,
                        @RequestParam(name = "cash", required = false) long price,
                        Model model) {

        model.addAttribute("message", message);
        model.addAttribute("orderUid", id);
        model.addAttribute("cash",price);
        return "order";
    }
    private final PaymentService paymentService;

    @GetMapping("/payment/{id}")
    public String paymentPage(@PathVariable(name = "id", required = false) String id, Model model){
        RequestPayDto requestDto = paymentService.findRequestDto(id);
        model.addAttribute("requestDto",requestDto);
        return "payment";
    }

    @GetMapping("/success-payment")
    public String successPaymentPage() {
        return "success-payment";
    }

    @GetMapping("/fail-payment")
    public String failPaymentPage() {
        return "fail-payment";
    }
    @PostMapping("/order")
    public String makeDonation(HttpServletRequest request, Long cash){
        String userId = (String) request.getAttribute("userId");
        if (userId == null){
            throw new AuthorizationServiceException("User ID is missing");
        }
        User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new RuntimeException("User not found"));

        Donation donation = donationService.makeDontaion(user,cash);
        String message = "주문 실패";
        if (donation != null) {
            message = "주문 성공";
        }
        String encode = URLEncoder.encode(message, StandardCharsets.UTF_8);

        return "redirect:/order?message="+encode+"&orderUid="+donation.getOrderUid()+"&cash="+cash;

    }

}
