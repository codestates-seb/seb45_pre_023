import { createSlice } from '@reduxjs/toolkit';

const questionSlice = createSlice({
  name: 'questions',

  initialState: { value: [], pageInfo: { totalSize : 0 }, queryParams:{ page : 1, sort:"" } },
  reducers: {
    setQuestions: (state, action) => {
      state.value = action.payload;
    },
    pageInfo: (state, action) => {
      state.pageInfo = action.payload;
    },
    filter: (state, action) => {
      state.queryParams = {...state.queryParams, ...action.payload};
    },
  },
});

export default questionSlice;


export const { setQuestions, pageInfo, filter } = questionSlice.actions;

