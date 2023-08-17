package sixman.stackoverflow.domain.member.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

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
    @DisplayName("myIntro, title, location, accounts 를 받아 MyInfo 객체를 수정한다.")
    void updateMyIntro() {
        //given
        MyInfo myInfo = createMyInfoDefault();
        String myIntro = "myIntro";
        String title = "title";
        String location = "location";
        List<String> accounts = List.of("account1", "account2");


        //when
        myInfo.updateMyInfo(myIntro, title, location, accounts);

        //then
        assertThat(myInfo.getMyIntro()).isEqualTo(myIntro);
        assertThat(myInfo.getTitle()).isEqualTo(title);
        assertThat(myInfo.getLocation()).isEqualTo(location);
        assertThat(myInfo.getAccounts()).isEqualTo(accounts);
    }

    @Test
    @DisplayName("MyInfo 객체를 수정할 때 값이 null 이면 수정하지 않는다.")
    void updateMyIntroNullcheck() {
        //given
        String myIntro = "myIntro";
        String title = "title";
        String location = "location";
        List<String> accounts = List.of("account1", "account2");
        MyInfo myInfo = createMyInfo(myIntro, title, location, accounts);

        String newMyIntro = null;
        String newTitle = null;
        String newLocation = null;
        List<String> newAccounts = null;

        //when
        myInfo.updateMyInfo(newMyIntro, newTitle, newLocation, newAccounts);

        //then
        assertThat(myInfo.getMyIntro()).isEqualTo(myIntro);
        assertThat(myInfo.getTitle()).isEqualTo(title);
        assertThat(myInfo.getLocation()).isEqualTo(location);
        assertThat(myInfo.getAccounts()).isEqualTo(accounts);
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

    private MyInfo createMyInfo(String myIntro, String title, String location, List<String> accounts) {
        return MyInfo.builder()
                .myIntro(myIntro)
                .title(title)
                .location(location)
                .accounts(accounts)
                .build();
    }
}