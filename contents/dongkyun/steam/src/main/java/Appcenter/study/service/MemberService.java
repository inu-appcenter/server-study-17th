package Appcenter.study.service;

import Appcenter.study.dto.req.UpdateEmailRequest;
import Appcenter.study.dto.req.UpdateMypageRequest;
import Appcenter.study.dto.res.UpdateEmailResponse;
import Appcenter.study.dto.res.UpdateMypageResponse;
import Appcenter.study.entity.Member;
import Appcenter.study.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

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
}
