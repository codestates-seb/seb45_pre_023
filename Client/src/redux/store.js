import { configureStore } from '@reduxjs/toolkit';
import tipboxSlice from './createSlice/tipboxSlice';
import OAuthSlice from './createSlice/oauthSlice';
import AskSlice from './createSlice/askSlice';
import LoginInfoSlice from './createSlice/LoginInfoSlice';
import ErrMsgSlice from './createSlice/ErrMsgSlice';
import SignUpInfoSlice from './createSlice/SignUpInfoSlice';
import questionSlice from './createSlice/QuestionSlice';
import questionDetailSlice from './createSlice/QuestionDetailSlice';
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
  reducer: {
    tipbox: tipboxSlice.reducer,
    oauth: OAuthSlice.reducer,
    ask: AskSlice.reducer,
    questions: questionSlice.reducer,
    detail: questionDetailSlice.reducer,
    reducer: persistedReducer,
  },

});

export default store;
