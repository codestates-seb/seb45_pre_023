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
import { useDispatch, useSelector } from 'react-redux';
import { useEffect } from 'react';

import axios from 'axios';
import { logintoken } from './redux/createSlice/LoginInfoSlice';

function App() {
  const dispatch = useDispatch();
  const provider = useSelector((state) => state.oauth.provider);

  const getAccessToken = (authorizationCode) => {
    return axios
      .get(
        `http://ec2-43-201-249-199.ap-northeast-2.compute.amazonaws.com/auth/oauth?provider=${provider}&code=${authorizationCode}`
      )
      .then((res) => {
        dispatch(logintoken(res.headers.authorization));
        alert('로그인 되었습니다.');
        window.location.assign(
          'http://sixman-front-s3.s3-website.ap-northeast-2.amazonaws.com/questions'
        );
      })
      .catch((err) => {
        alert(`로그인에 실패했습니다. ${err.response.data.code}`);
      });
  };

  useEffect(() => {
    const url = new URL(window.location.href);
    const authorizationCode = url.searchParams.get('code');
    if (authorizationCode) {
      getAccessToken(authorizationCode);
    }
  }, []);

  return (
    <BrowserRouter>
      <Header />
      <Routes>
        <Route path={RouteConst.Login} element={<Login />} />
        <Route path={RouteConst.SignUp} element={<SignUp />} />
        <Route path={RouteConst.Ask} element={<Ask />} />
        <Route path={RouteConst.Main} element={<Main />} />
        <Route path={RouteConst.Question} element={<Question />} />
        <Route path={RouteConst.memberMain} element={<MemberMain />} />
        <Route path={RouteConst.memberProfile} element={<MemberProfile />} />
        <Route path={RouteConst.memberEdit} element={<MemberEdit />} />
        <Route path={RouteConst.memberDelete} element={<MemberDelete />} />
      </Routes>
      <Footer />
    </BrowserRouter>
  );
}

export default App;
