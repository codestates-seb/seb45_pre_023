import { configureStore } from '@reduxjs/toolkit';
import tipboxSlice from './createSlice/TipboxSlice';
import OAuthSlice from './createSlice/OAuthSlice';
import AskSlice from './createSlice/AskSlice';
import LoginInfoSlice from './createSlice/LoginInfoSlice';
import ErrMsgSlice from './createSlice/ErrMsgSlice';
import SignUpInfoSlice from './createSlice/SignUpInfoSlice';
import questionSlice from './createSlice/QuestionSlice';
import questionDetailSlice from './createSlice/QuestionDetailSlice';
import { combineReducers } from '@reduxjs/toolkit';
import storage from 'redux-persist/lib/storage';
import { persistReducer } from 'redux-persist';
import MemberSlice from './createSlice/memberSlice';

const reducers = combineReducers({
  tipbox: tipboxSlice.reducer,
  oauth: OAuthSlice.reducer,
  ask: AskSlice.reducer,
  logininfo: LoginInfoSlice.reducer,
  signupinfo: SignUpInfoSlice.reducer,
  errmsg: ErrMsgSlice.reducer,
  questions:questionSlice.reducer,
  detail:questionDetailSlice.reducer,
  memberinfo:MemberSlice.reducer,
});

const persistConfig = {
  key: 'root',
  storage,
  whitelist: ['oauth', 'logininfo','memberinfo'],
};

const persistedReducer = persistReducer(persistConfig, reducers);

const store = configureStore({
  reducer: persistedReducer

});

export default store;
