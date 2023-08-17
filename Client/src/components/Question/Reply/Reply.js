import { useState } from 'react';

export default function Reply() {
  // 대댓글 입력창 상태변경
  const [isInputVisible, setInputVisible] = useState(false);
  const toggleInputVisibility = () => {
    setInputVisible(!isInputVisible);
  };

  return (
    <>
      {/* 답변 단 사람 */}
      <div className="w-[1000px] h-[200px] flex justify-between items-center ">
        {/* 대댓글 */}
        <div className="mt-20">
          {isInputVisible ? (
            <div>
              <input
                className="w-[1000px] h-[100px] mt-4 border-2 border-gray-300 rounded-md"
                type="text"
                placeholder="Add a comment..."
              />
              <button className="w-[100px] mt-4 ml-6 rounded-2xl bg-[#bac2c9] text-[white]">
                Submit
              </button>
              <button
                onClick={toggleInputVisibility}
                className="w-[100px] mt-4 ml-6 rounded-2xl bg-[#bac2c9] text-[white] hover:text-[#afdcf7] text-sm"
              >
                Cancel
              </button>
            </div>
          ) : (
            <button
              onClick={toggleInputVisibility}
              className="ml-6 rounded-full hover:text-[#afdcf7] text-sm"
            >
              Reply Comment
            </button>
          )}
        </div>
      </div>
    </>
  );
}
