import { createSlice } from '@reduxjs/toolkit';

const OAuthSlice = createSlice({
  name: 'OAuth', // action 이름
  initialState: { value: '' }, // 초기값
  reducers: {
    // reducer들 모음
    google: (state, action) => {
      state.value = 'GOOGLE'; // action에서 값을 받으면 action.payload 으로 들어옴
    },
    github: (state, action) => {
      state.value = "GITHUB"
    },
    kakao: (state, action) => {
      state.value = "KAKAO"
    },
  },
});

export default OAuthSlice;
export const { google, github, kakao } = OAuthSlice.actions;
