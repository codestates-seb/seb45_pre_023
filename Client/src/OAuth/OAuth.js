const redirectUrl =
  'http://sixman-front-s3.s3-website.ap-northeast-2.amazonaws.com';
const Google_CLIENT_ID =
  '690166915653-paa941cs02li19r4b778hldnjbaldp1h.apps.googleusercontent.com';
const Kakao_CLIENT_ID = '0c96492a94d3847f094fda7ec7a29407';
const Github_CLIENT_ID = '482acf418bf96174b896';

export const handleGoogleLogin = () => {
  return window.location.assign(
    `https://accounts.google.com/o/oauth2/v2/auth?client_id=690166915653-paa941cs02li19r4b778hldnjbaldp1h.apps.googleusercontent.com&redirect_uri=http://sixman-front-s3.s3-website.ap-northeast-2.amazonaws.com&response_type=code&scope=email`
  );
};

export const handleGithubLogin = () => {
  return window.location.assign(
    `https://github.com/login/oauth/authorize?client_id=482acf418bf96174b896&redirect_uri=http://sixman-front-s3.s3-website.ap-northeast-2.amazonaws.com&scope=user:email, read:user`
  );
};

export const handleKakaoLogin = () => {
  return window.location.assign(
    `https://kauth.kakao.com/oauth/authorize?client_id=0c96492a94d3847f094fda7ec7a29407&redirect_uri=http://sixman-front-s3.s3-website.ap-northeast-2.amazonaws.com&response_type=code&scope=profile_nickname, account_email`
  );
};