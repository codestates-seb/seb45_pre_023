import { configureStore } from '@reduxjs/toolkit';
import tipboxSlice from './createSlice/tipboxSlice';
import OAuthSlice from './createSlice/oauthSlice';
import AskSlice from './createSlice/askSlice';
import questionSlice from './createSlice/QuestionSlice';
import questionDetailSlice from './createSlice/QuestionDetailSlice';

const store = configureStore({
  reducer: {
    tipbox: tipboxSlice.reducer,
    oauth: OAuthSlice.reducer,
    ask: AskSlice.reducer,
    questions: questionSlice.reducer,
    detail: questionDetailSlice.reducer,
  },
});

export default store;