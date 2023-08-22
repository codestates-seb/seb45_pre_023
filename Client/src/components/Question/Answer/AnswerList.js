import VoteButton from '../VoteButtons';
import AnswerShareBtn from './AnswerShareBtn';
import Reply from '../Reply/Reply';
import { useSelector, useDispatch } from 'react-redux';
import { useState } from 'react';
import { answerEdit } from '../../../redux/createSlice/AnswerSlice';
import { answerAdd } from '../../../redux/createSlice/QuestionDetailSlice';
import ReplyLists from '../Reply/ReplyLists';

export default function AnswerList({ el, index }) {
  const [answerEditMode, setAnswerEditMode] = useState(false);

  const answerObjectData = useSelector((state) => state.detail.answer);
  const answerData = Object.values(answerObjectData);

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
      <li className="flex">
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
            <div className="w-[150px] h-[80px] mr-20 my-12 bg-[#afdcf7b4]">
              <p className="text-[#949292]">{el.createdDate}</p>
              <p className="ml-4 text-sm text-[#4FC3F7]">
                {el.member.nickname}
              </p>
            </div>
          </div>
          {/* 대댓글 */}

          <Reply answerId={el.answerId} />

          <ul className='flex flex-col'>
            {el.reply.replies.map((reply, idx) => (
              <ReplyLists answerIdx={index} reply={reply} key={idx} idx={idx} />
            ))}
          </ul>
          

        </div>
      </li>
    </>
  );
}
