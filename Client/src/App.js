import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { RouteConst } from './Interface/RouteConst';
import Main from './pages/Basic/Main';
import Login from './pages/Basic/Login';
import SignUp from './pages/Basic/SignUp';
import Question from './pages/Basic/Question';
import UserMain from './pages/User/UserMain';
import UserProfile from './pages/User/Profile';
import UserEdit from './pages/User/Settings/Edit';
import UserDelete from './pages/User/Settings/Delete';
import Footer from './components/Footer';
import Header from './components/Header';

function App() {
  return (
    <BrowserRouter>
      <Header />
      <Routes>
        <Route path={RouteConst.Login} element={<Login />} />
        <Route path={RouteConst.Main} element={<Main />} />
        <Route path={RouteConst.SignUp} element={<SignUp />} />
        <Route path={RouteConst.Question} element={<Question />} />
        <Route path={RouteConst.UserMain} element={<UserMain />} />
        <Route path={RouteConst.UserProfile} element={<UserProfile />} />
        <Route path={RouteConst.UserEdit} element={<UserEdit />} />
        <Route path={RouteConst.UserDelete} element={<UserDelete />} />
      </Routes>
      <Footer />.
    </BrowserRouter>
  );
}

export default App;
