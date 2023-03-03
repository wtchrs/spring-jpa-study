package study.datajpa.dto;

import lombok.Data;
import study.datajpa.entity.Member;

@Data
public class MemberDto {

    private Long id;
    private String username;
    private String teamName;

    public MemberDto(Long id, String username, String teamName) {
        this.id = id;
        this.username = username;
        this.teamName = teamName;
    }

    public static MemberDto of(Member member) {
        if (member.getTeam() == null) return new MemberDto(member.getId(), member.getUsername(), null);
        return new MemberDto(member.getId(), member.getUsername(), member.getTeam().getName());
    }
}
