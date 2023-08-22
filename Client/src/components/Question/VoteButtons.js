import { useSelector } from 'react-redux';
import axios from 'axios';

export default function VoteButtons() {
  const vote = useSelector((state) => state.detail.vote);
  const questionId = useSelector((state) => state.detail.value.questionId);
  const token = useSelector((state) => state.logininfo.token);


  const handleUpVote = () => {
    return axios.patch(
      `http://ec2-3-39-228-109.ap-northeast-2.compute.amazonaws.com/questions/${questionId}/upvote`,
      {
        headers: {
          Authorization: token,
        },
      }
    );
  };

  const handleDownVote = () => {
    return axios.patch(
      `http://ec2-3-39-228-109.ap-northeast-2.compute.amazonaws.com/questions/${questionId}/downvote`,
      {
        headers: {
          Authorization: token,
        },
      }
    );
  }

  return (
    <>
      {/* 투표수 */}
      <div className="flex flex-col items-center">
        <button
          onClick={handleUpVote}
          className="w-[50px] h-[50px] my-2 border-1 rounded-full"
        >
          ▲
        </button>
        <h2 className="text-2xl">{vote}</h2>
        <button onClick={handleDownVote} className="w-[50px] h-[50px] my-2 border-1 rounded-full">
          ▼
        </button>
        <h2 className="text-2xl">◻</h2>
        <h2 className="text-2xl">Ò</h2>
      </div>
    </>
  );
}
