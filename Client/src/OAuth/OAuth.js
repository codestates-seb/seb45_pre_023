const redirectUrl =
  'http://sixman-front-s3.s3-website.ap-northeast-2.amazonaws.com'; //'http://localhost:3000';
const Google_CLIENT_ID =
  '690166915653-paa941cs02li19r4b778hldnjbaldp1h.apps.googleusercontent.com';
const Github_CLIENT_ID = '482acf418bf96174b896';
const Kakao_CLIENT_ID = '0c96492a94d3847f094fda7ec7a29407';

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
