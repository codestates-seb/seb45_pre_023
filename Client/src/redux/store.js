import { configureStore } from '@reduxjs/toolkit';
import tipboxSlice from './createSlice/tipboxSlice';
import OAuthSlice from './createSlice/oauthSlice';
import AskSlice from './createSlice/askSlice';

const store = configureStore({
  reducer: {
    tipbox: tipboxSlice.reducer,
    oauth: OAuthSlice.reducer,
    ask: AskSlice.reducer,
  },
});

export default store;