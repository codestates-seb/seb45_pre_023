import { useState } from 'react';
import axios from 'axios';
import { useSelector } from 'react-redux';

export default function Reply({ answerId }) {
  const token = useSelector((state) => state.logininfo.token);

  // 대댓글 입력창 상태변경
  const [isInputVisible, setInputVisible] = useState(false);
  const [inputValue, setInputValue] = useState(''); // 입력된 내용을 저장할 상태 변수
  const [isReplyCommentVisible, setReplyCommentVisible] = useState(true); // Reply Comment 버튼 상태

  const toggleInputVisibility = () => {
    setInputVisible(!isInputVisible);
    setReplyCommentVisible(!isReplyCommentVisible); // Reply Comment 버튼 상태를 토글
  };

  const handleInputChange = (e) => {
    setInputValue(e.target.value);
  };

  // 선택한 답변 아래에 댓글을 추가
  const handleSubmit = () => {
    return axios
      .post(
        `http://ec2-3-39-228-109.ap-northeast-2.compute.amazonaws.com/answers/${answerId}/replies`,
        { content: inputValue },
        {
          headers: {
            Authorization: token,
          },
        }
      )
      .then((res) => {
        alert('댓글이 달렸습니다.');
        console.log(res);

        // 입력 내용 초기화
        setInputValue('');

        // Reply Comment 버튼을 다시 보이게 하고 입력창 닫기
        setReplyCommentVisible(true);
        setInputVisible(false);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <>
      {/* 답변 단 사람 */}
      <div className="w-[1000px] h-[200px] flex flex-col ">
        {/* 대댓글 입력창 */}
        <input
          onChange={handleInputChange}
          value={inputValue}
          className="w-[1000px] h-[200px] mt-4 border-2 border-gray-300 rounded-md"
          type="text"
          placeholder="Add a comment..."
        />
        <div className="mt-4 mb-8">
          {isInputVisible ? (
            <div>
              <button
                onClick={handleSubmit}
                className="w-[100px] h-[30px] mt-4 rounded-2xl bg-[#bac2c9] text-[white]"
              >
                Submit
              </button>
              <button
                onClick={toggleInputVisibility}
                className="w-[100px] h-[30px] mt-4 ml-4 rounded-2xl bg-[#bac2c9] text-[white] hover:text-[#afdcf7] text-sm"
              >
                Cancel
              </button>
            </div>
          ) : (
            <button
              onClick={toggleInputVisibility}
              className="w-[150px] mt-4 rounded-2xl bg-[#bac2c9] text-[white]"
            >
              Reply Comment
            </button>
          )}
        </div>
      </div>
    </>
  );
}
