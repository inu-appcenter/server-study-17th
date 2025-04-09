package Appcenter.study.service;

import Appcenter.study.dto.res.GameInfoResponse;
import Appcenter.study.dto.res.RefundResponse;
import Appcenter.study.entity.Game;
import Appcenter.study.entity.Member;
import Appcenter.study.repository.GameRepository;
import Appcenter.study.repository.MemberRepository;
import Appcenter.study.repository.PurchaseRepository;
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
                .orElseThrow(() -> new IllegalArgumentException("해당 게임이 존재하지 않습니다. ID = " + gameId));

        return GameInfoResponse.builder().game(game).build();
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
