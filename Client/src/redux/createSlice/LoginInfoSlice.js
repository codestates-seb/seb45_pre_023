import { createSlice } from '@reduxjs/toolkit';

const LoginInfoSlice = createSlice({
  name: 'LoginInfo',
  initialState: { value: { email: '', password: '' }, token: '', myid: '' },
  reducers: {
    email: (state, action) => {
      state.value.email = action.payload;
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
    initLogin: (state) => {
      state.value.email = '';
      state.value.password = '';
      state.token = '';
    },
  },
});

export default LoginInfoSlice;
export const { email, password, logintoken, myid, initLogin } = LoginInfoSlice.actions;

