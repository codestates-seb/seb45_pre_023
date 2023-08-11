import FilteringButton from '../../components/Main/FilteringButton';
import QuestionList from '../../components/Main/QuestionLists';
import LeftSidebar from '../../components/SideBar/LeftSidebar';
import RightSidebar from '../../components/SideBar/RightSidebar';

export default function Main() {
  return (
    <>
      <div className="flex">
        <LeftSidebar />
        <RightSidebar />
        <div>
          <FilteringButton />
          <QuestionList />
        </div>
      </div>
    </>
  );
}
