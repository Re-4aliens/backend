package com.aliens.backend.chat.service.model;

import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.global.BaseServiceTest;
import com.aliens.backend.global.DummyGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MemberPairTest extends BaseServiceTest {

    @Autowired
    private DummyGenerator dummyGenerator;

    @Test
    @DisplayName("MemberPair 비교 테스트")
    void MemberPairEqualsTest() {
        //Given
        List<Member> members = dummyGenerator.generateMultiMember(2);
        MemberPair pair1 = new MemberPair(members.get(0), members.get(1));
        MemberPair pair2 = new MemberPair(members.get(0), members.get(1));

        //When
        boolean result = pair1.equals(pair2);

        //Then
        assertEquals(true, result);
    }

    @Test
    @DisplayName("MemberPair 중복 제거 테스트")
    void UniqueMemberPairsTest() {
        //Given
        List<Member> members = dummyGenerator.generateMultiMember(2);
        MemberPair pair1 = new MemberPair(members.get(0), members.get(1));
        MemberPair pair2 = new MemberPair(members.get(1), members.get(0));

        //When
        Set<MemberPair> uniquePairs = new HashSet<>(Arrays.asList(pair1, pair2));

        //Then
        assertEquals(1, uniquePairs.size());
    }
}