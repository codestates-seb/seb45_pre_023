import VoteButtons from './VoteButtons';
import ShareButton from './ShareButton';
import { useParams } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';
import { useDispatch } from 'react-redux';
import {
  detail,
  content,
  member,
  answerAdd,
  replyAdd,
} from '../../redux/createSlice/QuestionDetailSlice';
import Answer from './Answer/Answer';
import getTimeDifference from './Time';

export default function QuestionDetail() {
  const { questionId } = useParams();
  
  const question = useSelector((state) => state.detail.value); // 질문 데이터 가져오기
  const contents = useSelector((state) => state.detail.content); // 컨텐츠 데이터 가져오기
  const nickname = useSelector((state) => state.detail.member);

  const dispatch = useDispatch();

  // 시간 계산
  const inputTime = question.createdDate;
  const hoursAgo = getTimeDifference(inputTime);

  const [editMode, setEditMode] = useState(false); // 편집모드

  useEffect(() => {
    axios
      .get(
        `http://ec2-43-201-249-199.ap-northeast-2.compute.amazonaws.com/questions/${questionId}`
      )
      .then((res) => {
        dispatch(detail(res.data.data));
        dispatch(content(res.data.data.detail));
        dispatch(member(res.data.data.member));
        dispatch(answerAdd(res.data.data.answer.answers));
        dispatch(replyAdd(res.data.data.answer.answers));
        console.log(res.data.data);
      })
      .catch((err) => {
        console.log(err);
      });
  }, []);

  return (
    <div className="w-[1000px] flex flex-col justify-center border-l-2">
      {/* 제목 */}
      <div className="flex justify-between ml-8 mt-12">
        <h2 className=" text-3xl">{question.title}</h2>
        <Link to={`/questions/add`}>
          <button className="w-[150px] h-[50px] text-[white] rounded-2xl bg-[#2196F3] ">
            Asked Question
          </button>
        </Link>
      </div>

      {/* 생성 날짜 */}
      <div className="w-[550px] h-[40px] flex justify-around mt-4 ml-4 text-[#6179c9]">
        <span className="">Asked {hoursAgo} hour ago</span>
        <span className="">Modified {hoursAgo} hour ago</span>
        <span className="">Viewed {question.views}</span>
      </div>

      {/* border 용 div */}
      <div className="w-[1000px] ml-8 border-b-2"></div>

      <div className="w-full flex ml-8">
        {/* 질문 상세내용 창*/}
        <div className="w-full">
          <div className="flex">
            <VoteButtons />
            <div className="flex flex-col">
              {editMode ? (
                <input
                  onChange={(event) => dispatch(content(event.target.value))}
                  value={contents}
                  className="h-[300px] text-xl ml-4 mt-6"
                ></input>
              ) : (
                <p className="h-[300px] ml-4 mt-6 text-xl">{contents}</p>
              )}
              <p className="ml-4 mt-6 text-xl">{question.expect}</p>

              <div className="w-[1000px] flex justify-between">
                <div className="mr-4">
                  {/* {question.tags.map((tag, index) => (
                    <button
                      key={index}
                      className="w-[100px] bg-[#afdcf7b4] rounded-full mr-4 text-[#95a6dd] hover:translate-x-1 hover:translate-y-1 leading-2"
                    >
                      {tag.tagName}
                    </button>
                  ))} */}
                </div>
              </div>

              {/* Share, Edit Allow 버튼 */}
              <div className="flex justify-between">
                <ShareButton editMode={editMode} setEditMode={setEditMode} />
                <div className="w-[150px] h-[80px] mr-20 bg-[#afdcf7b4]">
                  <p className="text-[#949292]">{nickname.nickname}</p>
                  <p className="text-[#949292]">{hoursAgo} hour Ago</p>
                </div>
              </div>
            </div>
          </div>
          <Answer />
        </div>
      </div>
    </div>
  );
}
