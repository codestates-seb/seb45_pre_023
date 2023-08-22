import { createSlice } from '@reduxjs/toolkit';

const questionSlice = createSlice({
  name: 'questions',

  initialState: { value: [], pageInfo: { totalSize : 0 } },
  reducers: {
    setQuestions: (state, action) => {
      state.value = action.payload;
    },
    pageInfo: (state, action) => {
      state.pageInfo = action.payload;
    },
  },
});

export default questionSlice;


export const { setQuestions, pageInfo } = questionSlice.actions;

