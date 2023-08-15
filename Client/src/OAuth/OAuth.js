const redirectUrl = 'http://localhost:3000';
const Google_CLIENT_ID =
  '8689262456-6jmp4a8j91jbfhij1sso4i2ptks2d6dv.apps.googleusercontent.com';
const Github_CLIENT_ID = 'Iv1.54c54153b34335b3';
const Kakao_CLIENT_ID = '94c027fd4dc937a6356193e675fc39e8';

export const handleGoogleLogin = () => {
  return window.location.assign(
    `https://accounts.google.com/o/oauth2/v2/auth?client_id=${Google_CLIENT_ID}&redirect_uri=${redirectUrl}&response_type=code&scope=https://www.googleapis.com/auth/iam.test`
  );
};

export const handleGithubLogin = () => {
  return window.location.assign(
    `https://github.com/login/oauth/authorize?client_id=${Github_CLIENT_ID}`
  );
};

export const handleKakaoLogin = () => {
  return window.location.assign(
    `https://kauth.kakao.com/oauth/authorize?client_id=${Kakao_CLIENT_ID}&redirect_uri=${redirectUrl}&response_type=code`
  );
};
