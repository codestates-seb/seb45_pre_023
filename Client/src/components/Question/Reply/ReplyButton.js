import axios from 'axios';
import { useSelector } from 'react-redux';

export default function ReplyButton({
  replyEditMode,
  setReplyEditMode,
  replyId,
  replyContent,
}) {
  const token = useSelector((state) => state.logininfo.token);

  const handleEdit = () => {
    return axios
      .patch(
        `http://ec2-3-39-228-109.ap-northeast-2.compute.amazonaws.com/replies/${replyId}`,
        { content: replyContent },
        {
          headers: {
            Authorization: token,
          },
        }
      )
      .then((res) => {
        alert('댓글이 수정되었습니다.');
        console.log(res);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const handleReplyDelete = () => {
    const shouldDelete = window.confirm('정말 삭제하시겠습니까?');
    if (shouldDelete) {
    return axios
      .delete(
        `http://ec2-3-39-228-109.ap-northeast-2.compute.amazonaws.com/replies/${replyId}`,
        {
          headers: {
            Authorization: token,
          },
        }
      )
      .then((res) => {
        console.log(res);
      })
      .catch((err) => {
        console.log(err);
      });
    }
  };

  return (
    <div>
      <button
        onClick={() => {
          setReplyEditMode(!replyEditMode);
          if (replyEditMode) {
            handleEdit();
          }
        }}
        className="w-[55px] text-[#e0e0e0] border-2 rounded-2xl hover:text-[black]"
      >
        {replyEditMode ? 'Save' : 'Edit'}
      </button>
      <button
        onClick={handleReplyDelete}
        className="w-[55px] ml-4 text-[#e0e0e0] border-2 rounded-2xl hover:text-[black]"
      >
        Delete
      </button>
    </div>
  );
}
