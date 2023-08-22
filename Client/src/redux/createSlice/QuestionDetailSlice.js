import { createSlice } from '@reduxjs/toolkit';

const questionDetailSlice = createSlice({
  name: 'questionDetail',
  
  initialState: {
    value: [],
    content: '',
    member: '',
    answer: {},
    reply: [],
    vote: 0,
  },
  reducers: {
    detail: (state, action) => {
      state.value = action.payload;
    },
    content: (state, action) => {
      state.content = action.payload;
    },
    member: (state, action) => {
      state.member = action.payload;
    },
    vote: (state, action) => {
      state.vote = action.payload;
    },
    answerAdd: (state, action) => {
      state.answer = action.payload;
    },
    replyAdd: (state, action) => {
      state.reply = action.payload;
    },
  },
});

export default questionDetailSlice;


export const { detail, content, member, vote, answerAdd, replyAdd } =
  questionDetailSlice.actions;
