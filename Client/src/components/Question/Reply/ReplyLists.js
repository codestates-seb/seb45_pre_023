import { useDispatch } from 'react-redux';
import { answerAdd } from '../../../redux/createSlice/QuestionDetailSlice';
import { useSelector } from 'react-redux';
import { useState } from 'react';
import ReplyButton from './ReplyButton';

export default function ReplyLists({ reply, answerIdx, idx }) {
  const answerObjectData = useSelector((state) => state.detail.answer);
  const answerData = Object.values(answerObjectData);

  const [replyEditMode, setReplyEditMode] = useState(false);

  const dispatch = useDispatch();

  const handleEditReply = (e) => {
    const editData = answerData.map((answerObj, a) => {
      if (a === answerIdx) {
        const updatedReplies = answerObj.reply.replies.map(
          (replyObj, replyIdx) => {
            if (idx === replyIdx) {
              return (replyObj = { ...replyObj, content: e.target.value });
            } else {
              return replyObj;
            }
          }
        );
        return {
          ...answerObj,
          reply: { ...answerObj.reply, replies: updatedReplies },
        };
      } else {
        return answerObj;
      }
    });
    dispatch(answerAdd(editData));
  };

  return (
    <li>
      {!replyEditMode ? (
        <div className="h-[50px] text-xl ml-4 my-8">{reply.content}</div>
      ) : (
        <input
          value={reply.content}
          onChange={(e) => handleEditReply(e)}
          className="h-[50px] text-xl ml-4 my-8"
        />
      )}
      <ReplyButton
        replyEditMode={replyEditMode}
        setReplyEditMode={setReplyEditMode}
        replyContent={reply.content}
        replyId={reply.replyId}
      />
    </li>
  );
}
