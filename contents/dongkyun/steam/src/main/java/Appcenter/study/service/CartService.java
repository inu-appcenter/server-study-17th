package Appcenter.study.service;

import Appcenter.study.dto.res.CartResponse;
import Appcenter.study.entity.Cart;
import Appcenter.study.entity.Game;
import Appcenter.study.entity.Member;
import Appcenter.study.repository.GameRepository;
import Appcenter.study.repository.MemberRepository;
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
}
