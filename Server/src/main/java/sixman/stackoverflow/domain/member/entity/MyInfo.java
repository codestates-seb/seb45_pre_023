package sixman.stackoverflow.domain.member.entity;

import lombok.*;
import sixman.stackoverflow.global.entity.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long MyInfoId;

    @Lob
    private String myIntro;

    private String image;

    private String title;

    private String location;

    @ElementCollection
    private List<String> accounts = new ArrayList<>();


    public static MyInfo createMyInfo(){
        return new MyInfo();
    }

    public void updateMyInfo(String myIntro, String title, String location, List<String> accounts){
        this.myIntro = myIntro == null ? this.myIntro : myIntro;
        this.title = title == null ? this.title : title;
        this.location = location == null ? this.location : location;
        this.accounts = accounts == null ? this.accounts : accounts;
    }

    public void updateImage(String image){
        this.image = image;
    }

}
