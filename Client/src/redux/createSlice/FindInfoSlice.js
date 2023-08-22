import { createSlice } from '@reduxjs/toolkit';

const FindInfoSlice = createSlice({
  name: 'FindInfo',
  initialState: {
    email: '',
    password: '',
    code: '',
    mode: { modal: false, entercode: false, enterpw: false },
    msg: ""
  },
  reducers: {
    findemail: (state, action) => {
      state.email = action.payload;
    },
    findpassword: (state, action) => {
      state.password = action.payload;
    },
    findcode: (state, action) => {
      state.code = action.payload;
    },
    findmode: (state, action) => {
      state.mode.modal = action.payload.modal;
      state.mode.entercode = action.payload.entercode;
      state.mode.enterpw = action.payload.enterpw;
    },
    findmodemodal: (state, action) => {
      state.mode.modal = action.payload;
    },
    findmodeentercode: (state, action) => {
      state.mode.entercode = action.payload;
    },
    findmodeenterpw: (state, action) => {
      state.mode.enterpw = action.payload;
    },
    findmsg: (state, action) => {
      state.msg = action.payload;
    },
    findinit: (state) => {
      state.email = '';
      state.password = '';
      state.code = '';
      state.mode = { modal: false, entercode: false, enterpw: false };
      state.msg = '';
    },
  },
});

export default FindInfoSlice;
export const {
  findemail,
  findpassword,
  findcode,
  findmodemodal,
  findmodeentercode,
  findmodeenterpw,
  findmsg,
  findinit,
} = FindInfoSlice.actions;
