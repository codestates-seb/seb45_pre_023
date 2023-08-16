import Buttons from '../../components/Members/Buttons';
import MemberInfo from '../../components/Members/MemberInfo';
import MemberSidebar from '../../components/SideBar/MemberSidebar';

export default function MemberMain() {
  return (
    <div className="flex flex-col">
      <div className="flex flex-col p-6 h-152">
        <MemberInfo />
        <Buttons />
        <div className="flex">
          <MemberSidebar />
          <div className="flex flex-col pl-[9rem] h-min justify-between">
            <div>
              <div className=" font-bold text-2xl">Answers</div>
              <div className=" border-gray-600 border-[1px] w-[40rem] min-h-[5rem] mt-5">
                <div>답변 내용들</div>
                <div>답변 내용들</div>
              </div>
            </div>
            <div>
              <div className=" font-bold text-2xl">Questions</div>
              <div className=" border-gray-600 border-[1px] w-[40rem] min-h-[5rem] mt-5">
                답변 내용들
              </div>
            </div>
            <div>
              <div className=" font-bold text-2xl">Followed posts</div>
              <div className=" border-gray-600 border-[1px] w-[40rem] min-h-[5rem] mt-5">
                답변 내용들
              </div>
            </div>
            <div>
              <div className=" font-bold text-2xl">Votes cast</div>
              <div className=" border-gray-600 border-[1px] w-[40rem] min-h-[5rem] mt-5">
                답변 내용들
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
