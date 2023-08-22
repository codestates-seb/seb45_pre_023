import QuestionDetail from '../../components/Question/QuestionDetail';
import LeftSidebar from '../../components/SideBar/LeftSidebar';
import RightSidebar from '../../components/SideBar/RightSidebar';
// import Answer from "../../components/Question/Answer/Answer"

export default function Question() {
  return (
    <div className='flex'>
      <LeftSidebar />
      <QuestionDetail />
      <RightSidebar />
    </div>
  );
}
