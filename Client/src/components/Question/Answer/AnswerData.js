import React, { useState } from 'react';
import FilteringButton from '../../Main/FilteringButton';
import VoteButtons from '../VoteButtons';
import ShareButton from '../ShareButton';

export default function AnswerData( {el} ) {
  // 답변 상태 변경
  const [isInputVisible, setInputVisible] = useState(false);
  const toggleInputVisibility = () => {
    setInputVisible(!isInputVisible);
  };
  return (
    <>
      <div className="w-full h-[1200px] mt-14 ml-8 border-t-2">
        {/* 답변 최상단 */}
        <div className="w-full flex justify-between my-8">
          <h1 className="text-3xl ml-8">1 Answer</h1>
          <FilteringButton />
        </div>

        {/* 답변 내용 */}
        <div className="w-full flex">
          <VoteButtons />
          <div className="w-full">
            <p className="h-[150px] text-xl ml-4 mt-6">
              {el.content}
            </p>
            {/* 답변 단 사람 */}
            <div className="w-[1000px] flex justify-between items-center ">
              <ShareButton />
              <div className="w-[150px] h-[80px] mr-20 bg-[#afdcf7b4]">
                <p className="text-[#949292]">{el.reply.createDate}</p>
                <p className="text-[#949292]">{el.member.nickname}</p>
              </div>
            </div>
          </div>
        </div>

        {/* 대댓글 */}
        <div className="mt-8">
          <button
            onClick={toggleInputVisibility}
            className="ml-6 rounded-full hover:text-[#afdcf7] text-sm"
          >
            Reply Comment
          </button>

          {isInputVisible && (
            <div>
              <input
                className="w-[1000px] h-[100px] ml-6 mt-4 border-2 border-gray-300 rounded-md"
                type="text"
                placeholder="Add a comment..."
              />
              <button className="w-[100px] mt-4 ml-6 rounded-2xl bg-[#bac2c9] text-[white]">
                Submit
              </button>
            </div>
          )}
        </div>
      </div>
    </>
  );
}
