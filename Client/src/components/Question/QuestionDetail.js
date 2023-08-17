import VoteButtons from './VoteButtons';
import ShareButton from './ShareButton';
import { useParams } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';
import { useDispatch } from 'react-redux';
import { detail } from '../../redux/createSlice/QuestionDetailSlice';
import FilteringButton from '../Main/FilteringButton';
import data from '../Main/data.json';
import getTimeDifference from './Time';
import Reply from './Reply/Reply';
import YourAnswer from './Answer/YourAnswer';

export default function QuestionDetail() {
  const { questionId } = useParams();
  // const question = useSelector((state) => state.detail.value); // 질문 데이터 가져오기
  const question = data.data;
  console.log(question);
  const dispatch = useDispatch();

  const inputTime = question.createdDate;
  const hoursAgo = getTimeDifference(inputTime);

  useEffect(() => {
    axios
      .get(
        `http://ec2-43-201-249-199.ap-northeast-2.compute.amazonaws.com/questions/${questionId}`
      )
      .then((res) => {
        dispatch(detail(res.data.data));
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
            <div>
              <p className="h-[300px] text-xl ml-4 mt-6">{question.detail}</p>
              <div className="w-[1000px] flex justify-between">
                <div className="mr-4">
                  {question.tags.map((tag, index) => (
                    <button
                      key={index}
                      className="w-[100px] bg-[#afdcf7b4] rounded-full mr-4 text-[#95a6dd] hover:translate-x-1 hover:translate-y-1 leading-2"
                    >
                      {tag.tagName}
                    </button>
                  ))}
                </div>
              </div>

              {/* Share, Edit Allow 버튼 */}
              <div className="flex justify-between">
                <ShareButton />
                <div className="w-[150px] h-[80px] mr-20 bg-[#afdcf7b4]">
                  <p className="text-[#949292]">
                    {question.answer.answers.createdDate}
                  </p>
                  <p className="text-[#949292]">
                    {/* {question.answer.answers.member.nickname} */}
                  </p>
                </div>
              </div>
            </div>
          </div>

          {/* Answer */}
          <div className="w-full mt-20 border-t-2">
            {/* 답변 최상단 */}
            <div className="w-full flex justify-between my-8">
              <h1 className="text-3xl">1 Answer</h1>
              <FilteringButton />
            </div>

            {/* 답변 내용 */}
            <div className="w-full flex">
              <VoteButtons />
              <div className="w-full">
                <p className="h-[250px] text-xl ml-4 my-8">
                  {question.answer.answers.content}1
                </p>

                {/* Share, Edit Allow 버튼 */}
                <div className="w-full flex justify-between mt-12 border-t-2">
                  <ShareButton />
                  <div className="w-[150px] h-[80px] mr-20 mt-12 bg-[#afdcf7b4]">
                    <p className="text-[#949292]">{question.CreatedAt}</p>
                    <p className="ml-4 text-sm text-[#4FC3F7]">
                      {question.AskUser}
                    </p>
                  </div>
                </div>
                {/* 대댓글 */}
                <Reply />
              </div>
            </div>
          </div>
          {/* 댓글 적는 공간 */}
          <YourAnswer />
        </div>
      </div>
    </div>
  );
}
