package Appcenter.study.domain.game;

import Appcenter.study.domain.member.Member;
import Appcenter.study.domain.member.MemberRepository;
import Appcenter.study.domain.purchase.PurchaseRepository;
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
public class GameService {

    private final GameRepository gameRepository;
    private final MemberRepository memberRepository;
    private final PurchaseRepository purchaseRepository;

    @Transactional(readOnly = true)
    public GameInfoResponse getGameInfo(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new CustomException(ErrorCode.GAME_NOT_FOUND));

        return GameInfoResponse.builder().game(game).build();
    }

    @Transactional
    public RefundResponse refund(UserDetailsImpl userDetails, Long gameId) {
        Member member = memberRepository.findById(userDetails.getMember().getId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new CustomException(ErrorCode.GAME_NOT_FOUND));

        purchaseRepository.deleteByMemberAndGame(member, game);
        log.info("delete purchase");

        return RefundResponse.builder().memberNickname(member.getNickname()).gameTitle(game.getTitle()).build();
    }
}
