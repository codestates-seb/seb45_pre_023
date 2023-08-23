import { useEffect } from 'react';
import QuestionList from '../../components/Main/QuestionLists';
import LeftSidebar from '../../components/SideBar/LeftSidebar';
import RightSidebar from '../../components/SideBar/RightSidebar';
import { useNavigate } from 'react-router-dom';
import { RouteConst } from '../../Interface/RouteConst';

export default function Main() {
  const navigate = useNavigate();

  useEffect(() => {
    window.history.pushState(null, '', '');
    window.onpopstate = () => {
      navigate(RouteConst.Main);
    };
  }, []);
  
  return (
    <div className="flex">
      <LeftSidebar />
      <QuestionList />
      <RightSidebar />
    </div>
  );
}
