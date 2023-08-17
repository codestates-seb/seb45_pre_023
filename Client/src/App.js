import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { RouteConst } from './Interface/RouteConst';
import Main from './pages/Basic/Main';
import Login from './pages/Basic/Login';
import SignUp from './pages/Basic/SignUp';
import Ask from './pages/Basic/Ask';
import Question from './pages/Basic/Question';
import Footer from './components/Footer';
import Header from './components/Header/Header';
import MemberProfile from './pages/Member/Profile';
import MemberEdit from './pages/Member/Settings/Edit';
import MemberDelete from './pages/Member/Settings/Delete';
import MemberMain from './pages/Member/memberMain';
import LeftSidebar from './components/SideBar/LeftSidebar';
import RightSidebar from './components/SideBar/RightSidebar';
import { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';


import axios from 'axios';
import { oauthtoken } from './redux/createSlice/OAuthSlice';

function App() {
  const dispatch = useDispatch();
  const provider = useSelector((state) => state.oauth.provider);

  const getAccessToken = async (authorizationCode) => {
    return axios
      .get(
        `http://ec2-43-201-249-199.ap-northeast-2.compute.amazonaws.com/auth/oauth?provider=${provider}&code=${authorizationCode}`
      )
      .then((res) => {
        dispatch(oauthtoken(res.headers.Authorization));
      })
      .catch((err) => {
        console.log(err.response.data);
      });
  };

  useEffect(() => {
    const url = new URL(window.location.href);
    const authorizationCode = url.searchParams.get('code');
    if (authorizationCode) {
      console.log(authorizationCode);
      getAccessToken(authorizationCode); // getAccessToken 함수 호출로 바꾸기
    }
  }, []);

  return (
    <BrowserRouter>
      <Header />
      {/* <div className="flex ml-40 h-[80rem]"> */}
      {/* <LeftSidebar /> */}
      <Routes>
        <Route path={RouteConst.Login} element={<Login />} />
        <Route path={RouteConst.SignUp} element={<SignUp />} />
        <Route path={RouteConst.Main} element={<Main />} />
        <Route path={RouteConst.Question} element={<Question />} />
        <Route path={RouteConst.Ask} element={<Ask />} />
        <Route path={RouteConst.memberMain} element={<MemberMain />} />
        <Route path={RouteConst.memberProfile} element={<MemberProfile />} />
        <Route path={RouteConst.memberEdit} element={<MemberEdit />} />
        <Route path={RouteConst.memberDelete} element={<MemberDelete />} />
      </Routes>
      {/* <RightSidebar /> */}
      {/* </div> */}
      <Footer />
    </BrowserRouter>
  );
}

export default App;
