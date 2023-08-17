import { configureStore } from '@reduxjs/toolkit';
import tipboxSlice from './createSlice/TipboxSlice';
import OAuthSlice from './createSlice/OAuthSlice';
import AskSlice from './createSlice/AskSlice';
import LoginInfoSlice from './createSlice/LoginInfoSlice';
import ErrMsgSlice from './createSlice/ErrMsgSlice';
import SignUpInfoSlice from './createSlice/SignUpInfoSlice';

const store = configureStore({
  reducer: {
    tipbox: tipboxSlice.reducer,
    oauth: OAuthSlice.reducer,
    ask: AskSlice.reducer,
    logininfo: LoginInfoSlice.reducer,
    signupinfo: SignUpInfoSlice.reducer,
    errmsg: ErrMsgSlice.reducer,
  },
});

export default store;
