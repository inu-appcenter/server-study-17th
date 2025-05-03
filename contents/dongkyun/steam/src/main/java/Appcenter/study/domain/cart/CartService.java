package Appcenter.study.domain.cart;

import Appcenter.study.domain.game.Game;
import Appcenter.study.domain.member.Member;
import Appcenter.study.domain.game.GameRepository;
import Appcenter.study.domain.member.MemberRepository;
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
        Member member = memberRepository.findById(userDetails.getMember().getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게임이 존재하지 않습니다. ID = " + gameId));

        Cart cart = Cart.builder().member(member).game(game).build();

        member.addCart(cart);
        log.info("addCart");

        return CartResponse.builder().cart(cart).build();
    }
}
