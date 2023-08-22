import { useSelector } from 'react-redux';
import Buttons from '../../components/Members/Buttons';
import MemberInfo from '../../components/Members/MemberInfo';
import LeftSidebar from '../../components/SideBar/LeftSidebar';
import RightSidebar from '../../components/SideBar/RightSidebar';

export default function MemberProfile() {
  const question = useSelector((state) => state.memberinfo.value.question);
  const myintro = useSelector((state) => state.memberinfo.value.myIntro);
  const account = useSelector((state) => state.memberinfo.value.account);
  const answer = useSelector((state) => state.memberinfo.value.answer);
  return (
    <div className="flex min-h-[70rem] ">
      <LeftSidebar />
      <div className="flex flex-col p-6">
        <MemberInfo />
        <Buttons />
        <div className="flex justify-between">
          <div>
            <div className=" text-2xl py-3">Stats</div>
            <div className="flex border-[1px] rounded-lg border-gray-400 w-[15rem] h-[8rem] justify-around">
              <div className="flex flex-col justify-around">
                <div>1</div>
                <div>reputation</div>
                <div>{answer.length}</div>
                <div>answers</div>
              </div>
              <div className="flex flex-col justify-around">
                <div>0</div>
                <div>reached</div>
                <div>{question.length}</div>
                <div>questions</div>
              </div>
            </div>
          </div>
          <div>
            <div>
              <div className=" text-2xl py-3">Stats</div>
              <div className="flex border-[1px] rounded-lg border-gray-400 w-[37rem] h-[8rem] justify-around">
                {myintro}
              </div>
            </div>
            <div>
              <div className=" text-2xl py-3">Acounts</div>
              <div className="flex border-[1px] rounded-lg border-gray-400 w-[37rem] h-[8rem] justify-around">
                {account}
              </div>
            </div>
            <div>
              <div className=" text-2xl py-3">Posts</div>
              <div className="flex flex-col border-[1px] rounded-lg border-gray-400 w-[37rem] min-h-[8rem] justify-around py-1">
                {question.map((item) => {
                  return <div>{item.title}</div>;
                })}
              </div>
            </div>
          </div>
        </div>
      </div>
      <RightSidebar />
    </div>
  );
}
