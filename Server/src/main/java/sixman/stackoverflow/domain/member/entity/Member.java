package sixman.stackoverflow.domain.member.entity;

import lombok.*;
import sixman.stackoverflow.global.entity.BaseEntity;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Authority authority;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "my_info_id")
    private MyInfo myInfo;

    public static Member createMember(String email, String nickname, String password) {
        return Member.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .authority(Authority.ROLE_USER)
                .myInfo(MyInfo.builder().build())
                .build();
    }
}
