package sixman.stackoverflow.domain.member.entity;

import lombok.*;
import sixman.stackoverflow.global.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long MyInfoId;

    private String myIntro;

    private String image;

    public static MyInfo createMyInfo(){
        return new MyInfo();
    }

    public void updateMyIntro(String myIntro){
        this.myIntro = myIntro;
    }

    public void updateImage(String image){
        this.image = image;
    }
}
