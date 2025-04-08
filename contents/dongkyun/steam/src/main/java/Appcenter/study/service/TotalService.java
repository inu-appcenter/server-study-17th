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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TotalService {

    private final GameRepository gameRepository;
    private final MemberRepository memberRepository;
    private final PurchaseRepository purchaseRepository;

    public GameInfoResponse getGameInfo(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게임이 존재하지 않습니다. ID = " + gameId));

        return GameInfoResponse.builder().game(game).build();
    }

    @Transactional
    public CartResponse addCart(Long memberId, Long gameId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. ID = " + memberId));
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게임이 존재하지 않습니다. ID = " + gameId));

        Cart cart = Cart.builder().member(member).game(game).build();

        member.addCart(cart);
        log.info("addCart");

        return CartResponse.builder().cart(cart).build();
    }

    @Transactional
    public UpdateMypageResponse updateMypage(Long memberId, UpdateMypageRequest updateMypageRequest) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. ID = " + memberId));

        member.updateMypage(updateMypageRequest);
        log.info("updateMypage");

        return UpdateMypageResponse.builder().member(member).build();
    }

    @Transactional
    public UpdateEmailResponse updateEmail(Long memberId, UpdateEmailRequest updateEmailRequest) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. ID = " + memberId));

        member.updateEmail(updateEmailRequest);
        log.info("updateEmail");

        return UpdateEmailResponse.builder().member(member).build();
    }

    @Transactional
    public RefundResponse refund(Long memberId, Long gameId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. ID=" + memberId));
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게임이 존재하지 않습니다. ID=" + gameId));

        purchaseRepository.deleteByMemberAndGame(member, game);
        log.info("delete purchase");

        return RefundResponse.builder().memberNickname(member.getNickname()).gameTitle(game.getTitle()).build();
    }
}
