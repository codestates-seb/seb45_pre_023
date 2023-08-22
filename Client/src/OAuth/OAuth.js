const redirectUrl =
  'http://sixman-front-s3.s3-website.ap-northeast-2.amazonaws.com'; //'http://localhost:3000';
const Google_CLIENT_ID = process.env.Google_CLIENT_ID;
const Github_CLIENT_ID = process.env.Github_CLIENT_ID;
const Kakao_CLIENT_ID = process.env.Kakao_CLIENT_ID;

export const handleGoogleLogin = () => {
  return window.location.assign(
    `https://accounts.google.com/o/oauth2/v2/auth?client_id=${Google_CLIENT_ID}&redirect_uri=${redirectUrl}&response_type=code&scope=email`
  );
};

export const handleGithubLogin = () => {
  return window.location.assign(
    `https://github.com/login/oauth/authorize?client_id=${Github_CLIENT_ID}&redirect_uri=${redirectUrl}`
  );
};

export const handleKakaoLogin = () => {
  return window.location.assign(
    `https://kauth.kakao.com/oauth/authorize?client_id=${Kakao_CLIENT_ID}&redirect_uri=${redirectUrl}&response_type=code`
  );
};
