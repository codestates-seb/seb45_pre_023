import { createSlice } from '@reduxjs/toolkit';


const questionDetailSlice = createSlice({
    name: 'questionDetail',
    initialState: {value : []},
    reducers: {
      detail: (state, action) => {
        state.value = action.payload;
      },
    },
  });

export default questionDetailSlice;


export const { detail } = questionDetailSlice.actions;

