package librasys.service;

import java.util.ArrayList;
import java.util.List;
import librasys.model.Member;

/**
 *
 * @author AmmarPasifiky
 */
public class MemberService {

    private List<Member> members;

    public MemberService() {
        this.members = new ArrayList<>();
    }

    public void registerMember(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null.");
        }
        if (findMemberByMemberNumber(member.getMemberNumber()) != null) {
            throw new IllegalArgumentException(
                    "Member number already exists: " + member.getMemberNumber());
        }
        members.add(member);
    }

    public void updateMember(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null.");
        }

        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getMemberNumber().equals(member.getMemberNumber())) {
                members.set(i, member);
                return;
            }
        }

        throw new IllegalArgumentException(
                "Member not found: " + member.getMemberNumber());
    }

    public void removeMemberByMemberNumber(String memberNumber) {
        if (isBlank(memberNumber)) {
            throw new IllegalArgumentException("Member number cannot be empty.");
        }

        boolean removed = members.removeIf(
                member -> member.getMemberNumber().equals(memberNumber));
        if (!removed) {
            throw new IllegalArgumentException("Member not found: " + memberNumber);
        }
    }

    public Member findMemberByMemberNumber(String memberNumber) {
        if (isBlank(memberNumber)) {
            return null;
        }

        for (Member member : members) {
            if (member.getMemberNumber().equals(memberNumber)) {
                return member;
            }
        }
        return null;
    }

    public Member findMemberByEmail(String email) {
        if (isBlank(email)) {
            return null;
        }

        for (Member member : members) {
            if (member.getEmail().equalsIgnoreCase(email)) {
                return member;
            }
        }
        return null;
    }

    public List<Member> getAllMembers() {
        return new ArrayList<>(members);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
