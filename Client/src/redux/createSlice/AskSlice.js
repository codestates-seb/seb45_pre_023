import { createSlice } from '@reduxjs/toolkit';

const AskSlice = createSlice({
  name: 'Ask',
  initialState: {
    value: {
      title: '',
      detail: '',
      expect: '',
      tags: [],
    },
    errmsg: {
      title: '',
      detail: '',
      expect: '',
      tags: '',
    },
  },
  reducers: {
    title: (state, action) => {
      state.value.title = action.payload;
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
    initValue: (state, action) => {
      state.value = { title: '', detail: '', expect: '', tags: [] };
    },
    titleError: (state, action) => {
      state.errmsg.title = action.payload;
    },
    detailError: (state, action) => {
      state.errmsg.detail = action.payload;
    },
    expectError: (state, action) => {
      state.errmsg.expect = action.payload;
    },
    tagsError: (state, action) => {
      state.errmsg.tags = action.payload;
    },
    initError: (state, action) => {
      state.errmsg = { title: '', detail: '', expect: '', tags: '' };
    },
  },
});

export default AskSlice;
export const {
  title,
  detail,
  expect,
  tags,
  initValue,
  titleError,
  detailError,
  expectError,
  tagsError,
  initError
} = AskSlice.actions;
