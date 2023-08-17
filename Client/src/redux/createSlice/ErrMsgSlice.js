import { createSlice } from '@reduxjs/toolkit';

const ErrMsgSlice = createSlice({
  name: 'ErrMsg', // action 이름
  initialState: { value: '' }, // 초기값
  reducers: {
    // reducer들 모음
    errmsg: (state, action) => {
      state.value = action.payload; // action에서 값을 받으면 action.payload 으로 들어옴
    },
  },
});

export default ErrMsgSlice;
export const { errmsg } = ErrMsgSlice.actions;
