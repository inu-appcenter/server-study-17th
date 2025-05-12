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

        log.info("[게임 정보 조회 요청] gameId={}", gameId);

        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> {
                    log.warn("[게임 정보 조회 실패] 존재하지 않는 게임: gameId={}", gameId);
                    return new CustomException(ErrorCode.GAME_NOT_FOUND);
                });

        log.info("[게임 정보 조회 성공] gameId={}, title={}", game.getId(), game.getTitle());
        return GameInfoResponse.builder().game(game).build();
    }

    @Transactional
    public RefundResponse refund(UserDetailsImpl userDetails, Long gameId) {

        log.info("[게임 환불 요청] memberId={}, gameId={}", userDetails.getMember().getId(), gameId);

        Member member = memberRepository.findById(userDetails.getMember().getId())
                .orElseThrow(() -> {
                    log.warn("[환불 실패] 존재하지 않는 사용자: memberId={}", userDetails.getMember().getId());
                    return new CustomException(ErrorCode.MEMBER_NOT_FOUND);
                });
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> {
                    log.warn("[환불 실패] 존재하지 않는 게임: gameId={}", gameId);
                    return new CustomException(ErrorCode.GAME_NOT_FOUND);
                });

        purchaseRepository.deleteByMemberAndGame(member, game);

        log.info("[환불 성공] memberId={}, nickname={}, gameId={}, title={}",
                member.getId(), member.getNickname(), game.getId(), game.getTitle());

        return RefundResponse.builder().memberNickname(member.getNickname()).gameTitle(game.getTitle()).build();
    }
}
