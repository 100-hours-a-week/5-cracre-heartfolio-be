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
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@CrossOrigin("*")
@RequestMapping("/api/donation")
public class LocalHomeController {
    private final UserRepository userRepository;
    private final DonationService donationService;


//    @GetMapping("/order")
//    public String order(@RequestParam(name = "message", required = false) String message,
//                        @RequestParam(name = "orderUid", required = false) String id,
//                        @RequestParam(name = "cash", required = false) long price,
//                        Model model) {
//
//        model.addAttribute("message", message);
//        model.addAttribute("orderUid", id);
//        model.addAttribute("cash",price);
//        return "order";
//    }
    private final PaymentService paymentService;

    @GetMapping("/payment/{orderUid}")
    public ResponseEntity<RequestPayDto> paymentPage(@PathVariable(name = "orderUid", required = false) String id){
        RequestPayDto requestDto = paymentService.findRequestDto(id);
        return ResponseEntity.ok(requestDto);
    }// JSON 형식으로 response 변경

    @PostMapping("/order")
    public ResponseEntity<Map<String, String>> makeDonation(HttpServletRequest request, @RequestBody Map<String, Long> requestBody){ // TODO : Long cash 말고 받는 형식 변경
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null){
            throw new AuthorizationServiceException("User ID is missing");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Long cash = requestBody.get("price");
        Donation donation = donationService.makeDontaion(user,cash);


        Map<String, String> response = new HashMap<>();
        response.put("orderUid", donation.getOrderUid());
        return ResponseEntity.ok(response);

    }

}
