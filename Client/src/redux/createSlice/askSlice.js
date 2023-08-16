import { createSlice } from '@reduxjs/toolkit';

const AskSlice = createSlice({
  name: 'Ask', // action 이름
  initialState: { value: { title: '', detail: '', expect: '', tags: [] } }, // 초기값
  reducers: {
    // reducer들 모음
    title: (state, action) => {
      state.value.title = action.payload; // action에서 값을 받으면 action.payload 으로 들어옴
    },
    detail: (state, action) => {
      state.value.detail = action.payload;
    },
    expect: (state, action) => {
      state.value.expect = action.payload;
    },
    tags: (state, action) => {
      state.value.tags = action.payload;
    },
    init: (state, action) => {
      state.value = { title: '', detail: '', expect: '', tags: [] };
    },
  },
});

export default AskSlice;
export const { title, detail, expect, tags, init } = AskSlice.actions;
