import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { RouteConst } from './Interface/RouteConst';
import Main from './pages/Basic/Main';
import Login from './pages/Basic/Login';
import SignUp from './pages/Basic/SignUp';
import Question from './pages/Basic/Question';
import Footer from './components/Footer';
import Header from './components/Header/Header';
import MemberProfile from './pages/Member/Profile';
import MemberEdit from './pages/Member/Settings/Edit';
import MemberDelete from './pages/Member/Settings/Delete';
import MemberMain from './pages/Member/memberMain';
import LeftSidebar from './components/SideBar/LeftSidebar';
import RightSidebar from './components/SideBar/RightSidebar';

function App() {
  return (
    <BrowserRouter>
      <Header />
      <div className="flex ml-40 h-[80rem]">
        <LeftSidebar />
        <Routes>
          <Route path={RouteConst.Login} element={<Login />} />
          <Route path={RouteConst.Main} element={<Main />} />
          <Route path={RouteConst.SignUp} element={<SignUp />} />
          <Route path={RouteConst.Question} element={<Question />} />
          <Route path={RouteConst.memberMain} element={<MemberMain />} />
          <Route path={RouteConst.memberProfile} element={<MemberProfile />} />
          <Route path={RouteConst.memberEdit} element={<MemberEdit />} />
          <Route path={RouteConst.memberDelete} element={<MemberDelete />} />
        </Routes>
        <RightSidebar />
      </div>
      <Footer />
    </BrowserRouter>
  );
}

export default App;
