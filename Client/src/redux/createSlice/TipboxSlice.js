import { createSlice } from '@reduxjs/toolkit';

const tipboxSlice = createSlice({
  name: 'tipbox', // action 이름
  initialState: { position: 1, tipboxName: '' }, // 초기값
  reducers: {
    // reducer들 모음
    next: (state, action) => {
      state.position += 1; // action에서 값을 받으면 action.payload 으로 들어옴
    },
    tipbox: (state, action) => {
      state.tipboxName = action.payload;
    },
  },
});

export default tipboxSlice;
export const { next, tipbox } = tipboxSlice.actions;
