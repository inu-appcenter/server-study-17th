package Appcenter.study.domain.cart;

import Appcenter.study.domain.game.Game;
import Appcenter.study.domain.member.Member;
import Appcenter.study.domain.game.GameRepository;
import Appcenter.study.domain.member.MemberRepository;
import Appcenter.study.global.exception.CustomException;
import Appcenter.study.global.exception.ErrorCode;
import Appcenter.study.global.security.jwt.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private final GameRepository gameRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public CartResponse addCart(UserDetailsImpl userDetails, Long gameId) {

        log.info("[장바구니 추가 요청] memberId={}, gameId={}", userDetails.getMember().getId(), gameId);

        Member member = memberRepository.findById(userDetails.getMember().getId())
                .orElseThrow(() -> {
                    log.warn("[장바구니 추가 실패] 사용자 없음: memberId={}", userDetails.getMember().getId());
                    return new CustomException(ErrorCode.MEMBER_NOT_FOUND);
                });
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> {
                    log.warn("[장바구니 추가 실패] 게임 없음: gameId={}", gameId);
                    return new CustomException(ErrorCode.GAME_NOT_FOUND);
                });

        Cart cart = Cart.builder().member(member).game(game).build();

        member.addCart(cart);

        log.info("[장바구니 추가 완료] memberId={}, gameId={}, cartId={}", member.getId(), gameId, cart.getId());

        return CartResponse.builder().cart(cart).build();
    }
}
