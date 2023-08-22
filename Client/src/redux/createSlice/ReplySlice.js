import { createSlice } from '@reduxjs/toolkit';

const replySlice = createSlice({
  name: 'replyCRUD',
  initialState: { value : { content: "" }},
  reducers: {
    replyCreate: (state, action) => {
      state.value.content = action.payload;
    },
    replyEdit : (state, action) => {
      state.replyEditContent = action.payload;
    },
  },
});

export default replySlice;

export const { replyCreate, replyEdit } = replySlice.actions;


