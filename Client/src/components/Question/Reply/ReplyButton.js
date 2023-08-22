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
        `http://ec2-43-201-249-199.ap-northeast-2.compute.amazonaws.com/replies/${replyId}`, 
        {content : replyContent},
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
    return axios
      .delete(
        `http://ec2-43-201-249-199.ap-northeast-2.compute.amazonaws.com/replies/${replyId}`,
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
        className="text-[#e0e0e0] hover:text-[black]"
      >
        {replyEditMode ? 'Save' : 'Edit'}
      </button>
      <button onClick={handleReplyDelete}>Delete</button>
    </div>
  );
}
