import { configureStore } from '@reduxjs/toolkit';
import tipboxSlice from './createSlice/TipboxSlice';
import OAuthSlice from './createSlice/OAuthSlice';
import AskSlice from './createSlice/AskSlice';
import LoginInfoSlice from './createSlice/LoginInfoSlice';
import ErrMsgSlice from './createSlice/ErrMsgSlice';
import SignUpInfoSlice from './createSlice/SignUpInfoSlice';

import { combineReducers } from '@reduxjs/toolkit';
import storage from 'redux-persist/lib/storage';
import { persistReducer } from 'redux-persist';

const reducers = combineReducers({
  tipbox: tipboxSlice.reducer,
  oauth: OAuthSlice.reducer,
  ask: AskSlice.reducer,
  logininfo: LoginInfoSlice.reducer,
  signupinfo: SignUpInfoSlice.reducer,
  errmsg: ErrMsgSlice.reducer,
});

const persistConfig = {
  key: 'root',
  storage,
  whitelist: ['oauth', 'logininfo'],
};

const persistedReducer = persistReducer(persistConfig, reducers);

const store = configureStore({
  reducer: persistedReducer,
});

export default store;
