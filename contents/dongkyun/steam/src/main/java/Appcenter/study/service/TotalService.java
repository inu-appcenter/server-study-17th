package Appcenter.study.service;

import Appcenter.study.dto.req.UpdateEmailRequest;
import Appcenter.study.dto.req.UpdateMypageRequest;
import Appcenter.study.dto.res.*;
import Appcenter.study.entity.Cart;
import Appcenter.study.entity.Game;
import Appcenter.study.entity.Member;
import Appcenter.study.repository.GameRepository;
import Appcenter.study.repository.MemberRepository;
import Appcenter.study.repository.PurchaseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TotalService {

    private final GameRepository gameRepository;
    private final MemberRepository memberRepository;
    private final PurchaseRepository purchaseRepository;

    public GameInfoResponse getGameInfo(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow();

        return GameInfoResponse.builder().game(game).build();
    }

    @Transactional
    public CartResponse addCart(Long memberId, Long gameId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow();
        Game game = gameRepository.findById(gameId)
                .orElseThrow();

        Cart cart = Cart.builder().member(member).game(game).build();

        member.addCart(cart);

        return CartResponse.builder().cart(cart).build();
    }

    @Transactional
    public UpdateMypageResponse updateMypage(Long memberId, UpdateMypageRequest updateMypageRequest) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow();

        member.updateMypage(updateMypageRequest);

        return UpdateMypageResponse.builder().member(member).build();
    }

    @Transactional
    public UpdateEmailResponse updateEmail(Long memberId, UpdateEmailRequest updateEmailRequest) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow();

        member.updateEmail(updateEmailRequest);

        return UpdateEmailResponse.builder().member(member).build();
    }

    @Transactional
    public RefundResponse refund(Long memberId, Long gameId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow();
        Game game = gameRepository.findById(gameId)
                .orElseThrow();

        purchaseRepository.deleteByMemberAndGame(member, game);

        return RefundResponse.builder().memberNickname(member.getNickname()).gameTitle(game.getTitle()).build();
    }
}
