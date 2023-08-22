import axios from 'axios';
import { useSelector, useDispatch } from 'react-redux';
import { answerCreate } from '../../../redux/createSlice/AnswerSlice';

export default function YourAnswer() {
  const questionId = useSelector((state) => state.detail.value.questionId);
  const token = useSelector((state) => state.logininfo.token);
  const answerCreateContent = useSelector((state) => state.answerCRUD.value);
  const dispatch = useDispatch();



  const handleCreateAnswer = () => {
    return axios
      .post(
        `http://ec2-3-39-228-109.ap-northeast-2.compute.amazonaws.com/questions/${questionId}/answers`, answerCreateContent,
        {
          headers: {
            Authorization: token,
          },
        }
      )
      .then((res) => {
        alert('답변이 등록되었습니다.');
        console.log(res);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <>
      {/* Answer 입력 창 */}
      <div className="mt-8 border-t-2">
        <h1 className="text-3xl my-6">Your Answer</h1>
        <input
          onChange={(e) => dispatch(answerCreate(e.target.value))}
          className="w-[1000px] h-[250px] mt-4 border-2 border-gray-300 rounded-md"
          type="text"
          placeholder="내용 입력"
        />
        <button
          onClick={handleCreateAnswer}
          className="w-[150px] h-[50px] mt-12 mb-12 rounded-2xl bg-[#2196F3] text-[white]"
        >
          Reply Answer
        </button>
      </div>
    </>
  );
}
