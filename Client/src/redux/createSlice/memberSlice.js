import { createSlice } from '@reduxjs/toolkit';

const MemberSlice = createSlice({
  name: 'Member', // action 이름
  initialState: { value:{account :[], answer:[], question:[],title:'',profileimage:'',location:'',nickname:'',myIntro:''} }, // 초기값
  reducers: {
    // reducer들 모음
    myinfo: (state, action) => {
      state.value.answer = action.payload.answer.answers // action에서 값을 받으면 action.payload 으로 들어옴
      state.value.question = action.payload.question.questions;
      state.value.account = action.payload.accounts;
      state.value.title = action.payload.title;
      state.value.profileimage = action.payload.profileimage;
      state.value.location = action.payload.location;
      state.value.nickname = action.payload.nickname;
      state.value.myIntro = action.payload.myIntro;
      state.value.image = action.payload.image;
    },
  },
});

export default MemberSlice;
export const {
  myinfo
} = MemberSlice.actions;
