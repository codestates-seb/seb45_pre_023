package sixman.stackoverflow.domain.member.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MyInfoTest {

    @Test
    @DisplayName("아무 정보가 없는 myInfo 객체를 생성한다.")
    void createMyInfo() {
        //given

        //when
        MyInfo myInfo = MyInfo.createMyInfo();

        //then
        assertThat(myInfo.getImage()).isNull();
        assertThat(myInfo.getMyIntro()).isNull();
    }

    @Test
    @DisplayName("myIntro 를 받아 MyInfo 객체를 수정한다.")
    void updateMyIntro() {
        //given
        MyInfo myInfo = createMyInfoDefault();

        //when
        myInfo.updateMyIntro("new myIntro");

        //then
        assertThat(myInfo.getMyIntro()).isEqualTo("new myIntro");

    }

    @Test
    @DisplayName("image.html 를 받아 MyInfo 객체를 수정한다.")
    void updateImage() {
        //given
        MyInfo myInfo = createMyInfoDefault();

        //when
        myInfo.updateImage("new image.html");

        //then
        assertThat(myInfo.getImage()).isEqualTo("new image.html");

    }

    private MyInfo createMyInfoDefault() {
        return MyInfo.builder()
                .build();
    }
}