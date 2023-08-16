import Buttons from '../../components/Members/Buttons';
import MemberInfo from '../../components/Members/MemberInfo';

export default function MemberProfile() {
  return (
    <>
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
                <div>0</div>
                <div>answers</div>
              </div>
              <div className="flex flex-col justify-around">
                <div>0</div>
                <div>reached</div>
                <div>0</div>
                <div>questions</div>
              </div>
            </div>
          </div>
          <div>
            <div>
              <div className=" text-2xl py-3">Stats</div>
              <div className="flex border-[1px] rounded-lg border-gray-400 w-[37rem] h-[8rem] justify-around">
                About 에 들어갈 내용
              </div>
            </div>
            <div>
              <div className=" text-2xl py-3">Acounts</div>
              <div className="flex border-[1px] rounded-lg border-gray-400 w-[37rem] h-[8rem] justify-around">
                깃허브 계정 or 구글계정 or 개인 개발 블로그 등등..
              </div>
            </div>
            <div>
              <div className=" text-2xl py-3">Posts</div>
              <div className="flex border-[1px] rounded-lg border-gray-400 w-[37rem] h-[8rem] justify-around">
               자신이 작성한 질문글
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}
