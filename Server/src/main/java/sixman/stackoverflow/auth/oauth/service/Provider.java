package sixman.stackoverflow.auth.oauth.service;

import sixman.stackoverflow.global.entity.BaseEnum;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public enum Provider implements BaseEnum {

    GOOGLE("google", (attributes) ->
            MemberProfile.builder()
            .email((String) attributes.get("email"))
            .build()),

    GITHUB("github", (attributes) ->
            MemberProfile.builder()
            .email((String) attributes.get("email"))
            .build()),

    KAKAO("kakao", (attributes) -> {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

        return MemberProfile.builder()
                .email((String) kakaoAccount.get("email"))
                .build();
    });

    private final String registrationId;
    private final Function<Map<String, Object>, MemberProfile> of;

    Provider(String registrationId, Function<Map<String, Object>, MemberProfile> of) {
        this.registrationId = registrationId;
        this.of = of;
    }

    public static MemberProfile extract(String registrationId, Map<String, Object> attributes) {
        return Arrays.stream(values())
                .filter(provider -> registrationId.equals(provider.registrationId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .of.apply(attributes);
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getDescription() {
        return this.registrationId;
    }
}
