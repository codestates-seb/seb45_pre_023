import { createSlice } from '@reduxjs/toolkit';

const SignUpInfoSlice = createSlice({
  name: 'SignUpInfo', // action 이름
  initialState: {
    value: {
      email: '',
      password: '',
      nickname: '',
    },
    checkedoption: { send: false, robot: false },
    code: '',
  }, // 초기값
  reducers: {
    // reducer들 모음
    email: (state, action) => {
      state.value.email = action.payload; // action에서 값을 받으면 action.payload 으로 들어옴
    },
    password: (state, action) => {
      state.value.password = action.payload;
    },
    nickname: (state, action) => {
      state.value.nickname = action.payload;
    },
    checkedsend: (state, action) => {
      state.checkedoption.send = action.payload;
    },
    checkedrobot: (state, action) => {
      state.checkedoption.robot = action.payload;
    },
    code: (state, action) => {
      state.code = action.payload;
    },
  },
});

export default SignUpInfoSlice;
export const { email, password, nickname, checkedsend, checkedrobot, code } =
  SignUpInfoSlice.actions;
