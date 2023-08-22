import VoteButton from '../VoteButtons';
import AnswerShareBtn from './AnswerShareBtn';
import Reply from '../Reply/Reply';
import { useSelector, useDispatch } from 'react-redux';
import { useState } from 'react';
import { answerEdit } from '../../../redux/createSlice/AnswerSlice';
import { answerAdd } from '../../../redux/createSlice/QuestionDetailSlice';
import ReplyLists from '../Reply/ReplyLists';
import getTimeDifference from '../Time';

export default function AnswerList({ el, index }) {
  const [answerEditMode, setAnswerEditMode] = useState(false);

  const inputTime = el.createdDate;
  const hoursAgo = getTimeDifference(inputTime);

  const answerObjectData = useSelector((state) => state.detail.answer);
  const answerData = Object.values(answerObjectData);
  console.log(answerData);

  const dispatch = useDispatch();

  const handleEditAnswer = (e) => {
    dispatch(answerEdit(e.target.value));

    const editData = answerData.map((editAnswer, idx) => {
      if (idx === index) {
        editAnswer = { ...editAnswer, content: e.target.value };
        return editAnswer;
      } else {
        return editAnswer;
      }
    });
    console.log(editData);
    dispatch(answerAdd(editData));
  };

  return (
    <>
      <li className="flex border-t-2 mt-8">
        <VoteButton />
        <div>
          {!answerEditMode ? (
            <div className="h-[250px] text-xl ml-4 my-8">{el.content}</div>
          ) : (
            <input
              onChange={(e) => handleEditAnswer(e)}
              value={el.content}
              className="h-[250px] text-xl ml-4 my-8"
            />
          )}

          {/* Share, Edit Allow 버튼 */}
          <div className="w-full flex justify-between mt-4 border-b-2">
            <AnswerShareBtn
              answerEditMode={answerEditMode}
              setAnswerEditMode={setAnswerEditMode}
              answerId={el.answerId}
              memberId={el.member.memberId}
            />
            <div className="w-[150px] h-[80px] mr-20 my-12">
              <div className="flex">
                <img
                  src="https://i.pinimg.com/564x/40/dc/af/40dcaf3511a13efabcfc4d741a632324.jpg"
                  alt="짱구 이미지"
                  className="w-[20px] h-[20px] mr-4"
                />
                <p className="text-[#949292]">{el.member.nickname}</p>
              </div>
              <p className="text-[#949292] text-sm mt-2">{hoursAgo} hours ago</p>
            </div>
          </div>
          {/* 대댓글 */}

          <Reply answerId={el.answerId} />

          <ul className="flex flex-col">
            {el.reply.replies.map((reply, idx) => (
              <ReplyLists answerIdx={index} reply={reply} key={idx} idx={idx} />
            ))}
          </ul>
        </div>
      </li>
    </>
  );
}
