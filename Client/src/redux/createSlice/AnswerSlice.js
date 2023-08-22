import { createSlice } from '@reduxjs/toolkit';

const answerSlice = createSlice({
  name: 'answerCRUD',
  initialState: { value : { content: "" } , answerContent : "" },
  reducers: {
    answerCreate: (state, action) => {
      state.value.content = action.payload;
    },
    answerEdit: (state, action) => {
      state.answerContent = action.payload;
    },
  },
});

export default answerSlice;

export const { answerCreate, answerEdit } = answerSlice.actions;
