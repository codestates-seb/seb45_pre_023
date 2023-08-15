import { createSlice } from '@reduxjs/toolkit';

export const tipboxSlice = createSlice({
  name: 'tipbox', // action 이름
  initialState: { value: 1 }, // 초기값
  reducers: {
    // reducer들 모음
    next: (state, action) => {
      state.value = state.value + 1; // action에서 값을 받으면 action.payload 으로 들어옴
    },
  },
});

export default tipboxSlice;
export const { next } = tipboxSlice.actions;