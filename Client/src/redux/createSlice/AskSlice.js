import { createSlice } from '@reduxjs/toolkit';

const AskSlice = createSlice({
  name: 'Ask',
  initialState: {
    value: {
      title: '',
      detail: '',
      expect: '',
      tagNames: [],
    },
    errmsg: {
      title: '',
      detail: '',
      expect: '',
      tags: '',
    },
    tagsdata: { tagslist: [], mode: false },
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
      state.value.tagNames = [...action.payload];
    },
    addtags: (state, action) => {
      state.value.tagNames = [...state.value.tagNames, action.payload];
    },
    removetags: (state) => {
      state.value.tagNames = state.value.tagNames.filter(
        (el, idx) => idx !== state.value.tagNames.length - 1
      );
    },
    initValue: (state) => {
      state.value = { title: '', detail: '', expect: '', tagNames: [] };
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
    initError: (state) => {
      state.errmsg.title = '';
      state.errmsg.detail = '';
      state.errmsg.expect = '';
      state.errmsg.tags = '';
    },
    setTagList: (state, action) => {
      state.tagsdata.tagslist = action.payload;
    },
    setTagMode: (state, action) => {
      state.tagsdata.mode = action.payload;
    },
  },
});

export default AskSlice;
export const {
  title,
  detail,
  expect,
  tags,
  addtags,
  removetags,
  initValue,
  titleError,
  detailError,
  expectError,
  tagsError,
  initError,
  setTagList,
  setTagMode,
} = AskSlice.actions;
