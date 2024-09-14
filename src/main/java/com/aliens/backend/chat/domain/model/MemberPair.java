package com.aliens.backend.chat.domain.model;

import com.aliens.backend.auth.domain.Member;

import java.util.Objects;

public record MemberPair(Member first, Member second) {
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        MemberPair that = (MemberPair) object;
        return (Objects.equals(first, that.first) && Objects.equals(second, that.second)) ||
                (Objects.equals(first, that.second) && Objects.equals(second, that.first));
    }

    @Override
    public int hashCode() {
        if (first.getId() < second.getId()) {
            return Objects.hash(first, second);
        } else {
            return Objects.hash(second, first);
        }
    }
}