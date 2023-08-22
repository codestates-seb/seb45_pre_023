import QuestionList from '../../components/Main/QuestionLists';
import LeftSidebar from '../../components/SideBar/LeftSidebar';
import RightSidebar from '../../components/SideBar/RightSidebar';

export default function Main() {
  return (
    <div className='flex'>  
        <LeftSidebar />
        <QuestionList />
        <RightSidebar />
    </div>
  );
}
