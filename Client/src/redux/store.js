import { configureStore } from '@reduxjs/toolkit';
import { tipboxSlice } from './createSlice/tipboxSlice';

const store = configureStore({
  reducer: {
    tipbox: tipboxSlice.reducer,
  },
});

export default store;

/*
function reducer(state, action) {
    if (action.type === 'next') {
        return {...state, value: state.value + 1}
    }
}

const initialState = { value: 0 };
export const store = createStore(reducer, initialState);
*/
