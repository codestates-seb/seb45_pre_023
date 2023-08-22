import axios from 'axios';
import Buttons from '../../components/Members/Buttons';
import MemberInfo from '../../components/Members/MemberInfo';
import LeftSidebar from '../../components/SideBar/LeftSidebar';
import MemberSidebar from '../../components/SideBar/MemberSidebar';
import RightSidebar from '../../components/SideBar/RightSidebar';
import { useSelector, useDispatch } from 'react-redux';
import { useEffect} from 'react';
import { myinfo } from '../../redux/createSlice/memberSlice';

export default function MemberMain() {
  const memberId = useSelector((state) => state.logininfo.myid); //member Id값 불러오기
  const memberAnswer = useSelector((state) => state.memberinfo?.value.answer);
  const memberQuestionList = useSelector((state)=>state.memberinfo?.value.question)
  const dispatch = useDispatch();
  useEffect(() => {
    axios
      .get(
        `http://ec2-3-39-228-109.ap-northeast-2.compute.amazonaws.com/members/${memberId}`
      )
      .then((res) => {
        dispatch(myinfo(res.data.data));
        console.log(res.data.data);
      })
      .catch((err) => console.log(err));
  }, []);

  return (
    <div className="flex">
      <LeftSidebar />
      <div className="flex flex-col min-h-[70rem]">
        <div className="flex flex-col p-6 h-152">
          <MemberInfo />
          <Buttons />
          <div className="flex">
            <MemberSidebar />
            <div className="flex flex-col pl-[9rem] h-min justify-between">
              <div>
                <div className=" font-bold text-2xl">Answers</div>
                <div className=" border-gray-600 border-[1px] w-[40rem] min-h-[5rem] mt-5">
                  {memberAnswer ? (
                    memberAnswer.map((item) => {
                      return (
                        <div key={item.answerId}>
                          {item.content ? item.content : '댓글'} -
                          {item.questionTitle ? item.questionTitle : ''}
                        </div>
                      );
                    })
                  ) : (
                    <div>Loading</div>
                  )}
                </div>
              </div>
              <div>
                <div className=" font-bold text-2xl">Questions</div>
                <div className=" border-gray-600 border-[1px] w-[40rem] min-h-[5rem] mt-5">
                  {memberQuestionList ? (
                    memberQuestionList.map((item) => {
                      return <div key={item.id}>{item.title}</div>;
                    })
                  ) : (
                    <div>Loading</div>
                  )}
                </div>
              </div>
              <div>
                <div className=" font-bold text-2xl">Followed posts</div>
                <div className=" border-gray-600 border-[1px] w-[40rem] min-h-[5rem] mt-5">
                  미구현 입니다.
                </div>
              </div>
              <div>
                <div className=" font-bold text-2xl">Votes cast</div>
                <div className=" border-gray-600 border-[1px] w-[40rem] min-h-[5rem] mt-5">
                  미구현 입니다.
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <RightSidebar />
    </div>
  );
}
