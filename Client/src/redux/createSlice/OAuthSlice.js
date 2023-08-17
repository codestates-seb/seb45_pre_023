import { createSlice } from '@reduxjs/toolkit';

const OAuthSlice = createSlice({
  name: 'OAuth', // action 이름
  initialState: { provider: '', token: '' }, // 초기값
  reducers: {
    // reducer들 모음
    google: (state, action) => {
      state.provider = 'GOOGLE'; // action에서 값을 받으면 action.payload 으로 들어옴
    },
    github: (state, action) => {
      state.provider = 'GITHUB';
    },
    kakao: (state, action) => {
      state.provider = 'KAKAO';
    },
    oauthtoken: (state, action) => {
      state.token = action.payload;
    },
  },
});

export default OAuthSlice;
export const { google, github, kakao, oauthtoken } = OAuthSlice.actions;
