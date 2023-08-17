import { createSlice } from '@reduxjs/toolkit';

const LoginInfoSlice = createSlice({
  name: 'LoginInfo', // action 이름
  initialState: { value: { email: '', password: '' }, token: '', myid: '' }, // 초기값
  reducers: {
    // reducer들 모음
    email: (state, action) => {
      state.value.email = action.payload; // action에서 값을 받으면 action.payload 으로 들어옴
    },
    password: (state, action) => {
      state.value.password = action.payload;
    },
    logintoken: (state, action) => {
      state.token = action.payload;
    },
    myid: (state, action) => {
      state.myid = action.payload;
    },
  },
});

export default LoginInfoSlice;
export const { email, password, logintoken } = LoginInfoSlice.actions;
