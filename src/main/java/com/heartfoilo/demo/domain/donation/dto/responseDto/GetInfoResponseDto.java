package com.heartfoilo.demo.domain.donation.dto.responseDto;



import com.heartfoilo.demo.domain.user.entity.User;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;


@Data
@Getter
public class GetInfoResponseDto {
    private final long cash;
    private final long totalPurchase;


    @Builder
    public GetInfoResponseDto(User user, long cash, long totalPurchase){

        this.cash = cash;
        this.totalPurchase = totalPurchase;
    }
}
