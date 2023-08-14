import { legacy_createStore as createStore, combineReducers, applyMiddleware } from 'redux';
import {} from './reducers';
import thunk from 'redux-thunk';
  
export const store = createStore(combineReducers({}), applyMiddleware(thunk));
  