import { createSlice } from '@reduxjs/toolkit';


const questionSlice = createSlice({
    name: 'questions',
    initialState: {value : []},
    reducers: {
      setQuestions: (state, action) => {
        state.value = action.payload;
      },
    },
  });

export default questionSlice;


export const { setQuestions } = questionSlice.actions;

